package com.bcit.cheung.jason.asynclab;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ToastTask aTask = new ToastTask(this);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                aTask.execute("I waited 2 seconds");
            }
        }, 2000);
    }

    public void showToast(String message) {
        Toast t = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        t.show();
    }

    class ToastTask extends AsyncTask<String, String, Void> {

        MainActivity theActivity;
        String message;

        ToastTask(MainActivity a) {
            this.theActivity = a;
        }

        @Override
        protected Void doInBackground(String... params) {
            // LAB START
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Message", Toast.LENGTH_SHORT).show();
                }
            });
            // LAB END

            message = params[0];
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            theActivity.showToast(message);
        }
    }

}
