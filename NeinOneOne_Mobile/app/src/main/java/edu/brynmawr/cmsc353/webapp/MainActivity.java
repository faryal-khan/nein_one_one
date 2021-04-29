package edu.brynmawr.cmsc353.project;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public static final int SEARCH_ACTIVITY_ID = 1;
    public static final int ADD_NEW_ACTIVITY_ID = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onAddNewButtonClick(View v) {

        Intent i = new Intent(this, AddNewActivity.class);
        startActivityForResult(i,ADD_NEW_ACTIVITY_ID);

    }
    public void onSearchButtonClick(View v){
        Intent i = new Intent(this, SearchActivity.class);
        startActivityForResult(i,SEARCH_ACTIVITY_ID);
    }
}
/*
    public void onAddNewButtonClick(View v) {

        EditText tv = findViewById(R.id.webField);
        EditText tv2 = findViewById(R.id.nameField);

        try {
            // assumes that there is a server running on the AVD's host on port 3000
            // and that it has a /test endpoint that returns a JSON object with
            // a field called "status"
            String result = "http:10.0.2.2:3000";
            result += "name="+tv2.getText()+ "&age="+tv.getText();
            URL url = new URL(result);
            tv.setText(result);
            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            Toast.makeText(MainActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            // uh oh
            e.printStackTrace();
            tv.setText(e.toString());
        }


    }
}
*/
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
