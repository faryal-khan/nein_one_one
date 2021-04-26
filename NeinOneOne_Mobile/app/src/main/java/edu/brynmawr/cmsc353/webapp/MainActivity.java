package edu.brynmawr.cmsc353.webapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onConnectButtonClick(View v) {

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