package com.github.beardlybread.orgestrator.org.antlr;

// Generated from OrgParser.g4 by ANTLR 4.5
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class OrgParser extends Parser {
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
	public static final int
		RULE_file = 0, RULE_thing = 1, RULE_line = 2, RULE_headingLine = 3, RULE_todoLine = 4, 
		RULE_table = 5, RULE_tableRow = 6, RULE_tableCol = 7, RULE_unenumeratedLine = 8, 
		RULE_enumeratedLine = 9, RULE_event = 10, RULE_scheduled = 11, RULE_deadline = 12, 
		RULE_closed = 13, RULE_timestamp = 14, RULE_date = 15, RULE_propertyList = 16, 
		RULE_property = 17, RULE_propertyPair = 18, RULE_lastRepeat = 19, RULE_empty = 20;
	public static final String[] ruleNames = {
		"file", "thing", "line", "headingLine", "todoLine", "table", "tableRow", 
		"tableCol", "unenumeratedLine", "enumeratedLine", "event", "scheduled", 
		"deadline", "closed", "timestamp", "date", "propertyList", "property", 
		"propertyPair", "lastRepeat", "empty"
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

	@Override
	public String getGrammarFileName() { return "OrgParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public OrgParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class FileContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(OrgParser.EOF, 0); }
		public List<ThingContext> thing() {
			return getRuleContexts(ThingContext.class);
		}
		public ThingContext thing(int i) {
			return getRuleContext(ThingContext.class,i);
		}
		public FileContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_file; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).enterFile(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).exitFile(this);
		}
	}

	public final FileContext file() throws RecognitionException {
		FileContext _localctx = new FileContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_file);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(45);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << HEADING_LEVEL) | (1L << INDENT) | (1L << TABLE) | (1L << ULIST) | (1L << ILIST) | (1L << EMPTY) | (1L << LINE))) != 0)) {
				{
				{
				setState(42);
				thing();
				}
				}
				setState(47);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(48);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ThingContext extends ParserRuleContext {
		public EmptyContext empty() {
			return getRuleContext(EmptyContext.class,0);
		}
		public LineContext line() {
			return getRuleContext(LineContext.class,0);
		}
		public HeadingLineContext headingLine() {
			return getRuleContext(HeadingLineContext.class,0);
		}
		public TodoLineContext todoLine() {
			return getRuleContext(TodoLineContext.class,0);
		}
		public TableContext table() {
			return getRuleContext(TableContext.class,0);
		}
		public UnenumeratedLineContext unenumeratedLine() {
			return getRuleContext(UnenumeratedLineContext.class,0);
		}
		public EnumeratedLineContext enumeratedLine() {
			return getRuleContext(EnumeratedLineContext.class,0);
		}
		public EventContext event() {
			return getRuleContext(EventContext.class,0);
		}
		public PropertyListContext propertyList() {
			return getRuleContext(PropertyListContext.class,0);
		}
		public ThingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_thing; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).enterThing(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).exitThing(this);
		}
	}

	public final ThingContext thing() throws RecognitionException {
		ThingContext _localctx = new ThingContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_thing);
		try {
			setState(59);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(50);
				empty();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(51);
				line();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(52);
				headingLine();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(53);
				todoLine();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(54);
				table();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(55);
				unenumeratedLine();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(56);
				enumeratedLine();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(57);
				event();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(58);
				propertyList();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LineContext extends ParserRuleContext {
		public TerminalNode LINE() { return getToken(OrgParser.LINE, 0); }
		public TerminalNode INDENT() { return getToken(OrgParser.INDENT, 0); }
		public TerminalNode EMPTY() { return getToken(OrgParser.EMPTY, 0); }
		public LineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_line; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).enterLine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).exitLine(this);
		}
	}

	public final LineContext line() throws RecognitionException {
		LineContext _localctx = new LineContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_line);
		int _la;
		try {
			setState(66);
			switch (_input.LA(1)) {
			case INDENT:
			case LINE:
				enterOuterAlt(_localctx, 1);
				{
				setState(62);
				_la = _input.LA(1);
				if (_la==INDENT) {
					{
					setState(61);
					match(INDENT);
					}
				}

				setState(64);
				match(LINE);
				}
				break;
			case EMPTY:
				enterOuterAlt(_localctx, 2);
				{
				setState(65);
				match(EMPTY);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HeadingLineContext extends ParserRuleContext {
		public TerminalNode HEADING_LEVEL() { return getToken(OrgParser.HEADING_LEVEL, 0); }
		public TerminalNode HEADING() { return getToken(OrgParser.HEADING, 0); }
		public HeadingLineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_headingLine; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).enterHeadingLine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).exitHeadingLine(this);
		}
	}

	public final HeadingLineContext headingLine() throws RecognitionException {
		HeadingLineContext _localctx = new HeadingLineContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_headingLine);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(68);
			match(HEADING_LEVEL);
			setState(69);
			match(HEADING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TodoLineContext extends ParserRuleContext {
		public TerminalNode HEADING_LEVEL() { return getToken(OrgParser.HEADING_LEVEL, 0); }
		public TerminalNode TODO() { return getToken(OrgParser.TODO, 0); }
		public TerminalNode HEADING() { return getToken(OrgParser.HEADING, 0); }
		public TodoLineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_todoLine; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).enterTodoLine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).exitTodoLine(this);
		}
	}

	public final TodoLineContext todoLine() throws RecognitionException {
		TodoLineContext _localctx = new TodoLineContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_todoLine);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(71);
			match(HEADING_LEVEL);
			setState(72);
			match(TODO);
			setState(73);
			match(HEADING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TableContext extends ParserRuleContext {
		public List<TableRowContext> tableRow() {
			return getRuleContexts(TableRowContext.class);
		}
		public TableRowContext tableRow(int i) {
			return getRuleContext(TableRowContext.class,i);
		}
		public TableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_table; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).enterTable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).exitTable(this);
		}
	}

	public final TableContext table() throws RecognitionException {
		TableContext _localctx = new TableContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_table);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(76); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(75);
					tableRow();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(78); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TableRowContext extends ParserRuleContext {
		public TerminalNode TABLE() { return getToken(OrgParser.TABLE, 0); }
		public TerminalNode END_TABLE() { return getToken(OrgParser.END_TABLE, 0); }
		public TerminalNode INDENT() { return getToken(OrgParser.INDENT, 0); }
		public List<TableColContext> tableCol() {
			return getRuleContexts(TableColContext.class);
		}
		public TableColContext tableCol(int i) {
			return getRuleContext(TableColContext.class,i);
		}
		public TableRowContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableRow; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).enterTableRow(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).exitTableRow(this);
		}
	}

	public final TableRowContext tableRow() throws RecognitionException {
		TableRowContext _localctx = new TableRowContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_tableRow);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81);
			_la = _input.LA(1);
			if (_la==INDENT) {
				{
				setState(80);
				match(INDENT);
				}
			}

			setState(83);
			match(TABLE);
			setState(85); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(84);
				tableCol();
				}
				}
				setState(87); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==TABLE_COL || _la==TABLE_SEP );
			setState(89);
			match(END_TABLE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TableColContext extends ParserRuleContext {
		public TerminalNode TABLE_SEP() { return getToken(OrgParser.TABLE_SEP, 0); }
		public TerminalNode TABLE_COL() { return getToken(OrgParser.TABLE_COL, 0); }
		public TableColContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableCol; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).enterTableCol(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).exitTableCol(this);
		}
	}

	public final TableColContext tableCol() throws RecognitionException {
		TableColContext _localctx = new TableColContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_tableCol);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92);
			_la = _input.LA(1);
			if (_la==TABLE_COL) {
				{
				setState(91);
				match(TABLE_COL);
				}
			}

			setState(94);
			match(TABLE_SEP);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnenumeratedLineContext extends ParserRuleContext {
		public TerminalNode ULIST() { return getToken(OrgParser.ULIST, 0); }
		public TerminalNode LINE() { return getToken(OrgParser.LINE, 0); }
		public TerminalNode INDENT() { return getToken(OrgParser.INDENT, 0); }
		public UnenumeratedLineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unenumeratedLine; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).enterUnenumeratedLine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).exitUnenumeratedLine(this);
		}
	}

	public final UnenumeratedLineContext unenumeratedLine() throws RecognitionException {
		UnenumeratedLineContext _localctx = new UnenumeratedLineContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_unenumeratedLine);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(97);
			_la = _input.LA(1);
			if (_la==INDENT) {
				{
				setState(96);
				match(INDENT);
				}
			}

			setState(99);
			match(ULIST);
			setState(100);
			match(LINE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumeratedLineContext extends ParserRuleContext {
		public TerminalNode ILIST() { return getToken(OrgParser.ILIST, 0); }
		public TerminalNode LINE() { return getToken(OrgParser.LINE, 0); }
		public TerminalNode INDENT() { return getToken(OrgParser.INDENT, 0); }
		public EnumeratedLineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumeratedLine; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).enterEnumeratedLine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).exitEnumeratedLine(this);
		}
	}

	public final EnumeratedLineContext enumeratedLine() throws RecognitionException {
		EnumeratedLineContext _localctx = new EnumeratedLineContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_enumeratedLine);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103);
			_la = _input.LA(1);
			if (_la==INDENT) {
				{
				setState(102);
				match(INDENT);
				}
			}

			setState(105);
			match(ILIST);
			setState(106);
			match(LINE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EventContext extends ParserRuleContext {
		public ScheduledContext scheduled() {
			return getRuleContext(ScheduledContext.class,0);
		}
		public DeadlineContext deadline() {
			return getRuleContext(DeadlineContext.class,0);
		}
		public ClosedContext closed() {
			return getRuleContext(ClosedContext.class,0);
		}
		public EventContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_event; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).enterEvent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).exitEvent(this);
		}
	}

	public final EventContext event() throws RecognitionException {
		EventContext _localctx = new EventContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_event);
		try {
			setState(111);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(108);
				scheduled();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(109);
				deadline();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(110);
				closed();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ScheduledContext extends ParserRuleContext {
		public TerminalNode INDENT() { return getToken(OrgParser.INDENT, 0); }
		public TerminalNode SCHEDULED() { return getToken(OrgParser.SCHEDULED, 0); }
		public TimestampContext timestamp() {
			return getRuleContext(TimestampContext.class,0);
		}
		public TerminalNode END_SCHEDULE() { return getToken(OrgParser.END_SCHEDULE, 0); }
		public ScheduledContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_scheduled; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).enterScheduled(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).exitScheduled(this);
		}
	}

	public final ScheduledContext scheduled() throws RecognitionException {
		ScheduledContext _localctx = new ScheduledContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_scheduled);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(113);
			match(INDENT);
			setState(114);
			match(SCHEDULED);
			setState(115);
			timestamp();
			setState(116);
			match(END_SCHEDULE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeadlineContext extends ParserRuleContext {
		public TerminalNode INDENT() { return getToken(OrgParser.INDENT, 0); }
		public TerminalNode DEADLINE() { return getToken(OrgParser.DEADLINE, 0); }
		public TimestampContext timestamp() {
			return getRuleContext(TimestampContext.class,0);
		}
		public TerminalNode END_SCHEDULE() { return getToken(OrgParser.END_SCHEDULE, 0); }
		public DeadlineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_deadline; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).enterDeadline(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).exitDeadline(this);
		}
	}

	public final DeadlineContext deadline() throws RecognitionException {
		DeadlineContext _localctx = new DeadlineContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_deadline);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(118);
			match(INDENT);
			setState(119);
			match(DEADLINE);
			setState(120);
			timestamp();
			setState(121);
			match(END_SCHEDULE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClosedContext extends ParserRuleContext {
		public TerminalNode INDENT() { return getToken(OrgParser.INDENT, 0); }
		public TerminalNode CLOSED() { return getToken(OrgParser.CLOSED, 0); }
		public List<TimestampContext> timestamp() {
			return getRuleContexts(TimestampContext.class);
		}
		public TimestampContext timestamp(int i) {
			return getRuleContext(TimestampContext.class,i);
		}
		public TerminalNode END_SCHEDULE() { return getToken(OrgParser.END_SCHEDULE, 0); }
		public TerminalNode CLOSED_DEADLINE() { return getToken(OrgParser.CLOSED_DEADLINE, 0); }
		public TerminalNode CLOSED_SCHEDULED() { return getToken(OrgParser.CLOSED_SCHEDULED, 0); }
		public ClosedContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_closed; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).enterClosed(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).exitClosed(this);
		}
	}

	public final ClosedContext closed() throws RecognitionException {
		ClosedContext _localctx = new ClosedContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_closed);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(123);
			match(INDENT);
			setState(124);
			match(CLOSED);
			setState(125);
			timestamp();
			setState(128);
			_la = _input.LA(1);
			if (_la==CLOSED_DEADLINE || _la==CLOSED_SCHEDULED) {
				{
				setState(126);
				_la = _input.LA(1);
				if ( !(_la==CLOSED_DEADLINE || _la==CLOSED_SCHEDULED) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(127);
				timestamp();
				}
			}

			setState(130);
			match(END_SCHEDULE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TimestampContext extends ParserRuleContext {
		public TerminalNode TIMESTAMP() { return getToken(OrgParser.TIMESTAMP, 0); }
		public DateContext date() {
			return getRuleContext(DateContext.class,0);
		}
		public TerminalNode END_TIMESTAMP() { return getToken(OrgParser.END_TIMESTAMP, 0); }
		public TimestampContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_timestamp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).enterTimestamp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).exitTimestamp(this);
		}
	}

	public final TimestampContext timestamp() throws RecognitionException {
		TimestampContext _localctx = new TimestampContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_timestamp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(132);
			match(TIMESTAMP);
			setState(133);
			date();
			setState(134);
			match(END_TIMESTAMP);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DateContext extends ParserRuleContext {
		public TerminalNode DATE() { return getToken(OrgParser.DATE, 0); }
		public TerminalNode DOW() { return getToken(OrgParser.DOW, 0); }
		public TerminalNode TIME() { return getToken(OrgParser.TIME, 0); }
		public TerminalNode REPEAT() { return getToken(OrgParser.REPEAT, 0); }
		public DateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_date; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).enterDate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).exitDate(this);
		}
	}

	public final DateContext date() throws RecognitionException {
		DateContext _localctx = new DateContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_date);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(136);
			match(DATE);
			setState(137);
			match(DOW);
			setState(139);
			_la = _input.LA(1);
			if (_la==TIME) {
				{
				setState(138);
				match(TIME);
				}
			}

			setState(142);
			_la = _input.LA(1);
			if (_la==REPEAT) {
				{
				setState(141);
				match(REPEAT);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropertyListContext extends ParserRuleContext {
		public TerminalNode INDENT() { return getToken(OrgParser.INDENT, 0); }
		public TerminalNode PROPERTIES() { return getToken(OrgParser.PROPERTIES, 0); }
		public TerminalNode END_PROPERTIES() { return getToken(OrgParser.END_PROPERTIES, 0); }
		public List<PropertyContext> property() {
			return getRuleContexts(PropertyContext.class);
		}
		public PropertyContext property(int i) {
			return getRuleContext(PropertyContext.class,i);
		}
		public PropertyListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).enterPropertyList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).exitPropertyList(this);
		}
	}

	public final PropertyListContext propertyList() throws RecognitionException {
		PropertyListContext _localctx = new PropertyListContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_propertyList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144);
			match(INDENT);
			setState(145);
			match(PROPERTIES);
			setState(149);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LAST_REPEAT || _la==PROPERTY) {
				{
				{
				setState(146);
				property();
				}
				}
				setState(151);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(152);
			match(END_PROPERTIES);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropertyContext extends ParserRuleContext {
		public PropertyPairContext propertyPair() {
			return getRuleContext(PropertyPairContext.class,0);
		}
		public LastRepeatContext lastRepeat() {
			return getRuleContext(LastRepeatContext.class,0);
		}
		public PropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_property; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).enterProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).exitProperty(this);
		}
	}

	public final PropertyContext property() throws RecognitionException {
		PropertyContext _localctx = new PropertyContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_property);
		try {
			setState(156);
			switch (_input.LA(1)) {
			case PROPERTY:
				enterOuterAlt(_localctx, 1);
				{
				setState(154);
				propertyPair();
				}
				break;
			case LAST_REPEAT:
				enterOuterAlt(_localctx, 2);
				{
				setState(155);
				lastRepeat();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropertyPairContext extends ParserRuleContext {
		public TerminalNode PROPERTY() { return getToken(OrgParser.PROPERTY, 0); }
		public TerminalNode VALUE() { return getToken(OrgParser.VALUE, 0); }
		public PropertyPairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyPair; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).enterPropertyPair(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).exitPropertyPair(this);
		}
	}

	public final PropertyPairContext propertyPair() throws RecognitionException {
		PropertyPairContext _localctx = new PropertyPairContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_propertyPair);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(158);
			match(PROPERTY);
			setState(160);
			_la = _input.LA(1);
			if (_la==VALUE) {
				{
				setState(159);
				match(VALUE);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LastRepeatContext extends ParserRuleContext {
		public TerminalNode LAST_REPEAT() { return getToken(OrgParser.LAST_REPEAT, 0); }
		public DateContext date() {
			return getRuleContext(DateContext.class,0);
		}
		public TerminalNode END_TIMESTAMP() { return getToken(OrgParser.END_TIMESTAMP, 0); }
		public LastRepeatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lastRepeat; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).enterLastRepeat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).exitLastRepeat(this);
		}
	}

	public final LastRepeatContext lastRepeat() throws RecognitionException {
		LastRepeatContext _localctx = new LastRepeatContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_lastRepeat);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
			match(LAST_REPEAT);
			setState(163);
			date();
			setState(164);
			match(END_TIMESTAMP);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EmptyContext extends ParserRuleContext {
		public TerminalNode EMPTY() { return getToken(OrgParser.EMPTY, 0); }
		public EmptyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_empty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).enterEmpty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OrgParserListener ) ((OrgParserListener)listener).exitEmpty(this);
		}
	}

	public final EmptyContext empty() throws RecognitionException {
		EmptyContext _localctx = new EmptyContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_empty);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(166);
			match(EMPTY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\37\u00ab\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\3\2\7\2.\n\2\f\2\16\2\61\13\2"+
		"\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3>\n\3\3\4\5\4A\n\4\3\4"+
		"\3\4\5\4E\n\4\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\7\6\7O\n\7\r\7\16\7P\3\b\5"+
		"\bT\n\b\3\b\3\b\6\bX\n\b\r\b\16\bY\3\b\3\b\3\t\5\t_\n\t\3\t\3\t\3\n\5"+
		"\nd\n\n\3\n\3\n\3\n\3\13\5\13j\n\13\3\13\3\13\3\13\3\f\3\f\3\f\5\fr\n"+
		"\f\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3"+
		"\17\5\17\u0083\n\17\3\17\3\17\3\20\3\20\3\20\3\20\3\21\3\21\3\21\5\21"+
		"\u008e\n\21\3\21\5\21\u0091\n\21\3\22\3\22\3\22\7\22\u0096\n\22\f\22\16"+
		"\22\u0099\13\22\3\22\3\22\3\23\3\23\5\23\u009f\n\23\3\24\3\24\5\24\u00a3"+
		"\n\24\3\25\3\25\3\25\3\25\3\26\3\26\3\26\2\2\27\2\4\6\b\n\f\16\20\22\24"+
		"\26\30\32\34\36 \"$&(*\2\3\3\2\25\26\u00ae\2/\3\2\2\2\4=\3\2\2\2\6D\3"+
		"\2\2\2\bF\3\2\2\2\nI\3\2\2\2\fN\3\2\2\2\16S\3\2\2\2\20^\3\2\2\2\22c\3"+
		"\2\2\2\24i\3\2\2\2\26q\3\2\2\2\30s\3\2\2\2\32x\3\2\2\2\34}\3\2\2\2\36"+
		"\u0086\3\2\2\2 \u008a\3\2\2\2\"\u0092\3\2\2\2$\u009e\3\2\2\2&\u00a0\3"+
		"\2\2\2(\u00a4\3\2\2\2*\u00a8\3\2\2\2,.\5\4\3\2-,\3\2\2\2.\61\3\2\2\2/"+
		"-\3\2\2\2/\60\3\2\2\2\60\62\3\2\2\2\61/\3\2\2\2\62\63\7\2\2\3\63\3\3\2"+
		"\2\2\64>\5*\26\2\65>\5\6\4\2\66>\5\b\5\2\67>\5\n\6\28>\5\f\7\29>\5\22"+
		"\n\2:>\5\24\13\2;>\5\26\f\2<>\5\"\22\2=\64\3\2\2\2=\65\3\2\2\2=\66\3\2"+
		"\2\2=\67\3\2\2\2=8\3\2\2\2=9\3\2\2\2=:\3\2\2\2=;\3\2\2\2=<\3\2\2\2>\5"+
		"\3\2\2\2?A\7\4\2\2@?\3\2\2\2@A\3\2\2\2AB\3\2\2\2BE\7\22\2\2CE\7\b\2\2"+
		"D@\3\2\2\2DC\3\2\2\2E\7\3\2\2\2FG\7\3\2\2GH\7\n\2\2H\t\3\2\2\2IJ\7\3\2"+
		"\2JK\7\t\2\2KL\7\n\2\2L\13\3\2\2\2MO\5\16\b\2NM\3\2\2\2OP\3\2\2\2PN\3"+
		"\2\2\2PQ\3\2\2\2Q\r\3\2\2\2RT\7\4\2\2SR\3\2\2\2ST\3\2\2\2TU\3\2\2\2UW"+
		"\7\5\2\2VX\5\20\t\2WV\3\2\2\2XY\3\2\2\2YW\3\2\2\2YZ\3\2\2\2Z[\3\2\2\2"+
		"[\\\7\17\2\2\\\17\3\2\2\2]_\7\20\2\2^]\3\2\2\2^_\3\2\2\2_`\3\2\2\2`a\7"+
		"\21\2\2a\21\3\2\2\2bd\7\4\2\2cb\3\2\2\2cd\3\2\2\2de\3\2\2\2ef\7\6\2\2"+
		"fg\7\22\2\2g\23\3\2\2\2hj\7\4\2\2ih\3\2\2\2ij\3\2\2\2jk\3\2\2\2kl\7\7"+
		"\2\2lm\7\22\2\2m\25\3\2\2\2nr\5\30\r\2or\5\32\16\2pr\5\34\17\2qn\3\2\2"+
		"\2qo\3\2\2\2qp\3\2\2\2r\27\3\2\2\2st\7\4\2\2tu\7\13\2\2uv\5\36\20\2vw"+
		"\7\23\2\2w\31\3\2\2\2xy\7\4\2\2yz\7\f\2\2z{\5\36\20\2{|\7\23\2\2|\33\3"+
		"\2\2\2}~\7\4\2\2~\177\7\r\2\2\177\u0082\5\36\20\2\u0080\u0081\t\2\2\2"+
		"\u0081\u0083\5\36\20\2\u0082\u0080\3\2\2\2\u0082\u0083\3\2\2\2\u0083\u0084"+
		"\3\2\2\2\u0084\u0085\7\23\2\2\u0085\35\3\2\2\2\u0086\u0087\7\24\2\2\u0087"+
		"\u0088\5 \21\2\u0088\u0089\7\33\2\2\u0089\37\3\2\2\2\u008a\u008b\7\34"+
		"\2\2\u008b\u008d\7\35\2\2\u008c\u008e\7\36\2\2\u008d\u008c\3\2\2\2\u008d"+
		"\u008e\3\2\2\2\u008e\u0090\3\2\2\2\u008f\u0091\7\37\2\2\u0090\u008f\3"+
		"\2\2\2\u0090\u0091\3\2\2\2\u0091!\3\2\2\2\u0092\u0093\7\4\2\2\u0093\u0097"+
		"\7\16\2\2\u0094\u0096\5$\23\2\u0095\u0094\3\2\2\2\u0096\u0099\3\2\2\2"+
		"\u0097\u0095\3\2\2\2\u0097\u0098\3\2\2\2\u0098\u009a\3\2\2\2\u0099\u0097"+
		"\3\2\2\2\u009a\u009b\7\27\2\2\u009b#\3\2\2\2\u009c\u009f\5&\24\2\u009d"+
		"\u009f\5(\25\2\u009e\u009c\3\2\2\2\u009e\u009d\3\2\2\2\u009f%\3\2\2\2"+
		"\u00a0\u00a2\7\31\2\2\u00a1\u00a3\7\32\2\2\u00a2\u00a1\3\2\2\2\u00a2\u00a3"+
		"\3\2\2\2\u00a3\'\3\2\2\2\u00a4\u00a5\7\30\2\2\u00a5\u00a6\5 \21\2\u00a6"+
		"\u00a7\7\33\2\2\u00a7)\3\2\2\2\u00a8\u00a9\7\b\2\2\u00a9+\3\2\2\2\23/"+
		"=@DPSY^ciq\u0082\u008d\u0090\u0097\u009e\u00a2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}