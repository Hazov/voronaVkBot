package ru.voronavk.entities;

import lombok.Getter;
import lombok.Setter;
import ru.voronavk.utils.hibernate.Hiber;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public
class PrintPhotoMultiOrder implements PartOfMultiOrder {
    @Id
    @GeneratedValue
    @Column
    Long id;
    @OneToMany
    private List<PrintPhotoOrder> printPhotoOrders;
    @Column
    private String comment;

    public static void save(PrintPhotoMultiOrder printPhotoMultiOrder) {
        EntityManager em = Hiber.getEntityManager();
        em.getTransaction().begin();
        em.persist(printPhotoMultiOrder);
        em.flush();
        em.getTransaction().commit();
    }
}