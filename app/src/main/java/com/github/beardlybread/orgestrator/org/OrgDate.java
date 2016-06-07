package com.github.beardlybread.orgestrator.org;

import com.github.beardlybread.orgestrator.org.antlr.OrgParser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrgDate extends GregorianCalendar {

    public class REPEAT {
        public static final int NONE = -1;
        public static final int ONCE = -2;
        public static final int FROM_NOW = -3;
        public static final int UNTIL_NOW = -4;
    }

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT =
            new SimpleDateFormat("yyyy/MM/dd");
    public static final SimpleDateFormat DEFAULT_DATETIME_FORMAT =
            new SimpleDateFormat("yyyy/MM/dd HH:mm");

    public static final Pattern MATCH_DATE = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");
    public static final Pattern MATCH_TIME = Pattern.compile("(\\d{2}):(\\d{2})");
    public static final Pattern MATCH_REPEAT = Pattern.compile("([.+]?)\\+(\\d+)([hdwmy])");

    protected int repeatTime = -1;
    protected int repeatUnit = Calendar.HOUR_OF_DAY;
    protected int repeatType = REPEAT.NONE;
    protected boolean hasTime = false;

    public OrgDate() { super(); }

    public OrgDate(OrgParser.DateContext ctx) {
        super();
        this.setWithContext(ctx);
    }

    public int getRepeatType () { return this.repeatType; }

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
            month = Integer.parseInt(d.group(2)) - 1;
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
                this.repeatType = (OrgDate.REPEAT_StringToInteger.containsKey(r.group(1)))
                        ? OrgDate.REPEAT_StringToInteger.get(r.group(1)) : REPEAT.NONE;
                this.repeatTime = Integer.parseInt(r.group(2));
                this.repeatUnit = (OrgDate.REPEAT_StringToInteger.containsKey(r.group(3)))
                        ? OrgDate.REPEAT_StringToInteger.get(r.group(3)) : REPEAT.NONE;
            }
        }

    }

    public String toDateString () {
        return OrgDate.DEFAULT_DATE_FORMAT.format(this.getTime());
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

    @Override
    public String toString () {
        return OrgDate.DEFAULT_DATETIME_FORMAT.format(this.getTime());
    }

    public boolean isToday () {
        GregorianCalendar today = new GregorianCalendar();
        return today.get(Calendar.YEAR) == this.get(Calendar.YEAR)
                && today.get(Calendar.DAY_OF_YEAR) == this.get(Calendar.DAY_OF_YEAR);
    }

    public OrgDate today () {
        OrgDate now = new OrgDate();
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.MILLISECOND, 0);
        return now;
    }

    public OrgDate next () {
        OrgDate nextDate = null;
        switch (this.repeatType) {
            case REPEAT.ONCE:
                nextDate = (OrgDate) this.clone();
                nextDate.add(this.repeatUnit, this.repeatTime);
                break;
            case REPEAT.FROM_NOW:
                nextDate = this.today();
                nextDate.add(this.repeatUnit, this.repeatTime);
                break;
            case REPEAT.UNTIL_NOW:
                nextDate = (OrgDate) this.clone();
                OrgDate today = this.today();
                while (nextDate.isToday() || today.compareTo(nextDate) > 0)
                    nextDate.add(this.repeatUnit, this.repeatTime);
                break;
            case REPEAT.NONE:
            default:
                break;
        }
        return nextDate;
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Helpers
    ////////////////////////////////////////////////////////////////////////////////

    private static Map<String, Integer> REPEAT_StringToInteger = new HashMap<>();
    static {
        REPEAT_StringToInteger.put("", REPEAT.ONCE);
        REPEAT_StringToInteger.put(".", REPEAT.FROM_NOW);
        REPEAT_StringToInteger.put("+", REPEAT.UNTIL_NOW);
        REPEAT_StringToInteger.put("h", Calendar.HOUR_OF_DAY);
        REPEAT_StringToInteger.put("d", Calendar.DAY_OF_YEAR);
        REPEAT_StringToInteger.put("w", Calendar.WEEK_OF_YEAR);
        REPEAT_StringToInteger.put("m", Calendar.MONTH);
        REPEAT_StringToInteger.put("y", Calendar.YEAR);
    }

    private static Map<Integer, String> REPEAT_IntegerToString = new HashMap<>();
    static {
        REPEAT_IntegerToString.put(REPEAT.ONCE, "");
        REPEAT_IntegerToString.put(REPEAT.FROM_NOW, ".");
        REPEAT_IntegerToString.put(REPEAT.UNTIL_NOW, "+");
        REPEAT_IntegerToString.put(Calendar.HOUR_OF_DAY, "h");
        REPEAT_IntegerToString.put(Calendar.DAY_OF_YEAR, "d");
        REPEAT_IntegerToString.put(Calendar.WEEK_OF_YEAR, "w");
        REPEAT_IntegerToString.put(Calendar.MONTH, "m");
        REPEAT_IntegerToString.put(Calendar.YEAR, "y");
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
        sb.append(OrgDate.REPEAT_IntegerToString.get(this.repeatType));
        sb.append("+").append(this.repeatTime);
        sb.append(OrgDate.REPEAT_IntegerToString.get(this.repeatUnit));
    }

}
