package fr.lacazethomas.lab1.Controller;

import fr.lacazethomas.lab1.Model.Movie;
import fr.lacazethomas.lab1.Repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieRepository movieRepository;

    public ResponseEntity<?> getAllMovie() {
        var movies = movieRepository.findAll();

        if(movies.isEmpty()){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok(movies);
        }

    }

    public ResponseEntity<?> addMovie(@RequestBody Movie movie) {
        movieRepository.save(movie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable(value = "id") Long movieId) {
        var movie =  movieRepository.findById(movieId);

        return movie.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable(value = "id") Long movieId, @RequestBody Movie movieDetails) {
        var movie = movieRepository.findById(movieId);

        if(movie.isPresent()){
            var _movie = movie.get();
            _movie.setReleaseDate(movieDetails.getReleaseDate());
            _movie.setTitle(movieDetails.getTitle());
            movieRepository.save(_movie);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable(value = "id") Long movieId) {
        movieRepository.delete(movieRepository.getById(movieId));

        return ResponseEntity.ok().build();
    }
}
