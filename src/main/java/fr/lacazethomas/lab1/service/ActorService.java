package fr.lacazethomas.lab1.service;

import fr.lacazethomas.lab1.controller.dto.ActorDTO;
import fr.lacazethomas.lab1.controller.dto.MovieDTO;
import lombok.AllArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@AllArgsConstructor
public class ActorService {
    private CircuitBreakerFactory circuitBreakerFactory;

    private static List<ActorDTO> defaultActors() {
        return List.of(new ActorDTO("Doe", "John", "2020-12-22"));
    }

    public List<ActorDTO> getMovieActors(String movieTitle) {
        var restTemplate = new RestTemplate();

        //
        return circuitBreakerFactory.create("circuitbreaker").run(() -> restTemplate.exchange(
                        "http://localhost:8081/actors/" + movieTitle,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<ActorDTO>>() {
                        }).getBody()
                , throwable -> defaultActors());
    }

    public void populateMovieWithActors(MovieDTO movieDTO) {
        List<ActorDTO> movieActors = getMovieActors(movieDTO.getTitle());

        movieActors.forEach(movieDTO::addActorDTO);
    }
}
