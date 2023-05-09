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

//        if(SaveSharedPreference.getPhoneNo(MainActivity.this).length() != 0)
//        {
//
//        }
        //startActivity(new Intent(MainActivity.this,WelcomePage.class));

        bt_signups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MainActivity.this,SignUp.class));
                if(!dbHandler.checkRecord("1234567890")) {
                    System.out.println("New record");
                    dbHandler.addNewRecord("1234567890");
                    startActivity(new Intent(MainActivity.this,WelcomePage.class));
                }
                else {
                    startActivity(new Intent(MainActivity.this,WelcomePage.class));
                }


            }
        });
    }


}