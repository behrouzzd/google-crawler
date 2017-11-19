package org.web.search.google;

import org.web.search.api.*;

import java.util.List;

/**
 * Created by Behrouz-ZD on 11/18/2017.
 */

/**
 * Google search engine including query maker and query runner
 */
public class GoogleSearchEngine implements WebSearchEngine {

    private SearchQueryPreparer searchQueryPreparer;
    private SearchQueryRunner searchQueryRunner;

    public void setSearchQueryPreparer(SearchQueryPreparer searchQueryPreparer) {
        this.searchQueryPreparer = searchQueryPreparer;
    }

    public void setSearchQueryRunner(SearchQueryRunner searchQueryRunner) {
        this.searchQueryRunner = searchQueryRunner;
    }

    @Override
    public SearchResult search(SearchQuery searchQuery) {

        String searchUri = searchQueryPreparer.prepareSearchUri(searchQuery);
        List<String> results = searchQueryRunner.runSearch(searchUri);
        return new SearchResult(results);

    }

}
