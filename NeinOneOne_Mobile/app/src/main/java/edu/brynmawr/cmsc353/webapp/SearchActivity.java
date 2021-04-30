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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        editTextSearch = (EditText) findViewById(R.id.editSearch);
    }

    public void onSearchButtonClick(View v) {

        TextView res = findViewById(R.id.result);

        try {
            String request = editTextSearch.getText().toString();
            if (request.equals("") || request.isEmpty() || request == null || request.equals(" ")){
                res.setText("Please enter a valid name");
            }
            else {
                URL url = new URL("http://10.0.2.2:3000/api?name=" + request);

                AccessWebTask task = new AccessWebTask();
                task.execute(url);
                //String pwd;
            /*
            if ( editTextSearch.getText().toString() == null) {
                pwd = "Please enter the name of the resource";
            }
            else {
            */
                //resources = task.getResources();

                String pwd = task.get();

                //String phone = resources.get(request)[0];
                //String website = resources.get(request)[1];
                //String description = resources.get(request)[2];

                //pwd = "Phone: " + phone + "\n" + "Website: " + website
                if (pwd == null) {
                    pwd = "Sorry, there are no resources with this name";
                }
                res.setText(pwd);
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
