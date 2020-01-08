package creative.soft.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText userName, password;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        userName = findViewById(R.id.EtName);
        password = findViewById(R.id.EtPSWD);
        loginButton = findViewById(R.id.login_btn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((userName.getText().toString().equals("abc"))&&(password.getText().toString().equals("123"))){

                    userName.setText("");
                    password.setText("");

                    Intent loginEntry = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(loginEntry);

                    Toast.makeText(LoginActivity.this, "Logged In Successfully !", Toast.LENGTH_SHORT).show();
                } else if((userName.getText().toString().isEmpty()) && (password.getText().toString().isEmpty())){
                    Toast.makeText(LoginActivity.this, "Credentials can not be Empty !", Toast.LENGTH_SHORT).show();
                }else if((!userName.getText().toString().equals("abc")) || (!password.getText().toString().equals("123"))){
                    Toast.makeText(LoginActivity.this, "Invalid Credentials !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
