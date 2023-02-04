package ru.voronavk.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.voronavk.utils.hibernate.Hiber;

import javax.persistence.*;

@Entity(name = "url_file")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlFile {
    @Id
    @GeneratedValue
    Long id;
    @Column(name = "url")
    String url;

    public static void save(UrlFile urlFile) {
        EntityManager em = Hiber.getEntityManager();
        em.getTransaction().begin();
        em.persist(urlFile);
        em.flush();
        em.getTransaction().commit();
    }
}
