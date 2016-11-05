package info.iconmaster.ccomb.function;

import java.util.ArrayList;
import java.util.Collection;

import info.iconmaster.ccomb.exceptions.CatacombException;
import info.iconmaster.ccomb.execute.CCombStack;
import info.iconmaster.ccomb.types.FuncType;

/**
 * A user function. It is composed of a series of other functions.
 * 
 * @author iconmaster
 *
 */
public class UserFunction extends Function {
	ArrayList<Function> funcs;
	
	public UserFunction(Collection<? extends Function> funcs) throws CatacombException {
		funcs = new ArrayList<>(funcs);
		// TODO: compose types, get type sig
		type = new FuncType();
	}
	
	@Override
	public void execute(CCombStack stack) throws CatacombException {
		for (Function func : funcs) {
			func.execute(stack);
		}
	}
}
