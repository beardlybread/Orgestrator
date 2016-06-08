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
