package com.kang.floapptest.adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kang.floapptest.MainActivity;
import com.kang.floapptest.R;
import com.kang.floapptest.model.Music;
import com.kang.floapptest.service.PlayService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder>{

    private static final String TAG = "MusicAdapter";
    private final MainActivity mainActivity;
    private MediaPlayer mp = new MediaPlayer();

    private List<Music> musics = new ArrayList<>();

    public MusicAdapter(MainActivity mainActivity, MediaPlayer mp) {
        this.mainActivity = mainActivity;
        //this.mp = mp;  //안 되네.... 서비스 바인딩 어케하지...
    }

    public Integer getMovieId(int position){
        return musics.get(position).getId();
    }


    public void setMusics(List<Music> musics){
        this.musics = musics;
        notifyDataSetChanged();
    }

    public void playMusic(int position) throws IOException { //mutable live data만 삭제..
        mp.reset();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setDataSource(musics.get(position).getUrl());
        mp.prepare(); // might take long! (for buffering, etc)
        mp.start();

    }

    




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.music_item,parent,false);

        return new MyViewHolder(view); //view가 리스트뷰에 하나 그려짐.
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setItem(musics.get(position));
    }

    @Override
    public int getItemCount() {
        return musics.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        //2번 user_item이 가지고 있는 위젯들을 선언
        private TextView tvArtist;
        private TextView tvName;
        private Button btnPlay;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvArtist = itemView.findViewById(R.id.tv_artist);
            tvName = itemView.findViewById(R.id.tv_name);
            btnPlay = itemView.findViewById(R.id.btn_play);

            btnPlay.setOnClickListener(v -> {
                Log.d(TAG, "MyViewHolder: 클릭됨"+getAdapterPosition());
                try {
                    playMusic(getAdapterPosition());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }

        public void setItem(Music music){
            tvName.setText(music.getName());
            tvArtist.setText(music.getArtist());

        }



    }
}
