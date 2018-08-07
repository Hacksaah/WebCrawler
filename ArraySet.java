import java.util.*; //List, ArrayList, Collections

//A set with unique values that sorts values that are added to it
public class ArraySet<T extends Comparable<T>>
{
  private List<T> internal; //Internal List that will be used to make the ArraySet
  
  //Constructor to create an empty ArraySet
  public ArraySet()
  {
    this.internal = new ArrayList<>();
  }
  
  //Returns the number of elements in the ArraySet
  public int size()
  {
    return internal.size();
  }
  
  //Returns a List version of the ArraySet
  public List<T> asList()
  {
    return internal;
  }
  
  //Returns true if the given query is found in the ArraySet; false otherwise
  public boolean contains(T query)
  {
    int ans = Collections.binarySearch(internal, query);
    
    if(ans>=0)
      return true;
    else
      return false;
  }
  
  //Adds given item to ArraySet in its correct position. Returns true if item is adde; false otherwise
  //Throws a RuntimeException if the given item is null
  public boolean add(T item)
  {
    if(item == null)
      throw new RuntimeException();
    
    if(contains(item))
      return false;
    
    int pos = Math.abs(Collections.binarySearch(internal, item))-1;
    
    internal.add(pos, item);
    return true;
  }
  
  //Returns the item in the ArraySet that the given query is equal to; returns null if no item is equal
  public T get(T query)
  {
    int ans = Collections.binarySearch(internal, query);
    
    if(ans>=0)
      return internal.get(ans);
    else
      return null;
  }
  
  //Returns value at the given index
  public T getValue(int index)
  {
    return internal.get(index);
  }
  
  //Removes the Object at the given index and returns it
  public T remove(int index)
  {
    return this.internal.remove(index);
  }
  
  //Uses List's String representation as ArraySet's
  public String toString()
  {
    return internal.toString();
  }
}