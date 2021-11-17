package fr.lacazethomas.lab1.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO extends BaseDTO {
    private Long id;
    private String title;
    private String releaseDate;
    private String plot;
}
