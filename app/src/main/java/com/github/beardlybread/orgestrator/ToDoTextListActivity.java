package com.github.beardlybread.orgestrator;

import android.app.ListActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.os.Bundle;

import com.github.beardlybread.orgestrator.org.OrgNode;
import com.github.beardlybread.orgestrator.org.OrgToDo;
import com.github.beardlybread.orgestrator.org.Orgestrator;

public class ToDoTextListActivity extends ListActivity {

    // This is the Adapter being used to display the list's data
    private ArrayAdapter<OrgNode> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Orgestrator org = Orgestrator.getInstance();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        adapter.addAll(org.search(OrgToDo.incomplete));
        adapter.addAll(org.search(OrgToDo.complete));
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
    }
}
