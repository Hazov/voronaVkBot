package ru.voronavk.entities;

import api.longpoll.bots.model.objects.additional.Keyboard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.voronavk.annotations.ForApi;
import ru.voronavk.utils.hibernate.Hiber;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Entity(name = "phase")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Phase {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "phrase")
    private String phrase;
    @Transient
    private Keyboard answerKeyBoard;
    @Column(name = "keyboard_name")
    private String KeyboardName;
    @Column(name = "to_section")
    private String toSection;
    @Column(name = "phase_key")
    private String phaseKey;
    @Column(name = "files_waiting_count")
    private int filesWaitingCount;
    @Column(name = "wait_for_message")
    private boolean waitForMessage;
    @Column(name = "callback_on_message")
    private String callbackOnMessage;
    @Column(name = "additional_callback_on_btn")
    private String additionalCallbackOnBtn;
    //Поля для проверки, которые должны присутствовать, чтобы перейти на эту фазу
    // Поля, которые должны были быть получены на предыдущих фазах. Есть автопроверка
    //Поля к изменению. Можно не писать, если изменением управляет callbackOnMessage или additionalCallbackOnBtn
    @ManyToMany()
    private List<PersonField> fields;

    @Column(name = "prev_phase_key")
    private String prevPhaseKey;
    @Column(name = "next_phase_key")
    private String nextPhaseKey;
    @Column(name = "is_error")
    private boolean isError;

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

    public static List<Phase> findPhasesBySection(String section) {

        CriteriaBuilder cb = Hiber.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Phase> cr = cb.createQuery(Phase.class);
        Root<Phase> root = cr.from(Phase.class);
        cr.select(root).where(cb.equal(root.get("toSection"), section));
        TypedQuery<Phase> query = Hiber.getEntityManager().createQuery(cr);
        return query.getResultList();

    }

    //TODO разобраться с lombok и удалить этот метод
    public boolean getWaitForMessage() {
        return waitForMessage;
    }
}
