import java.awt.*;
import java.util.List;
import javax.swing.*;

public class TelaInicial extends JFrame {

    public TelaInicial() {
        setTitle("Sobrevivência Jurássica - Menu");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel painelTitulo = new JPanel();
        painelTitulo.setBackground(new Color(30, 30, 30));
        JLabel lblTitulo = new JLabel("<html><center>SOBREVIVÊNCIA<br>JURÁSSICA</center></html>");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(200, 220, 200));
        painelTitulo.add(lblTitulo);
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        add(painelTitulo, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new GridLayout(4, 1, 10, 15));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 60, 40, 60));

        JButton btnNovoJogo = new JButton("Novo Jogo");
        JButton btnCarregar = new JButton("Carregar Jogo");
        JButton btnRanking = new JButton("Ver Ranking");
        JButton btnSair = new JButton("Sair");

        Font fonteBotoes = new Font("Arial", Font.BOLD, 16);
        btnNovoJogo.setFont(fonteBotoes);
        btnCarregar.setFont(fonteBotoes);
        btnRanking.setFont(fonteBotoes);
        btnSair.setFont(fonteBotoes);

        btnNovoJogo.addActionListener(e -> {
            Jogo novoJogo = new Jogo(3); 
            new TelaJogo(novoJogo).setVisible(true);
            this.dispose(); 
        });

        btnCarregar.addActionListener(e -> carregarJogo());
        btnRanking.addActionListener(e -> mostrarRanking());
        btnSair.addActionListener(e -> System.exit(0));

        painelBotoes.add(btnNovoJogo);
        painelBotoes.add(btnCarregar);
        painelBotoes.add(btnRanking);
        painelBotoes.add(btnSair);

        add(painelBotoes, BorderLayout.CENTER);
    }

    private void carregarJogo() {
        Jogo jogoCarregado = GerenciadorSalvar.carregarJogo();
        if (jogoCarregado != null) {
            new TelaJogo(jogoCarregado).setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Nenhum jogo salvo encontrado ou erro ao carregar.", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void mostrarRanking() {
        List<String> ranking = GerenciadorSalvar.obterRanking();
        
        if (ranking.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O ranking está vazio!", "Ranking", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("<html><h2>🏆 TOP 5 SOBREVIVENTES 🏆</h2>");
        int limite = Math.min(5, ranking.size()); 
        
        for (int i = 0; i < limite; i++) {
            String[] partes = ranking.get(i).split(":");
            if (partes.length == 2) {
                sb.append("<b>").append(i + 1).append("º Lugar:</b> ")
                  .append(partes[0]).append(" - ").append(partes[1]).append(" PTS<br>");
            }
        }
        sb.append("</html>");

        JOptionPane.showMessageDialog(this, sb.toString(), "Ranking Geral", JOptionPane.PLAIN_MESSAGE);
    }
}