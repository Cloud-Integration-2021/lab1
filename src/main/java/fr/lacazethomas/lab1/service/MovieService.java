package fr.lacazethomas.lab1.service;

import fr.lacazethomas.lab1.controller.dto.MovieDTO;
import fr.lacazethomas.lab1.mapper.MovieMapper;
import fr.lacazethomas.lab1.model.Movie;
import fr.lacazethomas.lab1.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fr.lacazethomas.lab1.mapper.MovieMapper.INSTANCE;

@Service
@RequiredArgsConstructor
public class MovieService implements CrudService<MovieDTO> {

    private final MovieRepository movieRepository;

    @Override
    public List<MovieDTO> findAll() {
        List<MovieDTO> movieDTOList = new ArrayList<>();
        movieRepository.findAll().forEach(movie -> movieDTOList.add(INSTANCE.movieToDto(movie)));
        return movieDTOList;
    }

    @Override
    public Optional<MovieDTO> findById(Long id) {
        Optional<Movie> movieOptional = movieRepository.findById(id);
        return movieOptional.map(MovieMapper.INSTANCE::movieToDto);
    }

    @Override
    public MovieDTO save(MovieDTO movieDTO) {
        Movie movie = INSTANCE.dtoToMovie(movieDTO);
        return INSTANCE.movieToDto(movieRepository.save(movie));
    }

    @Override
    public void delete(Long id) {
        movieRepository.deleteById(id);
    }

    @Override
    public MovieDTO update(Long id, MovieDTO movieDTO) {
        Movie savedMovie = movieRepository.findById(id).orElseThrow();
        Movie movieToUpdate = INSTANCE.dtoToMovie(movieDTO);


        savedMovie.setTitle(movieToUpdate.getTitle());
        savedMovie.setReleaseDate(movieToUpdate.getReleaseDate());
        savedMovie.setPlot(movieToUpdate.getPlot());

        return INSTANCE.movieToDto(movieRepository.save(savedMovie));
    }
}
