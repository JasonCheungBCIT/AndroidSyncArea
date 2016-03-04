package cheung.jason.asynctest;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
	public static final String MAIN_KEY = "user_input";
	public static final int REQUEST_REPLY = 101;
	
	private EditText userInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		userInput = (EditText) findViewById(R.id.edit);

    }

    // runOnUiThread cannot operate from a static context
    private class SomeTask extends AsyncTask<String, Void, Void> {

        private String storedString;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // Note the capitalized Void
        @Override
        protected Void doInBackground(String... params) {
            storedString = params[0];   // Remember, params is an array (and we usually operator on the first entry)
            return null;
        }

        // Note the UNcapitalized void
        @Override
        protected void onPostExecute(Void aVoid) {

            runOnUiThread(new Runnable() {
                public void run() {
                    // Toast.makeText(getApplicationContext(), storedString, Toast.LENGTH_SHORT).show();   // remember to show()
					Intent intent = new Intent(getApplicationContext(), ReceiverActivity.class);
					intent.putExtra(MAIN_KEY, userInput.getText().toString()); // getText() returns data type "Editable" , not a raw string.
					startActivityForResult(intent, REQUEST_REPLY);
				}
            });

        }
    }

    public void onYoloClick(View view) {
        final SomeTask task = new SomeTask(); // MUST be final, as it's ran from an (anon) inner class
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                task.execute("Some sample text");
            }
        }, 1500);
        Toast.makeText(getApplicationContext(), "Sent asynctask", Toast.LENGTH_SHORT).show();
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		
		switch (requestCode) {
			case REQUEST_REPLY:
				Toast.makeText(this, data.getStringExtra(MAIN_KEY), Toast.LENGTH_LONG).show();
				break;
			default:
				Log.d("MainActivity", "Unkown key result code detected");
				break;
		}
		
	}
}
