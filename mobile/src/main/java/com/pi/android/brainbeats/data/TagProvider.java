package com.pi.android.brainbeats.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

public class TagProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private TagDbHelper mOpenHelper;

    static final int TAG = 100;
    static final int SONGTAG = 200;

    @Override
    public boolean onCreate() {
        mOpenHelper = new TagDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case TAG:
                SQLiteDatabase db = mOpenHelper.getReadableDatabase();
                retCursor = db.query(TagContract.TagEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                db.close();
                break;
            case SONGTAG:
                SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
                StringBuilder sb = new StringBuilder();
                sb.append(TagContract.TagEntry.TABLE_NAME);
                sb.append(" INNER JOIN ");
                sb.append(TagContract.SongTagEntry.TABLE_NAME);
                sb.append(" ON (");
                sb.append(TagContract.TagEntry.TABLE_NAME +"."+TagContract.TagEntry._ID);
                sb.append(" = ");
                sb.append(TagContract.SongTagEntry.TABLE_NAME + "." + TagContract.SongTagEntry._ID);
                sb.append(")");
                queryBuilder.setTables(sb.toString());
                retCursor = queryBuilder.query(mOpenHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case TAG:
                return TagContract.TagEntry.CONTENT_TYPE;
            case SONGTAG:
                return TagContract.SongTagEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case TAG: {
                long _id = db.insert(TagContract.TagEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = TagContract.TagEntry.buildTagUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case SONGTAG: {
                long _id = db.insert(TagContract.SongTagEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = TagContract.SongTagEntry.buildSongTagUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }


    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(TagContract.CONTENT_AUTHORITY, TagContract.PATH_TAG, TAG);
        matcher.addURI(TagContract.CONTENT_AUTHORITY, TagContract.PATH_SONGTAG, SONGTAG);
        return matcher;
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

}
