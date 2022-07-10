package com.varshith.varshith.qr_generator;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.varshith.varshith.qr_generator.R;;

public class MainActivity extends AppCompatActivity {

    LinearLayout generate_textview, scan_textview;
    ImageView inform;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generate_textview = findViewById(R.id.generate_qr);
        scan_textview = findViewById(R.id.scan_qr);
        inform = findViewById(R.id.info);
        generate_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, GenerateQrActivity.class);
                startActivity(i);
            }
        });

        scan_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(i);
            }
        });
        inform.bringToFront();
        inform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Info.class);
                startActivity(myIntent);
            }
        });
    }
        @Override
        public void onBackPressed(){
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);

    }
}