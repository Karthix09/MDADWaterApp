package mdad.localdata.mypracticeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class NewUser extends AppCompatActivity{

    Button confirmBtn;
    EditText userName;
    EditText password;
    RadioGroup genderGroup;
    RadioButton maleBtn;
    RadioButton femaleBtn;

    String selectedGender;
    //Declare String Variable for Radio button

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        confirmBtn = findViewById(R.id.confirm_button);
        maleBtn = findViewById(R.id.male);
        femaleBtn = findViewById(R.id.female);
        genderGroup = findViewById(R.id.genderGroup);
        userName = findViewById(R.id.username);
        password = findViewById(R.id.password);


        //SharedPreferences from Main
        preferences = getSharedPreferences("UserAuthentication",MODE_PRIVATE);

        // Get Values From preferences
        userName.setText(preferences.getString("UserName","UNKNOWN_USER"));
        password.setText(preferences.getString("Password","UNKNOWN_USER"));

        //Upon pressing confirm button, store values received from username text, password text and GenderRadioButton
        View.OnClickListener confirmBtnLister = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameText = userName.getText().toString();
                String pwdText = password.getText().toString();


//                Log.d("Gender", selectedGender);

//              Toast.makeText(NewUser.this, "Username Saved - " + userName + "\nPassword saved - " + pwdValue, Toast.LENGTH_SHORT).show();

                if (userNameText.isEmpty() || pwdText.isEmpty()){
                    Toast.makeText(NewUser.this, "Please create your username and password", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(maleBtn.isChecked() || femaleBtn.isChecked()){

                        // Save changed/retrieved values to preferences
                        preferences.edit().putString("UserName", userNameText).apply();
                        preferences.edit().putString("Password", pwdText).apply();
                        preferences.edit().putString("Gender", selectedGender).apply();

                        Intent intent = new Intent(NewUser.this, NewUserAge.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(NewUser.this, "Please select your Gender", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        };

        confirmBtn.setOnClickListener(confirmBtnLister);

        //setOnCheckedChangeListener Radio groups and buttons
        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup genderGroup, int checkedId) {
                switch(checkedId) {
                    case R.id.male:
                        Toast.makeText(NewUser.this, "Male", Toast.LENGTH_SHORT).show();
                        selectedGender = "Male";
                        break;
                    case R.id.female:
                        Toast.makeText(NewUser.this, "Female", Toast.LENGTH_SHORT).show();
                        selectedGender = "Female";
                        break;
                }
            };
        });
    };


}





