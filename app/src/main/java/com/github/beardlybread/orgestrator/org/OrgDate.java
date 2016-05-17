package com.github.beardlybread.orgestrator.org;

import com.github.beardlybread.orgestrator.org.antlr.OrgParser;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrgDate extends GregorianCalendar {

    public class REPEAT {
        public static final int NONE = -1;

        public static final int NEXT = 0;
        public static final int NOW = 1;
        public static final int NEXT_UNTIL_NOW = 2;

        public static final int HOUR = 0;
        public static final int DAY = 1;
        public static final int WEEK = 2;
        public static final int MONTH = 3;
        public static final int YEAR = 4;
    }

    public static final Pattern MATCH_DATE = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");
    public static final Pattern MATCH_TIME = Pattern.compile("(\\d{2}):(\\d{2})");
    public static final Pattern MATCH_REPEAT = Pattern.compile("([.+]?)\\+(\\d+)([hdwmy])");

    protected int repeatTime = -1;
    protected int repeatUnit = REPEAT.NONE;
    protected int repeatType = REPEAT.NONE;
    protected boolean hasTime = false;

    public OrgDate() { super(); }

    public OrgDate(OrgParser.DateContext ctx) {
        super();
        this.setWithContext(ctx);
    }

    public void setRepeat (int time, int unit, int type) {
        this.repeatTime = time;
        this.repeatUnit = unit;
        this.repeatType = type;
    }

    public void setWithContext (OrgParser.DateContext ctx) {
        Matcher d = MATCH_DATE.matcher(ctx.DATE().getText());
        int year = 0, month = 0, day = 0, hour = 0, minute = 0;
        if (d.matches()) {
            year = Integer.parseInt(d.group(1));
            month = Integer.parseInt(d.group(2));
            day = Integer.parseInt(d.group(3));
        }
        // ignore DOW
        if (ctx.TIME() != null) {
            Matcher t = MATCH_TIME.matcher(ctx.TIME().getText());
            if (t.matches()) {
                this.hasTime = true;
                hour = Integer.parseInt(t.group(1));
                minute = Integer.parseInt(t.group(2));
            }
        }
        this.set(year, month, day, hour, minute);
        if (ctx.REPEAT() != null) {
            Matcher r = MATCH_REPEAT.matcher(ctx.REPEAT().getText());
            if (r.matches()) {
                switch (r.group(1)) {
                    case "":
                        this.repeatType = REPEAT.NEXT;
                        break;
                    case ".":
                        this.repeatType = REPEAT.NOW;
                        break;
                    case "+":
                        this.repeatType = REPEAT.NEXT_UNTIL_NOW;
                        break;
                    default:
                        this.repeatType = REPEAT.NONE;
                }
                this.repeatTime = Integer.parseInt(r.group(2));
                switch (r.group(3)) {
                    case "h":
                        this.repeatUnit = REPEAT.HOUR;
                        break;
                    case "d":
                        this.repeatUnit = REPEAT.DAY;
                        break;
                    case "w":
                        this.repeatUnit = REPEAT.WEEK;
                        break;
                    case "m":
                        this.repeatUnit = REPEAT.MONTH;
                        break;
                    case "y":
                        this.repeatUnit = REPEAT.YEAR;
                        break;
                    default:
                        this.repeatUnit = REPEAT.NONE;
                }
            }
        }

    }

    public String toOrgString () {
        StringBuilder sb = new StringBuilder();
        this.dateString(sb);
        sb.append(" ");
        this.dowString(sb);
        if (this.hasTime) {
            sb.append(" ");
            this.timeString(sb);
        }
        if (this.repeatType != REPEAT.NONE) {
            sb.append(" ");
            this.repeatString(sb);
        }
        return sb.toString();
    }

    private void dateString (StringBuilder sb) {
        sb.append(this.get(Calendar.YEAR)).append("-");
        int month = this.get(Calendar.MONTH) + 1;
        sb.append(month < 10 ? "0" : "").append(month).append("-");
        int day = this.get(Calendar.DAY_OF_MONTH);
        sb.append(day < 10 ? "0" : "").append(day);
    }

    private void dowString (StringBuilder sb) {
        switch (this.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                sb.append("Sun");
                break;
            case Calendar.MONDAY:
                sb.append("Mon");
                break;
            case Calendar.TUESDAY:
                sb.append("Tue");
                break;
            case Calendar.WEDNESDAY:
                sb.append("Wed");
                break;
            case Calendar.THURSDAY:
                sb.append("Thu");
                break;
            case Calendar.FRIDAY:
                sb.append("Fri");
                break;
            case Calendar.SATURDAY:
                sb.append("Sat");
                break;
            default:
                break;
        }
    }

    private void timeString (StringBuilder sb) {
        int hour = this.get(Calendar.HOUR_OF_DAY);
        sb.append(hour < 10 ? "0" : "").append(hour).append(":");
        int minute = this.get(Calendar.MINUTE);
        sb.append(minute < 10 ? "0" : "").append(minute);
    }

    private void repeatString (StringBuilder sb) {
        switch (this.repeatType) {
            case REPEAT.NOW:
                sb.append(".");
                break;
            case REPEAT.NEXT_UNTIL_NOW:
                sb.append("+");
                break;
            default:
                break;
        }
        sb.append("+").append(this.repeatTime);
        switch (this.repeatUnit) {
            case REPEAT.HOUR:
                sb.append("h");
                break;
            case REPEAT.DAY:
                sb.append("d");
                break;
            case REPEAT.WEEK:
                sb.append("w");
                break;
            case REPEAT.MONTH:
                sb.append("m");
                break;
            case REPEAT.YEAR:
                sb.append("y");
                break;
            default:
                break;
        }
    }
}
