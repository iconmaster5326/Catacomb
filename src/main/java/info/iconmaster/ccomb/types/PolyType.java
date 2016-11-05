package info.iconmaster.ccomb.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
	public boolean primitive = false;
	
	private PolyType() {}
	
	public PolyType(String name) throws CatacombException {
		this.name = name;
		this.typevars = new ArrayList<>();
		this.supertypes = new ArrayList<>();
		
		assertWellFormed();
		
		PolyType other = polyTypes.get(name);
		addSupertypes(other);
		this.primitive = other.primitive;
	}
	
	public PolyType(String name, Collection<? extends CCombType> typevars) throws CatacombException {
		this.name = name;
		this.typevars = new ArrayList<>(typevars);
		this.supertypes = new ArrayList<>();
		
		assertWellFormed();
		
		PolyType other = polyTypes.get(name);
		addSupertypes(other);
		this.primitive = other.primitive;
	}
	
	public PolyType(PolyType other) {
		this.name = other.name;
		this.typevars = new ArrayList<>(other.typevars);
		this.supertypes = new ArrayList<>(other.supertypes);
		this.primitive = other.primitive;
		
		// always well-formed, so no error-checking needed here
	}
	
	public PolyType(PolyType other, Collection<? extends CCombType> typevars) throws CatacombException {
		this.name = other.name;
		this.typevars = new ArrayList<>(typevars);
		this.supertypes = new ArrayList<>();
		this.primitive = other.primitive;
		
		assertWellFormed();
		addSupertypes(other);
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
			sb.append(name);
			sb.append(" ");
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
	 * A poly type is castable to other if:
	 * 1. they are equal,
	 * 2. they are the same type name, and all params are castable, or
	 * 3. a supertype is castable to other.
	 */
	@Override
	public boolean isCastableTo(CCombType other) {
		
		if (this.equals(other)) return true;
		if (ANY.equals(other)) return true;
		
		if (other instanceof PolyType && ((PolyType)other).name.equals(name)) {
			boolean allMatch = true;
			int i = 0;
			for (CCombType type : ((PolyType)other).typevars) {
				if (!typevars.get(i).isCastableTo(type)) allMatch = false;
				i++;
			}
			if (allMatch) return true;
		}
		
		for (CCombType type : supertypes) {
			if (type.isCastableTo(other)) return true;
		}
		
		return false;
	}
	
	@Override
	public CCombType withVarsReplaced(Map<VarType.TypeGroup, CCombType> types) {
		ArrayList<CCombType> newTypevars = new ArrayList<>();
		ArrayList<CCombType> newSupertypes = new ArrayList<>();
		
		for (CCombType type : typevars) {
			newTypevars.add(type.withVarsReplaced(types));
		}
		
		for (CCombType type : supertypes) {
			newSupertypes.add(type.withVarsReplaced(types));
		}
		
		PolyType retType = new PolyType(this);
		retType.typevars = newTypevars;
		retType.supertypes = newSupertypes;
		return retType;
	}
	
	// stuff for type lookup.
	
	/**
	 * A map of type names to corresponding type templates.
	 */
	public static final HashMap<String, PolyType> polyTypes = new HashMap<>();
	
	/**
	 * Static variables for easy access to common types used in other classes, like 'any'.
	 */
	public static PolyType ANY;
	public static PolyType FUNC;
	
	/**
	 * Registers the smallest number of classes so that type checking doesn't crash when you try to use it.
	 * 
	 * @throws CatacombException
	 */
	public static void registerMinimumTypes() throws CatacombException {
		ANY = registerPolyType("any", Arrays.asList(), Arrays.asList());
		FUNC = registerPolyType("func", Arrays.asList(), Arrays.asList(ANY));
	}
	
	/**
	 * This sets up the whole type graph.
	 * 
	 * @throws CatacombException
	 */
	public static void registerTypes() throws CatacombException {
		registerMinimumTypes();
	}
	
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
	 * Throws an error if the type is not well-formed.
	 * That is, this errors if the type does not match the template registered for it.
	 * 
	 * Intended for use in the constructors.
	 * 
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
	
	/**
	 * Adds the supertypes to this type, modifying them in correspondence with the type params.
	 * 
	 * Intended for use in the constructors.
	 * 
	 * @param types
	 */
	public void addSupertypes(PolyType template) {
		HashMap<VarType.TypeGroup, CCombType> typemap = new HashMap<>();
		for (int i = 0; i < typevars.size(); i++) {
			if (template.typevars.get(i) instanceof VarType) typemap.put(((VarType)template.typevars.get(i)).group, typevars.get(i));
		}
		
		for (CCombType type : template.supertypes) {
			supertypes.add(type.withVarsReplaced(typemap));
		}
	}
	
	/**
	 * A poly type is concrete if it is primitive, or if it has a concrete supertype.
	 */
	@Override
	public boolean isConcrete() {
		if (primitive) {
			for (CCombType type : typevars) {
				if (!type.isConcrete()) return false;
			}
			return true;
		}
		
		for (CCombType type : supertypes) {
			if (type.isConcrete()) return true;
		}
		return false;
	}
}
