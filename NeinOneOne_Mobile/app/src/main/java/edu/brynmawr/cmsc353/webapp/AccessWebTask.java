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
    HashMap<String, String> resources = new HashMap<>();
    String name, description = "temp";
    public HashMap<String, String> getResources(){
        return resources;
    }
    public String getName(){
        return name;
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
                //name = jo.getJSONObject(i).get("name").toString();
                name = "name";
                description = "desc";
                Log.v("name", name);
                //change names
                //description = jo.getJSONObject(i).get("age").toString();
                Log.v("age", description);
                resources.put(name, description);
            }
            return "";
        }
        catch (Exception e) {
            return null;
        }
    }
    @Override
    protected void onPostExecute(String s) {
        // this method would be called in the UI thread after doInBackground finishes
        // it can access the Views and update them asynchronously
    }

}
