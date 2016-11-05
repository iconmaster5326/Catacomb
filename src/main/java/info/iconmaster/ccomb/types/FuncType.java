package info.iconmaster.ccomb.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import info.iconmaster.ccomb.types.VarType.TypeGroup;

public class FuncType extends CCombType {
	
	ArrayList<CCombType> lhs, rhs;
	
	public FuncType() {
		this.lhs = new ArrayList<>();
		this.rhs = new ArrayList<>();
	}
	
	public FuncType(Collection<? extends CCombType> rhs) {
		this.lhs = new ArrayList<>();
		this.rhs = new ArrayList<>(rhs);
	}
	
	public FuncType(Collection<? extends CCombType> lhs, Collection<? extends CCombType> rhs) {
		this.lhs = new ArrayList<>(lhs);
		this.rhs = new ArrayList<>(rhs);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (CCombType type : lhs) {
			sb.append(type);
			sb.append(" ");
		}
		sb.append("-- ");
		for (CCombType type : rhs) {
			sb.append(type);
			sb.append(" ");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("}");
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		
		if (other instanceof FuncType) {
			FuncType other2 = (FuncType)other;
			if (lhs.size() != other2.lhs.size() || rhs.size() != other2.rhs.size()) return false;
			
			for (int i = 0; i < lhs.size(); i++) {
				if (!lhs.get(i).equals(other2.lhs.get(i))) return false;
			}
			
			for (int i = 0; i < rhs.size(); i++) {
				if (!rhs.get(i).equals(other2.rhs.get(i))) return false;
			}
			
			return true;
		} else if (other instanceof VarType) {
			VarType other2 = (VarType)other;
			if (other2.group.supertype.equals(this)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Function types are castable if:
	 * 1. the other is the root type or function root type,
	 * 2. they are both function types, and all the types are castable, or
	 * 3. they are both function types, the other has a longer type signature, but they are equal on both sides.
	 */
	@Override
	public boolean isCastableTo(CCombType other) {
		if (other instanceof FuncType) {
			FuncType other2 = (FuncType)other;
			
			if (lhs.size() > other2.lhs.size() || rhs.size() > other2.rhs.size()) return false;
			
			int li = 0;
			for (li = 0; li < lhs.size(); li++) {
				if (!lhs.get(li).isCastableTo(other2.lhs.get(li))) return false;
			}
			
			int ri = 0;
			for (ri = 0; ri < rhs.size(); ri++) {
				if (!rhs.get(ri).isCastableTo(other2.rhs.get(ri))) return false;
			}
			
			if (li-other2.lhs.size() != ri-other2.rhs.size()) return false;
			
			for (int i = 0; i < li-other2.lhs.size(); i++) {
				if (!other2.lhs.get(i+li).isCastableTo(other2.rhs.get(i+ri))) return false;
			}
			
			return true;
		} else if (PolyType.FUNC.isCastableTo(other)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public CCombType withVarsReplaced(Map<TypeGroup, CCombType> types) {
		FuncType newType = new FuncType();
		
		for (CCombType type : lhs) {
			newType.lhs.add(type.withVarsReplaced(types));
		}
		
		for (CCombType type : rhs) {
			newType.rhs.add(type.withVarsReplaced(types));
		}
		
		return newType;
	}
	
	@Override
	public boolean isConcrete() {
		return true;
	}

}
