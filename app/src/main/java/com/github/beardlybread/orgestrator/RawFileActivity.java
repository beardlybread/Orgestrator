package com.github.beardlybread.orgestrator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.beardlybread.orgestrator.org.OrgFile;
import com.github.beardlybread.orgestrator.org.Orgestrator;

import java.io.IOException;
import java.io.StringWriter;

public class RawFileActivity extends AppCompatActivity {

    private Orgestrator org = null;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Orgestrator org = Orgestrator.getInstance();
        OrgFile file = org.getFile(0);
        Log.i("getResourcePath()", org.getResourcePath(0));
        Log.i("getResourceType()", "" + org.getResourceType(0));
        StringWriter w = new StringWriter();
        try {
            file.write(w);
            Log.i("RAW", w.toString());
            w.close();
        } catch (IOException e) {
            Log.e("OrgFile.write()", "file failed to write or Writer failed to close");
        }
        setContentView(R.layout.activity_raw_file);
    }

}
