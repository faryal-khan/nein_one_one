package edu.brynmawr.cmsc353.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URL;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {
    EditText editTextSearch;
    Intent i = new Intent();
    HashMap<String, String[]> resources = new HashMap<>();
    String[] resource = new String[7];


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        editTextSearch = (EditText) findViewById(R.id.editSearch);
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
                URL url = new URL("http://10.0.2.2:3000/api?");

                AccessWebTask task = new AccessWebTask();
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
                res.setText(result);
            }
        } catch (Exception e) {
            // uh oh
            //e.printStackTrace();
            res.setText("The name is invalid or the resource was not found");
        }
    }

    public void onFinishButtonClick(View v) {

        setResult(RESULT_OK, i);
        finish();
    }
}
