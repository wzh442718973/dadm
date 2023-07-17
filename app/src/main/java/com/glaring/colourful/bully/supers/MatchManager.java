package com.glaring.colourful.bully.supers;

import java.util.ArrayList;

public class MatchManager {


    public static abstract class Match<KEY, Observer, Obj> {
        public final KEY mKey;
        public final Class<? extends Observer> mClszz;

        public Match(KEY key, Class<? extends Observer> clszz) {
            this.mKey = key;
            this.mClszz = clszz;
        }


        public abstract Obj newObject(Object obj);
    }


    private final ArrayList<Match> mMatchs = new ArrayList<>();

    public final int size() {
        return mMatchs.size();
    }

    private final Match _match(Object key) {
        for(Match match : mMatchs){
            if(match.equals(key)){
                return match;
            }
        }
        return null;
    }


    public <V extends Match> V find(Object key) {
        if (key != null) {
            return (V) _match(key);
        }
        return null;
    }

    public void add(Match match) {
        if (match != null) {
            if (null == _match(match.mKey)) {
                mMatchs.add(match);
            }
        }
    }
}
