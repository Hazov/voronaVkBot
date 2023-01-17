package ru.voronavk.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DifferentCountPhoto {
    @Id
    @GeneratedValue
    Long id;
    @Column
    Integer count;
    @OneToOne
    UrlFile urlFile;
}
