    package com.cu.gastosmerchant1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cu.gastosmerchant.R;
import com.cu.gastosmerchant1.Data.Userinfo;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {
    EditText ownernameview,shopnameview,Emailview,Phonenoview,addressview;
    Button next,Verify;
    CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth firebaseAuth1;
    int verify=0;
    Spinner locationspinner;
        String encoded;
    View popupdialog;
    Button verifybtn , canclebtn;
    EditText otpedittext;
    TextView resendtext;
    AlertDialog alertDialog;
    AlertDialog.Builder builder;
    String dob;

    ImageView mImageView;


    String imageresponce;
    int RESULT_LOAD_IMAGE = 1;
    Uri imageuri;

    private boolean initiate = false;
    private boolean linked = false;
    private FirebaseAuth mAuth;
    SharedPreferences sharedpreferences;
    FirebaseDatabase database  ;
    ByteArrayOutputStream byteArrayOutputStream;
    private String mVerificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = getSharedPreferences("Mypref", Context.MODE_PRIVATE);

        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        firebaseAuth1 = FirebaseAuth.getInstance();

        mImageView = findViewById(R.id.femaleImageView);



        builder = new AlertDialog.Builder(Register.this);
        alertDialog = builder.create();

        Log.w("tag",sharedpreferences.getString ("name","11")+" "+  sharedpreferences.getString ("email","") );
        Verify = findViewById(R.id.button2);
        next  =  findViewById(R.id.button5);
        ownernameview = findViewById(R.id.editText);
        shopnameview = findViewById(R.id.editText3);
        Emailview  =  findViewById(R.id.editText4);
        Phonenoview  = findViewById(R.id.editText6);
        addressview      = findViewById(R.id.editText7);
        final Userinfo userInfo = new Userinfo();
        firebaseAuth = FirebaseAuth.getInstance();
        Log.d("TAG", "onCreate: "+firebaseAuth.getUid());



        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(Intent.createChooser(i, "Select Picture"), RESULT_LOAD_IMAGE);


            }
        });




        ownernameview.setText(sharedpreferences.getString ("name","") );
        Emailview.setText(sharedpreferences.getString ("email",""));
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Location");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange ( @NonNull DataSnapshot dataSnapshot ) {
                List<String> list  = new ArrayList<>();

                for (DataSnapshot issue : dataSnapshot.getChildren()) {
                    Log.d("TAG", "Value is: " + issue.getKey().toString());
                    if(((Boolean) issue.getValue())==true){
                        list.add(issue.getKey().toString());

                    }
                    ArrayAdapter<String> arrayAdapter  = new ArrayAdapter<String>(Register.this , android.R.layout.simple_selectable_list_item,list);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    locationspinner = findViewById(R.id.spinner);
                    locationspinner.setAdapter(arrayAdapter);

                }

            }

            @Override
            public void onCancelled ( @NonNull DatabaseError databaseError ) {

            }
        });






//Comment This from here to!!!

//        Verify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sendVerificationCode(Phonenoview.getText().toString());
//
//                Log.e("Phonenoview" , "onCreate: " +Phonenoview.getText().toString() );
//
//                builder = new AlertDialog.Builder(Register.this);
//                builder.setCancelable(false);
//                LayoutInflater layoutInflater = LayoutInflater.from(Register.this);
//
//                popupdialog = layoutInflater.inflate(R.layout.verify_alert, null);
//
//
//                otpedittext = popupdialog.findViewById(R.id.editText2);
//                popupdialog.findViewById(R.id.btnverfy).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick ( View view ) {
//                        if(!otpedittext.getText().toString().trim().equals(""))
//                            verifyVerificationCode(otpedittext.getText().toString().trim());
//                        Log.e("otpedittext", "onClick: " +otpedittext.getText().toString().trim() );
//                    }
//                });
//                popupdialog.findViewById(R.id.btncancle).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        alertDialog.dismiss();
//                    }
//                });
//                popupdialog.findViewById(R.id.resendtextid).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick ( View view ) {
//                        sendVerificationCode(Phonenoview.getText().toString());
//
//                    }
//                });
//                builder.setView(popupdialog);
//
//                alertDialog = builder.create();
//                alertDialog.setTitle("Enter OTP");
//                alertDialog.show();
//
//
//            }
//        });
//
//

        //Commented Till here!!!!

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("TAG", "onClick: "+Phonenoview.getEditableText().toString().trim().length()+"  "+ linked);
                int z=0;
                if(ownernameview.getEditableText().toString().trim().equals("")){
                    ownernameview.setError("Please enter Owner name ");
                    z=1;
                }
                if(shopnameview.getEditableText().toString().trim().equals("")){
                    shopnameview.setError("Please enter Shop name or - ");
                    z=1;
                }
//                if(!linked){  //Commented This!!!
//                    Phonenoview.setError("This Phone no is linked with other account");
//                    z = 1;
//
//                }
                if(Phonenoview.getEditableText().toString().trim().equals("")){
                    Phonenoview.setError("Please enter Phone no ");
                    z=1;
                }

                else if((Phonenoview.getEditableText().toString().trim().length()<10 )){
                    Phonenoview.setError("Please enter a Valid Phone no ");
                    z=1;
                }
                else if((Phonenoview.getEditableText().toString().trim().length()>10 )) {
                    Phonenoview.setError("Please enter a Valid Phone no ");
                    z = 1;
                }
//                if(!initiate){ //Commented this here!!!
//                    Phonenoview.setError("Verify Your no");
//                    z = 1;
//
//                }
                if(addressview.getEditableText().toString().trim().equals("")){
                    z = 1;
                    addressview.setError("Please enter Address");
                }

                if(z==0){

                    final AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    // set the custom layout
                    final View customLayout = getLayoutInflater().inflate(R.layout.agreement_popup, null);
                    builder.setView(customLayout);


                    final AlertDialog dialog = builder.create();
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();



                    Button confirm_btn = customLayout.findViewById(R.id.button6);

                    confirm_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Log.d("TAG" , "onCreate: 133312"+locationspinner.getSelectedItem().toString());

                            userInfo.setOwnername(ownernameview.getEditableText().toString().trim());
                            userInfo.setShopname(shopnameview.getEditableText().toString().trim());
                            userInfo.setEmail(Emailview.getEditableText().toString().trim());
                            userInfo.setAddress(addressview.getEditableText().toString().trim());
                            userInfo.setPhoneno(Phonenoview.getEditableText().toString().trim());
                            userInfo.setLocation(locationspinner.getSelectedItem().toString());

                            if(encoded != null)
                                userInfo.setShoppic(encoded);
                            else
                                userInfo.setShoppic("00");
                            DatabaseReference myRef = database.getReference("/Merchant_data/"+firebaseAuth.getUid());
                            Log.e("uid" , "onClick: " +firebaseAuth1.getCurrentUser().getEmail()+" "+ mAuth.getUid()+" ");
                            Log.e("uid" , "onClick: " +firebaseAuth1.getUid()+" "+ mAuth.getCurrentUser().getEmail()+" ");
                            myRef.setValue(userInfo);
                            myRef = database.getReference("/Location-based/"+userInfo.getLocation()+"/"+firebaseAuth.getUid());
                            myRef.setValue(userInfo);
                            myRef = database.getReference("Merchant_search/"+userInfo.getPhoneno());
                            myRef.setValue(firebaseAuth1.getUid());
                            myRef.push();

                        }
                    });


                }
//                {
//                    Log.d("TAG" , "onCreate: 133312"+locationspinner.getSelectedItem().toString());
//
//                    userInfo.setOwnername(ownernameview.getEditableText().toString().trim());
//                    userInfo.setShopname(shopnameview.getEditableText().toString().trim());
//                    userInfo.setEmail(Emailview.getEditableText().toString().trim());
//                    userInfo.setAddress(addressview.getEditableText().toString().trim());
//                    userInfo.setPhoneno(Phonenoview.getEditableText().toString().trim());
//                    userInfo.setLocation(locationspinner.getSelectedItem().toString());
//
//                    if(encoded != null)
//                    userInfo.setShoppic(encoded);
//                    else
//                        userInfo.setShoppic("00");
//                    DatabaseReference myRef = database.getReference("/Merchant_data/"+firebaseAuth.getUid());
//                    Log.e("uid" , "onClick: " +firebaseAuth1.getCurrentUser().getEmail()+" "+ mAuth.getUid()+" ");
//                    Log.e("uid" , "onClick: " +firebaseAuth1.getUid()+" "+ mAuth.getCurrentUser().getEmail()+" ");
//                    myRef.setValue(userInfo);
//                    myRef = database.getReference("/Location-based/"+userInfo.getLocation()+"/"+firebaseAuth.getUid());
//                    myRef.setValue(userInfo);
//                    myRef = database.getReference("Merchant_search/"+userInfo.getPhoneno());
//                    myRef.setValue(firebaseAuth1.getUid());
//                    myRef.push();
//
//                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE  && resultCode == RESULT_OK) {
            imageuri = data.getData();


            CropImage.activity(imageuri)
                    .setAspectRatio(1 , 1)
                    .start(Register.this);

        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK)
            {
                Uri resulturi = result.getUri();
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver() , resulturi);

                    Bitmap converetdImage = getResizedBitmap(bitmap, 200);
                    byteArrayOutputStream = new ByteArrayOutputStream();

                    converetdImage.compress(Bitmap.CompressFormat.WEBP, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();
                    encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
                    Bitmap decodedbit = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    mImageView.setImageBitmap(converetdImage);


                    int ans = converetdImage.getByteCount();


//                    Log.e("SIZE" , String.valueOf(ans));
//                    Log.e("SIZE" , String.valueOf(encoded.length()));
//                    Log.e("SIZE" , String.valueOf(byteArrayOutputStream.toString().length()));
//                    Log.e("SIZE" , String.valueOf(decodedbit.getByteCount()));

//                    imageresponce = BitMapToString(converetdImage);




                }catch (Exception e)
                {
                    Log.e("EXCEPTION IMAGE" , e.getMessage());
                }
            }

        }

    }


    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }


    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

//Commented From here!!!


//
//    private void sendVerificationCode(String mobile) {
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                "+91" + mobile,
//                90,
//                TimeUnit.SECONDS,
//                TaskExecutors.MAIN_THREAD,
//                mCallbacks);
//        Log.e(   "VerificationCompleted:","testing"  );
//
//    }


//
//    //the callback to detect the verification status
//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//        @Override
//        public void onVerificationCompleted( PhoneAuthCredential phoneAuthCredential) {
//
//            //Getting the code sent by SMS
//            String code = phoneAuthCredential.getSmsCode();
//
//            //sometime the code is not detected automatically
//            //in this case the code will be null
//            //so user has to manually enter the code
//            if (code != null) {
//
//                Log.e(   "VerificationCompleted:",code  );
//                otpedittext.setText(code);
////                editTextCode.setText(code);
//                //verifying the code
//                verifyVerificationCode(code);
//            }
//        }
//
//        @Override
//        public void onVerificationFailed( FirebaseException e) {
//            Log.e(   "VerificationCompleted:",e.getMessage()  );
//        }
//
//        @Override
//        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//            super.onCodeSent(s, forceResendingToken);
//            Log.e(     "onCodeSent: ",s+forceResendingToken.toString() );
//            //storing the verification id that is sent to the user
//            mVerificationId = s;
//        }
//    };
//
//
//    private void verifyVerificationCode(String code) {
//        try{
//            //creating the credential
//            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code );
//            //signing the user
//            signInWithPhoneAuthCredential(credential);
//
//        }catch (Exception  e){
//            Log.e("error" , "verifyVerificationCode: " +e.getMessage() );
//        }
//    }

//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//
//
//
//
//        mAuth.getCurrentUser().linkWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            linked = true;
//                            Log.d("TAG", "linkWithCredential:success");
//                            alertDialog.cancel();
//                            initiate =  true;
//                            if (linked)
//                                Phonenoview.setEnabled(false);
//                            Toast.makeText(getBaseContext(),"Verified   ",Toast.LENGTH_SHORT).show();
//
//                        } else {
//                            Log.w("TAG", "linkWithCredential:failure", task.getException());
//                            Toast.makeText(Register.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            linked=false;
////                            updateUI(null);
//
//
//
//                            String message = " ";
//
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                message = "Invalid code entered...";
//                            }
//                            Toast.makeText(Register.this, message+"  "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//
//                        }
//
//                        // ...
//                    }
//                });
//
//    }

    //Commneted till here!!!!!


    @Override
    public void onBackPressed() {
        try {
            if (alertDialog.isShowing())
                alertDialog.dismiss();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        firebaseAuth = FirebaseAuth.getInstance();

//        firebaseAuth.getCurrentUser().unlink(firebaseAuth.getCurrentUser().getProviderId())
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Log.w("unlink" , "onComplete: " +"done" );
//                        }
//                        Log.w("unlink" , "onComplete: " +"failed"+task.getException().getMessage());
//                    }
//                });

//        try{
//         firebaseAuth.getCurrentUser().unlink(mVerificationId);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//







        callbackManager = CallbackManager.Factory.create();
        mGoogleSignInClient = GoogleSignIn.getClient(this,  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).build());
        mGoogleSignInClient.signOut();
        firebaseAuth.signOut();
        LoginManager.getInstance().logOut();
        finish();

        super.onBackPressed();
    }


//    @Override
//    protected void onPause() {
//        super.onPause();
//        try {
//            if (alertDialog.isShowing())
//                alertDialog.dismiss();
//        }
//        catch (NullPointerException e){
//            e.printStackTrace();
//        }
//        firebaseAuth = FirebaseAuth.getInstance();
//        callbackManager = CallbackManager.Factory.create();
//        mGoogleSignInClient = GoogleSignIn.getClient(this,  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id)).build());
//        mGoogleSignInClient.signOut();
//
//        firebaseAuth.signOut();
//        LoginManager.getInstance().logOut();
//finish();
//    }


}

