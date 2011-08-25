package com.googlecode.prmf.webscraper.core;
import java.util.List;



public abstract class Search 
{
    protected static int resultsPerPull;
    protected static String searchForTerm;
    protected String searchNearTerm;
    protected String searchURL;
    
    List<Page> pages;
    
    public String getSearchNearTerm() {
        return searchNearTerm;
    }
    
    public void setSearchNearTerm(String searchNearTerm) {
        this.searchNearTerm = searchNearTerm;
        buildSearchURL();
    }
    
    abstract public void startScan();
    
    abstract protected void buildSearchURL();
}
