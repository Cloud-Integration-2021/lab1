package fr.lacazethomas.lab1.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ActorDTO {
    private String firstName;
    private String lastName;
    private String birthDate;
}