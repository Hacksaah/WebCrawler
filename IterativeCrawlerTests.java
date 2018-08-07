import java.io.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test; // fixes some compile problems with annotations

public class IterativeCrawlerTests{

  public static void main(String args[]) {
    org.junit.runner.JUnitCore.main("IterativeCrawlerTests");
  }

  @Test public void constructor_methods1(){
    Crawler c = new IterativeCrawler();
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


  // Helper method that just checks the found, skipped, and pending
  // pages against expectations
  public static void check_crawler_no_crawl(IterativeCrawler c, String expectPending,
                                            String expectFound, String expectSkipped)
  {
    String msg, expect, actual;
    // pendingPagesString()
    expect = expectPending;
    actual = c.pendingPagesString();
    msg = "";
    msg += "Pending pages wrong:\n";
    msg += String.format("Expect:\n%s\n",expect);
    msg += String.format("Actual:\n%s\n",actual);
    assertEquals(msg, expect, actual);                                    // Check pending
    int expectSize = expectPending.length()==0 ? 0 : expectPending.split("\\n").length;
    assertEquals(expectSize,c.pendingPagesSize()); // Check pending size
    // foundPagesString()
    expect = expectFound;
    actual = c.foundPagesString();
    msg = "";
    msg += "Found pages wrong:\n";
    msg += String.format("Expect:\n%s\n",expect);
    msg += String.format("Actual:\n%s\n",actual);
    assertEquals(msg, expect, actual);
    // skippedPagesString()
    if(expectSkipped != null){
      expect = expectSkipped;
      actual = c.skippedPagesString();
      msg = "";
      msg += "Skipped pages wrong:\n";
      msg += String.format("Expect:\n%s\n",expect);
      msg += String.format("Actual:\n%s\n",actual);
      assertEquals(msg, expect, actual);
    }
  }

  @Test public void pendingPages_start1(){
    IterativeCrawler c = new IterativeCrawler();
    c.addPendingPage("crawls/small-site/start.html");
    check_crawler_no_crawl(c,
                           "crawls/small-site/start.html\n",
                           "",
                           "");
    assertEquals(1,c.pendingPagesSize());
  }
  // Check for no redundancy in pending 
  @Test public void pendingPages_start2(){
    IterativeCrawler c = new IterativeCrawler();
    c.addPendingPage("crawls/small-site/start.html");
    c.addPendingPage("crawls/small-site/start.html");
    check_crawler_no_crawl(c,
                           "crawls/small-site/start.html\n",
                           "",
                           "");
    assertEquals(1,c.pendingPagesSize());
  }

  @Test public void crawlNextPage_start1(){
    IterativeCrawler c = new IterativeCrawler();
    String pending, found, skipped;
    // 
    c.addPendingPage("crawls/small-site/start.html");
    pending =
      "crawls/small-site/start.html\n"+
      "";
    found =
      "";
    skipped =
      "";
    check_crawler_no_crawl(c, pending, found, skipped);
    // 
    c.crawlNextPage();
    pending =
      "crawls/small-site/A.html\n"+
      "crawls/small-site/B.html\n"+
      "crawls/small-site/C.html\n"+
      "crawls/small-site/subdir/D.html\n"+
      "";
    found =
      "crawls/small-site/start.html\n"+
      "";
    skipped =
      "spider-man.png\n"+
      "";
    check_crawler_no_crawl(c, pending, found, skipped);
  }
  @Test public void crawlNextPage_start2(){
    IterativeCrawler c = new IterativeCrawler();
    String pending, found, skipped;
    c.addPendingPage("crawls/small-site/start.html");
    c.crawlNextPage();
    c.crawlNextPage();
    pending =
      "crawls/small-site/A.html\n"+
      "crawls/small-site/B.html\n"+
      "crawls/small-site/C.html\n"+
      "crawls/small-site/subdir/E.html\n"+
      "";
    found =
      "crawls/small-site/start.html\n"+
      "crawls/small-site/subdir/D.html\n"+
      "";
    skipped =
      "spider-man.png\n"+
      "";
    check_crawler_no_crawl(c, pending, found, skipped);
    // 
    c.crawlNextPage();
    c.crawlNextPage();
    pending =
      "crawls/small-site/A.html\n"+
      "crawls/small-site/B.html\n"+
      "crawls/small-site/C.html\n"+
      "";
    found =
      "crawls/small-site/start.html\n"+
      "crawls/small-site/subdir/D.html\n"+
      "crawls/small-site/subdir/E.html\n"+
      "crawls/small-site/subdir/F.html\n"+
      "";
    skipped =
      "spider-man.png\n"+
      "";
    check_crawler_no_crawl(c, pending, found, skipped);
  }
  // Check for no redundancy in pending 
  @Test public void crawlNextPage_start3(){
    IterativeCrawler c = new IterativeCrawler();
    String pending, found, skipped;
    c.addPendingPage("crawls/small-site/start.html");
    c.crawlNextPage();
    c.crawlNextPage();
    pending =
      "crawls/small-site/A.html\n"+
      "crawls/small-site/B.html\n"+
      "crawls/small-site/C.html\n"+
      "crawls/small-site/subdir/E.html\n"+
      "";
    found =
      "crawls/small-site/start.html\n"+
      "crawls/small-site/subdir/D.html\n"+
      "";
    skipped =
      "spider-man.png\n"+
      "";
    check_crawler_no_crawl(c, pending, found, skipped);
    c.addPendingPage("crawls/small-site/B.html");
    check_crawler_no_crawl(c, pending, found, skipped);
  }
  @Test public void crawlNextPage_white_index1(){
    IterativeCrawler c = new IterativeCrawler();
    String pending, found, skipped;
    c.addPendingPage("crawls/white/index.html");
    c.crawlNextPage();
    pending =
      "crawls/white/Pages/links.html\n"+
      "crawls/white/Pages/research.html\n"+
      "crawls/white/Pages/teaching.html\n"+
      "";
    found =
      "crawls/white/index.html\n"+
      "";
    skipped =
      "http://vse.gmu.edu\n"+
      "http://www.cs.gmu.edu/\n"+
      "http://www.gmu.edu\n"+
      "mailto:white@cs.gmu.edu\n"+
      "";
    check_crawler_no_crawl(c, pending, found, skipped);
  }
  @Test public void crawlNextPage_white_index2(){
    IterativeCrawler c = new IterativeCrawler();
    String pending, found, skipped;
    c.addPendingPage("crawls/white/index.html");
    c.crawlNextPage();
    c.crawlNextPage();
    pending =
      "crawls/white/Pages/Syllabi/syllabus367F15.html\n"+
      "crawls/white/Pages/Syllabi/syllabus440S15.html\n"+
      "crawls/white/Pages/Syllabi/syllabus540F14.html\n"+
      "crawls/white/Pages/Syllabi/syllabus640S13.html\n"+
      "crawls/white/Pages/links.html\n"+
      "crawls/white/Pages/research.html\n"+
      "";
    found =
      "crawls/white/Pages/teaching.html\n"+
      "crawls/white/index.html\n"+
      "";
    skipped =
      "http://cs.gmu.edu/~white/Pages/Syllabi/syllabus477F15.pdf\n"+
      "http://vse.gmu.edu\n"+
      "http://www.cs.gmu.edu/\n"+
      "http://www.cs.gmu.edu/~white/CS363/syllabusS12.html\n"+
      "http://www.cs.gmu.edu/~white/CS707/707syllabus.html\n"+
      "http://www.gmu.edu\n"+
      "mailto:white@cs.gmu.edu\n"+
      "";
    check_crawler_no_crawl(c, pending, found, skipped);
  }
  @Test public void crawlNextPage_white_index3(){
    IterativeCrawler c = new IterativeCrawler();
    String pending, found, skipped;
    c.addPendingPage("crawls/white/index.html");
    c.crawlNextPage();
    c.crawlNextPage();
    c.crawlNextPage();
    c.crawlNextPage();
    c.crawlNextPage();
    c.crawlNextPage();
    pending =
      "crawls/white/Pages/Syllabi/syllabus367F15.html\n"+
      "crawls/white/Pages/Syllabi/syllabus440S15.html\n"+
      "crawls/white/Pages/Syllabi/syllabus540F14.html\n"+
      "crawls/white/Pages/Syllabi/syllabus640S13.html\n"+
      "crawls/white/Pages/ci.html\n"+
      "crawls/white/Pages/cra.html\n"+
      "";
    found =
      "crawls/white/Pages/dis.html\n"+
      "crawls/white/Pages/dr.html\n"+
      "crawls/white/Pages/links.html\n"+
      "crawls/white/Pages/research.html\n"+
      "crawls/white/Pages/teaching.html\n"+
      "crawls/white/index.html\n"+
      "";
    skipped =
      "file://thumper.cs.umd.edu/files/docs/umcp-cs.html\n"+
      "http://cra.org/Activities/craw/\n"+
      "http://cs.gmu.edu/~white/Pages/Gifs/hopper.gif\n"+
      "http://cs.gmu.edu/~white/Pages/Syllabi/syllabus477F15.pdf\n"+
      "http://cs.gmu.edu/~white/Papers/cgf6.ps\n"+
      "http://cs.gmu.edu/~white/Papers/dis12.ps\n"+
      "http://cs.gmu.edu/~white/Papers/dis13.ps\n"+
      "http://cs.gmu.edu/~white/Papers/dis14.ps\n"+
      "http://info.acm.org/\n"+
      "http://liinwww.ira.uka.de/bibliography/index.html\n"+
      "http://nac.gmu.edu/~kfrosch/96-15-012.ps\n"+
      "http://src.doc.ic.ac.uk/bySubject/Computing/Overview.html\n"+
      "http://vse.gmu.edu\n"+
      "http://www.ai.mit.edu/people/ellens/gender.html\n"+
      "http://www.cera2.com/softeng.htm\n"+
      "http://www.comlab.ox.ac.uk/archive/concurrent.html\n"+
      "http://www.computer.org/\n"+
      "http://www.cra.org/\n"+
      "http://www.cs.gmu.edu/\n"+
      "http://www.cs.gmu.edu/dpcl.html\n"+
      "http://www.cs.gmu.edu/~cfia\n"+
      "http://www.cs.gmu.edu/~white/CS363/syllabusS12.html\n"+
      "http://www.cs.gmu.edu/~white/CS707/707syllabus.html\n"+
      "http://www.cs.rice.edu/~roth/conferences.html\n"+
      "http://www.cs.umd.edu/~pugh/java/\n"+
      "http://www.cs.wisc.edu/~markhill/conference-etiquette.html\n"+
      "http://www.cs.wisc.edu/~markhill/conference-talk.html\n"+
      "http://www.cs.yale.edu/homes/tap/\n"+
      "http://www.cs.yale.edu/homes/tap/tap-resources.html\n"+
      "http://www.cs.yale.edu/homes/tap/tap.html\n"+
      "http://www.gmu.edu\n"+
      "http://www.iftech.com/oltc/c/c0.stm\n"+
      "http://www.ise.gmu.edu/SERL\n"+
      "http://www.isse.gmu.edu/~ofut/hotlist.html#THOUGHTS\n"+
      "http://www.javasoft.com:80/products/jdk/1.1/docs/index.html\n"+
      "http://www.javaworld.com/javaworld\n"+
      "http://www.lincom-asg.com/~rjamison/byacc/\n"+
      "http://www.lpac.ac.uk/SEL-HPC/Articles/CompilersArchive.html\n"+
      "http://www.ncstrl.org/\n"+
      "http://www.nsf.gov/\n"+
      "http://www.onr.navy.mil/\n"+
      "http://www.osf.org/mall/\n"+
      "http://www.phoaks.com/phoaks/comp/lang/ada/\n"+
      "http://www.sdsc.edu/Hopper/\n"+
      "http://www.sei.cmu.edu/\n"+
      "http://www.suntest.com/Jack/\n"+
      "http://www.yahoo.com/Science/Computer_Science/Distributed_Computing/\n"+
      "mailto:white@cs.gmu.edu\n"+
      "";
    check_crawler_no_crawl(c, pending, found, skipped);
  }

  @Test public void crawlRemaining_start1(){
    IterativeCrawler c = new IterativeCrawler();
    String pending, found, skipped;
    c.addPendingPage("crawls/small-site/start.html");
    c.crawlRemaining();
    // 
    pending =
      "";
    found =
      "crawls/small-site/A.html\n"+
      "crawls/small-site/B.html\n"+
      "crawls/small-site/C.html\n"+
      "crawls/small-site/start.html\n"+
      "crawls/small-site/subdir/D.html\n"+
      "crawls/small-site/subdir/E.html\n"+
      "crawls/small-site/subdir/F.html\n"+
      "";
    skipped =
      "http://cs.gmu.edu\n"+
      "javascript:alert('An alert');\n"+
      "spider-man.png\n"+
      "";
    check_crawler_no_crawl(c, pending, found, skipped);
  }
  @Test public void crawlRemaining_start2(){
    IterativeCrawler c = new IterativeCrawler();
    String pending, found, skipped;
    c.addPendingPage("crawls/small-site/start.html");
    c.crawlNextPage();
    c.crawlNextPage();
    c.crawlRemaining();
    // 
    pending =
      "";
    found =
      "crawls/small-site/A.html\n"+
      "crawls/small-site/B.html\n"+
      "crawls/small-site/C.html\n"+
      "crawls/small-site/start.html\n"+
      "crawls/small-site/subdir/D.html\n"+
      "crawls/small-site/subdir/E.html\n"+
      "crawls/small-site/subdir/F.html\n"+
      "";
    skipped =
      "http://cs.gmu.edu\n"+
      "javascript:alert('An alert');\n"+
      "spider-man.png\n"+
      "";
    check_crawler_no_crawl(c, pending, found, skipped);
  }
  @Test public void crawlRemaining_white_index1(){
    IterativeCrawler c = new IterativeCrawler();
    String pending, found, skipped;
    c.addPendingPage("crawls/white/index.html");
    c.crawlRemaining();
    // 
    pending =
      "";
    found =
      "crawls/white/Pages/Syllabi/syllabus367F15.html\n"+
      "crawls/white/Pages/Syllabi/syllabus440S15.html\n"+
      "crawls/white/Pages/Syllabi/syllabus540F14.html\n"+
      "crawls/white/Pages/Syllabi/syllabus640S13.html\n"+
      "crawls/white/Pages/ci.html\n"+
      "crawls/white/Pages/cra.html\n"+
      "crawls/white/Pages/dis.html\n"+
      "crawls/white/Pages/dr.html\n"+
      "crawls/white/Pages/links.html\n"+
      "crawls/white/Pages/research.html\n"+
      "crawls/white/Pages/teaching.html\n"+
      "crawls/white/Pages/vyacc.html\n"+
      "crawls/white/Vyacc/dist.html\n"+
      "crawls/white/Vyacc/vyacc_readme.html\n"+
      "crawls/white/index.html\n"+
      "";
    skipped =
      "file://thumper.cs.umd.edu/files/docs/umcp-cs.html\n"+
      "http://cra.org/Activities/craw/\n"+
      "http://cs.gmu.edu/~white/Pages/Gifs/hopper.gif\n"+
      "http://cs.gmu.edu/~white/Pages/Syllabi/syllabus477F15.pdf\n"+
      "http://cs.gmu.edu/~white/Papers/cgf6.ps\n"+
      "http://cs.gmu.edu/~white/Papers/dis12.ps\n"+
      "http://cs.gmu.edu/~white/Papers/dis13.ps\n"+
      "http://cs.gmu.edu/~white/Papers/dis14.ps\n"+
      "http://cs.gmu.edu/~white/Vyacc/vyacc.tar.gz\n"+
      "http://info.acm.org/\n"+
      "http://jiju.gmu.edu/catalog/apolicies/honor.html\n"+
      "http://liinwww.ira.uka.de/bibliography/index.html\n"+
      "http://nac.gmu.edu/~kfrosch/96-15-012.ps\n"+
      "http://oai.gmu.edu/honor-code/\n"+
      "http://ods.gmu.edu\n"+
      "http://src.doc.ic.ac.uk/bySubject/Computing/Overview.html\n"+
      "http://vse.gmu.edu\n"+
      "http://www.ai.mit.edu/people/ellens/gender.html\n"+
      "http://www.cera2.com/softeng.htm\n"+
      "http://www.comlab.ox.ac.uk/archive/concurrent.html\n"+
      "http://www.computer.org/\n"+
      "http://www.cra.org\n"+
      "http://www.cra.org/\n"+
      "http://www.cs.gmu.edu/\n"+
      "http://www.cs.gmu.edu/dpcl.html\n"+
      "http://www.cs.gmu.edu/~cfia\n"+
      "http://www.cs.gmu.edu/~white\n"+
      "http://www.cs.gmu.edu/~white/CS363/syllabusS12.html\n"+
      "http://www.cs.gmu.edu/~white/CS707/707syllabus.html\n"+
      "http://www.cs.rice.edu/~roth/conferences.html\n"+
      "http://www.cs.umd.edu/~pugh/java/\n"+
      "http://www.cs.wisc.edu/~markhill/conference-etiquette.html\n"+
      "http://www.cs.wisc.edu/~markhill/conference-talk.html\n"+
      "http://www.cs.yale.edu/homes/tap/\n"+
      "http://www.cs.yale.edu/homes/tap/tap-resources.html\n"+
      "http://www.cs.yale.edu/homes/tap/tap.html\n"+
      "http://www.gmu.edu\n"+
      "http://www.iftech.com/oltc/c/c0.stm\n"+
      "http://www.ise.gmu.edu/SERL\n"+
      "http://www.isse.gmu.edu/~ofut/hotlist.html#THOUGHTS\n"+
      "http://www.javasoft.com:80/products/jdk/1.1/docs/index.html\n"+
      "http://www.javaworld.com/javaworld\n"+
      "http://www.lincom-asg.com/~rjamison/byacc/\n"+
      "http://www.lpac.ac.uk/SEL-HPC/Articles/CompilersArchive.html\n"+
      "http://www.ncstrl.org/\n"+
      "http://www.nsf.gov/\n"+
      "http://www.onr.navy.mil/\n"+
      "http://www.osf.org/mall/\n"+
      "http://www.phoaks.com/phoaks/comp/lang/ada/\n"+
      "http://www.sdsc.edu/Hopper/\n"+
      "http://www.sei.cmu.edu/\n"+
      "http://www.suntest.com/Jack/\n"+
      "http://www.yahoo.com/Science/Computer_Science/Distributed_Computing/\n"+
      "mailto:white@cs.gmu.edu\n"+
      "";
    check_crawler_no_crawl(c, pending, found, skipped);
  }
  @Test public void crawlRemaining_white_index2(){
    IterativeCrawler c = new IterativeCrawler();
    String pending, found, skipped;
    c.addPendingPage("crawls/white/index.html");
    for(int i=0; i<8; i++){     // Crawl 8 pages individually
      c.crawlNextPage();
    }
    c.crawlRemaining();         // Crawl remainder
    // 
    pending =
      "";
    found =
      "crawls/white/Pages/Syllabi/syllabus367F15.html\n"+
      "crawls/white/Pages/Syllabi/syllabus440S15.html\n"+
      "crawls/white/Pages/Syllabi/syllabus540F14.html\n"+
      "crawls/white/Pages/Syllabi/syllabus640S13.html\n"+
      "crawls/white/Pages/ci.html\n"+
      "crawls/white/Pages/cra.html\n"+
      "crawls/white/Pages/dis.html\n"+
      "crawls/white/Pages/dr.html\n"+
      "crawls/white/Pages/links.html\n"+
      "crawls/white/Pages/research.html\n"+
      "crawls/white/Pages/teaching.html\n"+
      "crawls/white/Pages/vyacc.html\n"+
      "crawls/white/Vyacc/dist.html\n"+
      "crawls/white/Vyacc/vyacc_readme.html\n"+
      "crawls/white/index.html\n"+
      "";
    skipped =
      "file://thumper.cs.umd.edu/files/docs/umcp-cs.html\n"+
      "http://cra.org/Activities/craw/\n"+
      "http://cs.gmu.edu/~white/Pages/Gifs/hopper.gif\n"+
      "http://cs.gmu.edu/~white/Pages/Syllabi/syllabus477F15.pdf\n"+
      "http://cs.gmu.edu/~white/Papers/cgf6.ps\n"+
      "http://cs.gmu.edu/~white/Papers/dis12.ps\n"+
      "http://cs.gmu.edu/~white/Papers/dis13.ps\n"+
      "http://cs.gmu.edu/~white/Papers/dis14.ps\n"+
      "http://cs.gmu.edu/~white/Vyacc/vyacc.tar.gz\n"+
      "http://info.acm.org/\n"+
      "http://jiju.gmu.edu/catalog/apolicies/honor.html\n"+
      "http://liinwww.ira.uka.de/bibliography/index.html\n"+
      "http://nac.gmu.edu/~kfrosch/96-15-012.ps\n"+
      "http://oai.gmu.edu/honor-code/\n"+
      "http://ods.gmu.edu\n"+
      "http://src.doc.ic.ac.uk/bySubject/Computing/Overview.html\n"+
      "http://vse.gmu.edu\n"+
      "http://www.ai.mit.edu/people/ellens/gender.html\n"+
      "http://www.cera2.com/softeng.htm\n"+
      "http://www.comlab.ox.ac.uk/archive/concurrent.html\n"+
      "http://www.computer.org/\n"+
      "http://www.cra.org\n"+
      "http://www.cra.org/\n"+
      "http://www.cs.gmu.edu/\n"+
      "http://www.cs.gmu.edu/dpcl.html\n"+
      "http://www.cs.gmu.edu/~cfia\n"+
      "http://www.cs.gmu.edu/~white\n"+
      "http://www.cs.gmu.edu/~white/CS363/syllabusS12.html\n"+
      "http://www.cs.gmu.edu/~white/CS707/707syllabus.html\n"+
      "http://www.cs.rice.edu/~roth/conferences.html\n"+
      "http://www.cs.umd.edu/~pugh/java/\n"+
      "http://www.cs.wisc.edu/~markhill/conference-etiquette.html\n"+
      "http://www.cs.wisc.edu/~markhill/conference-talk.html\n"+
      "http://www.cs.yale.edu/homes/tap/\n"+
      "http://www.cs.yale.edu/homes/tap/tap-resources.html\n"+
      "http://www.cs.yale.edu/homes/tap/tap.html\n"+
      "http://www.gmu.edu\n"+
      "http://www.iftech.com/oltc/c/c0.stm\n"+
      "http://www.ise.gmu.edu/SERL\n"+
      "http://www.isse.gmu.edu/~ofut/hotlist.html#THOUGHTS\n"+
      "http://www.javasoft.com:80/products/jdk/1.1/docs/index.html\n"+
      "http://www.javaworld.com/javaworld\n"+
      "http://www.lincom-asg.com/~rjamison/byacc/\n"+
      "http://www.lpac.ac.uk/SEL-HPC/Articles/CompilersArchive.html\n"+
      "http://www.ncstrl.org/\n"+
      "http://www.nsf.gov/\n"+
      "http://www.onr.navy.mil/\n"+
      "http://www.osf.org/mall/\n"+
      "http://www.phoaks.com/phoaks/comp/lang/ada/\n"+
      "http://www.sdsc.edu/Hopper/\n"+
      "http://www.sei.cmu.edu/\n"+
      "http://www.suntest.com/Jack/\n"+
      "http://www.yahoo.com/Science/Computer_Science/Distributed_Computing/\n"+
      "mailto:white@cs.gmu.edu\n"+
      "";
    check_crawler_no_crawl(c, pending, found, skipped);
  }
  @Test public void crawlRemaining_start_white1(){
    IterativeCrawler c = new IterativeCrawler();
    String pending, found, skipped;
    c.addPendingPage("crawls/white/index.html");
    c.addPendingPage("crawls/small-site/start.html");
    c.crawlRemaining();
    // 
    pending =
      "";
    found =
      "crawls/small-site/A.html\n"+
      "crawls/small-site/B.html\n"+
      "crawls/small-site/C.html\n"+
      "crawls/small-site/start.html\n"+
      "crawls/small-site/subdir/D.html\n"+
      "crawls/small-site/subdir/E.html\n"+
      "crawls/small-site/subdir/F.html\n"+
      "crawls/white/Pages/Syllabi/syllabus367F15.html\n"+
      "crawls/white/Pages/Syllabi/syllabus440S15.html\n"+
      "crawls/white/Pages/Syllabi/syllabus540F14.html\n"+
      "crawls/white/Pages/Syllabi/syllabus640S13.html\n"+
      "crawls/white/Pages/ci.html\n"+
      "crawls/white/Pages/cra.html\n"+
      "crawls/white/Pages/dis.html\n"+
      "crawls/white/Pages/dr.html\n"+
      "crawls/white/Pages/links.html\n"+
      "crawls/white/Pages/research.html\n"+
      "crawls/white/Pages/teaching.html\n"+
      "crawls/white/Pages/vyacc.html\n"+
      "crawls/white/Vyacc/dist.html\n"+
      "crawls/white/Vyacc/vyacc_readme.html\n"+
      "crawls/white/index.html\n"+
      "";
    skipped =
      "file://thumper.cs.umd.edu/files/docs/umcp-cs.html\n"+
      "http://cra.org/Activities/craw/\n"+
      "http://cs.gmu.edu\n"+
      "http://cs.gmu.edu/~white/Pages/Gifs/hopper.gif\n"+
      "http://cs.gmu.edu/~white/Pages/Syllabi/syllabus477F15.pdf\n"+
      "http://cs.gmu.edu/~white/Papers/cgf6.ps\n"+
      "http://cs.gmu.edu/~white/Papers/dis12.ps\n"+
      "http://cs.gmu.edu/~white/Papers/dis13.ps\n"+
      "http://cs.gmu.edu/~white/Papers/dis14.ps\n"+
      "http://cs.gmu.edu/~white/Vyacc/vyacc.tar.gz\n"+
      "http://info.acm.org/\n"+
      "http://jiju.gmu.edu/catalog/apolicies/honor.html\n"+
      "http://liinwww.ira.uka.de/bibliography/index.html\n"+
      "http://nac.gmu.edu/~kfrosch/96-15-012.ps\n"+
      "http://oai.gmu.edu/honor-code/\n"+
      "http://ods.gmu.edu\n"+
      "http://src.doc.ic.ac.uk/bySubject/Computing/Overview.html\n"+
      "http://vse.gmu.edu\n"+
      "http://www.ai.mit.edu/people/ellens/gender.html\n"+
      "http://www.cera2.com/softeng.htm\n"+
      "http://www.comlab.ox.ac.uk/archive/concurrent.html\n"+
      "http://www.computer.org/\n"+
      "http://www.cra.org\n"+
      "http://www.cra.org/\n"+
      "http://www.cs.gmu.edu/\n"+
      "http://www.cs.gmu.edu/dpcl.html\n"+
      "http://www.cs.gmu.edu/~cfia\n"+
      "http://www.cs.gmu.edu/~white\n"+
      "http://www.cs.gmu.edu/~white/CS363/syllabusS12.html\n"+
      "http://www.cs.gmu.edu/~white/CS707/707syllabus.html\n"+
      "http://www.cs.rice.edu/~roth/conferences.html\n"+
      "http://www.cs.umd.edu/~pugh/java/\n"+
      "http://www.cs.wisc.edu/~markhill/conference-etiquette.html\n"+
      "http://www.cs.wisc.edu/~markhill/conference-talk.html\n"+
      "http://www.cs.yale.edu/homes/tap/\n"+
      "http://www.cs.yale.edu/homes/tap/tap-resources.html\n"+
      "http://www.cs.yale.edu/homes/tap/tap.html\n"+
      "http://www.gmu.edu\n"+
      "http://www.iftech.com/oltc/c/c0.stm\n"+
      "http://www.ise.gmu.edu/SERL\n"+
      "http://www.isse.gmu.edu/~ofut/hotlist.html#THOUGHTS\n"+
      "http://www.javasoft.com:80/products/jdk/1.1/docs/index.html\n"+
      "http://www.javaworld.com/javaworld\n"+
      "http://www.lincom-asg.com/~rjamison/byacc/\n"+
      "http://www.lpac.ac.uk/SEL-HPC/Articles/CompilersArchive.html\n"+
      "http://www.ncstrl.org/\n"+
      "http://www.nsf.gov/\n"+
      "http://www.onr.navy.mil/\n"+
      "http://www.osf.org/mall/\n"+
      "http://www.phoaks.com/phoaks/comp/lang/ada/\n"+
      "http://www.sdsc.edu/Hopper/\n"+
      "http://www.sei.cmu.edu/\n"+
      "http://www.suntest.com/Jack/\n"+
      "http://www.yahoo.com/Science/Computer_Science/Distributed_Computing/\n"+
      "javascript:alert('An alert');\n"+
      "mailto:white@cs.gmu.edu\n"+
      "spider-man.png\n"+
      "";
    check_crawler_no_crawl(c, pending, found, skipped);
  }


  public static void check_crawler(String startPage, String expectFound, String expectSkipped)
  {
    Crawler c = new IterativeCrawler();
    c.crawl(startPage);
    String msg, expect, actual;
    // foundPagesString()
    expect = expectFound;
    actual = c.foundPagesString();
    msg = "";
    msg += "Found pages wrong:\n";
    msg += String.format("Start Page: %s\n",startPage);
    msg += String.format("Expect:\n%s\n",expect);
    msg += String.format("Actual:\n%s\n",actual);
    assertEquals(msg, expect, actual);
    // skippedPagesString()
    if(expectSkipped != null){
      expect = expectSkipped;
      actual = c.skippedPagesString();
      msg = "";
      msg += "Skipped pages wrong:\n";
      msg += String.format("Start Page: %s\n",startPage);
      msg += String.format("Expect:\n%s\n",expect);
      msg += String.format("Actual:\n%s\n",actual);
      assertEquals(msg, expect, actual);
    }
    // foundPagesList()
    expect = "[" + expectFound.replaceAll("\n",", ").replaceAll(", $","") + "]";
    actual = c.foundPagesList().toString();
    msg = "";
    msg += "Found pages list wrong:\n";
    msg += String.format("Start Page: %s\n",startPage);
    msg += String.format("Expect:\n%s\n",expect);
    msg += String.format("Actual:\n%s\n",actual);
    assertEquals(msg, expect, actual);
    // skippedPagesList()
    if(expectSkipped != null){
      expect = "[" + expectSkipped.replaceAll("\n",", ").replaceAll(", $","") + "]";
      actual = c.skippedPagesList().toString();
      msg = "";
      msg += "Skipped pages list wrong:\n";
      msg += String.format("Start Page: %s\n",startPage);
      msg += String.format("Expect:\n%s\n",expect);
      msg += String.format("Actual:\n%s\n",actual);
      assertEquals(msg, expect, actual);
    }
  }

  @Test public void crawl_start(){
    String startPage = "crawls/small-site/start.html";
    String expectFound = 
      "crawls/small-site/A.html\n"+
      "crawls/small-site/B.html\n"+
      "crawls/small-site/C.html\n"+
      "crawls/small-site/start.html\n"+
      "crawls/small-site/subdir/D.html\n"+
      "crawls/small-site/subdir/E.html\n"+
      "crawls/small-site/subdir/F.html\n"+
      "";
    String expectSkipped =
      "http://cs.gmu.edu\n"+
      "javascript:alert('An alert');\n"+
      "spider-man.png\n"+
      "";
    check_crawler(startPage,expectFound,expectSkipped);
  }

  @Test public void crawl_A(){
    String startPage = "crawls/small-site/A.html";
    String expectFound = 
      "crawls/small-site/A.html\n"+
      "crawls/small-site/B.html\n"+
      "crawls/small-site/C.html\n"+
      "crawls/small-site/start.html\n"+
      "crawls/small-site/subdir/D.html\n"+
      "crawls/small-site/subdir/E.html\n"+
      "crawls/small-site/subdir/F.html\n"+
      "";
    String expectSkipped =
      "http://cs.gmu.edu\n"+
      "javascript:alert('An alert');\n"+
      "spider-man.png\n"+
      "";
    check_crawler(startPage,expectFound,expectSkipped);
  }
  @Test public void crawl_B(){
    String startPage = "crawls/small-site/B.html";
    String expectFound = 
      "crawls/small-site/A.html\n"+
      "crawls/small-site/B.html\n"+
      "crawls/small-site/C.html\n"+
      "crawls/small-site/start.html\n"+
      "crawls/small-site/subdir/D.html\n"+
      "crawls/small-site/subdir/E.html\n"+
      "crawls/small-site/subdir/F.html\n"+
      "";
    String expectSkipped =
      "http://cs.gmu.edu\n"+
      "javascript:alert('An alert');\n"+
      "spider-man.png\n"+
      "";
    check_crawler(startPage,expectFound,expectSkipped);
  }
  @Test public void crawl_C(){
    String startPage = "crawls/small-site/C.html";
    String expectFound = 
      "crawls/small-site/C.html\n"+
      "";
    String expectSkipped =
      "";
    check_crawler(startPage,expectFound,expectSkipped);
  }
  @Test public void crawl_F(){
    String startPage = "crawls/small-site/subdir/F.html";
    String expectFound = 
      "crawls/small-site/subdir/F.html\n"+
      "";
    String expectSkipped =
      "";
    check_crawler(startPage,expectFound,expectSkipped);
  }
  @Test public void crawl_D(){
    String startPage = "crawls/small-site/subdir/D.html";
    String expectFound = 
      "crawls/small-site/A.html\n"+
      "crawls/small-site/B.html\n"+
      "crawls/small-site/C.html\n"+
      "crawls/small-site/start.html\n"+
      "crawls/small-site/subdir/D.html\n"+
      "crawls/small-site/subdir/E.html\n"+
      "crawls/small-site/subdir/F.html\n"+
      "";
    String expectSkipped =
      "http://cs.gmu.edu\n"+
      "javascript:alert('An alert');\n"+
      "spider-man.png\n"+
      "";
    check_crawler(startPage,expectFound,expectSkipped);
  }
  @Test public void crawl_white_teaching(){
    String startPage = "crawls/white/Pages/teaching.html";
    String expectFound = 
      "crawls/white/Pages/Syllabi/syllabus367F15.html\n"+
      "crawls/white/Pages/Syllabi/syllabus440S15.html\n"+
      "crawls/white/Pages/Syllabi/syllabus540F14.html\n"+
      "crawls/white/Pages/Syllabi/syllabus640S13.html\n"+
      "crawls/white/Pages/teaching.html\n"+
      "";
    String expectSkipped =
      "http://cs.gmu.edu/~white/Pages/Syllabi/syllabus477F15.pdf\n"+
      "http://jiju.gmu.edu/catalog/apolicies/honor.html\n"+
      "http://oai.gmu.edu/honor-code/\n"+
      "http://ods.gmu.edu\n"+
      "http://www.cs.gmu.edu/~white\n"+
      "http://www.cs.gmu.edu/~white/CS363/syllabusS12.html\n"+
      "http://www.cs.gmu.edu/~white/CS707/707syllabus.html\n"+
      "";
    check_crawler(startPage,expectFound,expectSkipped);
  }
  @Test public void crawl_white_vyacc(){
    String startPage = "crawls/white/Pages/vyacc.html";
    String expectFound = 
      "crawls/white/Pages/cra.html\n"+
      "crawls/white/Pages/vyacc.html\n"+
      "crawls/white/Vyacc/dist.html\n"+
      "crawls/white/Vyacc/vyacc_readme.html\n"+
      "";
    String expectSkipped =
      "http://cs.gmu.edu/~white/Vyacc/vyacc.tar.gz\n"+
      "http://www.cra.org\n"+
      "";
    check_crawler(startPage,expectFound,expectSkipped);
  }
  @Test public void crawl_white_index(){
    String startPage = "crawls/white/index.html";
    String expectFound = 
      "crawls/white/Pages/Syllabi/syllabus367F15.html\n"+
      "crawls/white/Pages/Syllabi/syllabus440S15.html\n"+
      "crawls/white/Pages/Syllabi/syllabus540F14.html\n"+
      "crawls/white/Pages/Syllabi/syllabus640S13.html\n"+
      "crawls/white/Pages/ci.html\n"+
      "crawls/white/Pages/cra.html\n"+
      "crawls/white/Pages/dis.html\n"+
      "crawls/white/Pages/dr.html\n"+
      "crawls/white/Pages/links.html\n"+
      "crawls/white/Pages/research.html\n"+
      "crawls/white/Pages/teaching.html\n"+
      "crawls/white/Pages/vyacc.html\n"+
      "crawls/white/Vyacc/dist.html\n"+
      "crawls/white/Vyacc/vyacc_readme.html\n"+
      "crawls/white/index.html\n"+
      "";
    String expectSkipped =
      "file://thumper.cs.umd.edu/files/docs/umcp-cs.html\n"+
      "http://cra.org/Activities/craw/\n"+
      "http://cs.gmu.edu/~white/Pages/Gifs/hopper.gif\n"+
      "http://cs.gmu.edu/~white/Pages/Syllabi/syllabus477F15.pdf\n"+
      "http://cs.gmu.edu/~white/Papers/cgf6.ps\n"+
      "http://cs.gmu.edu/~white/Papers/dis12.ps\n"+
      "http://cs.gmu.edu/~white/Papers/dis13.ps\n"+
      "http://cs.gmu.edu/~white/Papers/dis14.ps\n"+
      "http://cs.gmu.edu/~white/Vyacc/vyacc.tar.gz\n"+
      "http://info.acm.org/\n"+
      "http://jiju.gmu.edu/catalog/apolicies/honor.html\n"+
      "http://liinwww.ira.uka.de/bibliography/index.html\n"+
      "http://nac.gmu.edu/~kfrosch/96-15-012.ps\n"+
      "http://oai.gmu.edu/honor-code/\n"+
      "http://ods.gmu.edu\n"+
      "http://src.doc.ic.ac.uk/bySubject/Computing/Overview.html\n"+
      "http://vse.gmu.edu\n"+
      "http://www.ai.mit.edu/people/ellens/gender.html\n"+
      "http://www.cera2.com/softeng.htm\n"+
      "http://www.comlab.ox.ac.uk/archive/concurrent.html\n"+
      "http://www.computer.org/\n"+
      "http://www.cra.org\n"+
      "http://www.cra.org/\n"+
      "http://www.cs.gmu.edu/\n"+
      "http://www.cs.gmu.edu/dpcl.html\n"+
      "http://www.cs.gmu.edu/~cfia\n"+
      "http://www.cs.gmu.edu/~white\n"+
      "http://www.cs.gmu.edu/~white/CS363/syllabusS12.html\n"+
      "http://www.cs.gmu.edu/~white/CS707/707syllabus.html\n"+
      "http://www.cs.rice.edu/~roth/conferences.html\n"+
      "http://www.cs.umd.edu/~pugh/java/\n"+
      "http://www.cs.wisc.edu/~markhill/conference-etiquette.html\n"+
      "http://www.cs.wisc.edu/~markhill/conference-talk.html\n"+
      "http://www.cs.yale.edu/homes/tap/\n"+
      "http://www.cs.yale.edu/homes/tap/tap-resources.html\n"+
      "http://www.cs.yale.edu/homes/tap/tap.html\n"+
      "http://www.gmu.edu\n"+
      "http://www.iftech.com/oltc/c/c0.stm\n"+
      "http://www.ise.gmu.edu/SERL\n"+
      "http://www.isse.gmu.edu/~ofut/hotlist.html#THOUGHTS\n"+
      "http://www.javasoft.com:80/products/jdk/1.1/docs/index.html\n"+
      "http://www.javaworld.com/javaworld\n"+
      "http://www.lincom-asg.com/~rjamison/byacc/\n"+
      "http://www.lpac.ac.uk/SEL-HPC/Articles/CompilersArchive.html\n"+
      "http://www.ncstrl.org/\n"+
      "http://www.nsf.gov/\n"+
      "http://www.onr.navy.mil/\n"+
      "http://www.osf.org/mall/\n"+
      "http://www.phoaks.com/phoaks/comp/lang/ada/\n"+
      "http://www.sdsc.edu/Hopper/\n"+
      "http://www.sei.cmu.edu/\n"+
      "http://www.suntest.com/Jack/\n"+
      "http://www.yahoo.com/Science/Computer_Science/Distributed_Computing/\n"+
      "mailto:white@cs.gmu.edu\n"+
      "";
    check_crawler(startPage,expectFound,expectSkipped);
  }
  @Test public void crawl_sean_book_index(){
    String startPage = "crawls/sean/book/metaheuristics/index.html";
    String expectFound = 
      "crawls/sean/book/metaheuristics/index.html\n"+
      "";
    String expectSkipped =
      "http://creativecommons.org/licenses/by-nd/3.0/us/\n"+
      "http://cs.gmu.edu/~eclab/projects/ecj\n"+
      "http://qai.narod.ru/GA/metaheuristics.html\n"+
      "http://rogeralsing.com/2008/12/07/genetic-programming-evolution-of-mona-lisa/\n"+
      "http://www.amazon.com/gp/product/1300549629/ref=as_li_tl?ie=UTF8&camp=1789&creative=390957&creativeASIN=1300549629&linkCode=as2&tag=essentiofmeta-20&linkId=AZZMWSXQDDIGFYJQ\n"+
      "http://www.lulu.com/shop/sean-luke/essentials-of-metaheuristics-second-edition/paperback/product-21080150.html\n"+
      "http://www.springer.com/computer/ai/journal/10710\n"+
      "http://www.springerlink.com/content/m35866530833v367/\n"+
      "https://cs.gmu.edu/~sean/book/metaheuristics/Essentials.pdf\n"+
      "https://cs.gmu.edu/~sean/book/metaheuristics/Essentials.zip\n"+
      "mailto:metaheuristics@cs.gmu.edu\n"+
      "";
    check_crawler(startPage,expectFound,expectSkipped);
  }
  @Test public void crawl_sean_lil_gp_index(){
    String startPage = "crawls/sean/research/lil-gp-patch/index.html";
    String expectFound = 
      "crawls/sean/research/lil-gp-patch/bugs.html\n"+
      "crawls/sean/research/lil-gp-patch/index.html\n"+
      "crawls/sean/research/lil-gp-patch/peters-patch.html\n"+
      "";
    String expectSkipped =
      "gp.tar.gz\n"+
      "http://garage.cps.msu.edu/software/software-index.html#lilgp\n"+
      "http://www.cs.umd.edu/users/seanl/patched-gp/gp.tar.gz\n"+
      "http://www.daimi.aau.dk/~ptr/\n"+
      "http://www.robocup.org/RoboCup/RoboCup.html\n"+
      "";
    check_crawler(startPage,expectFound,expectSkipped);
  }
  @Test public void crawl_sean_index(){
    String startPage = "crawls/sean/index.html";
    String expectFound = 
      "crawls/sean/book/metaheuristics/index.html\n"+
      "crawls/sean/index.html\n"+
      "crawls/sean/lisp/LispTutorial.html\n"+
      "crawls/sean/lisp/cons.html\n"+
      "crawls/sean/lisp/index.html\n"+
      "crawls/sean/papers/index.html\n"+
      "crawls/sean/projects/gizmo/index.html\n"+
      "crawls/sean/projects/newton/BigCountdown/index.html\n"+
      "crawls/sean/projects/newton/Books/index.html\n"+
      "crawls/sean/projects/newton/Emma/index.html\n"+
      "crawls/sean/projects/newton/MP100Power.html\n"+
      "crawls/sean/projects/newton/Overlord/index.html\n"+
      "crawls/sean/projects/newton/Phantom/index.html\n"+
      "crawls/sean/projects/newton/Radicals/index.html\n"+
      "crawls/sean/projects/newton/ToyStory/index.html\n"+
      "crawls/sean/projects/newton/YaleInput/index.html\n"+
      "crawls/sean/projects/next/Resound/index.html\n"+
      "crawls/sean/research.1.html\n"+
      "crawls/sean/research/lil-gp-patch/bugs.html\n"+
      "crawls/sean/research/lil-gp-patch/index.html\n"+
      "crawls/sean/research/lil-gp-patch/peters-patch.html\n"+
      "crawls/sean/research/mersenne.1.html\n"+
      "crawls/sean/stuff.html\n"+
      "crawls/sean/stuff/java-objc.html\n"+
      "";
    String expectSkipped = null;
    check_crawler(startPage,expectFound,expectSkipped);
  }
  @Test public void crawl_sean_stuff(){
    String startPage = "crawls/sean/stuff.html";
    String expectFound = 
      "crawls/sean/book/metaheuristics/index.html\n"+
      "crawls/sean/index.html\n"+
      "crawls/sean/lisp/LispTutorial.html\n"+
      "crawls/sean/lisp/cons.html\n"+
      "crawls/sean/lisp/index.html\n"+
      "crawls/sean/papers/index.html\n"+
      "crawls/sean/projects/gizmo/index.html\n"+
      "crawls/sean/projects/newton/BigCountdown/index.html\n"+
      "crawls/sean/projects/newton/Books/index.html\n"+
      "crawls/sean/projects/newton/Emma/index.html\n"+
      "crawls/sean/projects/newton/MP100Power.html\n"+
      "crawls/sean/projects/newton/Overlord/index.html\n"+
      "crawls/sean/projects/newton/Phantom/index.html\n"+
      "crawls/sean/projects/newton/Radicals/index.html\n"+
      "crawls/sean/projects/newton/ToyStory/index.html\n"+
      "crawls/sean/projects/newton/YaleInput/index.html\n"+
      "crawls/sean/projects/next/Resound/index.html\n"+
      "crawls/sean/research.1.html\n"+
      "crawls/sean/research/lil-gp-patch/bugs.html\n"+
      "crawls/sean/research/lil-gp-patch/index.html\n"+
      "crawls/sean/research/lil-gp-patch/peters-patch.html\n"+
      "crawls/sean/research/mersenne.1.html\n"+
      "crawls/sean/stuff.html\n"+
      "crawls/sean/stuff/java-objc.html\n"+
      "";
    String expectSkipped = null;
    check_crawler(startPage,expectFound,expectSkipped);
  }

  // Don't crawl pages, just check found/skipped are correct
  public static void check_crawler_nocrawl(Crawler c, String expectFound, String expectSkipped)
  {
    String msg, expect, actual;
    // foundPagesString()
    expect = expectFound;
    actual = c.foundPagesString();
    msg = "";
    msg += "Found pages wrong:\n";
    msg += String.format("Expect:\n%s\n",expect);
    msg += String.format("Actual:\n%s\n",actual);
    assertEquals(msg, expect, actual);
    // skippedPagesString()
    if(expectSkipped != null){
      expect = expectSkipped;
      actual = c.skippedPagesString();
      msg = "";
      msg += "Skipped pages wrong:\n";
      msg += String.format("Expect:\n%s\n",expect);
      msg += String.format("Actual:\n%s\n",actual);
      assertEquals(msg, expect, actual);
    }
  }
  @Test public void crawl_start_start(){
    String startPage, expectFound, expectSkipped;
    Crawler c = new RecursiveCrawler();
    // 
    startPage = "crawls/small-site/start.html";
    c.crawl(startPage);
    expectFound = 
      "crawls/small-site/A.html\n"+
      "crawls/small-site/B.html\n"+
      "crawls/small-site/C.html\n"+
      "crawls/small-site/start.html\n"+
      "crawls/small-site/subdir/D.html\n"+
      "crawls/small-site/subdir/E.html\n"+
      "crawls/small-site/subdir/F.html\n"+
      "";
    expectSkipped =
      "http://cs.gmu.edu\n"+
      "javascript:alert('An alert');\n"+
      "spider-man.png\n"+
      "";
    check_crawler_nocrawl(c,expectFound,expectSkipped);
    //
    startPage = "crawls/small-site/start.html";
    c.crawl(startPage);
    expectFound = 
      "crawls/small-site/A.html\n"+
      "crawls/small-site/B.html\n"+
      "crawls/small-site/C.html\n"+
      "crawls/small-site/start.html\n"+
      "crawls/small-site/subdir/D.html\n"+
      "crawls/small-site/subdir/E.html\n"+
      "crawls/small-site/subdir/F.html\n"+
      "";
    expectSkipped =
      "http://cs.gmu.edu\n"+
      "javascript:alert('An alert');\n"+
      "spider-man.png\n"+
      "";
    check_crawler_nocrawl(c,expectFound,expectSkipped);
  }
  @Test public void crawl_start_white(){
    String startPage, expectFound, expectSkipped;
    Crawler c = new RecursiveCrawler();
    // 
    startPage = "crawls/small-site/start.html";
    c.crawl(startPage);
    expectFound = 
      "crawls/small-site/A.html\n"+
      "crawls/small-site/B.html\n"+
      "crawls/small-site/C.html\n"+
      "crawls/small-site/start.html\n"+
      "crawls/small-site/subdir/D.html\n"+
      "crawls/small-site/subdir/E.html\n"+
      "crawls/small-site/subdir/F.html\n"+
      "";
    expectSkipped =
      "http://cs.gmu.edu\n"+
      "javascript:alert('An alert');\n"+
      "spider-man.png\n"+
      "";
    check_crawler_nocrawl(c,expectFound,expectSkipped);
    //
    startPage = "crawls/white/index.html";
    c.crawl(startPage);
    expectFound = 
      "crawls/small-site/A.html\n"+
      "crawls/small-site/B.html\n"+
      "crawls/small-site/C.html\n"+
      "crawls/small-site/start.html\n"+
      "crawls/small-site/subdir/D.html\n"+
      "crawls/small-site/subdir/E.html\n"+
      "crawls/small-site/subdir/F.html\n"+
      "crawls/white/Pages/Syllabi/syllabus367F15.html\n"+
      "crawls/white/Pages/Syllabi/syllabus440S15.html\n"+
      "crawls/white/Pages/Syllabi/syllabus540F14.html\n"+
      "crawls/white/Pages/Syllabi/syllabus640S13.html\n"+
      "crawls/white/Pages/ci.html\n"+
      "crawls/white/Pages/cra.html\n"+
      "crawls/white/Pages/dis.html\n"+
      "crawls/white/Pages/dr.html\n"+
      "crawls/white/Pages/links.html\n"+
      "crawls/white/Pages/research.html\n"+
      "crawls/white/Pages/teaching.html\n"+
      "crawls/white/Pages/vyacc.html\n"+
      "crawls/white/Vyacc/dist.html\n"+
      "crawls/white/Vyacc/vyacc_readme.html\n"+
      "crawls/white/index.html\n"+
      "";
    expectSkipped =
      "file://thumper.cs.umd.edu/files/docs/umcp-cs.html\n"+
      "http://cra.org/Activities/craw/\n"+
      "http://cs.gmu.edu\n"+
      "http://cs.gmu.edu/~white/Pages/Gifs/hopper.gif\n"+
      "http://cs.gmu.edu/~white/Pages/Syllabi/syllabus477F15.pdf\n"+
      "http://cs.gmu.edu/~white/Papers/cgf6.ps\n"+
      "http://cs.gmu.edu/~white/Papers/dis12.ps\n"+
      "http://cs.gmu.edu/~white/Papers/dis13.ps\n"+
      "http://cs.gmu.edu/~white/Papers/dis14.ps\n"+
      "http://cs.gmu.edu/~white/Vyacc/vyacc.tar.gz\n"+
      "http://info.acm.org/\n"+
      "http://jiju.gmu.edu/catalog/apolicies/honor.html\n"+
      "http://liinwww.ira.uka.de/bibliography/index.html\n"+
      "http://nac.gmu.edu/~kfrosch/96-15-012.ps\n"+
      "http://oai.gmu.edu/honor-code/\n"+
      "http://ods.gmu.edu\n"+
      "http://src.doc.ic.ac.uk/bySubject/Computing/Overview.html\n"+
      "http://vse.gmu.edu\n"+
      "http://www.ai.mit.edu/people/ellens/gender.html\n"+
      "http://www.cera2.com/softeng.htm\n"+
      "http://www.comlab.ox.ac.uk/archive/concurrent.html\n"+
      "http://www.computer.org/\n"+
      "http://www.cra.org\n"+
      "http://www.cra.org/\n"+
      "http://www.cs.gmu.edu/\n"+
      "http://www.cs.gmu.edu/dpcl.html\n"+
      "http://www.cs.gmu.edu/~cfia\n"+
      "http://www.cs.gmu.edu/~white\n"+
      "http://www.cs.gmu.edu/~white/CS363/syllabusS12.html\n"+
      "http://www.cs.gmu.edu/~white/CS707/707syllabus.html\n"+
      "http://www.cs.rice.edu/~roth/conferences.html\n"+
      "http://www.cs.umd.edu/~pugh/java/\n"+
      "http://www.cs.wisc.edu/~markhill/conference-etiquette.html\n"+
      "http://www.cs.wisc.edu/~markhill/conference-talk.html\n"+
      "http://www.cs.yale.edu/homes/tap/\n"+
      "http://www.cs.yale.edu/homes/tap/tap-resources.html\n"+
      "http://www.cs.yale.edu/homes/tap/tap.html\n"+
      "http://www.gmu.edu\n"+
      "http://www.iftech.com/oltc/c/c0.stm\n"+
      "http://www.ise.gmu.edu/SERL\n"+
      "http://www.isse.gmu.edu/~ofut/hotlist.html#THOUGHTS\n"+
      "http://www.javasoft.com:80/products/jdk/1.1/docs/index.html\n"+
      "http://www.javaworld.com/javaworld\n"+
      "http://www.lincom-asg.com/~rjamison/byacc/\n"+
      "http://www.lpac.ac.uk/SEL-HPC/Articles/CompilersArchive.html\n"+
      "http://www.ncstrl.org/\n"+
      "http://www.nsf.gov/\n"+
      "http://www.onr.navy.mil/\n"+
      "http://www.osf.org/mall/\n"+
      "http://www.phoaks.com/phoaks/comp/lang/ada/\n"+
      "http://www.sdsc.edu/Hopper/\n"+
      "http://www.sei.cmu.edu/\n"+
      "http://www.suntest.com/Jack/\n"+
      "http://www.yahoo.com/Science/Computer_Science/Distributed_Computing/\n"+
      "javascript:alert('An alert');\n"+
      "mailto:white@cs.gmu.edu\n"+
      "spider-man.png\n"+
      "";
    check_crawler_nocrawl(c,expectFound,expectSkipped);
  }
  @Test public void crawl_white_start(){
    String startPage, expectFound, expectSkipped;
    Crawler c = new RecursiveCrawler();
    // 
    startPage = "crawls/white/index.html";
    c.crawl(startPage);
    expectFound = 
      "crawls/white/Pages/Syllabi/syllabus367F15.html\n"+
      "crawls/white/Pages/Syllabi/syllabus440S15.html\n"+
      "crawls/white/Pages/Syllabi/syllabus540F14.html\n"+
      "crawls/white/Pages/Syllabi/syllabus640S13.html\n"+
      "crawls/white/Pages/ci.html\n"+
      "crawls/white/Pages/cra.html\n"+
      "crawls/white/Pages/dis.html\n"+
      "crawls/white/Pages/dr.html\n"+
      "crawls/white/Pages/links.html\n"+
      "crawls/white/Pages/research.html\n"+
      "crawls/white/Pages/teaching.html\n"+
      "crawls/white/Pages/vyacc.html\n"+
      "crawls/white/Vyacc/dist.html\n"+
      "crawls/white/Vyacc/vyacc_readme.html\n"+
      "crawls/white/index.html\n"+
      "";
    expectSkipped =
      "file://thumper.cs.umd.edu/files/docs/umcp-cs.html\n"+
      "http://cra.org/Activities/craw/\n"+
      "http://cs.gmu.edu/~white/Pages/Gifs/hopper.gif\n"+
      "http://cs.gmu.edu/~white/Pages/Syllabi/syllabus477F15.pdf\n"+
      "http://cs.gmu.edu/~white/Papers/cgf6.ps\n"+
      "http://cs.gmu.edu/~white/Papers/dis12.ps\n"+
      "http://cs.gmu.edu/~white/Papers/dis13.ps\n"+
      "http://cs.gmu.edu/~white/Papers/dis14.ps\n"+
      "http://cs.gmu.edu/~white/Vyacc/vyacc.tar.gz\n"+
      "http://info.acm.org/\n"+
      "http://jiju.gmu.edu/catalog/apolicies/honor.html\n"+
      "http://liinwww.ira.uka.de/bibliography/index.html\n"+
      "http://nac.gmu.edu/~kfrosch/96-15-012.ps\n"+
      "http://oai.gmu.edu/honor-code/\n"+
      "http://ods.gmu.edu\n"+
      "http://src.doc.ic.ac.uk/bySubject/Computing/Overview.html\n"+
      "http://vse.gmu.edu\n"+
      "http://www.ai.mit.edu/people/ellens/gender.html\n"+
      "http://www.cera2.com/softeng.htm\n"+
      "http://www.comlab.ox.ac.uk/archive/concurrent.html\n"+
      "http://www.computer.org/\n"+
      "http://www.cra.org\n"+
      "http://www.cra.org/\n"+
      "http://www.cs.gmu.edu/\n"+
      "http://www.cs.gmu.edu/dpcl.html\n"+
      "http://www.cs.gmu.edu/~cfia\n"+
      "http://www.cs.gmu.edu/~white\n"+
      "http://www.cs.gmu.edu/~white/CS363/syllabusS12.html\n"+
      "http://www.cs.gmu.edu/~white/CS707/707syllabus.html\n"+
      "http://www.cs.rice.edu/~roth/conferences.html\n"+
      "http://www.cs.umd.edu/~pugh/java/\n"+
      "http://www.cs.wisc.edu/~markhill/conference-etiquette.html\n"+
      "http://www.cs.wisc.edu/~markhill/conference-talk.html\n"+
      "http://www.cs.yale.edu/homes/tap/\n"+
      "http://www.cs.yale.edu/homes/tap/tap-resources.html\n"+
      "http://www.cs.yale.edu/homes/tap/tap.html\n"+
      "http://www.gmu.edu\n"+
      "http://www.iftech.com/oltc/c/c0.stm\n"+
      "http://www.ise.gmu.edu/SERL\n"+
      "http://www.isse.gmu.edu/~ofut/hotlist.html#THOUGHTS\n"+
      "http://www.javasoft.com:80/products/jdk/1.1/docs/index.html\n"+
      "http://www.javaworld.com/javaworld\n"+
      "http://www.lincom-asg.com/~rjamison/byacc/\n"+
      "http://www.lpac.ac.uk/SEL-HPC/Articles/CompilersArchive.html\n"+
      "http://www.ncstrl.org/\n"+
      "http://www.nsf.gov/\n"+
      "http://www.onr.navy.mil/\n"+
      "http://www.osf.org/mall/\n"+
      "http://www.phoaks.com/phoaks/comp/lang/ada/\n"+
      "http://www.sdsc.edu/Hopper/\n"+
      "http://www.sei.cmu.edu/\n"+
      "http://www.suntest.com/Jack/\n"+
      "http://www.yahoo.com/Science/Computer_Science/Distributed_Computing/\n"+
      "mailto:white@cs.gmu.edu\n"+
      "";
    check_crawler_nocrawl(c,expectFound,expectSkipped);
    //
    startPage = "crawls/small-site/start.html";
    c.crawl(startPage);
    expectFound = 
      "crawls/small-site/A.html\n"+
      "crawls/small-site/B.html\n"+
      "crawls/small-site/C.html\n"+
      "crawls/small-site/start.html\n"+
      "crawls/small-site/subdir/D.html\n"+
      "crawls/small-site/subdir/E.html\n"+
      "crawls/small-site/subdir/F.html\n"+
      "crawls/white/Pages/Syllabi/syllabus367F15.html\n"+
      "crawls/white/Pages/Syllabi/syllabus440S15.html\n"+
      "crawls/white/Pages/Syllabi/syllabus540F14.html\n"+
      "crawls/white/Pages/Syllabi/syllabus640S13.html\n"+
      "crawls/white/Pages/ci.html\n"+
      "crawls/white/Pages/cra.html\n"+
      "crawls/white/Pages/dis.html\n"+
      "crawls/white/Pages/dr.html\n"+
      "crawls/white/Pages/links.html\n"+
      "crawls/white/Pages/research.html\n"+
      "crawls/white/Pages/teaching.html\n"+
      "crawls/white/Pages/vyacc.html\n"+
      "crawls/white/Vyacc/dist.html\n"+
      "crawls/white/Vyacc/vyacc_readme.html\n"+
      "crawls/white/index.html\n"+
      "";
    expectSkipped =
      "file://thumper.cs.umd.edu/files/docs/umcp-cs.html\n"+
      "http://cra.org/Activities/craw/\n"+
      "http://cs.gmu.edu\n"+
      "http://cs.gmu.edu/~white/Pages/Gifs/hopper.gif\n"+
      "http://cs.gmu.edu/~white/Pages/Syllabi/syllabus477F15.pdf\n"+
      "http://cs.gmu.edu/~white/Papers/cgf6.ps\n"+
      "http://cs.gmu.edu/~white/Papers/dis12.ps\n"+
      "http://cs.gmu.edu/~white/Papers/dis13.ps\n"+
      "http://cs.gmu.edu/~white/Papers/dis14.ps\n"+
      "http://cs.gmu.edu/~white/Vyacc/vyacc.tar.gz\n"+
      "http://info.acm.org/\n"+
      "http://jiju.gmu.edu/catalog/apolicies/honor.html\n"+
      "http://liinwww.ira.uka.de/bibliography/index.html\n"+
      "http://nac.gmu.edu/~kfrosch/96-15-012.ps\n"+
      "http://oai.gmu.edu/honor-code/\n"+
      "http://ods.gmu.edu\n"+
      "http://src.doc.ic.ac.uk/bySubject/Computing/Overview.html\n"+
      "http://vse.gmu.edu\n"+
      "http://www.ai.mit.edu/people/ellens/gender.html\n"+
      "http://www.cera2.com/softeng.htm\n"+
      "http://www.comlab.ox.ac.uk/archive/concurrent.html\n"+
      "http://www.computer.org/\n"+
      "http://www.cra.org\n"+
      "http://www.cra.org/\n"+
      "http://www.cs.gmu.edu/\n"+
      "http://www.cs.gmu.edu/dpcl.html\n"+
      "http://www.cs.gmu.edu/~cfia\n"+
      "http://www.cs.gmu.edu/~white\n"+
      "http://www.cs.gmu.edu/~white/CS363/syllabusS12.html\n"+
      "http://www.cs.gmu.edu/~white/CS707/707syllabus.html\n"+
      "http://www.cs.rice.edu/~roth/conferences.html\n"+
      "http://www.cs.umd.edu/~pugh/java/\n"+
      "http://www.cs.wisc.edu/~markhill/conference-etiquette.html\n"+
      "http://www.cs.wisc.edu/~markhill/conference-talk.html\n"+
      "http://www.cs.yale.edu/homes/tap/\n"+
      "http://www.cs.yale.edu/homes/tap/tap-resources.html\n"+
      "http://www.cs.yale.edu/homes/tap/tap.html\n"+
      "http://www.gmu.edu\n"+
      "http://www.iftech.com/oltc/c/c0.stm\n"+
      "http://www.ise.gmu.edu/SERL\n"+
      "http://www.isse.gmu.edu/~ofut/hotlist.html#THOUGHTS\n"+
      "http://www.javasoft.com:80/products/jdk/1.1/docs/index.html\n"+
      "http://www.javaworld.com/javaworld\n"+
      "http://www.lincom-asg.com/~rjamison/byacc/\n"+
      "http://www.lpac.ac.uk/SEL-HPC/Articles/CompilersArchive.html\n"+
      "http://www.ncstrl.org/\n"+
      "http://www.nsf.gov/\n"+
      "http://www.onr.navy.mil/\n"+
      "http://www.osf.org/mall/\n"+
      "http://www.phoaks.com/phoaks/comp/lang/ada/\n"+
      "http://www.sdsc.edu/Hopper/\n"+
      "http://www.sei.cmu.edu/\n"+
      "http://www.suntest.com/Jack/\n"+
      "http://www.yahoo.com/Science/Computer_Science/Distributed_Computing/\n"+
      "javascript:alert('An alert');\n"+
      "mailto:white@cs.gmu.edu\n"+
      "spider-man.png\n"+
      "";
    check_crawler_nocrawl(c,expectFound,expectSkipped);
  }

}
