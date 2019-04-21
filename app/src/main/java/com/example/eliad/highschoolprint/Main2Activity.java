package com.example.eliad.highschoolprint;


import android.content.DialogInterface;
import android.content.Intent;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Main2Activity extends AppCompatActivity {

    private SignInButton mGoogleBtn;

    private static final int RC_SIGN_IN = 1;


    boolean ge = false;
    String checkstat = "";
    String email = "";
    String root = "";
    AlertDialog.Builder adb;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference mRootRef = db.getReference("users");
    private DatabaseReference mRootRef2 = db.getReference("req");
    private DatabaseReference mDatabase ;




    private String gm = "@gmail.com";//keep the last part of the mail so you can add it after delTen

    String signSt = "";




    private GoogleApiClient  mGoogleApiClient;

    private FirebaseAuth mAuth;

    private static final String TAG = "MAIN_ACTIVITY";

    RadioButton manag ;
    RadioButton spmg ;
    RadioButton norm ;
//    RadioButton printmg ;

//    Button sign;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String x = "";
    boolean rd = false;//to know if the user in sign in mode or sign up

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        manag = (RadioButton) findViewById(R.id.manag);
        norm = (RadioButton) findViewById(R.id.norm);
        spmg = (RadioButton) findViewById(R.id.supManag);
 //       printmg = (RadioButton) findViewById(R.id.printManag);
//        sign = (Button) findViewById(R.id.sign);
        mAuth = FirebaseAuth.getInstance();
        mGoogleBtn = (SignInButton) findViewById(R.id.googleBtn);


        mGoogleBtn.setVisibility(View.VISIBLE);
        manag.setVisibility(View.VISIBLE);
        spmg.setVisibility(View.VISIBLE);
        norm.setVisibility(View.VISIBLE);
   //     printmg.setVisibility(View.VISIBLE);
    //    sign.setText("sign in");


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser()!= null)
                {

                    mGoogleBtn.setVisibility(View.GONE);
                    // startActivity(new Intent(Main2Activity.this, Main4Activity.class));
                    getEmail();



                }
            }
        };




// Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("773768488219-nnunkd06paebu2apphrn36nj08dsm0ru.apps.googleusercontent.com")
                .requestEmail()
                .build();



        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener()
                {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
                    {
                        Toast.makeText(getApplicationContext(), "you got an error", Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        mGoogleBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                signIn();

            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);


    }

    private void signIn()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        mGoogleBtn.setVisibility(View.GONE);
        getEmail();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess())
            {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            }
            else{

            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }

                        // ...
                    }
                });
    }



    public void next2(View view)
    {
        getEmail();

        if(ge == true)
        {

            Intent intent = new Intent(Main2Activity.this, Main4Activity.class);

            startActivity(intent);
        }
        else if (ge == false)
        {

            Toast.makeText(getApplicationContext(), "you can't move to the next activity", Toast.LENGTH_LONG).show();

        }
    }



    public void onCheckboxClicked(View view)
    {
        if(norm.isChecked())
        {
            checkstat = "norm";
        }
        else{
            if(spmg.isChecked())
            {
                checkstat = "spmg";
            }
            else{
                if(manag.isChecked())
                {
                    checkstat = "manag";
                }
                else{
                    Toast.makeText(getApplicationContext(), "you got an error", Toast.LENGTH_LONG).show();
//                    if(printmg.isChecked())
//                    {
//                        checkstat = "printmg";
//                    }
//                    else{
//                        Toast.makeText(getApplicationContext(), "you got an error", Toast.LENGTH_LONG).show();
//                    }
                }
            }
        }


    }


    public void  getEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null)
        {
            email = user.getEmail();

            enterdb();
        }
    }



    public void enterdb()//check if the email is empty and if not it send it to a action thet split the email in the @ so thet the email can be entered as a root in the db
    {

        adb = new AlertDialog.Builder(this);
        if (email.isEmpty())
        {
            adb.setMessage("error with geting the email");
            return;

        }
        else
        {
            email= delTen(email);
            cheackIfRighet();
            return;
        }

    }

    public void cheackIfRighet()//the purpose of this action is to check that you are registered and to check if you can or already did chose a status
    {

//        signSt = sign.getText().toString();
//        if(signSt.equals("sign up"))
//        {
//            mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//                {
//                    x = dataSnapshot.child(email).getValue(String.class);
//                    if(x == null)
//                    {
//                        mRootRef2.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//                            {
//                                x = dataSnapshot.child(email).getValue(String.class);
//                                if(x == null)
//                                {
//                                    Toast.makeText(getApplicationContext(), "you are not register", Toast.LENGTH_LONG).show();
//                                    ge = false;
//
//                                    manag.setVisibility(View.VISIBLE);
//                                    spmg.setVisibility(View.VISIBLE);
//                                    norm.setVisibility(View.VISIBLE);
//                         //           printmg.setVisibility(View.VISIBLE);
//             //                       sign.setText("sign up");
//
//                                }
//                                else {
//                                    ge = true;
//                                    startActivity(new Intent(Main2Activity.this, Main4Activity.class));
//
//
//                                }
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//                    else{
//                        ge = true;
//                        Toast.makeText(getApplicationContext(), "you are register", Toast.LENGTH_LONG).show();
//                        startActivity(new Intent(Main2Activity.this, Main4Activity.class));
//
//
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//
////        }
////        if(signSt.equals("sign in"))
////        {
//


            mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    x = dataSnapshot.child(email).getValue(String.class);

                    if(x == null)
                    {

                        mRootRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                x = dataSnapshot.child(email).getValue(String.class);
                                if(x != null)
                                {
                                    Toast.makeText(getApplicationContext(), "you already chose status", Toast.LENGTH_LONG).show();
                                    ge = true;

//                                    manag.setVisibility(View.GONE);
//                                    spmg.setVisibility(View.GONE) ;
//                                    norm.setVisibility(View.GONE) ;
//                        //            printmg.setVisibility(View.GONE) ;
//                                    sign.setVisibility(View.GONE);
                                    startActivity(new Intent(Main2Activity.this, Main4Activity.class));

                                }
                                else{
                                    if (checkstat.isEmpty())
                                    {
                                        Toast.makeText(getApplicationContext(), "you didn't checked no box", Toast.LENGTH_LONG).show();
                                    }else {

                                        ge = true;
                                        mDatabase.child("req").child(email).setValue(checkstat);

                                        Toast.makeText(getApplicationContext(), "uploaded", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(Main2Activity.this, Main4Activity.class));
                                    }

                                }



                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        ge =true;
                        Toast.makeText(getApplicationContext(), "you already sign up", Toast.LENGTH_LONG).show();

//                        manag.setVisibility(View.GONE);
//                        spmg.setVisibility(View.GONE) ;
//                        norm.setVisibility(View.GONE) ;
//               //         printmg.setVisibility(View.GONE) ;
//                        //    sign.setText("sign up");
//                        sign.setVisibility(View.GONE);
                        startActivity(new Intent(Main2Activity.this, Main4Activity.class));


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
//    }

    public String delTen(String a)// split the email string in the @ and return the username so thet he can be entered as a root in the db
    {

        String[] parts = a.split("@");
        String part1 = parts[0]; // 004
        String part2 = parts[1];
//        int i=a.length();
//        a=a.substring(0 ,i-10);
        return (part1);

    }

//    public void sign(View view)//the purpose of this action is to change according to what the user choice
//    {
//        signSt = sign.getText().toString();
//        if(signSt.equals("sign up"))
//        {
//            manag.setVisibility(View.VISIBLE);
//            spmg.setVisibility(View.VISIBLE);
//            norm.setVisibility(View.VISIBLE);
//         //   printmg.setVisibility(View.VISIBLE);
//            sign.setText("sign in");
//        }
//        if(signSt.equals("sign in"))
//        {
//
//            manag.setVisibility(View.GONE);
//            spmg.setVisibility(View.GONE) ;
//            norm.setVisibility(View.GONE) ;
//        //    printmg.setVisibility(View.GONE) ;
//            sign.setText("sign up");
//        }
//    }
}



