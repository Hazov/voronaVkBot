package ru.voronavk.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
}
