package com.cu.gastosmerchant1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.cu.gastosmerchant.R;
import com.cu.gastosmerchant1.Data.Transaction_History;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

    public class HomeAdapter extends ArrayAdapter<Transaction_History> {

        ArrayList<Transaction_History> arrayList = new ArrayList<>();
        Context sContext;


    public HomeAdapter(Context context, ArrayList<Transaction_History> objects) {
        super(context, R.layout.payment_list_row, R.id.textView2, objects);
        this.arrayList = objects;
        this.sContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }


    @SuppressLint("NewApi")
    private View initView(int position, View convertView, ViewGroup parent) {
        Transaction_History customer = getItem(position);
        if (convertView == null) {
            if (parent == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.payment_list_row, null);
            else
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.payment_list_row, parent, false);
        }
        TextView txtname = (TextView) convertView.findViewById(R.id.textView2);
        TextView txtpayment = convertView.findViewById(R.id.textView3);
        ImageView img = convertView.findViewById(R.id.circleImageView);

        if (txtname != null) {
            if (customer != null ){
                txtname.setText(customer.getUser_name());
                if(customer.getStatus().equals("Pending")||customer.getStatus().equals("Processing"))
                    txtpayment.setTextColor(Color.parseColor("#F8E71C"));
                else if(customer.getStatus().equals("Failed") )
                    txtpayment.setTextColor(Color.parseColor("#9F1600"));
            }




        }

        if(txtpayment != null){
            txtpayment.setText("+"+customer.getAmount_paid()+" ₹");
        }
        if(customer.getMode().equals("paytm")){
            img.setImageDrawable(ContextCompat.getDrawable( convertView.getContext(),R.drawable.paytm));
         }
        else if(customer.getMode().equals("gpay")){
            img.setImageDrawable(ContextCompat.getDrawable( convertView.getContext(),R.drawable.gpay));
        }
        else if(customer.getMode().equals("bhim")){
            img.setImageDrawable(ContextCompat.getDrawable( convertView.getContext(),R.drawable.bhim2));
        }
        else if(customer.getMode().equals("phonepe")){
            img.setImageDrawable(ContextCompat.getDrawable( convertView.getContext(),R.drawable.phonepe));
        }
        DateFormat simple = new SimpleDateFormat(" HH:mm/dd MMM yy");

        // Creating date from milliseconds
        // using Date() constructor
        Date result = new Date(customer.getTimestamp());
        ((TextView)convertView.findViewById(R.id.payment_time)).setText(simple.format(result));


        return convertView;
    }
}
