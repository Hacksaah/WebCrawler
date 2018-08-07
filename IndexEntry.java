import java.util.List;
import java.util.ArrayList;

//Entries in the PageIndex: represented by a term and tracks down every page with the given term
public class IndexEntry implements Comparable<IndexEntry>
{
  protected String term; //The String that will be looked for in every page
  protected ArraySet<String> pages; //Data structure to hold all the pages containing the term
  
  //Constuctor assigning the given term to the internal field and
  //an empty set of pages
  public IndexEntry(String term)
  {
    this.term = term.toLowerCase();
    this.pages = new ArraySet<>();
  }
  
  //Returns the internal term
  public String getTerm()
  {
    return this.term;
  }
  
  //Returns the internal List of pages
  public List<String> getPages()
  {
    return this.pages.asList();
  }
  
  //Returns true if the given String represents a page in the internal List
  public boolean containsPage(String pageFileName)
  {
    return this.pages.contains(pageFileName);
  }
  
  //Returns true if the given String is not in the internal List of pages and is added to it;
  //returns false otherwise
  public boolean addPage(String filename)
  {
    return this.pages.add(filename);
  }
  
  //Compares the internal terms to each other. Returns a negative number if this < that,
  //a positive number if this > that, and a zero if they're equal
  @Override public int compareTo(IndexEntry that)
  {
    return this.term.compareTo(that.getTerm());
  }
  
  //Returns true if the given Object is an IndexEntry and their terms are equal
  @Override public boolean equals(Object other)
  {
    if(!(other instanceof IndexEntry))
      return false;
    
    IndexEntry otherIE = (IndexEntry) other;
    if(this.compareTo(otherIE) == 0)
      return true;
    else
      return false;
  }
  
  //Returns the String representation of an IndexEntry
  @Override public String toString()
  {
    String ans = "@ "+this.term+"\n";
    
    for(int x=0; x<this.pages.size(); x++)
    {
      ans += this.pages.getValue(x)+"\n";
    }
    
    return ans;
  }
}