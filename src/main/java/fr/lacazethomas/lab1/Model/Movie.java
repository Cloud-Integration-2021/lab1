package fr.lacazethomas.lab1.model;

import lombok.*;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie extends BaseEntity {
    private String title;
    private LocalDate releaseDate;
}
