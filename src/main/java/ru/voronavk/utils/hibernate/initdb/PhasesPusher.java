package ru.voronavk.utils.hibernate.initdb;

import ru.voronavk.entities.PersonField;
import ru.voronavk.entities.Phase;
import ru.voronavk.utils.dialogs.statics.Keyboards;

import java.util.ArrayList;
import java.util.Collections;

public class PhasesPusher {
    public static void commonPhasesInit(){
        Phase phaseWhatFormat = new Phase();
        phaseWhatFormat.setToSection("$common");
        phaseWhatFormat.setPhaseKey("@whatOrder");
        phaseWhatFormat.setPhrase("Что хотите заказать?");
        phaseWhatFormat.setFilesWaitingCount(0);
        phaseWhatFormat.setKeyboardName("ordersMenu");
        phaseWhatFormat.setWaitForMessage(false);
        phaseWhatFormat.setCallbackOnMessage(null);
        phaseWhatFormat.setAdditionalCallbackOnBtn(null);
        Phase.save(phaseWhatFormat);
    }
    public static void printPhotoPhasesInit() {
        //$print-photos
        //Какой формат (whatFormat)
        PersonField personFieldWhatFormat = new PersonField();
        personFieldWhatFormat.setClassName("PrintPhotoOrder");
        personFieldWhatFormat.setSetter("setFormatPhoto");
        personFieldWhatFormat.setType("FormatPhoto");
        personFieldWhatFormat.setToChange(true);
        PersonField.save(personFieldWhatFormat);

        Phase phaseWhatFormat = new Phase();

        phaseWhatFormat.setToSection("$print-photos");
        phaseWhatFormat.setPhaseKey("@whatFormat");
        phaseWhatFormat.setPhrase("Какой формат?");
        phaseWhatFormat.setFilesWaitingCount(0);
        phaseWhatFormat.setWaitForMessage(false);
        phaseWhatFormat.setCallbackOnMessage(null);
        phaseWhatFormat.setKeyboardName("formatsPhotoKeyBoard");
        phaseWhatFormat.setAdditionalCallbackOnBtn(null);
        phaseWhatFormat.setPrevPhaseKey("@manyFormats");
        phaseWhatFormat.setNextPhaseKey("@papperType");
        phaseWhatFormat.setFields(Collections.singletonList(personFieldWhatFormat));
        Phase.save(phaseWhatFormat);

        //Какой формат (manyFormats)
        PersonField personFieldManyFormats = new PersonField();
        personFieldManyFormats.setClassName("PrintPhotoOrder");
        personFieldManyFormats.setSetter("setIsPartOfMultiPhotoOrder");
        personFieldManyFormats.setType("boolean");
        personFieldManyFormats.setToChange(true);
        PersonField.save(personFieldManyFormats);

        Phase phaseManyFormats = new Phase();
        phaseManyFormats.setToSection("$print-photos");
        phaseManyFormats.setPhaseKey("@manyFormats");
        phaseManyFormats.setPhrase("Все фотографии будут одного формата");
        phaseManyFormats.setFilesWaitingCount(0);
        phaseManyFormats.setWaitForMessage(false);
        phaseManyFormats.setCallbackOnMessage(null);
        phaseManyFormats.setKeyboardName("formatsPhotoKeyBoard");
        phaseManyFormats.setAdditionalCallbackOnBtn(null);
        phaseManyFormats.setPrevPhaseKey(null);
        phaseManyFormats.setNextPhaseKey("@whatFormat");
        phaseManyFormats.setFields(Collections.singletonList(personFieldManyFormats));
        Phase.save(phaseManyFormats);

        //Давайте начнем с первого. Какой будет первый формат? (firstFormat)
        PersonField personFieldFirstFormat = new PersonField();
        personFieldFirstFormat.setClassName("PrintPhotoOrder");
        personFieldFirstFormat.setSetter("setFormatPhoto");
        personFieldFirstFormat.setType("FormatPhoto");
        personFieldFirstFormat.setToChange(true);
        PersonField.save(personFieldFirstFormat);

        Phase phaseFirstFormat = new Phase();
        phaseFirstFormat.setToSection("$print-photos");
        phaseFirstFormat.setPhaseKey("@firstFormat");
        phaseFirstFormat.setPhrase("Давайте начнем с первого. Какой будет первый формат?");
        phaseFirstFormat.setFilesWaitingCount(0);
        phaseFirstFormat.setWaitForMessage(false);
        phaseFirstFormat.setKeyboardName("formatsPhotoKeyBoard");
        phaseFirstFormat.setCallbackOnMessage(null);
        phaseFirstFormat.setAdditionalCallbackOnBtn(null);
        phaseManyFormats.setPrevPhaseKey("@manyFormats");
        phaseManyFormats.setNextPhaseKey("@papperType");
        phaseFirstFormat.setFields(Collections.singletonList(personFieldFirstFormat));
        Phase.save(phaseFirstFormat);

        //К сожалению, такого формата фотографии мы не делаем (warnNoFormat)
        PersonField personFieldWarnNoFormat = new PersonField();
        personFieldWarnNoFormat.setClassName("PrintPhotoOrder");
        personFieldWarnNoFormat.setSetter("setFormatPhoto");
        personFieldWarnNoFormat.setType("FormatPhoto");
        personFieldWarnNoFormat.setToChange(true);
        PersonField.save(personFieldWarnNoFormat);

        Phase phaseWarnNoFormat = new Phase();
        phaseWarnNoFormat.setToSection("$print-photos");
        phaseWarnNoFormat.setPhaseKey("@warnNoFormat");
        phaseWarnNoFormat.setPhrase("К сожалению, формат [0] см мы не делаем(");
        phaseWarnNoFormat.setFilesWaitingCount(0);
        phaseWarnNoFormat.setWaitForMessage(true);
        phaseWarnNoFormat.setKeyboardName("warnNoFormatKeyBoard");
        phaseWarnNoFormat.setCallbackOnMessage(null);
        phaseWarnNoFormat.setAdditionalCallbackOnBtn(null);
        phaseWarnNoFormat.setError(true);
        phaseWarnNoFormat.setPrevPhaseKey("@whatFormat");
        phaseWarnNoFormat.setNextPhaseKey("@papperType");
        phaseWarnNoFormat.setFields(Collections.singletonList(personFieldWarnNoFormat));
        Phase.save(phaseWarnNoFormat);

        //Выберите тип бумаги @papperType"
        PersonField personFieldPapperType = new PersonField();
        personFieldPapperType.setClassName("PrintPhotoOrder");
        personFieldPapperType.setSetter("setPapperType");
        personFieldPapperType.setType("String");
        personFieldPapperType.setToChange(true);
        PersonField.save(personFieldPapperType);

        Phase phasePapperType = new Phase();
        phasePapperType.setToSection("$print-photos");
        phasePapperType.setPhaseKey("@papperType");
        phasePapperType.setPhrase("Выберите тип бумаги");
        phasePapperType.setFilesWaitingCount(0);
        phasePapperType.setWaitForMessage(false);
        phasePapperType.setKeyboardName("papperTypeKeyBoard");
        phasePapperType.setCallbackOnMessage(null);
        phasePapperType.setAdditionalCallbackOnBtn(null);
        phasePapperType.setPrevPhaseKey("@whatFormat");
        phasePapperType.setNextPhaseKey("@repeatPhotos");
        phasePapperType.setFields(Collections.singletonList(personFieldPapperType));
        Phase.save(phasePapperType);


        // Будут ли среди фотографий те, которые нужно отпечатать несколько раз? @repeatPhotos
        PersonField personFieldRepeatPhotos = new PersonField();
        personFieldRepeatPhotos.setClassName("PrintPhotoOrder");
        personFieldRepeatPhotos.setSetter("setHaveDifferentCountPhotos");
        personFieldRepeatPhotos.setType("boolean");
        personFieldRepeatPhotos.setToChange(true);
        PersonField.save(personFieldRepeatPhotos);

        Phase phaseRepeatPhotos = new Phase();
        phaseRepeatPhotos.setToSection("$print-photos");
        phaseRepeatPhotos.setPhaseKey("@repeatPhotos");
        phaseRepeatPhotos.setPhrase("Будут ли среди фотографий те, которые нужно отпечатать несколько раз?");
        phaseRepeatPhotos.setFilesWaitingCount(0);
        phaseRepeatPhotos.setWaitForMessage(false);
        phaseRepeatPhotos.setKeyboardName("repeatPhotosKeyBoard");
        phaseRepeatPhotos.setCallbackOnMessage(null);
        phaseRepeatPhotos.setAdditionalCallbackOnBtn(null);
        phaseRepeatPhotos.setPrevPhaseKey("@papperType");
        phaseRepeatPhotos.setNextPhaseKey("yes:@byOneWithCount, no:@sizes");
        phaseRepeatPhotos.setFields(Collections.singletonList(personFieldRepeatPhotos));
        Phase.save(phaseRepeatPhotos);


        //Пожалуйста, скиньте сначала их ПО ОДНОЙ, при этом напишите в сообщении к фотографии ее количество @byOneWithCount
        Phase phaseByOneWithCount = new Phase();
        phaseByOneWithCount.setToSection("$print-photos");
        phaseByOneWithCount.setPhaseKey("@byOneWithCount");
        phaseByOneWithCount.setPhrase("Пожалуйста, скиньте сначала их (по одной!), и напишите в сообщении количество данной фотографии");
        phaseByOneWithCount.setFilesWaitingCount(1);
        phaseByOneWithCount.setWaitForMessage(true);
        phaseByOneWithCount.setKeyboardName("byOneWithCountKeyBoard");
        phaseByOneWithCount.setCallbackOnMessage("assignFileOrCountToLastDifferentCountPhoto");
        phaseByOneWithCount.setAdditionalCallbackOnBtn(null);
        phaseByOneWithCount.setPrevPhaseKey("@repeatPhotos");
        phaseByOneWithCount.setNextPhaseKey("@afterDownloading");
        Phase.save(phaseByOneWithCount);


        //Введите размер (примерно так: \"14 на 18.7\" или так \"7,5x10\" @sizes
        Phase phaseSizes = new Phase();
        phaseSizes.setToSection("$print-photos");
        phaseSizes.setPhaseKey("@sizes");
        phaseSizes.setPhrase("Введите размер (примерно так: \"14 на 18.7\" или так \"7,5x10.5\"");
        phaseSizes.setFilesWaitingCount(0);
        phaseSizes.setWaitForMessage(true);
        phaseSizes.setKeyboardName("sizesKeyBoard");
        phaseSizes.setCallbackOnMessage("tryCastFormatPhoto");
        phaseSizes.setAdditionalCallbackOnBtn(null);
        phaseSizes.setPrevPhaseKey("@papperType");
        phaseSizes.setNextPhaseKey("@repeatPhotos");
        Phase.save(phaseSizes);


        // Ваш формат: [0]см на [1]см? я правильно вас понял? @spellOutFormat
        PersonField personFieldSpellOutFormat = new PersonField();
        personFieldSpellOutFormat.setClassName("PrintPhotoOrder");
        personFieldSpellOutFormat.setSetter("setHaveDifferentCountPhotos");
        personFieldSpellOutFormat.setType("boolean");
        personFieldSpellOutFormat.setToChange(true);
        PersonField.save(personFieldSpellOutFormat);

        Phase phaseSpellOutFormat = new Phase();
        phaseSpellOutFormat.setToSection("$print-photos");
        phaseSpellOutFormat.setPhaseKey("@spellOutFormat");
        phaseSpellOutFormat.setPhrase("Ваш формат: [0]см на [1]см? я правильно вас понял?");
        phaseSpellOutFormat.setFilesWaitingCount(0);
        phaseSpellOutFormat.setWaitForMessage(false);
        phaseSpellOutFormat.setCallbackOnMessage(null);
        phaseSpellOutFormat.setKeyboardName("spellOutFormatKeyBoard");
        phaseSpellOutFormat.setAdditionalCallbackOnBtn(null);
        phaseSpellOutFormat.setPrevPhaseKey("@whatFormat");
        phaseSpellOutFormat.setNextPhaseKey("yes:@byOneWithCount, no:@sizes");
        phaseSpellOutFormat.setFields(Collections.singletonList(personFieldSpellOutFormat));
        Phase.save(phaseSpellOutFormat);


        // К сожалению я не смог понять какой Вам нужен формат @noRecognizeFormat
        Phase phaseNoRecognizeFormat = new Phase();
        phaseNoRecognizeFormat.setToSection("$print-photos");
        phaseNoRecognizeFormat.setPhaseKey("@noRecognizeFormat");
        phaseNoRecognizeFormat.setPhrase("К сожалению я не смог понять какой Вам нужен формат");
        phaseNoRecognizeFormat.setFilesWaitingCount(0);
        phaseNoRecognizeFormat.setWaitForMessage(true);
        phaseNoRecognizeFormat.setKeyboardName("noRecognizeFormatKeyBoard");
        phaseNoRecognizeFormat.setCallbackOnMessage("tryCastFormatPhoto");
        phaseNoRecognizeFormat.setAdditionalCallbackOnBtn(null);
        phaseNoRecognizeFormat.setPrevPhaseKey("@whatFormat");
        phaseNoRecognizeFormat.setNextPhaseKey("@sizes");
        Phase.save(phaseNoRecognizeFormat);


        //@moreThanOneErrorByOne Вы скинули [0] фотографий вместо одной. Пожалуйста, скиньте одну и напишите нужное количество
        Phase phaseMoreThanOneErrorByOne = new Phase();
        phaseMoreThanOneErrorByOne.setToSection("$print-photos");
        phaseMoreThanOneErrorByOne.setPhaseKey("@moreThanOneErrorByOne");
        phaseMoreThanOneErrorByOne.setPhrase("Вы скинули [0] фотографий вместо одной. Пожалуйста, скиньте одну и напишите нужное количество");
        phaseMoreThanOneErrorByOne.setFilesWaitingCount(1);
        phaseMoreThanOneErrorByOne.setWaitForMessage(false);
        phaseMoreThanOneErrorByOne.setCallbackOnMessage("assignFileOrCountToLastDifferentCountPhoto");
        phaseMoreThanOneErrorByOne.setError(true);
        phaseMoreThanOneErrorByOne.setAdditionalCallbackOnBtn(null);
        phaseMoreThanOneErrorByOne.setPrevPhaseKey("@repeatPhotos");
        phaseMoreThanOneErrorByOne.setNextPhaseKey("@afterDownloadingByOne");
        Phase.save(phaseMoreThanOneErrorByOne);


        //Вы не указали количество. Сколько нужно печатать таких фотографий? @warnNoCountHowManyByOne
        Phase phaseWarnNoCountHowManyByOne = new Phase();
        phaseWarnNoCountHowManyByOne.setToSection("$print-photos");
        phaseWarnNoCountHowManyByOne.setPhaseKey("@warnNoCountHowManyByOne");
        phaseWarnNoCountHowManyByOne.setPhrase("Вы не указали количество. Сколько нужно печатать таких фотографий?");
        phaseWarnNoCountHowManyByOne.setFilesWaitingCount(0);
        phaseWarnNoCountHowManyByOne.setWaitForMessage(true);
        phaseWarnNoCountHowManyByOne.setCallbackOnMessage("assignFileOrCountToLastDifferentCountPhoto");
        phaseWarnNoCountHowManyByOne.setAdditionalCallbackOnBtn(null);
        phaseMoreThanOneErrorByOne.setPrevPhaseKey("@repeatPhotos");
        phaseMoreThanOneErrorByOne.setNextPhaseKey("@afterDownloadingByOne");
        Phase.save(phaseWarnNoCountHowManyByOne);

        // "скиньте еще или нажмите кнопку \"Готово\"." @afterDownloadingByOne
        Phase phaseAfterDownloadingByOne = new Phase();
        phaseAfterDownloadingByOne.setToSection("$print-photos");
        phaseAfterDownloadingByOne.setPhaseKey("@afterDownloading");
        phaseAfterDownloadingByOne.setPhrase("Cкиньте еще или нажмите кнопку \"Готово\".");
        phaseAfterDownloadingByOne.setFilesWaitingCount(1);
        phaseAfterDownloadingByOne.setWaitForMessage(false);
        phaseAfterDownloadingByOne.setKeyboardName("afterDownloadingKeyBoard");
        phaseAfterDownloadingByOne.setCallbackOnMessage("assignFileOrCountToLastDifferentCountPhoto");
        phaseAfterDownloadingByOne.setAdditionalCallbackOnBtn(null);
        Phase.save(phaseAfterDownloadingByOne);


        // "скиньте еще или нажмите кнопку \"Готово\"." @afterDownloadingByTen
        Phase phaseAfterDownloadingByTen = new Phase();
        phaseAfterDownloadingByTen.setToSection("$print-photos");
        phaseAfterDownloadingByTen.setPhaseKey("@afterDownloading");
        phaseAfterDownloadingByTen.setPhrase("Cкиньте еще или нажмите кнопку \"Готово\".");
        phaseAfterDownloadingByTen.setFilesWaitingCount(1);
        phaseAfterDownloadingByTen.setWaitForMessage(false);
        phaseAfterDownloadingByTen.setKeyboardName("afterDownloadingKeyBoard");
        phaseAfterDownloadingByTen.setCallbackOnMessage("assignFileOrCountToLastDifferentCountPhoto");
        phaseAfterDownloadingByTen.setAdditionalCallbackOnBtn(null);
        Phase.save(phaseAfterDownloadingByTen);


        // Хорошо, прежде чем вы скинете фотки. Есть ли какие-нибудь моменты, о которых мы должны знать @moments
        Phase phaseMoments = new Phase();
        phaseMoments.setToSection("$print-photos");
        phaseMoments.setPhaseKey("@moments");
        phaseMoments.setPhrase("Хорошо, прежде чем вы скинете фотки, есть ли какие-нибудь моменты, о которых мы должны знать?\n (Вы также можете прикрепить какой нибудь файл)");
        phaseMoments.setFilesWaitingCount(0);
        phaseMoments.setWaitForMessage(false);
        phaseMoments.setKeyboardName("momentsKeyBoard");
        phaseMoments.setCallbackOnMessage(null);
        phaseMoments.setAdditionalCallbackOnBtn(null);
        phaseMoments.setPrevPhaseKey("@whatFormat");
        phaseMoments.setNextPhaseKey("yes:@priceDependsOnComment, no:@sizes");
        Phase.save(phaseMoments);


        //@priceDependsOnComment  Внимание, цена может поменяться в зависимости от вашего пожелания!
        Phase phasePriceDependsOnComment = new Phase();
        phasePriceDependsOnComment.setToSection("$print-photos");
        phasePriceDependsOnComment.setPhaseKey("@priceDependsOnComment");
        phasePriceDependsOnComment.setPhrase("Внимание, может случится так, что цена может поменяться в зависимости от вашего пожелания, либо мы не сможем это сделать!\n В этих случаях мы вам отпишемся \n Напишите свой комментарий.");
        phasePriceDependsOnComment.setFilesWaitingCount(0);
        phasePriceDependsOnComment.setWaitForMessage(true);
        phasePriceDependsOnComment.setKeyboardName("priceDependsOnCommentKeyBoard");
        phasePriceDependsOnComment.setCallbackOnMessage("setTempComment");
        phasePriceDependsOnComment.setAdditionalCallbackOnBtn(null);
        phasePriceDependsOnComment.setPrevPhaseKey("@moments");
        phasePriceDependsOnComment.setNextPhaseKey("@commentForPartOrAll");
        Phase.save(phasePriceDependsOnComment);


        //@commentForPartOrAll  Эти пожелания касаются всех форматов, или только для этого формата?
        Phase phaseCommentForPartOrAll = new Phase();
        phaseCommentForPartOrAll.setToSection("$print-photos");
        phaseCommentForPartOrAll.setPhaseKey("@commentForPartOrAll");
        phaseCommentForPartOrAll.setPhrase("Эти пожелания касаются всех форматов, или только для этого формата?");
        phaseCommentForPartOrAll.setFilesWaitingCount(0);
        phaseCommentForPartOrAll.setWaitForMessage(false);
        phaseCommentForPartOrAll.setKeyboardName("commentForPartOrAllKeyBoard");
        phaseCommentForPartOrAll.setCallbackOnMessage("tempCommentToCurrentComment");
        phaseCommentForPartOrAll.setAdditionalCallbackOnBtn(null);
        phaseCommentForPartOrAll.setPrevPhaseKey("@moments");
        phaseCommentForPartOrAll.setNextPhaseKey("@ifUnclearNotify");
        Phase.save(phaseCommentForPartOrAll);


        //@downloadByTen "Скиньте все свои фотографии (можно по 10 штук). Для сохранения качества фотографий лучше скидывайте их как \"документ\".
        Phase phaseDownloadByTen = new Phase();
        phaseDownloadByTen.setToSection("$print-photos");
        phaseDownloadByTen.setPhaseKey("@downloadByTen");
        phaseDownloadByTen.setPhrase("Эти пожелания касаются всех форматов, или только для этого формата?");
        phaseDownloadByTen.setFilesWaitingCount(10);
        phaseDownloadByTen.setWaitForMessage(false);
        phaseDownloadByTen.setKeyboardName("downloadByTenKeyBoard");
        phaseDownloadByTen.setCallbackOnMessage("tempCommentToCurrentComment");
        phaseDownloadByTen.setAdditionalCallbackOnBtn(null);
        phaseDownloadByTen.setPrevPhaseKey("@moments");
        phaseDownloadByTen.setNextPhaseKey("@afterDownloadingByTen");
        Phase.save(phaseDownloadByTen);


        // "@ifUnclearNotify" "Мы прочитаем и учтем ваши пожелания, если нам что-то будет непонятно мы отпишемся Вам."
        Phase phaseIfUnclearNotify = new Phase();
        phaseIfUnclearNotify.setToSection("$print-photos");
        phaseIfUnclearNotify.setPhaseKey("@ifUnclearNotify");
        phaseIfUnclearNotify.setPhrase("Мы прочитаем и учтем ваши пожелания, если нам что-то будет непонятно мы отпишемся Вам");
        phaseIfUnclearNotify.setFilesWaitingCount(10);
        phaseIfUnclearNotify.setWaitForMessage(false);
        phaseIfUnclearNotify.setCallbackOnMessage("tempCommentToCurrentComment");
        phaseIfUnclearNotify.setAdditionalCallbackOnBtn(null);
        phaseIfUnclearNotify.setPrevPhaseKey("@moments");
        phaseIfUnclearNotify.setNextPhaseKey("@downloadByTen");
        Phase.save(phaseIfUnclearNotify);


        //@nextFormat Какой следующий формат?
        PersonField personFieldNextFormat = new PersonField();
        personFieldNextFormat.setClassName("PrintPhotoOrder");
        personFieldNextFormat.setSetter("setFormatPhoto");
        personFieldNextFormat.setType("FormatPhoto");
        personFieldNextFormat.setToChange(true);
        PersonField.save(personFieldNextFormat);

        Phase phaseNextFormat = new Phase();
        phaseNextFormat.setToSection("$print-photos");
        phaseNextFormat.setPhaseKey("@nextFormat");
        phaseNextFormat.setPhrase("Не удаляйте фотографии до выполнения заказа. Какой следующий формат?");
        phaseNextFormat.setFilesWaitingCount(0);
        phaseNextFormat.setWaitForMessage(false);
        phaseNextFormat.setKeyboardName("formatsPhotoKeyBoard");
        phaseNextFormat.setCallbackOnMessage(null);
        phaseNextFormat.setAdditionalCallbackOnBtn(null);
        phaseNextFormat.setPrevPhaseKey("@nextFormat");
        phaseNextFormat.setNextPhaseKey("papperType");
        phaseNextFormat.setFields(Collections.singletonList(personFieldNextFormat));
        Phase.save(phaseNextFormat);

    }
}
