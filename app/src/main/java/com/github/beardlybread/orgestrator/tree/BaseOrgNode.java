import java.util.ArrayList;
import java.util.Collection;

public class BaseOrgNode implements OrgNode {

    protected ArrayList<OrgNode> nodes; 
    protected OrgNode parent; 

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    public BaseOrgNode () {
        this.nodes = new ArrayList<>();
        this.parent = null;
    }

    public BaseOrgNode (OrgNode parent) {
        this();
        this.parent = parent;
    }

    public BaseOrgNode (Collection<OrgNode> nodes) {
        this.nodes = new ArrayList<>(nodes);
    }

    public BaseOrgNode (OrgNode parent, Collection<OrgNode> nodes) {
        this(nodes);
        this.parent = parent;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////
    
    @Override
    public OrgNode get (int index)
            throws IndexOutOfBoundsException {
        return this.nodes.get(index);
    }

    @Override
    public int indexOf (OrgNode node) {
        return this.nodes.indexOf(node);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Setters
    ////////////////////////////////////////////////////////////////////////////

    @Override
    public void add (OrgNode node) {
        this.nodes.add(node);
    }
    
    @Override
    public void add (int index, OrgNode node) {
        this.nodes.add(index, node);
    }

    @Override
    public boolean remove (OrgNode node) {
        return this.nodes.remove(node);
    } 

    ////////////////////////////////////////////////////////////////////////////
    // Utility
    ////////////////////////////////////////////////////////////////////////////
    
    @Override
    public String toString () {
        return "";
    }

    @Override
    public void write (Writer target) {
        target.write(this.toString());
        for (OrgNode n: this.nodes) {
            n.write(target);
        }
    }

}
