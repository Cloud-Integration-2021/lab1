package fr.lacazethomas.lab1.controller;

import fr.lacazethomas.lab1.controller.dto.BaseDTO;
import fr.lacazethomas.lab1.service.CrudService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.swagger.annotations.ApiOperation;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@CrossOrigin(origins = "*", maxAge = 3600)
public abstract class CrudController<T extends BaseDTO> {

    private final CrudService<T> service;

    @Value("${backendA:http://localhost:8081}")
    private String backendA;

    private RestTemplate restTemplate;

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    public CrudController(CrudService<T> crudService) {
        this.service = crudService;
        this.restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
        factory.setHttpClient(httpClient);
        restTemplate.setRequestFactory(factory);
    }

    @GetMapping("")
    @ApiOperation(value = "List all")
    @CircuitBreaker(name = "MovieService", fallbackMethod = "getAllFallback")
    @Retry(name = "MovieService", fallbackMethod = "getAllFallback")
    @TimeLimiter(name = "MovieService", fallbackMethod = "getAllFallback")
    public CompletableFuture<ResponseEntity<String>> getAll() {
        ResponseEntity<String> resp = restTemplate.exchange(backendA + "/movies",HttpMethod.GET, null,String.class);
        return CompletableFuture.completedFuture(new ResponseEntity<>((String) resp.getBody(), resp.getStatusCode()));
    }

    private CompletableFuture<ResponseEntity<List<T>>> getAllFallback(Exception e) {
        return CompletableFuture.completedFuture(new ResponseEntity<>(service.findAll(), HttpStatus.OK));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Get by Id")
    @CircuitBreaker(name = "MovieService", fallbackMethod = "getByIdFallback")
    @Retry(name = "MovieService", fallbackMethod = "getByIdFallback")
    @TimeLimiter(name = "MovieService", fallbackMethod = "getByIdFallback")
    public CompletableFuture<ResponseEntity<String>> getById(@PathVariable Long id) {
        ResponseEntity<String> resp = restTemplate.exchange(backendA + "/movies/"+id, HttpMethod.GET, null,String.class);
        return CompletableFuture.completedFuture(new ResponseEntity<>((String) resp.getBody(), resp.getStatusCode()));
    }

    public CompletableFuture<ResponseEntity<T>> getByIdFallback(@PathVariable Long id, Exception e) {
        Optional<T> optionalT = service.findById(id);

        return CompletableFuture.completedFuture(optionalT.map(T ->
                        new ResponseEntity<>(T, HttpStatus.OK))
                .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND)));
    }

    @PostMapping("")
    @ApiOperation(value = "Create a new one")
    @CircuitBreaker(name = "MovieService", fallbackMethod = "saveFallback")
    @Retry(name = "MovieService", fallbackMethod = "saveFallback")
    public ResponseEntity<String> save(@RequestBody T body) {
        ResponseEntity<String> resp = restTemplate.postForEntity(backendA+"/movies/", body, String.class);
        return new ResponseEntity<>((String) resp.getBody(), resp.getStatusCode());
    }

    public ResponseEntity<T> saveFallback(@RequestBody T body, Exception e) {
        return new ResponseEntity<>(service.save(body), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Delete by Id")
    @CircuitBreaker(name = "MovieService", fallbackMethod = "deleteFallback")
    @Retry(name = "MovieService", fallbackMethod = "deleteFallback")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        ResponseEntity<String> resp = restTemplate.exchange(backendA + "/movies/"+id,HttpMethod.DELETE, null,String.class);

        return new ResponseEntity<>((String) resp.getBody(), resp.getStatusCode());
    }

    public ResponseEntity<?> deleteFallback(@PathVariable Long id, Exception e) {
        Optional<T> optional = service.findById(id);
        optional.ifPresent(n -> service.delete(id));

        return optional.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    @ApiOperation(value = "Update by Id")
    @CircuitBreaker(name = "MovieService", fallbackMethod = "updateFallback")
    @Retry(name = "MovieService", fallbackMethod = "updateFallback")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody T body) {

        HttpEntity<?> httpEntity = new HttpEntity<Object>(body, null);

        ResponseEntity<String> resp = restTemplate.exchange(backendA + "/movies/"+id,HttpMethod.PUT, httpEntity,String.class);
        return new ResponseEntity<>((String) resp.getBody(), resp.getStatusCode());
    }

    public ResponseEntity<?> updateFallback(@PathVariable Long id, @RequestBody T body, Exception e) {
        Optional<T> optional = service.findById(id);
        optional.ifPresent(n -> service.update(id, body));

        return optional.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
