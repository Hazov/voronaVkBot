package ru.voronavk;

import api.longpoll.bots.model.objects.additional.Keyboard;
import api.longpoll.bots.model.objects.additional.Template;
import api.longpoll.bots.model.objects.additional.buttons.*;
import api.longpoll.bots.model.objects.additional.carousel.Carousel;
import api.longpoll.bots.model.objects.additional.carousel.Element;
import com.google.gson.JsonObject;
import java.util.Arrays;
import java.util.List;

public class Key {
    public static JsonObject negativeButtonPayloadPayload1 = new JsonObject();
    public static Button back = new TextButton(Button.Color.NEGATIVE, new TextButton.Action(
            "Кнопки",
            negativeButtonPayloadPayload1
    ));
    public static List<Button> rowBack = Arrays.asList(back);

    public static Keyboard getSimpleKeyboard() {
        JsonObject positiveButtonPayload = new JsonObject();
        Button positiveButton = new TextButton(Button.Color.POSITIVE, new TextButton.Action(
                "Приложения",
                positiveButtonPayload));
        JsonObject negativeButtonPayload = new JsonObject();
        Button negativeButton = new TextButton(Button.Color.NEGATIVE, new TextButton.Action(
                "Ссылки",
                negativeButtonPayload));
        JsonObject primaryButtonPayload = new JsonObject();
        Button primaryButton = new TextButton(Button.Color.PRIMARY, new TextButton.Action(
                "CallBack",
                primaryButtonPayload));
        JsonObject secondaryButtonPayload = new JsonObject();
        Button secondaryButton = new TextButton(Button.Color.SECONDARY, new TextButton.Action(
                "Карусель",
                secondaryButtonPayload));
        JsonObject secondaryButtonPayload1 = new JsonObject();
        Button secondaryButton1 = new TextButton(Button.Color.SECONDARY, new TextButton.Action(
                "VkPay and location",
                secondaryButtonPayload1));
        List<Button> row1 = Arrays.asList(positiveButton, negativeButton);
        List<Button> row2 = Arrays.asList(primaryButton, secondaryButton);
        List<Button> row3 = Arrays.asList(secondaryButton1);
        Keyboard keyboard = new Keyboard(Arrays.asList(row1, row2, row3));
        return keyboard;
    }

    public static Keyboard getAppKeyboard(){
        Button button = new VKAppsButton(new VKAppsButton.Action(
                7573968,
                162283018,
                "Аптеки",
                null,
                null
        ));
        Button button1 = new VKAppsButton(new VKAppsButton.Action(
                200880347,
                162283018,
                "Спроси",
                null,
                null
        ));
        Button button2 = new VKAppsButton(new VKAppsButton.Action(
                7636837,
                11349227,
                "Призыва Нет",
                null,
                null
        ));
        List<Button> row1 = Arrays.asList(button);
        List<Button> row2 = Arrays.asList(button1, button2);
        Keyboard keyboardApp = new Keyboard(Arrays.asList(row1, row2, rowBack));
        return keyboardApp;
    }

    public static Keyboard getLinkKeyboard(){
        Button button = new OpenLinkButton(new OpenLinkButton.Action(
                "https://vk.com/porridge2",
                "VK",
                null));
        Button button1 = new OpenLinkButton(new OpenLinkButton.Action(
                "https://www.youtube.com/channel/UCqPVk_N33TZN8KiTT0kxgCw",
                "YouTube",
                null));
        Button button2 = new OpenLinkButton(new OpenLinkButton.Action(
                "https://www.instagram.com/pelix.png/",
                "Instagram",
                null));
        List<Button> row1 = Arrays.asList(button);
        List<Button> row2 = Arrays.asList(button1);
        List<Button> row3 = Arrays.asList(button2);
        Keyboard keyboard = new Keyboard(Arrays.asList(row1, row2, row3, rowBack));
        return keyboard;
    }

    public static Keyboard callBackKeyboard(){
        JsonObject payload = new JsonObject();
        payload.addProperty("1", "1");
        JsonObject payload1 = new JsonObject();
        payload1.addProperty("2", "2");
        Button button = new CallbackButton(Button.Color.PRIMARY, new CallbackButton.Action("Уведомление", payload));
        Button button1 = new CallbackButton(Button.Color.NEGATIVE, new CallbackButton.Action("Кнопки", payload1));
        List<Button> row1 = Arrays.asList(button, button1);
        Keyboard keyboard = new Keyboard(Arrays.asList(row1));
        return keyboard;
    }

    public static Template callCarouselKeyboard(){
        Button button1 = new TextButton(Button.Color.POSITIVE, new TextButton.Action("Приложения", null));
        Button button2 = new TextButton(Button.Color.NEGATIVE, new TextButton.Action("Ссылки", null));
        Button button3 = new TextButton(Button.Color.PRIMARY, new TextButton.Action("CallBack", null));
        Button button4 = new TextButton(Button.Color.SECONDARY, new TextButton.Action("VkPay and location", null));
        Button button5 = new TextButton(Button.Color.SECONDARY, new TextButton.Action("Карусель", null));
        Element element1 = new Element()
                .setTitle("Список") //Название
                .setDescription("Список с кнопками") //Описание
                .setButtons(button1, button2, button3);
        Element element2 = new Element()
                .setTitle("Ещё кнопки")
                .setDescription("То что не влезло")
                .setButtons(button4, button5, back);
        Template carousel = new Carousel(Arrays.asList(element1, element2));
        return carousel;
    }

    public static Keyboard vkPayAndLocationKeyboard(){
        Button locationButtonButton = new LocationButton(new LocationButton.Action(null));
        Button VkPaybutton = new VKPayButton(new VKPayButton.Action(
                "action=transfer-to-group&group_id=199601838&aid=10",
                null));
        List<Button> row1 = Arrays.asList(locationButtonButton);
        List<Button> row2 = Arrays.asList(VkPaybutton);
        Keyboard keyboard = new Keyboard(Arrays.asList(row1, row2, rowBack));
        return keyboard;
    }
}



