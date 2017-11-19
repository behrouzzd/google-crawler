package org.web.google.search.crawler;

import org.junit.Before;
import org.junit.Test;
import org.web.utils.ApplicationProperties;
import org.web.crawl.BatchCrawlerRunner;
import org.web.crawl.PageCrawler;
import org.web.search.api.SearchQuery;
import org.web.search.google.GoogleQueryParameter;
import org.web.search.google.GoogleSearchEngine;
import org.web.search.google.GoogleSearchQueryPreparer;
import org.web.search.google.GoogleSearchQueryRunner;

import java.util.List;
import java.util.Map;

/**
 * Created by Behrouz-ZD on 11/18/2017.
 */
public class CrawlerUnitTest {


    GoogleSearchQueryPreparer searchQueryPreparer;
    GoogleSearchQueryRunner googleSearchQueryRunner;
    GoogleSearchEngine googleSearchEngine;
    BatchCrawlerRunner batchCrawlerRunner;

    @Before
    public void setUp() {

        googleSearchEngine = new GoogleSearchEngine();
        searchQueryPreparer = new GoogleSearchQueryPreparer();
        googleSearchQueryRunner = new GoogleSearchQueryRunner();
        googleSearchEngine.setSearchQueryPreparer(searchQueryPreparer);
        googleSearchEngine.setSearchQueryRunner(googleSearchQueryRunner);
        ApplicationProperties.threadPoolSize=12;
        batchCrawlerRunner = new BatchCrawlerRunner();
    }

    @Test
    public void testExtractJSLibrary() {

        List<String> list = PageCrawler.extractJSLibrary("http://www.creativebloq.com/web-design/examples-of-javascript-1233964");

        assert list.size() == 3;

    }

    @Test
    public void testGetPageContent() {

        String ct = PageCrawler.getPageContent("http://www.creativebloq.com/web-design/examples-of-javascript-1233964");

        assert !ct.isEmpty();
    }


    @Test
    public void testCrawl() {

        SearchQuery searchQuery = new SearchQuery.Builder()
                .putParameter(GoogleQueryParameter.QUERY, "Concurrency")
                .putParameter(GoogleQueryParameter.RESULTS_NUM, "12")
                .build();

        String url = searchQueryPreparer.prepareSearchUri(searchQuery);

        List<String> results = googleSearchQueryRunner.runSearch(url);

        Map<String, Integer> jsResult = batchCrawlerRunner.extractJSLibraryFrequency(results);


        assert !jsResult.isEmpty();

        System.out.println("********************************************************");
        System.out.println("********************************************************");
        System.out.println("***************Javascript library used*****************");

        for (Map.Entry<String, Integer> entry : jsResult.entrySet()) {
            System.out.println(entry.getKey());
        }

        System.out.println("********************************************************");
        System.out.println("********************************************************");
        System.out.println("********************************************************");

    }

}
