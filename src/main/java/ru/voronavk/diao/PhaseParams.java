package ru.voronavk.diao;


import api.longpoll.bots.model.objects.additional.Keyboard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
@Setter
@AllArgsConstructor
public class PhaseParams {
    String phrase;
    Supplier<Keyboard> keyboardSupplier;

}
