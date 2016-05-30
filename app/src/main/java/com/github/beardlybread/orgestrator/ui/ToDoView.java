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

    public void setToDo (OrgToDo todo) { this.todo = todo; }

    public void toggle () {
        this.getCheckBox().toggle();
        this.todo.toggle();
        this.invalidate();
    }

    @Override
    public void invalidate () {
        this.getMessage().setText(this.todo.toString());
        this.getCheckBox().setChecked(this.todo.getStatus());
        this.getDate().setText(this.todo.getEvent().toString());
        this.getPath().setText(this.todo.getFullPath());
        this.updateColors();
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
