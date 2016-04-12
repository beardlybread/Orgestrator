import java.util.Collection;
import java.util.List;

public interface OrgNode {

    // Getters
    public OrgNode get(int index)
            throws IndexOutOfBoundsException;
    public int indexOf(OrgNode node);

    // Setters
    public void add (OrgNode node);
    public void add (int index, OrgNode node);
    public boolean remove (OrgNode node);

    // Utility
    public String toString ();
    public void write (Writer target);

}
