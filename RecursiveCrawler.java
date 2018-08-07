import java.util.ArrayList;
import java.io.*; //Document, File
import org.jsoup.nodes.*;
import org.jsoup.*;
import org.jsoup.select.*;

//A recursive implementation of a Crawler to chase all links from a starting point
public class RecursiveCrawler extends Crawler
{
  //Creates an empty Crawler
  public RecursiveCrawler()
  {
    super();
  }
  
  //Visits the given page and parses its contents.
  //Examines all the links on the page and should recursively visit
  //valid links. All pages that can be reached from the starting point
  //can be returned from the foundPages() methods
  @Override public void crawl(String pageFileName)
  {
    if(foundPages.contains(pageFileName))
      return;
    
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
        
        crawl(linkFile);
      }
    }
    catch(IOException e){}
  }
}
