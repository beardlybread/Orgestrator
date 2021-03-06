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

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.beardlybread.orgestrator.R;
import com.github.beardlybread.orgestrator.org.OrgEvent;
import com.github.beardlybread.orgestrator.org.OrgToDo;

public class ToDoView extends LinearLayout {

    private OrgToDo todo = null;

    private CheckBox checkBox = null;
    private TextView date = null;
    private TextView message = null;
    private TextView path = null;
    private ToDoAdapter parent = null;

    public ToDoView (Context context) {
        super(context);
    }

    public ToDoView (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ToDoView (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CheckBox getCheckBox () {
        if (this.checkBox == null)
            this.checkBox = (CheckBox) this.findViewById(R.id.to_do_checkbox);
        return this.checkBox;
    }

    public TextView getDate () {
        if (this.date == null)
            this.date = (TextView) this.findViewById(R.id.to_do_date);
        return this.date;
    }

    public TextView getMessage () {
        if (this.message == null)
            this.message = (TextView) this.findViewById(R.id.to_do_message);
        return this.message;
    }

    public TextView getPath () {
        if (this.path == null)
            this.path = (TextView) this.findViewById(R.id.to_do_path);
        return this.path;
    }

    public void setParent (ToDoAdapter parent) { this.parent = parent; }
    public void setToDo (OrgToDo todo) { this.todo = todo; }

    public void toggle () {
        this.getCheckBox().toggle();
        this.todo.toggle();
        this.invalidate();
    }

    public void initialize () {
        this.getMessage().setText(this.todo.toString());
        this.getCheckBox().setChecked(this.todo.getStatus());
        this.getDate().setText(this.todo.getEvent().toString());
        this.getPath().setText(this.todo.getFullPath());
        this.updateColors();
        super.invalidate();
    }

    @Override
    public void invalidate () {
        this.getMessage().setText(this.todo.toString());
        this.getCheckBox().setChecked(this.todo.getStatus());
        this.getDate().setText(this.todo.getEvent().toString());
        this.getPath().setText(this.todo.getFullPath());
        this.updateColors();
        this.parent.notifyDataSetChanged();
        super.invalidate();
    }

    public void updateColors () {
        int background = (this.todo.getStatus()) // DONE if true
                ? R.color.todo_closed : R.color.todo_open;
        switch (this.todo.getEvent().getStatus()) {
            case OrgEvent.CLOSED:
                break;
            case OrgEvent.DEADLINE:
            case OrgEvent.SCHEDULED:
                background = R.color.todo_early;
                if (this.todo.getEvent().isToday()) {
                    background = R.color.todo_today;
                } else if (this.todo.getEvent().isLate()) {
                    background = R.color.todo_late;
                }
        }
        this.getMessage().setBackgroundResource(background);
        this.getDate().setBackgroundResource(background);
        this.getPath().setBackgroundResource(background);
    }

}
