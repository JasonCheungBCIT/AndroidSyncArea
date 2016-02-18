package movies.comp3717.bcit.ca.movies;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import movies.comp3717.bcit.ca.movies.DataContract.DataEntry;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// Most of the things in here are on the midterm.

// List activity simplifies a common case of an activity that is make up of only a list (?)
public class MainActivity extends ListActivity
{
    /**
     * Read from the database.
     * Navigates/selects a row from a table, much like a pointer.
     */
    private SimpleCursorAdapter titlesAdapater;

    /**
     * Sets up the (SQLite) database.
     */
    private DataDbHelper dataDBHelper;

    /**
     * Read from a REST api. (Using mongodb for this project)
     */
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        final Resources      res;       // Stuff in the XML - Not on the exam
        final SQLiteDatabase db;        // Database that stores the actual content.

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataDBHelper   = new DataDbHelper(this);
        res            = getResources();

        // make sure we can store the titles into the database
        db             = dataDBHelper.getWritableDatabase();
        dataDBHelper.onCreate(db);

        // gets the data from the REST api and writes it to the database
        new RetrieveDataTask(db).execute("https://api.mongolab.com/api/1/databases/comp3717/collections/test?apiKey=7NxSEeut--6JUnTVlcldQ-ZqUo9oM9-6");

        // get the data form the db ...
        titlesAdapater = new SimpleCursorAdapter(this,
                                android.R.layout.simple_list_item_1,
                                getAllTitles(db),
                                new String[] { DataEntry.COLUMN_NAME_VALUE }, // why not table?
                                new int[] { android.R.id.text1 }, // What's this
                                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        // ... and stuff it into the list
        setListAdapter(titlesAdapater);
    }

    /**
     * when the user taps an item on the list
     * @param list
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onListItemClick(final ListView list,
                                final View     view,
                                final int      position,
                                final long     id)
    {
        final String title;

        // Cursor is a pointer to a row in a database
        Cursor cursor = titlesAdapater.getCursor();

        // We're moving the pointer to record #position.
        if (cursor.moveToPosition(position)) {
            // Get the value of the column (which is our color)
            String data = cursor.getString(cursor.getColumnIndex(DataEntry.COLUMN_NAME_VALUE));
            Log.d("DEBUG", data);

            // Exam: starting a new intent, passing data, retrieving data.
            Intent intent = new Intent(getApplicationContext(), DisplayColor.class);
            intent.putExtra("color", data);
            startActivity(intent);
        }

        // title = titlesAdapater.getItem(position);
        // Toast.makeText(this, Integer.toString(position), Toast.LENGTH_SHORT).show();
        // Toast.makeText(this, )
    }

    /**
     * Queries the database for all the titles.
     * @param db
     * @return
     */
    public Cursor getAllTitles(final SQLiteDatabase db)
    {
        Cursor cursor;

        cursor = db.query(DataEntry.TABLE_NAME,
                          new String[]
                          {
                              DataEntry._ID, //whats this, just know it makes it work
                              DataEntry.COLUMN_NAME_VALUE
                          },
                          null,
                          null,
                          null,
                          null,
                          null);

        Log.d("X", Integer.toString(cursor.getCount()));

        return cursor;
    }

    // ON THE MIDTERM! THIS WHOLE CLASS , don't worry about the progress
    /**
     * Gets data from the REST api and stores it in the database.
     * AsyncTask b/c the application will freeze waiting for a result (in reality it'll just crash)
     */
    class RetrieveDataTask
        // String - list of things to act on
        // Void   - progress type, not using so can be void
        // String - return type of the data (size of data)
        extends AsyncTask<String, Void, String>
    {
        /**
         * Incase there is an error (can't throw?)
         */
        private Exception exception;

        /**
         * Database so we can write the result
         */
        private SQLiteDatabase database;

        RetrieveDataTask(final SQLiteDatabase db)
        {
            database = db;
        }

        /**
         * Time consuming thing that we don't want to block the main/UI-thread for
         *
         * @param urls  url to download (only using urls[0])
         * @return      JSON from the url
         */
        protected String doInBackground(final String... urls)
        {
            try
            {
                // Build the REST request
                Request request = new Request.Builder()
                        .url(urls[0])   // issues if these aren't colors
                        // .url("https://api.mongolab.com/api/1/databases/comp3717/collections/test?apiKey=7NxSEeut--6JUnTVlcldQ-ZqUo9oM9-6")
                        .build();

                /*
                create - post
                relate(reference) - get
                update - put
                delete - delete
                 */
                // Runs the get to obtain the JSON
                Response response = client.newCall(request).execute();

                // return the JSON response
                return response.body().string();
            }
            catch(final IOException e)
            {
                this.exception = e;
                Log.d("error", e.toString());
                return (null);
            }
        }

        // Exam: Doesn't expect us to know the JSON api, but to know what is and how to read JSON

        /**
         * Called when data is received from the server
         * @param data
         */
        protected void onPostExecute(final String data)
        {
            final JSONArray array;

            // Data is a json array
            Log.d("data", data);

            try
            {
                array = new JSONArray(data);

                // for each movie
                for(int i = 0; i < array.length(); i++)
                {
                    final long       newRowId;
                    final JSONObject object;
                    final String     title;
                    final ContentValues values = new ContentValues();

                    // grab the JSON for that movie
                    object = array.getJSONObject(i);

                    // get the title
                    title  = object.getString("name");
                    Log.d("title", title);

                    // add the title to the row data
                    values.put(DataEntry.COLUMN_NAME_VALUE, title);

                    // insert the row (record) into the database.
                    newRowId = database.insert(
                            DataEntry.TABLE_NAME,
                            null,
                            values);
                    Log.d("row id = ", Long.toString(newRowId));
                }

                // re-run the query so we have the current data
                titlesAdapater.getCursor().requery();   // Updating the query?

                // tell the table that we have new data
                titlesAdapater.notifyDataSetChanged();
            }
            catch(final JSONException ex)
            {
                this.exception = ex;
                Log.d("error", ex.toString());
            }
        }
    }
}
