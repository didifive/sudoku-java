package br.dev.zancanela.sudoku.domain.model;

import br.dev.zancanela.sudoku.domain.enums.JogoEventEnum;
import br.dev.zancanela.sudoku.domain.enums.JogoStatusEnum;
import br.dev.zancanela.sudoku.domain.event.JogoEventListener;
import br.dev.zancanela.sudoku.domain.event.JogoNotifierService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static br.dev.zancanela.sudoku.domain.enums.JogoEventEnum.ADICIONAR_ERRO_AO_JOGO;
import static br.dev.zancanela.sudoku.domain.enums.JogoEventEnum.JOGO_LIMPO;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public final class Jogo {
    private final JogoNotifierService notifier = new JogoNotifierService();
    private Long id;
    private Tabuleiro tabuleiro;
    private Jogador jogador;
    private JogoStatusEnum status;
    private LocalDateTime dataHoraInicio;
    private int erros = 0;
    private boolean modoRascunho = false;

    public Jogo() {
    }

    public Jogo(Jogador jogador, Tabuleiro tabuleiro) {
        this.jogador = jogador;
        this.tabuleiro = tabuleiro;
        this.status = JogoStatusEnum.NAO_INICIADO;
        this.dataHoraInicio = LocalDateTime.now();
        this.modoRascunho = false;
    }

    public static Jogo deserializar(final String estado) {
        Jogo jogo = new Jogo();
        String[] linhas = estado.split("\n");
        for (String linha : linhas) {
            if (linha.startsWith("Status: ")) {
                String statusName = linha.substring(8).trim();
                jogo.status = JogoStatusEnum.valueOf(statusName);
            } else if (linha.startsWith("DataHoraInicio: ")) {
                String dataStr = linha.substring(16).trim();
                jogo.dataHoraInicio = java.time.LocalDateTime.parse(dataStr, ISO_LOCAL_DATE_TIME);
            } else if (linha.startsWith("Erros: ")) {
                jogo.erros = Integer.parseInt(linha.substring(7).trim());
            } else if (linha.startsWith("Tabuleiro:")) {
                List<List<Celula>> celulas = new ArrayList<>();
                int idx = Arrays.asList(linhas).indexOf("Tabuleiro:") + 1;
                for (; idx < linhas.length; idx++) {
                    String l = linhas[idx].trim();
                    if (l.isEmpty()) continue;
                    String[] valores = l.split(";");
                    List<Celula> linhaCelulas = new ArrayList<>();
                    for (String v : valores) {
                        if (!v.isEmpty()) {
                            String[] partesCelula = v.split(",");
                            int valor = Integer.parseInt(partesCelula[0]);
                            boolean fixa = Boolean.parseBoolean(partesCelula[1]);
                            int esperado = Integer.parseInt(partesCelula[2]);
                            linhaCelulas.add(new Celula(valor, fixa, esperado));
                        }
                    }
                    celulas.add(linhaCelulas);
                }
                jogo.tabuleiro = new Tabuleiro(celulas);
            }
        }
        return jogo;
    }

    public void addJogoEventListener(JogoEventEnum event, JogoEventListener listener) {
        notifier.subscribe(event, listener);
    }

    public JogoNotifierService getNotifier() {
        return notifier;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public JogoStatusEnum getStatus() {
        return status;
    }

    public void setStatusConcluido() {
        this.status = JogoStatusEnum.COMPLETO;
    }

    public void setStatusIniciado() {
        this.status = JogoStatusEnum.INCOMPLETO;
    }

    public boolean verificarTabuleiro() {
        boolean tabuleiroCompleto = true;
        for (List<Celula> linha : tabuleiro.celulas()) {
            for (Celula celula : linha) {
                if (celula.getValor() == 0 || celula.getValor() != celula.getEsperado()) {
                    incrementarErro();
                    return false;
                }
            }
        }
        return tabuleiroCompleto;
    }

    public String getDataHoraInicio() {
        return dataHoraInicio.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public int getErros() {
        return erros;
    }

    public void incrementarErro() {
        erros++;
        notifier.notify(ADICIONAR_ERRO_AO_JOGO, this.erros);
    }

    public boolean isModoRascunho() {
        return modoRascunho;
    }

    public void setModoRascunho(boolean ativo) {
        this.modoRascunho = ativo;
        notifier.notify(ADICIONAR_ERRO_AO_JOGO, this.erros);
    }

    public String serializar() {
        StringBuilder sb = new StringBuilder();
        sb.append("Status: ").append(status.name()).append("\n");
        sb.append("DataHoraInicio: ").append(dataHoraInicio.format(ISO_LOCAL_DATE_TIME)).append("\n");
        sb.append("Erros: ").append(erros).append("\n");
        sb.append("Tabuleiro:\n");
        for (List<Celula> linha : tabuleiro.celulas()) {
            for (int i = 0; i < linha.size(); i++) {
                Celula celula = linha.get(i);
                sb.append(celula.getValor())
                        .append(",")
                        .append(celula.isFixa())
                        .append(",")
                        .append(celula.getEsperado());
                if (i < linha.size() - 1) sb.append(";");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "(" +
                id +
                ") Início: " + getDataHoraInicio() + " - " +
                "Status: " + status.getLabel();
    }

    public void limparCelulasNaoFixas() {
        for (List<Celula> linha : tabuleiro.celulas()) {
            for (Celula celula : linha) {
                if (!celula.isFixa()) {
                    celula.setValor(0);
                }
            }
        }
        notifier.notify(JOGO_LIMPO, 0);
    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }
}