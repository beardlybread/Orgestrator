package com.github.beardlybread.orgestrator.org.antlr;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;


public class OrgLexerRun {

    public static void main (String[] args) throws IOException {
        try (InputStreamReader r = new InputStreamReader(
                args.length < 1 ? System.in : new FileInputStream(args[0]))) {
            OrgLexer lexer = new OrgLexer(null);
            ANTLRInputStream ais = new ANTLRInputStream(r);
            lexer.setInputStream(ais);
            CommonTokenStream toks = new CommonTokenStream(lexer);
            toks.fill();
            List<Token> tokens = toks.getTokens();
            for (Token tok : tokens) {
                System.out.printf("%16s -> \"%s\"\n",
                        OrgLexer.VOCABULARY.getSymbolicName(tok.getType()),
                        tok.getText().replace("\n", "\\n"));
            }
        }
    }
}
