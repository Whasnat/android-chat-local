package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class FindContactsActivity extends AppCompatActivity {

    private RecyclerView uContactList;
    private RecyclerView.Adapter uContactListAdapter;
    private RecyclerView.LayoutManager uContactListLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_contacts);

        initRecyclerView();
    }

    private void initRecyclerView() {
        uContactList = findViewById(R.id.contact_list);
        uContactList.setNestedScrollingEnabled(false);
        uContactList.setHasFixedSize(false);
        uContactListLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        uContactList.setLayoutManager(uContactListLayoutManager);

        uContactListAdapter = new ContactListAdapter(uContactList);
        uContactList.setAdapter(uContactListAdapter);
    }
}
