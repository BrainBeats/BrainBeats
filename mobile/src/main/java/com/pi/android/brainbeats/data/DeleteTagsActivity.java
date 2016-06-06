package com.pi.android.brainbeats.data;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class DeleteTagsActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Tagger(this).deleteAll();
        Toast.makeText(this, "Deleted all tags!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
