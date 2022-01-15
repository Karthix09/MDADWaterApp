package mdad.localdata.mypracticeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class NewUserAge extends AppCompatActivity {

    NumberPicker agePicker;
    TextView ageDisplay;
    Button confirmBtn;

    SharedPreferences preferences;

    int userAge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_age);

        ageDisplay = findViewById(R.id.weightDisplay);
        agePicker = findViewById(R.id.numberPicker);
        confirmBtn = findViewById(R.id.confirmButton);

        //SharedPreferences from Main
        preferences = getSharedPreferences("UserAuthentication",MODE_PRIVATE);


        agePicker.setMinValue(10);
        agePicker.setMaxValue(100);

        agePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                ageDisplay.setText(String.format("Your Age is: %s", newVal));
                userAge = newVal;
            }
        });

        View.OnClickListener confirmBtnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("UserAge", "age:"+userAge);
                if (userAge==0){
                    Toast.makeText(NewUserAge.this, "Please select your Age", Toast.LENGTH_SHORT).show();
                }
                else{
                    // Save Age to preferences
                    preferences.edit().putInt("UserAge", userAge).apply();

                    Intent newPage = new Intent(NewUserAge.this, NewUserWeight.class);
                    startActivity(newPage);
                }
            }
        };
        confirmBtn.setOnClickListener(confirmBtnListener);

    }

}