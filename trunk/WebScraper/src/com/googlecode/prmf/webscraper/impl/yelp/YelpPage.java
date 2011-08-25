package com.googlecode.prmf.webscraper.impl.yelp;

import com.googlecode.prmf.webscraper.core.Page;
import com.googlecode.prmf.webscraper.core.ScrapablePage;
import com.googlecode.prmf.webscraper.core.Search;


public class YelpPage extends ScrapablePage{

    public YelpPage(String url, Search search) {
        super(url, search);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected ScrapablePage buildScrapablePageObject(String pageURL) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String getNextPageURL() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean isEmptyResultPage() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void scanThisPage() {
        // TODO Auto-generated method stub
        
    }

}
