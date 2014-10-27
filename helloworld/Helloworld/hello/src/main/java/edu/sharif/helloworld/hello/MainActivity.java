package edu.sharif.helloworld.hello;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Calendar;


//TODO Today button in dialog


public class MainActivity extends ActionBarActivity {

    private String user_name;
    private String password;

    private int pYear;
    private int pMonth;
    private int pDay;
    private int hour;
    private int min;

    private ImageButton date_button;
    private TextView date_textview;

    private Button inc_hour;
    private Button dec_hour;
    private TextView hour_textview;

    private Button inc_min;
    private Button dec_min;
    private TextView min_textview;

    private Button call_button;
    private EditText phone_text;
    static final int DATE_DIALOG = 0;

    private ImageButton wakeUp_button;

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener(){
        public void onDateSet(DatePicker view, int year, int month, int day){
            pYear = year;
            pMonth = month;
            pDay = day;
            updateDisplay();

        }
    };



    private class Get_call extends AsyncTask<String, Object, Object>{

        private HttpClient httpClient;
        private String url;
        private InputStream inputStream;

        @Override
        protected void onPreExecute() {
            httpClient = new DefaultHttpClient();
            url = "http://wake.huri.ir/wake/?user=";
        }

        @Override
        protected String doInBackground(String... strings) {
            String res = "";
            url = url + URLEncoder.encode(strings[0]) + "&pass=" + URLEncoder.encode(strings[1]) + "&d=" + URLEncoder.encode(strings[2]) + "&t=" + URLEncoder.encode(strings[3]) + "&p=" + URLEncoder.encode(strings[4]);
            try {
                HttpResponse httpResponse = httpClient.execute(new HttpGet(url));
                inputStream = httpResponse.getEntity().getContent();
                if(inputStream == null){
                    System.out.println("Error!!!! inputStream is null");
                    res = null;
                }
                else{
                    res = convertInputStreamToString(inputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Exception in sending req" + e.getMessage());
            }
            return res;
        }

        @Override
        protected void onPostExecute(Object o) {
            String input = (String) o;
            //System.out.println("###" + input);
            if(input == null){
                Toast.makeText(getApplicationContext(), "Something was wrong. Please try later", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                if(input.equals("1")){
                    Toast.makeText(getApplicationContext(), "Your Wake up call request Accepted", Toast.LENGTH_LONG).show();
                    return;
                }
                if(input.equals("2")){
                    Toast.makeText(getApplicationContext(), "Username or Password is incorrect", Toast.LENGTH_LONG).show();
                    return;
                }
                if(input.equals("3")){
                    Toast.makeText(getApplicationContext(), "Some of the parameters are wrong", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }


    private class Get_wake extends AsyncTask<String, Object, Object>{
        HttpClient httpClient;
        String url;
        @Override
        protected void onPreExecute() {
            httpClient = new DefaultHttpClient();
            url = "http://wake.huri.ir/call/?user=";
        }

        @Override
        protected String doInBackground(String... strings) {
            String res = "";
            InputStream inputStream;
            url = url + URLEncoder.encode(strings[0]) + "&pass=" + URLEncoder.encode(strings[1]);
            try {
                HttpResponse httpResponse = httpClient.execute(new HttpGet(url));
                inputStream = httpResponse.getEntity().getContent();
                if(inputStream == null){
                    System.out.println("Error!!!! inputStream is null");
                    res = null;
                }
                else{
                    res = convertInputStreamToString(inputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Exception in sending req" + e.getMessage());
            }
            return res;
        }

        @Override
        protected void onPostExecute(Object o) {
            String input = (String) o;
            if(input == null){
                Toast.makeText(getApplicationContext(), "Something was wrong. Please try later.", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                if(input.equals("0")){
                    Toast.makeText(getApplicationContext(), "No phone number received", Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    Intent intent_call = new Intent();
                    intent_call.setAction(Intent.ACTION_CALL);
                    intent_call.setData(Uri.parse("tel:" + input));
                    startActivity(intent_call);
                }
            }
        }
    }


    // convert inputStream to String
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


    private void updateDisplay(){
        date_textview.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(pMonth + 1).append("/")
                .append(pDay).append("/")
                .append(pYear).append(" "));
    }


    private void today(){
        Calendar calendar = Calendar.getInstance();
        pYear = calendar.get(Calendar.YEAR);
        pMonth = calendar.get(Calendar.MONTH);
        pDay = calendar.get(Calendar.DAY_OF_MONTH);
        updateDisplay();
        switch (calendar.get(Calendar.AM_PM)){
            case Calendar.AM:
                hour = calendar.get(Calendar.HOUR);
                break;
            case Calendar.PM:
                hour = (calendar.get(Calendar.HOUR) + 12) % 24;
        }
        min = calendar.get(Calendar.MINUTE);
        hour_textview.setText(new StringBuilder().append(hour));
        min_textview.setText(new StringBuilder().append(min));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get username and password from intent
        user_name = getIntent().getStringExtra("user");
        password = getIntent().getStringExtra("password");



        //date
        date_textview = (TextView) findViewById(R.id.date_textview);
        date_button = (ImageButton) findViewById(R.id.date_button);
        date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_DIALOG);
            }
        });

        final Calendar calendar = Calendar.getInstance();

        //time
        inc_hour = (Button) findViewById(R.id.inc_hour_button);
        hour_textview = (TextView) findViewById(R.id.hour_text);
        dec_hour = (Button) findViewById(R.id.dec_hour_button);

        inc_hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hour = (hour + 1) % 24;
                hour_textview.setText(String.valueOf(hour));
            }
        });

        dec_hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hour = (hour + 23) % 24;
                hour_textview.setText(String.valueOf(hour));
            }
        });

        inc_min = (Button) findViewById(R.id.inc_min_button);
        min_textview = (TextView) findViewById(R.id.min_text);
        dec_min = (Button) findViewById(R.id.dec_min_button);

        inc_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                min = (min + 1) % 60;
                min_textview.setText(String.valueOf(min));
            }
        });

        dec_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                min = (min + 59) % 60;
                min_textview.setText(String.valueOf(min));
            }
        });

        //Set date and time of NOW!!!
        today();

        //Call
        phone_text = (EditText) findViewById(R.id.phone_number_text);
        call_button = (Button) findViewById(R.id.call_button);
        call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phone_text.getText() == null){
                    Toast.makeText(getApplicationContext(), "Write the Phone number", Toast.LENGTH_SHORT);
                    return;
                }
                else{
                    Get_call getCall = new Get_call();
                    getCall.execute(new String[] {user_name,
                                            password,
                                            String.valueOf(pYear) + "-" +String.valueOf(pMonth + 1) + "-" + String.valueOf(pDay),
                                            String.valueOf(hour) + "-" +String.valueOf(min),
                                            phone_text.getText().toString()});
                }

            }
        });

        //Wake
        wakeUp_button = (ImageButton) findViewById(R.id.wakeUp_button);
        wakeUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Get_wake getWake = new Get_wake();
                getWake.execute(new String[] {user_name,password});

            }
        });

    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this,
                        dateSetListener,
                        pYear, pMonth, pDay);
        }
        return null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent log_intent = new Intent(MainActivity.this, log_in.class);
            this.startActivity(log_intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public FragmentManager getFragmentManager() {
        return super.getFragmentManager();
    }


    @Override
    protected void onStart() {
        today();
        super.onStart();
    }


    @Override
    protected void onRestart() {
        today();
        super.onRestart();
    }
}
