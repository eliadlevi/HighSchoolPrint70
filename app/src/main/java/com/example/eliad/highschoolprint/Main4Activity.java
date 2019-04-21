package com.example.eliad.highschoolprint;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Main4Activity extends AppCompatActivity {

    String email = "";
    String stat = "the firsst value thet entered";

    String uppa = null;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference mRootRef = db.getReference("users");
    private DatabaseReference mRootRef2 = db.getReference("req");
    private DatabaseReference mRootRef3 = db.getReference("printReq");
    private DatabaseReference mRootRef4 = db.getReference("printFinal");

    private DatabaseReference mdatabase;

    private Button mLogOutBtn;

    private FirebaseAuth mAuth;

    String printStat;

    boolean a;

    private FirebaseAuth.AuthStateListener mAuthListener;

    boolean pa = false;//print approval
    boolean ua = false;//user approval
    private GoogleApiClient mGoogleApiClient;

    TextView mainStat;
    TextView mainName;
    TextView printReqStat;

    ArrayList<String> cars = new ArrayList<String>();

    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};

    Button userAp;
    Button printAp;
    Button printReq;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        mdatabase = FirebaseDatabase.getInstance().getReference();

        userAp = (Button) findViewById(R.id.userApproval);
        printAp = (Button) findViewById(R.id.printApproval);
        printReq = (Button) findViewById(R.id.printReq);
        mainStat = (TextView) findViewById(R.id.mainStat);
        mainName = (TextView) findViewById(R.id.mainName);
        printReqStat = (TextView) findViewById(R.id.printReqStat);
        mainStat.setText("");
        mainName.setText("");

        printReq.setVisibility(View.GONE);
        userAp.setVisibility(View.GONE);
        printAp.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {

                if(firebaseAuth.getCurrentUser() == null)
                {
                    startActivity(new Intent(Main4Activity.this, Main2Activity.class));
                }
            }
        };



        getEmail();

        if(checkRef3()==true)
        {
            printReqStat.setText("your printing request still on standby");
        }
        else {
            checkRef4();
        }

        mainName.setText(email);
        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                stat = dataSnapshot.child(delTen(email)).getValue(String.class);
                if(stat== null)
                {
                    mRootRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            stat = dataSnapshot.child(delTen(email)).getValue(String.class);
                            if(stat.equals("no"))
                            {
                                mainStat.setText("you'r request was terned down");
                            }
                            else
                            {
                                mainStat.setText("you'r request still on standby");

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    if(stat.equals("norm"))
                    {
                        printReq.setVisibility(View.VISIBLE);
                        mainStat.setText("normal");
                    }
                    else if(stat.equals("spmg"))
                    {
                        mainStat.setText("super manager");
                        printReq.setVisibility(View.VISIBLE);
                        userAp.setVisibility(View.VISIBLE);
                        printAp.setVisibility(View.VISIBLE);
                    }
                    else if(stat.equals("manag"))
                    {
                        mainStat.setText("manager");
                        printReq.setVisibility(View.VISIBLE);
                        printAp.setVisibility(View.VISIBLE);
                    }
//                    else if(stat.equals("printmg"))
//                    {
//                        mainStat.setText("print manager");
//                        printReq.setVisibility(View.VISIBLE);
//                        //still not sure in the difference between print manager to normal user
//                    }
                    else
                    {
                        mainStat.setText("problem");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mLogOutBtn = (Button) findViewById(R.id.logout);

        mLogOutBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mAuth.signOut();

            }
        });


        if(stat.equals("norm"))
        {
            printReq.setVisibility(View.VISIBLE);
            mainStat.setText("normal");
        }
        else if(stat.equals("spmg"))
        {
            mainStat.setText("super manager");
            userAp.setVisibility(View.VISIBLE);
            printAp.setVisibility(View.VISIBLE);
            printReq.setVisibility(View.VISIBLE);

        }
        else if(stat.equals("manag"))
        {
            mainStat.setText("manager");
            printAp.setVisibility(View.VISIBLE);
            printReq.setVisibility(View.VISIBLE);

        }
//        else if(stat.equals("printmg"))
//        {
//            mainStat.setText("print manager");
//            printReq.setVisibility(View.VISIBLE);
//            //still not sure in the difference between print manager to normal user
//        }
        else
        {
            mainStat.setText("problem");
        }



    }

    @Override
    protected void onStart()
    {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    public void printApproval(View view)
    {



        uppa ="printAp";

        Intent t=new Intent(this,MainActivity.class);

        t.putExtra("uppa",uppa);

        startActivity(t);

//        startActivity(new Intent(Main4Activity.this, MainActivity.class));

    }

    public void userApproval(View view)
    {
        uppa ="userAp";

        Intent t=new Intent(this,MainActivity.class);

        t.putExtra("uppa",uppa);

        startActivity(t);



    }


    public void  getEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
        {
            email = user.getEmail();
        }
    }

    public String delTen(String a)
    {

        String[] parts = a.split("@");
        String part1 = parts[0]; // 004
        String part2 = parts[1];
//        int i=a.length();
//        a=a.substring(0 ,i-10);
        return (part1);

    }

    public void printReq(View view)
    {
//        mRootRef3.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//            {
////                cars = dataSnapshot.child(delTen(email)).getValue(ArrayList.class);
//
//                List<String> yourStringArray = dataSnapshot.child(delTen(email)).getValue(t);
//                if(yourStringArray == null || yourStringArray.isEmpty())
//                {
//                    startActivity(new Intent(Main4Activity.this, Main3Activity.class));
//
//                }
//                else{
//                    Toast.makeText(getApplicationContext(), "you already have a printing request", Toast.LENGTH_LONG).show();
//
//                    return;
//                }
//
//
//
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        if(checkRef3()==false)
        {
            Toast.makeText(getApplicationContext(), "you already have a printing request", Toast.LENGTH_LONG).show();

        }
        else{
            startActivity(new Intent(Main4Activity.this, Main3Activity.class));

        }


    }


    public boolean checkRef3 ()
    {

        mRootRef3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
//                cars = dataSnapshot.child(delTen(email)).getValue(ArrayList.class);

                List<String> yourStringArray = dataSnapshot.child(delTen(email)).getValue(t);
                if(yourStringArray == null || yourStringArray.isEmpty())
                {

                    a =true;
                }
                else{

                    a = false;
                }



            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return a;
    }

    public void checkRef4()
    {
        mRootRef4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                printStat = dataSnapshot.child(delTen(email)).getValue(String.class);
                if(printStat == null )
                {
                    printReqStat.setText(" you request steel on standby");
                }
                else if(printStat.equals("yes"))
                {
                    printReqStat.setText("you'r printing request approved and on printing process , you now can send another printing request");
                    mdatabase.child("printFinal").child(delTen(email)).removeValue();

                }
                else if(printStat.equals("no") )
                {

                    printReqStat.setText("you'r printing request rejected ,you now can send another printing request");
                    mdatabase.child("printFinal").child(delTen(email)).removeValue();

                }
                else{
                    printReqStat.setText(" you now can send printing request");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
