package ru.voronavk.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.voronavk.utils.hibernate.Hiber;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "different_count_photo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DifferentCountPhoto {
    @Id
    @GeneratedValue
    Long id;
    @Column(name = "count")
    Integer count;
    @OneToOne
    UrlFile urlFile;

    public static DifferentCountPhoto createNew(Person person) {
        PrintPhotoOrder lastPrintPhotoOrder = Person.lastPrintPhotoOrder(person);
        DifferentCountPhoto differentCountPhoto = new DifferentCountPhoto();
        save(differentCountPhoto);
        List<DifferentCountPhoto> differentCountPhotos = lastPrintPhotoOrder.getDifferentCountPhotos();
        if(differentCountPhotos == null || differentCountPhotos.size() > 0){
            differentCountPhotos = new ArrayList<>();
        }
        differentCountPhotos.add(differentCountPhoto);
        lastPrintPhotoOrder.setDifferentCountPhotos(differentCountPhotos);
        PrintPhotoOrder.save(lastPrintPhotoOrder);
        Person.save(person);
        return differentCountPhoto;
    }
    public static void save(DifferentCountPhoto differentCountPhoto){
        EntityManager em = Hiber.getEntityManager();
        em.getTransaction().begin();
        em.persist(differentCountPhoto);
        em.flush();
        em.getTransaction().commit();
    }
}
