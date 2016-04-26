package com.github.beardlybread.orgestrator.org;

import com.github.beardlybread.orgestrator.org.antlr.OrgParser;

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

    public OrgDate(OrgParser.DateContext ctx) {
        super();
        Matcher d = MATCH_DATE.matcher(ctx.DATE().getText());
        int year = Integer.parseInt(d.group(1));
        int month = Integer.parseInt(d.group(2));
        int day = Integer.parseInt(d.group(3));
        int hour = 0, minute = 0;
        // ignore DOW
        if (ctx.TIME() != null) {
            Matcher t = MATCH_TIME.matcher(ctx.TIME().getText());
            hour = Integer.parseInt(t.group(1));
            minute = Integer.parseInt(t.group(2));
        }
        this.set(year, month, day, hour, minute);
        if (ctx.REPEAT() != null) {
            Matcher r = MATCH_REPEAT.matcher(ctx.REPEAT().getText());
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
