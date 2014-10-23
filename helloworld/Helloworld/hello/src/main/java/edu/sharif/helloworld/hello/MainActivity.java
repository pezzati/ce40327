package edu.sharif.helloworld.hello;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends ActionBarActivity {
    private int pYear;
    private int pMonth;
    private int pDay;
    private int hour;
    private int min;


    private Button date_button;
    private TextView date_textview;

    private Button inc_hour;
    private Button dec_hour;
    private TextView hour_textview;

    private Button inc_min;
    private Button dec_min;
    private TextView min_textview;

    static final int DATE_DIALOG = 0;
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener(){
        public void onDateSet(DatePicker view, int year, int month, int day){
            pYear = year;
            pMonth = month;
            pDay = day;
            updateDisplay();

        }
    };

    private void updateDisplay(){
        date_textview.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(pMonth + 1).append("/")
                .append(pDay).append("/")
                .append(pYear).append(" "));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        //login
        final EditText user_text = (EditText) findViewById(R.id.user_field);
        final EditText pass_test = (EditText) findViewById(R.id.pass_field);
        Button save_button = (Button) findViewById(R.id.save_button);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user_name = String.valueOf(user_text.getText());
                final String password = String.valueOf(pass_test.getText());
                editor.putString("user",user_name);
                editor.putString("password", password);
                editor.commit();
            }
        });


        //set date
        date_textview = (TextView) findViewById(R.id.date_textview);
        date_button = (Button) findViewById(R.id.date_button);
        date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_DIALOG);
            }
        });

        final Calendar calendar = Calendar.getInstance();
        pYear = calendar.get(Calendar.YEAR);
        pMonth = calendar.get(Calendar.MONTH);
        pDay = calendar.get(Calendar.DAY_OF_MONTH);
        updateDisplay();


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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public FragmentManager getFragmentManager() {
        return super.getFragmentManager();
    }
}
