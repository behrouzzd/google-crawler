package org.web.search.google;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.web.search.api.SearchQueryRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Behrouz-ZD on 11/18/2017.
 */

/**
 * Running a google search url
 */
public class GoogleSearchQueryRunner implements SearchQueryRunner {

    private String urlRegex = "/url\\?q=(.*)&sa.*";
    private final Pattern urlPattern = Pattern.compile(urlRegex);
    private String cacheUrl = "http://webcache.googleusercontent.com";

    public String getUrlRegex() {
        return urlRegex;
    }

    public Pattern getUrlPattern() {
        return urlPattern;
    }

    public String getCacheUrl() {
        return cacheUrl;
    }

    public void setUrlRegex(String urlRegex) {
        this.urlRegex = urlRegex;
    }

    public void setCacheUrl(String cacheUrl) {
        this.cacheUrl = cacheUrl;
    }

    /**
     * Running google search
     *
     * @param searchUri
     * @return
     */
    @Override
    public List<String> runSearch(String searchUri) {

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(searchUri);
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            throw new RuntimeException("Error executing request!", e);
        }

        HttpEntity entity = response.getEntity();
        List<String> hitsUrls;
        try {
            hitsUrls = parseResponse(EntityUtils.toString(entity));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return hitsUrls;
    }

    private static List<String> parseResponse(String html) throws Exception {
        List<String> result = new ArrayList<String>();
        String pattern1 = "<h3 class=\"r\"><a href=\"/url?q=";
        String pattern2 = "\">";
        Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
        Matcher m = p.matcher(html);

        while (m.find()) {
            String domainName = m.group(0).trim();
            domainName = domainName.substring(domainName.indexOf("/url?q=") + 7);
            domainName = domainName.substring(0, domainName.indexOf("&amp;"));

            result.add(domainName);
        }
        return result;
    }

}
