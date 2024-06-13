package com.example.meetingsceduling;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerMeetingAdapterE extends RecyclerView.Adapter<RecyclerMeetingAdapterE.ViewHolder> {

    private Context context;
    private ArrayList<MeetingModel> meetingList;

    public RecyclerMeetingAdapterE(Context context, ArrayList<MeetingModel> meetingList) {
        this.context = context;
        this.meetingList = meetingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.meeting_card_row_e, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MeetingModel meeting = meetingList.get(position);
        holder.meetingTitle.setText(meeting.getTitle());
        holder.meetingDate.setText(meeting.getDate());
        holder.meetingTime.setText(meeting.getStartTime() + " - " + meeting.getEndTime());
        holder.meetingEmployees.setText(meeting.getEmployee());
        holder.meetingLocation.setText(meeting.getLocation());
        holder.meetingDescription.setText(meeting.getDescription());
        holder.meetingStatus.setText(meeting.getStatus());
    }

    @Override
    public int getItemCount() {
        return meetingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView meetingTitle, meetingDate, meetingTime, meetingEmployees, meetingLocation, meetingDescription, meetingStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            meetingTitle = itemView.findViewById(R.id.meetingTitle);
            meetingDate = itemView.findViewById(R.id.meetingDate);
            meetingTime = itemView.findViewById(R.id.meetingTime);
            meetingEmployees = itemView.findViewById(R.id.meetingEmployees);
            meetingLocation = itemView.findViewById(R.id.meetingLocation);
            meetingDescription = itemView.findViewById(R.id.meetingDescription);
            meetingStatus = itemView.findViewById(R.id.meetingStatus);
        }
    }
}
