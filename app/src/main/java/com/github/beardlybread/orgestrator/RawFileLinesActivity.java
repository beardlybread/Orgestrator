package com.github.beardlybread.orgestrator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
        OrgFile file = this.org.getFile(0);
        Log.i("RAW", "resource path: " + this.org.getResourcePath(0));
        Log.i("RAW", "resource type: " + this.org.getResourceType(0));
        StringWriter w = new StringWriter();
        try {
            file.write(w);
            Log.i("RAW", w.toString());
            w.close();
        } catch (IOException e) {
            Log.e("OrgFile.write()", "file failed to write or Writer failed to close");
        }
        setContentView(R.layout.activity_raw_file_lines);
    }

    private void loadTestFile () {
        this.org = Orgestrator.getInstance();
        if (this.org.isEmpty()) {
            InputStream inStream = getResources().openRawResource(R.raw.org_test_file);
            if (!this.org.add(inStream, "org_test_file", Orgestrator.RAW_RESOURCE)) {
                Log.e("Orgestrator.add()", this.org.getError().getMessage());
                this.org.clearError();
            }
        }
    }
}
