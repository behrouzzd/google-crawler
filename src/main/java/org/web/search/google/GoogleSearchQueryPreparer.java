package org.web.search.google;

import org.web.search.api.QueryParameter;
import org.web.search.api.SearchQuery;
import org.web.search.api.SearchQueryPreparer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Behrouz-ZD on 11/18/2017.
 */

/**
 * Preparing google url including query parameters
 */
public class GoogleSearchQueryPreparer implements SearchQueryPreparer {

    private String googleSearchUri = "https://www.google.com/search?";
    private String googleQueryParameter = "q";
    private String parameterJoiner = "&";


    public String getGoogleSearchUri() {
        return googleSearchUri;
    }

    public String getGoogleQueryParameter() {
        return googleQueryParameter;
    }

    public String getParameterJoiner() {
        return parameterJoiner;
    }

    /**
     * return google search url with parameters
     * @param searchQuery
     * @return
     */
    @Override
    public String prepareSearchUri(SearchQuery searchQuery) {

        Map<QueryParameter, String> parameterValues = searchQuery.getParameterValues();

        String uri = getGoogleSearchUri();

        for (Map.Entry<QueryParameter, String> entry : parameterValues.entrySet()) {
            uri += formatAndJoinParameterValue(entry.getKey(), entry.getValue()) + getParameterJoiner();
        }

        uri+="lr=lang_en";

        return uri;
    }

    private String formatAndJoinParameterValue(QueryParameter parameter, String value) {
        if (parameter.getName().equals(getGoogleQueryParameter())) {
            try {
                value = URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Error encoding query value!", e);
            }
        }
        return parameter.getName() + "=" + value;
    }


}
