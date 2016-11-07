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
	
	@Test // {-- a} && {-- b} ==> {-- a b}
	public void test1() throws Throwable {
		SystemFunction func1 = new TestFunction(new FuncType(Arrays.asList(new PolyType("int"))));
		SystemFunction func2 = new TestFunction(new FuncType(Arrays.asList(new PolyType("double"))));
		
		FuncType composed = FunctionComposer.compose(Arrays.asList(func1, func2));
		assertEquals(new FuncType(Arrays.asList(new PolyType("int"), new PolyType("double"))), composed);
	}
	
	@Test // {-- a} && {a -- b} ==> {-- b}
	public void test2() throws Throwable {
		SystemFunction func1 = new TestFunction(new FuncType(Arrays.asList(new PolyType("int"))));
		SystemFunction func2 = new TestFunction(new FuncType(Arrays.asList(new PolyType("int")), Arrays.asList(new PolyType("double"))));
		
		FuncType composed = FunctionComposer.compose(Arrays.asList(func1, func2));
		assertEquals(new FuncType(Arrays.asList(new PolyType("double"))), composed);
	}
	
	@Test // {a -- b} && {b -- c} ==> {a -- c}
	public void test3() throws Throwable {
		SystemFunction func1 = new TestFunction(new FuncType(Arrays.asList(new PolyType("int")), Arrays.asList(new PolyType("double"))));
		SystemFunction func2 = new TestFunction(new FuncType(Arrays.asList(new PolyType("double")), Arrays.asList(new PolyType("char"))));
		
		FuncType composed = FunctionComposer.compose(Arrays.asList(func1, func2));
		assertEquals(new FuncType(Arrays.asList(new PolyType("int")), Arrays.asList(new PolyType("char"))), composed);
	}
	
	@Test // {a -- b} && {c b -- d} ==> {c a -- d}
	public void test4() throws Throwable {
		SystemFunction func1 = new TestFunction(new FuncType(Arrays.asList(new PolyType("int")), Arrays.asList(new PolyType("double"))));
		SystemFunction func2 = new TestFunction(new FuncType(Arrays.asList(new PolyType("char"), new PolyType("double")), Arrays.asList(new PolyType("byte"))));
		
		FuncType composed = FunctionComposer.compose(Arrays.asList(func1, func2));
		assertEquals(new FuncType(Arrays.asList(new PolyType("char"), new PolyType("int")), Arrays.asList(new PolyType("byte"))), composed);
	}
	
	@Test // {a b -- c} && {d c -- e} ==> {d a b -- e}
	public void test5() throws Throwable {
		SystemFunction func1 = new TestFunction(new FuncType(Arrays.asList(new PolyType("int"), new PolyType("uint")), Arrays.asList(new PolyType("double"))));
		SystemFunction func2 = new TestFunction(new FuncType(Arrays.asList(new PolyType("char"), new PolyType("double")), Arrays.asList(new PolyType("byte"))));
		
		FuncType composed = FunctionComposer.compose(Arrays.asList(func1, func2));
		assertEquals(new FuncType(Arrays.asList(new PolyType("char"), new PolyType("int"), new PolyType("uint")), Arrays.asList(new PolyType("byte"))), composed);
	}
	
	@Test // {a b c -- d e} && {f d e -- g} ==> {f a b c -- g}
	public void test6() throws Throwable {
		SystemFunction func1 = new TestFunction(new FuncType(Arrays.asList(new PolyType("int"), new PolyType("uint"), new PolyType("short")), Arrays.asList(new PolyType("double"), new PolyType("float"))));
		SystemFunction func2 = new TestFunction(new FuncType(Arrays.asList(new PolyType("str"), new PolyType("double"), new PolyType("float")), Arrays.asList(new PolyType("byte"))));
		
		FuncType composed = FunctionComposer.compose(Arrays.asList(func1, func2));
		assertEquals(new FuncType(Arrays.asList(new PolyType("str"), new PolyType("int"), new PolyType("uint"), new PolyType("short")), Arrays.asList(new PolyType("byte"))), composed);
	}
	
	@Test // { -- int} && {'A -- 'A} ==> { -- int}
	public void test7() throws Throwable {
		VarType a = new VarType("A");
		
		SystemFunction func1 = new TestFunction(new FuncType(Arrays.asList(), Arrays.asList(new PolyType("int"))));
		SystemFunction func2 = new TestFunction(new FuncType(Arrays.asList(a), Arrays.asList(a)));
		
		FuncType composed = FunctionComposer.compose(Arrays.asList(func1, func2));
		assertEquals(new FuncType(Arrays.asList(), Arrays.asList(new PolyType("int"))), composed);
	}
	
	@Test // { -- int} && {'A 'B -- 'B 'A} ==> {'A -- int 'A}
	public void test8() throws Throwable {
		VarType a = new VarType("A");
		VarType b = new VarType("B");
		
		SystemFunction func1 = new TestFunction(new FuncType(Arrays.asList(), Arrays.asList(new PolyType("int"))));
		SystemFunction func2 = new TestFunction(new FuncType(Arrays.asList(a, b), Arrays.asList(b, a)));
		
		FuncType composed = FunctionComposer.compose(Arrays.asList(func1, func2));
		assertEquals(new FuncType(Arrays.asList(a), Arrays.asList(new PolyType("int"), a)), composed);
	}
	
	@Test // { -- int} && {'A 'A -- 'A} ==> {int -- int}
	public void test9() throws Throwable {
		VarType a = new VarType("A");
		
		SystemFunction func1 = new TestFunction(new FuncType(Arrays.asList(), Arrays.asList(new PolyType("int"))));
		SystemFunction func2 = new TestFunction(new FuncType(Arrays.asList(a, a), Arrays.asList(a)));
		
		FuncType composed = FunctionComposer.compose(Arrays.asList(func1, func2));
		assertEquals(new FuncType(Arrays.asList(new PolyType("int")), Arrays.asList(new PolyType("int"))), composed);
	}
}
