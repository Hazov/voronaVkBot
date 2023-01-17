package ru.voronavk.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "person_state")
@Getter
@Setter
public class PersonState{
    @Id
    @GeneratedValue
    @Column(name = "id")
    Long id;
    @Column(name = "personId")
    long personId;
    //Дата/время начала заказа
    @Column(name = "start_ts")
    Date startTs;
    //Собирающийся заказ
    @OneToOne
    MultiOrder currentOrder;
    @OneToOne
    Phase orderPhase;
    //Находится в состоянии заказа
    @Column(name = "in_order")
    boolean inOrder;
    @Column(name = "in_chat")
    boolean inChat;
}
