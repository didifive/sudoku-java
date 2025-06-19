package br.dev.zancanela.sudoku.domain.model;

import java.util.List;

public record Tabuleiro (List<List<Celula>> celulas) {
}
