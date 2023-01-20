package ru.voronavk.utils.reflection;

import ru.voronavk.entities.Person;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Finder {

    private static final char PKG_SEPARATOR = '.';

    private static final char DIR_SEPARATOR = '/';

    private static final String CLASS_FILE_SUFFIX = ".class";

    private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";
    private static Object findEntity;


    /**
     * Возвращает список классов в пакете
     */
    public static List<Class<?>> find(String scannedPackage) {
        String scannedPath = scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR);
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
        if (scannedUrl == null) {
            throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
        }
        File scannedDir = new File(scannedUrl.getFile());
        List<Class<?>> classes = new ArrayList<>();
        for (File file : scannedDir.listFiles()) {
            classes.addAll(find(file, scannedPackage));
        }
        return classes;
    }

    private static List<Class<?>> find(File file, String scannedPackage) {
        List<Class<?>> classes = new ArrayList<>();
        String resource = scannedPackage + PKG_SEPARATOR + file.getName();
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                classes.addAll(find(child, resource));
            }
        } else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
            int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
            String className = resource.substring(0, endIndex);
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException ignore) {
            }
        }
        return classes;
    }

    public static Class<?> findSameClassWithMulti(Class<?> clazz, String entities){
        List<Class<?>> classes = find(entities);
        String[] split = clazz.getName().split("(?=[A-Z])");
        List<String> variants = new ArrayList<>();

        for (int i = 0; i < split.length; i++){
            StringBuilder result = new StringBuilder();
            for (int j = 0; j < split.length; j++){
                if(i != j){
                    result.append(split[j]);
                } else {
                    result.append(split[j]).append("Multi");
                }
            }
            variants.add(result.toString());
        }
        return classes.stream().filter(c -> variants.stream().anyMatch(v -> v.equals(c.getName()))).collect(Collectors.toList()).get(0);
    }
    public static Method findMethodByName(Class<?> clazz, String methodName){
        List<Method> collect = Arrays.stream(clazz.getMethods()).filter(m -> m.getName().equals(methodName)).collect(Collectors.toList());
        if(collect.size() > 0){
            return collect.get(0);
        }
        return null;
    }

    public static Object findEntityInPerson(Object person, Class<?> toSearchClass) {
        Class<?> pClass = person.getClass();
        if(person.getClass() == toSearchClass) return person;
        List<Method> methods = Arrays.stream(pClass.getDeclaredMethods()).filter(m -> m.getReturnType().getName().equals(toSearchClass.getName())).collect(Collectors.toList());
        if(methods.size() == 0){
            List<Method> getters = Arrays.stream(pClass.getDeclaredMethods())
                    .filter(m -> m.getName().startsWith("get"))
                    .collect(Collectors.toList());
            for (Method getter: getters){
                try{
                    findEntityInPerson(getter.invoke(person), toSearchClass);
                } catch (Exception e){

                }
            }



        } else {
            try{
                return methods.get(0).invoke(person);
            } catch (Exception e){
                return null;
            }
        }
        return null;
    }

    public static Class<?> searchClassByName(String typeString) {
        String javaPrefix = "java.lang.";
        String entityPrefix = "ru.voronavk.entities.";
        try{
            return Class.forName(javaPrefix + typeString);
        } catch (Exception e){
            try{
                return Class.forName(entityPrefix + typeString);
            } catch (Exception ex){
                return null;
            }
        }
    }
}

