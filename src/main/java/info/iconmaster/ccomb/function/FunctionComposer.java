package info.iconmaster.ccomb.function;

import java.util.Collection;

import info.iconmaster.ccomb.exceptions.CatacombException;
import info.iconmaster.ccomb.types.CCombType;
import info.iconmaster.ccomb.types.FuncType;

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
		//System.out.println("BEGIN: " + retType.toString());
		for (Function func : funcs) {
			//System.out.print(retType.toString() + " && " + func.type.toString());
			int producedSize = retType.rhs.size();
			int consumedSize = func.type.lhs.size();
			for (int i = 0; i < Math.max(producedSize, consumedSize); i++) {
				if (i >= producedSize) {
					// move to back of lhs of retType
					CCombType consumed = func.type.lhs.get(func.type.lhs.size()-i-1);
					retType.lhs.add(0, consumed);
				} else if (i >= consumedSize) {
					// move to back of rhs of retType (a no-op)
					break;
				} else {
					// ensure types match
					CCombType produced = retType.rhs.get(retType.rhs.size()-i-1);
					CCombType consumed = func.type.lhs.get(func.type.lhs.size()-i-1);
					
					if (!produced.isCastableTo(consumed)) {
						throw new CatacombException("Cannot compose functions");
					}
				}
			}
			// remove elements comsumed by func
			for (int i = 0; i < Math.min(producedSize, consumedSize); i++) {
				retType.rhs.remove(retType.rhs.size()-1);
			}
			// add elements produced by func
			for (CCombType type : func.type.rhs) {
				retType.rhs.add(type);
			}
			//System.out.println(" ==> " + retType.toString());
		}
		//System.out.println("END: " + retType.toString());
		return retType;
	}
}
