package ru.voronavk.diao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrintPhotosResponse {
    private int messageId;
    private String textForEditedMessageWithButtons;

    public PrintPhotosResponse(int messageId, String textForEditedMessageWithButtons) {
        this.messageId = messageId;
        this.textForEditedMessageWithButtons = textForEditedMessageWithButtons;
    }
}
