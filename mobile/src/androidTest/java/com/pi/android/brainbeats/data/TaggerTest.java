package com.pi.android.brainbeats.data;

import android.test.AndroidTestCase;

import org.junit.Test;

import static org.junit.Assert.*;

public class TaggerTest extends AndroidTestCase{

    private Tagger tagger;

    public void setUp() {
        tagger = new Tagger(mContext);
        tagger.deleteAll();
    }

    public void testAddTag() throws Exception {
        String tag = "exited";
        final long tagID = tagger.addTag(tag);
        assertEquals(tagger.getTagByTagID(tagID), tag);
    }

    public void testAddTagForSong() throws Exception {
        int id = 53;
        String tag = "calme";
        final long tagId = tagger.addTagToSong(id, tag);
        assertEquals(tagger.getTagByTagID(tagId), tag);
        String searchedTag = tagger.getTagBySongID(id);
        assertEquals(tag, searchedTag);
    }

    public void testGetUnknownTag() {
        assertNull(tagger.getTagBySongID(1000));
    }
}