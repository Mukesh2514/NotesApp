package com.example.noteapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNote extends AppCompatActivity {
    EditText title, desc;
    NotesProvider np;
    String t,d,tt=null,dd=null;
    Button update, save;
    int id,MAX;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        title = findViewById(R.id.t1);
        desc = findViewById(R.id.n1);
        update= findViewById(R.id.button3);
        save= findViewById(R.id.button);
        Toolbar tb = findViewById(R.id.toolbar);
        tb.setTitle("My Toolbar");
        MAX=showMax();
        setSupportActionBar(tb);
        np = new NotesProvider();
        tt=getIntent().getStringExtra("title");
        dd=getIntent().getStringExtra("notess");
        id=Integer.parseInt(getIntent().getStringExtra("cid"));
        if(!tt.isEmpty()){
            title.setText(tt);
            desc.setText(dd);
            save.setEnabled(false);
            save.setVisibility(View.INVISIBLE);
        }
        else{
            update.setEnabled(false);
            update.setVisibility(View.INVISIBLE);

        }
    }

    public void onClickAddDetails(View view) {
        t=title.getText().toString();
        d=desc.getText().toString();
        if(!t.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put(np.id,MAX+1);
            values.put(np.name, t);
            values.put(np.note, d);
            getContentResolver().insert(np.CONTENT_URI, values);
            Toast.makeText(getBaseContext(), "Note Saved", Toast.LENGTH_LONG).show();
            startActivity(new Intent(AddNote.this, MainActivity.class));
            finish();
        }else{
            Toast.makeText(getBaseContext(), "Please Enter Title", Toast.LENGTH_LONG).show();
        }
    }
    public void onClickUpdateDetails(View view) {
        t=title.getText().toString();
        d=desc.getText().toString();
        if(!t.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put(np.name, t);
            values.put(np.note, d);
            getContentResolver().update(np.CONTENT_URI, values,"id="+id,null);
            Toast.makeText(getBaseContext(), "Note Updated", Toast.LENGTH_LONG).show();
            startActivity(new Intent(AddNote.this, MainActivity.class));
            finish();
        }else{
            Toast.makeText(getBaseContext(), "Please Enter Title", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!tt.isEmpty()) {
            getMenuInflater().inflate(R.menu.menu2, menu);
            return super.onCreateOptionsMenu(menu);
        }
        else
            return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuUpdate:
            {
                update.performClick();
            }
            break;
            case R.id.menuHome:{
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent in=new Intent(AddNote.this, MainActivity.class);
                        startActivity(in);
                        finish();
                    }
                });
                builder.setNegativeButton("No", null);

                builder.show();
            }
            break;
            case R.id.menuDelete:{
                getContentResolver().delete(np.CONTENT_URI, "id="+id,null);
                //     np.updateId(id);
                MAX=showMax();
                ContentValues values = new ContentValues();
                int i=1;
                int f=0;
                while(MAX!=0){
                    values.put(np.id, i);
                    f++;
                    if(f!=id){
                        getContentResolver().update(np.CONTENT_URI, values, "id=" + f, null);
                        i++;
                    }
                    MAX--;
                }
                Toast.makeText(getBaseContext(), "Note Deleted", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
            break;

            //builder.setPositiveButton()
        }
        return true;
    }
    /////////////////////////////////////
    public int showMax(){
        Cursor cursor = getContentResolver().query(Uri.parse("content://com.example.noteapp/users"), null, null, null, null);
        if(cursor.moveToFirst()) {
            StringBuilder strBuild=new StringBuilder();
            while (!cursor.isAfterLast())
            {
                strBuild.append(cursor.getString(cursor.getColumnIndex("id")));
                cursor.moveToNext();
            }
            strBuild=strBuild.reverse();
            String z=String.valueOf(strBuild);
            System.out.println("////////z///"+z+"/////"+strBuild.length());
            int x=0;
            if(strBuild.length()>9) {
                char temp[]=new char[2];
                temp[0]= strBuild.charAt(0);
                temp[1] =strBuild.charAt(1);
                x=(Integer.parseInt(String.valueOf(temp[1]))*10)+Integer.parseInt(String.valueOf(temp[0]));;

            }else{
                char temp1 = strBuild.charAt(0);
                x = Character.getNumericValue(temp1);
            }
            return x;
        }
        else {
            return 0;
            //System.out.println("////////No Records Found");
        }
    }




}