package com.glaring.colourful.bully.base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public final class XFile {
    private final File mFile;
    private boolean bLock;
    private final LinkedList<Element> mLinked = new LinkedList<>();
    private final Set<Element> mAll = new HashSet<>();

    public enum OPT {
        ADD,
        REMOVE,
        REPLACE,
    }

    private static class Element {
        public OPT mOpt;
        public int nNumber;
        public String mSource;
        public String mTarget;

        public Element(OPT opt, int line, String content) {
            this.mOpt = opt;
            this.nNumber = line;
            this.mTarget = content;
        }

        public Element(OPT opt, int line, String source, String target) {
            this.mOpt = opt;
            this.nNumber = line;
            this.mSource = source;
            this.mTarget = target;
        }
    }

    public XFile(String file) {
        this(new File(file));
    }

    public XFile(File file) {
        this.mFile = file;
    }

    public XFile add(String content) {
        checkLock();
        mLinked.add(new Element(OPT.ADD, Integer.MAX_VALUE, content));
        return this;
    }

    public XFile set(int line, String content) {
        checkLock();
        if (line < 0) {
            line = Integer.MAX_VALUE;
        }
        mLinked.add(new Element(OPT.ADD, line, content));
        return this;
    }

    public XFile remove(int line) {
        checkLock();
        mLinked.add(new Element(OPT.REMOVE, line, null));
        return this;
    }

    public XFile replace(int line, String content) {
        checkLock();
        mLinked.add(new Element(OPT.REPLACE, line, content));
        return this;
    }

    public XFile replace(String source, String target) {
        checkLock();
        mAll.add(new Element(OPT.REPLACE, -1, source, target));
        return this;
    }

    private void checkLock() {
        if (bLock) {
            throw new RuntimeException(new IOException("file is opt!"));
        }
    }

    private void sortElement() {
        Collections.sort(mLinked, new Comparator<Element>() {
            @Override
            public int compare(Element element, Element t1) {
                return element.nNumber - t1.nNumber;
            }
        });
    }

    private static final String CRLN = "\n";

    /**
     * @param writer
     * @param line
     * @param text
     * @param element
     * @throws IOException
     * @return: 1:next line; 2:next element; 3: 1 && 2
     */
    private static int processText(Writer writer, int line, String text, Element element) throws IOException {
        if (element != null) {
            if (text == null) {
                if (element.mOpt == OPT.ADD) {
                    if (element.nNumber == Integer.MAX_VALUE || line == element.nNumber) {
                        writer.write(element.mTarget);
                        writer.write(CRLN);
                    } else {
                        writer.write(CRLN);
                        return 1;
                    }
                }
            } else if (line < element.nNumber) {
                //save source file
                writer.write(text);
                writer.write(CRLN);
                return 1;
            } else if (line > element.nNumber) {
                //
                System.out.println("-------------------------!");
            } else {
                if (element.mOpt == OPT.ADD) {
                    writer.write(element.mTarget);
                    writer.write(CRLN);
                    return 2;
                } else if (element.mOpt == OPT.REMOVE) {

                } else if (element.mOpt == OPT.REPLACE) {
                    writer.write(element.mTarget);
                    writer.write(CRLN);
                } else {

                }
            }
        } else {
            if (text != null) {
                writer.write(text);
                writer.write(CRLN);
            }
            return 1;
        }
        return 3;
    }

    public synchronized void build() throws Exception {
        checkLock();
        bLock = true;

        final Charset def = OS.getCharset();

        final File tmpFile = new File(mFile.getAbsoluteFile() + ".tmp");
        BufferedReader reader = null;
        BufferedWriter writer = null;
        sortElement();
        boolean sucess = false;
        try {
            if (mFile.exists()) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(mFile), def));
            } else {
                reader = new BufferedReader(new StringReader(""));
            }
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmpFile), def));


            String sLine = null;
            Element element = null;

            int next = 3;
            int nLine = -1;
            do {
                if ((next & 1) != 0) {
                    sLine = reader.readLine();
                    ++nLine;

                    if (sLine != null) {
                        for (Element el : mAll) {
                            sLine = sLine.replace(el.mSource, el.mTarget);
                        }
                    }
                }
                if ((next & 2) != 0) {
                    element = mLinked.poll();
                }
                next = processText(writer, nLine, sLine, element);
            } while (sLine != null || element != null);

            sucess = true;
        } finally {
            bLock = false;
            IO.Close(reader);
            IO.Close(writer);

            if (sucess) {
                final File bakFile = new File(mFile.getParentFile(), "." + mFile.getName());
                if (mFile.exists()) {
                    mFile.renameTo(bakFile);
                }
                if (tmpFile.renameTo(mFile)) {
                    bakFile.delete();
                } else {
                    throw new IOException("file opt error!");
                }
            }

        }
    }
}
