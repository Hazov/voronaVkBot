package ru.voronavk.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.voronavk.utils.PrintPhotoUtil;
import ru.voronavk.annotations.ForApi;
import ru.voronavk.utils.hibernate.Hiber;

import javax.persistence.*;

@Entity(name = "format_photo")
@Getter
@Setter
@NoArgsConstructor
public class FormatPhoto implements Comparable<FormatPhoto>{
    @Id
    @GeneratedValue
    Long id;
    @Column(name = "width")
    double width;
    @Column(name = "height")
    double height;

    public FormatPhoto(double width, double height) {
        this.width = Math.max(width, height);
        this.height = Math.min(width, height);
    }
    @ForApi
    public FormatPhoto(String str){
        FormatPhoto formatPhotoByString = PrintPhotoUtil.getFormatPhotoByString(str);
        if(formatPhotoByString != null){
            this.width = formatPhotoByString.width;
            this.height = formatPhotoByString.height;
        }
    }

    @Override
    public int compareTo(FormatPhoto o) {
        if(this.getWidth() >= o.getWidth() && this.getHeight() >= o.getHeight()){
         return 1;
        }
        return 0;
    }

    //используется в рефлексии
    public static void save(FormatPhoto formatPhoto){
        EntityManager em = Hiber.getEntityManager();
        em.getTransaction().begin();
        em.persist(formatPhoto);
        em.flush();
        em.getTransaction().commit();
    }
}
