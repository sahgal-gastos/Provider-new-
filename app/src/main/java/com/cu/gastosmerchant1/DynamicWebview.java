package com.cu.gastosmerchant1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cu.gastosmerchant.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DynamicWebview extends AppCompatActivity {
    private WebView web;
    String html;
    FirebaseDatabase database;
    DatabaseReference mref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_webview);
//        web=(WebView)findViewById(R.id.webview);

        web = new WebView(this);


        database = FirebaseDatabase.getInstance();
        Intent intent  = getIntent();
        mref = database.getReference(intent.getStringExtra("Mode"));
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                html = dataSnapshot.getValue(String.class);
                Log.e("TAG", "onDataChange: "+html+"sadasddsa");
//              html =  "<!DOCTYPE HTML> <HTML> <BODY> <P STYLE=\"margin-bottom: 0px; line-height: 100%\"> " +
//                        " Welcome to GASTOS, your number one solution of your daily payments." +
//                        "We're dedicated to providing you with the best of the services, with a focus on dependability. customer service, and your " +
//                        "benefits. </P>   <P STYLE=\"margin-bottom: 0.11px\">Instagram :" +
//                        " <A HREF=\"https://instagram.com/_gastos_?igshid=19hctlc76b8x9\">_gastos_</A></P>  <LI><P STYLE=\"margin-bottom: 0.11px\">Facebook :" +
//                        " <A HREF=\"https://www.facebook.com/sahgal.kumar.900\">GASTOS DEALZ</A></P> <LI><P STYLE=\"margin-bottom: 0.11px\">Twitter : " +
//                        "<A HREF=\"https://twitter.com/GASTOS48485267?s=09\">GASTOS</A></P> </UL> <P STYLE=\"margin-left: 0.5px; margin-bottom: " +
//                        "0.11px\">For any queries you can contact us as <A HREF=\"mailto:support@gastos.in\">support@gastos.in</A></P> </BODY> </HTML>";
//                Log.e("TAG", "onDataChange: "+html+"sadasddsa");
//                web.getSettings().setJavaScriptEnabled(true);
//
//                web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                web.getSettings().setDefaultTextEncodingName("utf-8");
                web.setWebViewClient(new WebViewClient(){
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                            view.getContext().startActivity(
                                    new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                            return true;
                        } else if (url.startsWith("mailto:")) {
                            startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse(url)));
                            return true;
                        }

                        else {
                            return false;
                        }
                    }
                });                try {
                    web.loadData(URLEncoder.encode(html, "utf-8").replaceAll("\\+", "%20"),  "text/html", "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                setContentView(web);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
