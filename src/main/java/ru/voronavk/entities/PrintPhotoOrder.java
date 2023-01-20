package ru.voronavk.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.voronavk.utils.hibernate.Hiber;

import javax.persistence.*;
import java.util.List;

@Entity(name = "print_photo_order")
@Getter
@Setter
@NoArgsConstructor
public class PrintPhotoOrder implements InnerPartOrder {
    @Id
    @GeneratedValue
    Long id;
    @OneToOne
    FormatPhoto formatPhoto;
    @Column(name = "papper_type")
    String papperType;
    @Column(name = "images_total_count")
    int imagesTotalCount;
    @Column(name = "part_of_multiorder")
    boolean partOfMultiPhotoOrder;
    @Column(name = "have_different_count_photos")
    boolean haveDifferentCountPhotos;
    @OneToMany
    List<DifferentCountPhoto> differentCountPhoto;
    @OneToMany
    List<UrlFile> photos;

    public static void save(PrintPhotoOrder printPhotoOrder) {
        EntityManager em = Hiber.getEntityManager();
        em.getTransaction().begin();
        em.persist(printPhotoOrder);
        em.flush();
        em.getTransaction().commit();
    }

}