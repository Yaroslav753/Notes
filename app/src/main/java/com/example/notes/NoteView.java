package com.example.notes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class NoteView extends AppCompatActivity {

    private int pos;
    private EditText etTitle;
    private EditText etText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_view);

        etTitle = findViewById(R.id.etTitle);
        etText = findViewById(R.id.etText);

        pos = getIntent().getExtras().getInt("pos");
        etTitle.setText(getIntent().getExtras().getString("title"));
        etText.setText(getIntent().getExtras().getString("text"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.buttons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        int id = menu.getItemId();

        if(id == R.id.okButton){
            if(pos >= MainActivity.Instance.mAdapter.getItemCount()) {
                MainActivity.Instance.AddNote(etTitle.getText().toString(), etText.getText().toString());
            }
            else{
                MainActivity.Instance.UpdateNote(pos, MainActivity.Instance.mAdapter.mNotes.get(pos).GetId(), etTitle.getText().toString(), etText.getText().toString());
            }

            NoteView.this.finish();
        }

        return super.onOptionsItemSelected(menu);
    }
}
