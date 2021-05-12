package com.cu.gastosmerchant1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cu.gastosmerchant.R;
import com.cu.gastosmerchant1.Data.Userinfo;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private LinearLayout gloginlayout,floginlayout;
    private static final String TAG = "MainActivity";
    private SignInButton signInButton;
    private LoginButton loginButton;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;
    String fname,lname, email,dob;
    String idToken;
    CallbackManager callbackManager;
    Userinfo userInfo;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    ProgressDialog progressDialog;
    SharedPreferences.Editor editor;
    SharedPreferences sharedpreferences;

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userInfo = new Userinfo();
        sharedpreferences = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();


        loginButton = findViewById(R.id.login_button);
        signInButton = findViewById(R.id.sign_in_button);
        progressDialog = new ProgressDialog(this);

        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait....");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        gloginlayout  =findViewById(R.id.gloginlayout);
        floginlayout  =findViewById(R.id.floginlayout);


        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);

        ImageView img = findViewById(R.id.imageView3);
        gloginlayout.startAnimation(aniFade);
        floginlayout.startAnimation(aniFade);
        img.startAnimation(aniFade);

        gloginlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                Intent intent =  mGoogleSignInClient.getSignInIntent();
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });
        floginlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

                loginButton.callOnClick();

            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));





        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                handlefacebooktoken(loginResult.getAccessToken());
                Bundle parameters = new Bundle();
                parameters.putString("fields","id,email,first_name,last_name,gender");
                GraphRequest request =   GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                } else {
                                    // get email and id of the user
                                    String email = me.optString("email");
                                    String id = me.optString("id");
                                    String name1 = me.optString("name");
                                    userInfo.setOwnername(me.optString("first_name")+" "+me.optString("last_name"));
                                    userInfo.setEmail(me.optString("email"));
                                     Log.w(TAG, "onCompleted: "+email+id +name1+"  "+me.toString());

                                }
                            }
                        }) ;


                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                if(progressDialog.isShowing())
                    progressDialog.cancel();            }

            @Override
            public void onError(FacebookException exception) {
                if(progressDialog.isShowing())
                    progressDialog.cancel();            }
        });






        //this is where we start the Auth state Listener to listen for whether the user is signed in or not

        authStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // Get signedIn user
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //if user is signed in, we call a helper method to save the user details to Firebase
                if (user != null) {
                    // User is signed in
                    // you could place other firebase code
                    //logic to save the user details to Firebase
                    gotoProfile();

                } else {
                    sharedpreferences.edit().clear().apply();

                    if(progressDialog.isShowing())
                        progressDialog.cancel();

                    // User is signed out
//                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))//you can also use R.string.default_web_client_id
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  mGoogleSignInClient.getSignInIntent();
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });
    }

    private void handlefacebooktoken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    Log.w(TAG, "onComplete: "+ "Email"+firebaseUser.getEmail()+"Name"+firebaseUser.getDisplayName());
                    Toast.makeText(LoginActivity.this,"Email"+firebaseUser.getEmail()+"Name"+firebaseUser.getDisplayName(),Toast.LENGTH_SHORT)
                            .show();

                    // UI Changes
                gotoProfile();
                }
                else{
                    if(progressDialog.isShowing())
                        progressDialog.cancel();
                    Toast.makeText(LoginActivity.this,"Error in login",Toast.LENGTH_SHORT)
                            .show();            }
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            assert account != null;
            idToken = account.getIdToken();

            userInfo.setOwnername(account.getDisplayName().split(" ")[0]+" "+account.getDisplayName().split(" ")[1]);

            userInfo.setEmail( account.getEmail());
            // you can store user data to SharedPreference
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            firebaseAuthWithGoogle(credential);
        }else{
            if(progressDialog.isShowing())
                progressDialog.cancel();
            // Google Sign In failed, update UI appropriately
            Log.e(TAG, "Login Unsuccessful. "+result);
            Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
        }
    }
    private void firebaseAuthWithGoogle(AuthCredential credential){

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if(task.isSuccessful()){
                            // UI Changes


                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        }else{
                            if(progressDialog.isShowing())
                                progressDialog.cancel();
                            Log.w(TAG, "signInWithCredential" + task.getException().getMessage());
                            task.getException().printStackTrace();
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }




    private void gotoProfile(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("/Merchant_data/"+firebaseAuth.getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Log.d(TAG, "Value is:  123c "+firebaseAuth.getUid());
                if(dataSnapshot.child("phoneno").exists()){
                    if(progressDialog.isShowing())
                        progressDialog.cancel();
                    Intent intent = new Intent(LoginActivity.this, Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else{
                    if(progressDialog.isShowing())
                        progressDialog.cancel();
                    Intent intent = new Intent(LoginActivity.this, Register.class);
                    if(userInfo.getOwnername()!=null) {
                        editor.putString("name" , userInfo.getOwnername());
                         editor.putString("email" , userInfo.getEmail());
                         editor.commit();
                    }
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });







    }
    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(authStateListener);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            gotoProfile();
        }else{

            if(progressDialog.isShowing())
                progressDialog.dismiss();

        }

//        if (authStateListener != null){
//            FirebaseAuth.getInstance().signOut();
//        }
    }
//
//    @Override
//    protected void onResume() {
//        firebaseAuth = FirebaseAuth.getInstance();
//
//        if (firebaseAuth.getCurrentUser() != null) {
//            gotoProfile();
//        }
//
//        super.onResume();
//    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }

    }

//    }
}