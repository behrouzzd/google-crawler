package org.web.search.google;

import org.web.search.api.QueryParameter;

/**
 * Created by Behrouz-ZD on 11/18/2017.
 */

/**
 * Query parameter for Google api which holds parameter name
 */
public enum GoogleQueryParameter implements QueryParameter {

    QUERY("q"),
    RESULTS_NUM("num"),
    SITE_OR_DOMAIN("as_sitesearch"),
    FILE_TYPE("fileType"),
    COUNTRY("cr");

    private String value;

    GoogleQueryParameter(String value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return this.value;
    }
}
