package mdad.localdata.mypracticeapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.lang.reflect.Array;

import me.itangqi.waveloadingview.WaveLoadingView;

public class Dashboard extends AppCompatActivity {

    BottomNavigationView bottomNav;
//    WaveLoadingView mWaveLoadingView;

    //Variables to store retrieved user info from MainActivity
    int user_id;
    String user_username;
    String user_gender;
    int user_age;
    int user_weight;
    String user_wakeUpTime;
    String user_bedTime;
    int user_waterRequirement;
    int user_waterDrankPerc;
    String user_waterStatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        user_id = getIntent().getIntExtra("UserId", 0);
        user_username = getIntent().getStringExtra("Username");
        user_gender = getIntent().getStringExtra("Gender");
        user_age = getIntent().getIntExtra("Age", 0);
        user_weight = getIntent().getIntExtra("Weight", 0);
        user_wakeUpTime = getIntent().getStringExtra("WakeUpTime");
        user_bedTime = getIntent().getStringExtra("BedTime");
        user_waterRequirement = getIntent().getIntExtra("WaterRequirement", 0);
        user_waterDrankPerc = getIntent().getIntExtra("WaterDrankPerc",0);
        user_waterStatus = getIntent().getStringExtra("WaterCompletionStatus");

        Log.d("Dashboard UserId", ""+user_id);
        Log.d("Dashboard Username", ""+user_username);
        Log.d("Dashboard gender", ""+user_gender);
        Log.d("Dashboard age", ""+user_age);
        Log.d("Dashboard weight", ""+user_weight);
        Log.d("Dashboard wakeUpTime", ""+user_wakeUpTime);
        Log.d("Dashboard bedTime", ""+user_bedTime);
        Log.d("Dashboard UserWaterReq", ""+user_waterRequirement);
        Log.d("Dashboard UserDrank", ""+user_waterDrankPerc);
        Log.d("Dashboard UserWaterStat", ""+user_waterStatus);

        //Bottom Navigation
        bottomNav = findViewById(R.id.bottomNavigationView);

        //Creating and Object for fragment
        DashboardFragment main = new DashboardFragment();
        Bundle newPage = new Bundle();
        newPage.putInt("UserId", user_id);
        newPage.putString("Username", user_username);
        newPage.putString("Gender", user_gender);
        newPage.putInt("Weight", user_weight);
        newPage.putInt("Age", user_age);
        newPage.putString("WakeUpTime", user_wakeUpTime);
        newPage.putString("BedTime", user_bedTime);
        newPage.putInt("WaterRequirement", user_waterRequirement);
        newPage.putInt("WaterDrankPerc", user_waterDrankPerc);
        newPage.putString("WaterCompletionStatus", user_waterStatus);
//        Log.d("DashBoardFrag", "Age in Activity:" + age);
        main.setArguments(newPage);

        //Setting DashBoard Fragment as main Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, main).commit();

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int id = item.getItemId();
                switch(id){
                    //check id
                    case R.id.item1:
                        selectedFragment = new DashboardFragment();
                        selectedFragment.setArguments(newPage);
                        break;

                    case R.id.item3:
                        selectedFragment = new ProfileFragment();
                        selectedFragment.setArguments(newPage);
                        break;
                }
                //Begin Transaction fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, selectedFragment).commit();

                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        //Alert Dialog for confirmation to Exit app
        AlertDialog alertDialog = new AlertDialog.Builder(Dashboard.this).create();
        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage("Are you sure you want to exit ?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish(); //kill activity
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


}