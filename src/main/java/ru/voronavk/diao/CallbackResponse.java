package ru.voronavk.diao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.voronavk.entities.Phase;

@AllArgsConstructor
@Getter
@Setter
public class CallbackResponse {
    boolean ok;
    Phase nextPhase;

}
