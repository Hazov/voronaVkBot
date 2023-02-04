package ru.voronavk.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.voronavk.utils.hibernate.Hiber;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    List<DifferentCountPhoto> differentCountPhotos;
    @OneToMany
    List<UrlFile> photos;

    public static void save(PrintPhotoOrder printPhotoOrder) {
        EntityManager em = Hiber.getEntityManager();
        em.getTransaction().begin();
        em.persist(printPhotoOrder);
        em.flush();
        em.getTransaction().commit();
    }

    public static PrintPhotoOrder createNew(Person person) {
        PrintPhotoMultiOrder printPhotoMultiOrder = person.getState().getCurrentOrder().getPrintPhotoMultiOrder();
        List<PrintPhotoOrder> printPhotoOrders = person.getState().getCurrentOrder().getPrintPhotoMultiOrder().getPrintPhotoOrders();
        if(printPhotoOrders == null){
            printPhotoOrders = new ArrayList<>();
            printPhotoMultiOrder.setPrintPhotoOrders(printPhotoOrders);
            Person.save(person);
        }
        PrintPhotoOrder printPhotoOrder = new PrintPhotoOrder();
        save(printPhotoOrder);
        printPhotoOrders.add(printPhotoOrder);
        Person.save(person);
        return printPhotoOrder;
    }

    public static DifferentCountPhoto lastDifferentCountPhoto(PrintPhotoOrder lastPrintPhotoOrder) {
        List<DifferentCountPhoto> differentCountPhotos = lastPrintPhotoOrder.getDifferentCountPhotos();
        if(differentCountPhotos == null){
            differentCountPhotos = new ArrayList<>();
            lastPrintPhotoOrder.setDifferentCountPhotos(differentCountPhotos);
        }
        if(differentCountPhotos.size() == 0){
            DifferentCountPhoto differentCountPhoto = new DifferentCountPhoto();
            lastPrintPhotoOrder.getDifferentCountPhotos().add(differentCountPhoto);
            DifferentCountPhoto.save(differentCountPhoto);
            PrintPhotoOrder.save(lastPrintPhotoOrder);

            return differentCountPhoto;
        }
        long lastId = -1;
        differentCountPhotos = differentCountPhotos.stream()
                .filter(dcf -> dcf.getUrlFile() == null || dcf.getCount() == null || (dcf.getCount() != null && dcf.getCount() == 0))
                .collect(Collectors.toList());
        for(DifferentCountPhoto differentCountPhoto: differentCountPhotos)
            if(differentCountPhoto.getId() > lastId)
                lastId = differentCountPhoto.getId();
        if(lastId == -1) return null;
        else {
            long finalLastId = lastId;
            return differentCountPhotos.stream().filter(dcf -> dcf.getId() == finalLastId).collect(Collectors.toList()).get(0);
        }
    }
}