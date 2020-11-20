package com.example.subwaybooking.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.subwaybooking.DataModel.AllTrainModel;
import com.example.subwaybooking.Interface.ClickListener;
import com.example.subwaybooking.R;

import java.util.List;

public class AllTrainAdapter extends RecyclerView.Adapter<AllTrainAdapter.AllTrainViewHolder> {

    private List<AllTrainModel> list;
    private ClickListener clickListener;

    public AllTrainAdapter(List<AllTrainModel> list) {
        this.list = list;
    }

    public void setOnItemClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public AllTrainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_all_train_view, parent,false);
        final AllTrainViewHolder holder = new AllTrainViewHolder(view);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null){
                    int positon = holder.getAdapterPosition();
                    if (positon != RecyclerView.NO_POSITION){
                        clickListener.onClick(positon);
                    }
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AllTrainViewHolder holder, int position) {

        holder.departureCity.setText(list.get(position).getDepartureCity());
        holder.arrivalCity.setText(list.get(position).getArrivalCity());
        holder.departureTime.setText(list.get(position).getDepartureTime());
        holder.arrivalTime.setText(list.get(position).getArrivalTime());
        holder.noOfSeats.setText(""+list.get(position).getNoOfSeats());
        holder.price.setText("Kr "+list.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AllTrainViewHolder extends RecyclerView.ViewHolder {

        TextView departureCity, arrivalCity, departureTime,
                arrivalTime, noOfSeats, price;
        public AllTrainViewHolder(@NonNull View itemView) {
            super(itemView);

            departureCity = itemView.findViewById(R.id.departureCity);
            arrivalCity = itemView.findViewById(R.id.ArrivalCity);
            departureTime = itemView.findViewById(R.id.departureTime);
            arrivalTime = itemView.findViewById(R.id.arrivalTime);
            noOfSeats = itemView.findViewById(R.id.noOfSeats);
            price = itemView.findViewById(R.id.price);

        }
    }
}
