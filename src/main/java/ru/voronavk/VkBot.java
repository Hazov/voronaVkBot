package ru.voronavk;

import api.longpoll.bots.BotsLongPoll;
import api.longpoll.bots.LongPollBot;
import api.longpoll.bots.exceptions.VkApiException;
import api.longpoll.bots.methods.impl.users.Get;
import api.longpoll.bots.model.events.likes.LikeEvent;
import api.longpoll.bots.model.events.messages.MessageEvent;
import api.longpoll.bots.model.events.messages.MessageNewEvent;
import api.longpoll.bots.model.events.messages.MessageTypingStateEvent;
import api.longpoll.bots.model.events.users.GroupJoinEvent;
import api.longpoll.bots.model.events.users.GroupLeaveEvent;
import api.longpoll.bots.model.events.wall.comments.WallReplyEvent;
import api.longpoll.bots.model.objects.basic.Message;
import api.longpoll.bots.model.objects.basic.User;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.voronavk.diao.PrintPhotosResponse;
import ru.voronavk.entities.Person;
import ru.voronavk.entities.PersonState;
import ru.voronavk.entities.Phase;
import ru.voronavk.exception.CannotCreatePersonException;
import ru.voronavk.utils.ApiUtil;
import ru.voronavk.utils.dialogs.statics.Keyboards;
import ru.voronavk.utils.dialogs.statics.Phases;
import ru.voronavk.utils.hibernate.Hiber;
import ru.voronavk.utils.hibernate.initdb.PhasesPusher;

import java.util.Arrays;


@SpringBootApplication

public class VkBot extends LongPollBot {

    PrintPhotosDialogManager printPhotosDialogManager = new PrintPhotosDialogManager(vkBotsApi);


    public String[] makeOrderMatches = {"c","сделать заказ", "заказать", "меню", "начать"};
    @SneakyThrows
    @Override
    public void onMessageEvent(MessageEvent messageEvent) {
        PrintPhotosResponse printPhotosResponse = null;
        Person person = getPersonFromPeerId(messageEvent.getUserId());
        person = preparePerson(person);
        //Callback на callback-кнопки
        String userAnswer = ApiUtil.getUserAnswer(messageEvent);
        Phase phase = Person.getPhase(person);
        if(getToSection(phase).equals("$print-photos") || userAnswer.equals("printPhotos")){
            printPhotosResponse = printPhotosDialogManager.processEvent(messageEvent, person);
        }
            try {
                if(printPhotosResponse != null && printPhotosResponse.getMessageId() != 0){
                    vkBotsApi.messages().delete()
                            .setGroupId(getGroupId())
                            .setPeerId(messageEvent.getPeerId())
                            .setMessageIds(printPhotosResponse.getMessageId())
                            .setDeleteForAll(true)
                            .execute();
                }
            } catch (VkApiException e) {
                e.printStackTrace();
            }
    }

    private String getToSection(Phase phase) {
        if(phase != null && phase.getToSection() != null){
            return phase.getToSection();
        }
        return "";
    }

    private Person preparePerson(Person person) {
        if(person != null){
            if(person.getState() == null){
                PersonState personState = new PersonState();
                PersonState.save(personState);
            }
        }
        return person;
    }

    public boolean isMatch(String msg, String[] matches){
        return Arrays.stream(makeOrderMatches).anyMatch(mes -> mes.toLowerCase().equals(msg.toLowerCase()));
    }

    @Override
    public void onMessageNew(MessageNewEvent messageNewEvent) {
        Message message = messageNewEvent.getMessage();
        String msgText = message.getText();

        //TODO или кнопка Заказ нажата
        if (isMatch(msgText, makeOrderMatches) ) {
             try {
                 vkBotsApi.messages().send()
                         .setPeerId(message.getPeerId())
                         .setMessage(Phases.common("@whatOrder").getPhrase())
                         .setKeyboard(Keyboards.ordersMenu().setInline(true))
                         .execute();
            } catch (VkApiException e) {
                 e.printStackTrace();
             }
        } else {

            try{
                Person person = getPersonFromPeerId(messageNewEvent.getMessage().getPeerId());
                Phase phase =  person.getState().getPhase();
                if(phase.getToSection().equals("$print-photos")){
                    printPhotosDialogManager.processMessageOrFiles(messageNewEvent, person);
                }
            }catch (CannotCreatePersonException e){
            }


        }
    }

    @Override
    public void onWallReplyNew(WallReplyEvent wallReplyEvent) {

    }

    @Override
    public void onLikeAdd(LikeEvent likeEvent) {
        System.out.println(likeEvent);
    }

    @Override
    public void onLikeRemove(LikeEvent likeEvent) {
        System.out.println(likeEvent);
    }

    @Override
    public void onGroupLeave(GroupLeaveEvent groupLeaveEvent) {
        System.out.println(groupLeaveEvent);
    }

    @Override
    public void onGroupJoin(GroupJoinEvent groupJoinEvent) {
        System.out.println(groupJoinEvent);
    }

    @Override
    public void onMessageTypingState(MessageTypingStateEvent messageTypingStateEvent) {
        System.out.println(messageTypingStateEvent.getFromId());
        try {
            vkBotsApi.messages().send()
                    .setUserId(messageTypingStateEvent.getFromId())
                    .setMessage("Привет, сообщения используются только для рассылки новостей. Тебе здесь никто не ответит.")
                    .execute();
        } catch (VkApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getAccessToken() {
        return "vk1.a.MRC8ACxpL69sfFJHzrB9ttpGen7fyQ0cPrOleJe7wad989uO2l0YM_J6FR344jznoaLe6_y9Y6_4jqqrt31h9DGQFEVfRwSnMgLum0J0W7EFQWEyvIMrQM2BlQY0Zdu1QgT8KD_pq60ZXUdSGqGh6gkTZ6OSuedpW5hzbsh49PcU1r6vo9oe-AWy8gJYbSgyFPUv3Rs4wLInXHQkh6743g";
    }
    @Override
    public int getGroupId() {
        return 218164705;
    }

    public static void main(String[] args) throws VkApiException {
        Hiber.build();
        PhasesPusher.printPhotoPhasesInit();
        PhasesPusher.commonPhasesInit();
        new BotsLongPoll(new VkBot()).run();
    }


    private Person getPersonFromPeerId(int peerId) throws CannotCreatePersonException {
        Get.Response responseUser = null;
        try {
            responseUser = vkBotsApi.users().get().setUserIds(peerId).execute();
        } catch (VkApiException e) {
            e.printStackTrace();
        }
        Person person = Person.findById(peerId);
        if(person == null){
            person = createUser(responseUser);
        }
        return person;
    }

    private Person createUser(Get.Response responseUser) throws CannotCreatePersonException {
        Person person = null;
        User user;
        if(responseUser != null && responseUser.getResponseObject() != null && responseUser.getResponseObject().size() > 0){
            user = responseUser.getResponseObject().get(0);
            person = Person.insertNew(user);
        }
        if(person == null) throw new CannotCreatePersonException();
        return person;
    }
}













