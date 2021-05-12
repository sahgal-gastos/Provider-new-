package com.cu.gastosmerchant1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.zxing.Result;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class Scan extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    String input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView=new ZXingScannerView(this);
        setContentView(scannerView);

    }

    @Override
    public void handleResult(Result result) {
        String input=result.getText().trim();

        Pattern pattern=Pattern.compile("[a-zA-z0-9]+@[a-zA-z0-9]+");
        Matcher matcher=pattern.matcher(input);
        if(matcher.find())
        {
            MapsActivity.upiedit.setText(matcher.group(0));
            MapsActivity.upiedit2.setText(matcher.group(0));
        }
        onBackPressed();


    }


    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }


}
