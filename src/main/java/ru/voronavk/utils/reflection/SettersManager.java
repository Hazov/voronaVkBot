package ru.voronavk.utils.reflection;

import ru.voronavk.annotations.ForApi;
import ru.voronavk.entities.Person;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SettersManager {


    Method getSetter(String field, String currentClass){
        String[] fieldSplit = field.split(":");
        String className;
        if(fieldSplit[0].equals("currentClass")){
            className = currentClass;
        } else {
            className = fieldSplit[0];
        }

        String setterName = fieldSplit[1];
        setterName = "set" + setterName.replaceFirst(String.valueOf(setterName.charAt(0)), String.valueOf(Character.toUpperCase(setterName.charAt(0))));
        try{
            Class<?> aClass = Class.forName(className);
            String finalSetterName = setterName;
//            this.fieldSetter = Arrays.stream(aClass.getMethods()).filter(m -> m.getName().equals(finalSetterName)).collect(Collectors.toList()).get(0);
//            this.value = value;
//            this.person= person;
            Class<Person> personClass = Person.class;
//            setField(personClass, setterName, person, value);

        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


   public static  Object setField(Class<?> clazz, String setterName, Object entity, String value) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        List<Method> filteredMethods = Arrays.stream(clazz.getMethods())
                .filter(m -> m.getName().startsWith("get") || m.getName().startsWith("is"))
                .collect(Collectors.toList());
        for (Method getter: filteredMethods) {
            if(getter.getName().toLowerCase().equals("get" + setterName.substring(3).toLowerCase())
                    || getter.getName().toLowerCase().equals("is" + setterName.substring(3).toLowerCase())){
                Class<?> entityClass = entity.getClass();
                Method setter = Arrays.stream(entityClass.getMethods())
                        .filter(m -> m.getName().equals(setterName))
                        .collect(Collectors.toList())
                        .get(0);
                if(!getter.getReturnType().isPrimitive() && getter.getReturnType() != String.class){

                    Class<?> returnType = getter.getReturnType();
                    Constructor<?> constructor = Arrays.stream(returnType.getConstructors())
                            .filter(c -> c.getAnnotation(ForApi.class) != null)
                            .collect(Collectors.toList()).get(0);
                    Object o = constructor.newInstance(value);
                    setter.invoke(entity, o);
                    Method methodSave = Arrays.stream(returnType.getMethods())
                            .filter(m -> m.getName()
                                    .equals("save"))
                            .collect(Collectors.toList())
                            .get(0);
                    methodSave.invoke(null, o);

                } else {

                    if(value.toLowerCase().equals("yes")){
                        setter.invoke(entity, true);
                    } else if (value.toLowerCase().equals("no")){
                        setter.invoke(entity, false);
                    } else{
                        setter.invoke(entity, value);
                    }




                }
                break;
                //Иначе ищем вложенный метод в объект
            } else {
                try{
                    Object object = getter.invoke(entity);
                    if(object instanceof List){
                        object = ((List) object).get(0);
                    }
                    if(object != null && !(object instanceof Class)){
                        Class<?> aClass = object.getClass();
                        Object o = setField(aClass, setterName, object, value);
                        if(o != null){
                            break;
                        }
                    }
                }catch (Exception e){
                    continue;
                }
            }
        }
        return null;
    }
}
