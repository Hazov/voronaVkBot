package ru.voronavk.entities;

import api.longpoll.bots.model.objects.additional.Keyboard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.voronavk.diao.PhaseParams;
import ru.voronavk.utils.annotations.ForApi;
import ru.voronavk.utils.dialogs.statics.Keyboards;
import ru.voronavk.utils.hibernate.Hiber;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Phase {
    @Id
    @GeneratedValue
    Integer id;
    @Column(name = "phrase")
    String phrase;
    @Transient
    Keyboard answerKeyBoard;
    @Column
    String toSection;
    @Column
    String phaseKey;
    @Column
    int filesWaitingCount;
    @Column
    boolean waitForMessage;
    @Column
    String callbackOnMessage;
    @Column
    String additionalCallbackOnBtn;
    //Поля для проверки, которые должны присутствовать, чтобы перейти на эту фазу
    // Поля, которые должны были быть получены на предыдущих фазах. Есть автопроверка
    @ManyToMany
    List<PersonField> fieldsToCheck;
    //Поля к изменению. Можно не писать, если изменением управляет callbackOnMessage или additionalCallbackOnBtn
    @ManyToMany
    List<PersonField> fieldsToChange;
    @Column
    String prevPhaseKey;
    @Column
    String nextPhaseKey;
    @Column
    boolean isError;

@   ForApi
    public Phase(String phrase, Keyboard answerKeyBoard){
        this.phrase = phrase;
        this.answerKeyBoard = answerKeyBoard;
    }

    public Phase(Phase phase){
       this.phrase = phase.phrase;
       this.answerKeyBoard = phase.answerKeyBoard;
    }

    public static void save(Phase phase) {
        EntityManager em = Hiber.getEntityManager();
        em.getTransaction().begin();
        em.persist(phase);
        em.flush();
        em.getTransaction().commit();
    }
}
