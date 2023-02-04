package ru.voronavk;

import api.longpoll.bots.exceptions.VkApiException;
import api.longpoll.bots.methods.impl.VkBotsApi;
import api.longpoll.bots.methods.impl.messages.Send;
import api.longpoll.bots.model.events.messages.MessageEvent;
import api.longpoll.bots.model.events.messages.MessageNewEvent;
import api.longpoll.bots.model.objects.additional.Keyboard;
import api.longpoll.bots.model.objects.additional.PhotoSize;
import api.longpoll.bots.model.objects.basic.Message;
import api.longpoll.bots.model.objects.media.Attachment;
import api.longpoll.bots.model.objects.media.AttachmentType;
import api.longpoll.bots.model.objects.media.Doc;
import api.longpoll.bots.model.objects.media.Photo;
import ru.voronavk.annotations.ForApi;
import ru.voronavk.diao.CallBackOnMessageParams;
import ru.voronavk.diao.CallbackResponse;
import ru.voronavk.diao.CountOfPhotoResponse;
import ru.voronavk.diao.PrintPhotosResponse;
import ru.voronavk.entities.*;
import ru.voronavk.exception.UnsupportedTypeForFieldException;
import ru.voronavk.utils.ApiUtil;
import ru.voronavk.utils.FilesUtil;
import ru.voronavk.utils.dialogs.statics.Phases;
import ru.voronavk.utils.ButtonsUtil;
import ru.voronavk.utils.hibernate.Hiber;
import ru.voronavk.utils.reflection.Finder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


//to makeOrder phase: what

public class PrintPhotosDialogManager {
    public static final String currentClass = PrintPhotoOrder.class.getName();
    VkBotsApi vkBotsApi;
    public static final String startAnswer = "printPhotos";

    public PrintPhotosDialogManager(VkBotsApi vkBotsApi) {
        this.vkBotsApi = vkBotsApi;
    }

    public PrintPhotosResponse processMessageOrFiles(MessageNewEvent messageNewEvent, Person person){
        Phase phase = person.getState().getPhase();
        Message message = messageNewEvent.getMessage();
        boolean messageHasFiles = message.getAttachments().size() > 0;
        boolean messageHasText = message.getText() != null && !message.getText().equals("");

            CallBackOnMessageParams callBackOnMessageParams = new CallBackOnMessageParams(person, message.getText(), message.getAttachments());
            CallbackResponse callBackResponse = getCallBackResponse(callBackOnMessageParams, phase.getCallbackOnMessage());
            if(callBackResponse != null){
                Phase nextPhase = callBackResponse.getNextPhase();
                return processPhase(phase, nextPhase, null, person);
            }
        return null;
    }

    private CallbackResponse getCallBackResponse(CallBackOnMessageParams callBackOnMessageParams, String callbackOnMessage) {
        try{
            Class<? extends PrintPhotosDialogManager> thisClass = this.getClass();
            List<Method> methods = Arrays.stream(thisClass.getDeclaredMethods())
                    .filter(m -> m.getName().equals(callbackOnMessage))
                    .collect(Collectors.toList());
            if(methods.size() > 0){
                Method method = methods.get(0);
                return (CallbackResponse) method.invoke(this, callBackOnMessageParams);
            }
        } catch (Exception e){
            //TODO не смог взять callbackMethod
        }
        return null;
    }

    @ForApi
    private CallbackResponse assignFileOrCountToLastDifferentCountPhoto(CallBackOnMessageParams callBackOnMessageParams){
        //В этом методе ждем и фотку и текст (число фоток)
        Person person = callBackOnMessageParams.getPerson();
        String messageText = callBackOnMessageParams.getMessageText();
        List<Attachment> attachments = callBackOnMessageParams.getAttachments();
        Phase phase = person.getState().getPhase();
        //что мы ждем от пользователя (фотку потом сообщение)
        DifferentCountPhoto lastDifferentCountPhotoOrder = Person.lastDifferentCountPhotoOrder(person);
        //если есть фотка то ждем только текст с количеством
        if(lastDifferentCountPhotoOrder.getUrlFile() != null){
            //Ошибка: если не ввел количество
            if(messageText == null || messageText.equals("") || attachments.size() > 0){
                return new CallbackResponse(false, Phases.aboutPrintPhotos("@warnNoCountHowManyByOne"));
            } else {
                //пытаемся распознать
                CountOfPhotoResponse countOfPhotoResponse = tryRecognizeCountOfPhoto(messageText);

                if(countOfPhotoResponse.getCount() == 0){
                    return new CallbackResponse(false, countOfPhotoResponse.getNextPhase());
                } else {
                    lastDifferentCountPhotoOrder.setCount(countOfPhotoResponse.getCount());
                    DifferentCountPhoto.save(lastDifferentCountPhotoOrder);
                    return new CallbackResponse(true, countOfPhotoResponse.getNextPhase());
                }
            }
            //иначе ждем и то и другое
        } else {
            if(attachments.size() == 0){
                return new CallbackResponse(false, Phases.aboutPrintPhotos("@waitForOnePhoto"));
            } else {
                if(attachments.size() > person.getState().getPhase().getFilesWaitingCount()){
                    Phase moreThanOnePhase = Phases.aboutPrintPhotos("@moreThanOneErrorByOne");
                    moreThanOnePhase.setPhrase(moreThanOnePhase.getPhrase().replaceAll("\\[0\\]", String.valueOf(attachments.size())));
                    return new CallbackResponse(false, moreThanOnePhase);
                } else {
                    Attachment attachment = attachments.get(0);
                    String url = null;
                    if(attachment.getType() == AttachmentType.DOCUMENT){
                        String ext = ((Doc) (attachment.getAttachable())).getExt();
                        //Ошибка: если не картинка
                        if(!FilesUtil.isImageExtension(ext)){
                            return new CallbackResponse(false, Phases.aboutPrintPhotos("@noSupportedFormat"));
                        }
                        url = ((Doc) (attachment.getAttachable())).getUrl();

                    } else if (attachment.getType() == AttachmentType.PHOTO){
                        url = getMaxExtendUrl(((Photo) (attachment.getAttachable())).getPhotoSizes());
                        if(url == null || url.equals("")){
                            return new CallbackResponse(false, Phases.aboutPrintPhotos("@noSupportedFormat"));
                        }
                    } else {
                        return new CallbackResponse(false, Phases.aboutPrintPhotos("@noSupportedFormat"));
                    }
                    UrlFile urlFile = new UrlFile();
                    urlFile.setUrl(url);
                    UrlFile.save(urlFile);
                    lastDifferentCountPhotoOrder.setUrlFile(urlFile);
                    DifferentCountPhoto.save(lastDifferentCountPhotoOrder);
                    if(messageText == null || messageText.equals("")){
                        return new CallbackResponse(true, Phases.aboutPrintPhotos("@warnNoCountHowManyByOne"));
                    } else {
                        CountOfPhotoResponse countOfPhotoResponse = tryRecognizeCountOfPhoto(messageText);
                        if(countOfPhotoResponse.getCount() == 0){
                            return new CallbackResponse(false, countOfPhotoResponse.getNextPhase());
                        } else {
                            lastDifferentCountPhotoOrder.setCount(countOfPhotoResponse.getCount());
                            return new CallbackResponse(true, countOfPhotoResponse.getNextPhase());
                        }
                    }
                }
            }
        }
    }

    //Если countOfPhotoResponse.nextPhaseKey == null, то фазу решает метод выше
    private CountOfPhotoResponse tryRecognizeCountOfPhoto(String messageText) {
        Pattern pattern = Pattern.compile("(?:(\\d)*)");
        Matcher matcher = pattern.matcher(messageText);
        if(matcher.find()){
            try{
                Integer countOfPhoto = Integer.parseInt(messageText.substring(matcher.start(), matcher.end()));
                if(countOfPhoto >= 20){
                    Phase phase = Phases.aboutPrintPhotos("@specificationCount");
                    if(phase != null){
                        phase.setPhrase(phase.getPhrase().replaceAll("\\[0\\]", String.valueOf(countOfPhoto)));
                    }
                    return new CountOfPhotoResponse(countOfPhoto, phase);
                } else {
                    if(messageText.length() > matcher.end()){
                        Phase phase = Phases.aboutPrintPhotos("@specificationCount");
                        if(phase != null){
                            phase.setPhrase(phase.getPhrase().replaceAll("\\[0\\]", String.valueOf(countOfPhoto)));
                        }
                        return new CountOfPhotoResponse(countOfPhoto, phase);
                    }
                    return new CountOfPhotoResponse(countOfPhoto, Phases.aboutPrintPhotos("@afterDownloading"));
                }
            } catch (Exception e){

                return new CountOfPhotoResponse(0, Phases.aboutPrintPhotos("@noRecognizeCountOfPhoto"));

            }
        } else {
            return new CountOfPhotoResponse(0, Phases.aboutPrintPhotos("@noRecognizeCountOfPhoto"));
        }
    }

    private String getMaxExtendUrl(List<PhotoSize> photoSizes) {
        int maxWidth = 0;
        String maxUrl = "";
        for (PhotoSize photoSize: photoSizes){
            if(photoSize.getWidth() > maxWidth){
                maxWidth = photoSize.getWidth();
                maxUrl = photoSize.getUrl();
            }
        }
        return maxUrl;
    }

    public PrintPhotosResponse processEvent(MessageEvent messageEvent, Person person){
        //Подготавливаем пользователя
        person = preparePerson(person);
        //Ответ пользователя
        String userAnswer = ApiUtil.getUserAnswer(messageEvent);
        Phase currentPhase = person.getState().getPhase();

        PrintPhotosResponse printPhotosResponse = null;
        if (userAnswer == null || userAnswer.equals("printPhotos")) {

            printPhotosResponse = processPhase(currentPhase, userAnswer, person);
        } else {
            printPhotosResponse = processAnswer(currentPhase, userAnswer, person);
        }

        return printPhotosResponse;
    }

    private Phase defineNextPhase(Person person, String userAnswer) {
        PersonState personState =  person.getState();
        Phase phase = personState.getPhase();
        //Это начальная фаза
        if(personState.getPhase() == null){
            return Phases.aboutPrintPhotos("@whatFormat");
        } else {
            String nextPhaseKey = phase.getNextPhaseKey();
            if(nextPhaseKey != null){
                nextPhaseKey = nextPhaseKey.replaceAll(" ", "");
                if(nextPhaseKey.contains(",")){
                    Map<String, String> conditionsKeys = new HashMap<>();
                    String[] conditionsKeysStr = nextPhaseKey.split(",");
                    Arrays.stream(conditionsKeysStr).forEach(ck -> {
                        String[] split = ck.split(":");
                        conditionsKeys.put(split[0], split[1]);
                    });
                    return Phases.aboutPrintPhotos(conditionsKeys.get(userAnswer));
                } else {
                    return Phases.aboutPrintPhotos(nextPhaseKey);
                }
            }
        }
        return null;
    }

    private Person preparePerson(Person person) {
        if(person.getState().getCurrentOrder() == null){
            createNewMultiOrder(person);
        }
        if(person.getState().getCurrentOrder().getPrintPhotoMultiOrder() == null){
            createPrintPhotoMultiOrder(person);
        }
        List<PrintPhotoOrder> printPhotoOrders = person.getState().getCurrentOrder().getPrintPhotoMultiOrder().getPrintPhotoOrders();
        if(printPhotoOrders == null || printPhotoOrders.size() == 0){
            createPrintPhotoOrder(person);
        }
        return person;
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

     PrintPhotosResponse processPhase(Phase currentPhase, Phase nextPhase, String userAnswer, Person person){
         if(nextPhase == null){
             nextPhase = defineNextPhase(person, userAnswer);
         } else {
             if(currentPhase != null){
                 if(currentPhase.getNextPhaseKey() != null){
                     nextPhase = Phases.aboutPrintPhotos(currentPhase.getNextPhaseKey());
                 }
             } else {
                 nextPhase = Phases.aboutPrintPhotos("@whatFormat");
             }
         }
         person.getState().setPhase(nextPhase);
         Person.save(person);
         int personId = longToIntId(person.getId());
         int messageId;
         try{
             Send.Response response;
             Send send = vkBotsApi.messages().send()
                     .setPeerId(personId)
                     .setMessage(nextPhase.getPhrase());
             if(nextPhase.getAnswerKeyBoard() != null) {
                 send = send.setKeyboard(nextPhase.getAnswerKeyBoard().setInline(true));
             }
             response = send.execute();

             messageId = (Integer) response.getResponseObject() - 1;
             return new PrintPhotosResponse(messageId);
         }catch (Exception e){

         }
         return null;
     }
     PrintPhotosResponse processPhase(Phase currentPhase,String userAnswer, Person person){
         return processPhase(currentPhase, null, userAnswer, person);
    }

    private int longToIntId(Long id) {
        return Integer.parseInt(String.valueOf(id));
    }

    PrintPhotosResponse processAnswer(Phase currentPhase, String userAnswer, Person person){
        Phase nextPhase = defineNextPhase(person, userAnswer);
        int personId = Integer.parseInt(String.valueOf(person.getId()));
        changeFieldByAnswer(userAnswer, person);
        int messageId;
        Send.Response response = null;
        try {
            Keyboard replacedKeyboard = nextPhase.getAnswerKeyBoard();
            if(nextPhase.getAnswerKeyBoard() != null){
                replacedKeyboard = nextPhase.getAnswerKeyBoard().setButtons(ButtonsUtil.replaceLabels(nextPhase.getAnswerKeyBoard().getButtons(), person));
            }
            response = vkBotsApi.messages().send()
                    .setPeerId(personId)
                    .setMessage(nextPhase.getPhrase())
                    .setKeyboard(replacedKeyboard.setInline(true))
                    .execute();
            messageId = (Integer)response.getResponseObject() - 1;
            person.getState().setPhase(nextPhase);
            Person.save(person);
            return new PrintPhotosResponse(messageId);
        } catch (VkApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void changeFieldByAnswer(String userAnswer, Person person) {
        //TODO пока что сделано только для одного поля
        List<PersonField> fieldsToChange = Person.getFieldsToChange(person);
        fieldsToChange.forEach(f -> {
            try{
                String className = "ru.voronavk.entities." + f.getClassName();
                String setterName = f.getSetter();
                Class<?> aClass = Class.forName(className);
                Method setter = Finder.findMethodByName(aClass, setterName);
                Object entity = Finder.findEntityInPerson(person, aClass);
                String type = f.getType();
                Object answer = castAnswerToType(userAnswer,type);
                applySetter(setter, entity, answer);
                Hiber.save(entity);
                Person.save(person);
            }catch (Exception e){
e.printStackTrace();
            }
        });

    }

    private void applySetter(Method setter, Object entity, Object answer) {
        try {
            setter.invoke(entity, answer);
        } catch (Exception e){

        }

    }
    public Object castAnswerToType(String userAnswerString, String typeString) throws UnsupportedTypeForFieldException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Object answer = null;
        Class<?> type = Finder.searchClassByName(typeString);
        if(type == Boolean.class){
            if(userAnswerString.equals("yes")){
                answer = Boolean.TRUE;
            } else if (userAnswerString.equals("no")){
                answer = Boolean.FALSE;
            } else {
                throw new UnsupportedTypeForFieldException();
            }
        } else if(type == String.class){
            answer = userAnswerString;
        } else if(type == Double.class){
            answer = Double.parseDouble(userAnswerString);
        } else if(type == Integer.class){
            answer = Integer.parseInt(userAnswerString);
        } else if (type == Long.class){
            answer = Long.parseLong(userAnswerString);
        } else {
            answer = userAnswerString;
            Constructor<?> constructor = Arrays.stream(type.getConstructors())
                    .filter(c -> c.isAnnotationPresent(ForApi.class)).collect(Collectors.toList()).get(0);
            answer = constructor.newInstance(answer);
            Hiber.save(answer);
        }
        return answer;
    }


    public int getGroupId() {
        return 218164705;
    }
}
