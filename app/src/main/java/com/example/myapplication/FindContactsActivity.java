package com.example.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class FindContactsActivity extends AppCompatActivity {

    private Button back_btn;
    private RecyclerView uContactList;
    private RecyclerView.Adapter uContactListAdapter;
    private RecyclerView.LayoutManager uContactListLayoutManager;

    ArrayList<ContactListObject> contactList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_contacts);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });

        contactList = new ArrayList<>();

        initRecyclerView();
        getContactList();

    }

    private void getContactList() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Cursor pointer = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    null,
                    null);
            while (pointer.moveToNext()){
                String name = pointer.getString(pointer.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phone = pointer.getString(pointer.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                ContactListObject contact = new ContactListObject(name,phone);
                contactList.add(contact);
                uContactListAdapter.notifyDataSetChanged();
            }

        }

    }

    private void initRecyclerView() {
        uContactList = findViewById(R.id.contact_list);
        uContactList.setNestedScrollingEnabled(false);
        uContactList.setHasFixedSize(false);

        uContactListLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        uContactList.setLayoutManager(uContactListLayoutManager);

        uContactListAdapter = new ContactListAdapter(contactList);
        uContactList.setAdapter(uContactListAdapter);
    }
}
