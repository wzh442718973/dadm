package com.glaring.colourful.bully.ad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public final class XZip {

    private final HashMap<String, String> mAdds = new HashMap<>();
    private final HashSet<String> mRemoves = new HashSet<>();

    public XZip add(String name, String addfile) {
        mAdds.put(name, addfile);
        return this;
    }

    public XZip remove(String regex) {
        regex = regex.replace('.', '#');
        regex = regex.replaceAll("#", "\\\\.");
        regex = regex.replace('*', '#');
        regex = regex.replaceAll("#", ".*");
        regex = regex.replace('?', '#');
        regex = regex.replaceAll("#", ".?");
        regex = "^" + regex + "$";

        mRemoves.add(regex);
        return this;
    }

    private void writeZipEntry(ZipOutputStream zipOut, InputStream zipIn, byte[] buff) throws IOException {
        do {
            int rs = zipIn.read(buff);
            if (rs < 0) {
                break;
            } else if (rs > 0) {
                zipOut.write(buff, 0, rs);
                zipOut.flush();
            }
        } while (true);
    }

    private boolean isDelete(ZipEntry entry) {
        final String name = entry.getName();
        for (String regex : mRemoves) {
            if (name.matches(regex)) {
                return true;
            }
        }
        return false;
    }

    public void build(final String infile, final String outfile) throws IOException {
        if (infile == null || outfile == null) {
            throw new NullPointerException("in/out file is null");
        }

        Runnable rename = null;
        final String tmpfile = outfile + ".tmp";
        if (infile.equals(outfile)) {
            rename = new Runnable() {
                @Override
                public void run() {
                    File tmp = new File(tmpfile);
                    File out = new File(outfile);
                    if (tmp.exists()) {
                        out.delete();
                        tmp.renameTo(out);
                    }
                }
            };
        }

        ZipFile zipFile = null;
        ZipOutputStream zipOut = null;
        try {
            byte[] buff = new byte[1024 * 4];
            zipFile = new ZipFile(infile);
            zipOut = new ZipOutputStream(new FileOutputStream(tmpfile));

            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (isDelete(entry)) {
                    continue;
                }
                zipOut.putNextEntry(new ZipEntry(entry.getName()));
                writeZipEntry(zipOut, zipFile.getInputStream(entry), buff);

                zipOut.closeEntry();
            }
            for (String name : mAdds.keySet()) {
                FileInputStream inAdd = null;
                try {
                    inAdd = new FileInputStream(mAdds.get(name));
                    zipOut.putNextEntry(new ZipEntry(name));
                    writeZipEntry(zipOut, inAdd, buff);
                } catch (FileNotFoundException e) {
                    System.out.println(name + " : " + mAdds.get(name) + " >> " + e.getMessage());
                } finally {
                    IO.Close(inAdd);
                }
            }
        } finally {
            if (zipFile != null) {
                zipFile.close();
            }
            if (zipOut != null) {
                zipOut.close();
            }
            if (rename != null) {
                rename.run();
            }
        }

    }

}
