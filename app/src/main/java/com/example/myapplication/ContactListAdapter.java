package com.example.myapplication;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactListViewHolder> {

    ArrayList<ContactListObject> contactList;

    public ContactListAdapter(ArrayList<ContactListObject> contactList) {
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    //ViewHolder Class
    public class ContactListViewHolder extends RecyclerView.ViewHolder{

        public ContactListViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
