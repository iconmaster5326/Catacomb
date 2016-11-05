package info.iconmaster.ccomb.types;

/**
 * The base class for all Catacomb types.
 * 
 * @author iconmaster
 *
 */
public abstract class CCombType {
	/**
	 * Returns whether or not this type is castable to the other type.
	 * @param other
	 * @return
	 */
	public abstract boolean isCastableTo(CCombType other);
}
