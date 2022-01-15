package mdad.localdata.mypracticeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Locale;

public class NewUserSleepTime extends AppCompatActivity {

    Button bedTimeBtn;
    Button wakeUpTimeBtn;
    Button confirmBtn;
    int hour, minute;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_sleep_time);

        bedTimeBtn = findViewById(R.id.bedTimeBtn);
        wakeUpTimeBtn = findViewById(R.id.wakeUpTimeBtn);
        confirmBtn = findViewById(R.id.confirm_Button);

        //SharedPreferences from Main
        preferences = getSharedPreferences("UserAuthentication",MODE_PRIVATE);


        //Confirm Button OnClick Listener
        View.OnClickListener confirmation = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check if User has given input for their sleep time and wake up time
                if (wakeUpTimeBtn.getText().toString().matches("Select Your Wake Up time") || bedTimeBtn.getText().toString().matches("Select your Bed Time")) {
                    Toast.makeText(NewUserSleepTime.this, "Please select your BedTime and WakeUpTime", Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("WakeUpButton-Text", "" + wakeUpTimeBtn.getText());
                    Log.d("BedTimeButton-Text", "" + bedTimeBtn.getText());

                    preferences.edit().putString("UserWakeUpTime", wakeUpTimeBtn.getText().toString()).apply();
                    preferences.edit().putString("UserBedTime", bedTimeBtn.getText().toString()).apply();

                    Intent nextPage = new Intent(NewUserSleepTime.this, NewUserConfirmation.class);
                    startActivity(nextPage);
                }

            }
        };
        confirmBtn.setOnClickListener(confirmation);

    }

    //Time Picker for going to bed Time
    public void popBedTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener bedTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                bedTimeBtn.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        TimePickerDialog bedTimePickerDialog = new TimePickerDialog(this, bedTimeSetListener, hour, minute, true);
        bedTimePickerDialog.setTitle("What time do you go to bed?");
        bedTimePickerDialog.show();
    }


    //Time Picker for Wake Up Time
    public void popWakeUpTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener wakeUpTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                wakeUpTimeBtn.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        TimePickerDialog wakeUpTimePickerDialog = new TimePickerDialog(this, wakeUpTimeSetListener, hour, minute, true);
        wakeUpTimePickerDialog.setTitle("What time do you wake up?"); //Cont
        wakeUpTimePickerDialog.show();
    }
}