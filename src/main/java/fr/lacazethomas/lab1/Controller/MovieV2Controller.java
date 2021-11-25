package fr.lacazethomas.lab1.controller;

import fr.lacazethomas.lab1.controller.dto.MovieDTO;
import fr.lacazethomas.lab1.service.ActorService;
import fr.lacazethomas.lab1.service.MovieService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v2/movies")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MovieV2Controller {

    private final MovieService movieService;
    private final ActorService actorService;

    public MovieV2Controller(MovieService movieService, ActorService actorService) {
        this.movieService = movieService;
        this.actorService = actorService;
    }

    @GetMapping("")
    @ApiOperation(value = "List all")
    public ResponseEntity<?> getAll() {
        var movieDTOS = movieService.findAll();

        if (!movieDTOS.isEmpty()) {
            movieDTOS.forEach(actorService::populateMovieWithActors);
        }

        return ResponseEntity.ok(movieDTOS);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Get by Id")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<MovieDTO> optionalT = movieService.findById(id);

        optionalT.ifPresent(actorService::populateMovieWithActors);

        return optionalT.map(T ->
                        new ResponseEntity<>(T, HttpStatus.OK))
                .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PostMapping("")
    @ApiOperation(value = "Create a new one")
    public ResponseEntity<?> save(@RequestBody MovieDTO body) {
        return new ResponseEntity<>(movieService.save(body), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Delete by Id")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<MovieDTO> optional = movieService.findById(id);
        optional.ifPresent(n -> movieService.delete(id));

        return optional.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    @ApiOperation(value = "Update by Id")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody MovieDTO body) {

        Optional<MovieDTO> optional = movieService.findById(id);
        optional.ifPresent(n -> movieService.update(id, body));

        return optional.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
