package org.web;

import org.web.utils.ApplicationProperties;
import org.web.crawl.BatchCrawlerRunner;
import org.web.search.api.SearchQuery;
import org.web.search.api.SearchResult;
import org.web.search.google.GoogleQueryParameter;
import org.web.search.google.GoogleSearchEngine;
import org.web.search.google.GoogleSearchQueryPreparer;
import org.web.search.google.GoogleSearchQueryRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

/**
 * Created by Behrouz-ZD on 11/18/2017.
 */
public class ApplicationRunner {


    /**
     * It enables program to be configured through config file
     */
    public static void loadConfig() {

        Properties prop = new Properties();
        InputStream input = null;

        try {

            String filename = "application.properties";
            input = ApplicationProperties.class.getClassLoader().getResourceAsStream(filename);
            if (input == null)
                throw new RuntimeException("Could not found application.properties");
            prop.load(input);

            ApplicationProperties.threadPoolSize = Integer.parseInt(prop.getProperty("threadPoolSize"));

        } catch (IOException ex) {
            throw new RuntimeException("Could loading application.properties");
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    throw new RuntimeException("Could loading application.properties");
                }
            }
        }

    }

    public static void main(String[] args) throws IOException {

        ApplicationRunner.loadConfig();
        GoogleSearchEngine googleSearchEngine = new GoogleSearchEngine();
        GoogleSearchQueryPreparer searchQueryPreparer = new GoogleSearchQueryPreparer();
        GoogleSearchQueryRunner googleSearchQueryRunner = new GoogleSearchQueryRunner();
        googleSearchEngine.setSearchQueryPreparer(searchQueryPreparer);
        googleSearchEngine.setSearchQueryRunner(googleSearchQueryRunner);
        BatchCrawlerRunner crawlerRunner = new BatchCrawlerRunner();

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.print("Enter your search term or exit: ");
            String searchTerm = scanner.next();

            if ("exit".equals(searchTerm))
                break;

            SearchQuery searchQuery = new SearchQuery.Builder()
                    .putParameter(GoogleQueryParameter.QUERY, searchTerm)
                    .putParameter(GoogleQueryParameter.RESULTS_NUM, "12")
                    .build();

            SearchResult searchResult = googleSearchEngine.search(searchQuery);

            Map<String, Integer> jsResult = crawlerRunner.extractJSLibraryFrequency(searchResult.getHits());

            System.out.println("********************************************************");
            System.out.println("********************************************************");
            System.out.println("***************Javascript library used*****************");

            int index=0;
            for (Map.Entry<String, Integer> entry : jsResult.entrySet()) {
                System.out.println(entry.getKey());
                index++;
                if (index>=5)
                    break;
            }

            System.out.println("********************************************************");
            System.out.println("********************************************************");
            System.out.println("********************************************************");

        }

        crawlerRunner.shutdown();
    }

}
