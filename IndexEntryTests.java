import java.io.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test; // fixes some compile problems with annotations

public class IndexEntryTests{

  public static void main(String args[]) {
    org.junit.runner.JUnitCore.main("IndexEntryTests");
  }

  @Test public void constructor_getTerm_1(){
    IndexEntry entry = new IndexEntry("java");
    assertEquals("java",entry.getTerm());
  }
  // Should lowercase terms
  @Test public void constructor_getTerm_2(){
    IndexEntry entry = new IndexEntry("JAVA");
    assertEquals("java",entry.getTerm());
  }
  @Test public void constructor_getTerm_3(){
    IndexEntry entry = new IndexEntry("Apple");
    assertEquals("apple",entry.getTerm());
  }
  @Test public void containsPage_1(){
    IndexEntry entry = new IndexEntry("java");
    assertFalse(entry.containsPage("page.html"));
    assertFalse(entry.containsPage("directory/nested/deeply/page.html"));
    assertFalse(entry.containsPage("http://java.com"));
  }
  @Test public void add_contains_page_1(){
    IndexEntry entry = new IndexEntry("java");
    String [] hasPages = {
      "page.html",
      "directory/nested/deeply/page.html",
      "http://java.com",
    };
    // Original add returns true
    for(String page : hasPages){
      assertTrue(entry.addPage(page));
    }
    // Now contains pages
    for(String page : hasPages){
      assertTrue(entry.containsPage(page));
    }
    // Redundant add returns false
    for(String page : hasPages){
      assertFalse(entry.addPage(page));
    }      
  }
  @Test public void add_contains_page_2(){
    IndexEntry entry = new IndexEntry("CAT");
    String [] hasPages = {
      "http://kittypics.org/old/cats.html",
      "cat.html",
      "directory/nested/deeply/page.html",
      "subdir/animals.html",
      "http://kittypics.org/cutest.html",
    };
    // Original add returns true
    for(String page : hasPages){
      assertTrue(entry.addPage(page));
    }
    // Now contains pages
    for(String page : hasPages){
      assertTrue(entry.containsPage(page));
    }
    // Redundant add returns false
    for(String page : hasPages){
      assertFalse(entry.addPage(page));
    }      
  }
  @Test public void addPage_toString_1(){
    IndexEntry entry = new IndexEntry("java");
    String [] hasPages = {
      "page.html",
      "directory/nested/deeply/page.html",
      "http://java.com",
    };
    // Original add returns true
    for(String page : hasPages){
      assertTrue(entry.addPage(page));
    }
    String expect =
      "@ java\n"+
      "directory/nested/deeply/page.html\n"+
      "http://java.com\n"+
      "page.html\n"+
      "";
    String actual = entry.toString();
    String msg = "";
    msg += "toString() incorrect\n";
    msg += String.format("EXPECT:\n%s\n",expect);
    msg += String.format("ACTUAL:\n%s\n",actual);
    assertEquals(msg,expect,actual);    
  }
  @Test public void addPage_toString_2(){
    IndexEntry entry = new IndexEntry("CAT");
    String [] hasPages = {
      "http://kittypics.org/old/cats.html",
      "cat.html",
      "directory/nested/deeply/page.html",
      "subdir/animals.html",
      "http://kittypics.org/cutest.html",
    };
    // Original add returns true
    for(String page : hasPages){
      assertTrue(entry.addPage(page));
    }
    String expect =
      "@ cat\n"+
      "cat.html\n"+
      "directory/nested/deeply/page.html\n"+
      "http://kittypics.org/cutest.html\n"+
      "http://kittypics.org/old/cats.html\n"+
      "subdir/animals.html\n"+
      "";
    String actual = entry.toString();
    String msg = "";
    msg += "toString() incorrect\n";
    msg += String.format("EXPECT:\n%s\n",expect);
    msg += String.format("ACTUAL:\n%s\n",actual);
    assertEquals(msg,expect,actual);    
  }
  @Test public void getPages_0(){
    IndexEntry entry = new IndexEntry("nothing");
    String [] hasPages = {
    };
    // Original add returns true
    for(String page : hasPages){
      assertTrue(entry.addPage(page));
    }
    String expect = "[]";
    String actual = entry.getPages().toString();
    String msg = "";
    msg += "getPages() incorrect\n";
    msg += String.format("EXPECT:\n%s\n",expect);
    msg += String.format("ACTUAL:\n%s\n",actual);
    assertEquals(msg,expect,actual);    
  }
  @Test public void getPages_1(){
    IndexEntry entry = new IndexEntry("java");
    String [] hasPages = {
      "page.html",
      "directory/nested/deeply/page.html",
      "http://java.com",
    };
    // Original add returns true
    for(String page : hasPages){
      assertTrue(entry.addPage(page));
    }
    String expect = "[directory/nested/deeply/page.html, http://java.com, page.html]";
    String actual = entry.getPages().toString();
    String msg = "";
    msg += "getPages() incorrect\n";
    msg += String.format("EXPECT:\n%s\n",expect);
    msg += String.format("ACTUAL:\n%s\n",actual);
    assertEquals(msg,expect,actual);    
  }
  @Test public void getPages_2(){
    IndexEntry entry = new IndexEntry("CAT");
    String [] hasPages = {
      "http://kittypics.org/old/cats.html",
      "cat.html",
      "directory/nested/deeply/page.html",
      "subdir/animals.html",
      "http://kittypics.org/cutest.html",
    };
    // Original add returns true
    for(String page : hasPages){
      assertTrue(entry.addPage(page));
    }
    String expect = "[cat.html, directory/nested/deeply/page.html, http://kittypics.org/cutest.html, http://kittypics.org/old/cats.html, subdir/animals.html]";
    String actual = entry.getPages().toString();
    String msg = "";
    msg += "getPages() incorrect\n";
    msg += String.format("EXPECT:\n%s\n",expect);
    msg += String.format("ACTUAL:\n%s\n",actual);
    assertEquals(msg,expect,actual);    
  }
  @Test public void equality_1(){
    IndexEntry entry1 = new IndexEntry("java");
    IndexEntry entry2 = new IndexEntry("java");
    assertTrue(entry1.equals(entry2));
    assertTrue(entry2.equals(entry1));
  }
  // Equality does not depend on any pages
  @Test public void equality_2(){
    IndexEntry entry1 = new IndexEntry("java");
    IndexEntry entry2 = new IndexEntry("JAVA");
    entry1.addPage("http://java.com");
    entry1.addPage("javapage.html");
    assertTrue(entry1.equals(entry2));
    assertTrue(entry2.equals(entry1));
    entry2.addPage("somedir/page.html");
    assertTrue(entry1.equals(entry2));
    assertTrue(entry2.equals(entry1));
  }
  @Test public void equality_3(){
    IndexEntry entry1 = new IndexEntry("java");
    IndexEntry entry2 = new IndexEntry("Cat");
    assertFalse(entry1.equals(entry2));
    assertFalse(entry2.equals(entry1));
    entry1.addPage("http://java.com");
    entry2.addPage("somedir/page.html");
    assertFalse(entry1.equals(entry2));
    assertFalse(entry2.equals(entry1));
  }
  @Test public void equality_4(){
    IndexEntry entry1 = new IndexEntry("java");
    String str = "java";
    assertFalse(entry1.equals(str));
    assertFalse(str.equals(entry1));
    entry1.addPage("http://java.com");
    entry1.addPage("somedir/page.html");
    assertFalse(entry1.equals(str));
    assertFalse(str.equals(entry1));
  }
  @Test public void comparable_1(){
    IndexEntry entry1 = new IndexEntry("java");
    IndexEntry entry2 = new IndexEntry("cat");
    assertTrue( entry1.compareTo(entry2) > 0 );
    assertTrue( entry2.compareTo(entry1) < 0 );
  }
  @Test public void comparable_2(){
    IndexEntry entry1 = new IndexEntry("JAVA");
    IndexEntry entry2 = new IndexEntry("cat");
    assertTrue( entry1.compareTo(entry2) > 0 );
    assertTrue( entry2.compareTo(entry1) < 0 );
  }
  @Test public void comparable_3(){
    IndexEntry entry1 = new IndexEntry("java");
    IndexEntry entry2 = new IndexEntry("Java");
    assertTrue( entry1.compareTo(entry2) == 0 );
    assertTrue( entry2.compareTo(entry1) == 0 );
  }
  @Test public void comparable_sorting_1(){
    IndexEntry [] entries = {
      new IndexEntry("java"),
      new IndexEntry("cat"),
      new IndexEntry("APPLE"),
      new IndexEntry("ocaml"),
      new IndexEntry("zebra"),
      new IndexEntry("Lisp"),
    };
    Arrays.sort(entries);
    String expect =
      "[@ apple\n"+
      ", @ cat\n"+
      ", @ java\n"+
      ", @ lisp\n"+
      ", @ ocaml\n"+
      ", @ zebra\n"+
      "]"+
      "";
    String actual = Arrays.toString(entries);
    String msg = "";
    msg += "sorting incorrect: check compareToMethod()\n";
    msg += String.format("EXPECT:\n%s\n",expect);
    msg += String.format("ACTUAL:\n%s\n",actual);
    assertEquals(msg,expect,actual);    
  }
  @Test public void comparable_sorting_2(){
    IndexEntry [] entries = {
      new IndexEntry("java"),
      new IndexEntry("cat"),
      new IndexEntry("APPLE"),
      new IndexEntry("ocaml"),
      new IndexEntry("zebra"),
      new IndexEntry("Lisp"),
    };
    entries[0].addPage("javaPage.html");
    entries[0].addPage("another/javaPage.html");
    entries[1].addPage("http:/cutecats.com/cats.html");
    entries[3].addPage("http://ocaml.org/");
    entries[5].addPage("https://clojure.org/");
    entries[5].addPage("https://en.wikipedia.org/wiki/Clojure");
    Arrays.sort(entries);
    String expect =
      "@ apple\n"+
      "@ cat\n"+
      "http:/cutecats.com/cats.html\n"+
      "@ java\n"+
      "another/javaPage.html\n"+
      "javaPage.html\n"+
      "@ lisp\n"+
      "https://clojure.org/\n"+
      "https://en.wikipedia.org/wiki/Clojure\n"+
      "@ ocaml\n"+
      "http://ocaml.org/\n"+
      "@ zebra\n"+
      "";
    String actual = Arrays.toString(entries).replaceAll("\\[|, ","").replaceAll("]$","");
    String msg = "";
    msg += "sorting incorrect: check compareToMethod()\n";
    msg += String.format("EXPECT:\n%s\n",expect);
    msg += String.format("ACTUAL:\n%s\n",actual);
    assertEquals(msg,expect,actual);    
  }
  

  

}
