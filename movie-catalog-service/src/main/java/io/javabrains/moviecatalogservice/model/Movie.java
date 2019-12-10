package io.javabrains.moviecatalogservice.model;

public class Movie {

    private String id;
    private String title;
    private String overview;
    private String release_date;

    public Movie() {}

    public Movie(String id, String title, String overview, String release_date) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.release_date = release_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}
