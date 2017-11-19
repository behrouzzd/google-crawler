package org.web.crawl;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Behrouz-ZD on 11/18/2017.
 */

/**
 * Crawling web pages
 */
public class PageCrawler {

    private static String typeAttr = "type";
    private static String srcAttr = "src";
    private static String jsPostfix = ".js";
    private static String scriptAttr = "script";
    private static String typeVal = "application/javascript";

    /**
     * extract javascript lib for this url
     * @param url
     * @return
     */
    public static List<String> extractJSLibrary(String url) {

        Document doc;
        try {
            doc = Jsoup.connect(url).timeout(10000)
                    .ignoreHttpErrors(true)
                    .validateTLSCertificates(false)
                    .get();
        } catch (IOException e) {
            throw new RuntimeException("Error crawling the page!", e);
        }

        List<String> results = new ArrayList<String>();

        Elements scriptElements = doc.getElementsByTag(scriptAttr);

        for (Element element : scriptElements) {

            if (element.hasAttr(srcAttr) && element.attr(srcAttr).endsWith(jsPostfix)) {
                results.add(element.attr(srcAttr));
            }
        }

        return results;
    }

    /**
     * get page content for this url
     * @param url
     * @return
     */
    public static String getPageContent(String url) {

        try {

            if (url.startsWith("//"))
                url = url.replaceFirst("//", "http://");

            URL desUrl;

            try {
                desUrl = new URL(url);
            } catch (Exception e) {
                if (url.startsWith("http://"))
                    url = url.replaceFirst("http://", "https://");
                desUrl = new URL(url);
            }
            URLConnection con = desUrl.openConnection();
            InputStream in = con.getInputStream();
            String encoding = con.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            return IOUtils.toString(in, encoding);


        } catch (Exception e) {
            throw new RuntimeException("Error getting content!", e);
        }
    }

    /**
     * get content of list of pages
     * @param urls
     * @return
     */
    public static Map<String, String> getPageContent(List<String> urls) {

        // Map<PageUrl,PageContent>
        Map<String, String> urlContent = new HashMap<String, String>();

        for (String url : urls) {
            urlContent.put(url, getPageContent(url));
        }

        return urlContent;

    }

}
