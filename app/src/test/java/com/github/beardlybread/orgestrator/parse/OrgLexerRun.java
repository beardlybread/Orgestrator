import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.IOException;
import java.util.List;


public class OrgLexerRun {

    public static void main (String[] args) throws IOException {
        InputStreamReader r = new InputStreamReader(System.in);
        try {
            OrgLexer lexer = new OrgLexer(null);
            ANTLRInputStream ais = new ANTLRInputStream(r);
            lexer.setInputStream(ais);
            CommonTokenStream toks = new CommonTokenStream(lexer);
            toks.fill();
            List<Token> tokens = toks.getTokens();
            for (Token tok: tokens) {
                System.out.printf("%16s -> \"%s\"\n",
                        lexer.VOCABULARY.getSymbolicName(tok.getType()),  
                        tok.getText().replace("\n", "\\n"));
            }
        } finally {
            if (r != null) r.close();
        }
    }
}
