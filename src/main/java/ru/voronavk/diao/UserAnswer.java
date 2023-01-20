package ru.voronavk.diao;

import lombok.Getter;
import lombok.Setter;
import ru.voronavk.entities.Person;
import ru.voronavk.annotations.ForApi;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserAnswer {
    Method fieldSetter;
    String value;
    Person person;

    public UserAnswer(String field, String value, String currentClass, Person person) {


    }

    private Object setField(Class<?> clazz, String setterName, Object entity, String value) throws InvocationTargetException, IllegalAccessException {
        try{

        } catch (Exception e){
            return null;
        }

        return null;
    }
}
