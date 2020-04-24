package com.example.notes;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Debug;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Note> notes = new ArrayList<>();
    private RecyclerView mRecyclerView;
    public Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    DBHelper dbHelper;

    public static MainActivity Instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Instance = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowNoteView();
            }
        });

        dbHelper = new DBHelper(this);

        GetNotes();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new Adapter(this, notes);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void AddNote(String title, String text){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.KEY_TITLE, title);
        contentValues.put(DBHelper.KEY_TEXT, text);
        database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);

        mAdapter.AddNote(new Note(GetMaxId(), title, text));
        dbHelper.close();
    }

    public void UpdateNote(int pos, int id, String title, String text){

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.KEY_TITLE, title);
        contentValues.put(DBHelper.KEY_TEXT, text);
        database.update(DBHelper.TABLE_CONTACTS, contentValues, DBHelper.KEY_ID + "=" + id, null);

        mAdapter.UpdateNote(pos, new Note(id, title, text));
        dbHelper.close();
    }

    public void RemoveNote(int id){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(DBHelper.TABLE_CONTACTS, DBHelper.KEY_ID + "=" + id, null);
        dbHelper.close();
    }

    public void GetNotes(){

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
            int textIndex = cursor.getColumnIndex(DBHelper.KEY_TEXT);
            do {
                notes.add(new Note(cursor.getInt(idIndex), cursor.getString(titleIndex), cursor.getString(textIndex)));
                Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                        ", title = " + cursor.getString(titleIndex) +
                        ", text = " + cursor.getString(textIndex));
            } while (cursor.moveToNext());
        } else
            Log.d("mLog","0 rows");

        cursor.close();
        dbHelper.close();
    }

    private void ShowNoteView(){
        Intent intent = new Intent(this, NoteView.class);
        intent.putExtra("pos", GetMaxId() + 1);
        intent.putExtra("title", "");
        intent.putExtra("text", "");
        startActivity(intent);
    }

    public int GetMaxId(){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

        int maxId =0;
        if(cursor.moveToFirst()){
            do{
                int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                maxId = cursor.getInt(idIndex);
                }while (cursor.moveToNext());
        }
        dbHelper.close();
        cursor.close();
        return maxId;
    }
}
