package fr.lacazethomas.lab1.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO extends BaseDTO {
    private Long id;
    private String title;
    private LocalDate releaseDate;
}
