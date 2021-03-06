package fr.lacazethomas.lab1.controller;

import fr.lacazethomas.lab1.controller.dto.MovieDTO;
import fr.lacazethomas.lab1.service.MovieService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.swagger.annotations.ApiOperation;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/movies")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MovieController {

    private final MovieService movieService;

    @Value("${backendA:http://localhost:8081}")
    private String backendA;

    private final RestTemplate restTemplate;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
        this.restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
        factory.setHttpClient(httpClient);
        restTemplate.setRequestFactory(factory);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @GetMapping("")
    @ApiOperation(value = "List all")
    @CircuitBreaker(name = "MovieService", fallbackMethod = "getAllFallback")
    @Retry(name = "MovieService", fallbackMethod = "getAllFallback")
    @TimeLimiter(name = "MovieService", fallbackMethod = "getAllFallback")
    public CompletableFuture<ResponseEntity<List<MovieDTO>>> getAll() {
        ResponseEntity<List<MovieDTO>> resp = restTemplate.exchange(backendA + "/movies", HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
        return CompletableFuture.completedFuture(new ResponseEntity<>(resp.getBody(), resp.getStatusCode()));
    }

    private CompletableFuture<ResponseEntity<List<MovieDTO>>> getAllFallback(Exception e) {
        return CompletableFuture.completedFuture(new ResponseEntity<>(movieService.findAll(), HttpStatus.OK));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Get by Id")
    @CircuitBreaker(name = "MovieService", fallbackMethod = "getByIdFallback")
    @Retry(name = "MovieService", fallbackMethod = "getByIdFallback")
    @TimeLimiter(name = "MovieService", fallbackMethod = "getByIdFallback")
    public CompletableFuture<ResponseEntity<MovieDTO>> getById(@PathVariable Long id) {
        ResponseEntity<MovieDTO> resp = restTemplate.exchange(backendA + "/movies/" + id, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
        return CompletableFuture.completedFuture(new ResponseEntity<>(resp.getBody(), resp.getStatusCode()));
    }

    public CompletableFuture<ResponseEntity<MovieDTO>> getByIdFallback(@PathVariable Long id, Exception e) {
        Optional<MovieDTO> optionalT = movieService.findById(id);

        return CompletableFuture.completedFuture(optionalT.map(MovieDTO ->
                        new ResponseEntity<>(MovieDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND)));
    }

    @PostMapping("")
    @ApiOperation(value = "Create a new one")
    @CircuitBreaker(name = "MovieService", fallbackMethod = "saveFallback")
    @Retry(name = "MovieService", fallbackMethod = "saveFallback")
    public ResponseEntity<MovieDTO> save(@RequestBody MovieDTO body) {
        ResponseEntity<MovieDTO> resp = restTemplate.postForEntity(backendA + "/movies/", body, null, new ParameterizedTypeReference<>() {
        });
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }

    public ResponseEntity<MovieDTO> saveFallback(@RequestBody MovieDTO body, Exception e) {
        return new ResponseEntity<>(movieService.save(body), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Delete by Id")
    @CircuitBreaker(name = "MovieService", fallbackMethod = "deleteFallback")
    @Retry(name = "MovieService", fallbackMethod = "deleteFallback")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        ResponseEntity<String> resp = restTemplate.exchange(backendA + "/movies/" + id, HttpMethod.DELETE, null, String.class);

        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }

    public ResponseEntity<?> deleteFallback(@PathVariable Long id, Exception e) {
        Optional<MovieDTO> optional = movieService.findById(id);
        optional.ifPresent(n -> movieService.delete(id));

        return optional.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    @ApiOperation(value = "Update by Id")
    @CircuitBreaker(name = "MovieService", fallbackMethod = "updateFallback")
    @Retry(name = "MovieService", fallbackMethod = "updateFallback")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody MovieDTO body) {

        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, null);

        ResponseEntity<String> resp = restTemplate.exchange(backendA + "/movies/" + id, HttpMethod.PUT, httpEntity, String.class);
        return new ResponseEntity<>(resp.getBody(), resp.getStatusCode());
    }

    public ResponseEntity<?> updateFallback(@PathVariable Long id, @RequestBody MovieDTO body, Exception e) {
        Optional<MovieDTO> optional = movieService.findById(id);
        optional.ifPresent(n -> movieService.update(id, body));

        return optional.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}