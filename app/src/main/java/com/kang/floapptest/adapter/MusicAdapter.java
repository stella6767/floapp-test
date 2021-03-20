package com.kang.floapptest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.kang.floapptest.MainActivity;
import com.kang.floapptest.R;
import com.kang.floapptest.common.Constants;
import com.kang.floapptest.model.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder>{

    private static final String TAG = "MusicAdapter";
    private final MainActivity mainActivity;

    private List<Song> songList = new ArrayList<>();


    public MusicAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public Integer getMovieId(int position){
        return songList.get(position).getId();
    }


    public void setMusics(List<Song> musics){
        this.songList = musics;
        notifyDataSetChanged();
    }


    public String getMusic(int position){
        String musicUrl = Constants.BASEURL+ Constants.FILEPATH +songList.get(position).getFile();
        return musicUrl;
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
        holder.setItem(songList.get(position));
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        //2번 user_item이 가지고 있는 위젯들을 선언
        private TextView tvArtist;
        private TextView tvName;
        private ImageButton btnPlay; //이건 리사이클러뷰 안의 btn


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvArtist = itemView.findViewById(R.id.tv_artist);
            tvName = itemView.findViewById(R.id.tv_name);
            btnPlay = itemView.findViewById(R.id.btn_play);

            btnPlay.setOnClickListener(v -> {

                String musicUrl = getMusic(getAdapterPosition());




                mainActivity.isPlaying = mainActivity.isPlaying * -1;

                    if (mainActivity.isPlaying == 1) {

                        try {
                            mainActivity.playSong(musicUrl);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        mainActivity.musicPause();

                    }

                    mainActivity.uiHandleThread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            while (mainActivity.isPlaying == 1) {
                                mainActivity.handler.post(new Runnable() {// runOnUiThread랑 같음, 대신 이렇게 쓰면 uiHandleThread 쓰레드를 원하는데서 참조가능
                                    @Override //UI 변경하는 애만 메인 스레드에게 메시지를 전달
                                    public void run() {
                                        mainActivity.seekBar.setProgress(mainActivity.mp.getCurrentPosition());

                                        if (mainActivity.mp.getCurrentPosition() >= mainActivity.mp.getDuration()) {
                                            mainActivity.musicStop();
                                        }
                                    }
                                });

                                try {
                                    Thread.sleep(1000);
                                    if(mainActivity.threadStatus){
                                        mainActivity.uiHandleThread.interrupt(); //그 즉시 스레드 종료시키기 위해(강제종료), sleep을 무조건 걸어야 된다. 스레드가 조금이라도 쉬어야 동작함
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });

                    mainActivity.uiHandleThread.start();
                });

        }

        public void setItem(Song song){
            tvName.setText(song.getTitle());
            tvArtist.setText(song.getArtist());

        }



    }
}
