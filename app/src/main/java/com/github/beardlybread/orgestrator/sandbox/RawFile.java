package com.github.beardlybread.orgestrator.sandbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.beardlybread.orgestrator.R;
import com.github.beardlybread.orgestrator.org.OrgFile;
import com.github.beardlybread.orgestrator.org.Orgestrator;

import java.io.IOException;
import java.io.StringWriter;

public class RawFile extends AppCompatActivity {

    private Orgestrator org = null;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Orgestrator org = Orgestrator.getInstance();
        OrgFile file = org.getFile(0);
        Log.i("getResourcePath()", file.getResourcePath());
        Log.i("getResourceType()", "" + file.getResourceType());
        StringWriter w = new StringWriter();
        try {
            file.write(w);
            Log.i("RAW", w.toString());
            w.close();
        } catch (IOException e) {
            Log.e("OrgFile.write()", "file failed to write or Writer failed to close");
        }
    }

}