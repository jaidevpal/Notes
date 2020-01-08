package creative.soft.notes;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Utilities {

    public static final String FILE_EXTENSION = ".bin";

    public static boolean saveNote(Context context, Note note){
        String fileName = String.valueOf(note.getDateTime()) + FILE_EXTENSION;

        FileOutputStream fos;
        ObjectOutputStream Oos;

        try {
            fos = context.openFileOutput(fileName, context.MODE_PRIVATE);
            Oos = new ObjectOutputStream(fos);
            Oos.writeObject(note);
            Oos.close();
            fos.close();

        }catch (Exception e){
            e.printStackTrace();
            return false;

        }

        return true;

    }

    public static ArrayList<Note> getAllSavedNotes(Context context){

        ArrayList<Note> notes = new ArrayList<>();

        File fileDir = context.getFilesDir();
        ArrayList<String> noteFiles = new ArrayList<>();

        for (String file : fileDir.list()){
            if (file.endsWith(FILE_EXTENSION)){
                noteFiles.add(file);
            }
        }

        FileInputStream fis;
        ObjectInputStream ois;

        for (int i=1; i < noteFiles.size(); i++){
            try {
                fis = context.openFileInput(noteFiles.get(i));
                ois = new ObjectInputStream(fis);
                notes.add((Note)ois.readObject());

                fis.close();
                ois.close();

            }catch (IOException | ClassNotFoundException e){
                e. printStackTrace();
                return null;
            }
        }

        return notes;
    }

    public static Note getNoteByName(Context context, String fileName){
        File file = new File(context.getFilesDir(), fileName);
        Note note;

        if (file.exists()){
            FileInputStream fis;
            ObjectInputStream ois;

            try {
                fis = context.openFileInput(fileName);
                ois = new ObjectInputStream(fis);

                note = (Note)ois.readObject();

                fis.close();
                ois.close();

            }catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
                return null;
            }
            return note;
        }
        return null;
    }

    public static void deleteNote(Context context, String filename) {
        File dir = context.getFilesDir();
        File file = new File(dir, filename);

        if (file.exists()){
            file.delete();
        }
    }

//    To do list Modules
    public static final String FILE_NAME = "Listinfo.dat";

    public static void writeData(ArrayList<String> item, Context context){

        try {
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(item);
            oos.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public static ArrayList<String> readData(Context context){
        ArrayList<String> itemList = null;

        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            itemList = (ArrayList<String>) ois.readObject();
        }catch (FileNotFoundException e){
            itemList = new ArrayList<>();
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return itemList;
    }

//    Folder File Viewer
private String name;

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
