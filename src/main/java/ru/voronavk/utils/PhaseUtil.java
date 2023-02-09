package ru.voronavk.utils;

import ru.voronavk.entities.Phase;
import ru.voronavk.utils.dialogs.statics.Phases;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PhaseUtil {
    public static Phase getNextPhaseByAnswer(Phase phase, String answer){
        String nextPhaseKey = phase.getNextPhaseKey();
        String toSection = phase.getToSection();
        if(nextPhaseKey.contains(answer + ":")){
            int start = nextPhaseKey.indexOf(answer + ":");
            String subWithAnswer = nextPhaseKey.substring(start + answer.length() + 1);
            String phaseKey = null;
            if(subWithAnswer.contains(",")){
                phaseKey = subWithAnswer.substring(0, subWithAnswer.indexOf(","));
            } else {
                phaseKey = subWithAnswer;
            }
            phaseKey = phaseKey.trim();
            return Phases.getCurrentPhase(toSection, phaseKey);
        }
       return null;
    }
}
