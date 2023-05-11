package com.example.reminderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView tv_welcome;
    Button bt_signups;
    private DBHandler dbHandler;

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_welcome=findViewById(R.id.tv_welcome);
        bt_signups=findViewById(R.id.bt_signups);
         dbHandler= new DBHandler(this);

        bt_signups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!dbHandler.checkRecord("12345678900")) {
                    System.out.println("New record");
                    dbHandler.addNewRecord("12345678900");
                    startActivity(new Intent(MainActivity.this,WelcomePage.class));
                }
                else {
                    startActivity(new Intent(MainActivity.this,WelcomePage.class));
                }


            }
        });
    }


}