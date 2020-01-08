package creative.soft.notes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Voice_Note_Activity extends AppCompatActivity {
    public static final int PERMISSION_REQUEST_CODE = 1212;
    public static final String AUDIO_FILE_EXTENSION = ".3gp";
    Button buttonStart, buttonStop, buttonPlayLastRecordAudio, buttonStopPlayingRecording ;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer ;
    ListView listView;
    private List<File> mFiles;

    public static final int MY_REQUEST_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice__note);

        //        Check and request for Permission

        if (ContextCompat.checkSelfPermission(Voice_Note_Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(Voice_Note_Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(Voice_Note_Activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_PERMISSION);
            }else {
                ActivityCompat.requestPermissions(Voice_Note_Activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_PERMISSION);
            }
        }else {
            doStuff();
        }

        buttonStart = (Button) findViewById(R.id.record_btn);
        buttonStop = (Button) findViewById(R.id.stopRecord_btn);
        buttonPlayLastRecordAudio = (Button) findViewById(R.id.play_btn);
        buttonStopPlayingRecording = (Button)findViewById(R.id.stopPlay_btn);

        buttonStop.setEnabled(false);
        buttonPlayLastRecordAudio.setEnabled(false);
        buttonStopPlayingRecording.setEnabled(false);

        random = new Random();

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkPermission()) {

                    String currentDate = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss", Locale.getDefault()).format(new Date());
                    AudioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + "/VoiceNoteRecord/" + currentDate + "Voice Note.3gp";

                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    buttonStart.setEnabled(false);
                    buttonStop.setEnabled(true);

                    Toast.makeText(Voice_Note_Activity.this, "Recording started", Toast.LENGTH_LONG).show();
                }
                else {

                    requestPermission();

                }

            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaRecorder.stop();

                buttonStop.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);

                Toast.makeText(Voice_Note_Activity.this, "Recording Completed", Toast.LENGTH_LONG).show();

                doStuff();

            }
        });

        buttonPlayLastRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException, SecurityException, IllegalStateException {

                buttonStop.setEnabled(false);
                buttonStart.setEnabled(false);
                buttonStopPlayingRecording.setEnabled(true);

                mediaPlayer = new MediaPlayer();

                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();

                Toast.makeText(Voice_Note_Activity.this, "Recording Playing", Toast.LENGTH_LONG).show();

            }
        });

        buttonStopPlayingRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buttonStop.setEnabled(false);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);

                if(mediaPlayer != null){

                    mediaPlayer.stop();
                    mediaPlayer.release();

                    MediaRecorderReady();

                }

            }
        });
    }

    private void doStuff() {
        listView = findViewById(R.id.listView_Area_Voice_Note);
        listView.setAdapter(new File_Folder_Adapter(Voice_Note_Activity.this, getData()));

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                new AlertDialog.Builder(Voice_Note_Activity.this)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this file ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                                            && checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

                                        try {
//                                            Storage storage = new Storage(getApplicationContext());
//                                            final String file = ((Utilities)listView.getItemAtPosition(position)).getName();
//                                            final File fileDir = new File(getFilesDir(), file);
//                                            String path = fileDir.getAbsolutePath();
//                                            storage.deleteFile(path);
                                            File file = new File(((Utilities)listView.getItemAtPosition(position)).getName());
                                            file.delete();
                                            boolean deleted = file.delete();

                                            if (deleted){
                                                Toast.makeText(Voice_Note_Activity.this, "File Deleted", Toast.LENGTH_SHORT).show();
                                            }

                                        }catch (Exception e){
                                            System.out.println("This is the Error: " + e);
                                            e.printStackTrace();
                                        }

                                    }else {
                                        String [] permissionRequest = {Manifest.permission.READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
                                        requestPermissions(permissionRequest, PERMISSION_REQUEST_CODE);
                                    }
                                }

                            }
                        })
                .setNegativeButton("No", null)
                .setCancelable(false)
                .show();

                return false;
            }
        });
    }



    public static boolean delete(final Context context, final File file) {
        final String where = MediaStore.MediaColumns.DATA + "=?";
        final String[] selectionArgs = new String[] {
                file.getAbsolutePath()
        };
        final ContentResolver contentResolver = context.getContentResolver();
        final Uri filesUri = MediaStore.Files.getContentUri("external");

        contentResolver.delete(filesUri, where, selectionArgs);

        if (file.exists()) {

            contentResolver.delete(filesUri, where, selectionArgs);
        }
        return !file.exists();
    }



//    Fetching File's data from Directory

    private ArrayList<Utilities> getData(){
        ArrayList<Utilities> Util = new ArrayList<>();

        boolean success = true;
//        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File folder = new File(Environment.getExternalStorageDirectory()+"/VoiceNoteRecord/");

        Utilities u;

        if (!folder.exists()){
            success = folder.mkdir();

        }
        if (folder.exists()){
            File[] files = folder.listFiles();

            for (int i=0; i<files.length; i++){
                File file = files[i];

                u = new Utilities();
                u.setName(file.getName());

                Util.add(u);
            }

        }

        return Util;
    }

    public void MediaRecorderReady(){

        mediaRecorder=new MediaRecorder();

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);

        mediaRecorder.setOutputFile(AudioSavePathInDevice);

    }

    public String CreateRandomAudioFileName(int string){

        StringBuilder stringBuilder = new StringBuilder( string );

        int i = 0 ;
        while(i < string ) {

            stringBuilder.append(RandomAudioFileName.charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(Voice_Note_Activity.this, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {

                    boolean StoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {

                        Toast.makeText(Voice_Note_Activity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(Voice_Note_Activity.this,"Permission Denied",Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

    public boolean checkPermission() {

        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }
}
