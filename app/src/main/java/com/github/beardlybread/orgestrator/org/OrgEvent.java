package com.github.beardlybread.orgestrator.org;

import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class OrgEvent extends OrgNode {

    public static final int UNDEFINED = -1;
    public static final int CLOSED = 0;
    public static final int SCHEDULED = 1;
    public static final int DEADLINE = 2;

    public static final OrgEvent NO_EVENT = new OrgEvent(null);

    protected int status = UNDEFINED;
    protected int eventType = UNDEFINED;
    protected OrgDate current = null;
    protected OrgDate previous = null;

    public OrgEvent (OrgToDo parent) {
        // The null parent is here just for NO_EVENT instantiation.
        super((parent == null) ? 0 : parent.level + 1);
        this.eventType = this.status = OrgEvent.UNDEFINED;
    }

    public OrgEvent (TerminalNode indent, int status, OrgDate current) {
        super(indent);
        // If the status is closed but no previous type was defined, its type is undefined.
        // Otherwise, the type is just the status.
        this.eventType = this.status = status;
        this.current = current;
    }

    public OrgEvent (TerminalNode indent, int type, OrgDate current, OrgDate previous) {
        super(indent);
        this.eventType = type;
        this.status = OrgEvent.CLOSED;
        this.current = current;
        this.previous = previous;
    }

    public OrgDate getCurrent () { return this.current; }

    public int getEventType () { return this.eventType; }

    public OrgDate getPrevious () { return this.previous; }

    public int getStatus () { return this.status; }

    public void setClosed () {
        this.previous = this.current;
        if (this.current == null || this.current.getRepeatType() == OrgDate.REPEAT.NONE) {
            this.status = OrgEvent.CLOSED;
            this.current = new OrgDate();
        } else {
            this.status = this.eventType;
            this.current = this.current.next();
        }
    }

    public void undoClosed () {
        this.status = (this.eventType == OrgEvent.CLOSED ? OrgEvent.UNDEFINED : this.eventType);
        this.current = this.previous;
    }

    @Override
    public String toOrgString () {
        StringBuilder sb = new StringBuilder();
        switch (this.status) {
            case OrgEvent.CLOSED:
                this.doIndent(sb);
                sb.append("CLOSED: [").append(this.current.toOrgString()).append("]");
                switch (this.eventType) {
                    case OrgEvent.SCHEDULED:
                        sb.append(" SCHEDULED: <").append(this.previous.toOrgString()).append(">");
                        break;
                    case OrgEvent.DEADLINE:
                        sb.append(" DEADLINE: <").append(this.previous.toOrgString()).append(">");
                        break;
                    default:
                        break;
                }
                sb.append("\n");
                break;
            case OrgEvent.SCHEDULED:
                this.doIndent(sb);
                sb.append("SCHEDULED: <").append(this.current.toOrgString()).append(">\n");
                break;
            case OrgEvent.DEADLINE:
                this.doIndent(sb);
                sb.append("DEADLINE: <").append(this.current.toOrgString()).append(">\n");
                break;
            default:
                break; // If the event didn't exist and got undone, it will fall to this.
        }
        return sb.toString();
    }

    @Override
    public String toString () {
        switch (this.status) {
            case OrgEvent.CLOSED:
                return this.current.toString();
            case OrgEvent.DEADLINE:
            case OrgEvent.SCHEDULED:
                OrgDate cur = this.current;
                if (cur.get(Calendar.HOUR_OF_DAY) == 0
                        && cur.get(Calendar.MINUTE) == 0)
                    return this.current.toDateString();
                return this.current.toString();
            default:
                return ""; // An undone simple closed event should be the empty string.
        }
    }

    public boolean isLate () {
        return (this.status == OrgEvent.SCHEDULED || this.status == OrgEvent.DEADLINE)
                && this.current.compareTo(new GregorianCalendar()) < 0;
    }

    public boolean isToday () {
        return (this.status == OrgEvent.SCHEDULED || this.status == OrgEvent.DEADLINE)
                && this.current.isToday();
    }

}
