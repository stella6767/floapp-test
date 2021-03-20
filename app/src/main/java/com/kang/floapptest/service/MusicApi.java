package com.kang.floapptest.service;



import com.kang.floapptest.common.Constants;
import com.kang.floapptest.model.Song;
import com.kang.floapptest.model.ResponseDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface MusicApi {

    @GET("api/song")
    Call<ResponseDto<List<Song>>> findAll();


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
