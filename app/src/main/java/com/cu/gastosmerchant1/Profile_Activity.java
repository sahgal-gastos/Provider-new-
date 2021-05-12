package com.cu.gastosmerchant1;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.cu.gastosmerchant.R;
import com.cu.gastosmerchant1.Data.Userinfo;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Profile_Activity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference refphotoerence;
    CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    boolean isDetailincomplete=false;
    boolean paytmvisible=true;
    String encoded;
    ByteArrayOutputStream byteArrayOutputStream;
    boolean upivisible=true;
    int RESULT_LOAD_IMAGE = 1;
    Uri imageuri1;

    // provider details card
    TextView txt1, txt2, txt3, txt4, txt5, txt6, txt7;
    TextView userName, userEmail, UserContact, UserShopName, UserShopAddress, UserLocation;
    ImageView UserMap;
    ImageView img1;
    CardView mpro_details_card;


    // accounts details card
    TextView txt11, txt22;
    TextView paytm,upi2;
    CardView mAccountDetailsCard;
    ImageView img2;
    ImageView imageView;

    ImageView EditAccounts;

    Double Latitude;
    Double Longitude;
    ImageView but4;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        but4=(ImageView)findViewById(R.id.buttoniamge);

        firebaseAuth = FirebaseAuth.getInstance();
        final Userinfo userInfo = new Userinfo();

            Intent intent = getIntent();

        isDetailincomplete=intent.getBooleanExtra("account",false);
//        Toast.makeText(this,"isDetailincomplete"+isDetailincomplete,Toast.LENGTH_SHORT).show();

        mpro_details_card = findViewById(R.id.cardView4);
        img1 = findViewById(R.id.arrow_id1);
        txt1 = findViewById(R.id.textView7);
        imageView=(ImageView)findViewById(R.id.imageView2);
        txt2 = findViewById(R.id.textView6);
        txt3 = findViewById(R.id.textView8);
        txt4 = findViewById(R.id.textView9);
        txt5 = findViewById(R.id.textView10);
        txt6 = findViewById(R.id.textView11);
        txt7 = findViewById(R.id.textView14);
        txt22 = findViewById(R.id.textView22);
        txt11 = findViewById(R.id.textView16);

        userName = ((TextView)findViewById(R.id.username));
        userEmail = ((TextView)findViewById(R.id.useremail));
        UserContact = ((TextView)findViewById(R.id.userphoneno));
        UserShopName =  ((TextView)findViewById(R.id.usershop));
        UserShopAddress = ((TextView)findViewById(R.id.useraddress));
        UserLocation = ((TextView)findViewById(R.id.userlocation));
        UserMap = findViewById(R.id.map_id);
        upi2 = findViewById(R.id.upi_id2);
        paytm = findViewById(R.id.paytm_wallet_no_id);
        EditAccounts = findViewById(R.id.edit_accounts);

        findViewById(R.id.rateus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });


        database=  FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Merchant_data/"+firebaseAuth.getUid());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange ( @NonNull DataSnapshot dataSnapshot ) {

                    Userinfo info = dataSnapshot.getValue(Userinfo.class);
                    userName.setText(info.getOwnername());
                    UserShopAddress.setText(info.getAddress());
                    userEmail.setText(info.getEmail());
                    UserLocation.setText(info.getLocation());
                    UserContact.setText(info.getPhoneno());
                   UserShopName.setText(info.getShopname());
                    if(info.getShoppic().length()>=10) {
                        byte[] decodedString = Base64.decode(info.getShoppic(), Base64.DEFAULT);
                        Bitmap decodedbit = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        ((ImageView) findViewById(R.id.imageView2)).setImageBitmap(decodedbit);
                    }

                    if(info.getData()!=null){
                   if (!info.getData().getUpi().equals(""))
                    upi2.setText(info.getData().getUpi()+"");
                   else{
                       upi2.setVisibility(View.INVISIBLE);
                       txt22.setVisibility(View.INVISIBLE);
                       upivisible = false;
                   }
                     if (!info.getData().getPaytm().equals(""))
                    paytm.setText(info.getData().getPaytm()+"");
                    else{
                        paytmvisible = false;
                        paytm.setVisibility(View.INVISIBLE);
                        txt11.setVisibility(View.INVISIBLE);
                         Log.e("TAG", "onDataChange: "+"INVISIBLE" );
                    }

                    Latitude = info.getData().getLatitude();
                    Longitude = info.getData().getLongitude();

                    }





                }

                @Override
                public void onCancelled ( @NonNull DatabaseError databaseError ) {

                }
            });

        findViewById(R.id.Logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick ( View view ) {






                AlertDialog.Builder builder = new AlertDialog.Builder(Profile_Activity.this);
                builder.setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                firebaseAuth = FirebaseAuth.getInstance();

                                callbackManager = CallbackManager.Factory.create();
                                mGoogleSignInClient = GoogleSignIn.getClient(Profile_Activity.this,  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestIdToken(getString(R.string.default_web_client_id)).build());
                                mGoogleSignInClient.signOut();
                                firebaseAuth.signOut();
                                LoginManager.getInstance().logOut();
                                FirebaseUser user = firebaseAuth.getCurrentUser();

                                //if user is signed in, we call a helper method to save the user details to Firebase
                                if (user == null) {
                                    // User is signed in
                                    // you could place other firebase code
                                    //logic to save the user details to Firebase
                                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });



                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Logout?");
                alert.show();










            }
        });


        //Image Concept
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(Intent.createChooser(i, "Select Picture"), RESULT_LOAD_IMAGE);
                but4.setVisibility(VISIBLE);



            }
        });

        // Ends here Image Concept





        but4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {           //IMage Editing Concept!!




                userInfo.setOwnername(userName.getText().toString());
                userInfo.setShopname(UserShopName.getText().toString());
                userInfo.setEmail(userEmail.getText().toString());
                userInfo.setAddress(UserShopAddress.getText().toString());
                userInfo.setPhoneno(UserContact.getText().toString());
                userInfo.setLocation(UserLocation.getText().toString());

                if(encoded != null)
                    userInfo.setShoppic(encoded);
                else
                    userInfo.setShoppic("00");

                mAuth = FirebaseAuth.getInstance();

                database = FirebaseDatabase.getInstance();
                refphotoerence = database.getReference("Merchant_data/" + mAuth.getUid());
//                Log.e("uid" , "onClick: " +firebaseAuth1.getCurrentUser().getEmail()+" "+ mAuth.getUid()+" ");
//                Log.e("uid" , "onClick: " +firebaseAuth1.getUid()+" "+ mAuth.getCurrentUser().getEmail()+" ");
                refphotoerence.setValue(userInfo);
                refphotoerence = database.getReference("/Location-based/"+userInfo.getLocation()+"/"+firebaseAuth.getUid());
                refphotoerence.setValue(userInfo);
                refphotoerence = database.getReference("Merchant_search/"+userInfo.getPhoneno());
                refphotoerence.setValue(mAuth.getUid());
                refphotoerence.push();
                but4.setVisibility(GONE);
            }
        });


        // Provider Details Card Expandable View
        mpro_details_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.getVisibility() == GONE){
                    userName.setVisibility(View.VISIBLE);
                    userEmail.setVisibility(View.VISIBLE);
                    UserContact.setVisibility(View.VISIBLE);
                    UserLocation.setVisibility(View.VISIBLE);
                    UserShopAddress.setVisibility(View.VISIBLE);
                    UserShopName.setVisibility(View.VISIBLE);

                    txt1.setVisibility(View.VISIBLE);
                    txt2.setVisibility(View.VISIBLE);
                    txt3.setVisibility(View.VISIBLE);
                    txt4.setVisibility(View.VISIBLE);
                    txt5.setVisibility(View.VISIBLE);
                    txt6.setVisibility(View.VISIBLE);

                    img1.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_24px));
                }else{
                    userName.setVisibility(View.GONE);
                    userEmail.setVisibility(View.GONE);
                    UserContact.setVisibility(View.GONE);
                    UserLocation.setVisibility(View.GONE);
                    UserShopAddress.setVisibility(View.GONE);
                    UserShopName.setVisibility(View.GONE);

                    txt1.setVisibility(View.GONE);
                    txt2.setVisibility(View.GONE);
                    txt3.setVisibility(View.GONE);
                    txt4.setVisibility(View.GONE);
                    txt5.setVisibility(View.GONE);
                    txt6.setVisibility(View.GONE);

                    img1.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_right_24px));

                }
            }
        });


        mAccountDetailsCard = findViewById(R.id.cardView6);
        img2 = findViewById(R.id.img_);
        if (isDetailincomplete){
            img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_warning));

        }
        mAccountDetailsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDetailincomplete){
                    Intent intent = new Intent(Profile_Activity.this,MapsActivity.class);
                    startActivity(intent);

                }
                else if(UserMap.getVisibility() == VISIBLE){

                    paytm.setVisibility(GONE);
                     txt11.setVisibility(GONE);
                    txt22.setVisibility(GONE);
                    upi2.setVisibility(GONE);
                     txt7.setVisibility(GONE);
                    UserMap.setVisibility(GONE);
                    EditAccounts.setVisibility(GONE);

                    img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_right_24px));


                }else{
                    if(paytmvisible) {
                        paytm.setVisibility(VISIBLE);
                        txt11.setVisibility(VISIBLE);
                    }
                    if(upivisible) {
                        txt22.setVisibility(VISIBLE);
                        upi2.setVisibility(VISIBLE);
                    }

                        txt7.setVisibility(VISIBLE);
                     UserMap.setVisibility(VISIBLE);

                     EditAccounts.setVisibility(VISIBLE);


                    EditAccounts.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                    Intent intent1 = new Intent(Profile_Activity.this, MapsActivity.class);
                    startActivity(intent1);
                }
            });

                    UserMap.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String geoUri = "http://maps.google.com/maps?q=loc:" + Latitude.toString() + "," + Longitude.toString() + "(" + UserShopName.getText()  + ")";
                            Log.e("TAG", "onClick: "+geoUri );
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(geoUri));
                            startActivity(intent);
                        }
                    });


                    img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_24px));
                }

            }
        });



        findViewById(R.id.textView23).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Profile_Activity.this,DynamicWebview.class );
                intent1.putExtra("Mode","Contact_us");
                startActivity(intent1);
            }
        });

        findViewById(R.id.text322).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Profile_Activity.this,DynamicWebview.class );
                intent1.putExtra("Mode","Legal_Provider");
                startActivity(intent1);
            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE  && resultCode == RESULT_OK) {
            imageuri1 = data.getData();


            CropImage.activity(imageuri1)
                    .setAspectRatio(1 , 1)
                    .start(Profile_Activity.this);

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
                    imageView.setImageBitmap(converetdImage);


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
}
