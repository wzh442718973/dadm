package com.glaring.colourful.bully.base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public final class XShell {

    public interface OnShell {
        /**
         * @param shell
         */
        void onRecve(XShell shell);
    }

    protected void onShellRecve() {
        if (mCallBack != null) {
            mCallBack.onRecve(this);
        }
    }

    protected void onShellStart() {
        if (DBG_LOG) System.out.println("xshell.start");
    }

    protected void onShellEnd() {
        if (DBG_LOG) System.out.println("xshell.end");
    }

    /**
     * Console Print
     */
    private boolean mIsPrint;

    /**
     * Input echo
     */
    private boolean mIsEcho;

    public void setPrint(boolean print) {
        mIsPrint = print;
    }

    public void setEcho(boolean echo) {
        mIsEcho = echo;
    }

    private final ArrayList<String> mParameter = new ArrayList<>();
    private final ProcessBuilder mBuilder = new ProcessBuilder(mParameter);
    private final OnShell mCallBack;
    /**
     * Revce Cache
     */
    private LinkedBlockingDeque<String> mQeque = new LinkedBlockingDeque<>();

    /**
     * Exit Code
     */
    private final AtomicInteger mExitSysn = new AtomicInteger(-1);

    private volatile Process mProcess;
    private String mCharset;
    /**
     * stdin, stdout
     */
    private volatile BufferedReader mReader;
    private volatile BufferedWriter mWriter;

    private ThreadFactory mFactory;

    private static final ThreadFactory DEFAULT = new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r);
        }
    };

    private boolean bRunning = true;
    private static final int MAX_COUNT = 5;

    private final void readLoop() {
        try {
            int exitValue = -1;
            int excepCount = 0;
            onShellStart();
            do {
                String line = null;
                try {
                    line = mReader.readLine();
                    excepCount = 0;
                    if (line != null) {
                        if (mIsPrint) {
                            System.out.println(line);
                        }
                        synchronized (mQeque) {
                            mQeque.put(line);
                        }
                        onShellRecve();
                    } else {
                        throw new IOException("NULL");
                    }
                } catch (Throwable e) {
                    if (excepCount >= MAX_COUNT) {
                        e.printStackTrace();
                        mProcess.destroy();
                        bRunning = false;
                    } else {
                        try {
                            exitValue = mProcess.exitValue();
                            bRunning = false;
                        } catch (IllegalThreadStateException ee) {
                            //出现异常但进程却还没有停止，就再等等
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            } while (bRunning);

            synchronized (mExitSysn) {
                mProcess = null;
                mWriter = null;
                mReader = null;

                mExitSysn.set(exitValue);
                mExitSysn.notifyAll();
            }
        } finally {
            onShellEnd();
        }
    }

    private final Runnable localRead = new Runnable() {
        @Override
        public void run() {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(System.in, mCharset));
                do {
                    String line = null;
                    try {
                        line = reader.readLine();
                        if (bRunning && line != null) {
                            if (canWrite()) {
                                mWriter.write(line);
                                mWriter.write('\n');
                                mWriter.flush();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } while (bRunning);
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                IO.Close(reader);
            }
        }
    };

    private ThreadGroup shellGroup;
    private Thread remoteThread;
    private Thread localThread;

    private static final boolean DBG_LOG = false;

    public XShell(boolean print, OnShell callBack, String cmds) {
        this(print, callBack, cmds.split(" "));
    }

    public XShell(boolean print, OnShell callBack, String[] cmds) {
        this(print, null, callBack, null);
        if (cmds != null && cmds.length > 0) {
            mParameter.addAll(Arrays.asList(cmds));
        }
    }

    public XShell(boolean print, OnShell callBack, List<String> cmds) {
        this(print, null, callBack, cmds);
    }

    public XShell(boolean print, String charset, OnShell callBack, List<String> cmds) {
        this.mIsPrint = print;
        this.mCallBack = callBack;
        setCharset(charset);
//        if (console) {
//            mBuilder.inheritIO();
//        }
        mBuilder.redirectErrorStream(true);
        if (cmds != null && cmds.size() > 0) {
            mParameter.addAll(cmds);
        }
        mFactory = DEFAULT;
        if (DBG_LOG) {
            System.out.println(mBuilder.environment());
            System.out.println(mBuilder.directory());
            System.out.println(mBuilder.command());
        }
    }

    public XShell setThreadFactory(ThreadFactory factory) {
        this.mFactory = factory == null ? DEFAULT : factory;
        return this;
    }

    public List<String> command() {
        return mBuilder.command();
    }

    public File directory() {
        return mBuilder.directory();
    }

    public Map<String, String> environment() {
        return mBuilder.environment();
    }

    public XShell start() throws IOException {
        if (mProcess == null) {
            bRunning = true;
            mProcess = mBuilder.start();
            mReader = new BufferedReader(new InputStreamReader(mProcess.getInputStream(), mCharset));
            mWriter = new BufferedWriter(new OutputStreamWriter(mProcess.getOutputStream(), mCharset));
            remoteThread = mFactory.newThread(new Runnable() {
                @Override
                public void run() {
                    readLoop();
                }
            });
            remoteThread.start();

            if (mIsPrint) {
                localThread = mFactory.newThread(localRead);
                localThread.start();
            }
        }
        return this;
    }

    public XShell waitFor(long timeout) {
        if (mProcess != null) {
            synchronized (mExitSysn) {
                try {
                    mExitSysn.wait(timeout < 0 ? 0 : timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }

    public final void destroy() {
        try {
            bRunning = false;
            if (mProcess != null) {
                mProcess.destroy();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public int exitValue() {
        return mExitSysn.get();
    }

    public final boolean isAlive() {
        return mProcess != null;
    }

    public boolean canRead() throws IOException {
        return !mQeque.isEmpty();
    }

    public boolean canWrite() {
        return mProcess != null && mWriter != null;
    }

    public String readLine() {
        synchronized (mQeque) {
            if (mQeque.size() > 0) {
                return mQeque.poll();
            } else {
                return null;
            }
        }
    }

    public void writeLine(String line) {
        try {
            if (mIsEcho) {
                System.out.println(line);
            }
            mWriter.write(line);
            mWriter.write('\n');
            mWriter.flush();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public String getResult() {
        StringBuffer sb = new StringBuffer();
        do {
            String line = readLine();
            if (line == null) {
                break;
            }
            sb.append(line).append("\n");
        } while (true);
        return sb.toString();
    }

    public XShell setCharset(String charset) {
        if (charset == null) {
            charset = OS.getCharset().toString();
        }
        mCharset = charset;
        return this;
    }

    /**
     * 添加执行路径:path
     *
     * @param paths
     * @return
     */
    public XShell addPaths(String... paths) {
        if (paths != null && paths.length > 0) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < paths.length; ++i) {
                if (i > 0) {
                    sb.append(";");
                }
                sb.append(paths[i]);
            }
            addEnvs("path", sb.toString());
        }
        return this;
    }

    public XShell addEnvs(String key, String val) {
        final Map<String, String> envs = mBuilder.environment();
        String vals = envs.get(key);
        if (vals != null) {
            vals = vals + ";" + val;
        } else {
            vals = val;
        }
        envs.put(key, vals);
        return this;
    }

    public XShell addEnvs(Map<String, String> envp) {
        final Map<String, String> envs = mBuilder.environment();
        if (envp != null) {
            envs.putAll(envp);
        }
        return this;
    }

    public XShell add(String... parameters) {
        if (parameters != null) {
            mParameter.addAll(Arrays.asList(parameters));
        }
        return this;
    }

    public XShell directory(File directory) {
        mBuilder.directory(directory);
        return this;
    }

}