package com.example.meetingsceduling;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class DashBoard extends AppCompatActivity {

    private Button btn, logoutButton;
    private TextView headingDashboard;
    ArrayList<MeetingModel> meetingList = new ArrayList<>();
    RecyclerMeetingAdapter adapter;
    private SearchView searchView;
    private FirebaseFirestore db;


    private FirebaseAuth auth;
    private String currentUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        getWindow().setStatusBarColor(ContextCompat.getColor(DashBoard.this, R.color.primaryColor));

        RecyclerView recyclerView = findViewById(R.id.recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btn = findViewById(R.id.btn_meeting);
        btn.setOnClickListener(view -> {
            Intent s1 = new Intent(DashBoard.this, CreateMeeting.class);
            startActivity(s1);
        });

        logoutButton = findViewById(R.id.logoutButtonDashboard);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(DashBoard.this, MainActivity.class));
                finish();
            }
        });

        headingDashboard = findViewById(R.id.headingDashboard);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            db.collection("users").document(currentUser.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            currentUserName = task.getResult().getString("name");
                            headingDashboard.setText(currentUserName+"\'s Dashboard");
                        }
                    });
        }

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        db = FirebaseFirestore.getInstance();

        loadMeetingsFromFirestore();

        adapter = new RecyclerMeetingAdapter(this, meetingList);
        recyclerView.setAdapter(adapter);
    }

    private void loadMeetingsFromFirestore() {
        db.collection("meetings")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String title = document.getString("title");
                            String date = document.getString("date");
                            String startTime = document.getString("start_time");
                            String endTime = document.getString("end_time");
                            String employee = document.getString("employees");
                            String location = document.getString("location");
                            String description = document.getString("description");
                            String status = document.getString("status");

                            MeetingModel meeting = new MeetingModel(title, date, startTime, endTime, employee, location, description, status);
                            meetingList.add(meeting);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(DashBoard.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void filterList(String text) {
        ArrayList<MeetingModel> filteredList = new ArrayList<>();
        for (MeetingModel item : meetingList) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setFilteredList(filteredList);
        }
    }
}
