package br.dev.zancanela.sudoku.service;

import br.dev.zancanela.sudoku.domain.model.Jogador;
import br.dev.zancanela.sudoku.domain.model.Jogo;
import br.dev.zancanela.sudoku.domain.model.Tabuleiro;
import br.dev.zancanela.sudoku.repository.JogoRepository;

import java.util.List;

public class JogoService {
    private final JogoRepository jogoRepository;

    public JogoService(JogoRepository jogoRepository) {
        this.jogoRepository = jogoRepository;
    }

    public Jogo iniciarNovoJogo(final Jogador jogador, final Tabuleiro tabuleiro) {
        Jogo jogo = new Jogo(jogador, tabuleiro);
        return jogoRepository.salvar(jogo);
    }

    public Jogo atualizarJogo(final Jogo jogo) {
        return jogoRepository.salvar(jogo);
    }

    public List<Jogo> listarJogosByJogador(Jogador jogador) {
        return jogoRepository.listarJogosByJogador(jogador);
    }
}