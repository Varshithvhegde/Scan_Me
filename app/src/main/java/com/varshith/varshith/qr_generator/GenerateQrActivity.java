package com.varshith.varshith.qr_generator;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.varshith.varshith.qr_generator.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class GenerateQrActivity extends AppCompatActivity {

    private static int REQUEST_CODE = 100;
    EditText input_editText;
    Button saveImage_button,shareimage;
    TextView generate_button;
    ImageView output_imageView;
    private int flag = 0;
    private int saved=0;
    String fileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);

        input_editText = findViewById(R.id.input_et);
        generate_button = findViewById(R.id.generate_bn);
        saveImage_button = findViewById(R.id.save_gallery_bn);
        output_imageView = findViewById(R.id.output_iv);
        shareimage=findViewById(R.id.share);

        generate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = input_editText.getText().toString().trim();
                if (s.isEmpty()) {
                    input_editText.setError("cannot generate empty QR code");
                } else {
                    saved=1;
                    flag = 1;
                    MultiFormatWriter writer = new MultiFormatWriter();
                    try {
                        BitMatrix matrix = writer.encode(s, BarcodeFormat.QR_CODE, 350, 350);
                        BarcodeEncoder encoder = new BarcodeEncoder();
                        Bitmap bitmap = encoder.createBitmap(matrix);
                        output_imageView.setImageBitmap(bitmap);
                        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        manager.hideSoftInputFromWindow(input_editText.getApplicationWindowToken(), 0);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

//444
        saveImage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(GenerateQrActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    if(flag==1) {
                        saveImage();
                    }
                } else {
                    askPermission();
                }
            }
        });
        shareimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saved==2) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/jpeg");
                    share.putExtra(Intent.EXTRA_STREAM, Uri.parse("/storage/emulated/0/Download/generatedQR/" + fileName));
                    startActivity(Intent.createChooser(share, "Share Image"));
                }
                else if(saved==1){
                    Toast.makeText(getApplicationContext(),"Save the qr code first",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Create Save the qr code first",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //44444
    }


    private void askPermission() {
        ActivityCompat.requestPermissions(GenerateQrActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(flag==1) {
                    saveImage();
                }
            } else {
                Toast.makeText(this, "Please provide the required permissions", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void saveImage() {

        BitmapDrawable draw = (BitmapDrawable) output_imageView.getDrawable();
        Bitmap bitmap = draw.getBitmap();

        FileOutputStream outStream = null;
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/Download/"+ "/generatedQR");
        dir.mkdirs();
        fileName = String.format("%d.jpeg", System.currentTimeMillis());
        File outFile = new File(dir, fileName);
        try {
            outStream = new FileOutputStream(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        saved=2;
        Toast.makeText(this, "saved to gallery", Toast.LENGTH_SHORT).show();
        try {
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        intent.setData(Uri.fromFile(outFile));
//        String shareMessage = "Let me recommend you this application\n A QR Code Scanner\n\n";
//        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
//        intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        sendBroadcast(intent);
    }

}