/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.uamp.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.media.MediaMetadataCompat;

import com.example.android.uamp.utils.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Utility class to get a list of MusicTrack's based on a server-side JSON
 * configuration.
 */
public class RemoteJSONSource implements MusicProviderSource {

    private static final String TAG = LogHelper.makeLogTag(RemoteJSONSource.class);

    private Context applicationContext;

    public RemoteJSONSource(Context context) {
        applicationContext = context;
    }

    public void setContext(Context context) {
        applicationContext = context;
    }

    @Override
    public Iterator<MediaMetadataCompat> iterator() {
        try {
            ContentResolver cr = applicationContext.getContentResolver();
            ArrayList<MediaMetadataCompat> tracks = new ArrayList<>();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
            String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
            Cursor cur = cr.query(uri, null, selection, null, sortOrder);
            int count = 0;
            List<String> musicList = new ArrayList<>();
            if(cur != null) {
                count = cur.getCount();
                if(count > 0) {
                    while(cur.moveToNext()) {
                        tracks.add(buildFromStorage(cur, cr));
                    }

                }
            }
            cur.close();
            return tracks.iterator();
        } catch (JSONException e) {
            LogHelper.e(TAG, e, "Could not retrieve music list");
            throw new RuntimeException("Could not retrieve music list", e);
        }
    }

    private MediaMetadataCompat buildFromStorage(Cursor cur, ContentResolver cr) throws JSONException {
        String source = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA));
        String title = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
        String album = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ALBUM));
        String artist = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        String id = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media._ID));

        String iconUrl = getAlbumArt(cr, cur.getInt(cur.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));

        String genre = "unknown";
        int duration = cur.getInt(cur.getColumnIndex(MediaStore.Audio.Media.DURATION)) * 1000; //ms
        return new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id)
                .putString(MusicProviderSource.CUSTOM_METADATA_TRACK_SOURCE, source) //?
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, genre)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, iconUrl)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                .build();
    }

    /*
    Cette fonction ne trouve pas encore le photo d'Album
     */
    private String  getAlbumArt(ContentResolver cr, int albumId) {
        final Uri sArtworkUri = Uri
                .parse("content://media/external/audio/albumart");

        Uri uri = ContentUris.withAppendedId(sArtworkUri, albumId);
        final String path = uri.toString();
        return path;
    }
}
