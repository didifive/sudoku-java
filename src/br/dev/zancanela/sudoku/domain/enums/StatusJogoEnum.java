package br.dev.zancanela.sudoku.domain.enums;

public enum StatusJogoEnum {

    NAO_INICIADO("Não Iniciado"),
    INCOMPLETO("Incompleto"),
    COMPLETO("Completo");

    private final String label;

    StatusJogoEnum(final String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
