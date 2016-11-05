package info.iconmaster.ccomb.function;

import info.iconmaster.ccomb.exceptions.CatacombException;
import info.iconmaster.ccomb.execute.CCombStack;
import info.iconmaster.ccomb.types.FuncType;

/**
 * This class is a container for an executable function. It has a type, which is always a function type.
 * 
 * @author iconmaster
 *
 */
public abstract class Function {
	public FuncType type;
	
	protected Function() {}
	
	public Function(FuncType type) {
		this.type = type;
	}
	
	/**
	 * Execute the function on the stack.
	 * 
	 * @param stack
	 * @throws CatacombException
	 */
	public abstract void execute(CCombStack stack) throws CatacombException;
}
