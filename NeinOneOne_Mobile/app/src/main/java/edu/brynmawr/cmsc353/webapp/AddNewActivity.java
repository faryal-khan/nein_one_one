package edu.brynmawr.cmsc353.project;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AddNewActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
    }

    public void onConnectButtonClick(View v) {
        EditText webText = findViewById(R.id.webField);
        EditText nameText = findViewById(R.id.nameField);
        EditText phoneText = findViewById(R.id.phoneField);
        EditText descText = findViewById(R.id.descField);

        new AsyncTask<String, String, String>() {

            // 3. This method waits for 3 seconds
            protected String doInBackground(String... inputs) {
                try {
                    // creates an object to represent the URL
                    URL url = new URL("http:10.0.2.2:3000/suggest");

                    // represents the connection to the URL
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                    //conn.setRequestProperty("Content-Type", "application/json; utf-8");
                    conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");


                    // this is the JSON that you want to send
                    // note that you need double-quotes around strings
                    // and need to escape those double-quotes with the backslash character
                    String json = "{\"name\":\"" + nameText.getText() + "\",\"phone\":\" " + phoneText.getText() + "\" "+ ", \"description\":\" "
                            + descText.getText() +"\",\"website\":\"" + webText.getText() + " \"}";

                    byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

                    try(OutputStream os = conn.getOutputStream()) {
                        os.write(bytes, 0, bytes.length);
                    }
                    conn.connect();


                    // now the response comes back
                    int responsecode = conn.getResponseCode();

                    // make sure the response has "200 OK" as the status
                    if (responsecode != 200) {
                        System.out.println("Oops: " + responsecode);
                    }
                    else {

                        try(BufferedReader br = new BufferedReader(
                                new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                            StringBuilder response = new StringBuilder();
                            String responseLine = null;
                            while ((responseLine = br.readLine()) != null) {
                                response.append(responseLine.trim());
                            }
                            System.out.println(response.toString());
                        }
                    }

                    System.out.println("DONE");



                }
                catch (Exception e) {
                    System.out.println(e.toString());
                }
                return null;
            }

            // 4. This method will be run after doInBackground finishes
            protected void onPostExecute(String input) {
                Toast.makeText(AddNewActivity.this,"Successfully added resource! Our admin team will be in touch soon.",  Toast.LENGTH_SHORT).show();
                phoneText.setText("");
                descText.setText("");
                nameText.setText("");
                webText.setText("");
            }
        }.execute();

    }
    public void onFinishButtonClick(View v) {
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
    }
}