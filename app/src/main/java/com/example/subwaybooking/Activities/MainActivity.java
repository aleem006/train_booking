package com.example.subwaybooking.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.subwaybooking.R;
import com.example.subwaybooking.Util.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.DecimalFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView searchBtn, selectDate;
    AutoCompleteTextView selectDepartureCity, selectArrivalCity;
    Toolbar toolbar;
    TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setValues();
        setOnClick();

//        ///// Get firebase token
//        // Get updated InstanceID token.
//        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//            @Override
//            public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                if (!task.isSuccessful()) {
//                    Log.w("getInstanceId failed", task.getException());
//                    return;
//                }
//
//                // Get new Instance ID token
//                String token = task.getResult().getToken();
//
//                Log.d("fireBaseToken", token);
//
//
//            }
//        });
//        ////// ENd firebase token
    }

    private void initView(){

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(getResources().getString(R.string.app_name));
        searchBtn = findViewById(R.id.searchBtn);
        selectDate = findViewById(R.id.selectDate);
        selectDepartureCity = findViewById(R.id.selectDepartureCity);
        selectArrivalCity = findViewById(R.id.selectArrivalCity);

    }

    private void setValues(){

        String[] arr = {"Oslo", "Stavanger","Trondheim", "Tromsø", "Lillehammer", "Bergen", "Hamar"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, arr);

        selectDepartureCity.setThreshold(2);
        selectDepartureCity.setAdapter(adapter);


        selectArrivalCity.setThreshold(2);
        selectArrivalCity.setAdapter(adapter);

    }

    private void setOnClick(){
        searchBtn.setOnClickListener(this);
        selectDate.setOnClickListener(this);
    }

    private void search(){

        String departureCity = selectDepartureCity.getText().toString();
        String arrivalCity = selectArrivalCity.getText().toString();
        String selectedDate = selectDate.getText().toString();

        if (departureCity.isEmpty()){
            selectDepartureCity.setError("please select departure City");
            selectDepartureCity.requestFocus();
            return;
        }

        if (arrivalCity.isEmpty()){
            selectArrivalCity.setError("please select arrival City");
            selectArrivalCity.requestFocus();
            return;
        }

        if (departureCity.equals(arrivalCity)){
            selectArrivalCity.setError("both city could not be same");
            selectArrivalCity.requestFocus();
            return;
        }


        Intent intent = new Intent(MainActivity.this, AllTrainTiming.class);
        intent.putExtra("departureCity",departureCity);
        intent.putExtra("arrivalCity",arrivalCity);
        intent.putExtra("searchDate",selectedDate);
        startActivity(intent);
    }

    private void openCalender(){
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        DecimalFormat mFormat= new DecimalFormat("00");
                        String month = mFormat.format(Double.valueOf(monthOfYear + 1));
                        String day = mFormat.format(Double.valueOf(dayOfMonth));

                        selectDate.setText(year + "-" + month + "-" + day);

                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.myBooking:
                Intent intent = new Intent(MainActivity.this, MyBookings.class);
                startActivity(intent);
                break;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.searchBtn:
                search();
                break;
            case R.id.selectDate:
                openCalender();
                break;
        }
    }
}