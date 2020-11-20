package com.example.subwaybooking.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.subwaybooking.DataModel.BookingDataModel;
import com.example.subwaybooking.Interface.BookingAdapterClickListener;
import com.example.subwaybooking.Interface.ClickListener;
import com.example.subwaybooking.R;

import java.util.List;

public class MyBookingAdapter extends RecyclerView.Adapter<MyBookingAdapter.MyBookingHolder> {

    private List<BookingDataModel> list;
    private BookingAdapterClickListener clickListener;

    public MyBookingAdapter(List<BookingDataModel> list) {
        this.list = list;
    }

    public void setOnItemClickListener(BookingAdapterClickListener clickListener){
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyBookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_mybooking_view, parent, false);
        final MyBookingHolder holder = new MyBookingHolder(view);

        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null){
                    int positon = holder.getAdapterPosition();
                    if (positon != RecyclerView.NO_POSITION){
                        clickListener.onDelete(positon);
                    }
                }
            }
        });

        holder.editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null){
                    int positon = holder.getAdapterPosition();
                    if (positon != RecyclerView.NO_POSITION){
                        clickListener.onEdit(positon);
                    }
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyBookingHolder holder, int position) {

        BookingDataModel model = list.get(position);

        holder.departureCity.setText(model.getDepartureCity());
        holder.arrivalCity.setText(model.getArrivalCity());
        holder.departureTime.setText(model.getDepartureTime());
        holder.arrivalTime.setText(model.getArrivalTime());
        holder.noOfSeats.setText(""+model.getNoOfSeats());
        holder.price.setText("Kr "+model.getPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyBookingHolder extends RecyclerView.ViewHolder {

        TextView departureCity, arrivalCity, departureTime,
                arrivalTime, noOfSeats, price;
        ImageView deleteIcon, editIcon;
        public MyBookingHolder(@NonNull View itemView) {
            super(itemView);

            departureCity = itemView.findViewById(R.id.departureCity);
            arrivalCity = itemView.findViewById(R.id.ArrivalCity);
            departureTime = itemView.findViewById(R.id.departureTime);
            arrivalTime = itemView.findViewById(R.id.arrivalTime);
            noOfSeats = itemView.findViewById(R.id.noOfSeats);
            price = itemView.findViewById(R.id.price);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
            editIcon = itemView.findViewById(R.id.editIcon);
        }
    }
}
