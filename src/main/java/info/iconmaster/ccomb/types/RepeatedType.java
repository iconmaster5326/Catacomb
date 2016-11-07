package info.iconmaster.ccomb.types;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import info.iconmaster.ccomb.exceptions.CatacombException;
import info.iconmaster.ccomb.types.VarType.TypeGroup;

/**
 * This represents 0 or more of a type on the stack.
 * In practice, no variables should be of this type. It is used in function composition only.
 * @author iconmaster
 *
 */
public class RepeatedType extends CCombType {
	
	public CCombType supertype;
	
	public RepeatedType() {
		this.supertype = PolyType.ANY;
	}
	
	public RepeatedType(CCombType supertype) {
		this.supertype = supertype;
	}
	
	@Override
	public boolean isCastableTo(CCombType other) {
		return supertype.isCastableTo(other);
	}
	
	@Override
	public List<CCombType> withVarsReplaced(Map<TypeGroup, List<CCombType>> replaceWith) throws CatacombException {
		List<CCombType> replaced = supertype.withVarsReplaced(replaceWith);
		if (replaced.size() != 1) {
			throw new CatacombException("Cannot have nested repition types");
		}
		return Arrays.asList(new RepeatedType(replaced.get(0)));
	}
	
	@Override
	public boolean isConcrete() {
		return supertype.isConcrete();
	}
	
	@Override
	public String toString() {
		return "*" + supertype.toString();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof RepeatedType) {
			return this.supertype == ((RepeatedType)other).supertype;
		}
		return false;
	}
}
