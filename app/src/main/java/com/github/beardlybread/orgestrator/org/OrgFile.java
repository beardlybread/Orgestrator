package com.github.beardlybread.orgestrator.org;

import com.github.beardlybread.orgestrator.org.antlr.*;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;

public class OrgFile extends OrgParserBaseListener {

    protected ArrayList<OrgNode> roots = null;

    // Listener as input
    // on all exits, record last processed thing/indent level (stack) list and heading stack?
    // anything after headings > heading.level into heading on top
    // lists/tables after lists go into list if indent >= top
    // text gloms onto top text except for headings, ignoring indent
    // properties and timestamps attach to previous heading

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
 public void enterFile(OrgParser.FileContext ctx) { }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
 public void exitThing(OrgParser.ThingContext ctx) { }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
 public void enterLine(OrgParser.LineContext ctx) { }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
 public void enterHeadingLine(OrgParser.HeadingLineContext ctx) { }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
 public void enterTodoLine(OrgParser.TodoLineContext ctx) { }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
 public void enterTable(OrgParser.TableContext ctx) { }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
 public void enterTableRow(OrgParser.TableRowContext ctx) { }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
 public void enterTableCol(OrgParser.TableColContext ctx) { }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
 public void enterUnenumeratedLine(OrgParser.UnenumeratedLineContext ctx) { }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
 public void enterEnumeratedLine(OrgParser.EnumeratedLineContext ctx) { }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
 public void enterEvent(OrgParser.EventContext ctx) { }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
 public void enterScheduled(OrgParser.ScheduledContext ctx) { }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
 public void enterDeadline(OrgParser.DeadlineContext ctx) { }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
 public void enterClosed(OrgParser.ClosedContext ctx) { }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
 public void enterTimestamp(OrgParser.TimestampContext ctx) { }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
 public void enterPropertyList(OrgParser.PropertyListContext ctx) { }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
 public void enterProperty(OrgParser.PropertyContext ctx) { }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
 public void enterPropertyPair(OrgParser.PropertyPairContext ctx) { }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
 public void enterLastRepeat(OrgParser.LastRepeatContext ctx) { }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
 public void enterEveryRule(ParserRuleContext ctx) { }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
 public void exitEveryRule(ParserRuleContext ctx) { }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
 public void visitTerminal(TerminalNode node) { }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
 public void visitErrorNode(ErrorNode node) { }

}
