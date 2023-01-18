package ru.voronavk.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.voronavk.utils.hibernate.Hiber;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PersonField {
    @Id
    Integer id;
    @Column
    String className;
    @Column
    String setter;
    @Column
    String type;
    @Column
    Boolean accessNewIfIsList;


    public static void save(PersonField personField){
        EntityManager em = Hiber.getEntityManager();
        em.getTransaction().begin();
        em.persist(personField);
        em.flush();
        em.getTransaction().commit();
    }


}
