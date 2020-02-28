package com.example.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FindContactsActivity extends AppCompatActivity {

    private Button back_btn;
    private RecyclerView uContactList;
    private RecyclerView.Adapter uContactListAdapter;
    private RecyclerView.LayoutManager uContactListLayoutManager;

    ArrayList<ContactListObject> userList, contactList;


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
        userList = new ArrayList<>();

        initRecyclerView();
        getContactList();

    }

    private void getContactList() {

        String ISOPrefix = getCountryISO();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Cursor pointer = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    null,
                    null);
            while (pointer.moveToNext()) {
                String name = pointer.getString(pointer.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phone = pointer.getString(pointer.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                phone = phone.replace(" ", "");
                phone = phone.replace(")", "");
                phone = phone.replace("(", "");
                phone = phone.replace("-", "");

                if (!String.valueOf(phone.charAt(0)).equals("+"))
                    phone = ISOPrefix + phone;

                ContactListObject contact = new ContactListObject(name, phone);
                contactList.add(contact);
                //   uContactListAdapter.notifyDataSetChanged();
                getContactDetails(contact);
            }

        }

    }

    private void getContactDetails(ContactListObject contact) {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("user");
        Query query = userDb.orderByChild("phone").equalTo(contact.getPhone());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String phone = "",
                            name = "";
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        if (childSnapshot.child("phone").getValue() != null)
                            phone = childSnapshot.child("phone").getValue().toString();
                        if (childSnapshot.child("name").getValue() != null)
                            name = childSnapshot.child("phone").getValue().toString();

                        ContactListObject user = new ContactListObject(name, phone);
                        userList.add(user);
                        uContactListAdapter.notifyDataSetChanged();
                        return;


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    // GET THE COUNTRY ISO
    private String getCountryISO() {
        String iso = null;
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);

        if (telephonyManager.getNetworkCountryIso() != null)
            if (!telephonyManager.getNetworkCountryIso().equals(""))
                iso = telephonyManager.getNetworkCountryIso();

        return CountryISOPhone.getPhone(iso);
    }

    private void initRecyclerView() {
        uContactList = findViewById(R.id.contact_list);
        uContactList.setNestedScrollingEnabled(false);
        uContactList.setHasFixedSize(false);

        uContactListLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        uContactList.setLayoutManager(uContactListLayoutManager);

//        uContactListAdapter = new ContactListAdapter(contactList);
        uContactListAdapter = new ContactListAdapter(userList);
        uContactList.setAdapter(uContactListAdapter);
    }
}
