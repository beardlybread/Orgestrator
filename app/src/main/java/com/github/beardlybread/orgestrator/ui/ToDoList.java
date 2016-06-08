package com.github.beardlybread.orgestrator.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.beardlybread.orgestrator.R;
import com.github.beardlybread.orgestrator.org.OrgToDo;
import com.github.beardlybread.orgestrator.org.Orgestrator;

public class ToDoList extends Fragment {

    private ToDoAdapter todo = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_to_do_list, container, false);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.the_recycler_view);
        if (rv == null)
            throw new NullPointerException("ToDoList:OnCreateView:RecyclerView");
        rv.setHasFixedSize(true);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(lm);

        return v;
    }

    @Override
    public void onResume () {
        super.onResume();
        this.refresh();
    }

    @Override
    public void onPause () {
        // upload any changes to Drive
        super.onPause();
    }

    public void refresh () {
        RecyclerView rv = (RecyclerView) getView().findViewById(R.id.the_recycler_view);
        if (rv == null)
            throw new NullPointerException("ToDoList:onResume:RecyclerView");
        Orgestrator org = Orgestrator.getInstance();
        this.todo = new ToDoAdapter(org.search(OrgToDo.incomplete));
        rv.setAdapter(this.todo);
    }
}
