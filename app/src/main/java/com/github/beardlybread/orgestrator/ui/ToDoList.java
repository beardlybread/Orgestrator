package com.github.beardlybread.orgestrator.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.beardlybread.orgestrator.R;
import com.github.beardlybread.orgestrator.org.OrgToDo;
import com.github.beardlybread.orgestrator.org.Orgestrator;

public class ToDoList extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_to_do_list, container, false);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.the_recycler_view);
        if (rv == null)
            throw new NullPointerException("ToDoList:RecyclerView");
        rv.setHasFixedSize(true);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(lm);

        Orgestrator org = Orgestrator.getInstance();
        ToDoAdapter tda = new ToDoAdapter(org.search(OrgToDo.incomplete));
        rv.setAdapter(tda);

        return v;
    }

}
