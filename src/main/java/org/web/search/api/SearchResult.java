package org.web.search.api;

import java.util.List;

/**
 * Created by Behrouz-ZD on 11/18/2017.
 */

/**
 * list of search result including urls
 */
public class SearchResult {

    private List<String> hits;


    public SearchResult() {
    }

    public SearchResult(List<String> hits) {
        this.hits = hits;
    }

    public List<String> getHits() {
        return hits;
    }


    public void setHits(List<String> hits) {
        this.hits = hits;
    }
}
