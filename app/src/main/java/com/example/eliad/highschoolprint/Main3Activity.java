

package com.example.eliad.highschoolprint;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Main3Activity extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE_REQUEST = 234 ;
    ArrayList<String> alist = new ArrayList<String>();// keep all the names thet un the data base
    String Stxt;// string to convert to txt file
    String filename;
    String str;
    private Uri filePath;

    Button selectFile , upload;
    TextView notification;

    FirebaseStorage storge;
    FirebaseDatabase database;

    private Button btchose,btupload;
    private ImageView imageView;


    AlertDialog.Builder adb;

    String date;
    int amount;
    String res;

    boolean t = false;

    EditText etamount ;
    EditText etres;

    CalendarView calendarView;


    private DatabaseReference mDatabase ;

    String rand = null;

    boolean e = false;//if the user dont need manager approval

    String Cdate;
    private StorageReference storageReference;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        Calendar c = Calendar.getInstance();
        int Cyear = c.get(Calendar.YEAR);
        int Cmonth = c.get(Calendar.MONTH);
        int Cday = c.get(Calendar.DAY_OF_MONTH);
        Cdate = Cday + "."+Cmonth+ "."+Cyear;


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        email=delTen(email);

        imageView = (ImageView) findViewById(R.id.ImageView);
        btchose = (Button) findViewById(R.id.chose);
        btupload = (Button) findViewById(R.id.upload);
        etamount = (EditText) findViewById(R.id.amount);
        etres = (EditText) findViewById(R.id.res);
        calendarView = (CalendarView) findViewById(R.id.calendarView);


        storageReference = FirebaseStorage.getInstance().getReference();


        btchose.setOnClickListener(this);
        btupload.setOnClickListener(this);


        CalendarView();



    }


    public void CalendarView()
    {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                Cdate = dayOfMonth + "." + month+ "." +year ;

                Calendar c = Calendar.getInstance();
                int Cyear = c.get(Calendar.YEAR);
                int Cmonth = c.get(Calendar.MONTH);
                int Cday = c.get(Calendar.DAY_OF_MONTH);
                if (Cyear > year) {
                    Toast.makeText(getApplicationContext(), "the date can't by in the past plase select a legitimate date", Toast.LENGTH_LONG).show();
                    return;
                }
                if (Cyear < year) {
                    t = true;
                    e =true;
                    return;

                }
                if (Cmonth > month && t == false) {
                    Toast.makeText(getApplicationContext(), "the date can't by in the past plase select a legitimate date", Toast.LENGTH_LONG).show();
                    return;
                }
                if (Cmonth < month && t == false) {
                    t = true;
                    e = true;
                    return;

                }
                if (Cday > dayOfMonth && t == false) {
                    Toast.makeText(getApplicationContext(), "the date can't by in the past plase select a legitimate date", Toast.LENGTH_LONG).show();
                    return;
                }
                if((Cday+5)<dayOfMonth)
                {
                    t =true;
                    e = true;
                    return;

                }



            }
        });
    }


    @Override
    public void onClick(View view)
    {
        if (view == btchose)
        {
            showFileChooser();

        } else if (view == btupload)
        {
            uploadFile();
        }
    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST  &&  resultCode == RESULT_OK &&  data != null  && data.getData() != null)
        {
            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                 imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    private void showFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select an image"),PICK_IMAGE_REQUEST);
    }

    private  void uploadFile() {

        CalendarView();

        try {
            amount = Integer.parseInt(etamount.getText().toString());
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "you didn't write amount", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            if(amount > 40 || t==false)
                res = etres.getText().toString();
            else{
                e=true;
            }
            if (res.isEmpty()) {
                Toast.makeText(getApplicationContext(), "you didn't write a reason", Toast.LENGTH_LONG).show();
                return;
            }
            else {
                t = true;
            }
        } catch (Exception e) {
            if(t == false)
            {
                Toast.makeText(getApplicationContext(), "you didn't write a reason", Toast.LENGTH_LONG).show();
                return;
            }

        }






        if (t == true && filePath != null) {

            rand = UUID.randomUUID().toString().replace("-", "1xe");
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("uploading...");
            progressDialog.show();

            StorageReference riversRef = storageReference.child("images/printReq").child(rand+".pdf");

            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content

                            progressDialog.dismiss();

                            Toast.makeText(getApplicationContext(), "file uploaded", Toast.LENGTH_LONG).show();


                            Task<Uri> u = taskSnapshot.getMetadata().getReference().getDownloadUrl();


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();

                            Toast.makeText(getApplicationContext(), "you got an error", Toast.LENGTH_LONG).show();

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage(((int) progress) + "% uploaded...");
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "thar is some error in uploading the file", Toast.LENGTH_LONG).show();
            return;
        }

        ArrayList<String> cars = new ArrayList<String>();


        cars.add(rand);
        cars.add(Cdate);
        cars.add(String.valueOf(amount));
        cars.add(res);

        if(e == true)
        {
            cars.add(email);
            mDatabase.child("printing").child("email"+email+"fileName"+rand).setValue(cars);
        }
        else{
            mDatabase.child("printReq").child(email).setValue(cars);
        }

        Toast.makeText(getApplicationContext(), "your printing request has been transfered to the manager", Toast.LENGTH_LONG).show();

        startActivity(new Intent(Main3Activity.this, Main4Activity.class));

    }

    public String delTen(String a)// split the email string in the @ and return the username so thet he can be entered as a root in the db
    {

        String[] parts = a.split("@");
        String part1 = parts[0]; // 004
        String part2 = parts[1];
//        int i=a.length();
//        a=a.substring(0 ,i-10);
        return (part1);

    }


//        storge = FirebaseStorage.getInstance();
//        database = FirebaseDatabase.getInstance();
//
//        selectFile.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public  void onClick(View view)
//                {
//                    if(ContextCompat.checkSelfPermission(Main3Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
//                    {
//                        selectPdf();
//                    }
//                    else{
//                        ActivityCompat.requestPermissions(Main3Activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
//                    }
//                }
//            });
//
//        }
//
//       @Override
//        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//            if(requestCode==9&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
//            {
//                selectPdf();
//            }
//            else
//                Toast.makeText(Main3Activity.this,"plese provide permission...", Toast.LENGTH_LONG).show();
//        }
//
//        private  void selectPdf()//select file using file manager
//        {
//
//
//            Intent intent = new Intent();
//            Intent.setType("application/pdf");
//        }
//
//
    public void back2(View view)
    {
        Intent t = new Intent(this, Main4Activity.class);
        startActivity(t);
    }



   /* public void enter_storge(View view) throws IOException {
        String filename = UUID.randomUUID().toString() + ".txt";
        EditText edtxt = (EditText) findViewById(R.id.etStorge);
        Stxt = edtxt.getText().toString();
        FileOutputStream outputStream = null;
        File file = new File(filename);
        try {
            outputStream = openFileOutput(filename, MODE_PRIVATE);
            outputStream.write(Stxt.getBytes());
        } catch (IOException e) {
            System.out.println("worked!");
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FirebaseStorage storage;
        StorageReference storageReference;

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        String path="file:///data/data/com.example.eliad.highschoolprint/files/"+filename;
        filePath=Uri.parse(path);

        StorageReference ref = storageReference.child("files/"+ filename);
        ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Main3Activity.this, "Uploaded", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Main3Activity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                    }
                });;

    }
*/
}

