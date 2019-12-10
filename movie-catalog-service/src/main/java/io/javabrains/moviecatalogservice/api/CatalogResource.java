package io.javabrains.moviecatalogservice.api;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.javabrains.moviecatalogservice.model.CatalogItem;
import io.javabrains.moviecatalogservice.model.Movie;
import io.javabrains.moviecatalogservice.model.Rating;
import io.javabrains.moviecatalogservice.model.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${ratings-data-service.url}")
    private String ratingsDataServicePath;

    @Value("${movie-info-service.url}")
    private String movieInfoServicePath;

    @GetMapping("/{userId}")
    @HystrixCommand(fallbackMethod = "getFallbackCatalog")
    public  List<CatalogItem> getCatalog(@PathVariable String userId) {
        UserRating ratings = getUserRating(userId);
        return ratings.getUserRating().stream().map(rating -> getCatalogItem(movieInfoServicePath, rating)).collect(Collectors.toList());
    }

    private UserRating getUserRating(@PathVariable String userId) {
        return restTemplate.getForObject(ratingsDataServicePath + userId, UserRating.class);
    }

    private CatalogItem getCatalogItem(String movieInfoServicePath, Rating rating) {
        Movie movie = restTemplate.getForObject(movieInfoServicePath + rating.getMovieId(), Movie.class);
        return new CatalogItem(movie.getTitle(), movie.getOverview(), rating.getRating());
    }

    public List<CatalogItem> getFallbackCatalog(@PathVariable String userId) {
        return Arrays.asList(new CatalogItem("No movie", "", 0));
    }
}
