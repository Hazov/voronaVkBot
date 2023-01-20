package ru.voronavk.entities;

import com.google.gson.internal.$Gson$Types;
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
@Entity(name = "person_field")
public class PersonField {
    @Id
    @GeneratedValue
    Integer id;
    @Column(name = "class_name")
    String className;
    @Column(name = "setter")
    String setter;
    @Column(name = "type")
    String type;
    @Column(name = "accessNewIfIsList")
    Boolean accessNewIfIsList;
    @Column(name = "to_check")
    boolean toCheck;
    @Column(name = "to_change")
    boolean toChange;


    public static void save(PersonField personField){
        EntityManager em = Hiber.getEntityManager();
        em.getTransaction().begin();
        em.persist(personField);
        em.flush();
        em.getTransaction().commit();
    }


}
