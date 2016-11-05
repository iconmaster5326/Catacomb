package info.iconmaster.ccomb.types;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import info.iconmaster.ccomb.exceptions.CatacombException;

import org.junit.Before;
import org.junit.After;

public class VarTypeTest {
	
	// setup
	
	@Before
	public void setup() throws Throwable {
		PolyType.polyTypes.clear();
		PolyType.registerPolyType("any", Arrays.asList(), Arrays.asList());
	}
	
	@After
	public void teardown() {
		
	}
	
	// test cases
	
	@Test
	public void test1() throws Throwable {
		VarType var = new VarType();
		
		assertTrue(var.equals(var));
		
		assertTrue(var.isCastableTo(var));
	}
	
	@Test
	public void test2() throws Throwable {
		VarType a = new VarType();
		VarType b = new VarType();
		
		assertTrue(a.equals(b));
		assertTrue(b.equals(a));
		
		assertTrue(a.isCastableTo(b));
		assertTrue(b.isCastableTo(a));
	}
	
	@Test
	public void test3() throws Throwable {
		PolyType any = PolyType.polyTypes.get("any");
		PolyType nums = PolyType.registerPolyType("nums", Arrays.asList(), Arrays.asList(any));
		PolyType ints = PolyType.registerPolyType("ints", Arrays.asList(), Arrays.asList(nums));
		
		VarType a = new VarType(nums);
		VarType b = new VarType(ints);
		
		assertFalse(a.equals(b));
		assertFalse(b.equals(a));
		
		assertFalse(a.isCastableTo(b));
		assertTrue(b.isCastableTo(a));
	}
	
	@Test
	public void test4() throws Throwable {
		PolyType any = PolyType.polyTypes.get("any");
		PolyType nums = PolyType.registerPolyType("nums", Arrays.asList(), Arrays.asList(any));
		PolyType ints = PolyType.registerPolyType("ints", Arrays.asList(), Arrays.asList(nums));
		PolyType reals = PolyType.registerPolyType("reals", Arrays.asList(), Arrays.asList(nums));
		
		VarType a = new VarType(ints);
		VarType b = new VarType(reals);
		
		assertFalse(a.equals(b));
		assertFalse(b.equals(a));
		
		assertFalse(a.isCastableTo(b));
		assertFalse(b.isCastableTo(a));
	}
}
