package com.glaring.colourful.bully.ad;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public final class FileLock {

    private java.nio.channels.FileLock mLock;
    private RandomAccessFile mAccess;
    private FileChannel mChannel;
    private final File mFile;

    public FileLock(File file) throws FileNotFoundException {
        mFile = file;
        mAccess = new RandomAccessFile(file, "rw");
        mChannel = mAccess.getChannel();
    }

    public final File getLockFile() {
        return mFile;
    }

    public void lock() throws IOException {
        if (mLock == null) {
            mLock = mChannel.lock();
        }
    }

    public boolean tryLock() throws IOException {
        if (mLock != null) {
            return true;
        } else {
            mLock = mChannel.tryLock();
            return mLock != null;
        }
    }

    public void unlock() {
        if (mLock != null) {
            try {
                mLock.release();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mLock = null;

        try {
            mAccess.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mChannel = null;
        mAccess = null;

    }
}
