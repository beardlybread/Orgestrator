/*
The MIT License (MIT)

Copyright (c) 2016 Bradley Steinbacher

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.github.beardlybread.orgestrator.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.beardlybread.orgestrator.BuildConfig;
import com.github.beardlybread.orgestrator.R;
import com.github.beardlybread.orgestrator.org.OrgNode;
import com.github.beardlybread.orgestrator.org.OrgToDo;

import java.util.ArrayList;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder>
        implements View.OnClickListener {

    private ArrayList<OrgToDo> items = null;

    public ToDoAdapter (List<OrgNode> todos) {
        this.items = new ArrayList<>();
        for (OrgNode todo: todos) {
            if (BuildConfig.DEBUG && !todo.isType("OrgToDo"))
                throw new ClassCastException("unexpected class: " + todo.getClass().getSimpleName());
            this.items.add((OrgToDo) todo);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        ToDoView v = (ToDoView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.to_do_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, int position) {
        ToDoView v = holder.view;
        v.setOnClickListener(this);
        v.setToDo(this.get(position));
        v.invalidate();
    }

    @Override
    public int getItemCount () {
        return this.items.size();
    }

    public OrgToDo get (int index) {
        return this.items.get(index);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ToDoView view;
        public ViewHolder (ToDoView v) {
            super(v);
            this.view = v;
        }
    }

    @Override
    public void onClick (View v) {
        ((ToDoView) v).toggle();
    }

}
