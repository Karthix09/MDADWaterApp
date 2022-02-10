package mdad.localdata.mypracticeapplication;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.itangqi.waveloadingview.WaveLoadingView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //Variables to store retrieved user info from DashboardActivity
    int user_id;
    String user_username;
    String user_gender;
    int user_age;
    int user_weight;
    String user_wakeUpTime;
    String user_bedTime;

    //Variable to store the water requirement
    int user_waterRequirement_posted;

    //Variables to store retrieved user Drank percentage and Completion status
    int user_waterDrankPerc;
    String user_waterStatus;


    //Variable to store the water increment value
    int water_increment_A = 150;
    int water_increment_B = 200;
    int water_increment_C = 250;
    int water_increment_D = 300;
    int water_increment_E = 350;
    int water_increment_F = 400;

    //progress Bar Value
    int progressBarValue;

    //waterProgress
    int newWaterProgress;

    //old Water Progress
    int pastWaterProgress;

    //Water Completion status
    String waterCompletionStatus;

    //Button calculate water in ML
    int waterInML;

    //Vibrator
    Vibrator vibrator;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    WaveLoadingView mWaveLoadingView;
    Button drankUpBtn;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_dashboard,container,false);
        //Declarations for the water meter
        mWaveLoadingView = v.findViewById(R.id.waveLoadingView);

//        mWaveLoadingView.setProgressValue(10);
        //DrankUp Button
        drankUpBtn = v.findViewById(R.id.drankUp);

        //Getting values from Dashboard Activity
        user_id = getArguments().getInt("UserId");
        user_username = getArguments().getString("Username");
        user_gender = getArguments().getString("Gender");
        user_age = getArguments().getInt("Age");
        user_weight = getArguments().getInt("Weight");
        user_wakeUpTime = getArguments().getString("WakeUpTime");
        user_bedTime = getArguments().getString("BedTime");
        user_waterRequirement_posted = getArguments().getInt("WaterRequirement");

        //Package ID in JSON
        JSONObject idJsonData = new JSONObject();
        try {
            idJsonData.put("UserID", user_id);
            Log.d("UserId", ""+ user_id);
        }
        catch(JSONException e) {

        }

        //API call to get User Water Drank status
        getWaterProgressAPI(idJsonData);


        //Logging out Values of User Information
        Log.d("DashBoardFrag UserId", "" + user_id);
        Log.d("DashBoardFrag Username", "" + user_username);
//        Log.d("DashBoardFrag Gender", "" + user_gender);
//        Log.d("DashBoardFrag Age", "" + user_age);
//        Log.d("DashBoardFrag Weight", "" + user_weight);
//        Log.d("DashBoardFrag WakeTime", "" + user_wakeUpTime);
//        Log.d("DashBoardFrag BedTime", "" + user_bedTime);
//        Log.d("DashBoardFrag WaterReq", "" + user_waterRequirement);


        //Setting Button to specified drink up requirement each time on Button text
        if (user_waterRequirement_posted == 1500){
            drankUpBtn.setText("DRANK UP " + water_increment_A + "ml");
        }
        else if (user_waterRequirement_posted == 2000){
            drankUpBtn.setText("DRANK UP " + water_increment_B + "ml");
        }
        else if (user_waterRequirement_posted == 2500){
            drankUpBtn.setText("DRANK UP " + water_increment_C + "ml");
        }
        else if (user_waterRequirement_posted == 3000){
            drankUpBtn.setText("DRANK UP " + water_increment_D + "ml");
        }
        else if (user_waterRequirement_posted == 3500){
            drankUpBtn.setText("DRANK UP " + water_increment_E + "ml");
        }
        else if(user_waterRequirement_posted == 4000){
            drankUpBtn.setText("DRANK UP " + water_increment_F + "ml");
        }
        else {

        }

        //Set the progress value of the bar according to received water drank value

//        mWaveLoadingView.setCenterTitle("Your goal for today is " + user_waterRequirement_posted +"ml");
//        pastWaterProgress = 20;
//        mWaveLoadingView.setProgressValue(pastWaterProgress);

        View.OnClickListener drankUpBtnListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                btnClickCount = btnClickCount + 1;

                if (user_waterRequirement_posted == 1500) {

                    pastWaterProgress = mWaveLoadingView.getProgressValue();
//                    Log.d("Previous Value", "" + pastWaterProgress);
                    newWaterProgress = pastWaterProgress + 10;

                    if (newWaterProgress == 100) {
                        waterCompletionStatus = "Completed";
                        mWaveLoadingView.setProgressValue(newWaterProgress);
                        mWaveLoadingView.setCenterTitle("You reached your goal for the day!!");
                        Log.d("Water progress", "" + newWaterProgress);
                        Log.d("Status", "" + waterCompletionStatus);

                        //Creating JSON Object to send Water Status and New Water Progress in the POST request
                        JSONObject jsonData = new JSONObject();
                        try {
                            jsonData.put("UserID", user_id);
                            jsonData.put("WaterDrankPerc", newWaterProgress);
                            jsonData.put("WaterCompletion", waterCompletionStatus);
                        }
                        catch(JSONException e) {

                        }
                        //API call to Post
                        postWaterStatusApi(jsonData);

                        //Vibrate Effect
                        vibrator = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            vibrator.vibrate(500);
                        }

                        drankUpBtn.setEnabled(false);

                    }
                    else {
                        waterCompletionStatus = "Incomplete";
                        mWaveLoadingView.setProgressValue(newWaterProgress);
                        waterInML = (int)((newWaterProgress/100f) * user_waterRequirement_posted);
                        mWaveLoadingView.setCenterTitle("You have drank " + waterInML + "ml");
                        Log.d("Water progress", "" + newWaterProgress);
                        Log.d("Status", "" + waterCompletionStatus);
//                        Log.d("Water progress", "" + waterInML);

                        //Creating JSON Object to send Water Status and New Water Progress in the POST request
                        JSONObject jsonData = new JSONObject();
                        try {
                            jsonData.put("UserID", user_id);
                            jsonData.put("WaterDrankPerc", newWaterProgress);
                            jsonData.put("WaterCompletion", waterCompletionStatus);
                        }
                        catch(JSONException e) {

                        }
                        //API call to Post
                        postWaterStatusApi(jsonData);
                    }

                }
                else if (user_waterRequirement_posted == 2000){

                    pastWaterProgress = mWaveLoadingView.getProgressValue();
//                    Log.d("Previous Value", "" + pastWaterProgress);
                    newWaterProgress = pastWaterProgress + 10;

                    if (newWaterProgress == 100) {
                        waterCompletionStatus = "Completed";
                        mWaveLoadingView.setProgressValue(newWaterProgress);
                        mWaveLoadingView.setCenterTitle("You reached your goal for the day!!");
                        Log.d("Water progress", "" + newWaterProgress);
                        Log.d("Status", "" + waterCompletionStatus);

                        //Creating JSON Object to send Water Status and New Water Progress in the POST request
                        JSONObject jsonData = new JSONObject();
                        try {
                            jsonData.put("UserID", user_id);
                            jsonData.put("WaterDrankPerc", newWaterProgress);
                            jsonData.put("WaterCompletion", waterCompletionStatus);
                        }
                        catch(JSONException e) {

                        }
                        //API call to Post
                        postWaterStatusApi(jsonData);

                        //Vibrate Effect
                        vibrator = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            vibrator.vibrate(500);
                        }

                        drankUpBtn.setEnabled(false);

                    }
                    else {
                        waterCompletionStatus = "Incomplete";
                        mWaveLoadingView.setProgressValue(newWaterProgress);
                        waterInML = (int)((newWaterProgress/100f) * user_waterRequirement_posted);
                        mWaveLoadingView.setCenterTitle("You have drank " + waterInML + "ml");
                        Log.d("Water progress", "" + newWaterProgress);
                        Log.d("Status", "" + waterCompletionStatus);
//                        Log.d("Water progress", "" + waterInML);

                        //Creating JSON Object to send Water Status and New Water Progress in the POST request
                        JSONObject jsonData = new JSONObject();
                        try {
                            jsonData.put("UserID", user_id);
                            jsonData.put("WaterDrankPerc", newWaterProgress);
                            jsonData.put("WaterCompletion", waterCompletionStatus);
                        }
                        catch(JSONException e) {

                        }
                        //API call to Post
                        postWaterStatusApi(jsonData);
                    }

                }
                else if (user_waterRequirement_posted == 2500){

                    pastWaterProgress = mWaveLoadingView.getProgressValue();
//                    Log.d("Previous Value", "" + pastWaterProgress);
                    newWaterProgress = pastWaterProgress + 10;

                    if (newWaterProgress == 100) {
                        waterCompletionStatus = "Completed";
                        mWaveLoadingView.setProgressValue(newWaterProgress);
                        mWaveLoadingView.setCenterTitle("You reached your goal for the day!!");
                        Log.d("Water progress", "" + newWaterProgress);
                        Log.d("Status", "" + waterCompletionStatus);

                        //Creating JSON Object to send Water Status and New Water Progress in the POST request
                        JSONObject jsonData = new JSONObject();
                        try {
                            jsonData.put("UserID", user_id);
                            jsonData.put("WaterDrankPerc", newWaterProgress);
                            jsonData.put("WaterCompletion", waterCompletionStatus);
                        }
                        catch(JSONException e) {

                        }
                        //API call to Post
                        postWaterStatusApi(jsonData);

                        //Vibrate Effect
                        vibrator = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            vibrator.vibrate(500);
                        }

                        drankUpBtn.setEnabled(false);
                    }
                    else {
                        waterCompletionStatus = "Incomplete";
                        mWaveLoadingView.setProgressValue(newWaterProgress);
                        waterInML = (int)((newWaterProgress/100f) * user_waterRequirement_posted);
                        mWaveLoadingView.setCenterTitle("You have drank " + waterInML + "ml");
                        Log.d("Water progress", "" + newWaterProgress);
                        Log.d("Status", "" + waterCompletionStatus);
//                        Log.d("Water progress", "" + waterInML);

                        //Creating JSON Object to send Water Status and New Water Progress in the POST request
                        JSONObject jsonData = new JSONObject();
                        try {
                            jsonData.put("UserID", user_id);
                            jsonData.put("WaterDrankPerc", newWaterProgress);
                            jsonData.put("WaterCompletion", waterCompletionStatus);
                        }
                        catch(JSONException e) {

                        }
                        //API call to Post
                        postWaterStatusApi(jsonData);
                    }
                }
                else if (user_waterRequirement_posted == 3000){

                    pastWaterProgress = mWaveLoadingView.getProgressValue();
//                    Log.d("Previous Value", "" + pastWaterProgress);
                    newWaterProgress = pastWaterProgress + 10;

                    if (newWaterProgress == 100) {
                        waterCompletionStatus = "Completed";
                        mWaveLoadingView.setProgressValue(newWaterProgress);
                        mWaveLoadingView.setCenterTitle("You reached your goal for the day!!");
                        Log.d("Water progress", "" + newWaterProgress);
                        Log.d("Status", "" + waterCompletionStatus);

                        //Creating JSON Object to send Water Status and New Water Progress in the POST request
                        JSONObject jsonData = new JSONObject();
                        try {
                            jsonData.put("UserID", user_id);
                            jsonData.put("WaterDrankPerc", newWaterProgress);
                            jsonData.put("WaterCompletion", waterCompletionStatus);
                        }
                        catch(JSONException e) {

                        }
                        //API call to Post
                        postWaterStatusApi(jsonData);

                        //Vibrate Effect
                        vibrator = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            vibrator.vibrate(500);
                        }

                        drankUpBtn.setEnabled(false);
                    }
                    else {
                        waterCompletionStatus = "Incomplete";
                        mWaveLoadingView.setProgressValue(newWaterProgress);
                        waterInML = (int)((newWaterProgress/100f) * user_waterRequirement_posted);
                        mWaveLoadingView.setCenterTitle("You have drank " + waterInML + "ml");
                        Log.d("Water progress", "" + newWaterProgress);
                        Log.d("Status", "" + waterCompletionStatus);
//                        Log.d("Water progress", "" + waterInML);

                        //Creating JSON Object to send Water Status and New Water Progress in the POST request
                        JSONObject jsonData = new JSONObject();
                        try {
                            jsonData.put("UserID", user_id);
                            jsonData.put("WaterDrankPerc", newWaterProgress);
                            jsonData.put("WaterCompletion", waterCompletionStatus);
                        }
                        catch(JSONException e) {

                        }
                        //API call to Post
                        postWaterStatusApi(jsonData);
                    }

                }
                else if (user_waterRequirement_posted == 3500){

                    pastWaterProgress = mWaveLoadingView.getProgressValue();
//                    Log.d("Previous Value", "" + pastWaterProgress);
                    newWaterProgress = pastWaterProgress + 10;

                    if (newWaterProgress == 100) {
                        waterCompletionStatus = "Completed";
                        mWaveLoadingView.setProgressValue(newWaterProgress);
                        mWaveLoadingView.setCenterTitle("You reached your goal for the day!!");
                        Log.d("Water progress", "" + newWaterProgress);
                        Log.d("Status", "" + waterCompletionStatus);

                        //Creating JSON Object to send Water Status and New Water Progress in the POST request
                        JSONObject jsonData = new JSONObject();
                        try {
                            jsonData.put("UserID", user_id);
                            jsonData.put("WaterDrankPerc", newWaterProgress);
                            jsonData.put("WaterCompletion", waterCompletionStatus);
                        }
                        catch(JSONException e) {

                        }
                        //API call to Post
                        postWaterStatusApi(jsonData);

                        //Vibrate Effect
                        vibrator = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            vibrator.vibrate(500);
                        }

                        drankUpBtn.setEnabled(false);
                    }
                    else {
                        waterCompletionStatus = "Incomplete";
                        mWaveLoadingView.setProgressValue(newWaterProgress);
                        waterInML = (int)((newWaterProgress/100f) * user_waterRequirement_posted);
                        mWaveLoadingView.setCenterTitle("You have drank " + waterInML + "ml");
                        Log.d("Water progress", "" + newWaterProgress);
                        Log.d("Status", "" + waterCompletionStatus);
//                        Log.d("Water progress", "" + waterInML);

                        //Creating JSON Object to send Water Status and New Water Progress in the POST request
                        JSONObject jsonData = new JSONObject();
                        try {
                            jsonData.put("UserID", user_id);
                            jsonData.put("WaterDrankPerc", newWaterProgress);
                            jsonData.put("WaterCompletion", waterCompletionStatus);
                        }
                        catch(JSONException e) {

                        }
                        //API call to Post
                        postWaterStatusApi(jsonData);
                    }

                }
                else if(user_waterRequirement_posted == 4000){

                    pastWaterProgress = mWaveLoadingView.getProgressValue();
//                    Log.d("Previous Value", "" + pastWaterProgress);
                    newWaterProgress = pastWaterProgress + 10;

                    if (newWaterProgress == 100) {
                        waterCompletionStatus = "Completed";
                        mWaveLoadingView.setProgressValue(newWaterProgress);
                        mWaveLoadingView.setCenterTitle("You reached your goal for the day!!");
                        Log.d("Water progress", "" + newWaterProgress);
                        Log.d("Status", "" + waterCompletionStatus);

                        //Creating JSON Object to send Water Status and New Water Progress in the POST request
                        JSONObject jsonData = new JSONObject();
                        try {
                            jsonData.put("UserID", user_id);
                            jsonData.put("WaterDrankPerc", newWaterProgress);
                            jsonData.put("WaterCompletion", waterCompletionStatus);
                        }
                        catch(JSONException e) {

                        }
                        //API call to Post
                        postWaterStatusApi(jsonData);

                        //Vibrate Effect
                        vibrator = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            vibrator.vibrate(500);
                        }

                        drankUpBtn.setEnabled(false);
                    }
                    else {
                        waterCompletionStatus = "Incomplete";
                        mWaveLoadingView.setProgressValue(newWaterProgress);
                        waterInML = (int)((newWaterProgress/100f) * user_waterRequirement_posted);
                        mWaveLoadingView.setCenterTitle("You have drank " + waterInML + "ml");
                        Log.d("Water progress", "" + newWaterProgress);
                        Log.d("Status", "" + waterCompletionStatus);
//                        Log.d("Water progress", "" + waterInML);

                        //Creating JSON Object to send Water Status and New Water Progress in the POST request
                        JSONObject jsonData = new JSONObject();
                        try {
                            jsonData.put("UserID", user_id);
                            jsonData.put("WaterDrankPerc", newWaterProgress);
                            jsonData.put("WaterCompletion", waterCompletionStatus);
                        }
                        catch(JSONException e) {

                        }
                        //API call to Post
                        postWaterStatusApi(jsonData);
                    }

                }
                else {
                    Toast.makeText(getActivity(), "Login Invalid - NO WATER REQ", Toast.LENGTH_SHORT).show();
                }
            }
        };
        drankUpBtn.setOnClickListener(drankUpBtnListener);

        return  v;

    }

    //Update water Progress
    public void postWaterStatusApi(JSONObject jsonData) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = "http://drinkup.atspace.cc/update_waterCompletionJson.php";


        // Request a JSON RESPONSE from the provided URL
        JsonObjectRequest json_obj_req = new JsonObjectRequest(Request.Method.PUT, url, jsonData, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){

            }

        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                error.printStackTrace();
            }

        });

        requestQueue.add(json_obj_req);
    }

    // Get Water Progress API
    public void getWaterProgressAPI(JSONObject dataJson) {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = "http://drinkup.atspace.cc/get_waterCompletionJson.php";

        // Request a JSON RESPONSE from the provided URL
        JsonObjectRequest json_obj_req = new JsonObjectRequest(Request.Method.POST, url, dataJson, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){

                try {
                    //Get values from JSON array
                    int responseNum = response.getInt("success");

                    Log.d("checking response", "onResponse: " + responseNum);

                    if (responseNum == 1) {
                        Log.d("checking response", "onResponse: " + responseNum);

                        //Getting user JSON array from response
                        JSONArray userWaterData;
                        userWaterData = response.getJSONArray("completion");

                        for (int i = 0; i<userWaterData.length(); i++) {
                            JSONObject userStatObject = userWaterData.getJSONObject(i);

                            user_waterStatus = userStatObject.getString("WaterCompletion");
                            user_waterDrankPerc = userStatObject.getInt("WaterDrankPerc");
                        }
//                        Toast.makeText(getActivity(), ""+ user_waterStatus, Toast.LENGTH_SHORT).show();
                        Log.d("DashBoardFrag WaterDrnk", "" + user_waterDrankPerc);
                        Log.d("DashBoardFrag WaterStat", "" + user_waterStatus);

                        //Setting the progress bar to Water Drank percentage
                        mWaveLoadingView.setProgressValue(user_waterDrankPerc);

                        if (user_waterStatus.matches("Completed") || user_waterDrankPerc == 100){
                            mWaveLoadingView.setCenterTitle("You reached your goal for the day");
                            drankUpBtn.setEnabled(false); //disable button
                            //Vibrate Effect
                            vibrator = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                            // Vibrate for 500 milliseconds
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                //deprecated in API 26
                                vibrator.vibrate(500);
                            }
                        }
                        else{
                            waterInML = (int)((user_waterDrankPerc/100f) * user_waterRequirement_posted);
                            if (waterInML == 0){
                                mWaveLoadingView.setCenterTitle("Your goal for today is " + user_waterRequirement_posted +"ml");
                            }
                            else{
                                mWaveLoadingView.setCenterTitle("You have drank " + waterInML + " ml");
                            }
                        }

                    }
                    else{
                        Toast.makeText(getContext(), "ID does not exist!", Toast.LENGTH_SHORT).show();
                    }

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                error.printStackTrace();
            }

        });

        requestQueue.add(json_obj_req);
    }

//    private void postAPIRequest(JSONObject jsonData) {
//    }


    // 1. Set the water increment value, as text on the button by pressing the water required

}


