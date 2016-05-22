package com.github.beardlybread.orgestrator.org;

import org.antlr.v4.runtime.tree.TerminalNode;

public class OrgEvent extends OrgNode {

    public static final int UNDEFINED = -1;
    public static final int CLOSED = 0;
    public static final int SCHEDULED = 1;
    public static final int DEADLINE = 2;

    protected int status = UNDEFINED;
    protected int eventType = UNDEFINED;
    protected OrgDate current = null;
    protected OrgDate previous = null;

    public OrgEvent (TerminalNode indent, int status, OrgDate currentTimestamp) {
        super(indent);
        this.eventType = this.status = status;
        this.current = currentTimestamp;
    }

    public OrgEvent (TerminalNode indent, int type, OrgDate current, OrgDate previous) {
        super(indent);
        this.eventType = type;
        this.status = OrgEvent.CLOSED;
        this.current = current;
        this.previous = previous;
    }

    @Override
    public String toOrgString () {
        StringBuilder sb = new StringBuilder();
        this.doIndent(sb);
        switch (this.status) {
            case OrgEvent.CLOSED:
                sb.append("CLOSED: [").append(this.current.toOrgString()).append("]");
                if (this.eventType == OrgEvent.SCHEDULED) {
                    sb.append(" SCHEDULED: <").append(this.previous.toOrgString()).append(">");
                } else if (this.eventType == OrgEvent.DEADLINE) {
                    sb.append(" DEADLINE: <").append(this.previous.toOrgString()).append(">");
                }
                sb.append("\n");
                break;
            case OrgEvent.SCHEDULED:
                sb.append("SCHEDULED: <").append(this.current.toOrgString()).append(">\n");
                break;
            case OrgEvent.DEADLINE:
                sb.append("DEADLINE: <").append(this.current.toOrgString()).append(">\n");
                break;
        }
        return sb.toString();
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();
        switch (this.status) {
            case OrgEvent.CLOSED:
                sb.append("Closed: ");
                break;
            case OrgEvent.DEADLINE:
                sb.append("Deadline: ");
                break;
            case OrgEvent.SCHEDULED:
                sb.append("Scheduled: ");
                break;
        }
        return sb.append(this.current.toString()).toString();
    }

}
