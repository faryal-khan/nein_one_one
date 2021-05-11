package edu.brynmawr.cmsc353.project;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

import static androidx.core.content.ContextCompat.startActivity;

public class AccessWebTask extends AsyncTask<URL, String, String> {
    HashMap<String, String[]> resources = new HashMap<>();
    String[] attributes = new String[7];
    String name, phone, website, description, lat, lon, location, zipcode;
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
            //JSONObject jo = new JSONObject(response);
            JSONArray jo = new JSONArray(response);

            for (int i = 0; i < jo.length(); i++){
                name = jo.getJSONObject(i).get("name").toString();
                //res += "name: " + name + "\n";
                phone = jo.getJSONObject(i).get("phone").toString();
                //res += "phone: " + phone + "\n";
                if (jo.getJSONObject(i).get("website").toString() == null ||
                        jo.getJSONObject(i).get("website").toString().equals(""))
                    website = "";
                 //   res += "";
                else {
                    website = jo.getJSONObject(i).get("website").toString();
                    //res += "website: " + website + "\n";
                }
                if (jo.getJSONObject(i).get("description").toString() == null ||
                        jo.getJSONObject(i).get("description").toString().equals(""))
                    description = "";
                    //res += "";
                else {
                    description = jo.getJSONObject(i).get("description").toString();
                    //res += "description: " + website + "\n";
                }
                if (jo.getJSONObject(i).get("location").toString() == null ||
                        jo.getJSONObject(i).get("location").toString().equals(""))
                    location = "";
                    //res += "";
                else {
                    location = jo.getJSONObject(i).get("location").toString();
                    //res += "description: " + website + "\n";
                }
                if (jo.getJSONObject(i).get("zipcode").toString() == null ||
                        jo.getJSONObject(i).get("zipcode").toString().equals(""))
                    zipcode = "";
                    //res += "";
                else {
                    location = jo.getJSONObject(i).get("zipcode").toString();
                    //res += "description: " + website + "\n";
                }
                if (jo.getJSONObject(i).get("latitude").toString() == null ||
                        jo.getJSONObject(i).get("latitude").toString().equals(""))
                    lat = "";
                    //res += "";
                else {
                    lat = jo.getJSONObject(i).get("latitude").toString();
                    //res += "description: " + website + "\n";
                }
                if (jo.getJSONObject(i).get("longitude").toString() == null ||
                        jo.getJSONObject(i).get("longitude").toString().equals(""))
                    lon = "";
                    //res += "";
                else {
                    lon = jo.getJSONObject(i).get("longitude").toString();
                    //res += "description: " + website + "\n";
                }


                attributes[0] = phone;
                attributes[1] = website;
                attributes[2] = description;
                attributes[3] = location;
                attributes[4] = zipcode;
                attributes[5] = lat;
                attributes[6] = lon;

                //Log.v("age", description);
                resources.put(name, attributes);
            }
            return res;

            /*
            res += "name: " + jo.get("name").toString() + "\n";
            res += "phone: " + jo.get("phone").toString() + "\n";
            if (jo.get("website").toString() == null || jo.get("website").toString().equals(""))
                res += "";
            else {
                res += "website: " + jo.get("website").toString() + "\n";
            }
            if (jo.get("description").toString() == null || jo.get("description").toString().equals(""))
                res += "";
            else res += "description: " + jo.get("description").toString() + "\n";
            if (jo.get("description").toString() == null || jo.get("description").toString().equals(""))
                res += "";
            else res += "description: " + jo.get("description").toString() + "\n";
            SpannableString ss = new SpannableString(jo.get("phone").toString());
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    try {
                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", jo.get("phone").toString(), null));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            ss.setSpan(clickableSpan, 0, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return res;
*/
        }
        catch (Exception e) {
            String ex = "";
            if (phone == null && website == null && description == null)
                return "There are no resources with this name";
            if (name == null || name.isEmpty()) ex += "Name is empty, ";
            else ex += "name:" + name + "\n";
            if (phone == null || phone.isEmpty()) ex += "Phone is empty, ";
            else ex += "phone: " + phone + "\n";
            if (website == null || website.isEmpty()) ex += "Website is empty, ";
            else ex += "website: " + website + "\n";
            if (description == null || description.isEmpty()) ex += "Description is empty.";
            else ex += "description: "  + description;
            return ex;
        }
    }
    @Override
    protected void onPostExecute(String s) {
        // this method would be called in the UI thread after doInBackground finishes
        // it can access the Views and update them asynchronously
    }

}
