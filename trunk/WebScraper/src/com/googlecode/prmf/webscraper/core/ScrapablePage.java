package com.googlecode.prmf.webscraper.core;
import java.util.ArrayList;
import java.util.List;

public abstract class ScrapablePage extends Page
{
    private Search search;
    
    public ScrapablePage(String url, Search search)
    {
        super(url);
        this.search = search;
    }

    public List<ScrapablePage> getAllPagesAfter() 
    {
        List<ScrapablePage> pagesAfterThisOne = new ArrayList<ScrapablePage>();
        if(!isEmptyResultPage())
        {
            scanThisPage();
            pagesAfterThisOne.add(this);
            String nextPageURL = getNextPageURL();
            ScrapablePage nextPage = buildScrapablePageObject(nextPageURL);
            pagesAfterThisOne.addAll(nextPage.getAllPagesAfter());
        }
        return pagesAfterThisOne;
    }
    
    abstract protected ScrapablePage buildScrapablePageObject(String pageURL);
    
    abstract protected void scanThisPage();
    
    abstract protected String getNextPageURL();
    
    abstract protected boolean isEmptyResultPage();
    
}
