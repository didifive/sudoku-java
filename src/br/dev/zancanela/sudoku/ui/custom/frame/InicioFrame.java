package br.dev.zancanela.sudoku.ui.custom.frame;

import br.dev.zancanela.sudoku.domain.model.Jogador;
import br.dev.zancanela.sudoku.repository.JogadorRepository;
import br.dev.zancanela.sudoku.service.JogadorService;
import br.dev.zancanela.sudoku.ui.custom.button.AdicionarJogadorButton;
import br.dev.zancanela.sudoku.ui.custom.button.SelecionarJogadorButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class InicioFrame extends JFrame {
    private final JogadorService jogadorService = new JogadorService(new JogadorRepository());
    private final JComboBox<Jogador> comboJogadores;
    private final JTextField txtNovoJogador;

    public InicioFrame() {
        setTitle("Sudoku - Seleção de Jogador");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 200);
        setLocationRelativeTo(null);

        comboJogadores = new JComboBox<>();
        atualizarListaJogadores();

        txtNovoJogador = new JTextField(15);
        AdicionarJogadorButton btnNovo = new AdicionarJogadorButton(this::adicionarJogador);

        SelecionarJogadorButton btnSelecionar = new SelecionarJogadorButton(e -> selecionarJogador());

        JPanel painel = new JPanel();
        painel.add(new JLabel("Jogadores:"));
        painel.add(comboJogadores);
        painel.add(btnSelecionar);

        JPanel painelNovo = new JPanel();
        painelNovo.add(new JLabel("Novo jogador:"));
        painelNovo.add(txtNovoJogador);
        painelNovo.add(btnNovo);

        setLayout(new BorderLayout());
        add(painel, BorderLayout.NORTH);
        add(painelNovo, BorderLayout.SOUTH);
    }

    private void atualizarListaJogadores() {
        comboJogadores.removeAllItems();
        List<Jogador> jogadores = jogadorService.listarJogadores();
        for (Jogador j : jogadores) {
            comboJogadores.addItem(j);
        }
    }

    private void adicionarJogador(ActionEvent e) {
        String nome = txtNovoJogador.getText().trim();
        try {
            jogadorService.salvar(new Jogador(nome));
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao tentar salvar jogador", JOptionPane.ERROR_MESSAGE);
            txtNovoJogador.setText("");
            return;
        }
        atualizarListaJogadores();
        txtNovoJogador.setText("");
    }

    private void selecionarJogador() {
        Jogador jogador = (Jogador) comboJogadores.getSelectedItem();
        if (jogador != null) {
            new SelecionaJogoFrame(jogador).setVisible(true);
            dispose();
        }
    }
}