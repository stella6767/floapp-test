package com.kang.floapptest.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kang.floapptest.MainActivity;
import com.kang.floapptest.R;
import com.kang.floapptest.common.Constants;
import com.kang.floapptest.model.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> {

    private static final String TAG = "MusicAdapter";
    private final MainActivity mainActivity;

    private List<Song> songList = new ArrayList<>();


    public SongAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public Integer getMovieId(int position) {
        return songList.get(position).getId();
    }


    public void setMusics(List<Song> songList) {
        this.songList = songList;
        notifyDataSetChanged();
    }


    public String getSongUrl(int position) {
        String songUrl = Constants.BASEURL + Constants.FILEPATH + songList.get(position).getFile();
        return songUrl;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.music_item, parent, false);

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
        private ImageButton btnPlayItem; //이건 리사이클러뷰 안의 btn


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvArtist = itemView.findViewById(R.id.tv_artist);
            tvName = itemView.findViewById(R.id.tv_name);
            btnPlayItem = itemView.findViewById(R.id.btn_Item_play);

            btnPlayItem.setOnClickListener(v -> {

                String songUrl = getSongUrl(getAdapterPosition());

                    try {
                        Log.d(TAG, "MyViewHolder: 음악 클릭됨");
                        mainActivity.songPrepare(songUrl);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            });

        }

        public void setItem(Song song) {
            tvName.setText(song.getTitle());
            tvArtist.setText(song.getArtist());

        }


    }
}
