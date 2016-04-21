package com.github.beardlybread.orgestrator.org.antlr;

// Generated from OrgParser.g4 by ANTLR 4.5
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link OrgParser}.
 */
public interface OrgParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link OrgParser#file}.
	 * @param ctx the parse tree
	 */
	void enterFile(OrgParser.FileContext ctx);
	/**
	 * Exit a parse tree produced by {@link OrgParser#file}.
	 * @param ctx the parse tree
	 */
	void exitFile(OrgParser.FileContext ctx);
	/**
	 * Enter a parse tree produced by {@link OrgParser#thing}.
	 * @param ctx the parse tree
	 */
	void enterThing(OrgParser.ThingContext ctx);
	/**
	 * Exit a parse tree produced by {@link OrgParser#thing}.
	 * @param ctx the parse tree
	 */
	void exitThing(OrgParser.ThingContext ctx);
	/**
	 * Enter a parse tree produced by {@link OrgParser#line}.
	 * @param ctx the parse tree
	 */
	void enterLine(OrgParser.LineContext ctx);
	/**
	 * Exit a parse tree produced by {@link OrgParser#line}.
	 * @param ctx the parse tree
	 */
	void exitLine(OrgParser.LineContext ctx);
	/**
	 * Enter a parse tree produced by {@link OrgParser#headingLine}.
	 * @param ctx the parse tree
	 */
	void enterHeadingLine(OrgParser.HeadingLineContext ctx);
	/**
	 * Exit a parse tree produced by {@link OrgParser#headingLine}.
	 * @param ctx the parse tree
	 */
	void exitHeadingLine(OrgParser.HeadingLineContext ctx);
	/**
	 * Enter a parse tree produced by {@link OrgParser#todoLine}.
	 * @param ctx the parse tree
	 */
	void enterTodoLine(OrgParser.TodoLineContext ctx);
	/**
	 * Exit a parse tree produced by {@link OrgParser#todoLine}.
	 * @param ctx the parse tree
	 */
	void exitTodoLine(OrgParser.TodoLineContext ctx);
	/**
	 * Enter a parse tree produced by {@link OrgParser#table}.
	 * @param ctx the parse tree
	 */
	void enterTable(OrgParser.TableContext ctx);
	/**
	 * Exit a parse tree produced by {@link OrgParser#table}.
	 * @param ctx the parse tree
	 */
	void exitTable(OrgParser.TableContext ctx);
	/**
	 * Enter a parse tree produced by {@link OrgParser#tableRow}.
	 * @param ctx the parse tree
	 */
	void enterTableRow(OrgParser.TableRowContext ctx);
	/**
	 * Exit a parse tree produced by {@link OrgParser#tableRow}.
	 * @param ctx the parse tree
	 */
	void exitTableRow(OrgParser.TableRowContext ctx);
	/**
	 * Enter a parse tree produced by {@link OrgParser#tableCol}.
	 * @param ctx the parse tree
	 */
	void enterTableCol(OrgParser.TableColContext ctx);
	/**
	 * Exit a parse tree produced by {@link OrgParser#tableCol}.
	 * @param ctx the parse tree
	 */
	void exitTableCol(OrgParser.TableColContext ctx);
	/**
	 * Enter a parse tree produced by {@link OrgParser#unenumeratedLine}.
	 * @param ctx the parse tree
	 */
	void enterUnenumeratedLine(OrgParser.UnenumeratedLineContext ctx);
	/**
	 * Exit a parse tree produced by {@link OrgParser#unenumeratedLine}.
	 * @param ctx the parse tree
	 */
	void exitUnenumeratedLine(OrgParser.UnenumeratedLineContext ctx);
	/**
	 * Enter a parse tree produced by {@link OrgParser#enumeratedLine}.
	 * @param ctx the parse tree
	 */
	void enterEnumeratedLine(OrgParser.EnumeratedLineContext ctx);
	/**
	 * Exit a parse tree produced by {@link OrgParser#enumeratedLine}.
	 * @param ctx the parse tree
	 */
	void exitEnumeratedLine(OrgParser.EnumeratedLineContext ctx);
	/**
	 * Enter a parse tree produced by {@link OrgParser#event}.
	 * @param ctx the parse tree
	 */
	void enterEvent(OrgParser.EventContext ctx);
	/**
	 * Exit a parse tree produced by {@link OrgParser#event}.
	 * @param ctx the parse tree
	 */
	void exitEvent(OrgParser.EventContext ctx);
	/**
	 * Enter a parse tree produced by {@link OrgParser#scheduled}.
	 * @param ctx the parse tree
	 */
	void enterScheduled(OrgParser.ScheduledContext ctx);
	/**
	 * Exit a parse tree produced by {@link OrgParser#scheduled}.
	 * @param ctx the parse tree
	 */
	void exitScheduled(OrgParser.ScheduledContext ctx);
	/**
	 * Enter a parse tree produced by {@link OrgParser#deadline}.
	 * @param ctx the parse tree
	 */
	void enterDeadline(OrgParser.DeadlineContext ctx);
	/**
	 * Exit a parse tree produced by {@link OrgParser#deadline}.
	 * @param ctx the parse tree
	 */
	void exitDeadline(OrgParser.DeadlineContext ctx);
	/**
	 * Enter a parse tree produced by {@link OrgParser#closed}.
	 * @param ctx the parse tree
	 */
	void enterClosed(OrgParser.ClosedContext ctx);
	/**
	 * Exit a parse tree produced by {@link OrgParser#closed}.
	 * @param ctx the parse tree
	 */
	void exitClosed(OrgParser.ClosedContext ctx);
	/**
	 * Enter a parse tree produced by {@link OrgParser#timestamp}.
	 * @param ctx the parse tree
	 */
	void enterTimestamp(OrgParser.TimestampContext ctx);
	/**
	 * Exit a parse tree produced by {@link OrgParser#timestamp}.
	 * @param ctx the parse tree
	 */
	void exitTimestamp(OrgParser.TimestampContext ctx);
	/**
	 * Enter a parse tree produced by {@link OrgParser#propertyList}.
	 * @param ctx the parse tree
	 */
	void enterPropertyList(OrgParser.PropertyListContext ctx);
	/**
	 * Exit a parse tree produced by {@link OrgParser#propertyList}.
	 * @param ctx the parse tree
	 */
	void exitPropertyList(OrgParser.PropertyListContext ctx);
	/**
	 * Enter a parse tree produced by {@link OrgParser#property}.
	 * @param ctx the parse tree
	 */
	void enterProperty(OrgParser.PropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link OrgParser#property}.
	 * @param ctx the parse tree
	 */
	void exitProperty(OrgParser.PropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link OrgParser#propertyPair}.
	 * @param ctx the parse tree
	 */
	void enterPropertyPair(OrgParser.PropertyPairContext ctx);
	/**
	 * Exit a parse tree produced by {@link OrgParser#propertyPair}.
	 * @param ctx the parse tree
	 */
	void exitPropertyPair(OrgParser.PropertyPairContext ctx);
	/**
	 * Enter a parse tree produced by {@link OrgParser#lastRepeat}.
	 * @param ctx the parse tree
	 */
	void enterLastRepeat(OrgParser.LastRepeatContext ctx);
	/**
	 * Exit a parse tree produced by {@link OrgParser#lastRepeat}.
	 * @param ctx the parse tree
	 */
	void exitLastRepeat(OrgParser.LastRepeatContext ctx);
	/**
	 * Enter a parse tree produced by {@link OrgParser#empty}.
	 * @param ctx the parse tree
	 */
	void enterEmpty(OrgParser.EmptyContext ctx);
	/**
	 * Exit a parse tree produced by {@link OrgParser#empty}.
	 * @param ctx the parse tree
	 */
	void exitEmpty(OrgParser.EmptyContext ctx);
}