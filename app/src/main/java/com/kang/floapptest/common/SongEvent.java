package com.kang.floapptest.common;

public class SongEvent { //Event Bus에 담을 객체

    public final String songUrl;
    public int songState;

    public SongEvent(String songUrl, int Songstate) {
        this.songUrl = songUrl;
        this.songState = songState;
    }
}

