package org.web.search.api;

import java.util.List;

/**
 * Created by Behrouz-ZD on 11/18/2017.
 */
public interface SearchQueryRunner {

    List<String> runSearch(String uri);
}
