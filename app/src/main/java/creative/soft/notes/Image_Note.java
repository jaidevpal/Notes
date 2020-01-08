package creative.soft.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static creative.soft.notes.Voice_Note_Activity.RequestPermissionCode;

public class Image_Note extends AppCompatActivity {

    public static final int CAMERA_REQUEST_CODE = 11;
    public static final int CAMERA_REQUEST_PERMISSION_CODE = 22;

    ImageView imageView;
    ListView imgGallery;
    ImageButton click_Btn;
    public  static final int RequestPermissionCode  = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image__note);

        click_Btn = findViewById(R.id.CameraClick);
//        imageView = findViewById(R.id.imageFrame);
        imgGallery = findViewById(R.id.cam_IMG_ListView);

        EnableRuntimePermission();

        click_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//
//                startActivityForResult(intent, 7);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        invokeCamera();
                    }else {
                        String [] permissionRequest = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissionRequest, CAMERA_REQUEST_PERMISSION_CODE);
                    }
                }

            }
        });
    }

    private void invokeCamera() {
        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", createImgFile());

        Intent intentCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCam.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        intentCam.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        startActivityForResult(intentCam, CAMERA_REQUEST_CODE);

    }

    private File createImgFile() {
//        File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File picDir = new File(Environment.getExternalStorageDirectory()+"/ImageNote/");
        File imgFile = new File(picDir, getPicName());

        return imgFile;
    }

    private String getPicName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeStamp = sdf.format(new Date());

        return "IMG_Note_" + timeStamp + ".jpg";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_REQUEST_PERMISSION_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                invokeCamera();
            }else {
                Toast.makeText(this, "Can not access Camera without Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
//
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//
//            imageView.setImageBitmap(bitmap);
//        }
//
//    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(Image_Note.this, Manifest.permission.CAMERA))
        {

            Toast.makeText(Image_Note.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(Image_Note.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        imgGallery.setAdapter(new File_Folder_Adapter(Image_Note.this, getData()));

        imgGallery.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int position, long l) {

                AlertDialog.Builder dialogue = new AlertDialog.Builder(Image_Note.this)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this file ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    String fileName = imgGallery.getItemAtPosition(position) + ".jpg";
                                    File file = new File(fileName);

                                    if (file.exists()){
//                                        deleteFile(fileName);
                                        file.delete();
                                        imgGallery.setAdapter(new File_Folder_Adapter(Image_Note.this, getData()));
                                        return;
                                    } else {
                                        Toast.makeText(Image_Note.this, "File does not exist", Toast.LENGTH_SHORT).show();
                                    }
//                                    file.delete();

                                }catch (Exception e){
                                    e.printStackTrace();
                                }

//                                String fileName = ((Utilities)imgGallery.getItemAtPosition(position)).getName();
//                                File photoLcl = new File(getFilesDir(),fileName);
//                                Uri imageUriLcl = FileProvider.getUriForFile(Image_Note.this, getPackageName() + ".provider", photoLcl);
//                                Context context = null;
//                                context.grantUriPermission(getApplicationContext().getPackageName(),imageUriLcl, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                                ContentResolver contentResolver = getContentResolver();
//                                contentResolver.delete(imageUriLcl, null, null);

                                Toast.makeText(Image_Note.this, ((Utilities)imgGallery.getItemAtPosition(position)).getName() + " delete successfully !", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", null)
                        .setCancelable(false);
                dialogue.show();



                return true;
            }
        });

    }

        private ArrayList<Utilities> getData(){
        ArrayList<Utilities> Util = new ArrayList<>();

        boolean success = true;
//        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File folder = new File(Environment.getExternalStorageDirectory()+"/ImageNote/");

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



    //    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        switch (requestCode) {
//
//            case RequestPermissionCode:
//
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    Toast.makeText(Image_Note.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
//
//                } else {
//
//                    Toast.makeText(Image_Note.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
//
//                }
//                break;
//        }
//    }
}
