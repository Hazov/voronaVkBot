package ru.voronavk.utils.dialogs.statics;

import api.longpoll.bots.model.objects.additional.Keyboard;
import ru.voronavk.entities.Phase;
import ru.voronavk.annotations.ForApi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public class Phases {
    private static final Map<String, Phase> aboutPrintPhotos = new HashMap<>();
    private static final Map<String, Phase> common = new HashMap<>();
    static  {
        initPhaseMaps("$print-photos", aboutPrintPhotos);
        initPhaseMaps("$common", common);
    }

    static final Constructor<?> phaseConstructor = Arrays.stream(Phase.class.getConstructors()).filter(c -> c.getAnnotation(ForApi.class) != null).collect(Collectors.toList()).get(0);
    static final BiFunction<String, Supplier<Keyboard>, Phase> func = (phrase, keyboardSupplier) -> {
        try{
            return (Phase) phaseConstructor.newInstance(phrase, keyboardSupplier.get());
        } catch (Exception e){

        }
        return null;
    };



    private static void initPhaseMaps(String section, Map<String, Phase> map) {
        List<Phase> printPhotoPhasesList = Phase.findPhasesBySection(section);
        printPhotoPhasesList.forEach(p -> {
            String keyboardName = p.getKeyboardName();
            p.setAnswerKeyBoard(buildKeyBoardByName(keyboardName));
            map.put(p.getPhaseKey(), p);
        });
    }

    public static Phase aboutPrintPhotos(String phaseKey){
        Phase phase = aboutPrintPhotos.get(phaseKey);
        if(phase == null) return null;
        phase.setAnswerKeyBoard(buildKeyBoardByName(phase.getKeyboardName()));
        return phase;
    }
    public static Phase common(String phaseKey){
        Phase phase = common.get(phaseKey);
        phase.setAnswerKeyBoard(buildKeyBoardByName(phase.getKeyboardName()));
        return phase;
    }

    public static Phase getCurrentPhase(String to, String phaseKey) {
        if(to.equals("$print-photos")){
            return aboutPrintPhotos(phaseKey);
        }
        if(to.equals("$photo-on-docs")){
            return null;
        }
        return null;
    }

    private static Keyboard buildKeyBoardByName(String keyboardName){
        if(keyboardName != null){
            try{
                Class<Keyboards> keyboardsClass = Keyboards.class;
                Method keyboardGetter = Arrays.stream(keyboardsClass.getMethods())
                        .filter(m -> m.getName().equals(keyboardName))
                        .collect(Collectors.toList()).get(0);
                return (Keyboard) keyboardGetter.invoke(null);
            } catch (Exception e){
                return null;
            }
        }
        return null;
    }




}


