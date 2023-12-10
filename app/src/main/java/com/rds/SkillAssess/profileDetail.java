package com.rds.SkillAssess;

import android.net.Uri;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

class profileDetail {
    String Brithdate,FName,LName,Gender = "";
    Uri profileUri = null;
    int Total_Score = 0;

    public profileDetail()
    {
        DataQuery dq = new DataQuery();
        dq.fetchDetails(data -> {
            if (data.containsKey("Error")) {
                // Handle the error case
                String errorMessage = (String) data.get("Error");
                Log.d("profileDetail", "onDataReceived: "+errorMessage);
            } else {
                // Data retrieval was successful
                updateDetail(data);
            }
        });
    }

    protected void updateDetail(Map<String, Object> data) {
        String[] fullName = data.get("Full_name").toString().split(" ");
        this.FName = fullName.length > 0 ? fullName[0] : "";
        this.LName = fullName.length > 1 ? fullName[1] : "";
        this.Brithdate = data.get("Birthdate") != null ? data.get("Birthdate").toString() : "";
        this.Gender = data.get("Gender") != null ? data.get("Gender").toString() : "";
        this.profileUri = data.get("Profile_Image") == null ? null : (Uri) data.get("Profile_Image");
        this.Total_Score = data.get("Total_Score") != null ? ((Number) data.get("Total_Score")).intValue() : 0;
    }

    protected Map<String,Object> getData()
    {
        Map<String, Object> data = new HashMap<>();
        data.put("first_Name", this.FName);
        data.put("last_Name", this.LName);
        data.put("Birthdate", this.Brithdate);
        data.put("Gender", this.Gender);
        data.put("profileUri", this.profileUri);

        return data;
    }

    public Uri getprofileUri()
    {
        if(this.profileUri == null)
        {
            return null;
        }
        return this.profileUri;
    }

    public String getFullName() {
        if(this.FName == null || this.FName.isEmpty())
        {
            return null;
        }
        return this.FName + " " + this.LName;
    }
}