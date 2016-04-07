import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;

import java.io.StringReader;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

public class OrgLexerTest {

    @Parameters
    public static Collection<TestCase> data () {
        return Arrays.asList(new Object[][] {
            {   "Heading - 1 star",
                "* heading",
                new TestToken[] {
                    new TestToken("HEADING_LEVEL", "* "),
                    new TestToken("HEADING", "heading\n")
            }
            }, {"Heading - 3 stars",
                "*** heading",
                new TestToken[] {
                    new TestToken("HEADING_LEVEL", "*** "),
                    new TestToken("HEADING", "heading\n")
            }
            }, {"Heading - no space (negative)",
                "*heading",
                new TestToken[] {
                    new TestToken("LINE", "*heading\n")
            }
            }, {"Heading - TODO",
                "* TODO something",
                new TestToken[] {
                    new TestToken("HEADING_LEVEL", "* "),
                    new TestToken("TODO", "TODO"),
                    new TestToken("LINE", "something\n")
            }
            }, {"Heading - DONE",
                "** DONE something",
                new TestToken[] {
                    new TestToken("HEADING_LEVEL", "** "),
                    new TestToken("TODO", "DONE"),
                    new TestToken("LINE", "something\n")
            }
            }, {"Heading - TODO no space (negative)",
                "* TODOsomething",
                new TestToken[] {
                    new TestToken("HEADING_LEVEL", "* "),
                    new TestToken("HEADING", "TODOsomething\n")
            }
            }, {"Indent - 2 spaces",
                "  something",
                new TestToken[] {
                    new TestToken("INDENT", "  "),
                    new TestToken("LINE", "something\n")
            }
            }, {"Indent - 4 spaces",
                "    something",
                new TestToken[] {
                    new TestToken("INDENT", "    "),
                    new TestToken("LINE", "something\n")
            }
            }, {"Table - 1 column",
                "|content|",
                new TestToken[] {
                    new TestToken("TABLE", "|"),
                    new TestToken("TABLE_COL", "content"),
                    new TestToken("TABLE_SEP", "|"),
                    new TestToken("END_TABLE", "\n")
            }
            }, {"Table - 2 columns",
                "|more|content|",
                new TestToken[] {
                    new TestToken("TABLE", "|"),
                    new TestToken("TABLE_COL", "more"),
                    new TestToken("TABLE_SEP", "|"),
                    new TestToken("TABLE_COL", "content"),
                    new TestToken("TABLE_SEP", "|"),
                    new TestToken("END_TABLE", "\n")
            }
            }, {"Table - escape column separator",
                "|more\\|content|",
                new TestToken[] {
                    new TestToken("TABLE", "|"),
                    new TestToken("TABLE_COL", "more\\|content"),
                    new TestToken("TABLE_SEP", "|"),
                    new TestToken("END_TABLE", "\n")
            }
            }, {"Table - empty column",
                "||content|",
                new TestToken[] {
                    new TestToken("TABLE", "|"),
                    new TestToken("TABLE_COL", ""),
                    new TestToken("TABLE_SEP", "|"),
                    new TestToken("TABLE_COL", "content"),
                    new TestToken("TABLE_SEP", "|"),
                    new TestToken("END_TABLE", "\n")
            }
            }, {"Table - indent",
                "  |content|",
                new TestToken[] {
                    new TestToken("INDENT", "  "),
                    new TestToken("TABLE", "|"),
                    new TestToken("TABLE_COL", "content"),
                    new TestToken("TABLE_SEP", "|"),
                    new TestToken("END_TABLE", "\n")
            }
            }, {"Unenumerated - hyphen",
                "- item",
                new TestToken[] {
                    new TestToken("ULIST", "- "),
                    new TestToken("LINE", "item\n")
            }
            }, {"Unenumerated - plus",
                "+ item",
                new TestToken[] {
                    new TestToken("ULIST", "+ "),
                    new TestToken("LINE", "item\n")
            }
            }, {"Unenumerated - indent hyphen",
                "  - item",
                new TestToken[] {
                    new TestToken("INDENT", "  "),
                    new TestToken("ULIST", "- "),
                    new TestToken("LINE", "item\n")
            }
            }, {"Unenumerated - indent plus",
                "  + item",
                new TestToken[] {
                    new TestToken("INDENT", "  "),
                    new TestToken("ULIST", "+ "),
                    new TestToken("LINE", "item\n")
            }
            }, {"Unenumerated - indent star",
                "  * item",
                new TestToken[] {
                    new TestToken("INDENT", "  "),
                    new TestToken("ULIST", "* "),
                    new TestToken("LINE", "item\n")
            }
            }, {"Unenumerated - no space (negative)",
                "-42",
                new TestToken[] {
                    new TestToken("LINE", "-42\n")
            }
            }, {"Enumerated - 1 digit",
                "3. item",
                new TestToken[] {
                    new TestToken("ILIST", "3. "),
                    new TestToken("LINE", "item\n")
            }
            }, {"Enumerated - 2 digits",
                "19. item",
                new TestToken[] {
                    new TestToken("ILIST", "19. "),
                    new TestToken("LINE", "item\n")
            }
            }, {"Enumerated - no space (negative)",
                "2.718",
                new TestToken[] {
                    new TestToken("LINE", "2.718\n")
            }
            }, {"Schedule - scheduled",
                "  SCHEDULED:\n",
                new TestToken[] {
            }
            }, {"Schedule - deadline",
                "  DEADLINE:\n",
                new TestToken[] {
            }
            }, {"Schedule - closed",
                "  CLOSED:\n",
                new TestToken[] {
            }
            }, {"Schedule - open timestamp",
                "  SCHEDULED: <2000-01-01 Sat>",
                new TestToken[] {
            }
            }, {"Schedule - closed timestamp",
                "  CLOSED: [2000-01-01 Sat 00:00]",
                new TestToken[] {
            }
            }, {"Properties - last repeat",
                ("  :PROPERTIES:\n" +
                 "  :LAST_REPEAT: [2000-01-01 Sat 00:00]\n" +
                 "  :END:"),
                new TestToken[] {
                 }
            }, {"Properties - prop/value pair",
                ("  :PROPERTIES:\n" +
                 "  :PROP: value\n" +
                 "  :END:"),
                new TestToken[] {
                 }
            }, {"Timestamp - repeat",
                "  DEADLINE: <1999-12-31 Fri +1d>",
                new TestToken[] {
            }
            }, {"Timestamp - repeat dot",
                "  DEADLINE: <1999-12-31 Fri .+1d>",
                new TestToken[] {
            }
            }, {"Timestamp - repeat plus",
                "  DEADLINE: <1999-12-31 Fri ++1d>",
                new TestToken[] {
            }
            }, {"Timestamp - repeat 14 days",
                "  DEADLINE: <1999-12-31 Fri +14d>",
                new TestToken[] {
            }
            }, {"Timestamp - repeat 2 weeks",
                "  DEADLINE: <1999-12-31 Fri +2w>"
                    new TestToken[] {
                    }
            },
        }
    };

    }

    private String label;
    private String input;
    private TestToken[] expected;

    public OrgLexerTest (String label, String input, TestToken[] expected) {
        this.label = label;
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void tester () {
        StringReader r = new StringReader(input + "\n");
        try {
            ANTLRInputStream ais = new ANTLRInputStream(r);
            OrgLexer lexer = new OrgLexer(ais);
            CommonTokenStream cts = new CommonTokenStream(lexer);
            cts.fill();
            List<Token> toks = cts.getTokens();
            ArrayList<TestToken> ttoks = new ArrayList<>();
            for (int i = 0; i < toks.size() - 1; i++) {
                ttoks.add(new TestToken(
                    lexer.VOCABULARY.getSymbolicName(toks[i].getType()),
                    toks[i].getText()));
            }
            assertArrayEquals(label, expected, (TestToken[]) ttoks.toArray());
        } finally {
            if (r != null) r.close();
        }
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
