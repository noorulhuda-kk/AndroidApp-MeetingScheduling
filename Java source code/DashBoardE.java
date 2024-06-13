package com.example.meetingsceduling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DashBoardE extends AppCompatActivity {

    private RecyclerView recyclerViewE;
    private TextView noMeetingsTextView;
    private SearchView searchViewE;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private ArrayList<MeetingModel> meetingList;
    private RecyclerMeetingAdapterE adapter;
    private String currentUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_e);

        recyclerViewE = findViewById(R.id.recyclerE);
        noMeetingsTextView = findViewById(R.id.noMeetingsTextView);
        searchViewE = findViewById(R.id.searchViewE);

        recyclerViewE.setLayoutManager(new LinearLayoutManager(this));
        meetingList = new ArrayList<>();
        adapter = new RecyclerMeetingAdapterE(this, meetingList);
        recyclerViewE.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            db.collection("users").document(currentUser.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            currentUserName = task.getResult().getString("name");
                            loadMeetingsFromFirestore();
                        } else {
                            Toast.makeText(DashBoardE.this, "Failed to get user details.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        searchViewE.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
    }

    private void loadMeetingsFromFirestore() {
        db.collection("meetings")
                .whereEqualTo("status", "pending")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        meetingList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String employees = document.getString("employees");
                            if (employees != null && employees.contains(currentUserName)) {
                                MeetingModel meeting = new MeetingModel(
                                        document.getString("title"),
                                        document.getString("date"),
                                        document.getString("start_time"),
                                        document.getString("end_time"),
                                        employees,
                                        document.getString("location"),
                                        document.getString("description"),
                                        document.getString("status")
                                );
                                meetingList.add(meeting);
                            }
                        }
                        if (meetingList.isEmpty()) {
                            noMeetingsTextView.setVisibility(View.VISIBLE);
                        } else {
                            noMeetingsTextView.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(DashBoardE.this, "Error getting meetings: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void filterList(String text) {
        ArrayList<MeetingModel> filteredList = new ArrayList<>();
        for (MeetingModel item : meetingList) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase()) ||
                    item.getEmployee().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new RecyclerMeetingAdapterE(this, filteredList);
            recyclerViewE.setAdapter(adapter);
        }
    }
}
