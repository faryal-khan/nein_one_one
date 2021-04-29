package edu.brynmawr.cmsc353.project;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final int SEARCH_ACTIVITY_ID = 1;
    public static final int ADD_NEW_ACTIVITY_ID = 2;
    private static final String POST_PARAMS = "name=Fred";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onAddNewButtonClick(View v) {

        Intent i = new Intent(this, edu.brynmawr.cmsc353.project.AddNewActivity.class);
        startActivityForResult(i,ADD_NEW_ACTIVITY_ID);

    }
    public void onSearchButtonClick(View v){
        Intent i = new Intent(this, edu.brynmawr.cmsc353.project.SearchActivity.class);
        startActivityForResult(i,SEARCH_ACTIVITY_ID);
    }
}

/*
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
EditText editTextSearch;
TextView res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextSearch = (EditText)findViewById(R.id.editSearch);
    }

    public void onConnectButtonClick(View v) {

        TextView tv = findViewById(R.id.statusField);

        try {
            // assumes that there is a server running on the AVD's host on port 3000
            // and that it has a /test endpoint that returns a JSON object with
            // a field called "status"
            URL url = new URL("http://10.0.2.2:3000/api?name=Nigina");

            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            String status = task.get();

            tv.setText(status);

        } catch (Exception e) {
            // uh oh
            e.printStackTrace();
            tv.setText(e.toString());
        }
    }
    public void onSearchButtonClick(View v) {

        TextView res = findViewById(R.id.result);
        //editTextSearch = (EditText)findViewById(R.id.editSearch);
        //System.out.println(res.toString());

        try {
            // assumes that there is a server running on the AVD's host on port 3000
            // and that it has a /test endpoint that returns a JSON object with
            // a field called "status"

            URL url = new URL("http://10.0.2.2:3000/api?name=".concat(editTextSearch.getText().toString()));

            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            String status = task.get();
            String test = editTextSearch.getText().toString();
            res.setText(test);

        } catch (Exception e) {
            // uh oh
            e.printStackTrace();
            res.setText(e.toString());
        }
    }

}
*/
