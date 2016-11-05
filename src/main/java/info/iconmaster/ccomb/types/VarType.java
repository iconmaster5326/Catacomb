package info.iconmaster.ccomb.types;

/**
 * A variable type.
 * @author iconmaster
 *
 */
public class VarType extends CCombType {
	
	public static class TypeGroup {
		CCombType supertype;
		
		public TypeGroup() {
			this.supertype = PolyType.polyTypes.get("any");
		}
		
		public TypeGroup(CCombType supertype) {
			this.supertype = supertype;
		}
	}
	
	String name;
	TypeGroup group;
	
	public VarType() {
		this(new TypeGroup());
	}
	
	public VarType(String name) {
		this(name, new TypeGroup());
	}
	
	public VarType(CCombType supertype) {
		this(new TypeGroup(supertype));
	}
	
	public VarType(String name, CCombType supertype) {
		this(name, new TypeGroup(supertype));
	}
	
	public VarType(TypeGroup group) {
		this.group = group;
	}
	
	public VarType(String name, TypeGroup group) {
		this.name = name;
		this.group = group;
	}
	
	@Override
	public String toString() {
		if (name == null) {
			return "'?";
		} else {
			return "'" + name;
		}
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		
		if (other instanceof CCombType) {
			CCombType other2 = (CCombType)other;
			return group.supertype.equals(other2);
		}
		
		return false;
	}
	
	/**
	 * A variable is castable if the supertype is castable.
	 */
	@Override
	public boolean isCastableTo(CCombType other) {
		return group.supertype.isCastableTo(other);
	}

}