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
        for (int i = 0; i < this.indent; ++i) {
            sb.append(" ");
        }
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

}
