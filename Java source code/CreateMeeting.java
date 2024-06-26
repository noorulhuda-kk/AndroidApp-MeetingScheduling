package com.example.meetingsceduling;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateMeeting extends AppCompatActivity {

    private EditText meetingTitle, meetingDate, startTime, endTime, location, description;
    private LinearLayout employeeContainer;
    private Button upload, logoutButton;
    private FirebaseFirestore db;
    private ArrayList<CheckBox> employeeCheckboxes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);
        getWindow().setStatusBarColor(ContextCompat.getColor(CreateMeeting.this, R.color.primaryColor));

        meetingTitle = findViewById(R.id.meetingTitle);
        meetingDate = findViewById(R.id.meetingDate);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        location = findViewById(R.id.location);
        description = findViewById(R.id.description);
        employeeContainer = findViewById(R.id.employeeContainer);
        upload = findViewById(R.id.upload);
        logoutButton = findViewById(R.id.logoutButtonCreateMeeting);

        db = FirebaseFirestore.getInstance();

        meetingDate.setOnClickListener(v -> showDatePickerDialog());
        startTime.setOnClickListener(v -> showTimePickerDialog(startTime));
        endTime.setOnClickListener(v -> showTimePickerDialog(endTime));

        upload.setOnClickListener(v -> uploadMeetingDetails());

        loadEmployeesFromFirestore();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(CreateMeeting.this, MainActivity.class));
                finish();
            }
        });

    }



    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
            String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
            meetingDate.setText(date);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog(final EditText timeField) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            String time = hourOfDay + ":" + minute1;
            timeField.setText(time);
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private void loadEmployeesFromFirestore() {
        db.collection("users")
                .whereEqualTo("role", "employee")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String employeeName = document.getString("name");
                                CheckBox checkBox = new CheckBox(CreateMeeting.this);
                                checkBox.setText(employeeName);
                                employeeContainer.addView(checkBox);
                                employeeCheckboxes.add(checkBox);
                            }
                        } else {
                            Toast.makeText(CreateMeeting.this, "Error getting employees: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void uploadMeetingDetails() {
        String title = meetingTitle.getText().toString();
        String date = meetingDate.getText().toString();
        String start = startTime.getText().toString();
        String end = endTime.getText().toString();
        String loc = location.getText().toString();
        String desc = description.getText().toString();
        String employees = getSelectedEmployees();

        if (title.isEmpty() || date.isEmpty() || start.isEmpty() || end.isEmpty() || loc.isEmpty() || desc.isEmpty() || employees.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> meeting = new HashMap<>();
        meeting.put("title", title);
        meeting.put("date", date);
        meeting.put("start_time", start);
        meeting.put("end_time", end);
        meeting.put("location", loc);
        meeting.put("description", desc);
        meeting.put("employees", employees);
        meeting.put("status", "pending");

        db.collection("meetings").document(title)
                .set(meeting)
                .addOnSuccessListener(documentReference -> Toast.makeText(this, "Meeting details uploaded", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error adding document", Toast.LENGTH_SHORT).show());
        Intent s1 = new Intent(CreateMeeting.this, DashBoard.class);
        startActivity(s1);
    }

    private String getSelectedEmployees() {
        StringBuilder employees = new StringBuilder();
        for (CheckBox checkBox : employeeCheckboxes) {
            if (checkBox.isChecked()) {
                employees.append(checkBox.getText().toString()).append(", ");
            }
        }

        if (employees.length() > 0) {
            employees.setLength(employees.length() - 2); // Remove the last comma and space
        }
        return employees.toString();
    }
}
