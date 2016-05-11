package com.github.beardlybread.orgestrator.org;

import com.github.beardlybread.orgestrator.org.antlr.*;
import com.github.beardlybread.orgestrator.util.Predicate;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Stack;

public class OrgFile extends OrgParserBaseListener {

    protected ArrayList<OrgNode> roots = null;
    protected Stack<OrgTreeNode> lastParent = null;
    protected OrgNode last = null;

    /** Return the contents of the OrgFile.
     * @return the raw contents of this object
     */
    public ArrayList<OrgNode> getRoots () { return this.roots; }

    /** Return a list of nodes matching the predicate.
     * @param pred an implementation of Predicate<> to filter the contents
     * @return a list of nodes matching the predicate defined in pred
     */
    public ArrayList<OrgNode> search (Predicate<OrgNode> pred) {
        ArrayList<OrgNode> out = new ArrayList<>();
        for (OrgNode n: this.roots) {
            this.searchNode(n, pred, out);
        }
        return out;
    }

    /** Collect search results for public search() method.
     * @param node the location to search
     * @param predicate the filtering predicate
     * @param acc the accumulator
     */
    private void searchNode (OrgNode node, Predicate<OrgNode> predicate, ArrayList<OrgNode> acc) {
        if (predicate.call(node))
            acc.add(node);
        if (node instanceof OrgTreeNode) {
            for (OrgNode c: ((OrgTreeNode) node).getChildren()) {
                searchNode(c, predicate, acc);
            }
        }
    }

    /** Write the data to a Writer in proper org file format.
     * @param target the output object for constructing the text
     * @throws IOException
     */
    public void write (Writer target) throws IOException {
        for (OrgNode n: this.roots) {
            n.write(target);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Listener callbacks
    ////////////////////////////////////////////////////////////////////////////

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
        String text = ctx.LINE() == null ? "" : ctx.LINE().getText().trim();
        int indent = ctx.INDENT() == null ? 0 : ctx.INDENT().getText().length();
        // if the text is empty, current is empty
        OrgNode current = text.length() > 0 ? new OrgText(text, indent) : new OrgEmpty();
        if (this.last != null && this.last.isType("OrgText")) {
            // text appends to previous
            ((OrgText)this.last).add((OrgText)current);
        } else if (this.lastParent.empty()) {
            // top level
            this.roots.add(current);
            this.last = current;
        } else if (this.lastParent.peek().isType("OrgHeading", "OrgToDo")) {
            this.lastParent.peek().add(current);
            this.last = current;
        } else if (current.isType("OrgEmpty")) {
            // TODO empty nodes should NOT split lists, but for now...
            this.popToHeading();
            if (this.lastParent.empty()) {
                this.roots.add(current);
            } else {
                this.lastParent.peek().add(current);
            }
        } else {
            // parent is list
            ((OrgList)this.lastParent.peek()).addText((OrgText)current);
        }
    }

    /** Handle headings without 'to-do' semantics.
     * @param ctx the parser context
     */
    @Override
    public void enterHeadingLine (OrgParser.HeadingLineContext ctx) {
        OrgHeading current = new OrgHeading(ctx.HEADING_LEVEL().getText(), ctx.HEADING().getText());
        this.handleToDoOrHeading(current);
    }

    /** Handle 'to-do' headings.
     * @param ctx the parser context
     */
    @Override
    public void enterTodoLine (OrgParser.TodoLineContext ctx) {
        OrgToDo current = new OrgToDo(ctx.HEADING_LEVEL().getText(), ctx.HEADING().getText(),
                                      ctx.TODO().getText());
        this.handleToDoOrHeading(current);
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
        OrgTable current = new OrgTable(ctx.tableRow().size(), top.tableCol().size(), top.INDENT());
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
        OrgList current = new OrgList(ctx.INDENT(), ctx.ULIST().getText(), ctx.LINE().getText());
        this.handleList(current);
    }

    /** Handle enumerated list line generation.
     * @param ctx the parser context
     */
    @Override
    public void enterEnumeratedLine (OrgParser.EnumeratedLineContext ctx) {
        OrgList current = new OrgList(ctx.INDENT(), ctx.ILIST().getText(), ctx.LINE().getText());
        this.handleList(current);
    }

    /** Handle events parenting to closest heading.
     * @param ctx the parser context
     */
    @Override
    public void enterEvent(OrgParser.EventContext ctx) {
        OrgEvent current;
        if (ctx.scheduled() != null) {
            current = new OrgEvent(
                    ctx.scheduled().INDENT(),
                    OrgEvent.SCHEDULED,
                    new OrgDate(ctx.scheduled().timestamp().date()));
        } else if (ctx.deadline() != null) {
            current = new OrgEvent(
                    ctx.deadline().INDENT(),
                    OrgEvent.DEADLINE,
                    new OrgDate(ctx.deadline().timestamp().date()));
        } else if (ctx.closed().CLOSED_SCHEDULED() != null) {
            current = new OrgEvent(
                    ctx.closed().INDENT(),
                    OrgEvent.SCHEDULED,
                    new OrgDate(ctx.closed().timestamp(0).date()),
                    new OrgDate(ctx.closed().timestamp(1).date()));
        } else if (ctx.closed().CLOSED_DEADLINE() != null) {
            current = new OrgEvent(
                    ctx.closed().INDENT(),
                    OrgEvent.DEADLINE,
                    new OrgDate(ctx.closed().timestamp(0).date()),
                    new OrgDate(ctx.closed().timestamp(1).date()));
        } else {
            current = new OrgEvent(
                    ctx.closed().INDENT(),
                    OrgEvent.CLOSED,
                    new OrgDate(ctx.closed().timestamp(0).date()));
        }
        this.popToHeading();
        if (this.lastParent.empty()) {
            this.roots.add(current);
        } else {
            this.lastParent.peek().add(current);
        }
        this.last = current;
    }

    /** Handle properties parenting to closest heading.
     * @param ctx the parser context
     */
    @Override
    public void enterPropertyList(OrgParser.PropertyListContext ctx) {
        OrgProperty current = new OrgProperty(ctx.INDENT());
        for (OrgParser.PropertyContext pc: ctx.property()) {
            if (pc.lastRepeat() != null) {
                current.setLastRepeat(new OrgDate(pc.lastRepeat().date()));
            } else {
                String prop = pc.propertyPair().PROPERTY().getText();
                String val = pc.propertyPair().VALUE() == null ? ""
                        : pc.propertyPair().VALUE().getText();
                current.put(prop, val);
            }
        }
        this.popToHeading();
        if (this.lastParent.empty()) {
            this.roots.add(current);
        } else {
            this.lastParent.peek().add(current);
        }
    }

    /** Add empty nodes to correcty space write output.
     * @param ctx the parser context (unused)
     */
    @Override
    public void enterEmpty (OrgParser.EmptyContext ctx) {
        this.popToHeading();
        OrgEmpty current = new OrgEmpty();
        if (this.lastParent.empty()) {
            this.roots.add(current);
        } else {
            this.lastParent.peek().add(current);
        }
        this.last = current;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Helpers
    ////////////////////////////////////////////////////////////////////////////

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

    /** Place to-do and heading objects into the file forest.
     * @param current the heading/to-do being parsed
     */
    public void handleToDoOrHeading (OrgHeading current) {
        if (this.last == null) {
            // first line
            this.roots.add(current);
        } else {
            // Headings are children of the last parent that was a heading or top level.
            this.popToHeading();
            while (!this.lastParent.empty()
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

    /** Remove parents to the first heading.
     */
    private void popToHeading () {
        while (!this.lastParent.empty()
                && !this.lastParent.peek().isType("OrgHeading", "OrgToDo")) {
            this.lastParent.pop();
        }
    }
}
