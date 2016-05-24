package com.pi.android.brainbeats.data;

import android.provider.BaseColumns;

public class TagContract {

    public static final String CONTENT_AUTHORITY = "com.pi.android.brainbeats";

    public static final class TagEntry implements BaseColumns {
        public static final String TABLE_NAME = "tag";

        public static final String COLUMN_NAME= "name";
    }

    public static final class SongTagEntry implements BaseColumns {
        public static final String TABLE_NAME = "song_tag";

        public static final String COLUMN_SONG= "song";

        public static final String COLUMN_TAG= "tag";
    }

}
