package mdad.localdata.mypracticeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        segmentedControlGroup = findViewById(R.id.segmented_control_group)

        segmentedControlGroup.apply {
            setSelectedIndex(2, false)
            setOnSelectedOptionChangeCallback {
                Toast.makeText(context, "Button ${it + 1} selected", Toast.LENGTH_SHORT).show()
            }
        }
    }
}