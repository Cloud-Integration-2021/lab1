package fr.lacazethomas.lab1;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.lacazethomas.lab1.controller.MovieController;
import fr.lacazethomas.lab1.controller.MovieV2Controller;
import fr.lacazethomas.lab1.model.Movie;
import fr.lacazethomas.lab1.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.time.LocalDate;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@RunWith(SpringRunner.class)
public class MovieV2ControllerIntegrationTest {

    MockMvc mvc;

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MovieRepository repository;

    @Autowired
    MovieV2Controller movieController;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.standaloneSetup(this.movieController).build();
        repository.deleteAll();
    }

    @Test
    public void getAllMovies() throws Exception {
        mvc.perform(get("/V2/movies").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void postMovie() throws Exception {
        Movie m1 = new Movie("je suis un title", LocalDate.now(), "je suis un plot");
        mvc.perform(
                        post("/V2/movies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(m1)
                                ))
                .andExpect(status().isCreated());
    }

    @Test
    public void getMoviesWithActors() throws Exception {
        // Given


        var m1 = new Movie("je suis un title", LocalDate.of(2020, 1, 8), "je suis un plot");
        repository.save(m1);
        var m2 = new Movie("test2", LocalDate.of(2020, 1, 8), "test2plot");
        repository.save(m2);

        // When
        mvc.perform(
                        get("/V2/movies")
                                .contentType(MediaType.APPLICATION_JSON)
                )

                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("je suis un title")))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].releaseDate", is("2020-01-08")))
                .andExpect(jsonPath("$[0].plot", is("je suis un plot")))
                .andExpect(jsonPath("$[1].title", is("test2")))
                .andExpect(jsonPath("$[1].plot", is("test2plot")))
                .andExpect(jsonPath("$[1].actors[0].firstName", is("Doe")))
                .andExpect(jsonPath("$[1].actors[0].lastName", is("John")))
                .andExpect(jsonPath("$[1].actors[0].birthDate", is("2020-12-22")))
                .andExpect(jsonPath("$[1].releaseDate", is("2020-01-08")));
    }

    @Test
    public void deleteMovie() throws Exception {
        // Given
        var m1 = new Movie("je suis un title", LocalDate.of(2020, 1, 8), "je suis un plot");
        repository.save(m1);
        var m2 = new Movie("test2", LocalDate.of(2020, 1, 8), "test2plot");
        repository.save(m2);

        // When
        mvc.perform(
                        delete("/V2/movies/" + m2.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Then
        assertEquals("The subject should have been deleted", 1L, repository.count());
    }

    @Test
    public void updateMovie() throws Exception {
        // Given
        var m = new Movie("test2", LocalDate.of(2020, 1, 8), "test2plot");
        repository.save(m);

        var editDto = new Movie();
        editDto.setTitle("je suis un title 2");
        editDto.setPlot("je suis un plot 2");
        editDto.setReleaseDate(LocalDate.of(2021, 2, 9));

        // When
        mvc.perform(
                        put("/V2/movies/" + m.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(editDto)))
                .andExpect(status().isOk());

        // Then
        mvc.perform(
                        get("/V2/movies")
                                .contentType(MediaType.APPLICATION_JSON)
                )

                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("je suis un title 2")))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].releaseDate", is("2021-02-09")))
                .andExpect(jsonPath("$[0].plot", is("je suis un plot 2")));
    }

}