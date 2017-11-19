package org.web.google.search.crawler;

import org.junit.Before;
import org.junit.Test;
import org.web.search.api.SearchQuery;
import org.web.search.google.GoogleQueryParameter;
import org.web.search.google.GoogleSearchEngine;
import org.web.search.google.GoogleSearchQueryPreparer;
import org.web.search.google.GoogleSearchQueryRunner;

import java.util.List;


/**
 * Created by Behrouz-ZD on 11/18/2017.
 */

public class SearchUnitTest {


    GoogleSearchQueryPreparer searchQueryPreparer;

    GoogleSearchQueryRunner googleSearchQueryRunner;

    GoogleSearchEngine googleSearchEngine;


    @Before
    public void setUp() {

        googleSearchEngine = new GoogleSearchEngine();

        searchQueryPreparer = new GoogleSearchQueryPreparer();
        googleSearchQueryRunner = new GoogleSearchQueryRunner();

        googleSearchEngine.setSearchQueryPreparer(searchQueryPreparer);
        googleSearchEngine.setSearchQueryRunner(googleSearchQueryRunner);

    }


    @Test
    public void testSearchResultCountWithUrl() {

        List<String> results = googleSearchQueryRunner.runSearch("https://www.google.com/search?q=Concurrency&num=12&lr=lang_en");
        assert results.size() == 12;

    }

    @Test
    public void testSearchResultCount() {

        SearchQuery searchQuery = new SearchQuery.Builder()
                .putParameter(GoogleQueryParameter.QUERY, "Concurrency")
                .putParameter(GoogleQueryParameter.RESULTS_NUM, "12")
                .build();

        String url = searchQueryPreparer.prepareSearchUri(searchQuery);

        List<String> results = googleSearchQueryRunner.runSearch(url);
        assert results.size() == 12;

    }

    @Test
    public void testSearchResultContent() {

        SearchQuery searchQuery = new SearchQuery.Builder()
                .putParameter(GoogleQueryParameter.QUERY, "Concurrency")
                .putParameter(GoogleQueryParameter.RESULTS_NUM, "12")
                .build();

        String url = searchQueryPreparer.prepareSearchUri(searchQuery);

        List<String> results = googleSearchQueryRunner.runSearch(url);

        assert results.size() == 12;

        for (int i = 0; i < 12; i++) {
            assert results.get(i).contains("http://") || results.get(i).contains("https://");
        }

    }

}
