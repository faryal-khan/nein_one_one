package edu.brynmawr.cmsc353.project;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class AccessWebTask extends AsyncTask<URL, String, String> {
    HashMap<String, String[]> resources = new HashMap<>();
    String[] pwd = new String[3];
    String name, phone, website, description;
    public HashMap<String, String[]> getResources(){
        return resources;
    }
    //debugging
    public String getName(){
        return name;
    }
    public String getPhone(){
        return phone;
    }
    public String getDescription(){
        return description;
    }
    public String getWebsite(){
        return website;
    }
    public String doInBackground(URL... urls) {

        try {
            URL url = urls[0];

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            Scanner in = new Scanner(url.openStream());
            String response = in.nextLine();
            String res = "";
            JSONArray jo = new JSONArray(response);
            for (int i = 0; i < jo.length(); i++){
                name = jo.getJSONObject(i).get("name").toString();
                phone = jo.getJSONObject(i).get("phone").toString();
                website = jo.getJSONObject(i).get("website").toString();
                //phone = "phone";
                //website = "website";
                description = "desc";
                //description = jo.getJSONObject(i).get("description").toString();
                pwd[0] = phone;
                pwd[1] = website;
                pwd[2] = description;
                //Log.v("age", description);
                resources.put(name, pwd);
            }
            return "";
        }
        catch (Exception e) {
            return e.toString();
        }
    }
    @Override
    protected void onPostExecute(String s) {
        // this method would be called in the UI thread after doInBackground finishes
        // it can access the Views and update them asynchronously
    }

}
