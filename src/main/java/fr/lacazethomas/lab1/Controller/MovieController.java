package fr.lacazethomas.lab1.controller;

import fr.lacazethomas.lab1.controller.dto.MovieDTO;
import fr.lacazethomas.lab1.service.MovieService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/movies")
public class MovieController extends CrudController<MovieDTO> {

    public MovieController(MovieService movieService) {
        super(movieService);
    }
}