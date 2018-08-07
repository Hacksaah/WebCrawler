// Makes use of the MockCrawler Class that is provided

import java.io.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test; // fixes some compile problems with annotations

public class CrawlerTests{

  public static void main(String args[]) {
    org.junit.runner.JUnitCore.main("CrawlerTests");
  }

  @Test public void constructor_methods1(){
    String [] found = {};
    String [] skipped = {};
    Crawler c = new MockCrawler(found,skipped);
    String msg, expect, actual;
    // foundPagesString()
    expect = "";
    actual = c.foundPagesString();
    msg = "";
    msg += "Found pages wrong:\n";
    msg += String.format("Expect:\n%s\n",expect);
    msg += String.format("Actual:\n%s\n",actual);
    assertEquals(msg, expect, actual);
    // skippedPagesString()
    expect = "";
    actual = c.skippedPagesString();
    msg = "";
    msg += "Skipped pages wrong:\n";
    msg += String.format("Expect:\n%s\n",expect);
    msg += String.format("Actual:\n%s\n",actual);
    assertEquals(msg, expect, actual);
    // foundPagesList()
    expect = "[]";
    actual = c.foundPagesList().toString();
    msg = "";
    msg += "Found pages list wrong:\n";
    msg += String.format("Expect:\n%s\n",expect);
    msg += String.format("Actual:\n%s\n",actual);
    assertEquals(msg, expect, actual);
    // skippedPagesList()
    expect = "[]";
    actual = c.skippedPagesList().toString();
    msg = "";
    msg += "Skipped pages list wrong:\n";
    msg += String.format("Expect:\n%s\n",expect);
    msg += String.format("Actual:\n%s\n",actual);
    assertEquals(msg, expect, actual);
  }

  @Test public void constructor_methods2(){
    String [] found = {"start.html","A.html","B.html","C.html","subdir/D.html"};
    String [] skipped = {"http://x.com/X.html","spider-man.png","javascript:alert('An alert');"};
    Crawler c = new MockCrawler(found,skipped);
    String msg, expect, actual;
    // foundPagesString()
    expect = 
      "A.html\n"+
      "B.html\n"+
      "C.html\n"+
      "start.html\n"+
      "subdir/D.html\n"+
      "";
    actual = c.foundPagesString();
    msg = "";
    msg += "Found pages wrong:\n";
    msg += String.format("Expect:\n%s\n",expect);
    msg += String.format("Actual:\n%s\n",actual);
    assertEquals(msg, expect, actual);
    // skippedPagesString()
    expect =
      "http://x.com/X.html\n"+
      "javascript:alert('An alert');\n"+
      "spider-man.png\n"+
      "";
    actual = c.skippedPagesString();
    msg = "";
    msg += "Skipped pages wrong:\n";
    msg += String.format("Expect:\n%s\n",expect);
    msg += String.format("Actual:\n%s\n",actual);
    assertEquals(msg, expect, actual);
    // foundPagesList()
    expect = "[A.html, B.html, C.html, start.html, subdir/D.html]";
    actual = c.foundPagesList().toString();
    msg = "";
    msg += "Found pages list wrong:\n";
    msg += String.format("Expect:\n%s\n",expect);
    msg += String.format("Actual:\n%s\n",actual);
    assertEquals(msg, expect, actual);
    // skippedPagesList()
    expect = "[http://x.com/X.html, javascript:alert('An alert');, spider-man.png]";
    actual = c.skippedPagesList().toString();
    msg = "";
    msg += "Skipped pages list wrong:\n";
    msg += String.format("Expect:\n%s\n",expect);
    msg += String.format("Actual:\n%s\n",actual);
    assertEquals(msg, expect, actual);
  }

  @Test public void validPageLinks_true1(){
    String links[] = {
      "A.html",
      "start.html",
      "subdir/D.html",
      "small-site/subdir/D.html",
      "small-site/subdir/../B.html",
      "A.HTML",
      "./start.HTML",
      "subdir/D.HTML",
      "small-site/subdir/D.HTML",
      "small-site/subdir/../B.HTML",
    };
    for(String link : links){
      assertTrue("Link is valid", Crawler.validPageLink(link));
    }    
  }

  @Test public void validPageLinks_false1(){
    String links[] = {
      "http://x.com/X.html",
      "javascript:alert('An alert');",
      "spider-man.png",
      "http://A.html",
      "http://google.com",
      "picture.jpg",
      "subdir/deep/photo.png",
      "mailto:white@cs.gmu.edu",
    };
    for(String link : links){
      assertFalse("Link is NOT valid", Crawler.validPageLink(link));
    }    
  }


}
