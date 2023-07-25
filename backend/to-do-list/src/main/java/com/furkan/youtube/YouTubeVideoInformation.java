package com.furkan.youtube;


public class YouTubeVideoInformation {
    private String videoid;

    private String title;

    private String thubnailurl;

    public YouTubeVideoInformation(String videoid, String title, String thubnailurl) {
        this.videoid = videoid;
        this.title = title;
        this.thubnailurl = thubnailurl;
    }

    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThubnailurl() {
        return thubnailurl;
    }

    public void setThubnailurl(String thubnailurl) {
        this.thubnailurl = thubnailurl;
    }

    @Override
    public String toString() {
        return "YouTubeVideoInformation{" +
                "videoid='" + videoid + '\'' +
                ", title='" + title + '\'' +
                ", thubnailurl='" + thubnailurl + '\'' +
                '}';
    }
}
