package info.iconmaster.ccomb.types;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import info.iconmaster.ccomb.exceptions.CatacombException;

import org.junit.Before;
import org.junit.After;

public class PolyTypeTest {
	
	// setup
	
	@Before
	public void setup() {
		PolyType.polyTypes.clear();
	}
	
	@After
	public void teardown() {
		
	}
	
	// test cases
	
	@Test
	public void test1() throws Throwable {
		PolyType any = PolyType.registerPolyType("any", Arrays.asList(), Arrays.asList());
		
		assertTrue(any.equals(any));
		assertTrue(any.isCastableTo(any));
	}
	
	@Test
	public void test2() throws Throwable {
		PolyType any = PolyType.registerPolyType("any", Arrays.asList(), Arrays.asList());
		PolyType other = new PolyType("any");
		
		assertTrue(any.equals(other));
		assertTrue(any.isCastableTo(other));
	}
	
	@Test
	public void test3() throws Throwable {
		PolyType any = PolyType.registerPolyType("any", Arrays.asList(), Arrays.asList());
		PolyType nums = PolyType.registerPolyType("nums", Arrays.asList(), Arrays.asList(any));
		PolyType ints = PolyType.registerPolyType("ints", Arrays.asList(), Arrays.asList(nums));
		PolyType aInt = PolyType.registerPolyType("int", Arrays.asList(), Arrays.asList(ints));
		
		assertTrue(aInt.isCastableTo(nums));
	}
	
	@Test
	public void test4() throws Throwable {
		PolyType any = PolyType.registerPolyType("any", Arrays.asList(), Arrays.asList());
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
		PolyType.registerPolyType("any", Arrays.asList(), Arrays.asList());
		
		try {
			PolyType.registerPolyType("any", Arrays.asList(), Arrays.asList());
		} catch (CatacombException ex) {
			return;
		}
		
		throw new CatacombException("no error was thrown");
	}
}