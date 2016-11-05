package info.iconmaster.ccomb.types;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import info.iconmaster.ccomb.exceptions.CatacombException;

import org.junit.Before;
import org.junit.After;

public class ConcretenessTests {
	
	// setup
	
	@Before
	public void setup() throws Throwable {
		PolyType.polyTypes.clear();
		PolyType any = PolyType.registerPolyType("any", Arrays.asList(), Arrays.asList());
		PolyType ints = PolyType.registerPolyType("ints", Arrays.asList(), Arrays.asList(any));
		PolyType aInt = PolyType.registerPolyType("int", Arrays.asList(), Arrays.asList(ints)); aInt.primitive = true;
	}
	
	@After
	public void teardown() {
		
	}
	
	// test cases
	
	@Test
	public void test1() throws Throwable {
		assertFalse(PolyType.polyTypes.get("any").isConcrete());
	}
	
	@Test
	public void test2() throws Throwable {
		assertFalse(PolyType.polyTypes.get("ints").isConcrete());
	}
	
	@Test
	public void test3() throws Throwable {
		assertTrue(PolyType.polyTypes.get("int").isConcrete());
	}
	
	@Test
	public void test4() throws Throwable {
		PolyType intsub = PolyType.registerPolyType("intsub", Arrays.asList(), Arrays.asList(PolyType.polyTypes.get("int")));
		assertTrue(intsub.isConcrete());
	}
	
	@Test
	public void test5() throws Throwable {
		assertFalse(new VarType().isConcrete());
	}
	
	@Test
	public void test6() throws Throwable {
		assertTrue(new VarType(PolyType.polyTypes.get("int")).isConcrete());
	}
	
	@Test
	public void test7() throws Throwable {
		PolyType intsub = PolyType.registerPolyType("intsub", Arrays.asList(), Arrays.asList(PolyType.polyTypes.get("int")));
		assertTrue(new VarType(intsub).isConcrete());
	}
}
