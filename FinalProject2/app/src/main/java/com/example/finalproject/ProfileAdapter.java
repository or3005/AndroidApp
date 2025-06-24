package com.example.finalproject;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    MyViewModel myViewModel;
    Context context;
    ArrayList<Show> list;
    OnShowListener onshowlistener;
    MediaPlayer mediaPlayer;
    private int selected=-1;


    public class ViewHolder extends RecyclerView.ViewHolder {
        private OnShowListener showListener;

        TextView bandtextview;

        TextView cityTextView;
        TextView areaTextView;

        TextView dateTextView;
        TextView timeTextView;
        TextView priceTextView;
        Button orderButton;
        SeekBar sk;
        ImageView imageView;


        public ViewHolder(@NonNull View itemView, OnShowListener onshowlistener) {
            super(itemView);
            bandtextview = itemView.findViewById(R.id.BandText);

            cityTextView = itemView.findViewById(R.id.CityText);

            dateTextView = itemView.findViewById(R.id.Temp5);
            timeTextView = itemView.findViewById(R.id.TimeText);
            priceTextView = itemView.findViewById(R.id.PriceText);
            orderButton = itemView.findViewById(R.id.orderButton);
            areaTextView = itemView.findViewById(R.id.AreaText);
            sk = itemView.findViewById(R.id.seekBar);
            imageView=itemView.findViewById(R.id.imageView3);
            itemView.setOnClickListener(new View.OnClickListener(){


                @Override
                public void onClick(View view) {


                    switch (bandtextview.getText().toString()){
                        case "metallica":
                            mediaPlayer.pause();
                            mediaPlayer=MediaPlayer.create(context,R.raw.metallicasong);
                            mediaPlayer.start();

                            break;
                        case "slayer":
                            mediaPlayer.pause();
                            mediaPlayer=MediaPlayer.create(context,R.raw.slayersong);
                            mediaPlayer.start();

                            break;
                        case  "pink floyd":
                            mediaPlayer.pause();
                            mediaPlayer=MediaPlayer.create(context,R.raw.pinkfloydsong);
                            mediaPlayer.start();

                            break;
                        case  "oasis":
                            mediaPlayer.pause();
                            mediaPlayer=MediaPlayer.create(context,R.raw.oasissong);
                            mediaPlayer.start();

                            break;
                        case  "tool":
                            mediaPlayer.pause();
                            mediaPlayer=MediaPlayer.create(context,R.raw.toolsong);
                            mediaPlayer.start();

                            break;

                    }





                }
            });
            this.showListener = onshowlistener;

        }


        public void fillData(int position) throws IOException {
            Show show = list.get(position);
            bandtextview.setText(show.getBandname());
            cityTextView.setText(show.getCity());
            dateTextView.setText(show.getDate());
            timeTextView.setText(show.getTime());
            priceTextView.setText(show.getPrice());
            areaTextView.setText(show.getArea());

            orderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showListener.doOrder(position,sk.getProgress());
                }
            });

            switch (show.getBandname()){
                case "metallica":
                    imageView.setImageResource(R.drawable.metallica);
                    imageView.setVisibility(View.VISIBLE);
                    break;
                case "slayer":
                    imageView.setImageResource(R.drawable.slayer);
                    imageView.setVisibility(View.VISIBLE);
                    break;
                case  "pink floyd":
                    imageView.setImageResource(R.drawable.pinkfloyd);
                    imageView.setVisibility(View.VISIBLE);

                    break;
                case  "oasis":
                    imageView.setImageResource(R.drawable.oasis);
                    imageView.setVisibility(View.VISIBLE);
                    break;
                case  "tool":
                    imageView.setImageResource(R.drawable.tool);
                    imageView.setVisibility(View.VISIBLE);
                    break;

            }


        }
    }

    public interface OnShowListener {
        void doOrder(int position, int progress);
    }

    public ProfileAdapter(Context context, ArrayList<Show> list) {
        this.context = context;
        this.list = list;
        mediaPlayer=new MediaPlayer();
//        myViewModel=MainActivity.vm;
//        if (myViewModel.GetCurRow().getValue() != null) {
//
//            selected = myViewModel.GetCurRow().getValue();
//        }

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.tupple, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView, onshowlistener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.fillData(position);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setInterfae(OnShowListener showListener){
        this.onshowlistener = showListener;
    }

}
