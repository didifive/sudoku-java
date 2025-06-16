package br.dev.zancanela.sudoku.domain.event;

import br.dev.zancanela.sudoku.domain.enums.JogoEventEnum;

import java.util.*;

public class JogoNotifierService {
    private final Map<JogoEventEnum, List<JogoEventListener>> listeners = new EnumMap<>(JogoEventEnum.class);

    public void subscribe(JogoEventEnum eventType, JogoEventListener listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    public void notify(JogoEventEnum eventType, int novoValor) {
        List<JogoEventListener> list = listeners.get(eventType);
        if (list != null) {
            for (JogoEventListener l : list) {
                l.onEvent(eventType, novoValor);
            }
        }
    }
}
