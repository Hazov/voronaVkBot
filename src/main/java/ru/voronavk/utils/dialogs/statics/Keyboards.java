package ru.voronavk.utils.dialogs.statics;

import api.longpoll.bots.model.objects.additional.Keyboard;
import api.longpoll.bots.model.objects.additional.buttons.Button;

import java.util.ArrayList;
import java.util.List;

public class Keyboards {

    private static Keyboard buildKeyBoard(List<Button> buttons){
        int col = 0;
        List<List<Button>> all = new ArrayList<>();
        List<Button> row = new ArrayList<>();
        for (Button button : buttons){
            if(col == 0){

                if(row.size() > 0){
                    all.add(row);
                }
                row = new ArrayList<>();
            }
            row.add(button);
            if(++col > 4) col = 0;
        }
        if(row.size() > 0){
            all.add(row);
        }
        return new Keyboard(all);
    }


    public static Keyboard ordersMenu(){
        return buildKeyBoard(Buttons.menuButtons());
    }
    public static Keyboard formatsPhotoKeyBoard(){
        return buildKeyBoard(Buttons.formatsPhotoButtons());
    }
    public static Keyboard manyFormatsKeyBoard() {
        return buildKeyBoard(Buttons.manyFormatsButtons());
    }
    public static Keyboard papperTypeKeyBoard() {
        return buildKeyBoard(Buttons.papperTypeButtons());
    }


    public static Keyboard repeatPhotosKeyBoard() {
        return buildKeyBoard(Buttons.repeatPhotosButtons());
    }

    public static Keyboard warnNoFormatKeyBoard() {
        return buildKeyBoard(Buttons.warnNoFormatButtons());
    }

    public static Keyboard byOneWithCountKeyBoard() {
        return buildKeyBoard(Buttons.byOneWithCountButtons());
    }

    public static Keyboard spellOutFormatKeyBoard() {
        return buildKeyBoard(Buttons.spellOutFormatButtons());
    }

    public static Keyboard noRecognizeFormatKeyBoard() {
        return buildKeyBoard(Buttons.noRecognizeFormatButtons());
    }

    public static Keyboard afterDownloadingKeyBoard() {
        return buildKeyBoard(Buttons.afterDownloadingButtons());
    }

    public static Keyboard momentsKeyBoard() {
        return buildKeyBoard(Buttons.momentsButtons());
    }

    public static Keyboard priceDependsOnCommentKeyBoard() {
        return buildKeyBoard(Buttons.priceDependsOnCommentButtons());
    }

    public static Keyboard commentForPartOrAllKeyBoard() {
        return buildKeyBoard(Buttons.commentForPartOrAllButtons());
    }

    public static Keyboard downloadByTenKeyBoard() {
        return buildKeyBoard(Buttons.downloadByTenButtons());
    }

    public static Keyboard sizesKeyBoard() {
        return buildKeyBoard(Buttons.sizesButtons());
    }

    public static Keyboard specificationCountKeyBoard(){
        return buildKeyBoard(Buttons.specificationCountButtons());
    }
}
