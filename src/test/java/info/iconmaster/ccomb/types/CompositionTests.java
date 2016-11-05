package info.iconmaster.ccomb.types;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import info.iconmaster.ccomb.exceptions.CatacombException;
import info.iconmaster.ccomb.execute.CCombStack;
import info.iconmaster.ccomb.function.FunctionComposer;
import info.iconmaster.ccomb.function.SystemFunction;
import info.iconmaster.ccomb.function.UserFunction;
import static org.junit.Assert.*;

public class CompositionTests {
	
	public static class TestFunction extends SystemFunction {
		
		public TestFunction(FuncType type) {
			super(type);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void execute(CCombStack stack) throws CatacombException {}
	}
	
	// setup
	
	@Before
	public void setup() throws Throwable {
		PolyType.polyTypes.clear();
		PolyType.registerTypes();
	}
	
	@After
	public void teardown() {
		
	}
	
	// test cases
	
	@Test
	public void test1() throws Throwable {
		SystemFunction func1 = new TestFunction(new FuncType(Arrays.asList(new PolyType("int"))));
		SystemFunction func2 = new TestFunction(new FuncType(Arrays.asList(new PolyType("double"))));
		
		FuncType composed = FunctionComposer.compose(Arrays.asList(func1, func2));
		assertEquals(new FuncType(Arrays.asList(new PolyType("int"), new PolyType("double"))), composed);
	}
	
	@Test
	public void test2() throws Throwable {
		SystemFunction func1 = new TestFunction(new FuncType(Arrays.asList(new PolyType("int"))));
		SystemFunction func2 = new TestFunction(new FuncType(Arrays.asList(new PolyType("int")), Arrays.asList(new PolyType("double"))));
		
		FuncType composed = FunctionComposer.compose(Arrays.asList(func1, func2));
		assertEquals(new FuncType(Arrays.asList(new PolyType("double"))), composed);
	}
	
	@Test
	public void test3() throws Throwable {
		SystemFunction func1 = new TestFunction(new FuncType(Arrays.asList(new PolyType("int")), Arrays.asList(new PolyType("double"))));
		SystemFunction func2 = new TestFunction(new FuncType(Arrays.asList(new PolyType("double")), Arrays.asList(new PolyType("char"))));
		
		FuncType composed = FunctionComposer.compose(Arrays.asList(func1, func2));
		assertEquals(new FuncType(Arrays.asList(new PolyType("int")), Arrays.asList(new PolyType("char"))), composed);
	}
	
	@Test
	public void test4() throws Throwable {
		SystemFunction func1 = new TestFunction(new FuncType(Arrays.asList(new PolyType("int")), Arrays.asList(new PolyType("double"))));
		SystemFunction func2 = new TestFunction(new FuncType(Arrays.asList(new PolyType("char"), new PolyType("double")), Arrays.asList(new PolyType("byte"))));
		
		FuncType composed = FunctionComposer.compose(Arrays.asList(func1, func2));
		assertEquals(new FuncType(Arrays.asList(new PolyType("char"), new PolyType("int")), Arrays.asList(new PolyType("byte"))), composed);
	}
	
	@Test
	public void test5() throws Throwable {
		SystemFunction func1 = new TestFunction(new FuncType(Arrays.asList(new PolyType("int"), new PolyType("uint")), Arrays.asList(new PolyType("double"))));
		SystemFunction func2 = new TestFunction(new FuncType(Arrays.asList(new PolyType("char"), new PolyType("double")), Arrays.asList(new PolyType("byte"))));
		
		FuncType composed = FunctionComposer.compose(Arrays.asList(func1, func2));
		assertEquals(new FuncType(Arrays.asList(new PolyType("char"), new PolyType("int"), new PolyType("uint")), Arrays.asList(new PolyType("byte"))), composed);
	}
	
	@Test// {a b c -- d e}	{f d e -- g}	{f a b c -- g}
	public void test6() throws Throwable {
		SystemFunction func1 = new TestFunction(new FuncType(Arrays.asList(new PolyType("int"), new PolyType("uint"), new PolyType("short")), Arrays.asList(new PolyType("double"), new PolyType("float"))));
		SystemFunction func2 = new TestFunction(new FuncType(Arrays.asList(new PolyType("str"), new PolyType("double"), new PolyType("float")), Arrays.asList(new PolyType("byte"))));
		
		FuncType composed = FunctionComposer.compose(Arrays.asList(func1, func2));
		assertEquals(new FuncType(Arrays.asList(new PolyType("str"), new PolyType("int"), new PolyType("uint"), new PolyType("short")), Arrays.asList(new PolyType("byte"))), composed);
	}
}
