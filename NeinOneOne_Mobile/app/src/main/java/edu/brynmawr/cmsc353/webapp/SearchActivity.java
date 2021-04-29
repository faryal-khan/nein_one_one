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

            URL url = new URL("http://10.0.2.2:3000/api");

            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            String request = editTextSearch.getText().toString();
            String skip = task.get();
            //skip = task.doInBackground(url);
            //String name = task.getName();
            resources = task.getResources();
            String phone = resources.get(request)[0];
            String website = resources.get(request)[1];
            //String description = resources.get(request)[2];
            String pwd = "Phone: " + phone + "\n" + "Website: " + website;
            if (pwd == null){
                pwd = "Sorry, there are no resources with this name";
            }
            //if (name == null) name = "name is null";
            res.setText(pwd);
        } catch (Exception e) {
            // uh oh
            e.printStackTrace();
            res.setText(e.toString());
        }
    }

    public void onFinishButtonClick(View v) {

        setResult(RESULT_OK, i);
        finish();
    }
/*
    public String getResource(String name) {
        TextView res = findViewById(R.id.result);
        try {

            URL url = new URL("http://10.0.2.2:3000/all");
            i.putExtra("NAME", name);
            startActivity(i);
            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            String status = task.get();
            res.setText(status);
        } catch (Exception e) {
            // uh oh
            e.printStackTrace();
            res.setText(e.toString());
        }
    }
    */
}
