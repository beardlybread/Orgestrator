package com.github.beardlybread.orgestrator.sandbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.beardlybread.orgestrator.R;
import com.github.beardlybread.orgestrator.org.OrgNode;
import com.github.beardlybread.orgestrator.org.OrgToDo;
import com.github.beardlybread.orgestrator.org.Orgestrator;

import java.util.List;

public class FindToDo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Orgestrator org = Orgestrator.getInstance();
        logThem("complete", org.search(OrgToDo.complete));
        logThem("incomplete", org.search(OrgToDo.incomplete));
        setContentView(R.layout.activity_find_to_do);
    }

    private void logThem (String tag, List<OrgNode> them) {
        for (OrgNode n: them) {
            Log.i(tag, ((OrgToDo) n).getText().toString());
        }
    }
}
