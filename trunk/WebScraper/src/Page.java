import java.io.IOException;

import org.apache.log4j.Logger;
import org.jsoup.*;
import org.jsoup.nodes.Document;

public class Page 
{
    public static final Logger logger = Logger.getLogger(Page.class);
    Document doc;
    String url;
    
    public Page(String url)
    {
        this.url = url;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            logger.warn("Error connecting to url: " + url);
        }
    }
}
