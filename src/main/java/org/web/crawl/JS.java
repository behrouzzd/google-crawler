package org.web.crawl;

/**
 * Created by Behrouz-ZD on 11/18/2017.
 */
public class JS {

    private String content;
    private int hashCode;

    public JS(String content, int hashCode) {
        this.content = content;
        this.hashCode = hashCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getHashCode() {
        return hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }
}
