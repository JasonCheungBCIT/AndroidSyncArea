package movies.comp3717.bcit.ca.movies;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * Created by Jason on 2016-02-15.
 */
public class DisplayColor extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        LinearLayout bg = (LinearLayout)findViewById(R.id.display_bg);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("color");

            switch(value) {
                case "red":
                    bg.setBackgroundColor(Color.RED);
                    break;
                case "green":
                    bg.setBackgroundColor(Color.GREEN);
                    break;
                case "blue":
                    bg.setBackgroundColor(Color.BLUE);
                    break;
                default:
                    Log.d("DisplayColorActivity", "Unknown color");
            }

        }
    }

}
