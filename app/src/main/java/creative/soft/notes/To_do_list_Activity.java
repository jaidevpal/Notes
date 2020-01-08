package creative.soft.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class To_do_list_Activity extends AppCompatActivity {

    EditText enterTask;
    Button add;
    ListView list;

    ArrayList<String> item;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        enterTask = findViewById(R.id.et);
        add = findViewById(R.id.btn);
        list = findViewById(R.id.listView);

        item = Utilities.readData(this);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, item);
        list.setAdapter(adapter);

//        List Item deletes Data
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item.remove(i);

//                Update Saved data
                adapter.notifyDataSetChanged();
                Utilities.writeData(item, To_do_list_Activity.this);

                Toast.makeText(To_do_list_Activity.this, "Deleted !", Toast.LENGTH_SHORT).show();
            }
        });

//        Add Button saves data
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){

                    case R.id.btn:
                        if (enterTask.getText().toString().isEmpty()){
                            Toast.makeText(To_do_list_Activity.this, "Sorry, You can't save Empty Task", Toast.LENGTH_SHORT).show();
                        }else {
                            String itemEntered = enterTask.getText().toString();
                            adapter.add(itemEntered);
                            enterTask.setText("");

//                            Writes and Updates data into file.
                            Utilities.writeData(item, To_do_list_Activity.this);

                            Toast.makeText(To_do_list_Activity.this, "Task Added", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
    }
}
