package com.example.subwaybooking.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subwaybooking.Adapter.AllTrainAdapter;
import com.example.subwaybooking.DataModel.AllTrainModel;
import com.example.subwaybooking.Interface.ClickListener;
import com.example.subwaybooking.R;
import com.example.subwaybooking.Util.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AllTrainTiming extends AppCompatActivity implements View.OnClickListener {

    ImageView backBtn;
    TextView toolbarTitle, dateText;
    RecyclerView trainRecycler;
    AllTrainAdapter allTrainAdapter;
    DatabaseReference dbref;
    List<AllTrainModel> list;
    ProgressBar progress;

    String departureCity, arrivalCity, searchDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_train_timing);

        initView();
        setValues();
        setOnClick();
        getRoutes();
    }

    private void initView(){

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            departureCity = bundle.getString("departureCity","");
            arrivalCity = bundle.getString("arrivalCity","");
            searchDate = bundle.getString("searchDate","");
        }

        backBtn = findViewById(R.id.backBtn);
        backBtn.setVisibility(View.VISIBLE);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        dateText = findViewById(R.id.dateText);
        progress = findViewById(R.id.progress);

        trainRecycler = findViewById(R.id.trainRecycler);
        trainRecycler.setLayoutManager(new LinearLayoutManager(this));

    }

    private void setValues(){

        toolbarTitle.setText(getResources().getString(R.string.TrainTiming));
        dateText.setText(searchDate);
    }

    private void setOnClick(){
        backBtn.setOnClickListener(this);
    }

    private void setAdapterOnClick(){

        allTrainAdapter.setOnItemClickListener(new ClickListener() {
            @Override
            public void onClick(int position) {

                Intent intent = new Intent(AllTrainTiming.this, BookTicket.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("selectedData", (Serializable) list.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void getRoutes(){

        progress.setVisibility(View.VISIBLE);
        dbref = FirebaseDatabase.getInstance().getReference().child("routes");
        dbref.orderByChild("search").equalTo(departureCity+"-"+arrivalCity+"-"+searchDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list = new ArrayList<>();
                for (DataSnapshot postsnap: snapshot.getChildren()){
                    AllTrainModel reqModel = postsnap.getValue(AllTrainModel.class);
                    list.add(reqModel);
                }

                if (snapshot.hasChildren()) {

                    allTrainAdapter = new AllTrainAdapter(list);
                    trainRecycler.setAdapter(allTrainAdapter);

                    setAdapterOnClick();

                }else {
                    Utils.showToast("not found",false);
                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progress.setVisibility(View.GONE);
                Utils.showToast(""+error.getMessage(),false);
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