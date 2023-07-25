package com.furkan.youtube;

import com.fasterxml.jackson.annotation.JsonProperty;

public class YouTubeThumbnails {
    @JsonProperty("default")
    private YouTubeThumbnail defaultThumbnail;
    private YouTubeThumbnail medium;
    private YouTubeThumbnail high;

    public YouTubeThumbnail getDefaultThumbnail() {
        return defaultThumbnail;
    }

    public void setDefaultThumbnail(YouTubeThumbnail defaultThumbnail) {
        this.defaultThumbnail = defaultThumbnail;
    }

    public YouTubeThumbnail getMedium() {
        return medium;
    }

    public void setMedium(YouTubeThumbnail medium) {
        this.medium = medium;
    }

    public YouTubeThumbnail getHigh() {
        return high;
    }

    public void setHigh(YouTubeThumbnail high) {
        this.high = high;
    }
}
