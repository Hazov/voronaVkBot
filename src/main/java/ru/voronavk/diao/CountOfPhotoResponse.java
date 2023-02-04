package ru.voronavk.diao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.voronavk.entities.Phase;

@Getter
@Setter
@AllArgsConstructor
public class CountOfPhotoResponse {
    Integer count;
    Phase nextPhase;
}
