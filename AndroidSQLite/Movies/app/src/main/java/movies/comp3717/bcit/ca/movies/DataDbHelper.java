package movies.comp3717.bcit.ca.movies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import movies.comp3717.bcit.ca.movies.DataContract.DataEntry;

/**
 * Insulates us from the SQL
 */
public class DataDbHelper extends SQLiteOpenHelper
{
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 6;

    // name of the database file
    public static final String DATABASE_NAME = "Data.db";

    /* Not on the exam:
    Types: null, integer, real (boolean), text, blob (things like an image (but don't do that)) */
    private static final String TEXT_TYPE = " TEXT"; // we can't specifically set the size (like in oracle - varchar(10))
    private static final String COMMA_SEP = ",";

    // On the midterm, we don't need this junk, just a literal string.
    /*
    CREATE TABLE Data.db {
        _id INTEGER PRIMARY KEY,
        value TEXT
    }
     */
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DataEntry.TABLE_NAME + " (" +
                    DataEntry._ID + " INTEGER PRIMARY KEY," +
                    DataEntry.COLUMN_NAME_VALUE + TEXT_TYPE +
            " )"; // Note: IF NOT EXISTS doesn't do jack

    // Delete the table if it exists, useful when upgrading
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DataEntry.TABLE_NAME;

    public DataDbHelper(final Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * When the database needs to be created this gets called.
     * @param db the database being created
     */
    public void onCreate(final SQLiteDatabase db)
    {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    /**
     * Called when the version changes
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    /**
     * If the version number decreases.
     * Usually for rollbacks, but here everything is deleted.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }
}