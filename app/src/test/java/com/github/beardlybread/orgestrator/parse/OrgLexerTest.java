package com.github.beardlybread.orgestrator.parse;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import java.io.StringReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class OrgLexerTest { 

    @Parameters
    public static Collection<String[][]> data () {
        return Arrays.asList(new String[][][] {
            {  {"Heading - 1 star"},
                {"* heading"},
                {   "HEADING_LEVEL", "* ",
                    "HEADING", "heading\n"}

            }, {{"Heading - 3 stars"},
                {"*** heading"},
                {   "HEADING_LEVEL", "*** ",
                    "HEADING", "heading\n"}

            }, {{"Heading - no space (negative)"},
                {"*heading"},
                {   "LINE", "*heading\n"}

            }, {{"Heading - TODO"},
                {"* TODO something"},
                {   "HEADING_LEVEL", "* ",
                    "TODO", "TODO ",
                    "LINE", "something\n"}

            }, {{"Heading - DONE"},
                {"** DONE something"},
                {   "HEADING_LEVEL", "** ",
                    "TODO", "DONE ",
                    "LINE", "something\n"}

            }, {{"Heading - TODO no space (negative)"},
                {"* TODOsomething"},
                {   "HEADING_LEVEL", "* ",
                    "HEADING", "TODOsomething\n"}

            }, {{"Indent - 2 spaces"},
                {"  something"},
                {   "INDENT", "  ",
                    "LINE", "something\n"}

            }, {{"Indent - 4 spaces"},
                {"    something"},
                {   "INDENT", "    ",
                    "LINE", "something\n"}

            }, {{"Table - 1 column"},
                {"|content|"},
                {   "TABLE", "|",
                    "TABLE_COL", "content",
                    "TABLE_SEP", "|",
                    "END_TABLE", "\n"}

            }, {{"Table - 2 columns"},
                {"|more|content|"},
                {   "TABLE", "|",
                    "TABLE_COL", "more",
                    "TABLE_SEP", "|",
                    "TABLE_COL", "content",
                    "TABLE_SEP", "|",
                    "END_TABLE", "\n"}

            }, {{"Table - escape column separator"},
                {"|more\\|content|"},
                {   "TABLE", "|",
                    "TABLE_COL", "more\\|content",
                    "TABLE_SEP", "|",
                    "END_TABLE", "\n"}

            }, {{"Table - empty column"},
                {"||content|"},
                {   "TABLE", "|",
                    "TABLE_SEP", "|",
                    "TABLE_COL", "content",
                    "TABLE_SEP", "|",
                    "END_TABLE", "\n"}

            }, {{"Table - indent"},
                {"  |content|"},
                {   "INDENT", "  ",
                    "TABLE", "|",
                    "TABLE_COL", "content",
                    "TABLE_SEP", "|",
                    "END_TABLE", "\n"}

            }, {{"Unenumerated - hyphen"},
                {"- item"},
                {   "ULIST", "- ",
                    "LINE", "item\n"}

            }, {{"Unenumerated - plus"},
                {"+ item"},
                {   "ULIST", "+ ",
                    "LINE", "item\n"}

            }, {{"Unenumerated - indent hyphen"},
                {"  - item"},
                {   "INDENT", "  ",
                    "ULIST", "- ",
                    "LINE", "item\n"}

            }, {{"Unenumerated - indent plus"},
                {"  + item"},
                {   "INDENT", "  ",
                    "ULIST", "+ ",
                    "LINE", "item\n"}

            }, {{"Unenumerated - indent star"},
                {"  * item"},
                {   "INDENT", "  ",
                    "ULIST", "* ",
                    "LINE", "item\n"}

            }, {{"Unenumerated - no space (negative)"},
                {"-42"},
                {   "LINE", "-42\n"}

            }, {{"Enumerated - 1 digit"},
                {"3. item"},
                {   "ILIST", "3. ",
                    "LINE", "item\n"}

            }, {{"Enumerated - 2 digits"},
                {"19. item"},
                {   "ILIST", "19. ",
                    "LINE", "item\n"}

            }, {{"Enumerated - no space (negative)"},
                {"2.718"},
                {   "LINE", "2.718\n"}

            }, {{"Schedule - scheduled"},
                {"  SCHEDULED:\n"},
                {   "INDENT", "  ",
                    "SCHEDULE", "SCHEDULED:",
                    "END_SCHEDULE", "\n"}

            }, {{"Schedule - deadline"},
                {"  DEADLINE:\n"},
                {   "INDENT", "  ",
                    "SCHEDULE", "DEADLINE:",
                    "END_SCHEDULE", "\n"}

            }, {{"Schedule - closed"},
                {"  CLOSED:\n"},
                {   "INDENT", "  ",
                    "SCHEDULE", "CLOSED:",
                    "END_SCHEDULE", "\n"}

            }, {{"Schedule - open timestamp"},
                {"  SCHEDULED: <2000-01-01 Sat>"},
                {   "INDENT", "  ",
                    "SCHEDULE", "SCHEDULED:",
                    "TIMESTAMP", "<",
                    "DATE", "2000-01-01",
                    "DOW", "Sat",
                    "END_TIMESTAMP", ">",
                    "END_SCHEDULE", "\n"}

            }, {{"Schedule - closed timestamp"},
                {"  CLOSED: [2000-01-01 Sat 00:00]"},
                {   "INDENT", "  ",
                    "SCHEDULE", "CLOSED:",
                    "TIMESTAMP", "[",
                    "DATE", "2000-01-01",
                    "DOW", "Sat",
                    "TIME", "00:00",
                    "END_TIMESTAMP", "]",
                    "END_SCHEDULE", "\n"}

            }, {{"Properties - last repeat"},
                {"  :PROPERTIES:\n" +
                 "  :LAST_REPEAT: [2000-01-01 Sat 00:00]\n" +
                 "  :END:"},
                {   "INDENT", "  ",
                    "PROPERTIES", ":PROPERTIES:",
                    "LAST_REPEAT", ":LAST_REPEAT: [",
                    "DATE", "2000-01-01",
                    "DOW", "Sat",
                    "TIME", "00:00",
                    "END_TIMESTAMP", "]",
                    "END_PROPERTIES", ":END:"}

            }, {{"Properties - prop/value pair"},
                {"  :PROPERTIES:\n" +
                 "  :PROP: value\n" +
                 "  :END:"},
                {   "INDENT", "  ",
                    "PROPERTIES", ":PROPERTIES:",
                    "PROPERTY", ":PROP:",
                    "VALUE", "value",
                    "END_PROPERTIES", ":END:"}

            }, {{"Timestamp - repeat"},
                {"  DEADLINE: <1999-12-31 Fri +1d>"},
                {   "INDENT", "  ",
                    "SCHEDULE", "DEADLINE:",
                    "TIMESTAMP", "<",
                    "DATE", "1999-12-31",
                    "DOW", "Fri",
                    "REPEAT", "+1d",
                    "END_TIMESTAMP", ">",
                    "END_SCHEDULE", "\n"}

            }, {{"Timestamp - repeat dot"},
                {"  DEADLINE: <1999-12-31 Fri .+1d>"},
                {   "INDENT", "  ",
                    "SCHEDULE", "DEADLINE:",
                    "TIMESTAMP", "<",
                    "DATE", "1999-12-31",
                    "DOW", "Fri",
                    "REPEAT", ".+1d",
                    "END_TIMESTAMP", ">",
                    "END_SCHEDULE", "\n"}

            }, {{"Timestamp - repeat plus"},
                {"  DEADLINE: <1999-12-31 Fri ++1d>"},
                {   "INDENT", "  ",
                    "SCHEDULE", "DEADLINE:",
                    "TIMESTAMP", "<",
                    "DATE", "1999-12-31",
                    "DOW", "Fri",
                    "REPEAT", "++1d",
                    "END_TIMESTAMP", ">",
                    "END_SCHEDULE", "\n"}

            }, {{"Timestamp - repeat 14 days"},
                {"  DEADLINE: <1999-12-31 Fri +14d>"},
                {   "INDENT", "  ",
                    "SCHEDULE", "DEADLINE:",
                    "TIMESTAMP", "<",
                    "DATE", "1999-12-31",
                    "DOW", "Fri",
                    "REPEAT", "+14d",
                    "END_TIMESTAMP", ">",
                    "END_SCHEDULE", "\n"}

            }, {{"Timestamp - repeat 2 weeks"},
                {"  DEADLINE: <1999-12-31 Fri +2w>"},
                {   "INDENT", "  ",
                    "SCHEDULE", "DEADLINE:",
                    "TIMESTAMP", "<",
                    "DATE", "1999-12-31",
                    "DOW", "Fri",
                    "REPEAT", "+2w",
                    "END_TIMESTAMP", ">",
                    "END_SCHEDULE", "\n"}
            }
        });
    }

    private String label;
    private String input;
    private ArrayList<TestToken> expected;

    public OrgLexerTest (String[] label, String[] input, String[] expected) {
        this.label = label[0];
        this.input = input[0];
        this.expected = new ArrayList<>();
        for (int i = 0; i < expected.length; i += 2) {
            this.expected.add(new TestToken(expected[i], expected[i + 1]));
        }
    }

    @Test
    public void tester () throws IOException {
        try (StringReader r = new StringReader(input + "\n")) {
            ANTLRInputStream ais = new ANTLRInputStream(r);
            OrgLexer lexer = new OrgLexer(ais);
            CommonTokenStream cts = new CommonTokenStream(lexer);
            cts.fill();
            List<Token> toks = cts.getTokens();
            for (int i = 0; i < toks.size() - 1; i++) {
                assertEquals(this.label, this.expected.get(i).type,
                        OrgLexer.VOCABULARY.getSymbolicName(toks.get(i).getType()));
                assertEquals(this.label, this.expected.get(i).value,
                        toks.get(i).getText());
            }
        }
    }

    class TestToken {public String type;
        public String value;

        public TestToken (String t, String v) {
            this.type = t;
            this.value = v;
        }
    }

}
