package ru.voronavk.entities;

import lombok.Getter;
import lombok.Setter;
import ru.voronavk.utils.hibernate.Hiber;

import javax.persistence.*;
import java.util.List;

@Entity(name = "multi_order")
@Getter
@Setter
public class MultiOrder {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    @Column(name = "person_id")
    private Long personId;
    @Column(name = "comment")
    private String comment;
    @OneToOne(fetch=FetchType.EAGER)
    private PrintPhotoMultiOrder printPhotoMultiOrder;

    public static void save(MultiOrder currentOrder) {
        EntityManager em = Hiber.getEntityManager();
        em.getTransaction().begin();
        em.persist(currentOrder);
        em.flush();
        em.getTransaction().commit();
    }
}


















