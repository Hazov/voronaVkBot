package ru.voronavk.diao;

import api.longpoll.bots.model.events.messages.MessageNewEvent;
import api.longpoll.bots.model.objects.basic.Message;
import api.longpoll.bots.model.objects.media.Attachment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.voronavk.entities.Person;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class CallBackOnMessageParams {
    private Person person;
    private String messageText;
    private List<Attachment> attachments;

}
