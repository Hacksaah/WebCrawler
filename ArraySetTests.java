import java.io.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test; // fixes some compile problems with annotations

public class ArraySetTests{

  public static void main(String args[]) {
    org.junit.runner.JUnitCore.main("ArraySetTests");
  }

  @Test public void constructor1(){
    ArraySet<String> a = new ArraySet<String>();
    assertEquals(0, a.size());
    assertEquals("[]", a.toString());
  }
  @Test public void constructor2(){
    ArraySet<Integer> a = new ArraySet<Integer>();
    assertEquals(0, a.size());
  }
  @Test public void containsNothing2(){
    ArraySet<String> a = new ArraySet<String>();
    assertFalse(a.contains("hi"));
    assertFalse(a.contains("bye"));
    assertFalse(a.contains("Blackbird"));
  }

  @Test public void containsNothing1(){
    ArraySet<Integer> a = new ArraySet<Integer>();
    assertFalse(a.contains(1));
    assertFalse(a.contains(3));
    assertFalse(a.contains(20));
  }

  @Test public void addContainsGet1(){
    ArraySet<Integer> a = new ArraySet<Integer>();
    Integer [] inSet = {4, 8, 2, 6, 10, 14, 12};
    for(Integer i : inSet){
      assertTrue( a.add(i) );
    }
    String expect = "[2, 4, 6, 8, 10, 12, 14]";
    String actual = a.toString();
    String msg = "ArraySet toString wrong\n";
    msg += String.format("Expect: %s\n",expect);
    msg += String.format("Actual: %s\n",actual);
    assertEquals(msg, expect, actual);
    
    for(Integer i : inSet){
      assertTrue("Should be in set: "+i, a.contains(i) );
      assertEquals( i, a.get(i) );
    }

    Integer [] notInSet = {1, 3, 5, 9, 11, 13};
    for(Integer i : notInSet){
      assertFalse("Should not be in set: "+i, a.contains(i) );
      assertEquals( null, a.get(i) );
    }
  }

  @Test public void addContainsGet2(){
    ArraySet<String> a = new ArraySet<String>();
    String [] inSet = {"C", "B", "BZ","AAA","D","R","X","BB"};
    for(String i : inSet){
      assertTrue( a.add(i) );
    }
    String expect = "[AAA, B, BB, BZ, C, D, R, X]";
    String actual = a.toString();
    String msg = "ArraySet toString wrong\n";
    msg += String.format("Expect: %s\n",expect);
    msg += String.format("Actual: %s\n",actual);
    assertEquals(msg, expect, actual);
    
    for(String i : inSet){
      assertTrue("Should be in set: "+i, a.contains(i) );
      assertEquals( i, a.get(i) );
    }

    String [] notInSet = {"F","A","BC","BA","E","Y","W"};
    for(String i : notInSet){
      assertFalse("Should not be in set: "+i, a.contains(i) );
      assertEquals( null, a.get(i) );
    }
  }
  

  @Test public void asList1(){
    ArraySet<Integer> a = new ArraySet<Integer>();
    Integer [] inSet = {4, 8, 2, 6, 10, 14, 12};
    for(Integer i : inSet){
      assertTrue( a.add(i) );
    }

    // Remove an element from the set by accessing its internal list
    List<Integer> list = a.asList();
    list.remove(list.size()-1);
    Integer el = 14;
    assertFalse("Should not be in set: "+el, a.contains(el) );

    String expect = "[2, 4, 6, 8, 10, 12]";
    String actual = a.toString();

    String msg = "ArraySet toString wrong\n";
    msg += String.format("Expect: %s\n",expect);
    msg += String.format("Actual: %s\n",actual);
    assertEquals(msg, expect, actual);
    
    list.remove(list.size()-1);
    el = 12;
    assertFalse("Should not be in set: "+el, a.contains(el) );

    expect = "[2, 4, 6, 8, 10]";
    actual = a.toString();

    msg = "ArraySet toString wrong\n";
    msg += String.format("Expect: %s\n",expect);
    msg += String.format("Actual: %s\n",actual);
    assertEquals(msg, expect, actual);
  }

  @Test public void asList2(){
    ArraySet<String> a = new ArraySet<String>();
    String [] inSet = {"C", "B", "BZ","AAA","D","R","X","BB"};
    for(String i : inSet){
      assertTrue( a.add(i) );
    }

    // Remove an element from the set by accessing its internal list
    List<String> list = a.asList();
    list.remove(list.size()-1);
    String el = "X";
    assertFalse("Should not be in set: "+el, a.contains(el) );

    String expect = "[AAA, B, BB, BZ, C, D, R]";
    String actual = a.toString();

    String msg = "ArraySet toString wrong\n";
    msg += String.format("Expect: %s\n",expect);
    msg += String.format("Actual: %s\n",actual);
    assertEquals(msg, expect, actual);
    
    list.remove(list.size()-1);
    el = "R";
    assertFalse("Should not be in set: "+el, a.contains(el) );

    expect = "[AAA, B, BB, BZ, C, D]";
    actual = a.toString();

    msg = "ArraySet toString wrong\n";
    msg += String.format("Expect: %s\n",expect);
    msg += String.format("Actual: %s\n",actual);
    assertEquals(msg, expect, actual);
  }
  

}
