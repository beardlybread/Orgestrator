package com.github.beardlybread.orgestrator;

import android.app.ListActivity;
import android.support.v7.app.ActionBar.*;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

public class FindToDoActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProgressBar pb = new ProgressBar(this);
        pb.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        pb.setIndeterminate(true);
        getListView().setEmptyView(pb);

        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.addView(pb);

        String[] whatever;

        setContentView(R.layout.activity_find_to_do);
    }
}
