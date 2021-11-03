package fr.lacazethomas.lab1.Service;

import fr.lacazethomas.lab1.Exception.MovieNotFoundException;
import fr.lacazethomas.lab1.Model.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> findAll();

    Movie save(Movie movie);

    Movie findById(Long movieId) throws MovieNotFoundException;

    Movie updateMovie(Long movieId, Movie movieDetails) throws MovieNotFoundException;

    void deleteMovie(Long movieId);
}
