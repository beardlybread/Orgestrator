package com.github.beardlybread.orgestrator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.github.beardlybread.orgestrator.org.OrgNode;
import com.github.beardlybread.orgestrator.org.OrgToDo;
import com.github.beardlybread.orgestrator.org.Orgestrator;
import com.github.beardlybread.orgestrator.ui.ToDoAdapter;

import java.util.ArrayList;

public class CheckedListActivity extends AppCompatActivity {

    private RecyclerView view = null;
    private ToDoAdapter adapter = null;
    private RecyclerView.LayoutManager layoutManager = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checked_list);

        this.view = (RecyclerView) findViewById(R.id.the_recycler_view);
        if (this.view != null) {
            this.view.setHasFixedSize(true);
        } else {
            throw new NullPointerException("the_recycler_view");
        }
        this.layoutManager = new LinearLayoutManager(this);
        this.view.setLayoutManager(this.layoutManager);

        Orgestrator org = Orgestrator.getInstance();
        ArrayList<OrgNode> todos = org.search(OrgToDo.incomplete);
        todos.addAll(org.search(OrgToDo.complete));
        this.adapter = new ToDoAdapter(todos);
        this.view.setAdapter(this.adapter);
    }

    public void toggleToDo (View v) {
        OrgToDo todo = this.adapter.get(v.getId());
        Log.d("toggleToDo", "text     : " + todo.toString());
        Log.d("toggleToDo", "checkbox : " + ((CheckBox) v).isChecked());
        Log.d("toggleToDo", "before   : " + todo.getStatus());
        todo.toggle();
        Log.d("toggleToDo", "after    : " + todo.getStatus());
        Log.d("toggleToDo", "timestamp: " + todo.getEvent().toString());
    }
}
