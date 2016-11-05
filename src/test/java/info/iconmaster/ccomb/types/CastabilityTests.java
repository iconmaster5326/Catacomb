package info.iconmaster.ccomb.types;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import info.iconmaster.ccomb.exceptions.CatacombException;

import org.junit.Before;
import org.junit.After;

public class CastabilityTests {
	
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
		PolyType any = PolyType.polyTypes.get("any");
		
		assertTrue(any.equals(any));
		assertTrue(any.isCastableTo(any));
	}
	
	@Test
	public void test2() throws Throwable {
		PolyType any = PolyType.polyTypes.get("any");
		PolyType other = new PolyType("any");
		
		assertTrue(any.equals(other));
		assertTrue(any.isCastableTo(other));
	}
	
	@Test
	public void test3() throws Throwable {
		PolyType any = PolyType.polyTypes.get("any");
		PolyType nums = PolyType.registerPolyType("nums", Arrays.asList(), Arrays.asList(any));
		PolyType ints = PolyType.registerPolyType("ints", Arrays.asList(), Arrays.asList(nums));
		PolyType aInt = PolyType.registerPolyType("int", Arrays.asList(), Arrays.asList(ints));
		
		assertTrue(aInt.isCastableTo(nums));
	}
	
	@Test
	public void test4() throws Throwable {
		PolyType any = PolyType.polyTypes.get("any");
		PolyType nums = PolyType.registerPolyType("nums", Arrays.asList(), Arrays.asList(any));
		PolyType ints = PolyType.registerPolyType("ints", Arrays.asList(), Arrays.asList(nums));
		PolyType reals = PolyType.registerPolyType("reals", Arrays.asList(), Arrays.asList(nums));
		
		assertTrue(ints.isCastableTo(nums));
		assertTrue(reals.isCastableTo(nums));
		
		assertFalse(reals.isCastableTo(ints));
		assertFalse(ints.isCastableTo(reals));
	}
	
	@Test
	public void test5() throws Throwable {
		try {
			PolyType.registerPolyType("any", Arrays.asList(), Arrays.asList());
		} catch (CatacombException ex) {
			return;
		}
		
		throw new CatacombException("no error was thrown");
	}
	
	@Test
	public void test6() throws Throwable {
		PolyType any = PolyType.polyTypes.get("any");
		VarType t = new VarType("T");
		
		PolyType arrays = PolyType.registerPolyType("arrays", Arrays.asList(t), Arrays.asList(any));
		PolyType array = PolyType.registerPolyType("array", Arrays.asList(t), Arrays.asList(arrays));
		
		assertFalse(arrays.isCastableTo(array));
		assertTrue(array.isCastableTo(arrays));
	}
	
	@Test
	public void test7() throws Throwable {
		PolyType any = PolyType.polyTypes.get("any");
		VarType a = new VarType("A");
		VarType b = new VarType("B");
		
		PolyType arrays = PolyType.registerPolyType("arrays", Arrays.asList(a), Arrays.asList(any));
		PolyType array = PolyType.registerPolyType("array", Arrays.asList(b), Arrays.asList(new PolyType(arrays, Arrays.asList(b))));
		
		assertFalse(arrays.isCastableTo(array));
		assertTrue(array.isCastableTo(arrays));
	}
	
	@Test
	public void test8() throws Throwable {
		PolyType any = PolyType.polyTypes.get("any");
		VarType.TypeGroup g = new VarType.TypeGroup();
		VarType a = new VarType("A", g);
		VarType b = new VarType("B", g);
		
		PolyType arrays = PolyType.registerPolyType("arrays", Arrays.asList(a), Arrays.asList(any));
		PolyType array = PolyType.registerPolyType("array", Arrays.asList(b), Arrays.asList(arrays));
		
		assertFalse(arrays.isCastableTo(array));
		assertTrue(array.isCastableTo(arrays));
	}
	
	@Test
	public void test9() throws Throwable {
		VarType var = new VarType();
		
		assertTrue(var.equals(var));
		
		assertTrue(var.isCastableTo(var));
	}
	
	@Test
	public void test10() throws Throwable {
		VarType a = new VarType();
		VarType b = new VarType();
		
		assertTrue(a.equals(b));
		assertTrue(b.equals(a));
		
		assertTrue(a.isCastableTo(b));
		assertTrue(b.isCastableTo(a));
	}
	
	@Test
	public void test11() throws Throwable {
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
	public void test12() throws Throwable {
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
	
	@Test
	public void test13() throws Throwable {
		PolyType any = PolyType.polyTypes.get("any");
		VarType a = new VarType("A");
		VarType b = new VarType("B");
		
		PolyType arrays = PolyType.registerPolyType("arrays", Arrays.asList(a), Arrays.asList(any));
		PolyType array = PolyType.registerPolyType("array", Arrays.asList(b), Arrays.asList(arrays));
		
		assertFalse(arrays.isCastableTo(array));
		assertTrue(array.isCastableTo(arrays));
	}
	
	@Test
	public void test14() throws Throwable {
		PolyType any = PolyType.polyTypes.get("any");
		VarType a = new VarType("A");
		PolyType b = PolyType.registerPolyType("b", Arrays.asList(), Arrays.asList(any));
		
		PolyType.registerPolyType("array", Arrays.asList(a), Arrays.asList(any));
		
		PolyType arrayA = new PolyType("array", Arrays.asList(a));
		PolyType arrayB = new PolyType("array", Arrays.asList(b));
		
		assertFalse(arrayA.isCastableTo(arrayB));
		assertTrue(arrayB.isCastableTo(arrayA));
	}
}
