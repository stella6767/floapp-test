package com.kang.floapptest.common;

import android.media.MediaPlayer;

import java.io.IOException;

public class CustomMediaPlayer extends MediaPlayer {

    private String datasource;

    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException, IllegalStateException, SecurityException {
        super.setDataSource(path);
        datasource = path;
    }


    public String getDatasource(){
        return datasource;
    }

    @Override
    public int getDuration() {
        return super.getDuration();
    }



}
