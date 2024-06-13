package com.example.meetingsceduling;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class DashBoard extends AppCompatActivity {

    Button btn;
    ArrayList<MeetingModel> meetingList = new ArrayList<>();
    RecyclerMeetingAdapter adapter;
    private SearchView searchView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        RecyclerView recyclerView = findViewById(R.id.recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btn = findViewById(R.id.btn_meeting);
        btn.setOnClickListener(view -> {
            Intent s1 = new Intent(DashBoard.this, CreateMeeting.class);
            startActivity(s1);
        });

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
