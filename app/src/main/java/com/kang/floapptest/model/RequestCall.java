package com.kang.floapptest.model;

import com.kang.floapptest.common.Constants;

import java.util.ArrayList;

import lombok.Data;

@Data
public class RequestCall {
    private int status = Constants.STOPPED;
    private ArrayList<Song> songList = new ArrayList<>();

}
