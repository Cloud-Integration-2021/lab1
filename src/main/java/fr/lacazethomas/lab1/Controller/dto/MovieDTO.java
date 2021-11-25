package fr.lacazethomas.lab1.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

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

    @Transient
    private List<ActorDTO> actors;

    public void addActorDTO(ActorDTO actorDTO) {
        if (this.actors == null) {
            this.actors = new ArrayList<>();
        }
        this.actors.add(actorDTO);
    }
}