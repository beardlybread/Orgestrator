import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class OrgLexerTest {

    @Parameters
    public static Collection<TestCase> data () {
        return Arrays.asList(new TestCase[] {
            new TestCase(
                    "Heading - 1 star",
                    "* heading",
                    new TestToken[] {
                        new TestToken("HEADING_LEVEL", "* "),
                        new TestToken("HEADING", "heading\\n")
                    }),
            new TestCase(
                    "Heading - 3 stars",
                    "*** heading",
                    new TestToken[] {
                        new TestToken("HEADING_LEVEL", "*** "),
                        new TestToken("HEADING", "heading\\n")
                    }),
            new TestCase(
                    "Heading - no space (negative)",
                    "*heading",
                    new TestToken[] {
                        new TestToken("LINE", "*heading\\n")
                    }),
            new TestCase(
                    "Heading - TODO",
                    "* TODO something",
                    new TestToken[] {
                        new TestToken("HEADING_LEVEL", "* "),
                        new TestToken("TODO", "TODO"),
                        new TestToken("LINE", "something\\n")
                    }),
            new TestCase(
                    "Heading - DONE",
                    "** DONE something",
                    new TestToken[] {
                        new TestToken("HEADING_LEVEL", "** "),
                        new TestToken("TODO", "DONE"),
                        new TestToken("LINE", "something\\n")
                    }),
            new TestCase(
                    "Heading - TODO no space (negative)",
                    "* TODOsomething",
                    new TestToken[] {
                        new TestToken("HEADING_LEVEL", "* "),
                        new TestToken("HEADING", "TODOsomething\\n")
                    }),
            new TestCase(
                    "Indent - 2 spaces",
                    "  something",
                    new TestToken[] {
                        new TestToken("INDENT", "  "),
                        new TestToken("LINE", "something\\n")
                    }),
            new TestCase(
                    "Indent - 4 spaces",
                    "    something",
                    new TestToken[] {
                        new TestToken("INDENT", "    "),
                        new TestToken("LINE", "something\\n")
                    }),
            new TestCase(
                    "Table - 1 column",
                    "|content|",
                    new TestToken[] {
                        new TestToken("TABLE", "|"),
                        new TestToken("TABLE_COL", "content"),
                        new TestToken("TABLE_SEP", "|"),
                        new TestToken("END_TABLE", "\\n")
                    }),
            new TestCase(
                    "Table - 2 columns",
                    "|more|content|",
                    new TestToken[] {
                        new TestToken("TABLE", "|"),
                        new TestToken("TABLE_COL", "more"),
                        new TestToken("TABLE_SEP", "|"),
                        new TestToken("TABLE_COL", "content"),
                        new TestToken("TABLE_SEP", "|"),
                        new TestToken("END_TABLE", "\\n")
                    }),
            new TestCase(
                    "Table - escape column separator",
                    "|more\\|content|",
                    new TestToken[] {
                        new TestToken("TABLE", "|"),
                        new TestToken("TABLE_COL", "more\\|content"),
                        new TestToken("TABLE_SEP", "|"),
                        new TestToken("END_TABLE", "\\n")
                    }),
            new TestCase(
                    "Table - empty column",
                    "||content|",
                    new TestToken[] {
                        new TestToken("TABLE", "|"),
                        new TestToken("TABLE_COL", ""),
                        new TestToken("TABLE_SEP", "|"),
                        new TestToken("TABLE_COL", "content"),
                        new TestToken("TABLE_SEP", "|"),
                        new TestToken("END_TABLE", "\\n")
                    }),
            new TestCase(
                    "Table - indent",
                    "  |content|",
                    new TestToken[] {
                        new TestToken("INDENT", "  "),
                        new TestToken("TABLE", "|"),
                        new TestToken("TABLE_COL", "content"),
                        new TestToken("TABLE_SEP", "|"),
                        new TestToken("END_TABLE", "\\n")
                    }),
            new TestCase(
                    "Unenumerated - hyphen",
                    "- item",
                    new TestToken[] {
                        new TestToken("ULIST", "- "),
                        new TestToken("LINE", "item\\n")
                    }),
            new TestCase(
                    "Unenumerated - plus",
                    "+ item",
                    new TestToken[] {
                        new TestToken("ULIST", "+ "),
                        new TestToken("LINE", "item\\n")
                    }),
            new TestCase(
                    "Unenumerated - indent hyphen",
                    "  - item",
                    new TestToken[] {
                        new TestToken("INDENT", "  "),
                        new TestToken("ULIST", "- "),
                        new TestToken("LINE", "item\\n")
                    }),
            new TestCase(
                    "Unenumerated - indent plus",
                    "  + item",
                    new TestToken[] {
                        new TestToken("INDENT", "  "),
                        new TestToken("ULIST", "+ "),
                        new TestToken("LINE", "item\\n")
                    }),
            new TestCase(
                    "Unenumerated - indent star",
                    "  * item",
                    new TestToken[] {
                        new TestToken("INDENT", "  "),
                        new TestToken("ULIST", "* "),
                        new TestToken("LINE", "item\\n")
                    }),
            new TestCase(
                    "Unenumerated - no space (negative)",
                    "-42",
                    new TestToken[] {
                        new TestToken("LINE", "-42\\n")
                    }),
            new TestCase(
                    "Enumerated - 1 digit",
                    "3. item",
                    new TestToken[] {
                        new TestToken("ILIST", "3. "),
                        new TestToken("LINE", "item\\n")
                    }),
            new TestCase(
                    "Enumerated - 2 digits",
                    "19. item",
                    new TestToken[] {
                        new TestToken("ILIST", "19. "),
                        new TestToken("LINE", "item\\n")
                    }),
            new TestCase(
                    "Enumerated - no space (negative)",
                    "2.718",
                    new TestToken[] {
                        new TestToken("LINE", "2.718\\n")
                    }),
            new TestCase(
                    "Schedule - scheduled",
                    "  SCHEDULED:\\n",
                    new TestToken[] {
                    }),
            new TestCase(
                    "Schedule - deadline",
                    "  DEADLINE:\\n",
                    new TestToken[] {
                    }),
            new TestCase(
                    "Schedule - closed",
                    "  CLOSED:\\n",
                    new TestToken[] {
                    }),
            new TestCase(
                    "Schedule - open timestamp",
                    "  SCHEDULED: <2000-01-01 Sat>",
                    new TestToken[] {
                    }),
            new TestCase(
                    "Schedule - closed timestamp",
                    "  CLOSED: [2000-01-01 Sat 00:00]",
                    new TestToken[] {
                    }),
            new TestCase(
                    "Properties - last repeat",
                    ("  :PROPERTIES:\\n" +
                     "  :LAST_REPEAT: [2000-01-01 Sat 00:00]\\n" +
                     "  :END:"),
                    new TestToken[] {
                    }),
            new TestCase(
                    "Properties - prop/value pair",
                    ("  :PROPERTIES:\\n" +
                     "  :PROP: value\\n" +
                     "  :END:"),
                    new TestToken[] {
                    }),
            new TestCase(
                    "Timestamp - repeat",
                    "  DEADLINE: <1999-12-31 Fri +1d>",
                    new TestToken[] {
                    }),
            new TestCase(
                    "Timestamp - repeat dot",
                    "  DEADLINE: <1999-12-31 Fri .+1d>",
                    new TestToken[] {
                    }),
            new TestCase(
                    "Timestamp - repeat plus",
                    "  DEADLINE: <1999-12-31 Fri ++1d>",
                    new TestToken[] {
                    }),
            new TestCase(
                    "Timestamp - repeat 14 days",
                    "  DEADLINE: <1999-12-31 Fri +14d>",
                    new TestToken[] {
                    }),
            new TestCase(
                    "Timestamp - repeat 2 weeks",
                    "  DEADLINE: <1999-12-31 Fri +2w>"
                    new TestToken[] {
                    }),
        });

    }

    class TestToken {
        public String type;
        public String value;

        public TestToken (String t, String v) {
            this.type = t;
            this.value = v;
        }
    }

    class TestCase {
        public String label;
        public String input;
        public TestToken[] expected;

        public TestCase (String label, String input, TestToken[] expected) {
            this.label = label;
            this.input = input;
            this.expected = expected;
        }
    }

}
