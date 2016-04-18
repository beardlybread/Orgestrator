package com.github.beardlybread.orgestrator.org;

import com.github.beardlybread.orgestrator.org.antlr.*;

import java.util.ArrayList;
import java.util.Stack;

// TODO Set up lists so that they group together lines.

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

    /** Instantiate the data structures.
     * @param ctx the parser context (unused)
     */
    @Override
    public void enterFile(OrgParser.FileContext ctx) {
        this.roots = new ArrayList<>();
        this.lastParent = new Stack<>();
    }

    /** Handle lines that do not fall into a more specific type.
     * @param ctx the parser context
     */
    @Override
    public void enterLine(OrgParser.LineContext ctx) {
        this.current = new OrgText(ctx.LINE().getText(),
                ctx.INDENT() == null ? 0 : ctx.INDENT().getText().length());
        // if lastParent.empty
        //   if last is OrgText, add to last
        //   else add current to roots, last = current
        // else if lastParent is heading
        //   if last is OrgText, add to last
        //   else add current to heading, last = current
        // else (lastParent is list) add current to list text
    }

    /** Handle headings without 'to-do' semantics.
     * @param ctx the parser context
     */
    @Override
    public void enterHeadingLine(OrgParser.HeadingLineContext ctx) {
        this.current = new OrgHeading(ctx.HEADING_LEVEL().getText(), ctx.HEADING().getText());
        // if lastParent is a list
        //   pop until heading
        //   if lastParent is equal level, pop and parent (or roots)
        //   else parent
        // push current as parent, last = current
    }

    /** Handle 'to-do' headings.
     * @param ctx the parser context
     */
    @Override
    public void enterTodoLine(OrgParser.TodoLineContext ctx) {
        this.current = new OrgToDo(ctx.HEADING_LEVEL().getText(), ctx.HEADING().getText(),
                ctx.TODO().getText());
        // if lastParent is a list
        //   pop until heading
        //   if lastParent is equal level, pop and parent (or roots)
        //   else parent
        // push current as parent
    }

    /** Construct table from parser context.
     * `OrgTable` is constructed from the top level `table` rule. The table objects are fixed size
     * for now, so constructing them from this level gives access to all the necessary size
     * information at once. Also, this eliminates the need for more specific listeners for the rows
     * and columns.
     * @param ctx the parser context
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
        this.current.setParent(this.lastParent.peek());
        this.last = this.current;
    }

    /** Handle un-enumerated list line generation.
     * @param ctx the parser context
     */
    @Override
    public void enterUnenumeratedLine(OrgParser.UnenumeratedLineContext ctx) {
        this.current = new OrgList(ctx.INDENT() == null ? 0 : ctx.INDENT().getText().length(),
                ctx.ULIST().getText(), ctx.LINE().getText());
        // if lastParent is a list
        //   if indent level is the same pop until lower to parent
        //   else use lastParent as parent
        // else (heading) use lastParent as parent
    }

    /** Handle enumerated list line generation.
     * @param ctx the parser context
     */
    @Override
    public void enterEnumeratedLine(OrgParser.EnumeratedLineContext ctx) {
        this.current = new OrgList(ctx.INDENT() == null ? 0 : ctx.INDENT().getText().length(),
                ctx.ILIST().getText(), ctx.LINE().getText());
        // if lastParent is a list
        //   if indent level is the same pop until lower to parent
        //   else use lastParent as parent
        // else (heading) use lastParent as parent
    }

    /** Handle events parenting to closest heading.
     * @param ctx the parser context
     */
    @Override
    public void enterEvent(OrgParser.EventContext ctx) {
        if (ctx.scheduled() != null) {

        } else if (ctx.deadline() != null) {

        } else { // closed
        }
    }

    /** Handle properties parenting to closest heading.
     * @param ctx the parser context
     */
    @Override
    public void enterPropertyList(OrgParser.PropertyListContext ctx) { }

// I don't think I'll need these, but just in case...
//    /**
//     * @param ctx the parser context
//     */
//    @Override
//    public void enterEveryRule(ParserRuleContext ctx) { }
//
//    /**
//     * @param ctx the parser context
//     */
//    @Override
//    public void exitEveryRule(ParserRuleContext ctx) { }
//
//    /**
//     * @param node a lexer terminal
//     */
//    @Override
//    public void visitTerminal(TerminalNode node) { }
//
//    /** Handle errors.
//     * @param node the structure containing error information
//     */
//    @Override
//    public void visitErrorNode(ErrorNode node) { }

}
