package ru.voronavk.utils;

import api.longpoll.bots.model.objects.additional.buttons.Button;
import api.longpoll.bots.model.objects.additional.buttons.CallbackButton;
import ru.voronavk.diao.ObjectWithParams;
import ru.voronavk.entities.Person;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ButtonsUtil {

    private static final String thisClassName = "ru.voronavk.utils.PrintPhotoUtil";

    public static List<List<Button>> replaceLabels(List<List<Button>> buttons, Person person){
        Map<Integer, List<Integer>> removedMap = new HashMap<>();
        for (int i = 0; i < buttons.size(); i++) {
            for (int j = 0; j < buttons.get(i).size(); j++) {
                String oldLabel = ((CallbackButton.Action) buttons.get(i).get(j).getAction()).getLabel();
                String newLabel = applyFormula(oldLabel, person);
                if(newLabel == null){
                    removedMap.computeIfAbsent(i, k -> new ArrayList<>());
                    removedMap.get(i).add(j);
                    break;
                } else {
                    ((CallbackButton.Action) buttons.get(i).get(j).getAction()).setLabel(newLabel);
                }
            }
        }
        if(removedMap.size() > 0){
            for (Map.Entry<Integer, List<Integer>> entry : removedMap.entrySet()){
                int listId = entry.getKey();
                if(buttons.get(listId).size() == entry.getValue().size()){
                    buttons.remove(listId);
                    continue;
                }
                for (Integer b : entry.getValue()){
                    int buttonId = b;
                    buttons.get(listId).remove(buttonId);
                }
            }
        }
        return buttons;
    }

    public static String applyFormula(String oldLabel, Person person){
        if(oldLabel.indexOf('@') != -1) {
            String leftRestText = oldLabel.substring(0, oldLabel.indexOf('@'));
            String rightRestText = oldLabel.substring(oldLabel.lastIndexOf('@') + 1, oldLabel.length());
            try {
                String className, methodName;
                String paramNames[];
                String fullFormula = oldLabel.substring(oldLabel.indexOf('@') + 1, oldLabel.lastIndexOf('@'));
                className = fullFormula.substring(0, fullFormula.indexOf("."));
                if (className.equals("currentUtil")) className = thisClassName;
                fullFormula = fullFormula.substring(fullFormula.indexOf(".") + 1);
                methodName = fullFormula.substring(0, fullFormula.indexOf("("));
                fullFormula = fullFormula.substring(fullFormula.indexOf("(") + 1, fullFormula.lastIndexOf(")"));
                paramNames = fullFormula.split(",");


                Class<?> utilClass = Class.forName(className);
                Method method = Arrays.stream(utilClass.getMethods()).filter(m -> m.getName().equals(methodName)).collect(Collectors.toList()).get(0);
                ObjectWithParams objectWithParams = fetchParams(person, paramNames);

                Object textObj = method.invoke(objectWithParams.getObject(), objectWithParams.getParams());
                if (textObj != null) {
                    String text = textObj.toString();
                    return leftRestText + text + rightRestText;
                } else {
                    return null;
                }
            } catch (Exception e) {
                return leftRestText + rightRestText;
            }
        }
        return oldLabel;
    }


    private static ObjectWithParams fetchParams(Person person, String[] paramNames) {
        ObjectWithParams objectWithParams = new ObjectWithParams();
        try{
            Object object = null;
            Object objectForInnerMethod = null;
            List<Object> objects = new LinkedList<>();
            for (int i = 0; i < paramNames.length; i++){
                String[] paramsPath = paramNames[i].split("\\.");
                for(int j = 0; j < paramsPath.length; j++){
                    if(paramsPath[j].equals("person")){
                        objectForInnerMethod = person;
                        continue;
                    }
                    if(paramsPath[j].startsWith("\"")){
                        //убираем кавычки
                        String param = paramsPath[j].substring(1, paramsPath[j].length() - 1);
                        objects.add(param);
                    } else {
                        String methodName = paramsPath[j];
                        Class<?> personClass = Person.class;
                        Method method = Arrays.stream(personClass.getMethods())
                                .filter(m -> m.getName().equals(methodName))
                                .collect(Collectors.toList()).get(0);
                        Object invoke = method.invoke(objectForInnerMethod);
                        objects.add(invoke);
                    }
                }

            }
            Object[] objs = objects.toArray();
            objectWithParams.setObject(object);
            objectWithParams.setParams(objs);
        }catch (Exception e){
            return objectWithParams;
        }
        return objectWithParams;
    }
}

