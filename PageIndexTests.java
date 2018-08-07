import java.io.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test; // fixes some compile problems with annotations

public class PageIndexTests{

  public static void main(String args[]) {
    org.junit.runner.JUnitCore.main("PageIndexTests");
  }

  @Test public void constructor_size_1(){
    PageIndex index = new PageIndex();
    assertEquals(0,index.size());
  }

  // Utility to check the string output and size of an index
  public static void check_index(PageIndex index, String expect, int expectSize){
    String actual = index.toString();
    String msg = "";
    msg += "PageIndex toString() wrong\n";
    msg += String.format("Expect:\n%s\n",expect);
    msg += String.format("Actual:\n%s\n",actual);
    assertEquals(msg,expect,actual);
    assertEquals(expectSize,index.size());    
  }
  @Test public void constructor_toString_1(){
    PageIndex index = new PageIndex();
    String expect =
      "INDEX: 0 entries\n"+
      "--------------------\n"+
      "";
    check_index(index,expect,0);
  }
  @Test public void add_toString_1(){
    PageIndex index = new PageIndex();
    assertTrue( index.addTermAndPage("java","A.html") );
    String expect =
      "INDEX: 1 entries\n"+
      "--------------------\n"+
      "@ java\n"+
      "A.html\n"+
      "";
    check_index(index,expect,1);
  }
  @Test public void add_toString_2(){
    PageIndex index = new PageIndex();
    assertTrue( index.addTermAndPage("java","A.html") );
    assertTrue( index.addTermAndPage("c++","C.html") );
    assertTrue( index.addTermAndPage("rlang","R.html") );
    assertTrue( index.addTermAndPage("dlang","d.html") );
    String expect =
      "INDEX: 4 entries\n"+
      "--------------------\n"+
      "@ c++\n"+
      "C.html\n"+
      "@ dlang\n"+
      "d.html\n"+
      "@ java\n"+
      "A.html\n"+
      "@ rlang\n"+
      "R.html\n"+
      "";
    check_index(index,expect,4);
  }
  @Test public void add_toString_3(){
    PageIndex index = new PageIndex();
    index.addTermAndPage("java","A.html");
    index.addTermAndPage("c++","C.html");
    index.addTermAndPage("rlang","R.html");
    index.addTermAndPage("dlang","d.html");
    index.addTermAndPage("dlang","https://dlang.org");
    index.addTermAndPage("rlang","https://www.r-project.org");
    index.addTermAndPage("java","https://en.wikipedia.org/wiki/Java_(programming_language)");
    index.addTermAndPage("java","http://docs.oracle.com/javase/8/docs/technotes/guides/language/index.html");
    String expect =
      "INDEX: 4 entries\n"+
      "--------------------\n"+
      "@ c++\n"+
      "C.html\n"+
      "@ dlang\n"+
      "d.html\n"+
      "https://dlang.org\n"+
      "@ java\n"+
      "A.html\n"+
      "http://docs.oracle.com/javase/8/docs/technotes/guides/language/index.html\n"+
      "https://en.wikipedia.org/wiki/Java_(programming_language)\n"+
      "@ rlang\n"+
      "R.html\n"+
      "https://www.r-project.org\n"+
      "";
    check_index(index,expect,4);
  }
  @Test public void add_noRepeat_3(){
    PageIndex index = new PageIndex();
    assertTrue( index.addTermAndPage("java","A.html") );
    assertFalse( index.addTermAndPage("java","A.html") );
    assertTrue( index.addTermAndPage("c++","C.html") );
    assertTrue( index.addTermAndPage("rlang","R.html") );
    assertTrue( index.addTermAndPage("dlang","d.html") );
    assertTrue( index.addTermAndPage("dlang","https://dlang.org") );
    assertFalse( index.addTermAndPage("dlang","https://dlang.org") );
    assertTrue( index.addTermAndPage("rlang","https://www.r-project.org") );
    assertTrue( index.addTermAndPage("java","https://en.wikipedia.org/wiki/Java_(programming_language)") );
    assertTrue( index.addTermAndPage("java","http://docs.oracle.com/javase/8/docs/technotes/guides/language/index.html") );
    assertFalse( index.addTermAndPage("java","https://en.wikipedia.org/wiki/Java_(programming_language)") );
    assertFalse( index.addTermAndPage("dlang","https://dlang.org") );
    assertFalse( index.addTermAndPage("dlang","d.html") );
    assertFalse( index.addTermAndPage("java","https://en.wikipedia.org/wiki/Java_(programming_language)") );
    assertFalse( index.addTermAndPage("java","http://docs.oracle.com/javase/8/docs/technotes/guides/language/index.html") );
    assertFalse( index.addTermAndPage("java","A.html") );
    String expect =
      "INDEX: 4 entries\n"+
      "--------------------\n"+
      "@ c++\n"+
      "C.html\n"+
      "@ dlang\n"+
      "d.html\n"+
      "https://dlang.org\n"+
      "@ java\n"+
      "A.html\n"+
      "http://docs.oracle.com/javase/8/docs/technotes/guides/language/index.html\n"+
      "https://en.wikipedia.org/wiki/Java_(programming_language)\n"+
      "@ rlang\n"+
      "R.html\n"+
      "https://www.r-project.org\n"+
      "";
    check_index(index,expect,4);
  }
  @Test public void add_containsTerm_1(){
    PageIndex index = new PageIndex();
    index.addTermAndPage("java","A.html");
    assertTrue(index.containsTerm("java"));
    assertFalse(index.containsTerm("c++"));
    assertFalse(index.containsTerm("dlang"));
    assertFalse(index.containsTerm("rlang"));
  }
  @Test public void add_containsTerm_2(){
    PageIndex index = new PageIndex();
    index.addTermAndPage("java","A.html");
    index.addTermAndPage("c++","C.html");
    index.addTermAndPage("rlang","R.html");
    index.addTermAndPage("dlang","d.html");
    assertTrue(index.containsTerm("java"));
    assertTrue(index.containsTerm("c++"));
    assertTrue(index.containsTerm("dlang"));
    assertTrue(index.containsTerm("rlang"));
  }
  @Test public void getPagesWithTerm_1(){
    PageIndex index = new PageIndex();
    index.addTermAndPage("java","A.html");
    assertEquals("[A.html]",index.getPagesWithTerm("java").toString());
    assertEquals("[]",index.getPagesWithTerm("dlang").toString());
    assertEquals("[]",index.getPagesWithTerm("clojure").toString());
  }
  @Test public void getPagesWithTerm_2(){
    PageIndex index = new PageIndex();
    index.addTermAndPage("java","A.html");
    index.addTermAndPage("c++","C.html");
    index.addTermAndPage("rlang","R.html");
    index.addTermAndPage("dlang","d.html");
    index.addTermAndPage("dlang","https://dlang.org");
    index.addTermAndPage("rlang","https://www.r-project.org");
    index.addTermAndPage("java","https://en.wikipedia.org/wiki/Java_(programming_language)");
    index.addTermAndPage("java","xdir/Z.html");
    index.addTermAndPage("java","http://docs.oracle.com/javase/8/docs/technotes/guides/language/index.html");
    assertEquals("[A.html, http://docs.oracle.com/javase/8/docs/technotes/guides/language/index.html, https://en.wikipedia.org/wiki/Java_(programming_language), xdir/Z.html]",
                 index.getPagesWithTerm("java").toString());
    assertEquals("[R.html, https://www.r-project.org]",
                 index.getPagesWithTerm("rlang").toString());
    assertEquals("[d.html, https://dlang.org]",
                 index.getPagesWithTerm("dlang").toString());
    assertEquals("[C.html]",
                 index.getPagesWithTerm("c++").toString());
    assertEquals("[]",
                 index.getPagesWithTerm("clojure").toString());
  }
  @Test public void add_containsTerm_3(){
    PageIndex index = new PageIndex();
    assertTrue( index.addTermAndPage("java","A.html") );
    assertTrue( index.addTermAndPage("c++","C.html") );
    assertTrue( index.addTermAndPage("rlang","R.html") );
    assertTrue( index.addTermAndPage("dlang","d.html") );
    // Second adds are false - same page
    assertFalse( index.addTermAndPage("java","A.html") );
    assertFalse( index.addTermAndPage("c++","C.html") );
    assertFalse( index.addTermAndPage("rlang","R.html") );
    assertFalse( index.addTermAndPage("dlang","d.html") );
    // New pages
    assertTrue( index.addTermAndPage("java","J.html") );
    assertTrue( index.addTermAndPage("c++","CC.html") );
    assertTrue( index.addTermAndPage("rlang","rlang.html") );
    assertTrue( index.addTermAndPage("dlang","dir/d.html") );
    // Second adds are false - same page, vary term caps
    assertFalse( index.addTermAndPage("Java","A.html") );
    assertFalse( index.addTermAndPage("C++","C.html") );
    assertFalse( index.addTermAndPage("RLANG","R.html") );
    assertFalse( index.addTermAndPage("dLang","d.html") );
  }
  @Test public void validTerm_1(){
    PageIndex index = new PageIndex();
    assertFalse( index.validTerm("the") );
    assertFalse( index.validTerm("a") );
    assertFalse( index.validTerm("other") );
    assertFalse( index.validTerm("with") );
  }  
  @Test public void validTerm_2(){
    PageIndex index = new PageIndex();
    assertFalse( index.validTerm("The") );
    assertFalse( index.validTerm("A") );
    assertFalse( index.validTerm("OTHER") );
    assertFalse( index.validTerm("wiTH") );
  }  
  @Test public void ignore_invalid_1(){
    PageIndex index = new PageIndex();
    assertFalse( index.addTermAndPage("the","ignore.html") );
    assertFalse( index.addTermAndPage("a","ignore.html") );
    assertFalse( index.addTermAndPage("With","http://wrong.com/nope.html") );
    assertFalse( index.addTermAndPage("ARE","http://wrong.com/nope.html") );
    String expect =
      "INDEX: 0 entries\n"+
      "--------------------\n"+
      "";
    check_index(index,expect,0);
  }
  @Test public void ignore_invalid_2(){
    PageIndex index = new PageIndex();
    index.addTermAndPage("java","A.html");
    assertFalse( index.addTermAndPage("where","http://wrong.com/nope.html") );
    index.addTermAndPage("c++","C.html");
    index.addTermAndPage("rlang","R.html");
    assertFalse( index.addTermAndPage("with","http://wrong.com/nope.html") );
    index.addTermAndPage("dlang","d.html");
    index.addTermAndPage("dlang","https://dlang.org");
    assertFalse( index.addTermAndPage("about","ignore.html") );
    index.addTermAndPage("rlang","https://www.r-project.org");
    index.addTermAndPage("java","https://en.wikipedia.org/wiki/Java_(programming_language)");
    assertFalse( index.addTermAndPage("IF","nada.html") );
    index.addTermAndPage("java","xdir/Z.html");
    index.addTermAndPage("java","http://docs.oracle.com/javase/8/docs/technotes/guides/language/index.html");
    String expect =
      "INDEX: 4 entries\n"+
      "--------------------\n"+
      "@ c++\n"+
      "C.html\n"+
      "@ dlang\n"+
      "d.html\n"+
      "https://dlang.org\n"+
      "@ java\n"+
      "A.html\n"+
      "http://docs.oracle.com/javase/8/docs/technotes/guides/language/index.html\n"+
      "https://en.wikipedia.org/wiki/Java_(programming_language)\n"+
      "xdir/Z.html\n"+
      "@ rlang\n"+
      "R.html\n"+
      "https://www.r-project.org\n"+
      "";
    check_index(index,expect,4);
  }
  @Test public void addCrawled_1(){
    PageIndex index = new PageIndex();
    String [] found = {};
    String [] skipped = {};
    Crawler c = new MockCrawler(found,skipped);
    index.addCrawledPages(c);
    String expect =
      "INDEX: 0 entries\n"+
      "--------------------\n"+
      "";
    check_index(index,expect,0);
  }    
  @Test public void addCrawled_2(){
    PageIndex index = new PageIndex();
    String [] found = {"crawls/small-site/start.html"};
    String [] skipped = {"http://x.com/X.html","spider-man.png","javascript:alert('An alert');"};
    Crawler c = new MockCrawler(found,skipped);
    index.addCrawledPages(c);
    String expect =
      "INDEX: 14 entries\n"+
      "--------------------\n"+
      "@ crawled\n"+
      "crawls/small-site/start.html\n"+
      "@ crawler\n"+
      "crawls/small-site/start.html\n"+
      "@ finally\n"+
      "crawls/small-site/start.html\n"+
      "@ link\n"+
      "crawls/small-site/start.html\n"+
      "@ links\n"+
      "crawls/small-site/start.html\n"+
      "@ original\n"+
      "crawls/small-site/start.html\n"+
      "@ page\n"+
      "crawls/small-site/start.html\n"+
      "@ pages\n"+
      "crawls/small-site/start.html\n"+
      "@ scattered\n"+
      "crawls/small-site/start.html\n"+
      "@ site\n"+
      "crawls/small-site/start.html\n"+
      "@ starting\n"+
      "crawls/small-site/start.html\n"+
      "@ subdirectory\n"+
      "crawls/small-site/start.html\n"+
      "@ test\n"+
      "crawls/small-site/start.html\n"+
      "@ web\n"+
      "crawls/small-site/start.html\n"+
      "";
    check_index(index,expect,14);
  }    
  @Test public void addCrawled_3(){
    PageIndex index = new PageIndex();
    String [] found = {"crawls/small-site/start.html","crawls/small-site/A.html"};
    String [] skipped = {"http://x.com/X.html","spider-man.png","javascript:alert('An alert');"};
    Crawler c = new MockCrawler(found,skipped);
    index.addCrawledPages(c);
    String expect =
      "INDEX: 20 entries\n"+
      "--------------------\n"+
      "@ acrobat\n"+
      "crawls/small-site/A.html\n"+
      "@ argyle\n"+
      "crawls/small-site/A.html\n"+
      "@ bread\n"+
      "crawls/small-site/A.html\n"+
      "@ crawled\n"+
      "crawls/small-site/A.html\n"+
      "crawls/small-site/start.html\n"+
      "@ crawler\n"+
      "crawls/small-site/start.html\n"+
      "@ cs\n"+
      "crawls/small-site/A.html\n"+
      "@ finally\n"+
      "crawls/small-site/start.html\n"+
      "@ homepage\n"+
      "crawls/small-site/A.html\n"+
      "@ keywords\n"+
      "crawls/small-site/A.html\n"+
      "@ link\n"+
      "crawls/small-site/A.html\n"+
      "crawls/small-site/start.html\n"+
      "@ links\n"+
      "crawls/small-site/start.html\n"+
      "@ original\n"+
      "crawls/small-site/start.html\n"+
      "@ page\n"+
      "crawls/small-site/A.html\n"+
      "crawls/small-site/start.html\n"+
      "@ pages\n"+
      "crawls/small-site/start.html\n"+
      "@ scattered\n"+
      "crawls/small-site/start.html\n"+
      "@ site\n"+
      "crawls/small-site/start.html\n"+
      "@ starting\n"+
      "crawls/small-site/start.html\n"+
      "@ subdirectory\n"+
      "crawls/small-site/start.html\n"+
      "@ test\n"+
      "crawls/small-site/start.html\n"+
      "@ web\n"+
      "crawls/small-site/start.html\n"+
      "";
    check_index(index,expect,20);
  }    
  @Test public void addCrawled_4(){
    PageIndex index = new PageIndex();
    Crawler c = new RecursiveCrawler();
    c.crawl("crawls/small-site/C.html");
    index.addCrawledPages(c);
    String expect =
      "INDEX: 6 entries\n"+
      "--------------------\n"+
      "@ bread\n"+
      "crawls/small-site/C.html\n"+
      "@ champagne\n"+
      "crawls/small-site/C.html\n"+
      "@ cheese\n"+
      "crawls/small-site/C.html\n"+
      "@ daring\n"+
      "crawls/small-site/C.html\n"+
      "@ keywords\n"+
      "crawls/small-site/C.html\n"+
      "@ page\n"+
      "crawls/small-site/C.html\n"+
      "";
    check_index(index,expect,6);
  }    
  @Test public void addCrawled_5(){
    PageIndex index = new PageIndex();
    Crawler c = new RecursiveCrawler();
    c.crawl("crawls/small-site/start.html");
    index.addCrawledPages(c);
    String expect =
      "INDEX: 37 entries\n"+
      "--------------------\n"+
      "@ acrobat\n"+
      "crawls/small-site/A.html\n"+
      "crawls/small-site/subdir/E.html\n"+
      "crawls/small-site/subdir/F.html\n"+
      "@ alert\n"+
      "crawls/small-site/B.html\n"+
      "@ argyle\n"+
      "crawls/small-site/A.html\n"+
      "@ bored\n"+
      "crawls/small-site/B.html\n"+
      "@ bread\n"+
      "crawls/small-site/A.html\n"+
      "crawls/small-site/B.html\n"+
      "crawls/small-site/C.html\n"+
      "@ champagne\n"+
      "crawls/small-site/C.html\n"+
      "crawls/small-site/subdir/D.html\n"+
      "@ cheese\n"+
      "crawls/small-site/C.html\n"+
      "@ crawl\n"+
      "crawls/small-site/B.html\n"+
      "@ crawled\n"+
      "crawls/small-site/A.html\n"+
      "crawls/small-site/start.html\n"+
      "@ crawler\n"+
      "crawls/small-site/start.html\n"+
      "@ cs\n"+
      "crawls/small-site/A.html\n"+
      "@ daring\n"+
      "crawls/small-site/C.html\n"+
      "crawls/small-site/subdir/D.html\n"+
      "@ dead\n"+
      "crawls/small-site/subdir/E.html\n"+
      "@ directory\n"+
      "crawls/small-site/subdir/D.html\n"+
      "crawls/small-site/subdir/E.html\n"+
      "@ don\n"+
      "crawls/small-site/B.html\n"+
      "@ dreadful\n"+
      "crawls/small-site/subdir/D.html\n"+
      "@ end\n"+
      "crawls/small-site/subdir/E.html\n"+
      "@ exquisite\n"+
      "crawls/small-site/subdir/E.html\n"+
      "@ final\n"+
      "crawls/small-site/subdir/F.html\n"+
      "@ finally\n"+
      "crawls/small-site/start.html\n"+
      "@ graphic\n"+
      "crawls/small-site/subdir/F.html\n"+
      "@ gratuitous\n"+
      "crawls/small-site/subdir/F.html\n"+
      "@ homepage\n"+
      "crawls/small-site/A.html\n"+
      "@ keywords\n"+
      "crawls/small-site/A.html\n"+
      "crawls/small-site/B.html\n"+
      "crawls/small-site/C.html\n"+
      "crawls/small-site/subdir/D.html\n"+
      "crawls/small-site/subdir/E.html\n"+
      "crawls/small-site/subdir/F.html\n"+
      "@ link\n"+
      "crawls/small-site/A.html\n"+
      "crawls/small-site/B.html\n"+
      "crawls/small-site/start.html\n"+
      "crawls/small-site/subdir/D.html\n"+
      "crawls/small-site/subdir/E.html\n"+
      "@ links\n"+
      "crawls/small-site/start.html\n"+
      "@ original\n"+
      "crawls/small-site/start.html\n"+
      "@ page\n"+
      "crawls/small-site/A.html\n"+
      "crawls/small-site/B.html\n"+
      "crawls/small-site/C.html\n"+
      "crawls/small-site/start.html\n"+
      "crawls/small-site/subdir/D.html\n"+
      "crawls/small-site/subdir/E.html\n"+
      "crawls/small-site/subdir/F.html\n"+
      "@ pages\n"+
      "crawls/small-site/start.html\n"+
      "@ pop\n"+
      "crawls/small-site/B.html\n"+
      "@ scattered\n"+
      "crawls/small-site/start.html\n"+
      "@ site\n"+
      "crawls/small-site/start.html\n"+
      "@ start\n"+
      "crawls/small-site/B.html\n"+
      "crawls/small-site/subdir/D.html\n"+
      "crawls/small-site/subdir/E.html\n"+
      "@ starting\n"+
      "crawls/small-site/start.html\n"+
      "@ subdirectory\n"+
      "crawls/small-site/start.html\n"+
      "@ test\n"+
      "crawls/small-site/start.html\n"+
      "@ web\n"+
      "crawls/small-site/start.html\n"+
      "";
    check_index(index,expect,37);
  }    
  // Utility to check intersection of sorted lists
  public static void check_intersection(String [] xa, String [] ya, String [] ea){
    String expect = Arrays.asList(ea).toString();
    String actual = PageIndex.intersectionOfSorted(Arrays.asList(xa),Arrays.asList(ya)).toString();
    String msg = "Intersection of sorted incorrect\n";
    msg += String.format("x:      %s\n",Arrays.toString(xa));
    msg += String.format("y:      %s\n",Arrays.toString(ya));
    msg += String.format("Expect: %s\n",expect);
    msg += String.format("Actual: %s\n",actual);
    assertEquals(msg,expect,actual);
  }
  @Test public void intersectionOfSorted_1(){
    String [] xa = {"A"};
    String [] ya = {"A"};
    String [] ea = {"A"};
    check_intersection(xa,ya,ea);
  }
  @Test public void intersectionOfSorted_2(){
    String [] xa = {"A","B","C","D","E"};
    String [] ya = {"A","B","C","D","E"};
    String [] ea = {"A","B","C","D","E"};
    check_intersection(xa,ya,ea);
    check_intersection(ya,xa,ea);
  }
  @Test public void intersectionOfSorted_3(){
    String [] xa = {"A","B","C","D","E"};
    String [] ya = {"A","B","C"};
    String [] ea = {"A","B","C"};
    check_intersection(xa,ya,ea);
    check_intersection(ya,xa,ea);
  }
  @Test public void intersectionOfSorted_4(){
    String [] xa = {"A","B","C","D","E"};
    String [] ya = {"A",        "D","E"};
    String [] ea = {"A",        "D","E"};
    check_intersection(xa,ya,ea);
    check_intersection(ya,xa,ea);
  }
  @Test public void intersectionOfSorted_5(){
    String [] xa = {"A","B","C","D","E"};
    String [] ya = {"A",    "C","D","E"};
    String [] ea = {"A",    "C","D","E"};
    check_intersection(xa,ya,ea);
    check_intersection(ya,xa,ea);
  }
  @Test public void intersectionOfSorted_6(){
    String [] xa = {"A","B","C","D","E"};
    String [] ya = {    "B","C","D"    };
    String [] ea = {    "B","C","D"    };
    check_intersection(xa,ya,ea);
    check_intersection(ya,xa,ea);
  }
  @Test public void intersectionOfSorted_7(){
    String [] xa = {"A","B","C","D","E"};
    String [] ya = {"A",            "E"};
    String [] ea = {"A",            "E"};
    check_intersection(xa,ya,ea);
    check_intersection(ya,xa,ea);
  }
  @Test public void intersectionOfSorted_8(){
    String [] xa = {"A","B","C","D","E"};
    String [] ya = {    "B",        "E"};
    String [] ea = {    "B",        "E"};
    check_intersection(xa,ya,ea);
    check_intersection(ya,xa,ea);
  }
  @Test public void intersectionOfSorted_9(){
    String [] xa = {"A","B","C","D","E"};
    String [] ya = {                   };
    String [] ea = {                   };
    check_intersection(xa,ya,ea);
    check_intersection(ya,xa,ea);
  }
  @Test public void intersectionOfSorted_10(){
    String [] xa = {"A",    "C",    "E"};
    String [] ya = {    "B",    "D"    };
    String [] ea = {                   };
    check_intersection(xa,ya,ea);
    check_intersection(ya,xa,ea);
  }
  @Test public void intersectionOfSorted_11(){
    String [] xa = {"A","B",           };
    String [] ya = {        "C","D","E"};
    String [] ea = {                   };
    check_intersection(xa,ya,ea);
    check_intersection(ya,xa,ea);
  }
  // Utility to check the results of query
  public static void check_queries(String [][] terms_pages,
                                   String [][] queries_results)
  {
    PageIndex index = new PageIndex();
    for(String [] term_pages : terms_pages){
      String term = term_pages[0];
      for(int i=1; i<term_pages.length; i++){
        index.addTermAndPage(term,term_pages[i]);
      }
    }
    for(String [] qr : queries_results){
      String query = qr[0];
      String expect = qr[1];
      String actual = index.query(query).toString();
      String msg = "";
      msg += "Query results incorrect\n";
      msg += String.format("Query:  %s\n",query);
      msg += String.format("Expect: %s\n",expect);
      msg += String.format("Actual: %s\n",actual);
      msg += String.format("Index:\n%s\n",index.toString());
      assertEquals(msg,expect,actual);
    }
  }
  @Test public void query_1(){
    String [][] terms_pages = {
      {"java","A.html"}, 
    };
    String [][] queries_results = {
      {"java","[A.html]"}
    };
    check_queries(terms_pages,queries_results);
  }
  @Test public void query_2(){
    String [][] terms_pages = {
      {"java","A.html","B.html","C.html","D.html"},
    };
    String [][] queries_results = {
      {"java","[A.html, B.html, C.html, D.html]"}
    };
    check_queries(terms_pages,queries_results);
  }
  @Test public void query_3(){
    String [][] terms_pages = {
      {"java","A.html"},
      {"c++", "A.html"},
    };
    String [][] queries_results = {
      {"java c++","[A.html]"},
      {"c++ java","[A.html]"},
    };
    check_queries(terms_pages,queries_results);
  }
  @Test public void query_4(){
    String [][] terms_pages = {
      {"java","A.html","B.html","C.html","D.html"},
      {"c++","A.html","B.html","C.html","D.html"},
    };
    String [][] queries_results = {
      {"java","[A.html, B.html, C.html, D.html]"},
      {"c++","[A.html, B.html, C.html, D.html]"},
      {"java c++","[A.html, B.html, C.html, D.html]"},
      {"c++ java","[A.html, B.html, C.html, D.html]"},
    };
    check_queries(terms_pages,queries_results);
  }
  @Test public void query_5(){
    String [][] terms_pages = {
      {"java","A.html","B.html","C.html","D.html"},
      {"c++", "A.html",         "C.html",        },
    };
    String [][] queries_results = {
      {"java","[A.html, B.html, C.html, D.html]"},
      {"c++","[A.html, C.html]"},
      {"java c++","[A.html, C.html]"},
      {"c++ java","[A.html, C.html]"},
    };
    check_queries(terms_pages,queries_results);
  }
  @Test public void query_6(){
    String [][] terms_pages = {
      {"java", "A.html","B.html","C.html","D.html",                  "G.html"},
      {"c++",  "A.html",         "C.html",                                   },
      {"dlang",         "B.html","C.html","D.html","E.html","F.html",        },
      {"rlang",         "B.html","C.html","D.html",         "F.html","G.html"},
      {"blang",         "B.html",         "D.html",         "F.html","G.html"},
    };
    String [][] queries_results = {
      {"java dlang","[B.html, C.html, D.html]"},
      {"dlang java","[B.html, C.html, D.html]"},
      {"java rlang","[B.html, C.html, D.html, G.html]"},
      {"rlang java","[B.html, C.html, D.html, G.html]"},
      {"dlang rlang","[B.html, C.html, D.html, F.html]"},
      {"rlang dlang","[B.html, C.html, D.html, F.html]"},
      {"blang dlang","[B.html, D.html, F.html]"},
      {"dlang blang","[B.html, D.html, F.html]"},
    };
    check_queries(terms_pages,queries_results);
  }
  @Test public void query_7(){
    String [][] terms_pages = {
      {"java", "A.html","B.html","C.html","D.html",                  "G.html"},
      {"c++",  "A.html",         "C.html",                                   },
      {"dlang",         "B.html","C.html","D.html","E.html","F.html",        },
      {"rlang",         "B.html","C.html","D.html",         "F.html","G.html"},
      {"blang",         "B.html",         "D.html",         "F.html","G.html"},
    };
    String [][] queries_results = {
      {"java dlang c++","[C.html]"},
      {"dlang java c++","[C.html]"},
      {"dlang c++ java","[C.html]"},
      {"c++ dlang java","[C.html]"},
      {"dlang rlang blang","[B.html, D.html, F.html]"},
      {"rlang dlang blang","[B.html, D.html, F.html]"},
      {"dlang blang rlang","[B.html, D.html, F.html]"},
      {"blang rlang dlang","[B.html, D.html, F.html]"},
      {"java rlang blang","[B.html, D.html, G.html]"},
    };
    check_queries(terms_pages,queries_results);
  }
  @Test public void query_8(){
    String [][] terms_pages = {
      {"java", "A.html","B.html","C.html","D.html",                  "G.html"},
      {"c++",  "A.html",         "C.html",                                   },
      {"dlang",         "B.html","C.html","D.html","E.html","F.html",        },
      {"rlang",         "B.html","C.html","D.html",         "F.html","G.html"},
      {"blang",         "B.html",         "D.html",         "F.html","G.html"},
    };
    String [][] queries_results = {
      {"java c++ dlang rlang","[C.html]"},
      {"c++ java rlang dlang","[C.html]"},
      {"dlang rlang blang java","[B.html, D.html]"},
      {"dlang blang java rlang","[B.html, D.html]"},
      {"blang java dlang rlang","[B.html, D.html]"},
    };
    check_queries(terms_pages,queries_results);
  }

  // Utility to check the results of query
  public static void check_queries(PageIndex index,
                                   String [][] queries_results)
  {
    for(String [] qr : queries_results){
      String query = qr[0];
      String expect = qr[1];
      String actual = index.query(query).toString();
      String msg = "";
      msg += "Query results incorrect\n";
      msg += String.format("Query:  %s\n",query);
      msg += String.format("Expect: %s\n",expect);
      msg += String.format("Actual: %s\n",actual);
      msg += String.format("Index:\n%s\n",index.toString());
      assertEquals(msg,expect,actual);
    }
  }

  @Test public void small_query_1(){
    String [][] queries_results = {
      {"acrobat","[crawls/small-site/A.html, crawls/small-site/subdir/E.html, crawls/small-site/subdir/F.html]"},
      {"daring","[crawls/small-site/C.html, crawls/small-site/subdir/D.html]"},
      {"bread","[crawls/small-site/A.html, crawls/small-site/B.html, crawls/small-site/C.html]"},
      {"page","[crawls/small-site/A.html, crawls/small-site/B.html, crawls/small-site/C.html, crawls/small-site/start.html, crawls/small-site/subdir/D.html, crawls/small-site/subdir/E.html, crawls/small-site/subdir/F.html]"},
      {"link","[crawls/small-site/A.html, crawls/small-site/B.html, crawls/small-site/start.html, crawls/small-site/subdir/D.html, crawls/small-site/subdir/E.html]"},
      {"champagne","[crawls/small-site/C.html, crawls/small-site/subdir/D.html]"},
      {"start","[crawls/small-site/B.html, crawls/small-site/subdir/D.html, crawls/small-site/subdir/E.html]"},
    };
    Crawler c = new RecursiveCrawler();
    c.crawl("crawls/small-site/start.html");
    PageIndex index = new PageIndex();
    index.addCrawledPages(c);
    check_queries(index,queries_results);
  }
  @Test public void small_query_2(){
    String [][] queries_results = {
      {"daring acrobat","[]"},
      {"acrobat bread","[crawls/small-site/A.html]"},
      {"bread acrobat","[crawls/small-site/A.html]"},
      {"daring bread","[crawls/small-site/C.html]"},
      {"champagne link","[crawls/small-site/subdir/D.html]"},
      {"link champagne","[crawls/small-site/subdir/D.html]"},
      {"page link","[crawls/small-site/A.html, crawls/small-site/B.html, crawls/small-site/start.html, crawls/small-site/subdir/D.html, crawls/small-site/subdir/E.html]"},
      {"link page","[crawls/small-site/A.html, crawls/small-site/B.html, crawls/small-site/start.html, crawls/small-site/subdir/D.html, crawls/small-site/subdir/E.html]"},
      {"link bread","[crawls/small-site/A.html, crawls/small-site/B.html]"},
      {"bread link","[crawls/small-site/A.html, crawls/small-site/B.html]"},
    };
    Crawler c = new RecursiveCrawler();
    c.crawl("crawls/small-site/start.html");
    PageIndex index = new PageIndex();
    index.addCrawledPages(c);
    check_queries(index,queries_results);
  }
  @Test public void small_query_3(){
    String [][] queries_results = {
      {"link acrobat bread","[crawls/small-site/A.html]"},
      {"bread link page","[crawls/small-site/A.html, crawls/small-site/B.html]"},
      {"page link bread","[crawls/small-site/A.html, crawls/small-site/B.html]"},
      {"link start page","[crawls/small-site/B.html, crawls/small-site/subdir/D.html, crawls/small-site/subdir/E.html]"},
    };
    Crawler c = new RecursiveCrawler();
    c.crawl("crawls/small-site/start.html");
    PageIndex index = new PageIndex();
    index.addCrawledPages(c);
    check_queries(index,queries_results);
  }
  @Test public void white_query_1(){
    String [][] queries_results = {
      {"yacc","[crawls/white/Pages/Syllabi/syllabus540F14.html, crawls/white/Pages/cra.html, crawls/white/Pages/links.html, crawls/white/Pages/research.html, crawls/white/Pages/vyacc.html, crawls/white/Vyacc/dist.html, crawls/white/Vyacc/vyacc_readme.html]"},
      {"research yacc","[crawls/white/Pages/cra.html, crawls/white/Pages/links.html, crawls/white/Pages/research.html]"},
      {"syllabus","[crawls/white/Pages/Syllabi/syllabus367F15.html]"},
      {"grade","[crawls/white/Pages/Syllabi/syllabus367F15.html, crawls/white/Pages/Syllabi/syllabus440S15.html, crawls/white/Pages/Syllabi/syllabus540F14.html]"},
      {"abide grade","[crawls/white/Pages/Syllabi/syllabus440S15.html, crawls/white/Pages/Syllabi/syllabus540F14.html]"},
      {"dynamic computation","[crawls/white/Pages/ci.html, crawls/white/Pages/dr.html]"},
    };
    Crawler c = new RecursiveCrawler();
    c.crawl("crawls/white/index.html");
    PageIndex index = new PageIndex();
    index.addCrawledPages(c);
    check_queries(index,queries_results);
  }
  // There is a page
  //   crawls/sean/research/mersenne.1.html
  // which uses frames and will generate a NullPointerException when
  // jsoup tries to parse it. The addCrawledPages(..) method if
  // PageIndex will need to catch such exceptions and ignore the given
  // page.
  @Test public void sean_query_1(){
    String [][] queries_results = {
      {"web","[crawls/sean/lisp/index.html, crawls/sean/papers/index.html, crawls/sean/projects/newton/Radicals/index.html, crawls/sean/projects/newton/ToyStory/index.html, crawls/sean/projects/newton/YaleInput/index.html, crawls/sean/projects/next/Resound/index.html]"},
      {"work","[crawls/sean/book/metaheuristics/index.html, crawls/sean/lisp/LispTutorial.html, crawls/sean/papers/index.html, crawls/sean/projects/newton/MP100Power.html, crawls/sean/projects/next/Resound/index.html, crawls/sean/research/lil-gp-patch/bugs.html, crawls/sean/research/lil-gp-patch/index.html, crawls/sean/research/lil-gp-patch/peters-patch.html]"},
      {"web work","[crawls/sean/papers/index.html, crawls/sean/projects/next/Resound/index.html]"},
      {"work web","[crawls/sean/papers/index.html, crawls/sean/projects/next/Resound/index.html]"},
      {"weird lisp","[crawls/sean/lisp/LispTutorial.html, crawls/sean/research/lil-gp-patch/index.html]"},
      {"weird lisp version","[crawls/sean/lisp/LispTutorial.html, crawls/sean/research/lil-gp-patch/index.html]"},
      {"weird lisp version variant","[crawls/sean/lisp/LispTutorial.html]"},
    };
    Crawler c = new RecursiveCrawler();
    c.crawl("crawls/sean/index.html");
    PageIndex index = new PageIndex();
    index.addCrawledPages(c);
    check_queries(index,queries_results);
  }

  
}

