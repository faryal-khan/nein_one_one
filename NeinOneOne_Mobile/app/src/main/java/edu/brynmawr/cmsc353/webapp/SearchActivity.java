package edu.brynmawr.cmsc353.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URL;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {
    EditText editTextSearch;
    double myLongitude;
    double myLatitude;
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

                edu.brynmawr.cmsc353.project.AccessWebTask task = new edu.brynmawr.cmsc353.project.AccessWebTask();
                task.execute(url);
                //String pwd;
            /*
            if ( editTextSearch.getText().toString() == null) {
                pwd = "Please enter the name of the resource";
            }
            else {
            */
                resources = task.getResources();

                String pwd = task.get();

                for(String name : resources.keySet()) {
                    if (name.contains(request)) {
                        resource = resources.get(name);
                        if(distance(myLatitude, myLongitude, Double.parseDouble(resource[5]), Double.parseDouble(resource[6])) <= 10) {
                            result += "name: " + name + "\n";
                            result += "phone: " + resource[0] + "\n";

                            if (resource[1].equals("")) result += "";
                            else result += "website: " + resource[1] + "\n";
                            if (resource[2].equals("")) result += "";
                            else result += "description: " + resource[2] + "\n";
                            result += "location: " + resource[3] + "\n";
                            result += "zipcode: " + resource[4] + "\n";
                            result += "latitude: " + resource[5] + "\n";
                            result += "longitude: " + resource[6] + "\n";
                            result += "\n";
                        }
                    }
                }

                /*
                if (resource[1].equals(""))
                    result += "";
                else result += "website: " + resource[1] + "\n";
                if (resource[2].equals(""))
                    result += "";
                else result += "description: " + resource[2] + "\n";
                if (resource[3].equals(""))
                    result += "";
                else result += "location: " + resource[3] + "\n";
                if (resource[4].equals(""))
                    result += "";
                else result += "zipcode: " + resource[4] + "\n";
                if (resource[5].equals(""))
                    result += "";
                else result += "latitude: " + resource[5] + "\n";
                if (resource[6].equals(""))
                    result += "";
                else result += "longitude: " + resource[6] + "\n";
*/

                //pwd = "Phone: " + phone + "\n" + "Website: " + website
                if (pwd == null) {
                    pwd = "Sorry, there are no resources with this name";
                }

                //  System.out.println("RESULT" + resources.keySet());

                res.setText(result);
            }
        } catch (Exception e) {
            // uh oh
            e.printStackTrace();
            res.setText("The name is invalid or the resource was not found");
        }
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

    public void onFinishButtonClick(View v) {

        setResult(RESULT_OK, i);
        finish();
    }
}
