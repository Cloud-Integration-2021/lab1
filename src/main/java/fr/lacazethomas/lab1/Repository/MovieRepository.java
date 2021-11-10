package fr.lacazethomas.lab1.repository;

import fr.lacazethomas.lab1.model.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {

}
