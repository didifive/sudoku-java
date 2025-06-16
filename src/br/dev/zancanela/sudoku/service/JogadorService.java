package br.dev.zancanela.sudoku.service;

import br.dev.zancanela.sudoku.domain.model.Jogador;
import br.dev.zancanela.sudoku.repository.JogadorRepository;

import java.util.List;

public class JogadorService {

    private final JogadorRepository jogadorRepo;

    public JogadorService(JogadorRepository jogadorRepo) {
        this.jogadorRepo = jogadorRepo;
    }

    public List<Jogador> listarJogadores() {
        return jogadorRepo.listarTodos();
    }

    private boolean existeJogador(String nome) {
        return jogadorRepo.listarTodos().stream()
                .anyMatch(j -> j.getNome().equalsIgnoreCase(nome));
    }

    public void salvar(Jogador jogador) {
        if (jogador.getNome() == null || jogador.getNome().isEmpty()) {
            throw new IllegalArgumentException("O nome do jogador não pode ser vazio.");
        }
        if (existeJogador(jogador.getNome())) {
            throw new IllegalArgumentException("Já existe um jogador com o nome [" + jogador.getNome() + "].");
        }
        jogadorRepo.salvar(jogador);
    }
}