import java.util.List;



public class YelpSearch 
{
    public static final int resultsPerPull = 40;
    public static final String searchForTerm = "bubble tea";
    private String searchNearTerm;
    private String searchURL;
    
    List<YelpPage> yelpPages;
    
    public String getSearchNearTerm() {
        return searchNearTerm;
    }
    
    public void setSearchNearTerm(String searchNearTerm) {
        this.searchNearTerm = searchNearTerm;
        buildSearchURL();
    }
    
    public void startScan()
    {
        YelpPage firstPage = new YelpPage(searchURL, this);
        yelpPages = firstPage.getAllPagesAfter();
    }
    
    private void buildSearchURL()
    {
        this.searchURL = "http://www.yelp.com/search?find_desc="+cleanSearchForTerm(searchForTerm);
        this.searchURL += "&ns=1";
        this.searchURL += "&rpp="+this.resultsPerPull;
        this.searchURL += "&find_loc=" + this.searchNearTerm;
        this.searchURL += "#start=0";
    }
    
    private String cleanSearchForTerm(String term)
    {
        return term.replaceAll("\\s+", "+");
    }

    
}
