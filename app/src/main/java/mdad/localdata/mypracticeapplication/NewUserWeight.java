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

public class NewUserWeight extends AppCompatActivity {

    NumberPicker weightPicker;
    TextView weightDisplay;
    Button confirmBtn;

    SharedPreferences preferences;

    int userWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_weight);

        weightDisplay = findViewById(R.id.weightDisplay);
        weightPicker = findViewById(R.id.numberPicker);
        confirmBtn = findViewById(R.id.confirmButton);

        //SharedPreferences from Main
        preferences = getSharedPreferences("UserAuthentication",MODE_PRIVATE);


        weightPicker.setMinValue(20);
        weightPicker.setMaxValue(180);

        weightPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                weightDisplay.setText(String.format("Your Weight is: %s", newVal + "Kg"));
                userWeight = newVal;
            }
        });

        View.OnClickListener confirmBtnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("UserWeight", "weight:"+userWeight);
                if (userWeight==0){
                    Toast.makeText(NewUserWeight.this, "Please select your Weight", Toast.LENGTH_SHORT).show();
                }
                else{
                    // Save Age to preferences
                    preferences.edit().putInt("UserWeight", userWeight).apply();

                    Intent newPage = new Intent(NewUserWeight.this, NewUserSleepTime.class);
                    startActivity(newPage);
                }
            }
        };
        confirmBtn.setOnClickListener(confirmBtnListener);
    }
}