package ru.voronavk;

import api.longpoll.bots.exceptions.VkApiException;
import api.longpoll.bots.methods.impl.VkBotsApi;
import api.longpoll.bots.methods.impl.messages.Send;
import api.longpoll.bots.model.events.messages.MessageEvent;
import api.longpoll.bots.model.events.messages.MessageNewEvent;
import api.longpoll.bots.model.objects.additional.Keyboard;
import com.google.gson.JsonObject;
import ru.voronavk.diao.UserAnswer;
import ru.voronavk.diao.PrintPhotosResponse;
import ru.voronavk.entities.*;
import ru.voronavk.utils.dialogs.statics.Phases;
import ru.voronavk.utils.ButtonsUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


//to makeOrder phase: what

public class PrintPhotosDialogManager {
    public static final String currentClass = PrintPhotoOrder.class.getName();
    VkBotsApi vkBotsApi;

    public PrintPhotosDialogManager(VkBotsApi vkBotsApi) {
        this.vkBotsApi = vkBotsApi;
    }

    public PrintPhotosResponse processMessageOrFiles(MessageNewEvent messageNewEvent, Person person){
        Phase phase = person.getState().getOrderPhase();

        return null;
    }

    public PrintPhotosResponse process(MessageEvent messageEvent, Person person){
        createEmptyPrintPhotoOrderIfAbsent(person);

        JsonObject payload = messageEvent.getPayload().getAsJsonObject();
        String phaseStr = payload.get("phase").toString().replaceAll("\"", "");
        String answerField = null;
        String answerValue = null;
        try {
            answerField = payload.get("answerField").toString().replaceAll("\"", "");
            answerValue = payload.get("answerValue").toString().replaceAll("\"", "");
        } catch(NullPointerException e) {

        }
        UserAnswer userAnswer = null;
        if (answerValue != null || answerField != null) {
            userAnswer = new UserAnswer(answerField, answerValue, currentClass, person);
        }
        Phase phase = Phases.aboutPrintPhotos(phaseStr);

        PrintPhotosResponse printPhotosResponse;
        if (userAnswer == null && phase != null) {
            return processPhase(phase, person);
        } else {
            return processAnswer(phase, userAnswer, person);
        }
    }

    private void createEmptyPrintPhotoOrderIfAbsent(Person person) {
        if(person.getState().getCurrentOrder() == null){
            createNewMultiOrder(person);
        }
        if(person.getState().getCurrentOrder().getPrintPhotoMultiOrder() == null){
            createPrintPhotoMultiOrder(person);
        }
        if(person.getState().getCurrentOrder().getPrintPhotoMultiOrder() == null){
            createPrintPhotoMultiOrder(person);
        }
        List<PrintPhotoOrder> printPhotoOrders = person.getState().getCurrentOrder().getPrintPhotoMultiOrder().getPrintPhotoOrders();
        if(printPhotoOrders == null || printPhotoOrders.size() == 0){
            createPrintPhotoOrder(person);
        }
    }

    private void createPrintPhotoOrder(Person person) {
        PrintPhotoOrder printPhotoOrder = new PrintPhotoOrder();
        PrintPhotoOrder.save(printPhotoOrder);
        List<PrintPhotoOrder> printPhotoOrders = new ArrayList<>();
        printPhotoOrders.add(printPhotoOrder);
        person.getState().getCurrentOrder().getPrintPhotoMultiOrder().setPrintPhotoOrders(printPhotoOrders);
        Person.save(person);
    }


    private void createNewMultiOrder(Person person) {
        MultiOrder currentOrder = person.getState().getCurrentOrder();
        if(currentOrder == null){
            currentOrder = new MultiOrder();
            currentOrder.setPersonId(person.getId());
            MultiOrder.save(currentOrder);
            person.getState().setCurrentOrder(currentOrder);
            Person.save(person);
        }
    }
    private void createPrintPhotoMultiOrder(Person person) {
        PrintPhotoMultiOrder printPhotoMultiOrder = new PrintPhotoMultiOrder();
        PrintPhotoMultiOrder.save(printPhotoMultiOrder);
        person.getState().getCurrentOrder().setPrintPhotoMultiOrder(printPhotoMultiOrder);
        Person.save(person);

    }

    boolean changePhotoFormat(String format){
        return true;

    }

     PrintPhotosResponse processPhase(Phase phase, Person person){
         int personId = Integer.parseInt(String.valueOf(person.getId()));
         int messageId;
            try{
                Send.Response response;
                Send send = vkBotsApi.messages().send()
                .setPeerId(personId)
                .setMessage(phase.getPhrase());
                if(phase.getAnswerKeyBoard().getButtons().size() > 0) {
                   send = send.setKeyboard(phase.getAnswerKeyBoard().setInline(true));
                }
                response = send.execute();

                messageId = (Integer) response.getResponseObject() - 1;
                String textForEditedMessageWithButtons = "----------------------------------------------------------";
                return new PrintPhotosResponse(messageId, textForEditedMessageWithButtons);
            }catch (Exception e){

            }
            return null;
    }

    PrintPhotosResponse processAnswer(Phase phase, UserAnswer userAnswer, Person person){
        int personId = Integer.parseInt(String.valueOf(person.getId()));
        changeFieldByAnswer(userAnswer, person);
        System.out.println("Меняем поле у юзера");
        int messageId;
        Send.Response response = null;
        try {
            Keyboard replacedKeyboard = phase.getAnswerKeyBoard();
            if(phase.getAnswerKeyBoard() != null){
                replacedKeyboard = phase.getAnswerKeyBoard().setButtons(ButtonsUtil.replaceLabels(phase.getAnswerKeyBoard().getButtons(), person));
            }
            response = vkBotsApi.messages().send()
                    .setPeerId(personId)
                    .setMessage(phase.getPhrase())
                    .setKeyboard(replacedKeyboard.setInline(true))
                    .execute();
            messageId = (Integer)response.getResponseObject() - 1;
            String textForEditedMessageWithButtons = "----------------------------------------------------------";
            return new PrintPhotosResponse(messageId, textForEditedMessageWithButtons);
        } catch (VkApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void changeFieldByAnswer(UserAnswer userAnswer, Person person) {
//        if(person != null && person.getState() != null && person.getState().getCurrentOrder() != null && person.getState().getCurrentOrder().getPartOrders() != null){
//            person.getState().getCurrentOrder().getPartOrders().stream().max((po1, po2) -> po1.getId() - po2.getId())
//        }

        if(userAnswer != null){
            if(userAnswer.getFieldSetter() != null && userAnswer.getValue() != null){
                Method fieldSetter = userAnswer.getFieldSetter();
                try{
                    fieldSetter.invoke(userAnswer.getValue());
                }catch (Exception e){
                    return;
                }

            }
        }
    }
}
