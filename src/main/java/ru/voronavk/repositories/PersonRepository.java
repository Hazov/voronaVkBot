package ru.voronavk.repositories;

import ru.voronavk.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
  Person findOne(Long id);

}
