package info.iconmaster.ccomb.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import info.iconmaster.ccomb.exceptions.CatacombException;

/**
 * A polymorphic or single type.
 * 
 * @author iconmaster
 *
 */
public class PolyType extends CCombType {
	
	public String name;
	public ArrayList<CCombType> typevars;
	public ArrayList<CCombType> supertypes;
	
	private PolyType() {}
	
	public PolyType(String name) throws CatacombException {
		this.name = name;
		this.typevars = new ArrayList<>();
		this.supertypes = new ArrayList<>();
		
		assertWellFormed();
		unifyTypes(polyTypes.get(name));
	}
	
	public PolyType(String name, Collection<? extends CCombType> typevars) throws CatacombException {
		this.name = name;
		this.typevars = new ArrayList<>(typevars);
		this.supertypes = new ArrayList<>();
		
		assertWellFormed();
		unifyTypes(polyTypes.get(name));
	}
	
	public PolyType(PolyType other) {
		this.name = other.name;
		this.typevars = new ArrayList<>(other.typevars);
		this.supertypes = new ArrayList<>(other.supertypes);
		
		// always well-formed, so no error-checking needed here
	}
	
	public PolyType(PolyType other, Collection<? extends CCombType> typevars) throws CatacombException {
		this.name = other.name;
		this.typevars = new ArrayList<>(typevars);
		this.supertypes = new ArrayList<>();
		
		assertWellFormed();
		unifyTypes(polyTypes.get(name));
	}
	
	/**
	 * Two polymorphic types are equal if they have:
	 * 1. the same name
	 * 2. the same types of all the type parameters
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof PolyType) {
			PolyType other2 = (PolyType)other;
			if (other2.name.equals(this.name)) {
				// We assume that they have equal numbers of typevars, which should always be true.
				for (int i = 0; i < typevars.size(); i++) {
					if (!other2.typevars.get(i).equals(this.typevars.get(i))) return false;
				}
				return true;
			}
		} else if (other instanceof VarType) {
			VarType other2 = (VarType)other;
			if (other2.group.supertype.equals(this)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		if (typevars.isEmpty()) {
			return name;
		} else {
			StringBuilder sb = new StringBuilder();
			
			sb.append("(");
			for (CCombType type : typevars) {
				sb.append(type);
				sb.append(" ");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append(")");
			
			return sb.toString();
		}
	}
	
	/**
	 * A poly type is castable to other if they are equal, or a supertype is castable to other.
	 */
	@Override
	public boolean isCastableTo(CCombType other) {
		
		if (this.equals(other)) return true;
		
		for (CCombType type : supertypes) {
			if (type.isCastableTo(other)) return true;
		}
		
		return false;
	}
	
	// stuff for type lookup.
	
	/**
	 * A map of type names to corresponding type templates.
	 */
	public static final HashMap<String, PolyType> polyTypes = new HashMap<>();
	
	/**
	 * Before you create instances of PolyType, a template needs to be registered.
	 * This way, you can't have silly things, like two types with the same name, and two different supertypes.
	 * 
	 * @param name
	 * @param typevars
	 * @param supertypes
	 * @return
	 * @throws CatacombException
	 */
	public static PolyType registerPolyType(String name, Collection<? extends CCombType> typevars, Collection<? extends CCombType> supertypes) throws CatacombException {
		// if this type has already been registered, error
		if (polyTypes.containsKey(name)) {
			throw new CatacombException("Poly type already registered");
		}
		
		PolyType template = new PolyType();
		template.name = name;
		template.typevars = new ArrayList<>(typevars);
		template.supertypes = new ArrayList<>(supertypes);
		
		polyTypes.put(name, template);
		return template;
	}
	
	/**
	 * Sets this type's supertypes' type parameters to match this type's type parameters.
	 * @param other
	 */
	public void unifyTypes(PolyType template) {
		// TODO
	}
	
	/**
	 * Throws an error if the type is not well-formed.
	 * That is, this errors if the type does not match the template registered for it.
	 * @throws CatacombException 
	 */
	public void assertWellFormed() throws CatacombException {
		if (!polyTypes.containsKey(name)) {
			throw new CatacombException("Unregistered poly type");
		}
		
		PolyType template = polyTypes.get(name);
		
		if (typevars.size() != template.typevars.size()) {
			throw new CatacombException("Type params size mismatch");
		}
		
		if (!isCastableTo(template)) {
			throw new CatacombException("Type is not castable to template");
		}
	}
}
