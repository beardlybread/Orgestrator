package com.github.beardlybread.orgestrator.org;

public class OrgEvent extends OrgNode {

    public static final int UNDEFINED = -1;
    public static final int CLOSED = 0;
    public static final int SCHEDULED = 1;
    public static final int DEADLINE = 2;

    protected int status = UNDEFINED;
    protected int eventType = UNDEFINED;
    protected OrgDate current = null;
    protected OrgDate previous = null;

    public OrgEvent (int status, OrgDate currentTimestamp) {
        this.eventType = this.status = status;
        this.current = currentTimestamp;
    }

    public OrgEvent (int type, OrgDate current, OrgDate previous) {
        this.eventType = type;
        this.status = OrgEvent.CLOSED;
        this.current = current;
        this.previous = previous;
    }
}
