import java.util.*; //List, ArrayList, Arrays, Collections, Scanner
import java.io.*; //Document, File, Exception
import org.jsoup.nodes.*;
import org.jsoup.*;
import org.jsoup.select.*;

//Index of terms found on pages and the associated pages on which they were found.
public class PageIndex
{
  protected ArraySet<IndexEntry> entries; //The set of IndexEntries which track search terms found on pages 
  //along with the pages on which they are found
  //Create an empty PageIndex
  public PageIndex()
  {
    this.entries = new ArraySet<>();
  }
  
  //Return the number of entries in the index
  public int size()
  {
    return this.entries.size();
  }
  
  //Returns the String representation of a PageIndex
  @Override public String toString()
  {
    String ans = "INDEX: "+size()+" entries\n--------------------\n";
    
    for(int x=0; x<this.entries.size(); x++)
    {
      ans += this.entries.getValue(x).toString();
    }
    
    return ans;
  }
  
  //Determine if the term given is valid. Valid terms do not show up
  //in Util.STOP_WORDS. Valid terms are also longer than one character.
  //Returns true if term is valid; false otherwise
  public boolean validTerm(String term)
  {
    term = term.toLowerCase();
    
    if(term.length() < 2)
      return false;
    
    int ans = Arrays.binarySearch(Util.STOP_WORDS, term);
    if(ans>=0)
      return false;
    else
      return true;
  }
  
  //Returns true if the internal ArraySet contains an IndexEntry with the given term;
  //false otherwise
  public boolean containsTerm(String term)
  {
    term = term.toLowerCase();
    for(int x=0; x<entries.size(); x++)
    {
      if(entries.getValue(x).getTerm().equals(term))
        return true;
    }
    return false;
  }
  
  //Returns a List of the pages associated with the given term and an
  //empty List if no pages are associated with the given term
  public List<String> getPagesWithTerm(String term)
  {
    IndexEntry fake = new IndexEntry(term);
    
    if(!containsTerm(term))
      return new ArrayList<String>();
    else
      return this.entries.get(fake).getPages();
  }
  
  //Will add the given term and page to the internal ArraySet if they do not already exist, then returns true.
  //If the term exists, but the page doesn't, the page will be added to the term, then returns true.
  //If the term and page both exist, false will be returned
  public boolean addTermAndPage(String term, String page)
  {
    IndexEntry newEntry = new IndexEntry(term);
    newEntry.addPage(page);
    
    if(containsTerm(term))
      return this.entries.get(newEntry).addPage(page);
    
    if(validTerm(term))
      return this.entries.add(newEntry);
    else
      return false;
  }
  
  //Parses through the given Crawler's foundPages and adds the pages to entries associated with the terms
  //found on the pages
  public void addCrawledPages(Crawler crawler)
  {
    List<String> pages = crawler.foundPagesList();
    Scanner sc;
    
    for(String page : pages)
    {
      try
      {
        File input = new File(page);
        Document doc = Jsoup.parse(input, "UTF-8");
        
        String body = doc.body().text();
        sc = new Scanner(body);
        sc.useDelimiter("(\\p{Space}|\\p{Punct}|\\xA0)+");
        
        while(sc.hasNext())
        {
          addTermAndPage(sc.next(), page);
        }
      }
      catch(Exception e){continue;}
    }
  }
  
  //Returns a List of the intersection between two given Lists
  public static List<String> intersectionOfSorted(List<String> x, List<String> y)
  {
    List<String> z = new ArrayList<>();
    
    for(String a : x)
    {
      int pos = Collections.binarySearch(y,a);
      if(pos >= 0)
        z.add(y.get(pos));
    }
    
    return z;
  }
  
  //Returns a List of the intersection between the pages of all the given terms
  public List<String> query(String queryString)
  {
    String[] queries = queryString.split(" ");
    if(queries.length == 1)
      return getPagesWithTerm(queries[0]);
    
    List<String> ans = getPagesWithTerm(queries[0]);
    for(int x=1; x<queries.length; x++)
    {
      ans = intersectionOfSorted(ans, getPagesWithTerm(queries[x]));
    }
    
    return ans;
  }
}