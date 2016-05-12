package com.github.beardlybread.orgestrator;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.github.beardlybread.orgestrator.org.OrgFile;
import com.github.beardlybread.orgestrator.org.Orgestrator;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class RawFileLinesActivity extends AppCompatActivity {

    private Orgestrator org = null;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loadTestFile();
        OrgFile file = this.org.getData(0).getFile();
        StringWriter w = new StringWriter();
        try {
            file.write(w);
            Log.i("RAW", w.toString());
            w.close();
        } catch (IOException e) {
            Log.e("IO", "file failed to write");
        }
        setContentView(R.layout.activity_raw_file_lines);
    }

    private void loadTestFile () {
        InputStream inStream = getResources().openRawResource(R.raw.org_test_file);
        this.org = new Orgestrator();
        this.org.add(inStream, "org_test_file", Orgestrator.RAW_RESOURCE);
    }
}
