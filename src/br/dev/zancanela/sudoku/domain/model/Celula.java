package br.dev.zancanela.sudoku.domain.model;

final public class Celula {
    private final boolean fixa;
    private final int esperado;
    private int valor;

    public Celula(final int valor, final boolean fixa, final int esperado) {
        this.valor = valor;
        this.fixa = fixa;
        this.esperado = esperado;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(final int valor) {
        this.valor = valor;
    }

    public int getEsperado() {
        return esperado;
    }

    public boolean isFixa() {
        return fixa;
    }
}