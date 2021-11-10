package fr.lacazethomas.lab1.mapper;

import fr.lacazethomas.lab1.controller.dto.MovieDTO;
import fr.lacazethomas.lab1.model.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MovieMapper {

    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    MovieDTO movieToDto(Movie movie);

    Movie dtoToMovie(MovieDTO movieDTO);
}
