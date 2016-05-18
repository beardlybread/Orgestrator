package com.github.beardlybread.orgestrator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.beardlybread.orgestrator.org.OrgToDo;
import com.github.beardlybread.orgestrator.org.Orgestrator;
import com.github.beardlybread.orgestrator.ui.ToDoAdapter;

public class CheckedListActivity extends AppCompatActivity {

    private RecyclerView view;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.view = (RecyclerView) findViewById(R.id.the_recycler_view);
        this.view.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(this);
        this.view.setLayoutManager(this.layoutManager);

        Orgestrator org = Orgestrator.getInstance();
        this.adapter = new ToDoAdapter(org.search(OrgToDo.all));
        this.view.setAdapter(this.adapter);
    }

}
