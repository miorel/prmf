package com.googlecode.prmf.webscraper.impl.yelp;

import com.googlecode.prmf.webscraper.core.Search;


public class YelpSearch extends Search{

    @Override
    protected void buildSearchURL()
    {
        this.searchURL = "http://www.yelp.com/search?find_desc="+cleanSearchForTerm(searchForTerm);
        this.searchURL += "&ns=1";
        this.searchURL += "&rpp="+this.resultsPerPull;
        this.searchURL += "&find_loc=" + this.searchNearTerm;
        this.searchURL += "#start=0";
    }

    private String cleanSearchForTerm(String searchForTerm) {
        return searchForTerm.replaceAll("\\s+", "+");
    }

    @Override
    public void startScan() {
        // TODO Auto-generated method stub
        
    }

}
