package ru.voronavk.utils.dialogs.statics;

import api.longpoll.bots.model.objects.additional.Keyboard;
import ru.voronavk.diao.PhaseParams;
import ru.voronavk.entities.Phase;
import ru.voronavk.utils.annotations.ForApi;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public class Phases {
    //ЗАКАЗ ПЕЧАТИ ФОТО
    


    private static final Map<String, PhaseParams> aboutPrintPhotos = new HashMap<>();
    static final Constructor<?> phaseConstructor = Arrays.stream(Phase.class.getConstructors()).filter(c -> c.getAnnotation(ForApi.class) != null).collect(Collectors.toList()).get(0);
    static final BiFunction<String, Supplier<Keyboard>, Phase> func = (phrase, keyboardSupplier) -> {
        try{
            return (Phase) phaseConstructor.newInstance(phrase, keyboardSupplier.get());
        } catch (Exception e){

        }
        return null;
    };



    static {
//        aboutPrintPhotos.put("@manyFormats", new PhaseParams("Все фотографии будут одного формата?", Keyboards::manyFormatsKeyBoard));
//        aboutPrintPhotos.put("@firstFormat", new PhaseParams("Давайте начнем с первого. Какой будет первый формат?", Keyboards::formatsPhotoKeyBoard));
//        aboutPrintPhotos.put("@whatFormat", new PhaseParams("Какой формат?", Keyboards::formatsPhotoKeyBoard));
//        aboutPrintPhotos.put("@warnNoFormat", new PhaseParams("К сожалению, такого формата фотографии мы не делаем", Keyboards::warnNoFormatKeyBoard));
//        aboutPrintPhotos.put("@papperType", new PhaseParams("Выберите тип бумаги", Keyboards::papperTypeKeyBoard));
//        aboutPrintPhotos.put("@repeatPhotos", new PhaseParams("Будут ли среди фотографий те, которые нужно отпечатать несколько раз?", Keyboards::repeatPhotosKeyBoard));
//        aboutPrintPhotos.put("@byOneWithCount", new PhaseParams("Пожалуйста, скиньте сначала их ПО ОДНОЙ, при этом напишите в сообщении к фотографии ее количество", Keyboards::byOneWithCountKeyBoard));
//        aboutPrintPhotos.put("@sizes", new PhaseParams("Введите размер (примерно так: \"14 на 18.7\" или так \"7,5x10\")", Keyboards::sizesKeyBoard));
//        aboutPrintPhotos.put("@spellOutFormat", new PhaseParams("Ваш формат: [1]см на [2]см я правильно вас понял?", Keyboards::spellOutFormatKeyBoard));
//        aboutPrintPhotos.put("@noRecognizeFormat", new PhaseParams("К сожалению я не смог понять какой Вам нужен формат", Keyboards::noRecognizeFormatKeyBoard));
//        aboutPrintPhotos.put("@moreThanOneError", new PhaseParams("Вы скинули [1] фотографий вместо одной. Пожалуйста, скиньте одну и напишите нужное количество", Keyboards::sizesKeyBoard));
//        aboutPrintPhotos.put("@warnNoCountHowMany", new PhaseParams("Вы не указали количество. Сколько нужно печатать таких фотографий?", null));
//        aboutPrintPhotos.put("@afterDownloadingByOne", new PhaseParams("Cкиньте еще или нажмите кнопку \"Готово\".", Keyboards::afterDownloadingKeyBoard));                                                                                 //repea)t
//        aboutPrintPhotos.put("@afterDownloadingByTen", new PhaseParams("Cкиньте еще или нажмите кнопку \"Готово\".", Keyboards::afterDownloadingKeyBoard));                                                                                 //repea)t
//        aboutPrintPhotos.put("@moments", new PhaseParams("Хорошо, прежде чем вы скинете фотки. Есть ли какие-нибудь моменты, о которых мы должны знать?", Keyboards::momentsKeyBoard));
//        aboutPrintPhotos.put("@priceDependsOnComment", new PhaseParams("Внимание, цена может поменяться в зависимости от вашего пожелания!\n" + "Напишите свой комментарий.", null));
//        aboutPrintPhotos.put("@ifUnclearNotify", new PhaseParams("Мы прочитаем и учтем ваши пожелания, если нам что-то будет непонятно мы отпишемся Вам.", null));
//        aboutPrintPhotos.put("@commentForPartOrAll", new PhaseParams("Эти пожелания касаются всех форматов, или только [1]?", Keyboards::commentForPartOrAllKeyBoard));
//        aboutPrintPhotos.put("@downloadByTen", new PhaseParams("Скиньте все свои фотографии (можно по 10 штук)." + "(Для сохранения качества фотографий лучше скидывайте их как \"документ\".)", Keyboards::downloadByTenKeyBoard));
//        aboutPrintPhotos.put("@nextFormat", new PhaseParams("Какой следующий формат?", Keyboards::formatsPhotoKeyBoard));
    }
    //ОБЩИЕ
    private static final Map<String, PhaseParams> common = new HashMap<>();
    static {
        common.put("@whatOrder", new PhaseParams("Что хотите заказать?", Keyboards::ordersMenu));
//        common.put("@Thanks", new PhaseParams("Спасибо! Заказ взят в работу, мы сразу же начнем его делать, как только до него дойдет очередь. По готовности отпишемся Вам здесь."));
//        common.put("@orderComplete", new PhaseParams("Ваш заказ готов, его можно забирать. Работаем ежедневно с 11 до 19 часов. Спасибо!"));
    }
    public static Phase aboutPrintPhotos(String phaseKey) {
        PhaseParams phaseParams = aboutPrintPhotos.get(phaseKey);
        Phase phase = func.apply(phaseParams.getPhrase(), phaseParams.getKeyboardSupplier());
        phase.setToSection("$print-photos");
        phase.setPhaseKey(phaseKey);
        return phase;
    }
    public static Phase common(String phaseKey){
        PhaseParams phaseParams = common.get(phaseKey);
        Phase phase = func.apply(phaseParams.getPhrase(), phaseParams.getKeyboardSupplier());
        phase.setToSection("$common");
        phase.setPhaseKey(phaseKey);
        return phase;
    }

    public static Phase getCurrentPhase(String to, String phaseKey){
        if(to.equals("$print-photos")){
            return aboutPrintPhotos(phaseKey);
        }
        if(to.equals("$photo-on-docs")){
            return null;
        }
        return null;
    }




}


