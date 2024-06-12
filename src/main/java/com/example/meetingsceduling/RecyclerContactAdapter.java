package com.example.meetingsceduling;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerContactAdapter extends RecyclerView.Adapter<RecyclerContactAdapter.ViewHolder> {


    Context context;
    ArrayList<ContactModel> arrcontacts;
    RecyclerContactAdapter(Context context1, ArrayList<ContactModel> arrcontact){
        context = context1;
        arrcontacts = arrcontact;
    }

    public void setFilteredList(ArrayList<ContactModel> filteredList){
        this.arrcontacts = filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.contact_row, parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.contactImage.setImageResource(arrcontacts.get(position).img);
        holder.ContactName.setText(arrcontacts.get(position).name);
        holder.ContactNumber.setText(arrcontacts.get(position).number);

    }

    @Override
    public int getItemCount() {
        return arrcontacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ContactName, ContactNumber;
        ImageView contactImage;
        public ViewHolder(View itemView){
            super(itemView);

            ContactName = itemView.findViewById(R.id.text1);
            ContactNumber = itemView.findViewById(R.id.text2);
            contactImage = itemView.findViewById(R.id.contact_Image);
        }
    }
}