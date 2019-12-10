package io.javabrains.movieinfoservice.model;

import lombok.Data;

@Data
public class Movie {

    private String id;
    private String title;
    private String overview;
    private String release_date;

    private Movie() {}

    public Movie(String id, String title, String overview, String release_date) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.release_date = release_date;
    }
}
