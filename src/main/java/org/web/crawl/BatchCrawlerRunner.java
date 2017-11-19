package org.web.crawl;

import org.web.utils.ApplicationProperties;
import org.web.utils.ObjectUtils;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Behrouz-ZD on 11/18/2017.
 */
/*
This class is used for running crawler with multiple Thread
 */
public class BatchCrawlerRunner {

    private ExecutorService executor = Executors.newFixedThreadPool(ApplicationProperties.threadPoolSize);

    /**
     * @param urls : Extract javascript library from urls
     * @return Map<JS name,JS frequency>
     */
    public Map<String, Integer> extractJSLibraryFrequency(List<String> urls) {

        try {

            Collection<Callable<List<String>>> tasks = new ArrayList<Callable<List<String>>>();

            for (final String url : urls) {

                tasks.add(new Callable<List<String>>() {
                    @Override
                    public List<String> call() throws Exception {
                        return PageCrawler.extractJSLibrary(url);
                    }
                });
            }

            List<Future<List<String>>> futures = executor.invokeAll(tasks);

            Map<String, Integer> jsCount = new HashMap<String, Integer>();
            for (Future<List<String>> future : futures) {
                try {
                    List<String> result = future.get();
                    for (String js : result) {
                        Integer count = jsCount.get(js);
                        jsCount.put(js, (count == null) ? 1 : count + 1);
                    }
                } catch (Exception e) {

                }
            }


            return ObjectUtils.sortByValue(jsCount);

        } catch (Exception e) {
            throw new RuntimeException("Error running crawlers!", e);
        }
    }


    /**
     * Extract javascript content
     *
     * @param urls
     * @return List<Javascript file content>
     */
    public List<String> extractJSContent(List<String> urls) {

        try {

            Collection<Callable<String>> tasks = new ArrayList<Callable<String>>();

            for (final String url : urls) {

                tasks.add(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return PageCrawler.getPageContent(url);
                    }
                });
            }

            List<Future<String>> futures = executor.invokeAll(tasks);

            Set<String> jsContent = new HashSet<String>();
            for (Future<String> future : futures) {
                try {
                    jsContent.add(future.get());
                } catch (Exception e) {

                }
            }


            return new ArrayList<String>(jsContent);

        } catch (Exception e) {
            throw new RuntimeException("Error running crawlers!", e);
        }
    }

    /**
     * Deduplicate javascript libraries by comparing their content  (Not completed..!)
     *
     * @param urls
     * @return
     */
    public List<String> getDeduplicateResult(List<String> urls) {

        List<String> deduplicateJs = new ArrayList<>();

        List<String> contents = extractJSContent(urls);

        // copy javascript content and compute hashcode
        List<JS> copyContents;
        List<JS> tmp = copyJsContent(contents);

        // remove duplicate libraries by comparing their content
        for (int i = 0; i < contents.size(); i++) {
            int removed = 0;
            copyContents = copyJsContents(tmp);
            for (JS ct : copyContents) {
                if (contents.get(i).hashCode() == ct.getHashCode() && contents.get(i).equals(ct.getContent())) {
                    removed++;
                    tmp.remove(ct);
                }
            }
            if (removed > 0)
                deduplicateJs.add(contents.get(i));
        }

        return deduplicateJs;

    }

    private List<JS> copyJsContent(List<String> contents) {
        List<JS> jses = new ArrayList<>();
        for (String ct : contents)
            jses.add(new JS(ct, ct.hashCode()));
        return jses;
    }

    private List<JS> copyJsContents(List<JS> contents) {
        List<JS> jses = new ArrayList<>();
        for (JS ct : contents)
            jses.add(new JS(ct.getContent(), ct.getHashCode()));
        return jses;
    }

    public void shutdown() {
        executor.shutdown();
    }

}
