import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class TelaJogo extends JFrame {
    private final Jogo jogo;
    private final JButton[][] botoesGrid;
    private final int TAMANHO_MAPA = 20;

    private final JLabel lblFase;
    private final JLabel lblSaude;
    private final JLabel lblPercepcao;
    private final JLabel lblPontos;
    private final JLabel lblInventario;

    private final JTextArea txtLog;
    private final JPanel painelCombate;
    
    private Timer timerTempoReal; 

    public TelaJogo(Jogo jogo) {
        this.jogo = jogo;

        setTitle("Sobrevivência Jurássica - Área de Exploração");
        setSize(1050, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel painelMapa = new JPanel(new GridLayout(TAMANHO_MAPA, TAMANHO_MAPA));
        botoesGrid = new JButton[TAMANHO_MAPA][TAMANHO_MAPA];

        for (int l = 0; l < TAMANHO_MAPA; l++) {
            for (int c = 0; c < TAMANHO_MAPA; c++) {
                JButton btn = new JButton();
                btn.setFont(new Font("Monospaced", Font.BOLD, 11));
                btn.setFocusable(false);
                btn.setMargin(new Insets(0, 0, 0, 0));
                botoesGrid[l][c] = btn;
                painelMapa.add(btn);
            }
        }
        add(painelMapa, BorderLayout.CENTER);

        JPanel painelHUD = new JPanel();
        painelHUD.setLayout(new BoxLayout(painelHUD, BoxLayout.Y_AXIS));
        painelHUD.setBorder(BorderFactory.createTitledBorder("Status do Sobrevivente"));
        painelHUD.setPreferredSize(new Dimension(240, 700));

        lblFase       = new JLabel("Fase Atual: 1");
        lblFase.setFont(new Font("Arial", Font.BOLD, 16));
        lblSaude      = new JLabel("Saúde: ❤️ ❤️ ❤️ ❤️ ❤️");
        lblSaude.setFont(new Font("Arial", Font.PLAIN, 14));
        lblPercepcao  = new JLabel("Percepção: 0");
        lblPontos     = new JLabel("Pontos: 0");
        lblInventario = new JLabel("<html><b>Inventário:</b><br>- Kits: 0<br>- Bastão: Não<br>- Dardos: 0</html>");

        painelHUD.add(Box.createVerticalStrut(15));
        painelHUD.add(lblFase);
        painelHUD.add(Box.createVerticalStrut(15));
        painelHUD.add(lblSaude);
        painelHUD.add(Box.createVerticalStrut(10));
        painelHUD.add(lblPercepcao);
        painelHUD.add(Box.createVerticalStrut(10));
        painelHUD.add(lblPontos);
        painelHUD.add(Box.createVerticalStrut(25));
        painelHUD.add(lblInventario);

        JButton btnUsarKitFora = new JButton("Usar Kit Médico (+3 HP)");
        btnUsarKitFora.setFocusable(false);
        btnUsarKitFora.addActionListener(e -> {
            jogo.getJogador().usarKit();
            atualizarInterface();
        });
        painelHUD.add(Box.createVerticalStrut(25));
        painelHUD.add(btnUsarKitFora);

        JButton btnDebug = new JButton("Alternar Visão de Radar (Debug)");
        btnDebug.setFocusable(false);
        btnDebug.addActionListener(e -> {
            jogo.alternarDebugMode();
            atualizarInterface();
            this.requestFocusInWindow(); 
        });
        painelHUD.add(Box.createVerticalStrut(15));
        painelHUD.add(btnDebug);

        JButton btnSalvarSair = new JButton("Salvar e Sair do Jogo");
        btnSalvarSair.setFocusable(false);
        btnSalvarSair.setBackground(new Color(220, 50, 50)); 
        btnSalvarSair.setForeground(Color.WHITE);
        btnSalvarSair.addActionListener(e -> {
            if (timerTempoReal != null) {
                timerTempoReal.stop();
            }
            GerenciadorSalvar.salvarJogo(jogo);
            JOptionPane.showMessageDialog(this, 
                "Seu progresso foi salvo com sucesso!", 
                "Jogo Salvo", 
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new TelaInicial().setVisible(true); 
        });
        
        painelHUD.add(Box.createVerticalStrut(25));
        painelHUD.add(btnSalvarSair);

        add(painelHUD, BorderLayout.EAST);

        JPanel painelInferior = new JPanel(new BorderLayout(8, 8));
        painelInferior.setPreferredSize(new Dimension(1050, 140));

        txtLog = new JTextArea();
        txtLog.setEditable(false);
        txtLog.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtLog.setLineWrap(true);
        txtLog.setWrapStyleWord(true);
        txtLog.setBackground(new Color(245, 245, 245));
        JScrollPane scrollLog = new JScrollPane(txtLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Diário de Sobrevivência"));
        painelInferior.add(scrollLog, BorderLayout.CENTER);

        painelCombate = new JPanel(new GridLayout(1, 3, 5, 5));
        painelCombate.setBorder(BorderFactory.createTitledBorder("Ações de Combate"));
        painelCombate.setPreferredSize(new Dimension(420, 140));

        JButton btnAtacar = new JButton("1 - Ataque Físico");
        JButton btnDardo  = new JButton("2 - Dardo Tranquilizante");
        JButton btnFugir  = new JButton("3 - Fugir");

        btnAtacar.setFocusable(false);
        btnDardo.setFocusable(false);
        btnFugir.setFocusable(false);

        btnAtacar.addActionListener(e -> { jogo.processarAcaoCombate("1"); atualizarInterface(); this.requestFocusInWindow(); });
        btnDardo.addActionListener(e  -> { jogo.processarAcaoCombate("2"); atualizarInterface(); this.requestFocusInWindow(); });
        btnFugir.addActionListener(e  -> { jogo.processarAcaoCombate("3"); atualizarInterface(); this.requestFocusInWindow(); });

        painelCombate.add(btnAtacar);
        painelCombate.add(btnDardo);
        painelCombate.add(btnFugir);
        painelInferior.add(painelCombate, BorderLayout.EAST);

        add(painelInferior, BorderLayout.SOUTH);

        vincularControlesTeclado();

        timerTempoReal = new Timer(1500, e -> {
            if (jogo.getEstadoAtual() == Jogo.EstadoJogo.EXPLORACAO) {
                jogo.atualizarCicloTempoReal();
                atualizarInterface();
            }
        });
        timerTempoReal.start();

        atualizarInterface();
        setVisible(true);

        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    private void vincularControlesTeclado() {
        InputMap im = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = rootPane.getActionMap();

        Object[][] mapeamentos = {
            {'w', "CIMA",    -1,  0},
            {'W', "CIMA",    -1,  0},
            {'s', "BAIXO",    1,  0},
            {'S', "BAIXO",    1,  0},
            {'a', "ESQUERDA", 0, -1},
            {'A', "ESQUERDA", 0, -1},
            {'d', "DIREITA",  0,  1},
            {'D', "DIREITA",  0,  1},
        };

        for (Object[] m : mapeamentos) {
            char tecla    = (char) m[0];
            String id     = (String) m[1];
            int dl        = (int) m[2];
            int dc        = (int) m[3];

            im.put(KeyStroke.getKeyStroke(tecla), id);

            if (am.get(id) == null) {
                am.put(id, new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (jogo.getEstadoAtual() == Jogo.EstadoJogo.EXPLORACAO) {
                            jogo.receberComandoMovimento(dl, dc);
                            atualizarInterface();
                        }
                    }
                });
            }
        }
    }

    private void atualizarInterface() {
        Jogador j = jogo.getJogador();
        Mapa    m = jogo.getMapa();

        lblFase.setText("Fase Atual: " + jogo.getFaseAtual());
        lblPontos.setText("Pontos: " + j.getPontos() + " PTS");
        lblPercepcao.setText("Percepção: " + j.getPercepcao());

        StringBuilder vida = new StringBuilder("Saúde: ");
        int saudeAtual = j.getSaude();
        for (int i = 0; i < 5; i++) {
            if (i < saudeAtual) {
                vida.append("❤️ ");
            } else {
                vida.append("🖤 ");
            }
        }
        lblSaude.setText(vida.toString());

        lblInventario.setText("<html><b>Inventário:</b><br>"
            + "• Kits Médicos: " + j.getKitsMedicos() + " un<br>"
            + "• Bastão Elétrico: " + (j.hasBastao() ? "⚡ EQUIPADO" : "❌ NENHUM") + "<br>"
            + "• Munição de Dardos: " + j.getDardos() + " un</html>");

        txtLog.setText(jogo.getLogMensagens());

        boolean emCombate = jogo.getEstadoAtual() == Jogo.EstadoJogo.COMBATE;
        painelCombate.setVisible(emCombate);
        if (emCombate) {
            Dinossauro alvo = jogo.getDinossauroEmCombate();
            if (alvo != null) {
                painelCombate.setBorder(BorderFactory.createTitledBorder(
                    "EM COMBATE: " + alvo.getNome().toUpperCase() + " [HP: " + alvo.getSaude() + "]"
                ));
            }
        }

        int jl = j.getLinha();
        int jc = j.getColuna();

        for (int l = 0; l < TAMANHO_MAPA; l++) {
            for (int c = 0; c < TAMANHO_MAPA; c++) {
                JButton campo = botoesGrid[l][c];
                campo.setForeground(Color.BLACK);

                if (l == jl && c == jc) {
                    campo.setText("P");
                    campo.setBackground(Color.CYAN);

                } else if (m.isParede(l, c)) {
                    campo.setText("█");
                    campo.setBackground(Color.DARK_GRAY);
                    campo.setForeground(Color.WHITE);

                } else {
                    boolean visivel = jogo.isDebugMode() || isNaLinhaDeVisao(m, jl, jc, l, c);

                    Dinossauro dino = m.getDinossauro(l, c);
                    Caixa      cx   = m.getCaixa(l, c);

                    if (visivel && dino != null && dino.isVivo()) {
                        campo.setText(String.valueOf(dino.getSimbolo()));
                        campo.setBackground(Color.RED);
                        campo.setForeground(Color.WHITE);

                    } else if (visivel && cx != null) {
                        campo.setText("X");
                        campo.setBackground(Color.ORANGE);

                    } else if (visivel) {
                        campo.setText(".");
                        campo.setBackground(new Color(200, 220, 200)); 

                    } else {
                        campo.setText("?");
                        campo.setBackground(new Color(80, 80, 80));    
                        campo.setForeground(Color.GRAY);
                    }
                }
            }
        }

        this.revalidate();
        this.repaint();

        if (jogo.getEstadoAtual() == Jogo.EstadoJogo.GAME_OVER) {
            if (timerTempoReal != null) timerTempoReal.stop();
            salvarPontuacaoNoRanking(j.getPontos());
            
            JOptionPane.showMessageDialog(this,
                "Você não resistiu aos perigos da ilha!\nPontuação Final: " + j.getPontos() + " pontos.",
                "FIM DE JOGO",
                JOptionPane.ERROR_MESSAGE
            );
            dispose();
            new TelaInicial().setVisible(true);
        }

        if (jogo.getEstadoAtual() == Jogo.EstadoJogo.EXPLORACAO && m.todosDinossaurosDerrotados()) {
            if (timerTempoReal != null) timerTempoReal.stop();
            salvarPontuacaoNoRanking(j.getPontos());
            
            JOptionPane.showMessageDialog(this,
                "VITÓRIA! Todos os dinossauros foram eliminados!\nPontuação Final: " + j.getPontos() + " pontos.",
                "VOCÊ SOBREVIVEU!",
                JOptionPane.INFORMATION_MESSAGE
            );
            dispose();
            new TelaInicial().setVisible(true);
        }
    }

    private boolean isNaLinhaDeVisao(Mapa m, int jl, int jc, int l, int c) {
        if (l == jl && c == jc) return true;

        if (l == jl) {
            int minC = Math.min(c, jc);
            int maxC = Math.max(c, jc);
            for (int ci = minC + 1; ci < maxC; ci++) {
                if (m.isParede(l, ci) || m.temDinossauro(l, ci) || m.temCaixa(l, ci)) return false;
            }
            return true;
        }

        if (c == jc) {
            int minL = Math.min(l, jl);
            int maxL = Math.max(l, jl);
            for (int li = minL + 1; li < maxL; li++) {
                if (m.isParede(li, c) || m.temDinossauro(li, c) || m.temCaixa(li, c)) return false;
            }
            return true;
        }

        return false; 
    }

    private void salvarPontuacaoNoRanking(int pontos) {
        String nome = JOptionPane.showInputDialog(this, "Sobrevivente, digite seu nome para o ranking:");
        if (nome == null || nome.trim().isEmpty()) {
            nome = "Desconhecido";
        }
        GerenciadorSalvar.registrarPontuacao(nome, pontos);
    }
}