package info.iconmaster.ccomb.types;

import java.util.Map;

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
	
	/**
	 * Creates a copy of this class, with type variables replaced.
	 * It is only intended to use var types as keys, but any types can be used.
	 * 
	 * @return
	 */
	public abstract CCombType withVarsReplaced(Map<VarType.TypeGroup, CCombType> types);
	
	/**
	 * Returns true if the type is concrete.
	 * Returns false if the type is abstract.
	 * 
	 * @return
	 */
	public abstract boolean isConcrete();
}
