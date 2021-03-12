package com.kang.floapptest.service;



import com.kang.floapptest.model.Music;
import com.kang.floapptest.model.ResponseDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface MusicApi {

    @GET("api/music")
    Call<ResponseDto<List<Music>>> findAll();


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.10.225:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
