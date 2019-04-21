package com.example.eliad.highschoolprint;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.valueOf;

public class Main5Activity extends Activity {


    private Button mLogOutBtn;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private GoogleApiClient mGoogleApiClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, foods );
//
//        listViewUsers = (ListView) findViewById(R.id.listview2);
//
//        listViewUsers.setAdapter(adapter);
//
//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference("req");
//
//
//
//        myRef.addChildEventListener(new ChildEventListener()
//        {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
//            {
//                try
//                {
//                    //Map<String, String> valueMap = (HashMap<String, String>) dataSnapshot.getValue();
//                    String value = dataSnapshot.getValue(String.class);
//                    foods.add(value);
//                    adapter.notifyDataSetChanged();
////
//                }
//                catch (Exception e)
//                {
//                    Toast.makeText(getApplicationContext(), "you got an error", Toast.LENGTH_LONG).show();
//
//
//                }
//
////
////
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//        foods= {valueOf(mDatabase.child("req").)};
//        ListAdapter usersAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, foods);
//        ListView usersListView = (ListView) findViewById(R.id.listview2);
//        usersListView.setAdapter(usersAdapter);
//
//        usersListView.setOnItemClickListener(
//                new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//                    {
//                        String food = valueOf(parent.getItemAtPosition(position));
//                        Toast.makeText(getApplicationContext(), food, Toast.LENGTH_LONG).show();
//
//                    }
//                }
//        );
    }




//    private void setdata(DataSnapshot dataSnapshot)
//    {
//        for(DataSnapshot ds: dataSnapshot.getChildren())
//        {
//           foods =String[].class.cast(ds.child("req").getChildren());
//        }
//    }



}
