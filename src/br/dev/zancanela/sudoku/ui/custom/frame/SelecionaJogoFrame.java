package br.dev.zancanela.sudoku.ui.custom.frame;

import br.dev.zancanela.sudoku.domain.model.Jogador;
import br.dev.zancanela.sudoku.domain.model.Jogo;
import br.dev.zancanela.sudoku.domain.model.Tabuleiro;
import br.dev.zancanela.sudoku.repository.JogoRepository;
import br.dev.zancanela.sudoku.service.JogoService;
import br.dev.zancanela.sudoku.util.TabuleiroUtil;
import br.dev.zancanela.sudoku.ui.custom.button.NovoJogoButton;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SelecionaJogoFrame extends JFrame {
    private final transient JogoService jogoService = new JogoService(new JogoRepository());
    private final transient Jogador jogador;

    public SelecionaJogoFrame(Jogador jogador) {
        this.jogador = jogador;
        setTitle("Sudoku - Jogos de " + jogador.getNome());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 200);
        setLocationRelativeTo(null);

        List<Jogo> jogos = jogoService.listarJogosByJogador(jogador);

        JPanel painel = new JPanel();

        JButton btnNovo = new NovoJogoButton(e -> iniciarNovoJogo());
        painel.add(btnNovo);

        if (jogos != null && !jogos.isEmpty()) {
            DefaultListModel<Jogo> model = new DefaultListModel<>();
            jogos.forEach(model::addElement);
            JList<Jogo> listaJogos = new JList<>(model);
            listaJogos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scroll = new JScrollPane(listaJogos);
            scroll.setPreferredSize(new Dimension(300, 100));
            painel.add(scroll);

            JButton btnContinuar = new JButton("Continuar Tabuleiro Selecionado");
            btnContinuar.addActionListener(e -> {
                Jogo selecionado = listaJogos.getSelectedValue();
                if (selecionado != null) {
                    continuarJogo(selecionado);
                }
            });
            painel.add(btnContinuar);
        }

        add(painel, BorderLayout.CENTER);
    }

    private void iniciarNovoJogo() {
        Tabuleiro tabuleiro = TabuleiroUtil.iniciaNovoTabuleiro();
        Jogo novoJogo = jogoService.iniciarNovoJogo(jogador, tabuleiro);
        new SudokuFrame(novoJogo).setVisible(true);
        dispose();
    }

    private void continuarJogo(Jogo jogoSalvo) {
        new SudokuFrame(jogoSalvo).setVisible(true);
        dispose();
    }
}