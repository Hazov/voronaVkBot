package ru.voronavk.utils.dialogs.statics;

import api.longpoll.bots.model.objects.additional.buttons.Button;
import api.longpoll.bots.model.objects.additional.buttons.CallbackButton;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Buttons {

    public static List<Button> menuButtons() {
        List<Button> menuButtons = new ArrayList<>();
        //Меню
        JsonObject printPhotosPayload = new JsonObject();
        printPhotosPayload.addProperty("to", "$print-photos");
        printPhotosPayload.addProperty("phase", "@manyFormats");
        menuButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Печать фото", printPhotosPayload)));
        JsonObject photoOnDocsPayload = new JsonObject();
        photoOnDocsPayload.addProperty("to", "$photo-on-docs");
        photoOnDocsPayload.addProperty("phase", "----");
        menuButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Фото на документы", photoOnDocsPayload)));
        return menuButtons;
    }

    public static List<Button> formatsPhotoButtons() {
        List<Button> formatsPhotoButtons = new ArrayList<>();
        //Формат
        //10x15
        JsonObject formatPhotos1015Payload = new JsonObject();
        formatPhotos1015Payload.addProperty("to", "$print-photos");
        formatPhotos1015Payload.addProperty("phase", "@papperType");
        formatPhotos1015Payload.addProperty("answerField", "currentClass:formatPhoto");
        formatPhotos1015Payload.addProperty("answerValue", "10x15");
        formatsPhotoButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("10x15", formatPhotos1015Payload)));
        //15x21
        JsonObject formatPhotos1521Payload = new JsonObject();
        formatPhotos1521Payload.addProperty("to", "$print-photos");
        formatPhotos1521Payload.addProperty("phase", "@papperType");
        formatPhotos1521Payload.addProperty("answerField", "currentClass:formatPhoto");
        formatPhotos1521Payload.addProperty("answerValue", "15x21");
        formatsPhotoButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("15x21", formatPhotos1521Payload)));
        //A4
        JsonObject formatPhotosA4Payload = new JsonObject();
        formatPhotosA4Payload.addProperty("to", "$print-photos");
        formatPhotosA4Payload.addProperty("phase", "@papperType");
        formatPhotosA4Payload.addProperty("answerField", "currentClass:formatPhoto");
        formatPhotosA4Payload.addProperty("answerValue", "A4");
        formatsPhotoButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("A4", formatPhotosA4Payload)));
        //A3
        JsonObject formatPhotosA3Payload = new JsonObject();
        formatPhotosA3Payload.addProperty("to", "$print-photos");
        formatPhotosA3Payload.addProperty("phase", "@papperType");
        formatPhotosA3Payload.addProperty("answerField", "currentClass:formatPhoto");
        formatPhotosA3Payload.addProperty("answerValue", "A3");
        formatsPhotoButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("A3", formatPhotosA3Payload)));
        //Полароид
        JsonObject formatPhotosPolaroidPayload = new JsonObject();
        formatPhotosPolaroidPayload.addProperty("to", "$print-photos");
        formatPhotosPolaroidPayload.addProperty("phase", "@papperType");
        formatPhotosPolaroidPayload.addProperty("answerField", "currentClass:formatPhoto");
        formatPhotosPolaroidPayload.addProperty("answerValue", "Polaroid");
        formatsPhotoButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Полароид", formatPhotosPolaroidPayload)));
        //Другой
        JsonObject formatPhotosOtherPayload = new JsonObject();
        formatPhotosOtherPayload.addProperty("to", "$print-photos");
        formatPhotosOtherPayload.addProperty("phase", "@sizes");
        formatsPhotoButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Другой", formatPhotosOtherPayload)));
        return formatsPhotoButtons;
    }

    public static List<Button> manyFormatsButtons() {
        List<Button> manyFormatsButtons = new ArrayList<>();
        //ПЕЧАТЬ ФОТО
        //Все фотографии будут одного формата?
        //Да
        JsonObject manyFormatsYesPayload = new JsonObject();
        manyFormatsYesPayload.addProperty("to", "$print-photos");
        manyFormatsYesPayload.addProperty("phase", "@whatFormat");
        manyFormatsYesPayload.addProperty("answerField", "currentClass:partOfMultiPhotoOrder");
        manyFormatsYesPayload.addProperty("answerValue", "no");
        manyFormatsButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Да", manyFormatsYesPayload)));
        //Нет
        JsonObject manyFormatsNoPayload = new JsonObject();
        manyFormatsNoPayload.addProperty("to", "$print-photos");
        manyFormatsNoPayload.addProperty("phase", "@firstFormat");
        manyFormatsNoPayload.addProperty("answerField", "currentClass:partOfMultiPhotoOrder");
        manyFormatsNoPayload.addProperty("answerValue", "yes");
        manyFormatsButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Нет", manyFormatsNoPayload)));
        return manyFormatsButtons;
    }

    public static List<Button> papperTypeButtons() {

        List<Button> papperTypeButtons = new ArrayList<>();
        //Тип бумаги
        //Матовая
        JsonObject papperTypeMattePayload = new JsonObject();
        papperTypeMattePayload.addProperty("to", "$print-photos");
        papperTypeMattePayload.addProperty("phase", "@repeatPhotos");
        papperTypeMattePayload.addProperty("answerField", "currentClass:papperType");
        papperTypeMattePayload.addProperty("answerValue", "Matte");
        papperTypeButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Матовая (@currentUtil.calculatePriceBySizeAndTypeInt(person.getFormatOfLastPrintPhotoOrder,\"Matte\")@ р)", papperTypeMattePayload)));
        //Глянец
        JsonObject papperTypeGlossyPayload = new JsonObject();
        papperTypeGlossyPayload.addProperty("to", "$print-photos");
        papperTypeGlossyPayload.addProperty("phase", "@repeatPhotos");
        papperTypeGlossyPayload.addProperty("answerField", "currentClass:papperType");
        papperTypeGlossyPayload.addProperty("answerValue", "Glossy");
        papperTypeButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Глянец (@currentUtil.calculatePriceBySizeAndTypeInt(person.getFormatOfLastPrintPhotoOrder,\"Glossy\")@ р)", papperTypeGlossyPayload)));
        //Сатин
        JsonObject papperTypeSatinPayload = new JsonObject();
        papperTypeSatinPayload.addProperty("to", "$print-photos");
        papperTypeSatinPayload.addProperty("phase", "@repeatPhotos");
        papperTypeSatinPayload.addProperty("answerField", "currentClass:papperType");
        papperTypeSatinPayload.addProperty("answerValue", "Satin");
        papperTypeButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Сатин (@currentUtil.calculatePriceBySizeAndTypeInt(person.getFormatOfLastPrintPhotoOrder,\"Satin\")@ р)", papperTypeSatinPayload)));
        return papperTypeButtons;
    }

    public static List<Button> repeatPhotosButtons() {
        List<Button> repeatPhotosButtons = new ArrayList<>();
        //Будут ли среди фотографий те, которые нужно отпечатать несколько раз?
        //Да
        JsonObject repeatPhotosYesPayload = new JsonObject();
        repeatPhotosYesPayload.addProperty("to", "$print-photos");
        repeatPhotosYesPayload.addProperty("phase", "@byOneWithCount");
        repeatPhotosYesPayload.addProperty("answerField", "currentClass:haveDifferentCountPhotos");
        repeatPhotosYesPayload.addProperty("answerValue", "yes");
        repeatPhotosButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Да", repeatPhotosYesPayload)));
        //Нет
        JsonObject repeatPhotosNoPayload = new JsonObject();
        repeatPhotosNoPayload.addProperty("to", "$print-photos");
        repeatPhotosNoPayload.addProperty("phase", "@repeatPhotos");
        repeatPhotosNoPayload.addProperty("answerField", "currentClass:haveDifferentCountPhotos");
        repeatPhotosNoPayload.addProperty("answerValue", "no");
        repeatPhotosButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Нет", repeatPhotosNoPayload)));
        return repeatPhotosButtons;
    }

    public static List<Button> warnNoFormatButtons() {
        List<Button> warnNoFormatButtons = new ArrayList<>();
        //Нет такого типа бумаги
        //Ввести другой (или ждем Input)
        JsonObject warnNoFormatRepeatButtons = new JsonObject();
        warnNoFormatRepeatButtons.addProperty("to", "$print-photos");
        warnNoFormatRepeatButtons.addProperty("phase", "@sizes");
        warnNoFormatButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Ввести другой", warnNoFormatRepeatButtons)));
        //Выбрать из стандартных
        JsonObject warnNoFormatStandardFormatsButtons = new JsonObject();
        warnNoFormatStandardFormatsButtons.addProperty("to", "$print-photos");
        warnNoFormatStandardFormatsButtons.addProperty("phase", "@standardFormats");
        warnNoFormatButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Выбрать из стандартных", warnNoFormatStandardFormatsButtons)));
        return warnNoFormatButtons;
    }

    public static List<Button> byOneWithCountButtons() {
        List<Button> byOneWithCountButtons = new ArrayList<>();
        //TODO отсюда начинаем
        //Скиньте их по одной
        //Я передумал
        JsonObject byOneWithCountNoMatterPayload = new JsonObject();
        byOneWithCountNoMatterPayload.addProperty("to", "$print-photos");
        byOneWithCountNoMatterPayload.addProperty("phase", "@moments");
        byOneWithCountNoMatterPayload.addProperty("answerField", "currentClass:haveDifferentCountPhotos");
        byOneWithCountNoMatterPayload.addProperty("answerValue", "false");
        byOneWithCountButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Я передумал(а)", byOneWithCountNoMatterPayload)));
        //Готово
        JsonObject byOneWithCountCompletePayload = new JsonObject();
        byOneWithCountCompletePayload.addProperty("to", "$print-photos");
        byOneWithCountCompletePayload.addProperty("phase", "@moments");
        byOneWithCountCompletePayload.addProperty("answerField", "");
        byOneWithCountCompletePayload.addProperty("answerValue", "");
        byOneWithCountButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Готово", byOneWithCountCompletePayload)));
        return byOneWithCountButtons;
    }

    public static List<Button> sizesKeyBoardButtons() {
        List<Button> sizesKeyBoardButtons = new ArrayList<>();
        return sizesKeyBoardButtons;
    }

    public static List<Button> spellOutFormatButtons() {
        List<Button> spellOutFormatButtons = new ArrayList<>();
        //Ваш формат: [1]см на [2]см я правильно вас понял
        //Ввести еще раз
        JsonObject spellOutFormatRepeatPayload = new JsonObject();
        spellOutFormatRepeatPayload.addProperty("to", "$print-photos");
        spellOutFormatRepeatPayload.addProperty("phase", "@sizes");
        spellOutFormatRepeatPayload.addProperty("answerField", "currentClass:haveDifferentCountPhotos");
        spellOutFormatRepeatPayload.addProperty("answerValue", "false");
        spellOutFormatButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Готово", spellOutFormatRepeatPayload)));
        //Да
        JsonObject spellOutFormatYesPayload = new JsonObject();
        spellOutFormatYesPayload.addProperty("to", "$print-photos");
        spellOutFormatYesPayload.addProperty("phase", "@moments");
        spellOutFormatYesPayload.addProperty("answerField", "currentClass:haveDifferentCountPhotos");
        spellOutFormatYesPayload.addProperty("answerValue", "false");
        spellOutFormatButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Готово", spellOutFormatYesPayload)));
        return spellOutFormatButtons;
    }

    public static List<Button> noRecognizeFormatButtons() {
        List<Button> noRecognizeFormatButtons = new ArrayList<>();
        //Я не смог понять какой Вам нужен формат
        //Повторить
        JsonObject noRecognizeFormatRepeatPayload = new JsonObject();
        noRecognizeFormatRepeatPayload.addProperty("to", "$print-photos");
        noRecognizeFormatRepeatPayload.addProperty("phase", "@moments");
        noRecognizeFormatRepeatPayload.addProperty("answerField", "currentClass:haveDifferentCountPhotos");
        noRecognizeFormatRepeatPayload.addProperty("answerValue", "false");
        noRecognizeFormatButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Готово", noRecognizeFormatRepeatPayload)));
        //Выбрать из стандартных
        JsonObject noRecognizeFormatStandardFormatsPayload = new JsonObject();
        noRecognizeFormatRepeatPayload.addProperty("to", "$print-photos");
        noRecognizeFormatRepeatPayload.addProperty("phase", "@moments");
        noRecognizeFormatRepeatPayload.addProperty("answerField", "currentClass:haveDifferentCountPhotos");
        noRecognizeFormatRepeatPayload.addProperty("answerValue", "false");
        noRecognizeFormatButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Готово", noRecognizeFormatRepeatPayload)));

        //Есть какие-то моменты
        //Да
        JsonObject momentsYesPayload = new JsonObject();
        momentsYesPayload.addProperty("to", "$print-photos");
        momentsYesPayload.addProperty("phase", "@moments");
        momentsYesPayload.addProperty("answerField", "currentClass:haveDifferentCountPhotos");
        momentsYesPayload.addProperty("answerValue", "false");
        noRecognizeFormatButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Готово", momentsYesPayload)));
        //Нет
        JsonObject momentsNoPayload = new JsonObject();
        momentsNoPayload.addProperty("to", "$print-photos");
        momentsNoPayload.addProperty("phase", "@moments");
        momentsNoPayload.addProperty("answerField", "currentClass:haveDifferentCountPhotos");
        momentsNoPayload.addProperty("answerValue", "false");
        noRecognizeFormatButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Готово", momentsNoPayload)));
        return noRecognizeFormatButtons;
    }

    public static List<Button> momentsButtons() {
        List<Button> momentsButtons = new ArrayList<>();
        return momentsButtons;
    }

    public static List<Button> priceDependsOnCommentButtons() {
        List<Button> priceDependsOnCommentButtons = new ArrayList<>();
        return priceDependsOnCommentButtons;
    }

    public static List<Button> commentForPartOrAllButtons() {
        List<Button> commentForPartOrAllButtons = new ArrayList<>();
        //Этот комментарий к части заказа?
        //Ко всему заказу
        JsonObject commentForPartOrAllForAllPayload = new JsonObject();
        commentForPartOrAllForAllPayload.addProperty("to", "$print-photos");
        commentForPartOrAllForAllPayload.addProperty("phase", "@moments");
        commentForPartOrAllForAllPayload.addProperty("answerField", "currentClass:haveDifferentCountPhotos");
        commentForPartOrAllForAllPayload.addProperty("answerValue", "false");
        commentForPartOrAllButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Готово", commentForPartOrAllForAllPayload)));
        //Только для этого формата
        JsonObject commentForPartOrAllForPartPayload = new JsonObject();
        commentForPartOrAllForPartPayload.addProperty("to", "$print-photos");
        commentForPartOrAllForPartPayload.addProperty("phase", "@moments");
        commentForPartOrAllForPartPayload.addProperty("answerField", "currentClass:haveDifferentCountPhotos");
        commentForPartOrAllForPartPayload.addProperty("answerValue", "false");
        commentForPartOrAllButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Готово", commentForPartOrAllForPartPayload)));
        return commentForPartOrAllButtons;
    }

    public static List<Button> downloadByTenButtons() {
        List<Button> downloadByTenButtons = new ArrayList<>();
        //Загружайте по 10
        //Завершить
        JsonObject downloadByTenComplatePayload = new JsonObject();
        downloadByTenComplatePayload.addProperty("to", "$print-photos");
        downloadByTenComplatePayload.addProperty("phase", "@moments");
        downloadByTenComplatePayload.addProperty("answerField", "currentClass:haveDifferentCountPhotos");
        downloadByTenComplatePayload.addProperty("answerValue", "false");
        downloadByTenButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Завершить", downloadByTenComplatePayload)));
        return downloadByTenButtons;
    }
    public static List<Button> afterDownloadingButtons() {
        List<Button> afterDownloadingButtons = new ArrayList<>();
        //Вы загрузили [1] фотографий, киньте еще или нажмите Завершить
        JsonObject afterDownloadingPayload = new JsonObject();
        afterDownloadingPayload.addProperty("to", "$print-photos");
        afterDownloadingPayload.addProperty("phase", "@moments");
        afterDownloadingPayload.addProperty("answerField", "currentClass:haveDifferentCountPhotos");
        afterDownloadingPayload.addProperty("answerValue", "false");
        afterDownloadingButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Готово", afterDownloadingPayload)));
        return afterDownloadingButtons;
    }

    public static List<Button> sizesButtons() {
        List<Button> afterDownloadingButtons = new ArrayList<>();
        return afterDownloadingButtons;
    }
}
