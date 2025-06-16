package br.dev.zancanela.sudoku.domain.model;

import java.util.List;

final public class Tabuleiro {
    private final List<List<Celula>> celulas;

    public Tabuleiro(final List<List<Celula>> celulas) {
        this.celulas = celulas;
    }

    public List<List<Celula>> getCelulas() {
        return celulas;
    }

    public Celula getCelula(final int linha, final int coluna) {
        return celulas.get(linha).get(coluna);
    }

    public void setCelula(final int linha, final int coluna, final Celula celula) {
        celulas.get(linha).set(coluna, celula);
    }

}
