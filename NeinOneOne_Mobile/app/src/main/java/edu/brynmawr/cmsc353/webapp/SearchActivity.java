package edu.brynmawr.cmsc353.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.net.URL;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;
    String phone, pname = "";
    EditText editTextSearch;
    double myLongitude, myLatitude;
    LocationManager mLocationManager;
    Intent i = new Intent();
    HashMap<String, String[]> resources = new HashMap<>();
    String[] resource = new String[7];

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            myLatitude = location.getLatitude();
            myLongitude = location.getLongitude();

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @SuppressLint("MissingPermission")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        editTextSearch = (EditText) findViewById(R.id.editSearch);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1,
                1, mLocationListener);

}

    public void onSearchButtonClick(View v) {
        String result = "";
        TextView res = findViewById(R.id.result);

        try {
            String request = editTextSearch.getText().toString();
            if (request.equals("") || request.isEmpty() || request == null || request.equals(" ")){
                res.setText("Please enter a valid name");
            }
            else {
                URL url = new URL("http://10.0.2.2:3000/api");

                AccessWebTask task = new AccessWebTask();
                task.execute(url);


                String lat = task.get();
                resources = task.getResources();

                HashMap<String, String> phoneInd = new HashMap<>();
                String indxs = "";


                for(String name : resources.keySet()) {
                    if (name.contains(request)) {
                        resource = resources.get(name);
                        if(distance(myLatitude, myLongitude, Double.parseDouble(resource[5]),
                                Double.parseDouble(resource[6])) <= 10) {
                            pname = name;
                            result += "name: " + name + "\n";
                            phone = resource[0];
                            result += "phone: " + phone + "\n";
                            indxs = String.valueOf(result.length()-phone.length() - 1) + "," + result.length();
                            phoneInd.put(indxs, phone);
                            if (resource[1].equals("") || resource[1].isEmpty()) result += "";
                            else result += "website: " + resource[1] + "\n";

                            if (resource[2].equals("") || resource[2].isEmpty()) result += "";
                            else result += "description: " + resource[2] + "\n";

                            if (resource[3].equals("")|| resource[3].isEmpty()) result += "";
                            else result += "location: " + resource[3] + "\n";

                            if (resource[4].equals("") || resource[4].isEmpty()) result += "";
                            else result += "zipcode: " + resource[4] + "\n";

                            if (resource[5].equals("")|| resource[5].isEmpty()) result += "";
                            else result += "latitude: " + resource[5] + "\n";

                            if (resource[6].equals("")|| resource[6].isEmpty()) result += "";
                            else result += "longitude: " + resource[6] + "\n";
                            result += "\n";

                        }
                    }
                }

                if (result == null || result.equals("") || result.isEmpty()) {
                    result = "Sorry, there are no resources with this name nearby";
                    res.setText(result);
                }
                else {
                    SpannableString ss = new SpannableString(result);
                    for (String index : phoneInd.keySet()){
                        ClickableSpan cs1 = new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View widget) {
                                String dial = "tel:" + phoneInd.get(index);
                                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                            }
                        };
                        int start = Integer.parseInt(index.substring(0,index.indexOf(',')));
                        int finish = Integer.parseInt(index.substring(index.indexOf(',')+1));
                        ss.setSpan(cs1, start, finish, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //System.out.println(distance(myLatitude, myLongitude, Double.parseDouble(resource[5]),
                        //        Double.parseDouble(resource[6])));
                    }
                    res.setText(ss);
                    res.setMovementMethod(LinkMovementMethod.getInstance());
                }
            }
        } catch (Exception e) {
            // uh oh
            //e.printStackTrace();
            res.setText(e.toString());
        }
    }

    public void onFinishButtonClick(View v) {

        setResult(RESULT_OK, i);
        finish();
    }

    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;

        return dist; // output distance, in MILES
    }
}
