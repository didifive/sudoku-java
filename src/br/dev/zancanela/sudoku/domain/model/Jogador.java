package br.dev.zancanela.sudoku.domain.model;

final public class Jogador {
    private Long id;
    private String nome;

    public Jogador(String nome) {
        this.nome = nome;
    }

    public Jogador() {
    }

    public Jogador(final long id, final String nome) {
        this.id = id;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(final String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", id, nome);
    }
}