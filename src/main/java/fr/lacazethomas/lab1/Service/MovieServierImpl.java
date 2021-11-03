package fr.lacazethomas.lab1.Service;

import fr.lacazethomas.lab1.Exception.MovieNotFoundException;
import fr.lacazethomas.lab1.Model.Movie;
import fr.lacazethomas.lab1.Repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServierImpl implements MovieService{

    private MovieRepository movieRepository;

    @Autowired
    public MovieServierImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    @Override
    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public Movie findById(Long movieId) throws MovieNotFoundException{
        return movieRepository.findById(movieId).orElseThrow(MovieNotFoundException::new);
    }

    @Override
    public Movie updateMovie(Long movieId, Movie movieDetails) throws MovieNotFoundException {
        Movie movie = movieRepository.findById(movieId).orElseThrow(MovieNotFoundException::new);
        movie.setDate(movieDetails.getDate());
        movie.setTitle(movieDetails.getTitle());
        return movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(MovieNotFoundException::new);
        movieRepository.delete(movie);
    }
}
