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


    String name, phone, website, description, lat, lon, location, zipcode = "";
    public HashMap<String, String[]> getResources(){
        return resources;
    }
    //debugging
    public String getName(){
        return name;
    }
    public String getLat(String name){
        return resources.get(name)[5];
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
                String[] attributes = new String[8];
                for(int j = 0; j < 8; j++){
                    attributes[j] = "";
                }
                name = jo.getJSONObject(i).get("name").toString();
                phone = jo.getJSONObject(i).get("phone").toString();

                if (jo.getJSONObject(i).get("website") == null ||
                        jo.getJSONObject(i).get("website").toString().equals("") ||
                        jo.getJSONObject(i).get("website").toString().isEmpty())
                    website = "";
                else website = jo.getJSONObject(i).get("website").toString();

                if (jo.getJSONObject(i).get("description") == null ||
                        jo.getJSONObject(i).get("description").toString().equals("") ||
                        jo.getJSONObject(i).get("description").toString().isEmpty())
                    description = "";
                else description = jo.getJSONObject(i).get("description").toString();

                if (jo.getJSONObject(i).get("location") == null ||
                        jo.getJSONObject(i).get("location").toString().equals("") ||
                        jo.getJSONObject(i).get("location").toString().isEmpty())
                    location = "";
                else location = jo.getJSONObject(i).get("location").toString();

                if (jo.getJSONObject(i).get("zipcode") == null ||
                        jo.getJSONObject(i).get("zipcode").toString().equals("") ||
                        jo.getJSONObject(i).get("zipcode").toString().isEmpty())
                    zipcode = "";
                else zipcode = jo.getJSONObject(i).get("zipcode").toString();

                if (jo.getJSONObject(i).get("latitude") == null ||
                        jo.getJSONObject(i).get("latitude").toString().equals("") ||
                        jo.getJSONObject(i).get("latitude").toString().isEmpty())
                    lat = "";
                else lat = jo.getJSONObject(i).get("latitude").toString();

                if (jo.getJSONObject(i).get("longitude") == null ||
                        jo.getJSONObject(i).get("longitude").toString().equals("") ||
                        jo.getJSONObject(i).get("longitude").toString().isEmpty())
                    lon = "";
                else lon = jo.getJSONObject(i).get("longitude").toString();

                attributes[0] = phone;
                attributes[1] = website;
                attributes[2] = description;
                attributes[3] = location;
                attributes[4] = zipcode;
                attributes[5] = lat;
                attributes[6] = lon;
                attributes[7] = name;

                //Log.v("name1", name);
                resources.put(name, attributes);
                //attributes[0] = "";
            }
            //return resources;
            return resources.get("reston1")[0];
            //return jo.getJSONObject(0).get("name").toString();

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
