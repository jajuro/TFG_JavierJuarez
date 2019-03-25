/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spanish.parser.util;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.extractors.CommonExtractors;
import de.l3s.boilerpipe.sax.BoilerpipeSAXInput;
import de.l3s.boilerpipe.sax.HTMLDocument;
import de.l3s.boilerpipe.sax.HTMLFetcher;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xml.sax.SAXException;

public class URLTools {

    private String url;

    public URLTools(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isURL() {
        String regex = "^(https?://)?(([\\w!~*'().&=+$%-]+: )?[\\w!~*'().&=+$%-]+@)?(([0-9]"
                + "{1,3}\\.){3}[0-9]{1,3}|([\\w!~*'()-]+\\.)*([\\w^-][\\w-]{0,61})?[\\w]\\."
                + "[a-z]{2,6})(:[0-9]{1,4})?((/*)|(/+[\\w!~*'().;?:@&=+$,%#-]+)+/*)$";

        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(this.getUrl());
            return matcher.matches();
        } catch (RuntimeException e) {
            return false;
        }
    }
    
    public String extractFromURL(){
        HTMLDocument htmldoc = null;
        TextDocument doc = null;
        String res = null;
        try {
            htmldoc = HTMLFetcher.fetch(new URL(this.getUrl()));
            doc = new BoilerpipeSAXInput(htmldoc.toInputSource()).getTextDocument();
            res = CommonExtractors.ARTICLE_EXTRACTOR.getText(doc);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException | SAXException | BoilerpipeProcessingException ex) {
            ex.printStackTrace();
        }
        
        return res;
    }
}
