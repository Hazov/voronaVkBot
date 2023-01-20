package ru.voronavk.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
    @OneToOne()
    UrlFile urlFile;
}
