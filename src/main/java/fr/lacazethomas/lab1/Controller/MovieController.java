package fr.lacazethomas.lab1.Controller;

import fr.lacazethomas.lab1.Model.Movie;
import fr.lacazethomas.lab1.Service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/")
public class MovieController {
    MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getAllMovie() {
        return new ResponseEntity<>(movieService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/movies")
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        return new ResponseEntity<>(movieService.save(movie), HttpStatus.CREATED);
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable(value = "id") Long movieId) {
        return new ResponseEntity<>(movieService.findById(movieId), HttpStatus.OK);
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable(value = "id") Long movieId, @RequestBody Movie movieDetails) {
        return new ResponseEntity<>(movieService.updateMovie(movieId, movieDetails), HttpStatus.OK);
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable(value = "id") Long movieId) {
        movieService.deleteMovie(movieId);
        return ResponseEntity.ok().build();
    }
}
