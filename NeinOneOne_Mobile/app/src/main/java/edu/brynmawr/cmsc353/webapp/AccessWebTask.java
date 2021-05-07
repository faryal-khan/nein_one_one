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
            JSONObject jo = new JSONObject(response);
            /*
            for (int i = 0; i < jo.length(); i++){
                name = jo.getJSONObject(i).get("name").toString();

                if (jo.getJSONObject(i).get("phone").toString() == null)
                    phone = "This resource did not provide a phone number";
                else phone = jo.getJSONObject(i).get("phone").toString();

                if (jo.getJSONObject(i).get("website").toString() == null)
                    website = "This resource did not provide a website";
                else
                    website = jo.getJSONObject(i).get("website").toString();
                description = "desc";
                //if (jo.getJSONObject(i).get("description").toString() == null)
                //    description = "This resource did not provide a description";
                //else
                //    description = jo.getJSONObject(i).get("description").toString();
                pwd[0] = phone;
                pwd[1] = website;
                pwd[2] = description;
                //Log.v("age", description);
                resources.put(name, pwd);
            }
             */



            res += "name: " + jo.get("name").toString() + "\n";
            res += "phone: " + jo.get("phone").toString() + "\n";
            if (jo.get("website").toString() == null || jo.get("website").toString().equals(""))
                res += "";
            else res += "website: " + jo.get("website").toString() + "\n";
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


                /*
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                }
                 */
            };
            ss.setSpan(clickableSpan, 0, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return res;

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
    private String dialContactPhone(String phoneNumber) {
            new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
            return phoneNumber;
    }
    @Override
    protected void onPostExecute(String s) {
        // this method would be called in the UI thread after doInBackground finishes
        // it can access the Views and update them asynchronously
    }

}
