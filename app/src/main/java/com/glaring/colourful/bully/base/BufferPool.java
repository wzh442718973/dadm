package com.glaring.colourful.bully.base;

public class BufferPool {

    private final Object mPoolSyn = new Object();
    private final byte[][] mBuffer;
    private int nPoolPos;

    private final int nPoolSize;
    private final int nBuffSize;

    public BufferPool(int poolSize, int buffSize) {
        this.nPoolSize = poolSize;
        this.nBuffSize = buffSize;
        this.nPoolPos = 0;
        this.mBuffer = new byte[poolSize][];
    }

    public byte[] obtain() {
        synchronized (mPoolSyn) {
            if (nPoolPos > 0) {
                return mBuffer[--nPoolPos];
            }
        }
        return new byte[nBuffSize];
    }

    public byte[] recycle(byte[] buffer) {
        synchronized (mPoolSyn) {
            if (nPoolPos < nPoolSize) {
                mBuffer[nPoolPos++] = buffer;
            }
        }
        return null;
    }
}
