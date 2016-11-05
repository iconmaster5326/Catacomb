package info.iconmaster.ccomb.function;

import info.iconmaster.ccomb.types.FuncType;

/**
 * A system function. When executed, it runs custom Java code.
 * 
 * @author iconmaster
 *
 */
public abstract class SystemFunction extends Function {
	public SystemFunction(FuncType type) {
		super(type);
	}
}
