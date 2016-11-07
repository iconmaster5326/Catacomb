package info.iconmaster.ccomb.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import info.iconmaster.ccomb.exceptions.CatacombException;
import info.iconmaster.ccomb.types.CCombType;
import info.iconmaster.ccomb.types.FuncType;
import info.iconmaster.ccomb.types.VarType;

/**
 * A utility class to hold the code for composing functions together.
 * 
 * @author iconmaster
 *
 */
public class FunctionComposer {
	/**
	 * Do not instantiate this class.
	 */
	private FunctionComposer() {}
	
	/**
	 * Finds the return type for a function created by composing funcs together.
	 * Throws an error if the function cannot be composed.
	 * 
	 * @param funcs
	 * @return
	 * @throws CatacombException
	 */
	public static FuncType compose(Collection<? extends Function> funcs) throws CatacombException {
		FuncType retType = new FuncType();
		for (Function func : funcs) {
			MatchResult res = match(retType.rhs, func.type.lhs);
			
			ArrayList<CCombType> newLHS = new ArrayList<>();
			addReplaced(newLHS, res.consumedLeft, res.repls);
			addReplaced(newLHS, retType.lhs, res.repls);
			retType.lhs = newLHS;
			
			ArrayList<CCombType> newRHS = new ArrayList<>();
			addReplaced(newRHS, res.producedLeft, res.repls);
			addReplaced(newRHS, func.type.rhs, res.repls);
			retType.rhs = newRHS;
		}
		return retType;
	}
	
	private static void addReplaced(List<CCombType> toAdd, List<CCombType> toReplace, Map<VarType.TypeGroup, List<CCombType>> repls) throws CatacombException {
		toAdd.addAll(CCombType.withVarsReplaced(toReplace, repls));
	}
	
	public static class MatchResult {
		public HashMap<VarType.TypeGroup, List<CCombType>> repls = new HashMap<>();
		public Stack<CCombType> producedLeft = new Stack<>();
		public Stack<CCombType> consumedLeft = new Stack<>();
	}
	
	public static MatchResult match(Collection<? extends CCombType> produced, Collection<? extends CCombType> consumed) throws CatacombException {
		MatchResult res = new MatchResult();
		res.producedLeft.addAll(produced);
		res.consumedLeft.addAll(consumed);
		
		while (!res.producedLeft.isEmpty() && !res.consumedLeft.isEmpty()) {
			match(res, res.consumedLeft.pop());
		}
		
		return res;
	}
	
	private static Stack<CCombType> match(MatchResult res, CCombType consumedType) throws CatacombException {
		if (consumedType instanceof VarType) {
			Stack<CCombType> stack = match(res, ((VarType)consumedType).group.supertype);
			
			res.repls.put(((VarType)consumedType).group, stack);
			
			Stack<CCombType> producedLeftRepl = new Stack<>();
			producedLeftRepl.addAll(res.producedLeft);
			res.producedLeft.clear();
			for (int i = 0; i < producedLeftRepl.size(); i++) {
				res.producedLeft.addAll(producedLeftRepl.get(i).withVarsReplaced(res.repls));
			}
			
			Stack<CCombType> consumedLeftRepl = new Stack<>();
			consumedLeftRepl.addAll(res.consumedLeft);
			res.consumedLeft.clear();
			for (int i = 0; i < consumedLeftRepl.size(); i++) {
				res.consumedLeft.addAll(consumedLeftRepl.get(i).withVarsReplaced(res.repls));
			}
			
			return stack;
		} else {
			CCombType producedType = res.producedLeft.pop();
			
			if (!producedType.isCastableTo(consumedType)) {
				throw new CatacombException("could not match single types");
			}
			
			Stack<CCombType> stack = new Stack<>();
			stack.push(producedType);
			return stack;
		}
	}
}
