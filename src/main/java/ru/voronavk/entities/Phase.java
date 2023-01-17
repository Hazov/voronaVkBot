package ru.voronavk.entities;

import api.longpoll.bots.model.objects.additional.Keyboard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.voronavk.diao.PhaseParams;
import ru.voronavk.utils.annotations.ForApi;
import ru.voronavk.utils.dialogs.statics.Keyboards;
import ru.voronavk.utils.hibernate.Hiber;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Phase {
    @Id
    @GeneratedValue
    Integer id;
    @Column(name = "phrase")
    String phrase;
    @Transient
    Keyboard answerKeyBoard;
    @Column
    String toSection;
    @Column
    String phaseKey;
    @Column
    int filesWaitingCount;
    @Column
    boolean waitForMessage;
    @Column
    String callbackOnMessage;
    @Column
    String additionalCallbackOnBtn;
    //Поля для проверки, которые должны присутствовать, чтобы перейти на эту фазу
    // Поля, которые должны были быть получены на предыдущих фазах. Есть автопроверка
    @ManyToMany
    List<PersonField> fieldsToCheck;
    //Поля к изменению. Можно не писать, если изменением управляет callbackOnMessage или additionalCallbackOnBtn
    @ManyToMany
    List<PersonField> fieldsToChange;
    @Column
    String prevPhaseKey;
    @Column
    String nextPhaseKey;
    @Column
    boolean isError;

    {
        //$print-photos
        //Какой формат (whatFormat)
        Phase phaseWhatFormat = new Phase();
        phaseWhatFormat.setToSection("$print-photos");
        phaseWhatFormat.setPhaseKey("@whatFormat");
        phaseWhatFormat.setPhrase("Какой формат?");
        phaseWhatFormat.setFilesWaitingCount(0);
        phaseWhatFormat.setWaitForMessage(false);
        phaseWhatFormat.setCallbackOnMessage(null);
        phaseWhatFormat.setAdditionalCallbackOnBtn(null);
        phaseWhatFormat.setPrevPhaseKey("@manyFormats");
        phaseWhatFormat.setNextPhaseKey("@papperType");

        PersonField personFieldWhatFormat = new PersonField();
        personFieldWhatFormat.setClassName("PrintPhotoOrder");
        personFieldWhatFormat.setSetter("setFormatPhoto");
        personFieldWhatFormat.setType("FormatPhoto");
        phaseWhatFormat.setFieldsToChange(Collections.singletonList(personFieldWhatFormat));

        //Какой формат (manyFormats)
        Phase phaseManyFormats = new Phase();
        phaseManyFormats.setToSection("$print-photos");
        phaseManyFormats.setPhaseKey("@manyFormats");
        phaseManyFormats.setPhrase("Все фотографии будут одного формата");
        phaseManyFormats.setFilesWaitingCount(0);
        phaseManyFormats.setWaitForMessage(false);
        phaseManyFormats.setCallbackOnMessage(null);
        phaseManyFormats.setAdditionalCallbackOnBtn(null);
        phaseManyFormats.setPrevPhaseKey(null);
        phaseManyFormats.setNextPhaseKey("@whatFormat");

        PersonField personFieldManyFormats = new PersonField();
        personFieldManyFormats.setClassName("PrintPhotoOrder");
        personFieldManyFormats.setSetter("setIsPartOfMultiPhotoOrder");
        personFieldManyFormats.setType("boolean");
        phaseManyFormats.setFieldsToChange(Collections.singletonList(personFieldManyFormats));

        //Давайте начнем с первого. Какой будет первый формат? (firstFormat)
        Phase phaseFirstFormat = new Phase();
        phaseFirstFormat.setToSection("$print-photos");
        phaseFirstFormat.setPhaseKey("@firstFormat");
        phaseFirstFormat.setPhrase("Давайте начнем с первого. Какой будет первый формат?");
        phaseFirstFormat.setFilesWaitingCount(0);
        phaseFirstFormat.setWaitForMessage(false);
        phaseFirstFormat.setCallbackOnMessage(null);
        phaseFirstFormat.setAdditionalCallbackOnBtn(null);
        phaseManyFormats.setPrevPhaseKey("@manyFormats");
        phaseManyFormats.setNextPhaseKey("@papperType");

        PersonField personFieldFirstFormat = new PersonField();
        personFieldFirstFormat.setClassName("PrintPhotoOrder");
        personFieldFirstFormat.setSetter("setFormatPhoto");
        personFieldFirstFormat.setType("FormatPhoto");
        phaseFirstFormat.setFieldsToChange(Collections.singletonList(personFieldFirstFormat));

        //К сожалению, такого формата фотографии мы не делаем (warnNoFormat)
        Phase phaseWarnNoFormat = new Phase();
        phaseWarnNoFormat.setToSection("$print-photos");
        phaseWarnNoFormat.setPhaseKey("@warnNoFormat");
        phaseWarnNoFormat.setPhrase("К сожалению, формат [0] см мы не делаем(");
        phaseWarnNoFormat.setFilesWaitingCount(0);
        phaseWarnNoFormat.setWaitForMessage(true);
        phaseWarnNoFormat.setCallbackOnMessage(null);
        phaseWarnNoFormat.setAdditionalCallbackOnBtn(null);
        phaseWarnNoFormat.setError(true);
        phaseWarnNoFormat.setPrevPhaseKey("@whatFormat");
        phaseWarnNoFormat.setNextPhaseKey("@papperType");

        PersonField personFieldWarnNoFormat = new PersonField();
        personFieldWarnNoFormat.setClassName("PrintPhotoOrder");
        personFieldWarnNoFormat.setSetter("setFormatPhoto");
        personFieldWarnNoFormat.setType("FormatPhoto");
        phaseWarnNoFormat.setFieldsToChange(Collections.singletonList(personFieldWarnNoFormat));

        //Выберите тип бумаги @papperType"
        Phase phasePapperType = new Phase();
        phasePapperType.setToSection("$print-photos");
        phasePapperType.setPhaseKey("@papperType");
        phasePapperType.setPhrase("Выберите тип бумаги");
        phasePapperType.setFilesWaitingCount(0);
        phasePapperType.setWaitForMessage(false);
        phasePapperType.setCallbackOnMessage(null);
        phasePapperType.setAdditionalCallbackOnBtn(null);
        phasePapperType.setPrevPhaseKey("@whatFormat");
        phasePapperType.setNextPhaseKey("@repeatPhotos");

        PersonField personFieldPapperType = new PersonField();
        personFieldPapperType.setClassName("PrintPhotoOrder");
        personFieldPapperType.setSetter("setPapperType");
        personFieldPapperType.setType("String");
        phasePapperType.setFieldsToChange(Collections.singletonList(personFieldPapperType));


        // Будут ли среди фотографий те, которые нужно отпечатать несколько раз? @repeatPhotos
        Phase phaseRepeatPhotos = new Phase();
        phaseRepeatPhotos.setToSection("$print-photos");
        phaseRepeatPhotos.setPhaseKey("@repeatPhotos");
        phaseRepeatPhotos.setPhrase("Будут ли среди фотографий те, которые нужно отпечатать несколько раз?");
        phaseRepeatPhotos.setFilesWaitingCount(0);
        phaseRepeatPhotos.setWaitForMessage(false);
        phaseRepeatPhotos.setCallbackOnMessage(null);
        phaseRepeatPhotos.setAdditionalCallbackOnBtn(null);
        phaseRepeatPhotos.setPrevPhaseKey("@papperType");
        phaseRepeatPhotos.setNextPhaseKey("yes:@byOneWithCount, no:@sizes");

        PersonField personFieldRepeatPhotos = new PersonField();
        personFieldRepeatPhotos.setClassName("PrintPhotoOrder");
        personFieldRepeatPhotos.setSetter("setHaveDifferentCountPhotos");
        personFieldRepeatPhotos.setType("boolean");
        phaseRepeatPhotos.setFieldsToChange(Collections.singletonList(personFieldRepeatPhotos));


        //Пожалуйста, скиньте сначала их ПО ОДНОЙ, при этом напишите в сообщении к фотографии ее количество @byOneWithCount
        Phase phaseByOneWithCount = new Phase();
        phaseByOneWithCount.setToSection("$print-photos");
        phaseByOneWithCount.setPhaseKey("@byOneWithCount");
        phaseByOneWithCount.setPhrase("Пожалуйста, скиньте сначала их (по одной!), и напишите в сообщении количество данной фотографии");
        phaseByOneWithCount.setFilesWaitingCount(1);
        phaseByOneWithCount.setWaitForMessage(true);
        phaseByOneWithCount.setCallbackOnMessage("assignFileOrCountToLastDifferentCountPhoto");
        phaseByOneWithCount.setAdditionalCallbackOnBtn(null);
        phaseByOneWithCount.setPrevPhaseKey("@repeatPhotos");
        phaseByOneWithCount.setNextPhaseKey("@afterDownloading");


        //Введите размер (примерно так: \"14 на 18.7\" или так \"7,5x10\" @sizes
        Phase phaseSizes = new Phase();
        phaseSizes.setToSection("$print-photos");
        phaseSizes.setPhaseKey("@sizes");
        phaseSizes.setPhrase("Введите размер (примерно так: \"14 на 18.7\" или так \"7,5x10.5\"");
        phaseSizes.setFilesWaitingCount(0);
        phaseSizes.setWaitForMessage(true);
        phaseSizes.setCallbackOnMessage("tryCastFormatPhoto");
        phaseSizes.setAdditionalCallbackOnBtn(null);
        phaseSizes.setPrevPhaseKey("@papperType");
        phaseSizes.setNextPhaseKey("@repeatPhotos");


        // Ваш формат: [0]см на [1]см? я правильно вас понял? @spellOutFormat
        Phase phaseSpellOutFormat = new Phase();
        phaseSpellOutFormat.setToSection("$print-photos");
        phaseSpellOutFormat.setPhaseKey("@spellOutFormat");
        phaseSpellOutFormat.setPhrase("Ваш формат: [0]см на [1]см? я правильно вас понял?");
        phaseSpellOutFormat.setFilesWaitingCount(0);
        phaseSpellOutFormat.setWaitForMessage(false);
        phaseSpellOutFormat.setCallbackOnMessage(null);
        phaseSpellOutFormat.setAdditionalCallbackOnBtn(null);
        phaseSpellOutFormat.setPrevPhaseKey("@whatFormat");
        phaseSpellOutFormat.setNextPhaseKey("yes:@byOneWithCount, no:@sizes");

        PersonField personFieldSpellOutFormat = new PersonField();
        personFieldSpellOutFormat.setClassName("PrintPhotoOrder");
        personFieldSpellOutFormat.setSetter("setHaveDifferentCountPhotos");
        personFieldSpellOutFormat.setType("boolean");
        phaseSpellOutFormat.setFieldsToChange(Collections.singletonList(personFieldSpellOutFormat));


        // К сожалению я не смог понять какой Вам нужен формат @noRecognizeFormat
        Phase phaseNoRecognizeFormat = new Phase();
        phaseNoRecognizeFormat.setToSection("$print-photos");
        phaseNoRecognizeFormat.setPhaseKey("@noRecognizeFormat");
        phaseNoRecognizeFormat.setPhrase("К сожалению я не смог понять какой Вам нужен формат");
        phaseNoRecognizeFormat.setFilesWaitingCount(0);
        phaseNoRecognizeFormat.setWaitForMessage(true);
        phaseNoRecognizeFormat.setCallbackOnMessage("tryCastFormatPhoto");
        phaseNoRecognizeFormat.setAdditionalCallbackOnBtn(null);
        phaseNoRecognizeFormat.setPrevPhaseKey("@whatFormat");
        phaseNoRecognizeFormat.setNextPhaseKey("@sizes");


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

        // "скиньте еще или нажмите кнопку \"Готово\"." @afterDownloadingByOne
        Phase phaseAfterDownloadingByOne = new Phase();
        phaseAfterDownloadingByOne.setToSection("$print-photos");
        phaseAfterDownloadingByOne.setPhaseKey("@afterDownloading");
        phaseAfterDownloadingByOne.setPhrase("Cкиньте еще или нажмите кнопку \"Готово\".");
        phaseAfterDownloadingByOne.setFilesWaitingCount(1);
        phaseAfterDownloadingByOne.setWaitForMessage(false);
        phaseAfterDownloadingByOne.setCallbackOnMessage("assignFileOrCountToLastDifferentCountPhoto");
        phaseAfterDownloadingByOne.setAdditionalCallbackOnBtn(null);


        // "скиньте еще или нажмите кнопку \"Готово\"." @afterDownloadingByTen
        Phase phaseAfterDownloadingByTen = new Phase();
        phaseAfterDownloadingByTen.setToSection("$print-photos");
        phaseAfterDownloadingByTen.setPhaseKey("@afterDownloading");
        phaseAfterDownloadingByTen.setPhrase("Cкиньте еще или нажмите кнопку \"Готово\".");
        phaseAfterDownloadingByTen.setFilesWaitingCount(1);
        phaseAfterDownloadingByTen.setWaitForMessage(false);
        phaseAfterDownloadingByTen.setCallbackOnMessage("assignFileOrCountToLastDifferentCountPhoto");
        phaseAfterDownloadingByTen.setAdditionalCallbackOnBtn(null);


        // Хорошо, прежде чем вы скинете фотки. Есть ли какие-нибудь моменты, о которых мы должны знать @moments
        Phase phaseMoments = new Phase();
        phaseMoments.setToSection("$print-photos");
        phaseMoments.setPhaseKey("@moments");
        phaseMoments.setPhrase("Хорошо, прежде чем вы скинете фотки, есть ли какие-нибудь моменты, о которых мы должны знать?\n (Вы также можете прикрепить какой нибудь файл)");
        phaseMoments.setFilesWaitingCount(0);
        phaseMoments.setWaitForMessage(false);
        phaseMoments.setCallbackOnMessage(null);
        phaseMoments.setAdditionalCallbackOnBtn(null);
        phaseMoments.setPrevPhaseKey("@whatFormat");
        phaseMoments.setNextPhaseKey("yes:@priceDependsOnComment, no:@sizes");


        //@priceDependsOnComment  Внимание, цена может поменяться в зависимости от вашего пожелания!
        Phase phasePriceDependsOnComment = new Phase();
        phasePriceDependsOnComment.setToSection("$print-photos");
        phasePriceDependsOnComment.setPhaseKey("@priceDependsOnComment");
        phasePriceDependsOnComment.setPhrase("Внимание, может случится так, что цена может поменяться в зависимости от вашего пожелания, либо мы не сможем это сделать!\n В этих случаях мы вам отпишемся \n Напишите свой комментарий.");
        phasePriceDependsOnComment.setFilesWaitingCount(0);
        phasePriceDependsOnComment.setWaitForMessage(true);
        phasePriceDependsOnComment.setCallbackOnMessage("setTempComment");
        phasePriceDependsOnComment.setAdditionalCallbackOnBtn(null);
        phasePriceDependsOnComment.setPrevPhaseKey("@moments");
        phasePriceDependsOnComment.setNextPhaseKey("@commentForPartOrAll");


        //@commentForPartOrAll Эти пожелания касаются всех форматов, или только для этого формата?
        Phase phaseСommentForPartOrAll = new Phase();
        phaseСommentForPartOrAll.setToSection("$print-photos");
        phaseСommentForPartOrAll.setPhaseKey("@commentForPartOrAll");
        phaseСommentForPartOrAll.setPhrase("Эти пожелания касаются всех форматов, или только для этого формата?");
        phaseСommentForPartOrAll.setFilesWaitingCount(0);
        phaseСommentForPartOrAll.setWaitForMessage(false);
        phaseСommentForPartOrAll.setCallbackOnMessage("tempCommentToCurrentComment");
        phaseСommentForPartOrAll.setAdditionalCallbackOnBtn(null);
        phaseСommentForPartOrAll.setPrevPhaseKey("@moments");
        phaseСommentForPartOrAll.setNextPhaseKey("@ifUnclearNotify");


        //@downloadByTen "Скиньте все свои фотографии (можно по 10 штук). Для сохранения качества фотографий лучше скидывайте их как \"документ\".
        Phase phaseDownloadByTen = new Phase();
        phaseDownloadByTen.setToSection("$print-photos");
        phaseDownloadByTen.setPhaseKey("@downloadByTen");
        phaseDownloadByTen.setPhrase("Эти пожелания касаются всех форматов, или только для этого формата?");
        phaseDownloadByTen.setFilesWaitingCount(10);
        phaseDownloadByTen.setWaitForMessage(false);
        phaseDownloadByTen.setCallbackOnMessage("tempCommentToCurrentComment");
        phaseDownloadByTen.setAdditionalCallbackOnBtn(null);
        phaseDownloadByTen.setPrevPhaseKey("@moments");
        phaseDownloadByTen.setNextPhaseKey("@afterDownloadingByTen");


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


        //@nextFormat Какой следующий формат?

        Phase phaseNextFormat = new Phase();
        phaseNextFormat.setToSection("$print-photos");
        phaseNextFormat.setPhaseKey("@nextFormat");
        phaseNextFormat.setPhrase("Не удаляйте фотографии до выполнения заказа. Какой следующий формат?");
        phaseNextFormat.setFilesWaitingCount(0);
        phaseNextFormat.setWaitForMessage(false);
        phaseNextFormat.setCallbackOnMessage(null);
        phaseNextFormat.setAdditionalCallbackOnBtn(null);
        phaseNextFormat.setPrevPhaseKey("@nextFormat");
        phaseNextFormat.setNextPhaseKey("papperType");

        PersonField personFieldNextFormat = new PersonField();
        personFieldNextFormat.setClassName("PrintPhotoOrder");
        personFieldNextFormat.setSetter("setFormatPhoto");
        personFieldNextFormat.setType("FormatPhoto");
        phaseNextFormat.setFieldsToChange(Collections.singletonList(personFieldNextFormat));

    }



@   ForApi
    public Phase(String phrase, Keyboard answerKeyBoard){
        this.phrase = phrase;
        this.answerKeyBoard = answerKeyBoard;
    }

    public Phase(Phase phase){
       this.phrase = phase.phrase;
       this.answerKeyBoard = phase.answerKeyBoard;
    }

    public static void save(Phase phase) {
        EntityManager em = Hiber.getEntityManager();
        em.getTransaction().begin();
        em.persist(phase);
        em.flush();
        em.getTransaction().commit();
    }
}
