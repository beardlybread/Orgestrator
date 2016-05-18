package com.github.beardlybread.orgestrator.ui;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.github.beardlybread.orgestrator.BuildConfig;
import com.github.beardlybread.orgestrator.org.OrgNode;
import com.github.beardlybread.orgestrator.org.OrgToDo;

import java.util.ArrayList;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private ArrayList<OrgToDo> items = null;

    public ToDoAdapter (ArrayList<OrgNode> todos) {
        this.items = new ArrayList<>();
        for (OrgNode todo: todos) {
            if (BuildConfig.DEBUG && !todo.isType("OrgToDo"))
                throw new ClassCastException("unexpected class: " + todo.type);
            this.items.add((OrgToDo) todo);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CheckedTextView view;
        public ViewHolder (CheckedTextView v) {
            super(v);
            this.view = v;
        }
    }
}
