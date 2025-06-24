package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyReservationAdapter extends RecyclerView.Adapter<MyReservationAdapter.ViewHolder>{

    ArrayList<Reservations> list;
    Context context;
    OnShowsListenerReservations onShowsListenerReservations;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView show_string;
        TextView cityTextView;
        TextView date_time_TextView;
        Button cancelButton;
        OnShowsListenerReservations onshowslistenerReservations;

        public ViewHolder(@NonNull View itemView, OnShowsListenerReservations onShowsListenerReservations) {
            super(itemView);
            this.onshowslistenerReservations = onShowsListenerReservations;
            show_string = itemView.findViewById(R.id.ShowText_reservations);
            cityTextView = itemView.findViewById(R.id.CityText_reservations);
            date_time_TextView = itemView.findViewById(R.id.DateText_reservations);
            cancelButton = itemView.findViewById(R.id.cancelButton_reservations);
        }

        public void fillData(int position){
            Reservations reservation = list.get(position);
            show_string.setText(reservation.get_teams_string());
            cityTextView.setText(reservation.getCity());
            date_time_TextView.setText(reservation.getDate() +" "+ reservation.getTime());
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onshowslistenerReservations.cancelOrder(position);
                }
            });
        }
    }
    public interface OnShowsListenerReservations {
        void cancelOrder(int position);
    }


    public MyReservationAdapter(Context context, ArrayList<Reservations> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.tupple_reservations, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView, onShowsListenerReservations);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.fillData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setInterface(OnShowsListenerReservations onShowsListenerReservations) {
        this.onShowsListenerReservations = onShowsListenerReservations;
    }

}
