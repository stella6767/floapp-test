package com.kang.floapptest.model.repository;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.kang.floapptest.model.RequestCall;
import com.kang.floapptest.model.ResponseDto;
import com.kang.floapptest.model.Song;
import com.kang.floapptest.service.MusicApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongRepository {

    private static final String TAG = "SongRepository";
    private MutableLiveData<List<Song>> mtSongList;


    public SongRepository(MutableLiveData<List<Song>> mtSongList) {
        this.mtSongList = mtSongList;
    }

    public void networkConnect(){
        Call<ResponseDto<List<Song>>> call = MusicApi.retrofit.create(MusicApi.class).findAll();

        call.enqueue(new Callback<ResponseDto<List<Song>>>() {
            @Override
            public void onResponse(Call<ResponseDto<List<Song>>> call, Response<ResponseDto<List<Song>>> response) {
                Log.d(TAG, "onResponse: 성공");
                ResponseDto<List<Song>> result = response.body();
                Log.d(TAG, "onResponse: result: "+result);
                mtSongList.setValue(result.getData());
            }

            @Override
            public void onFailure(Call<ResponseDto<List<Song>>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }


//    public MutableLiveData<RequestCall> play(MediaPlayer mMediaPlayer, Context c, Song song) {
//        RequestCall r = new RequestCall();
//        r.setStatus(STOPPED);
//        r.setSongs(new ArrayList<>());
//        MutableLiveData<RequestCall> mLiveData = new MutableLiveData<>();
//        mLiveData.setValue(r);
//
//        if (mMediaPlayer == null) {
//            mMediaPlayer = MediaPlayer.create(c, Uri.parse(song.getData()));
//        }
//        mMediaPlayer.start();
//
//        r.setStatus(PLAYING);
//        mLiveData.postValue(r);
//        return mLiveData;
//    }


}
