package com.github.beardlybread.orgestrator.org;

import com.github.beardlybread.orgestrator.org.antlr.*;

import java.util.ArrayList;
import java.util.Stack;

public class OrgFile extends OrgParserBaseListener {

    protected ArrayList<OrgNode> roots = null;
    protected Stack<OrgTreeNode> lastParent = null;
    protected OrgNode last = null;

    /** Instantiate the data structures.
     * @param ctx the parser context (unused)
     */
    @Override
    public void enterFile (OrgParser.FileContext ctx) {
        this.roots = new ArrayList<>();
        this.lastParent = new Stack<>();
    }

    /** Handle lines that do not fall into a more specific type.
     * @param ctx the parser context
     */
    @Override
    public void enterLine (OrgParser.LineContext ctx) {
        OrgText current = new OrgText(
                ctx.LINE().getText(),
                ctx.INDENT() == null ? 0 : ctx.INDENT().getText().length());
        if (this.last != null && this.last.isType("OrgText")) {
            // text appends to previous
            ((OrgText)this.last).add(current);
        } else if (this.lastParent.empty()) {
            // top level
            this.roots.add(current);
            this.last = current;
        } else if (this.lastParent.peek().isType("OrgHeading", "OrgToDo")) {
            this.lastParent.peek().add(current);
        } else {
            // parent is list
            ((OrgList)this.lastParent.peek()).addText(current);
        }
    }

    /** Handle headings without 'to-do' semantics.
     * @param ctx the parser context
     */
    @Override
    public void enterHeadingLine (OrgParser.HeadingLineContext ctx) {
        OrgHeading current = new OrgHeading(
                ctx.HEADING_LEVEL().getText(),
                ctx.HEADING().getText());
        this.handleToDoOrHeading(current);
    }

    /** Handle 'to-do' headings.
     * @param ctx the parser context
     */
    @Override
    public void enterTodoLine (OrgParser.TodoLineContext ctx) {
        OrgToDo current = new OrgToDo(
                ctx.HEADING_LEVEL().getText(),
                ctx.HEADING().getText(),
                ctx.TODO().getText());
        this.handleToDoOrHeading(current);
    }

    /** Place to-do and heading objects into the file forest.
     * @param current the heading/to-do being parsed
     */
    public void handleToDoOrHeading (OrgHeading current) {
        if (this.last == null) {
            // first line
            this.roots.add(current);
        } else {
            // Headings are children of the last parent that was a heading or top level.
            while (!this.lastParent.empty()
                    && !this.lastParent.peek().isType("OrgHeading", "OrgToDo")
                    && ((OrgHeading)this.lastParent.peek()).level >= current.level) {
                this.lastParent.pop();
            }
            if (this.lastParent.empty()) {
                // top level
                this.roots.add(current);
            } else {
                this.lastParent.peek().add(current);
            }
        }
        this.lastParent.push(current);
        this.last = current;
    }

    /** Construct table from parser context.
     * `OrgTable` is constructed from the top level `table` rule. The table objects are fixed size
     * for now, so constructing them from this level gives access to all the necessary size
     * information at once. Also, this eliminates the need for more specific listeners for the rows
     * and columns.
     * @param ctx the parser context
     */
    @Override
    public void enterTable (OrgParser.TableContext ctx) {
        OrgParser.TableRowContext top = ctx.tableRow(0);
        OrgTable current = new OrgTable(
                ctx.tableRow().size(),
                top.tableCol().size(),
                top.INDENT() == null ? 0 : top.INDENT().getText().length());
        int r = 0;
        for (OrgParser.TableRowContext rc: ctx.tableRow()) {
            int c = 0;
            for (OrgParser.TableColContext cc: rc.tableCol()) {
                current.set(r, c, cc.TABLE_COL().getText());
                ++c;
            }
            ++r;
        }
        this.lastParent.peek().add(current);
        this.last = current;
    }

    /** Handle un-enumerated list line generation.
     * @param ctx the parser context
     */
    @Override
    public void enterUnenumeratedLine (OrgParser.UnenumeratedLineContext ctx) {
        OrgList current = new OrgList(
                ctx.INDENT() == null ? 0 : ctx.INDENT().getText().length(),
                ctx.ULIST().getText(),
                ctx.LINE().getText());
        this.handleList(current);
    }

    /** Handle enumerated list line generation.
     * @param ctx the parser context
     */
    @Override
    public void enterEnumeratedLine (OrgParser.EnumeratedLineContext ctx) {
        OrgList current = new OrgList(
                ctx.INDENT() == null ? 0 : ctx.INDENT().getText().length(),
                ctx.ILIST().getText(),
                ctx.LINE().getText());
        this.handleList(current);
    }

    /** Place list objects into the file forest.
     * @param current the list item being parsed
     */
    public void handleList (OrgList current) {
        if (this.last == null || this.lastParent.empty()) {
            // top level
            this.roots.add(current);
        } else if (this.lastParent.peek().isType("OrgList")) {
            while (this.lastParent.peek().indent > current.indent) {
                this.lastParent.pop();
            }
            OrgTreeNode parent = this.lastParent.peek();
            if (parent.isType("OrgHeading", "OrgToDo") || parent.indent < current.indent) {
                // parent found
                parent.add(current);
            } else if (((OrgList)parent).listType == current.listType
                       && ((OrgList)parent).marker.equals(current.marker)) {
                // sibling found
                ((OrgList)parent).addSibling(current);
                this.lastParent.pop(); // so we can just pop once in else case
            } else {
                // same indent list of different type found
                this.lastParent.pop();
                this.lastParent.peek().add(current);
            }
        } else {
            // parent is heading
            this.lastParent.peek().add(current);
        }
        this.lastParent.push(current);
        this.last = current;
    }

    /** Handle events parenting to closest heading.
     * @param ctx the parser context
     */
    @Override
    public void enterEvent(OrgParser.EventContext ctx) {
        if (ctx.scheduled() != null) {

        } else if (ctx.deadline() != null) {

        } else {
            // closed
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
