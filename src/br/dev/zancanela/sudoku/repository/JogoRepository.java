package br.dev.zancanela.sudoku.repository;

import br.dev.zancanela.sudoku.util.DatabaseUtil;
import br.dev.zancanela.sudoku.domain.model.Jogador;
import br.dev.zancanela.sudoku.domain.model.Jogo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JogoRepository {
    public JogoRepository() {
        criarTabela();
    }

    private void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS jogo (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "jogador_id INTEGER NOT NULL," +
                "estado TEXT, " +
                "FOREIGN KEY(jogador_id) REFERENCES jogador(id))";
        try (Connection conn = DatabaseUtil.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Jogo salvar(final Jogo jogo) {
        String sql = "REPLACE INTO jogo(id, jogador_id, estado) VALUES(?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (jogo.getId() != null && jogo.getId() > 0) {
                pstmt.setLong(1, jogo.getId());
            } else {
                pstmt.setNull(1, java.sql.Types.INTEGER);
            }
            pstmt.setLong(2, jogo.getJogador().getId());
            pstmt.setString(3, jogo.serializar());
            pstmt.executeUpdate();
            if (jogo.getId() == null || jogo.getId() == 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        jogo.setId(rs.getLong(1));
                    }
                }
            }
            return jogo;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<Jogo> listarJogosByJogador(final Jogador jogador) {
        List<Jogo> jogos = new ArrayList<>();
        String sql = "SELECT id, estado FROM jogo WHERE jogador_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, jogador.getId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Jogo jogo = Jogo.deserializar(rs.getString("estado"));
                jogo.setJogador(jogador);
                jogo.setId(rs.getLong("id"));
                jogos.add(jogo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jogos;
    }
}