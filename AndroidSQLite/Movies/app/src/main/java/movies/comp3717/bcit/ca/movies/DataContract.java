package movies.comp3717.bcit.ca.movies;

import android.provider.BaseColumns;

/**
 * Convenience class that holds information about our database.
 */
public final class DataContract
{
    private DataContract()
    {
    }

    public static abstract class DataEntry implements BaseColumns
    {
        // Also inherits fields _ID, and _COUNT (both strings)
        public static final String TABLE_NAME = "data";
        public static final String COLUMN_NAME_VALUE = "value";
    }
}