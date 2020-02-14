package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;



public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactListViewHolder> {

    ArrayList<ContactListObject> contactList;

    public ContactListAdapter(ArrayList<ContactListObject> contactList) {
        this.contactList = contactList;
    }


    //When THe ViewHolder is Created
    @NonNull
    @Override
    public ContactListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,
                null,
                false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(layoutParams);
        ContactListViewHolder contactListViewHolder = new ContactListViewHolder(layoutView);
        return contactListViewHolder;
    }

    //Bind the contact_items in the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ContactListViewHolder holder, int position) {
        holder.contact_name.setText(contactList.get(position).getName());
        holder.contact_phone.setText(contactList.get(position).getPhone());
    }

    //Get the number of contacts
    @Override
    public int getItemCount() {
        return contactList.size();
    }


    //ViewHolder Class
    public class ContactListViewHolder extends RecyclerView.ViewHolder{
        private TextView contact_name, contact_phone;
        public ContactListViewHolder(@NonNull View itemView) {
            super(itemView);
            contact_name = itemView.findViewById(R.id.contact_name);
            contact_phone = itemView.findViewById(R.id.contact_phone);
        }
    }
}
