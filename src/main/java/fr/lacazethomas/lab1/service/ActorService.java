package fr.lacazethomas.lab1.service;

import fr.lacazethomas.lab1.controller.dto.ActorDTO;
import fr.lacazethomas.lab1.controller.dto.MovieDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ActorService {
    private CircuitBreakerFactory circuitBreakerFactory;

    @Value("${backendA:http://localhost:8081}")
    private String backendA;

    public ActorService(CircuitBreakerFactory circuitBreakerFactory) {
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    private static List<ActorDTO> defaultActors() {
        return List.of(new ActorDTO("Doe", "John", "2020-12-22"));
    }

    public List<ActorDTO> getMovieActors(Long movieId) {
        var restTemplate = new RestTemplate();

        return circuitBreakerFactory.create("circuitbreaker").run(() -> restTemplate.exchange(
                        backendA + "/movies/" +movieId+ "/actors",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<ActorDTO>>() {
                        }).getBody()
                , throwable -> defaultActors());
    }

    public void populateMovieWithActors(MovieDTO movieDTO) {
        List<ActorDTO> movieActors = getMovieActors(movieDTO.getId());

        movieActors.forEach(movieDTO::addActorDTO);
    }
}
