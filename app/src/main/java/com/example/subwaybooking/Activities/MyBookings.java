package com.example.subwaybooking.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.subwaybooking.Adapter.MyBookingAdapter;
import com.example.subwaybooking.DataModel.AllTrainModel;
import com.example.subwaybooking.DataModel.BookingDataModel;
import com.example.subwaybooking.Interface.BookingAdapterClickListener;
import com.example.subwaybooking.R;
import com.example.subwaybooking.Util.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyBookings extends AppCompatActivity implements View.OnClickListener {

    TextView toolbarTitle;
    ImageView backBtn;
    DatabaseReference dbref;
    RecyclerView allBookingRecycler;
    MyBookingAdapter myBookingAdapter;
    List<BookingDataModel> list;
    ProgressBar progress;
    int newAvailableSeats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        initView();
        setValues();
        setOnClick();
    }

    private void initView(){

        dbref = FirebaseDatabase.getInstance().getReference();

        backBtn = findViewById(R.id.backBtn);
        backBtn.setVisibility(View.VISIBLE);

        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(getResources().getString(R.string.myBooking));

        progress = findViewById(R.id.progress);

        allBookingRecycler = findViewById(R.id.allBookingRecycler);
        allBookingRecycler.setLayoutManager(new LinearLayoutManager(this));

    }

    private void setAdapterClick(){

        myBookingAdapter.setOnItemClickListener(new BookingAdapterClickListener() {
            @Override
            public void onEdit(int position) {
                Intent intent = new Intent(MyBookings.this, EditBooking.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("selectedData", (Serializable) list.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onDelete(int position) {
                getAvailableNoOfSeats(list.get(position));
                showDeletePopup(list.get(position));
            }
        });
    }

    private void setValues(){

        progress.setVisibility(View.VISIBLE);
        dbref.child("MyBookings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot postsnap: snapshot.getChildren()){
                    BookingDataModel model = postsnap.getValue(BookingDataModel.class);
                    list.add(model);
                }

                if (snapshot.hasChildren()) {
                    myBookingAdapter = new MyBookingAdapter(list);
                    allBookingRecycler.setAdapter(myBookingAdapter);

                    setAdapterClick();

                }else {
                    Utils.showToast("no booking yet",false);
                }

                progress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progress.setVisibility(View.GONE);
                Utils.showToast(""+error.getMessage(),true);
            }
        });

    }

    private void setOnClick(){

        backBtn.setOnClickListener(this);
    }

    public void showDeletePopup(final BookingDataModel model){

        final Dialog dialog = new Dialog(MyBookings.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_delete_dialog);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final ImageView close = dialog.findViewById(R.id.closeBtn);
        final TextView messageText = dialog.findViewById(R.id.message);
        final TextView doneBtn = dialog.findViewById(R.id.doneBtn);

        messageText.setText(getResources().getString(R.string.sureToDelete));

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFunction(model);
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void deleteFunction(final BookingDataModel model){

        dbref.child("MyBookings")
                .child(model.getAutoGeneratedRequestID()).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Utils.showToast("booking canceled",false);

                // setting new Available seats
                dbref.child("routes")
                        .child(String.valueOf(model.getId()))
                        .child("noOfSeats").setValue(newAvailableSeats);

                finish();

            }
        });


    }

    private void getAvailableNoOfSeats(final BookingDataModel model){ // for getting available seats and update it with new Available seats

        dbref.child("routes")
                .child(String.valueOf(model.getId()))
                .child("noOfSeats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                long val = (long) snapshot.getValue();
                int avalableSeats = (int) val;

                newAvailableSeats = avalableSeats + model.getNoOfSeats();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Utils.showToast(error.getMessage(),false);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backBtn:
                onBackPressed();
                break;
        }
    }
}