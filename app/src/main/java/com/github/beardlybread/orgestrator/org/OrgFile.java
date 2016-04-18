package com.github.beardlybread.orgestrator.org;

import com.github.beardlybread.orgestrator.org.antlr.*;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.Stack;

public class OrgFile extends OrgParserBaseListener {

    protected ArrayList<OrgNode> roots = null;
    protected Stack<BaseTreeNode> lastParent = null;
    protected BaseOrgNode last = null;
    protected BaseOrgNode current = null;

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
    public void enterFile(OrgParser.FileContext ctx) {
        this.roots = new ArrayList<>();
        this.lastParent = new Stack<>();
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterLine(OrgParser.LineContext ctx) {
        this.current = new OrgText(ctx.LINE().getText(),
                ctx.INDENT() == null ? 0 : ctx.INDENT().getText().length());
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterHeadingLine(OrgParser.HeadingLineContext ctx) {
        this.current = new OrgHeading(ctx.HEADING_LEVEL().getText(), ctx.HEADING().getText());
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterTodoLine(OrgParser.TodoLineContext ctx) {
        this.current = new OrgToDo(ctx.HEADING_LEVEL().getText(), ctx.HEADING().getText(),
                ctx.TODO().getText());
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterTable(OrgParser.TableContext ctx) {
        OrgParser.TableRowContext top = ctx.tableRow(0);
        this.current = new OrgTable(ctx.tableRow().size(), top.tableCol().size(),
                top.INDENT() == null ? 0 : top.INDENT().getText().length());
        int r = 0;
        for (OrgParser.TableRowContext rc: ctx.tableRow()) {
            int c = 0;
            for (OrgParser.TableColContext cc: rc.tableCol()) {
                ((OrgTable)this.current).set(r, c, cc.TABLE_COL().getText());
                ++c;
            }
            ++r;
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterUnenumeratedLine(OrgParser.UnenumeratedLineContext ctx) {
        this.current = new OrgList(ctx.INDENT() == null ? 0 : ctx.INDENT().getText().length(),
                ctx.ULIST().getText(), ctx.LINE().getText());
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void enterEnumeratedLine(OrgParser.EnumeratedLineContext ctx) {
        this.current = new OrgList(ctx.INDENT() == null ? 0 : ctx.INDENT().getText().length(),
                ctx.ILIST().getText(), ctx.LINE().getText());
    }

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
