package com.github.beardlybread.orgestrator.org.antlr;

// Generated from OrgLexer.g4 by ANTLR 4.5
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class OrgLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		HEADING_LEVEL=1, INDENT=2, TABLE=3, ULIST=4, ILIST=5, EMPTY=6, TODO=7, 
		HEADING=8, SCHEDULED=9, DEADLINE=10, CLOSED=11, PROPERTIES=12, END_TABLE=13, 
		TABLE_COL=14, TABLE_SEP=15, LINE=16, END_SCHEDULE=17, TIMESTAMP=18, CLOSED_DEADLINE=19, 
		CLOSED_SCHEDULED=20, END_PROPERTIES=21, LAST_REPEAT=22, PROPERTY=23, VALUE=24, 
		END_TIMESTAMP=25, DATE=26, DOW=27, TIME=28, REPEAT=29;
	public static final int HEADING_MODE = 1;
	public static final int INDENT_MODE = 2;
	public static final int TABLE_MODE = 3;
	public static final int LINE_MODE = 4;
	public static final int SCHEDULE_MODE = 5;
	public static final int PROPERTIES_MODE = 6;
	public static final int DATE_MODE = 7;
	public static String[] modeNames = {
		"DEFAULT_MODE", "HEADING_MODE", "INDENT_MODE", "TABLE_MODE", "LINE_MODE", 
		"SCHEDULE_MODE", "PROPERTIES_MODE", "DATE_MODE"
	};

	public static final String[] ruleNames = {
		"IL", "NL", "WS", "DT", "DW", "TM", "HEADING_LEVEL", "INDENT", "TABLE", 
		"ULIST", "ILIST", "EMPTY", "Line", "TODO", "HEADING", "HeaderMore", "SCHEDULED", 
		"DEADLINE", "CLOSED", "PROPERTIES", "Table", "UList", "IList", "LineIndent", 
		"END_TABLE", "TABLE_COL", "TABLE_SEP", "LINE", "LineMore", "END_SCHEDULE", 
		"TIMESTAMP", "B_timestamp", "CLOSED_DEADLINE", "CLOSED_SCHEDULED", "Schedule_WS", 
		"END_PROPERTIES", "LAST_REPEAT", "PROPERTY", "VALUE", "Properties_WS", 
		"END_TIMESTAMP", "DATE", "DOW", "TIME", "REPEAT", "Date_WS"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, "'CLOSED:'", 
		"':PROPERTIES:'", null, null, null, null, null, "'<'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "HEADING_LEVEL", "INDENT", "TABLE", "ULIST", "ILIST", "EMPTY", "TODO", 
		"HEADING", "SCHEDULED", "DEADLINE", "CLOSED", "PROPERTIES", "END_TABLE", 
		"TABLE_COL", "TABLE_SEP", "LINE", "END_SCHEDULE", "TIMESTAMP", "CLOSED_DEADLINE", 
		"CLOSED_SCHEDULED", "END_PROPERTIES", "LAST_REPEAT", "PROPERTY", "VALUE", 
		"END_TIMESTAMP", "DATE", "DOW", "TIME", "REPEAT"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public OrgLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "OrgLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\37\u01b5\b\1\b\1"+
		"\b\1\b\1\b\1\b\1\b\1\b\1\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t"+
		"\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t"+
		"\17\4\20\t\20\4\21\t\21\4\22\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t"+
		"\26\4\27\t\27\4\30\t\30\4\31\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t"+
		"\35\4\36\t\36\4\37\t\37\4 \t \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4"+
		"\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4,\t,\4-\t-\4.\t.\4/\t/\3\2\6\2h\n\2\r"+
		"\2\16\2i\3\2\3\2\3\2\3\3\5\3p\n\3\3\3\3\3\3\4\6\4u\n\4\r\4\16\4v\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6\u0099\n\6\3\7"+
		"\3\7\3\7\3\7\3\7\3\7\3\b\6\b\u00a2\n\b\r\b\16\b\u00a3\3\b\3\b\3\b\3\b"+
		"\3\t\6\t\u00ab\n\t\r\t\16\t\u00ac\3\t\3\t\3\n\3\n\3\n\3\n\3\13\3\13\3"+
		"\13\3\13\3\13\3\f\3\f\3\f\3\f\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\17\3"+
		"\17\3\17\3\17\3\17\3\17\3\17\3\17\5\17\u00cd\n\17\3\17\3\17\3\17\3\17"+
		"\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\30"+
		"\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\33"+
		"\3\33\3\33\7\33\u0129\n\33\f\33\16\33\u012c\13\33\3\34\3\34\3\35\3\35"+
		"\3\35\3\35\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3 \3 \3 \3 \3!\3!\3"+
		"!\3!\3!\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3#\3#\3"+
		"#\3#\3#\3#\3$\3$\3$\3$\3$\3%\3%\3%\3%\3%\3%\3%\7%\u0166\n%\f%\16%\u0169"+
		"\13%\3%\3%\3%\3%\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\7&\u017e"+
		"\n&\f&\16&\u0181\13&\3&\3&\3&\3&\3\'\3\'\6\'\u0189\n\'\r\'\16\'\u018a"+
		"\3\'\3\'\3(\6(\u0190\n(\r(\16(\u0191\3)\6)\u0195\n)\r)\16)\u0196\3)\3"+
		")\3)\3*\3*\3*\3*\3+\3+\3,\3,\3-\3-\3.\5.\u01a7\n.\3.\3.\6.\u01ab\n.\r"+
		".\16.\u01ac\3.\3.\3/\3/\3/\3/\3/\3\u01ac\2\60\n\2\f\2\16\2\20\2\22\2\24"+
		"\2\26\3\30\4\32\5\34\6\36\7 \b\"\2$\t&\n(\2*\13,\f.\r\60\16\62\2\64\2"+
		"\66\28\2:\17<\20>\21@\22B\2D\23F\24H\2J\25L\26N\2P\27R\30T\31V\32X\2Z"+
		"\33\\\34^\35`\36b\37d\2\n\2\3\4\5\6\7\b\t\21\3\2\62;\4\2\13\13\"\"\3\2"+
		"\63;\3\2\62\63\3\2\62\65\3\2\62\64\3\2\62\67\4\2--//\4\2,-//\5\2\f\f\17"+
		"\17~~\4\2C\\aa\5\2\13\f\17\17\"\"\4\2@@__\4\2--\60\60\7\2ffjjooyy{{\u01bc"+
		"\2\26\3\2\2\2\2\30\3\2\2\2\2\32\3\2\2\2\2\34\3\2\2\2\2\36\3\2\2\2\2 \3"+
		"\2\2\2\2\"\3\2\2\2\3$\3\2\2\2\3&\3\2\2\2\3(\3\2\2\2\4*\3\2\2\2\4,\3\2"+
		"\2\2\4.\3\2\2\2\4\60\3\2\2\2\4\62\3\2\2\2\4\64\3\2\2\2\4\66\3\2\2\2\4"+
		"8\3\2\2\2\5:\3\2\2\2\5<\3\2\2\2\5>\3\2\2\2\6@\3\2\2\2\6B\3\2\2\2\7D\3"+
		"\2\2\2\7F\3\2\2\2\7H\3\2\2\2\7J\3\2\2\2\7L\3\2\2\2\7N\3\2\2\2\bP\3\2\2"+
		"\2\bR\3\2\2\2\bT\3\2\2\2\bV\3\2\2\2\bX\3\2\2\2\tZ\3\2\2\2\t\\\3\2\2\2"+
		"\t^\3\2\2\2\t`\3\2\2\2\tb\3\2\2\2\td\3\2\2\2\ng\3\2\2\2\fo\3\2\2\2\16"+
		"t\3\2\2\2\20x\3\2\2\2\22\u0098\3\2\2\2\24\u009a\3\2\2\2\26\u00a1\3\2\2"+
		"\2\30\u00aa\3\2\2\2\32\u00b0\3\2\2\2\34\u00b4\3\2\2\2\36\u00b9\3\2\2\2"+
		" \u00bd\3\2\2\2\"\u00bf\3\2\2\2$\u00cc\3\2\2\2&\u00d2\3\2\2\2(\u00d6\3"+
		"\2\2\2*\u00da\3\2\2\2,\u00e7\3\2\2\2.\u00f3\3\2\2\2\60\u00fd\3\2\2\2\62"+
		"\u010c\3\2\2\2\64\u0111\3\2\2\2\66\u0117\3\2\2\28\u011c\3\2\2\2:\u0121"+
		"\3\2\2\2<\u012a\3\2\2\2>\u012d\3\2\2\2@\u012f\3\2\2\2B\u0133\3\2\2\2D"+
		"\u0137\3\2\2\2F\u013b\3\2\2\2H\u013f\3\2\2\2J\u0144\3\2\2\2L\u014e\3\2"+
		"\2\2N\u0159\3\2\2\2P\u015e\3\2\2\2R\u016e\3\2\2\2T\u0186\3\2\2\2V\u018f"+
		"\3\2\2\2X\u0194\3\2\2\2Z\u019b\3\2\2\2\\\u019f\3\2\2\2^\u01a1\3\2\2\2"+
		"`\u01a3\3\2\2\2b\u01a6\3\2\2\2d\u01b0\3\2\2\2fh\t\2\2\2gf\3\2\2\2hi\3"+
		"\2\2\2ig\3\2\2\2ij\3\2\2\2jk\3\2\2\2kl\7\60\2\2lm\7\"\2\2m\13\3\2\2\2"+
		"np\7\17\2\2on\3\2\2\2op\3\2\2\2pq\3\2\2\2qr\7\f\2\2r\r\3\2\2\2su\t\3\2"+
		"\2ts\3\2\2\2uv\3\2\2\2vt\3\2\2\2vw\3\2\2\2w\17\3\2\2\2xy\t\4\2\2yz\t\2"+
		"\2\2z{\t\2\2\2{|\t\2\2\2|}\7/\2\2}~\t\5\2\2~\177\t\2\2\2\177\u0080\7/"+
		"\2\2\u0080\u0081\t\6\2\2\u0081\u0082\t\2\2\2\u0082\21\3\2\2\2\u0083\u0084"+
		"\7U\2\2\u0084\u0085\7w\2\2\u0085\u0099\7p\2\2\u0086\u0087\7O\2\2\u0087"+
		"\u0088\7q\2\2\u0088\u0099\7p\2\2\u0089\u008a\7V\2\2\u008a\u008b\7w\2\2"+
		"\u008b\u0099\7g\2\2\u008c\u008d\7Y\2\2\u008d\u008e\7g\2\2\u008e\u0099"+
		"\7f\2\2\u008f\u0090\7V\2\2\u0090\u0091\7j\2\2\u0091\u0099\7w\2\2\u0092"+
		"\u0093\7H\2\2\u0093\u0094\7t\2\2\u0094\u0099\7k\2\2\u0095\u0096\7U\2\2"+
		"\u0096\u0097\7c\2\2\u0097\u0099\7v\2\2\u0098\u0083\3\2\2\2\u0098\u0086"+
		"\3\2\2\2\u0098\u0089\3\2\2\2\u0098\u008c\3\2\2\2\u0098\u008f\3\2\2\2\u0098"+
		"\u0092\3\2\2\2\u0098\u0095\3\2\2\2\u0099\23\3\2\2\2\u009a\u009b\t\7\2"+
		"\2\u009b\u009c\t\2\2\2\u009c\u009d\7<\2\2\u009d\u009e\t\b\2\2\u009e\u009f"+
		"\t\2\2\2\u009f\25\3\2\2\2\u00a0\u00a2\7,\2\2\u00a1\u00a0\3\2\2\2\u00a2"+
		"\u00a3\3\2\2\2\u00a3\u00a1\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a4\u00a5\3\2"+
		"\2\2\u00a5\u00a6\7\"\2\2\u00a6\u00a7\3\2\2\2\u00a7\u00a8\b\b\2\2\u00a8"+
		"\27\3\2\2\2\u00a9\u00ab\7\"\2\2\u00aa\u00a9\3\2\2\2\u00ab\u00ac\3\2\2"+
		"\2\u00ac\u00aa\3\2\2\2\u00ac\u00ad\3\2\2\2\u00ad\u00ae\3\2\2\2\u00ae\u00af"+
		"\b\t\3\2\u00af\31\3\2\2\2\u00b0\u00b1\7~\2\2\u00b1\u00b2\3\2\2\2\u00b2"+
		"\u00b3\b\n\4\2\u00b3\33\3\2\2\2\u00b4\u00b5\t\t\2\2\u00b5\u00b6\7\"\2"+
		"\2\u00b6\u00b7\3\2\2\2\u00b7\u00b8\b\13\5\2\u00b8\35\3\2\2\2\u00b9\u00ba"+
		"\5\n\2\2\u00ba\u00bb\3\2\2\2\u00bb\u00bc\b\f\5\2\u00bc\37\3\2\2\2\u00bd"+
		"\u00be\5\f\3\2\u00be!\3\2\2\2\u00bf\u00c0\13\2\2\2\u00c0\u00c1\3\2\2\2"+
		"\u00c1\u00c2\b\16\6\2\u00c2\u00c3\b\16\5\2\u00c3#\3\2\2\2\u00c4\u00c5"+
		"\7V\2\2\u00c5\u00c6\7Q\2\2\u00c6\u00c7\7F\2\2\u00c7\u00cd\7Q\2\2\u00c8"+
		"\u00c9\7F\2\2\u00c9\u00ca\7Q\2\2\u00ca\u00cb\7P\2\2\u00cb\u00cd\7G\2\2"+
		"\u00cc\u00c4\3\2\2\2\u00cc\u00c8\3\2\2\2\u00cd\u00ce\3\2\2\2\u00ce\u00cf"+
		"\7\"\2\2\u00cf\u00d0\3\2\2\2\u00d0\u00d1\b\17\2\2\u00d1%\3\2\2\2\u00d2"+
		"\u00d3\5\f\3\2\u00d3\u00d4\3\2\2\2\u00d4\u00d5\b\20\7\2\u00d5\'\3\2\2"+
		"\2\u00d6\u00d7\13\2\2\2\u00d7\u00d8\3\2\2\2\u00d8\u00d9\b\21\6\2\u00d9"+
		")\3\2\2\2\u00da\u00db\7U\2\2\u00db\u00dc\7E\2\2\u00dc\u00dd\7J\2\2\u00dd"+
		"\u00de\7G\2\2\u00de\u00df\7F\2\2\u00df\u00e0\7W\2\2\u00e0\u00e1\7N\2\2"+
		"\u00e1\u00e2\7G\2\2\u00e2\u00e3\7F\2\2\u00e3\u00e4\7<\2\2\u00e4\u00e5"+
		"\3\2\2\2\u00e5\u00e6\b\22\b\2\u00e6+\3\2\2\2\u00e7\u00e8\7F\2\2\u00e8"+
		"\u00e9\7G\2\2\u00e9\u00ea\7C\2\2\u00ea\u00eb\7F\2\2\u00eb\u00ec\7N\2\2"+
		"\u00ec\u00ed\7K\2\2\u00ed\u00ee\7P\2\2\u00ee\u00ef\7G\2\2\u00ef\u00f0"+
		"\7<\2\2\u00f0\u00f1\3\2\2\2\u00f1\u00f2\b\23\b\2\u00f2-\3\2\2\2\u00f3"+
		"\u00f4\7E\2\2\u00f4\u00f5\7N\2\2\u00f5\u00f6\7Q\2\2\u00f6\u00f7\7U\2\2"+
		"\u00f7\u00f8\7G\2\2\u00f8\u00f9\7F\2\2\u00f9\u00fa\7<\2\2\u00fa\u00fb"+
		"\3\2\2\2\u00fb\u00fc\b\24\b\2\u00fc/\3\2\2\2\u00fd\u00fe\7<\2\2\u00fe"+
		"\u00ff\7R\2\2\u00ff\u0100\7T\2\2\u0100\u0101\7Q\2\2\u0101\u0102\7R\2\2"+
		"\u0102\u0103\7G\2\2\u0103\u0104\7T\2\2\u0104\u0105\7V\2\2\u0105\u0106"+
		"\7K\2\2\u0106\u0107\7G\2\2\u0107\u0108\7U\2\2\u0108\u0109\7<\2\2\u0109"+
		"\u010a\3\2\2\2\u010a\u010b\b\25\t\2\u010b\61\3\2\2\2\u010c\u010d\7~\2"+
		"\2\u010d\u010e\3\2\2\2\u010e\u010f\b\26\4\2\u010f\u0110\b\26\n\2\u0110"+
		"\63\3\2\2\2\u0111\u0112\t\n\2\2\u0112\u0113\7\"\2\2\u0113\u0114\3\2\2"+
		"\2\u0114\u0115\b\27\5\2\u0115\u0116\b\27\13\2\u0116\65\3\2\2\2\u0117\u0118"+
		"\5\n\2\2\u0118\u0119\3\2\2\2\u0119\u011a\b\30\5\2\u011a\u011b\b\30\f\2"+
		"\u011b\67\3\2\2\2\u011c\u011d\13\2\2\2\u011d\u011e\3\2\2\2\u011e\u011f"+
		"\b\31\6\2\u011f\u0120\b\31\5\2\u01209\3\2\2\2\u0121\u0122\5\f\3\2\u0122"+
		"\u0123\3\2\2\2\u0123\u0124\b\32\7\2\u0124;\3\2\2\2\u0125\u0126\7^\2\2"+
		"\u0126\u0129\7~\2\2\u0127\u0129\n\13\2\2\u0128\u0125\3\2\2\2\u0128\u0127"+
		"\3\2\2\2\u0129\u012c\3\2\2\2\u012a\u0128\3\2\2\2\u012a\u012b\3\2\2\2\u012b"+
		"=\3\2\2\2\u012c\u012a\3\2\2\2\u012d\u012e\7~\2\2\u012e?\3\2\2\2\u012f"+
		"\u0130\5\f\3\2\u0130\u0131\3\2\2\2\u0131\u0132\b\35\7\2\u0132A\3\2\2\2"+
		"\u0133\u0134\13\2\2\2\u0134\u0135\3\2\2\2\u0135\u0136\b\36\6\2\u0136C"+
		"\3\2\2\2\u0137\u0138\5\f\3\2\u0138\u0139\3\2\2\2\u0139\u013a\b\37\7\2"+
		"\u013aE\3\2\2\2\u013b\u013c\7>\2\2\u013c\u013d\3\2\2\2\u013d\u013e\b "+
		"\r\2\u013eG\3\2\2\2\u013f\u0140\7]\2\2\u0140\u0141\3\2\2\2\u0141\u0142"+
		"\b!\r\2\u0142\u0143\b!\16\2\u0143I\3\2\2\2\u0144\u0145\7F\2\2\u0145\u0146"+
		"\7G\2\2\u0146\u0147\7C\2\2\u0147\u0148\7F\2\2\u0148\u0149\7N\2\2\u0149"+
		"\u014a\7K\2\2\u014a\u014b\7P\2\2\u014b\u014c\7G\2\2\u014c\u014d\7<\2\2"+
		"\u014dK\3\2\2\2\u014e\u014f\7U\2\2\u014f\u0150\7E\2\2\u0150\u0151\7J\2"+
		"\2\u0151\u0152\7G\2\2\u0152\u0153\7F\2\2\u0153\u0154\7W\2\2\u0154\u0155"+
		"\7N\2\2\u0155\u0156\7G\2\2\u0156\u0157\7F\2\2\u0157\u0158\7<\2\2\u0158"+
		"M\3\2\2\2\u0159\u015a\5\16\4\2\u015a\u015b\3\2\2\2\u015b\u015c\b$\17\2"+
		"\u015c\u015d\b$\20\2\u015dO\3\2\2\2\u015e\u015f\7<\2\2\u015f\u0160\7G"+
		"\2\2\u0160\u0161\7P\2\2\u0161\u0162\7F\2\2\u0162\u0163\7<\2\2\u0163\u0167"+
		"\3\2\2\2\u0164\u0166\t\3\2\2\u0165\u0164\3\2\2\2\u0166\u0169\3\2\2\2\u0167"+
		"\u0165\3\2\2\2\u0167\u0168\3\2\2\2\u0168\u016a\3\2\2\2\u0169\u0167\3\2"+
		"\2\2\u016a\u016b\7\f\2\2\u016b\u016c\3\2\2\2\u016c\u016d\b%\7\2\u016d"+
		"Q\3\2\2\2\u016e\u016f\7<\2\2\u016f\u0170\7N\2\2\u0170\u0171\7C\2\2\u0171"+
		"\u0172\7U\2\2\u0172\u0173\7V\2\2\u0173\u0174\7a\2\2\u0174\u0175\7T\2\2"+
		"\u0175\u0176\7G\2\2\u0176\u0177\7R\2\2\u0177\u0178\7G\2\2\u0178\u0179"+
		"\7C\2\2\u0179\u017a\7V\2\2\u017a\u017b\7<\2\2\u017b\u017f\3\2\2\2\u017c"+
		"\u017e\t\3\2\2\u017d\u017c\3\2\2\2\u017e\u0181\3\2\2\2\u017f\u017d\3\2"+
		"\2\2\u017f\u0180\3\2\2\2\u0180\u0182\3\2\2\2\u0181\u017f\3\2\2\2\u0182"+
		"\u0183\7]\2\2\u0183\u0184\3\2\2\2\u0184\u0185\b&\r\2\u0185S\3\2\2\2\u0186"+
		"\u0188\7<\2\2\u0187\u0189\t\f\2\2\u0188\u0187\3\2\2\2\u0189\u018a\3\2"+
		"\2\2\u018a\u0188\3\2\2\2\u018a\u018b\3\2\2\2\u018b\u018c\3\2\2\2\u018c"+
		"\u018d\7<\2\2\u018dU\3\2\2\2\u018e\u0190\n\r\2\2\u018f\u018e\3\2\2\2\u0190"+
		"\u0191\3\2\2\2\u0191\u018f\3\2\2\2\u0191\u0192\3\2\2\2\u0192W\3\2\2\2"+
		"\u0193\u0195\t\r\2\2\u0194\u0193\3\2\2\2\u0195\u0196\3\2\2\2\u0196\u0194"+
		"\3\2\2\2\u0196\u0197\3\2\2\2\u0197\u0198\3\2\2\2\u0198\u0199\b)\17\2\u0199"+
		"\u019a\b)\20\2\u019aY\3\2\2\2\u019b\u019c\t\16\2\2\u019c\u019d\3\2\2\2"+
		"\u019d\u019e\b*\21\2\u019e[\3\2\2\2\u019f\u01a0\5\20\5\2\u01a0]\3\2\2"+
		"\2\u01a1\u01a2\5\22\6\2\u01a2_\3\2\2\2\u01a3\u01a4\5\24\7\2\u01a4a\3\2"+
		"\2\2\u01a5\u01a7\t\17\2\2\u01a6\u01a5\3\2\2\2\u01a6\u01a7\3\2\2\2\u01a7"+
		"\u01a8\3\2\2\2\u01a8\u01aa\7-\2\2\u01a9\u01ab\t\2\2\2\u01aa\u01a9\3\2"+
		"\2\2\u01ab\u01ac\3\2\2\2\u01ac\u01ad\3\2\2\2\u01ac\u01aa\3\2\2\2\u01ad"+
		"\u01ae\3\2\2\2\u01ae\u01af\t\20\2\2\u01afc\3\2\2\2\u01b0\u01b1\5\16\4"+
		"\2\u01b1\u01b2\3\2\2\2\u01b2\u01b3\b/\17\2\u01b3\u01b4\b/\20\2\u01b4e"+
		"\3\2\2\2\32\2\3\4\5\6\7\b\tiov\u0098\u00a3\u00ac\u00cc\u0128\u012a\u0167"+
		"\u017f\u018a\u0191\u0196\u01a6\u01ac\22\4\3\2\4\4\2\4\5\2\4\6\2\5\2\2"+
		"\4\2\2\4\7\2\4\b\2\t\5\2\t\6\2\t\7\2\7\t\2\t\24\2\t\b\2\b\2\2\6\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}