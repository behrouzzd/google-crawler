package org.web.search.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Behrouz-ZD on 11/18/2017.
 */

/**
 * Search query object for doing search in search engine
 */
public class SearchQuery {
    /*
    list of parameter for search query
     */
    private Map<QueryParameter, String> parameterValues;

    private SearchQuery(Builder builder) {
        this.parameterValues = builder.parameterValues;
    }

    public Map<QueryParameter, String> getParameterValues() {
        return parameterValues;
    }

    public static class Builder {

        private Map<QueryParameter, String> parameterValues = new HashMap<QueryParameter, String>();

        public Builder putParameter(QueryParameter queryParameter, String queryValue) {
            this.parameterValues.put(queryParameter, queryValue);
            return this;
        }

        public SearchQuery build() {
            return new SearchQuery(this);
        }

    }

}
