package com.cu.gastosmerchant1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.gastosmerchant.R;
import com.cu.gastosmerchant1.Data.Transaction_History;
import com.cu.gastosmerchant1.Data.Userinfo;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Home extends AppCompatActivity {
    CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ListView listView;
    CardView profile_card;
    Typeface typeface;
    ArrayList<Transaction_History> arrayList = new ArrayList<>();
    boolean isDetailincomplete=false;

    TextView see_more;


    ArrayList<Transaction_History> subarraylist = new ArrayList<>();

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

          typeface = Typeface.createFromAsset(this.getAssets(), "fonts/OCRAEXT.TTF");


        listView = findViewById(R.id.list_view_payment);
        profile_card = findViewById(R.id.cardView3);

        see_more = findViewById(R.id.see_more_txt);

        listView.setAdapter(null);



        auth = FirebaseAuth.getInstance();
        database=  FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Transaction_History_merchant/"+auth.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange ( @NonNull DataSnapshot dataSnapshot ) {
                arrayList.clear();

                for (DataSnapshot data :dataSnapshot.getChildren()) {

                    Transaction_History history = data.getValue(Transaction_History.class);


                    assert history != null;
                    if(history.getStatus().equals("Success")){
                        arrayList.add(data.getValue(Transaction_History.class));
                    }



//                } HomeAdapter adapter = new HomeAdapter(Home.this, arrayList);
//                listView.setAdapter(adapter);
                }
                subarraylist.clear();
                if(arrayList.size() > 10)
                subarraylist = new ArrayList<Transaction_History>(arrayList.subList(0,10));

                else
                    subarraylist = arrayList;

                Collections.sort(subarraylist);
                HomeAdapter adapter = new HomeAdapter(Home.this, subarraylist);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled ( @NonNull DatabaseError databaseError ) {

            }
        });

        ref = database.getReference("Merchant_data/"+auth.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange ( @NonNull DataSnapshot dataSnapshot ) {
                Userinfo info = dataSnapshot.getValue(Userinfo.class);
                ((TextView)findViewById(R.id.textView4)).setTypeface(typeface);
                ((TextView)findViewById(R.id.textView5)).setTypeface(typeface);
                ((TextView)findViewById(R.id.textView30)).setTypeface(typeface);
                ((TextView)findViewById(R.id.textView4)).setText(info != null ? info.getShopname().toUpperCase() : "Shop Name");
                ((TextView)findViewById(R.id.textView5)).setText(info != null ? info.getAddress().toUpperCase() : "Address");
                ((TextView)findViewById(R.id.textView30)).setText(info.getData() != null ? info.getData().getUpi().toUpperCase() : "ALPHA@UPI");
                View contextView = findViewById(R.id.constraint_layout2);
                try
                {if((info.getData() ==null)||((info.getData().getUpi().equals(""))&&(info.getData().getPaytm().equals(""))))
                   {
                       Snackbar snackbar = Snackbar.make(contextView, "Complete Your Account Details", Snackbar.LENGTH_INDEFINITE).setAction("Go to Profile", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        Intent intent = new Intent(Home.this,Profile_Activity.class);
                        intent.putExtra("account",true);
                        startActivity(intent);
                    }
                });
                isDetailincomplete=true;
                snackbar.show();}}
                catch (Exception e){
                    Log.e("TAG", "onDataChange: ",e );
                }

            }

            @Override
            public void onCancelled ( @NonNull DatabaseError databaseError ) {

            }
        });

//        (findViewById(R.id.see_more_txt)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


        see_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(arrayList);
                HomeAdapter adapter = new HomeAdapter(Home.this, arrayList);
                listView.setAdapter(adapter);
            }
        });




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(Home.this, "Item Clicked!" + position , Toast.LENGTH_SHORT).show();
            }
        });

        profile_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Profile_Activity.class);
                intent.putExtra("account",isDetailincomplete);
                startActivity(intent);
            }
        });
    }





}
