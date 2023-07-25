package com.furkan.youtube;

public class YouTubeSearchResult {
    private String kind;
    private String etag;
    private YouTubeVideoId id;
    private YouTubeSnippet snippet;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public YouTubeVideoId getId() {
        return id;
    }

    public void setId(YouTubeVideoId id) {
        this.id = id;
    }

    public YouTubeSnippet getSnippet() {
        return snippet;
    }

    public void setSnippet(YouTubeSnippet snippet) {
        this.snippet = snippet;
    }
}
