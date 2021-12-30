package mdad.localdata.mypracticeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button logIn, signUp;
    EditText username, password;

    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //finding buttons by Id
        logIn = findViewById(R.id.loginBtn);
        signUp = findViewById(R.id.signUpBtn);

        //Finding the editText field by Id
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        //Using Shared Preferences for persistent storage
        preferences = getSharedPreferences("UserValidation",MODE_PRIVATE);


        //Create a new activity for registration and store the User's data from the new page/activity in the SQl database
        //OnClick Listener for Register button
        View.OnClickListener regListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userNameValue = username.getText().toString();
                String pwdValue = password.getText().toString();
                // Save values to preferences
                preferences.edit().putString("UserName", userNameValue).apply();
                preferences.edit().putString("Password", pwdValue).apply();
                Toast.makeText(MainActivity.this, "Username Saved - " + userNameValue + "\nPassword saved - " + pwdValue, Toast.LENGTH_SHORT).show();
//                Toast.makeText(MainActivity.this, "Pa Saved - " + pwdValue, Toast.LENGTH_SHORT).show();

                //Moving to the next page

                Intent intent = new Intent(MainActivity.this, NewUser.class);
                startActivity(intent);
            }
        };

        // Take in String Values again from the text Field and check if Username and Password exist in SQL database
        //OnClick Listener for Login button
        View.OnClickListener loginListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Values From preferences
                String userNameOutput = preferences.getString("UserName","UNKNOWN_USER");
                String pwdOutput = preferences.getString("Password","UNKNOWN_USER");

                Toast.makeText(MainActivity.this, "Username - "+ userNameOutput + "\nPassword - "+ pwdOutput, Toast.LENGTH_SHORT).show();
//                Toast.makeText(MainActivity.this, pwdOutput, Toast.LENGTH_SHORT).show();

            }
        };

        signUp.setOnClickListener(regListener);
        logIn.setOnClickListener(loginListener);

    }
}