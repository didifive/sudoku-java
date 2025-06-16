package br.dev.zancanela.sudoku.ui.custom.frame;

import br.dev.zancanela.sudoku.domain.enums.JogoEventEnum;
import br.dev.zancanela.sudoku.domain.event.JogoEventListener;
import br.dev.zancanela.sudoku.domain.model.Jogo;
import br.dev.zancanela.sudoku.ui.custom.panel.MatrizPanel;

import br.dev.zancanela.sudoku.ui.custom.panel.TituloTabuleiroPanel;

import javax.swing.*;

import static br.dev.zancanela.sudoku.domain.enums.JogoEventEnum.ADICIONAR_ERRO_AO_JOGO;

public class SudokuFrame extends JFrame {
    private final MatrizPanel matrizPanel;

    public SudokuFrame(Jogo jogo) {
        this.matrizPanel = new MatrizPanel(jogo);

        setTitle("Sudoku Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 750);
        setLocationRelativeTo(null);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        add(new TituloTabuleiroPanel(jogo));
        add(matrizPanel);

        setResizable(false);
        setVisible(true);
    }


}