package com.example.eliad.highschoolprint;

import android.app.Dialog;
import android.provider.ContactsContract;
import android.renderscript.Sampler;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.ValueEventListener;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity implements ValueEventListener {


    ListView listViewUsers;
    DatabaseReference databaseUsers;

    String t = "";
    String t2 = "";
    String filename;
    String r = "";

    private DatabaseReference myRef;
    private FirebaseDatabase database;

    //String [] foods;
    boolean vis = false;

    ArrayList<String> foods = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    AlertDialog.Builder adb;
    String tdb;//text data base
    ArrayList<String> alist = new ArrayList<String>();// keep all the names thet un the data base



    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference mRootRef = db.getReference("req");
    private DatabaseReference mRootRef2 = db.getReference("req");
    private DatabaseReference mRootRef3 = db.getReference("printReq");

    private DatabaseReference mdatabase;
    private DatabaseReference mStringRef = mRootRef.push();

    RadioButton reject;
    RadioButton standby;
    RadioButton approv;
    Button button;
    TextView tv1;//name
    TextView tv2;//user aprov requast or print approv date
    TextView tv3;//user aprov or print approv
    TextView tv4;//print approv resone
    TextView tv5;// amount

    ArrayList<String> cars = new ArrayList<String>();


    ImageView imageView;

    String uppa;

    String gm = "@gmail.com";
    String rdState = "";

    String Cdate;
    String amount;
    String res;

    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListAdapter usersAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, foods);
        ListView usersListView = (ListView) findViewById(R.id.listView);
        usersListView.setAdapter(usersAdapter);

        mdatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        uppa=intent.getStringExtra("uppa");


        button = (Button) findViewById(R.id.button);
        tv1 = (TextView) findViewById(R.id.name);
        tv2 = (TextView) findViewById(R.id.reqState);
        tv3 = (TextView) findViewById(R.id.uppa);
        tv4 = (TextView) findViewById(R.id.res);
        tv5 = (TextView) findViewById(R.id.amount);
        imageView = (ImageView) findViewById(R.id.ImageView);
        reject = (RadioButton) findViewById(R.id.reject);
        standby = (RadioButton) findViewById(R.id.standby);
        approv = (RadioButton) findViewById(R.id.approv);
        reject.setVisibility(View.GONE);
        standby.setVisibility(View.GONE);
        approv.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        tv1.setText("");
        tv2.setText("");
        tv3.setText("");
        tv4.setText("");
        tv5.setText("");


        if(uppa.equals("userAp"))
        {
            mRootRef = mRootRef2;

        }
        else if(uppa.equals("printAp"))
        {
            mRootRef = mRootRef3;

        }
        else {
            startActivity(new Intent(MainActivity.this, Main4Activity.class));
        }

        usersListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                        vis = true;
                        final String food = valueOf(parent.getItemAtPosition(position));

                        button.setVisibility(View.VISIBLE);
                        reject.setVisibility(View.VISIBLE);
                        standby.setVisibility(View.VISIBLE);
                        approv.setVisibility(View.VISIBLE);
                        tv1.setText(food);

                        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(uppa.equals("userAp"))
                                {
                                    r= dataSnapshot.child(delTen(food)).getValue(String.class);
                                    if(  r.equals("spmg"))
                                    {
                                        r = "super manager";
                                    }
                                    else if(  r.equals("manag"))
                                    {
                                        r = "manager";
                                    }
                                    else if(  r.equals("norm"))
                                    {
                                        r = "normal";
                                    }
                                    else
                                    {
                                        r = "problem";
                                    }
                                    tv2.setText(r);


                                }
                                else if(uppa.equals("printAp"))
                                {
                                    email = delTen(food);
                                    filename = dataSnapshot.child(email).child("0").getValue(String.class);
                                    Cdate = dataSnapshot.child(email).child("1").getValue(String.class);
                                    amount = dataSnapshot.child(email).child("2").getValue(String.class) ;
                                    res = dataSnapshot.child(email).child("3").getValue(String.class) ;
                                    tv2.setText("date:"+Cdate);
                                    tv5.setText("amount:"+amount);
                                    if(res != null)
                                    {
                                        tv4.setText("reason:"+res);
                                    }


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
        );
    }



    public void updateList() {
        ListView lv = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> test = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alist);
        lv.setAdapter(test);
    }



    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        if (dataSnapshot.getChildren() != null) {
            alist.clear();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                String key = snapshot.getKey();
                alist.add(key + gm);
                // alist.add(snapshot.getValue(String.class));
            }
            updateList();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mRootRef.addValueEventListener(this);
    }

    public String delTen(String a) {
        String[] parts = a.split("@");
        String part1 = parts[0]; // 004
        String part2 = parts[1];

        return (part1);

    }

    public void finish(View view)
    {


        cars.add(filename);
        cars.add(Cdate);
        cars.add(amount);
        cars.add(res);
        cars.add(email);


        AlertDialog ab;
        adb = new AlertDialog.Builder(this);


        if(vis == true)
        {
            if (rdState.isEmpty())
            {
                Toast.makeText(getApplicationContext(), "you did not chose a state", Toast.LENGTH_LONG).show();

            }else {

                t = valueOf(tv1.getText());
                t2 = valueOf(tv2.getText());

                if(t2.equals("super manager"))
                {
                    t2 = "spmg";
                }
                else if(t2.equals("manager"))
                {
                    t2 = "manag";
                }
                else if (t2.equals("normal"))
                {
                    t2 = "norm";
                }

                ab = adb.create();
                if(rdState.equals("reject"))
                {
                    if(uppa.equals("userAp"))
                    {
                        adb.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                mdatabase.child("req").child(delTen(t)).removeValue();


                                /////////////remove the user ///////////

                                reject.setVisibility(View.GONE);
                                standby.setVisibility(View.GONE);
                                approv.setVisibility(View.GONE);
                                button.setVisibility(View.GONE);
                                tv1.setText("");
                                tv2.setText("");
                                tv3.setText("");
                                tv4.setText("");
                                tv5.setText("");



                            }
                        });
                        adb.setMessage("you choosed to reject this user");

                    }
                    else if(uppa.equals("printAp"))
                    {


                        adb.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {



                                mdatabase.child("printFinal").child(delTen(t)).setValue("no");
                                mdatabase.child("printReq").child(delTen(t)).removeValue();
                                reject.setVisibility(View.GONE);
                                standby.setVisibility(View.GONE);
                                approv.setVisibility(View.GONE);
                                button.setVisibility(View.GONE);
                                tv1.setText("");
                                tv2.setText("");
                                tv3.setText("");
                                tv4.setText("");
                                tv5.setText("");



                            }
                        });
                        adb.setMessage("you choosed to reject this user printing request");

                    }

                    adb.show();


                }else if(rdState.equals("approv"))
                {

                    if(uppa.equals("userAp"))
                    {
                        adb.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {


                                mdatabase.child("users").child(delTen(t)).setValue(t2);
                                mdatabase.child("req").child(delTen(t)).removeValue();
                                reject.setVisibility(View.GONE);
                                standby.setVisibility(View.GONE);
                                approv.setVisibility(View.GONE);
                                button.setVisibility(View.GONE);
                                tv1.setText("");
                                tv2.setText("");
                                tv3.setText("");
                                tv4.setText("");
                                tv5.setText("");

                            }
                        });
                        adb.setMessage("you choosed to approv this user");

                    }
                    else if(uppa.equals("printAp"))
                    {
                        adb.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {



                                mdatabase.child("printing").child("email"+email+"fileName"+filename).setValue(cars);

                                mdatabase.child("printFinal").child(delTen(t)).setValue("yes");
                                mdatabase.child("printReq").child(delTen(t)).removeValue();
                                reject.setVisibility(View.GONE);
                                standby.setVisibility(View.GONE);
                                approv.setVisibility(View.GONE);
                                button.setVisibility(View.GONE);
                                tv1.setText("");
                                tv2.setText("");
                                tv3.setText("");
                                tv4.setText("");
                                tv5.setText("");

                            }
                        });
                        adb.setMessage("you choosed to approv this printing request");

                    }
                     adb.show();



                }else if(rdState.equals("standby"))
                {


                    adb.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    adb.setMessage("you choosed to put this user request on stand by ");
                    adb.show();


                }
            }

        }else {
            Toast.makeText(getApplicationContext(), "you did not chose a user", Toast.LENGTH_LONG).show();
            return;
        }


    }

    public void RadioChanger(View view)
    {
        if (reject.isChecked())
        {
            rdState ="reject";
        } else {
            if (standby.isChecked())
            {
                rdState ="standby";
            } else {
                if (approv.isChecked())
                {
                    rdState ="approv";
                }

            }
        }
    }


    public void backToMain(View view)
    {
        startActivity(new Intent(MainActivity.this, Main4Activity.class));

    }
}
