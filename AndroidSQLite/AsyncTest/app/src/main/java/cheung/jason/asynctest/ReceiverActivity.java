package cheung.jason.asynctest;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * Created by Jason Cheung on 2016-02-22.
 */
public class ReceiverActivity extends Activity {

	private EditText userReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receiver);

		userReply = (EditText) findViewById(R.id.user_reply);
		
		Bundle extras = getIntent().getExtras();
		Toast.makeText(this, extras.getString(MainActivity.MAIN_KEY), Toast.LENGTH_LONG).show();
    }
	
	public void onDoneClick(View view) {
        Intent intent = new Intent();   // blank intent to load our data with
        intent.putExtra(MainActivity.MAIN_KEY, userReply.getText().toString());
		setResult(Activity.RESULT_OK, intent);  // code, intent (data)
		finish();   // blank, nothing here
	}

	class ReturnTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			// doo work on params[0];
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			// do more stuff after progress is finished
		}
	}
}
