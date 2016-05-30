package com.pi.android.brainbeats.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class Tagger {
    private ContentResolver contentResolver;
    private Uri tagUri;
    private Uri songTagUri;

    public Tagger(Context context) {
        contentResolver = context.getContentResolver();
        tagUri = TagContract.TagEntry.CONTENT_URI;
        songTagUri = TagContract.SongTagEntry.CONTENT_URI;
    }

    public long addTag(String tag) {
        ContentValues tagValues = new ContentValues();
        tagValues.put(TagContract.TagEntry.COLUMN_NAME, tag);
        final Uri insertedTag = contentResolver.insert(tagUri, tagValues);
        return ContentUris.parseId(insertedTag);
    }

    public String getTagByTagID(long tagID) {
        final Cursor tagQuery = contentResolver.query(
                tagUri,
                null,
                TagContract.TagEntry._ID + " = " + tagID,
                null,
                null
        );
        if (tagQuery.moveToNext()) {
            final String tag = tagQuery.getString(tagQuery.getColumnIndex(TagContract.TagEntry.COLUMN_NAME));
            tagQuery.close();
            return tag;
        }
        tagQuery.close();
        return null;
    }

    public long addTagToSong(int songID, String tag) {
        final Cursor query = contentResolver.query(tagUri, null, "name = ?", new String[]{tag}, null);
        long tagID = 0;

        if (query.getCount() < 1) {
            tagID = addTag(tag);
        } else {
            query.moveToFirst();
            String tagIDstr = query.getString(query.getColumnIndex(TagContract.TagEntry._ID));
            tagID = Long.parseLong(tagIDstr);
        }
        query.close();

        ContentValues songTagValues = new ContentValues();
        songTagValues.put(TagContract.SongTagEntry.COLUMN_SONG, songID);
        songTagValues.put(TagContract.SongTagEntry.COLUMN_TAG, tagID);
        contentResolver.insert(songTagUri, songTagValues);
        return tagID;
    }

    public String getTagBySongID(int songID) {
        final Cursor songTagQuery = contentResolver.query(
                songTagUri, null,
                TagContract.SongTagEntry.TABLE_NAME +"."+
                TagContract.SongTagEntry.COLUMN_SONG +
                " = " + songID,
                null, null
        );
        int count = songTagQuery.getCount();
        if (!songTagQuery.moveToNext()) {
            songTagQuery.close();
            return null;
        }
        final String tagID = songTagQuery.getString(
                songTagQuery.getColumnIndex(TagContract.SongTagEntry.COLUMN_TAG)
        );
        songTagQuery.close();
        final Cursor tagQuery = contentResolver.query(tagUri, null, "_ID = ?", new String[]{tagID}, null);
        if (!tagQuery.moveToNext()) {
            return null;
        }
        return tagQuery.getString(tagQuery.getColumnIndex(TagContract.TagEntry.COLUMN_NAME));
    }

    public void deleteAll() {
        contentResolver.delete(tagUri, null, null);
        contentResolver.delete(songTagUri, null, null);
    }
}
