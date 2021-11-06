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

    @GetMapping("")
    public ResponseEntity<?> getAllMovies() {
        var movies = movieRepository.findAll();

        if(movies.isEmpty()){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok(movies);
        }

    }

    @PostMapping("")
    public ResponseEntity<?> createMovie(@RequestBody Movie movie) {
        try {
            return ResponseEntity.ok(movieRepository.save(movie));
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
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
            try {
                return ResponseEntity.ok(movieRepository.save(_movie));
            }catch (Exception e){
                return ResponseEntity.internalServerError().build();
            }
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable(value = "id") Long movieId) {
        try{
            movieRepository.delete(movieRepository.getById(movieId));
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
}
