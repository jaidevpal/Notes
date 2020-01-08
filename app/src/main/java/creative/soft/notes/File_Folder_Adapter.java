package creative.soft.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class File_Folder_Adapter extends BaseAdapter {

    Context c;
    ArrayList<Utilities> Util;

    public File_Folder_Adapter(Context c, ArrayList<Utilities> util){
        this.c = c;
        this.Util = util;
    }


    @Override
    public int getCount() {
        return Util.size();
    }

    @Override
    public Object getItem(int i) {
        return Util.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){
            view = LayoutInflater.from(c).inflate(R.layout.list_item_style, viewGroup, false);
        }

        final Utilities u = (Utilities) this.getItem(i);

        TextView nameTXT = view.findViewById(R.id.li);

        nameTXT.setText(u.getName());

        return view;
    }

}
