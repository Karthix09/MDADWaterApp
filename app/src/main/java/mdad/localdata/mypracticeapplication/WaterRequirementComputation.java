package mdad.localdata.mypracticeapplication;

public class WaterRequirementComputation {

    int user_waterRequirement;

    public int waterRequirement(int user_weight) {
        if (user_weight >= 20 && user_weight < 45){
            user_waterRequirement = 1500;
        }
        else if(user_weight >= 45 && user_weight < 60){
            user_waterRequirement = 2000;
        }
        else if(user_weight >= 60 && user_weight < 75){
            user_waterRequirement = 2500;
        }
        else if(user_weight >= 75 && user_weight < 90){
            user_waterRequirement = 3000;
        }
        else if(user_weight >= 90 && user_weight < 120){
            user_waterRequirement = 3500;
        }
        else{
            user_waterRequirement = 4000;
        }
        return user_waterRequirement;
    }

}
