import java.util.List;
import java.util.ArrayList;

//Abstract class to crawl linkied pages. Child classes will override crawl(page) method
public abstract class Crawler
{
  protected ArraySet<String> foundPages; //Any page the Crawler comes across
  protected ArraySet<String> skippedPages; //Pages the Crawler does not search through
  
  //Contructor to create an empty Crawler
  public Crawler()
  {
    foundPages = new ArraySet<>();
    skippedPages = new ArraySet<>();
  }
  
  //Initiates a crawl on the given page;
  //Child classes will override this method
  public abstract void crawl(String pageFileName);
  
  //Returns the unique pages that have been found by the Crawler
  public List<String> foundPagesList()
  {
    return this.foundPages.asList();
  }
  
  //Returns the unique pages that have been skipped by the Crawler
  public List<String> skippedPagesList()
  {
    return this.skippedPages.asList();
  }
  
  //Returns a String of pages that have been found separated by \n
  public String foundPagesString()
  {
    String ans = "";
    for(int x=0; x<this.foundPages.size(); x++)
    {
      ans += this.foundPages.getValue(x)+"\n";
    }
    
    return ans;
  }
  
  //Returns a String of pages that have been skipped separated by \n
  public String skippedPagesString()
  {
    String ans = "";
    for(int x=0; x<this.skippedPages.size(); x++)
    {
      ans += this.skippedPages.getValue(x)+"\n";
    }
    
    return ans;
  }
  
  //Returns ture if the given page is valid; false otherwise
  public static boolean validPageLink(String pageFileName)
  {
    int extension = pageFileName.length()-5;
    
    if(pageFileName.startsWith("http://") || pageFileName.startsWith("https://")
       || pageFileName.startsWith("file://"))
      return false;
    
    if(pageFileName.startsWith(".html",extension) || pageFileName.startsWith(".HTML",extension))
      return true;
    else
      return false;
  }
}