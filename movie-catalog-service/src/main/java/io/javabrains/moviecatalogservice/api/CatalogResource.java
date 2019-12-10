package io.javabrains.moviecatalogservice.api;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.javabrains.moviecatalogservice.model.CatalogItem;
import io.javabrains.moviecatalogservice.model.Movie;
import io.javabrains.moviecatalogservice.model.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class CatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment env;

    @GetMapping("/{userId}")
    @HystrixCommand(fallbackMethod = "getFallbackCatalog")
    public  List<CatalogItem> getCatalog(@PathVariable String userId) {

        String ratingsDataServicePath = env.getProperty("ratings-data-service.url");
        String movieInfoServicePath = env.getProperty("movie-info-service.url");
        UserRating ratings = restTemplate.getForObject(ratingsDataServicePath + userId, UserRating.class);

        return ratings.getUserRating().stream().map(rating -> {
            Movie movie = restTemplate.getForObject(movieInfoServicePath + rating.getMovieId(), Movie.class);
            return new CatalogItem(movie.getTitle(), "Sample Description", rating.getRating());
        }).collect(Collectors.toList());

    }

    public List<CatalogItem> getFallbackCatalog(@PathVariable String userId) {
        return Arrays.asList(new CatalogItem("No movie", "", 0));
    }
}
