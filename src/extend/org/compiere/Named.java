package extend.org.compiere;

/**
 * An object with a name. Used as a base ancestor.
 * @author V.Sokolov
 *
 */

public class Named implements INamedSet {

	protected String name;

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Is there an established name
     *
     * @return
     */
    public boolean hasName() {
        return name != null && name.length() > 0;
    }

    /**
     * Check for a match case-insensitive name
     */
    public boolean hasName(String name) {
        return this.name != null && this.name.equalsIgnoreCase(name);
    }
    
}
