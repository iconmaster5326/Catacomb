package info.iconmaster.ccomb.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import info.iconmaster.ccomb.exceptions.CatacombException;

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
	public abstract List<CCombType> withVarsReplaced(Map<VarType.TypeGroup, List<CCombType>> replaceWith) throws CatacombException;
	
	/**
	 * Returns true if the type is concrete.
	 * Returns false if the type is abstract.
	 * 
	 * @return
	 */
	public abstract boolean isConcrete();
	
	/**
	 * This is a version of withVarsReplaced that works on a list of types instead of just one.
	 * @param typeToReplace
	 * @param group
	 * @param replaceWith
	 * @return
	 * @throws CatacombException
	 */
	public static List<CCombType> withVarsReplaced(List<CCombType> typesToReplace, Map<VarType.TypeGroup, List<CCombType>> replaceWith) throws CatacombException {
		ArrayList<CCombType> types = new ArrayList<>();
		for (CCombType type : typesToReplace) {
			types.addAll(type.withVarsReplaced(replaceWith));
		}
		return types;
	}
}
