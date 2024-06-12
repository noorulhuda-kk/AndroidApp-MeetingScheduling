package com.example.meetingsceduling;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RecyclerMeetingAdapter extends RecyclerView.Adapter<RecyclerMeetingAdapter.ViewHolder> {

    private Context context;
    private ArrayList<MeetingModel> meetingList;
    private FirebaseFirestore db;

    public RecyclerMeetingAdapter(Context context, ArrayList<MeetingModel> meetingList) {
        this.context = context;
        this.meetingList = meetingList;
        this.db = FirebaseFirestore.getInstance();
    }

    public void setFilteredList(ArrayList<MeetingModel> filteredList) {
        this.meetingList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.meeting_card_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MeetingModel meeting = meetingList.get(position);
        holder.meetingTitle.setText(meeting.getTitle());
        holder.meetingDate.setText(meeting.getDate());
        holder.meetingTime.setText(meeting.getStartTime() + " - " + meeting.getEndTime());
        holder.meetingEmployee.setText(meeting.getEmployee());
        holder.meetingStatus.setText(meeting.getStatus());

        holder.itemView.setOnClickListener(v -> showMeetingDetailsDialog(meeting));
    }

    @Override
    public int getItemCount() {
        return meetingList.size();
    }

    private void showMeetingDetailsDialog(MeetingModel meeting) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_meeting_details, null);
        builder.setView(dialogView);

        TextView title = dialogView.findViewById(R.id.meetingTitle);
        TextView date = dialogView.findViewById(R.id.meetingDate);
        TextView startTime = dialogView.findViewById(R.id.meetingStartTime);
        TextView endTime = dialogView.findViewById(R.id.meetingEndTime);
        TextView employees = dialogView.findViewById(R.id.meetingEmployees);
        TextView location = dialogView.findViewById(R.id.meetingLocation);
        TextView description = dialogView.findViewById(R.id.meetingDescription);
        TextView status = dialogView.findViewById(R.id.meetingStatus);
        Button closeButton = dialogView.findViewById(R.id.closeButton);
        Button deleteButton = dialogView.findViewById(R.id.deleteButton);
        Button completedButton = dialogView.findViewById(R.id.completedButton);

        title.setText(meeting.getTitle());
        date.setText(meeting.getDate());
        startTime.setText(meeting.getStartTime());
        endTime.setText(meeting.getEndTime());
        employees.setText(meeting.getEmployee());
        location.setText(meeting.getLocation());
        description.setText(meeting.getDescription());
        status.setText(meeting.getStatus());

        AlertDialog alertDialog = builder.create();

        closeButton.setOnClickListener(v -> alertDialog.dismiss());
        deleteButton.setOnClickListener(v -> deleteMeeting(meeting, alertDialog));
        completedButton.setOnClickListener(v -> markMeetingAsCompleted(meeting, alertDialog));

        alertDialog.show();
    }

    private void deleteMeeting(MeetingModel meeting, AlertDialog alertDialog) {
        db.collection("meetings").document(meeting.getTitle())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    meetingList.remove(meeting);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Meeting deleted", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Error deleting meeting", Toast.LENGTH_SHORT).show());
    }

    private void markMeetingAsCompleted(MeetingModel meeting, AlertDialog alertDialog) {
        db.collection("meetings").document(meeting.getTitle())
                .update("status", "attended")
                .addOnSuccessListener(aVoid -> {
                    meeting.setStatus("attended");
                    notifyDataSetChanged();
                    Toast.makeText(context, "Meeting marked as attended", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Error updating meeting status", Toast.LENGTH_SHORT).show());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView meetingTitle, meetingDate, meetingTime, meetingEmployee, meetingStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            meetingTitle = itemView.findViewById(R.id.meetingTitle);
            meetingDate = itemView.findViewById(R.id.meetingDate);
            meetingTime = itemView.findViewById(R.id.meetingTime);
            meetingEmployee = itemView.findViewById(R.id.meetingEmployee);
            meetingStatus = itemView.findViewById(R.id.meetingStatus);
        }
    }
}
