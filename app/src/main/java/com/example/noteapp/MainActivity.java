package com.example.noteapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {
    Toolbar tb;
    CardView cd[];
    TextView t1[];
    Context context;
    NotesProvider np;
    int N=0,cdid=0;
    String title, note,t[],n[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tb = findViewById(R.id.toolbar);
        tb.setTitle("My Toolbar");
        setSupportActionBar(tb);
        /////////////////////Adding Notes/////////////////////////////////////////////////////////////////////////////////////////
        context=getApplicationContext();
        np=new NotesProvider();
        LinearLayout myLinearLayout = (LinearLayout) findViewById(R.id.l1);
        N=count();
        cd=new CardView[N];
        t1=new TextView[N];
        t=new String[N];
        n=new String[N];
        for (int i = 0; i < N; i++) {
            cd[i] = new CardView(context);
            // Set the CardView layoutParams
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            params.setMargins(40,20,40,20);
            cd[i].setLayoutParams(params);
            cd[i].setMinimumHeight(400);
            cd[i].setRadius(30);
            cd[i].setClickable(true);
            cd[i].setId(i+1);
            cd[i].setCardBackgroundColor(Color.parseColor("#EAE5BB"));
            cd[i].setCardElevation(18);
            // Initialize a new TextView to put in CardView
            t1[i] = new TextView(context);
            t1[i].setLayoutParams(params);
            t1[i].setHeight(380);
            t1[i].setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
            t1[i].setTextSize(14);
            onClickShowDetails(t1[i],i+1);
            cdid++;
            cd[i].addView(t1[i]);
            myLinearLayout.addView(cd[i]);
        }
        for(int i=1; i<=cdid; i++) {
            cd[i-1]=findViewById(i);

            final int finalI = i;
            cd[i-1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in=new Intent(MainActivity.this,AddNote.class);
                    in.putExtra("cid",""+finalI);
                    in.putExtra("title",""+t[finalI-1]);
                    in.putExtra("notess",""+n[finalI-1]);
                    startActivity(in);
                }
            });
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    }

    public int count(){
        Cursor cursor = getContentResolver().query(Uri.parse("content://com.example.noteapp/users"), null, null, null, null);
        int strBuild = 0;
        strBuild=(cursor.getCount());
        System.out.println("/////strBuild///////"+strBuild);
        return strBuild;
    }

    public void onClickShowDetails(TextView textView,int id) {

        Cursor cursor = getContentResolver().query(Uri.parse("content://com.example.noteapp/users"), null, "id="+id, null, "id="+id);
        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                title=cursor.getString(cursor.getColumnIndex("name"));
                t[id-1]=title;
                note=cursor.getString(cursor.getColumnIndex("note"));
                n[id-1]=note;
                System.out.println("////////test1 id/////"+id);
                cursor.moveToNext();
            }
                textView.setText("Title: "+title+"\n"+note);
        }
        else {
            System.out.println("////////test1/////");
            textView.setText("No Records Found");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuAdd:
            case R.id.menuAdd2: {
                Intent in=new Intent(MainActivity.this, AddNote.class);
                in.putExtra("cid",""+0);
                in.putExtra("title","");
                in.putExtra("notess","");
                startActivity(in);
            }
                break;
            case R.id.menuLogout:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
                builder.setNegativeButton("Cancel", null);

                builder.show();
        }
        return true;
    }
}

