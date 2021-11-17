package fr.lacazethomas.lab1.controller;

import fr.lacazethomas.lab1.controller.dto.BaseDTO;
import fr.lacazethomas.lab1.service.CrudService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public abstract class CrudController<T extends BaseDTO> {

    private final CrudService<T> service;

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    public CrudController(CrudService<T> crudService) {
        this.service = crudService;
    }

    @GetMapping("")
    @ApiOperation(value = "List all")
    @CircuitBreaker(name = "MovieService", fallbackMethod = "getAllFallback")
    public ResponseEntity<String> getAll() {
        String response = restTemplate.getForObject("http://localhost:8081/movies", String.class);
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    public ResponseEntity<List<T>> getAllFallback(Exception e) {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Get by Id")
    @CircuitBreaker(name = "MovieService", fallbackMethod = "getByIdFallback")
    public ResponseEntity<String> getById(@PathVariable Long id) {
        String response = restTemplate.getForObject("http://localhost:8081/movies/" + id, String.class);
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    public ResponseEntity<T> getByIdFallback(@PathVariable Long id, Exception e) {
        Optional<T> optionalT = service.findById(id);

        return optionalT.map(T ->
                        new ResponseEntity<>(T, HttpStatus.OK))
                .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PostMapping("")
    @ApiOperation(value = "Create a new one")
    public ResponseEntity<T> save(@RequestBody T body) {
        return new ResponseEntity<>(service.save(body), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Delete by Id")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<T> optional = service.findById(id);
        optional.ifPresent(n -> service.delete(id));

        return optional.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    @ApiOperation(value = "Update by Id")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody T body) {
        Optional<T> optional = service.findById(id);
        optional.ifPresent(n -> service.update(id, body));

        return optional.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
