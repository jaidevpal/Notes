package creative.soft.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class NoteActivity extends AppCompatActivity {

    private EditText mEtTitle;
    private EditText mEtContent;

    private String NoteFileName;
    private Note LoadedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mEtTitle = findViewById(R.id.note_et_title);
        mEtContent = findViewById(R.id.note_et_content);

        NoteFileName = getIntent().getStringExtra("NOTE_FILE");
        if (NoteFileName != null && !NoteFileName.isEmpty()){
            LoadedNote = Utilities.getNoteByName(this, NoteFileName);

            if (LoadedNote != null){
                mEtTitle.setText(LoadedNote.getTitle());
                mEtContent.setText(LoadedNote.getContent());
            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_note_save:
                if (mEtTitle.getText().toString().isEmpty() && mEtContent.getText().toString().isEmpty()){
                    Toast.makeText(this, "Sorry, You can't save empty Notes !", Toast.LENGTH_SHORT).show();
                }else {
                    saveNote();
                }
                break;
            case R.id.action_note_delete:
                deleteNote();
                break;
        }

        return true;
    }

    private void saveNote(){

        Note note;

        if (LoadedNote == null){
            note = new Note(System.currentTimeMillis(), mEtTitle.getText().toString(), mEtContent.getText().toString());
        }else {
            note = new Note(LoadedNote.getDateTime(), mEtTitle.getText().toString(), mEtContent.getText().toString());
        }

        if (Utilities.saveNote(this, note)){
            Toast.makeText(this, "Note Saved Successfully", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Toast.makeText(this, "Sorry !, Can not saved Notes. Note Enough Space", Toast.LENGTH_SHORT).show();
        }
        finish();

    }

    public void deleteNote() {
        if (LoadedNote == null ){
            finish();
        }else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                    .setTitle("delete")
                    .setMessage("Are you sure, you want to delete "+ mEtTitle.getText().toString() +" text note ?")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Utilities.deleteNote(getApplicationContext(), LoadedNote.getDateTime() + Utilities.FILE_EXTENSION);
                            Toast.makeText(NoteActivity.this, mEtTitle.getText().toString() + " deleted successfully !", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .setCancelable(false);
            dialog.show();


        }
    }
}
