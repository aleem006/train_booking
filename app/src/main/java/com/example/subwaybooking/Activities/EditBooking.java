package com.example.subwaybooking.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.subwaybooking.DataModel.AllTrainModel;
import com.example.subwaybooking.DataModel.BookingDataModel;
import com.example.subwaybooking.R;
import com.example.subwaybooking.Util.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditBooking extends AppCompatActivity implements View.OnClickListener {

    BookingDataModel dataModel;
    TextView noOfSeats, price, departureCity, ArrivalCity, departureTime,
            arrivalTime, seatCount, bookBtn, priceTotal;
    ImageView addSeatBtn, removeSeatBtn, backBtn;
    int seatCounter = 0;
    int seatPrice = 0;
    int grandTotal = 0;
    int avalableSeats;
    DatabaseReference dbref;
    String ticketPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_booking);

        initView();
        getAvailableNoOfSeats(dataModel);
        setOnClick();
    }

    private void initView(){

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            dataModel = (BookingDataModel) bundle.getSerializable("selectedData");
        }

        dbref = FirebaseDatabase.getInstance().getReference();

        backBtn = findViewById(R.id.backBtn);
        backBtn.setVisibility(View.VISIBLE);
        noOfSeats = findViewById(R.id.noOfSeats);
        price = findViewById(R.id.price);
        priceTotal = findViewById(R.id.priceTotal);
        departureCity = findViewById(R.id.departureCity);
        ArrivalCity = findViewById(R.id.ArrivalCity);
        departureTime = findViewById(R.id.departureTime);
        arrivalTime = findViewById(R.id.arrivalTime);
        seatCount = findViewById(R.id.seatCount);
        bookBtn = findViewById(R.id.bookBtn);
        addSeatBtn = findViewById(R.id.addSeatBtn);
        removeSeatBtn = findViewById(R.id.removeSeatBtn);

    }

    private void setValues(){

        noOfSeats.setText(String.valueOf(avalableSeats));
        priceTotal.setText("Kr "+dataModel.getPrice());
        departureCity.setText(dataModel.getDepartureCity());
        ArrivalCity.setText(dataModel.getArrivalCity());
        departureTime.setText(dataModel.getDepartureTime());
        arrivalTime.setText(dataModel.getArrivalTime());

        seatCounter = dataModel.getNoOfSeats();
        seatCount.setText(String.valueOf(seatCounter));

        seatPrice = Integer.parseInt(ticketPrice);

        avalableSeats = avalableSeats + seatCounter;

    }

    private void setOnClick(){
        addSeatBtn.setOnClickListener(this);
        removeSeatBtn.setOnClickListener(this);
        bookBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    private void getAvailableNoOfSeats(final BookingDataModel model){ // for getting available seats and update it with new Available seats

        dbref.child("routes")
                .child(String.valueOf(model.getId())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                AllTrainModel model = snapshot.getValue(AllTrainModel.class);

                avalableSeats = model.getNoOfSeats();
                ticketPrice = model.getPrice();
                price.setText("Kr "+ticketPrice);

                setValues();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Utils.showToast(error.getMessage(),false);
            }
        });

    }

    private void updateTicketFunction(){

        String sCount = seatCount.getText().toString();

        if (sCount.equals("0")){
            seatCount.setError("please select number of seats");
            seatCount.requestFocus();
            return;
        }

        int seatCount = Integer.parseInt(sCount);

        final BookingDataModel model = new BookingDataModel();
        model.setId(dataModel.getId());
        model.setDepartureCity(dataModel.getDepartureCity());
        model.setArrivalCity(dataModel.getArrivalCity());
        model.setDepartureDate(dataModel.getDepartureDate());
        model.setDepartureTime(dataModel.getDepartureTime());
        model.setArrivalTime(dataModel.getArrivalTime());
        model.setNoOfSeats(seatCount);
        model.setBookingDate(Utils.getCurrentDate());
        model.setPrice(String.valueOf(grandTotal));

        final int newAvailableSeats = avalableSeats - seatCount;

        // sending request to firebase

        model.setAutoGeneratedRequestID(dataModel.getAutoGeneratedRequestID());
        dbref.child("MyBookings").child(dataModel.getAutoGeneratedRequestID()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Utils.showToast("Ticket Booked", true);

                    // setting new Available seats
                    dbref.child("routes").child(String.valueOf(dataModel.getId())).child("noOfSeats").setValue(newAvailableSeats);
                    finish();
                }
            }
        });




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addSeatBtn:
                if (seatCounter < 5) {
                    seatCounter++;
                    grandTotal = seatPrice * seatCounter;
                    seatCount.setText(String.valueOf(seatCounter));
                    priceTotal.setText("Kr "+grandTotal);
                }else {
                    Utils.showToast("book maximum 5 ticket at a time",false);
                }
                break;
            case R.id.removeSeatBtn:
                if (seatCounter > 0) {
                    seatCounter--;
                    grandTotal = seatPrice * seatCounter;
                    seatCount.setText(String.valueOf(seatCounter));
                    priceTotal.setText("Kr "+grandTotal);
                }else {
                    seatCount.setText("0");
                }
                break;
            case R.id.bookBtn:
                updateTicketFunction();
                break;

            case R.id.backBtn:
                onBackPressed();
                break;

        }
    }
}