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
        printPhotosPayload.addProperty("answer", "printPhotos");
        menuButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Печать фото", printPhotosPayload)));
        JsonObject photoOnDocsPayload = new JsonObject();
        photoOnDocsPayload.addProperty("answer", "photoOnDocs");
        menuButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Фото на документы", photoOnDocsPayload)));
        return menuButtons;
    }

    public static List<Button> formatsPhotoButtons() {
        List<Button> formatsPhotoButtons = new ArrayList<>();
        //Формат
        //10x15
        JsonObject formatPhotos1015Payload = new JsonObject();
        formatPhotos1015Payload.addProperty("answer", "10x15");
        formatsPhotoButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("10x15", formatPhotos1015Payload)));
        //15x21
        JsonObject formatPhotos1521Payload = new JsonObject();
        formatPhotos1521Payload.addProperty("answer", "15x21");
        formatsPhotoButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("15x21", formatPhotos1521Payload)));
        //A4
        JsonObject formatPhotosA4Payload = new JsonObject();
        formatPhotosA4Payload.addProperty("answer", "A4");
        formatsPhotoButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("A4", formatPhotosA4Payload)));
        //A3
        JsonObject formatPhotosA3Payload = new JsonObject();
        formatPhotosA3Payload.addProperty("answer", "A3");
        formatsPhotoButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("A3", formatPhotosA3Payload)));
        //Полароид
        JsonObject formatPhotosPolaroidPayload = new JsonObject();
        formatPhotosPolaroidPayload.addProperty("answer", "Polaroid");
        formatsPhotoButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Полароид", formatPhotosPolaroidPayload)));
        //Другой
        JsonObject formatPhotosOtherPayload = new JsonObject();
        formatPhotosPolaroidPayload.addProperty("answer", "other");
        formatsPhotoButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Другой", formatPhotosOtherPayload)));
        return formatsPhotoButtons;
    }

    public static List<Button> manyFormatsButtons() {
        List<Button> manyFormatsButtons = new ArrayList<>();
        //ПЕЧАТЬ ФОТО
        //Все фотографии будут одного формата?
        //Да
        JsonObject manyFormatsYesPayload = new JsonObject();
        manyFormatsYesPayload.addProperty("answer", "no");
        manyFormatsButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Да", manyFormatsYesPayload)));
        //Нет
        JsonObject manyFormatsNoPayload = new JsonObject();
        manyFormatsNoPayload.addProperty("answer", "yes");
        manyFormatsButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Нет", manyFormatsNoPayload)));
        return manyFormatsButtons;
    }

    public static List<Button> papperTypeButtons() {

        List<Button> papperTypeButtons = new ArrayList<>();
        //Тип бумаги
        //Матовая
        JsonObject papperTypeMattePayload = new JsonObject();
        papperTypeMattePayload.addProperty("answer", "Matte");
        papperTypeButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Матовая (@currentUtil.calculatePriceBySizeAndTypeInt(person.getFormatOfLastPrintPhotoOrder,\"Matte\")@ р)", papperTypeMattePayload)));
        //Глянец
        JsonObject papperTypeGlossyPayload = new JsonObject();
        papperTypeGlossyPayload.addProperty("answer", "Glossy");
        papperTypeButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Глянец (@currentUtil.calculatePriceBySizeAndTypeInt(person.getFormatOfLastPrintPhotoOrder,\"Glossy\")@ р)", papperTypeGlossyPayload)));
        //Сатин
        JsonObject papperTypeSatinPayload = new JsonObject();
        papperTypeSatinPayload.addProperty("answer", "Satin");
        papperTypeButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Сатин (@currentUtil.calculatePriceBySizeAndTypeInt(person.getFormatOfLastPrintPhotoOrder,\"Satin\")@ р)", papperTypeSatinPayload)));
        return papperTypeButtons;
    }

    public static List<Button> repeatPhotosButtons() {
        List<Button> repeatPhotosButtons = new ArrayList<>();
        //Будут ли среди фотографий те, которые нужно отпечатать несколько раз?
        //Да
        JsonObject repeatPhotosYesPayload = new JsonObject();
        repeatPhotosYesPayload.addProperty("answer", "yes");
        repeatPhotosButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Да", repeatPhotosYesPayload)));
        //Нет
        JsonObject repeatPhotosNoPayload = new JsonObject();
        repeatPhotosNoPayload.addProperty("answer", "no");
        repeatPhotosButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Нет", repeatPhotosNoPayload)));
        return repeatPhotosButtons;
    }

    public static List<Button> warnNoFormatButtons() {
        List<Button> warnNoFormatButtons = new ArrayList<>();
        //Нет такого типа бумаги
        //Ввести другой (или ждем Input)
        JsonObject warnNoFormatRepeatButtons = new JsonObject();
        warnNoFormatButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Ввести другой", warnNoFormatRepeatButtons)));
        //Выбрать из стандартных
        JsonObject warnNoFormatStandardFormatsButtons = new JsonObject();
        warnNoFormatButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Выбрать из стандартных", warnNoFormatStandardFormatsButtons)));
        return warnNoFormatButtons;
    }

    public static List<Button> byOneWithCountButtons() {
        List<Button> byOneWithCountButtons = new ArrayList<>();
        //TODO отсюда начинаем
        //Скиньте их по одной
        //Я передумал
        JsonObject byOneWithCountNoMatterPayload = new JsonObject();
        byOneWithCountNoMatterPayload.addProperty("answer", "no");
        byOneWithCountButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Я передумал(а)", byOneWithCountNoMatterPayload)));
        //Готово
        JsonObject byOneWithCountCompletePayload = new JsonObject();
        byOneWithCountCompletePayload.addProperty("answer", "");
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
        spellOutFormatRepeatPayload.addProperty("answer", "no");
        spellOutFormatButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Готово", spellOutFormatRepeatPayload)));
        //Да
        JsonObject spellOutFormatYesPayload = new JsonObject();
        spellOutFormatYesPayload.addProperty("answer", "no");
        spellOutFormatButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Готово", spellOutFormatYesPayload)));
        return spellOutFormatButtons;
    }

    public static List<Button> noRecognizeFormatButtons() {
        List<Button> noRecognizeFormatButtons = new ArrayList<>();
        //Я не смог понять какой Вам нужен формат
        //Повторить
        JsonObject noRecognizeFormatRepeatPayload = new JsonObject();
        noRecognizeFormatRepeatPayload.addProperty("answer", "no");
        noRecognizeFormatButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Готово", noRecognizeFormatRepeatPayload)));
        //Выбрать из стандартных
        JsonObject noRecognizeFormatStandardFormatsPayload = new JsonObject();
        noRecognizeFormatRepeatPayload.addProperty("answer", "no");
        noRecognizeFormatButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Готово", noRecognizeFormatRepeatPayload)));

        //Есть какие-то моменты
        //Да
        JsonObject momentsYesPayload = new JsonObject();
        momentsYesPayload.addProperty("answer", "no");
        noRecognizeFormatButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Готово", momentsYesPayload)));
        //Нет
        JsonObject momentsNoPayload = new JsonObject();
        momentsNoPayload.addProperty("answer", "no");
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
        commentForPartOrAllForAllPayload.addProperty("answer", "no");
        commentForPartOrAllButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Готово", commentForPartOrAllForAllPayload)));
        //Только для этого формата
        JsonObject commentForPartOrAllForPartPayload = new JsonObject();
        commentForPartOrAllForPartPayload.addProperty("answer", "no");
        commentForPartOrAllButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Готово", commentForPartOrAllForPartPayload)));
        return commentForPartOrAllButtons;
    }

    public static List<Button> downloadByTenButtons() {
        List<Button> downloadByTenButtons = new ArrayList<>();
        //Завершить
        JsonObject downloadByTenComplatePayload = new JsonObject();
        downloadByTenComplatePayload.addProperty("answer", "no");
        downloadByTenButtons.add(new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Завершить", downloadByTenComplatePayload)));
        return downloadByTenButtons;
    }
    public static List<Button> afterDownloadingButtons() {
        List<Button> afterDownloadingButtons = new ArrayList<>();
        //Вы загрузили [1] фотографий, киньте еще или нажмите Завершить
        JsonObject afterDownloadingPayload = new JsonObject();
        afterDownloadingPayload.addProperty("answer", "done");
        afterDownloadingButtons.add(new CallbackButton(Button.Color.SECONDARY, new CallbackButton.Action("Завершить", afterDownloadingPayload)));
        return afterDownloadingButtons;
    }

    public static List<Button> sizesButtons() {
        List<Button> afterDownloadingButtons = new ArrayList<>();
        return afterDownloadingButtons;
    }

    public static List<Button> specificationCountButtons() {
        List<Button> specificationCountButtons = new ArrayList<>();
        JsonObject specificationCountYesPayload = new JsonObject();
        specificationCountYesPayload.addProperty("answer", "yes");
        specificationCountButtons.add(new CallbackButton(Button.Color.POSITIVE, new CallbackButton.Action("Да", specificationCountYesPayload)));
        JsonObject specificationCountNoPayload = new JsonObject();
        specificationCountNoPayload.addProperty("answer", "no");
        specificationCountButtons.add(new CallbackButton(Button.Color.NEGATIVE, new CallbackButton.Action("Нет", specificationCountNoPayload)));
        return specificationCountButtons;
    }
}
