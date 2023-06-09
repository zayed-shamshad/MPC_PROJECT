package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class viewReminders extends AppCompatActivity {


    private ArrayList<reminderDetails> reminderDetailsArrayList;
    private DBHandler dbHandler;
    private ReminderRVAdapter reminderRVAdapter;
    private RecyclerView remindersRV;
    TextView txt_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reminders);

        txt_empty = findViewById(R.id.txt_empty);

        reminderDetailsArrayList=new ArrayList<>();
        dbHandler=new DBHandler(viewReminders.this);

        reminderDetailsArrayList=dbHandler.readReminders("12345678900");
        if(reminderDetailsArrayList.isEmpty()){
            txt_empty.setText("Nothing to see here.\nAdd a reminder first!");

        }
        reminderRVAdapter=new ReminderRVAdapter(reminderDetailsArrayList,viewReminders.this);
        remindersRV=findViewById(R.id.idRVReminders);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(viewReminders.this,RecyclerView.VERTICAL,false);
        remindersRV.setLayoutManager(linearLayoutManager);

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(remindersRV);

        remindersRV.setAdapter(reminderRVAdapter);

    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            reminderDetails modal = reminderDetailsArrayList.get(viewHolder.getAdapterPosition());


            String remName = modal.getReminderName();

            dbHandler=new DBHandler(viewReminders.this);

            //alert dialog inclusion start
            AlertDialog.Builder builder = new AlertDialog.Builder(viewReminders.this);
            builder.setMessage("Are you sure you want to delete this reminder?");
            builder.setTitle("CONFIRMATION:");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dbHandler.deleteReminder(remName,"12345678900");
                            reminderDetailsArrayList.remove(viewHolder.getAdapterPosition());
                            reminderRVAdapter.notifyDataSetChanged();
                            Toast.makeText(viewReminders.this, "Reminder deleted", Toast.LENGTH_SHORT).show();

                        }
                    });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog,int which){
                    dialog.cancel();
                    Toast.makeText(viewReminders.this, "Deletion cancelled", Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();


        }
    };
}