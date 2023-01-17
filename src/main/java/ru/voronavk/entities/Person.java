package ru.voronavk.entities;

import api.longpoll.bots.model.objects.basic.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.voronavk.utils.hibernate.Hiber;


import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Entity(name = "person")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "is_subscriber")
    private boolean isSubscriber;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @OneToOne()
    private PersonState state;

    public static Person findById(Integer userId) {
        try{
            CriteriaBuilder cb = Hiber.getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Person> cr = cb.createQuery(Person.class);
            Root<Person> root = cr.from(Person.class);
            cr.select(root).where(cb.equal(root.get("id"), userId));
            TypedQuery<Person> query = Hiber.getEntityManager().createQuery(cr);
            return query.getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    public static Person insertNew(User user){
        EntityManager em = Hiber.getEntityManager();

        Person person = new Person();
        Long longId = Long.valueOf(user.getId());

        person.setId(longId);
        person.setFirstName(user.getFirstName());
        person.setLastName(user.getLastName());

        PersonState personState = new PersonState();
        personState.setPersonId(person.getId());
        personState.setInChat(false);

        em.getTransaction().begin();

        em.persist(personState);
        em.flush();
        person.setState(personState);
        em.persist(person);
        em.flush();

        em.getTransaction().commit();
        return findById(user.getId());
    }

    public static void save(Person p) {
        EntityManager em = Hiber.getEntityManager();
        em.getTransaction().begin();
        em.persist(p);
        em.flush();
        em.getTransaction().commit();

    }

    public static void changePhase(Person person, Phase phase) {
        person.getState().setOrderPhase(phase);
        Person.save(person);
    }

    public PrintPhotoOrder getCurrentPrintPhotoOrder(){
        List<PrintPhotoOrder> printPhotoOrders = this.getState().getCurrentOrder().getPrintPhotoMultiOrder().getPrintPhotoOrders();
        Long maxId = 0L;
        int indexOfPrintPhotoOrder = 0;
        for (int i = 0; i < printPhotoOrders.size(); i++){
            if(printPhotoOrders.get(i).getId() > maxId){
                maxId = printPhotoOrders.get(i).getId();
                indexOfPrintPhotoOrder = i;
            }
        }
        return printPhotoOrders.get(indexOfPrintPhotoOrder);
    }


    public FormatPhoto getFormatOfLastPrintPhotoOrder(){
        PrintPhotoOrder currentPrintPhotoOrder = getCurrentPrintPhotoOrder();
        return currentPrintPhotoOrder.getFormatPhoto();
    }
}



