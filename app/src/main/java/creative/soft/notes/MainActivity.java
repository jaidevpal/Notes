package creative.soft.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView mListViewNotes;
    private Note LoadedNote;
    private EditText mEtTitle;
    ActionBarDrawerToggle toggle;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListViewNotes = findViewById(R.id.main_listView_notes);
        mEtTitle = findViewById(R.id.note_et_title);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){

//                    case R.id.textNote:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.frag_area, new Text_Note_Fragment()).addToBackStack(null).commit();
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        break;

                    case R.id.tdlNote:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.frag_area, new TodolistFragment()).addToBackStack(null).commit();
                        Intent startTDL = new Intent(MainActivity.this, To_do_list_Activity.class);
                        startActivity(startTDL);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.voiceNote:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.frag_area, new TodolistFragment()).addToBackStack(null).commit();
                        Intent startVN = new Intent(MainActivity.this, Voice_Note_Activity.class);
                        startActivity(startVN);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.imageNote:
                        Intent startIN = new Intent(MainActivity.this, Image_Note.class);
                        startActivity(startIN);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.faq:
                        Intent startFAQ = new Intent(MainActivity.this, faq_note.class);
                        startActivity(startFAQ);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.about:
                        Intent startABOUT = new Intent(MainActivity.this, about_note.class);
                        startActivity(startABOUT);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.logout:

                        drawerLayout.closeDrawer(GravityCompat.START);

                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("LOGOUT")
                                .setMessage("Are you sure, you want to Logout Notes ?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent LOGOUT = new Intent(MainActivity.this, LoginActivity.class);
                                        LOGOUT.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clears all activity states
                                        startActivity(LOGOUT);
                                    }
                                })
                                .setNegativeButton("No", null)
                                .setCancelable(false);
                        dialog.show();
                        break;

                }

                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{

            AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                    .setTitle("LOGOUT")
                    .setMessage("Are you sure, you want to Logout Notes ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MainActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("No", null)
                    .setCancelable(false);
            dialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (toggle.onOptionsItemSelected(item)){
            return true;
        }

        switch (item.getItemId()){
            case R.id.action_main_new_note:
                //Start new note activity
                startActivity(new Intent(this, NoteActivity.class));
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mListViewNotes.setAdapter(null);

        ArrayList<Note> notes = Utilities.getAllSavedNotes(this);

        if (notes == null/* || notes.size()==0*/){
            Toast.makeText(this, "You have not saved Notes !", Toast.LENGTH_SHORT).show();
            return;
        }else {
            NoteAdapter na = new NoteAdapter(this, R.layout.item_note, notes);
            mListViewNotes.setAdapter(na);

            mListViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String fileName = ((Note)mListViewNotes.getItemAtPosition(i)).getDateTime() + Utilities.FILE_EXTENSION;

                    Intent viewNoteIntent =  new Intent(getApplicationContext(), NoteActivity.class);
                    viewNoteIntent.putExtra("NOTE_FILE", fileName);
                    startActivity(viewNoteIntent);
                }
            });

            mListViewNotes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("delete")
                            .setMessage("Are you sure, you want to delete text note ?")
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    Utilities.deleteNote(MainActivity.this, LoadedNote.getDateTime() + Utilities.FILE_EXTENSION);
                                    String fileName = ((Note)mListViewNotes.getItemAtPosition(position)) + Utilities.FILE_EXTENSION;
                                    Toast.makeText(MainActivity.this, fileName + " deleted successfully !", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", null)
                            .setCancelable(false).show();

                    return true;
                }
            });
        }
    }
}
