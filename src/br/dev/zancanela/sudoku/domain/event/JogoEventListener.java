package br.dev.zancanela.sudoku.domain.event;

import br.dev.zancanela.sudoku.domain.enums.JogoEventEnum;

public interface JogoEventListener {
    void onEvent(JogoEventEnum eventType, int novoValor);
}