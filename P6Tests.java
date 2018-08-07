import java.io.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test; // fixes some compile problems with annotations

public class P6Tests{

  public static void main(String args[]) {
    org.junit.runner.JUnitCore.main("ArraySetTests",
                                    "CrawlerTests",
                                    "RecursiveCrawlerTests",
                                    "IterativeCrawlerTests",
                                    "IndexEntryTests",
                                    "PageIndexTests");
  }

}
