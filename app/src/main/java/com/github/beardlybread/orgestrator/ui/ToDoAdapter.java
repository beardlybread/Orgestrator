package com.github.beardlybread.orgestrator.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.github.beardlybread.orgestrator.BuildConfig;
import com.github.beardlybread.orgestrator.R;
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
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        CheckBox cb = (CheckBox) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.to_do_item, parent, false);
        return new ViewHolder(cb);
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, int position) {
        holder.view.setId(position);
        holder.view.setText(this.items.get(position).toString());
        holder.view.setChecked(this.items.get(position).getStatus());
    }

    @Override
    public int getItemCount () {
        return this.items.size();
    }

    public OrgToDo get (int index) {
        return this.items.get(index);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox view;
        public ViewHolder (CheckBox v) {
            super(v);
            this.view = v;
        }
    }
}
