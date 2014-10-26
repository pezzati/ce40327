package edu.sharif.helloworld.hello;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class log_in extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        final android.content.SharedPreferences.Editor editor = sharedPreferences.edit();

        //login
        final EditText user_text = (EditText) findViewById(R.id.user_field);
        final EditText pass_text = (EditText) findViewById(R.id.pass_field);
        Button save_button = (Button) findViewById(R.id.save_button);

        final Intent main_intent = new Intent(log_in.this, MainActivity.class);

        if(sharedPreferences.contains("user") && sharedPreferences.contains("password")){
            main_intent.putExtra("user", sharedPreferences.getString("user",""));
            main_intent.putExtra("password", sharedPreferences.getString("password",""));
            //log_in.this.startActivity(main_intent);
            //user_text.setText(sharedPreferences.getString("user", ""));
            //pass_text.setText(sharedPreferences.getString("password", ""));


        }

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user_name = String.valueOf(user_text.getText());
                final String password = String.valueOf(pass_text.getText());
                if(!(sharedPreferences.contains("user") && sharedPreferences.contains("password"))){
                    editor.putString("user",user_name);
                    editor.putString("password", password);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "Username and Password saved", Toast.LENGTH_SHORT).show();
                }
                main_intent.putExtra("user", user_name);
                main_intent.putExtra("password", password);
                log_in.this.startActivity(main_intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.log_in, menu);
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

}
