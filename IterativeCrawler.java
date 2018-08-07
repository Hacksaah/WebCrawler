import java.util.ArrayList;
import java.io.*; //Document, File
import org.jsoup.nodes.*;
import org.jsoup.*;
import org.jsoup.select.*;

//An implementation of Crawler that does not use recursion, but iterates over
//an internally stored list of pages to be visited
public class IterativeCrawler extends Crawler
{
  protected ArraySet<String> pendingPages; //A List of valid pages waiting to be visited
  
  //Creates an empty Crawler
  public IterativeCrawler()
  {
    super();
    this.pendingPages = new ArraySet<>();
  }
  
  //Master crawl() method that will start at the given page and crawl
  //all reachable pages from there
 @Override public void crawl(String pageFileName)
 {
    super.foundPages.add(pageFileName); //adds given page to internal List of found pages
    
    try
    {
      File input = new File(pageFileName);
      Document doc = Jsoup.parse(input, "UTF-8");          //Creates a Document from the given page
      
      ArrayList<Element> links = doc.select("a[href]"); //Extracts all the links from the page and puts them in a List                       
      
      //Iterates through every link on the given page to verify if the links are valid pages.
      //If they are, the links will be added to the internal List of pendingPages; otherwise
      //they get added to the internal List of skippedPages
      for(Element link : links)
      {
        String linkText = link.attr("href");
        if(!validPageLink(linkText))      //Checks for validity of link
        {
          super.skippedPages.add(linkText);
          continue;
        }
        
        String linkFile = Util.relativeFileName(pageFileName, linkText);
        try
        {
          File testFile = new File(linkFile);
          Document testDoc = Jsoup.parse(testFile, "UTF-8"); //Checks if the link has any documentation
        }
        catch(Exception e)
        {
          super.skippedPages.add(linkFile);                //Adds to skippedPages if it doesn't
          continue;
        }
        
        addPendingPage(linkFile);                         //Ads to pendingPages if it does.
      }
      
      crawlRemaining();
    }
    catch(IOException e){}
  }
  
  //Enter a loop that crawls individual pages until there are none remaining
  public void crawlRemaining()
  {
    while(pendingPages.size() != 0)
    {
      crawlNextPage();
    }
  }
  
  //Adds the given page to the internal ArraySet
  public void addPendingPage(String pageFileName)
  {
    this.pendingPages.add(pageFileName);
  }
  
  //Returns the number of pages left to visit
  public int pendingPagesSize()
  {
    return this.pendingPages.size();
  }
  
  //Returns the pages remaining to be visited on seprate lines
  public String pendingPagesString()
  {
    String ans = "";
    for(int x=0; x<this.pendingPages.size(); x++)
    {
      ans += this.pendingPages.getValue(x)+"\n";
    }
    
    return ans;
  }
  
  //Removes the next page in the internal List and parses through its contents
  public void crawlNextPage()
  {
    String pageFileName = this.pendingPages.remove(this.pendingPages.size()-1);
    super.foundPages.add(pageFileName);
    
    try
    {
      File input = new File(pageFileName);
      Document doc = Jsoup.parse(input, "UTF-8");
      
      ArrayList<Element> links = doc.select("a[href]");
      
      for(Element link : links)
      {
        String linkText = link.attr("href");
        if(!validPageLink(linkText))
        {
          super.skippedPages.add(linkText);
          continue;
        }
        
        String linkFile = Util.relativeFileName(pageFileName, linkText);
        try
        {
          File testFile = new File(linkFile);
          Document testDoc = Jsoup.parse(testFile, "UTF-8");
        }
        catch(Exception e)
        {
          super.skippedPages.add(linkFile);
          continue;
        }
        
        if(!super.foundPages.contains(linkFile))
          addPendingPage(linkFile);
      }
    }
    catch(IOException e){}
  }
  
  public static void main(String[] args)
  {
    IterativeCrawler c = new IterativeCrawler();
    c.addPendingPage("crawls/white/index.html");
    c.crawlNextPage();
  }
}