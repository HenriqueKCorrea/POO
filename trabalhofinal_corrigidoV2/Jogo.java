import java.io.Serializable;
import java.util.Random;

public class Jogo implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum EstadoJogo { EXPLORACAO, COMBATE, GAME_OVER }

    private final Mapa mapa;
    private final Jogador jogador;
    private final Random rand = new Random();
    private boolean debugMode = false;
    private int faseAtual = 1;

    private EstadoJogo estadoAtual = EstadoJogo.EXPLORACAO;
    private Dinossauro dinossauroEmCombate = null;
    private String logMensagens = "Bem-vindo ao Sobrevivência Jurássica! Use W, A, S, D para andar.";

    public Jogo(int percepcao) {
        this.jogador = new Jogador(5, percepcao);
        this.mapa = new Mapa(20, this.jogador);
        this.mapa.gerarMapa();
    }

    public void receberComandoMovimento(int deltaLinha, int deltaColuna) {
        if (estadoAtual != EstadoJogo.EXPLORACAO || !jogador.isVivo()) return;

        int nl = jogador.getLinha() + deltaLinha;
        int nc = jogador.getColuna() + deltaColuna;

        if (!mapa.posicaoValida(nl, nc) || mapa.isParede(nl, nc)) {
            logMensagens = "Movimento inválido! Caminho bloqueado.";
            return;
        }

        Dinossauro dino = mapa.getDinossauro(nl, nc);
        if (dino != null && dino.isVivo()) {
            iniciarCombate(dino);
            return; 
        }

        jogador.setPosicao(nl, nc);
        logMensagens = "Você se moveu para " + (char)('A' + nl) + (nc + 1) + ".";

        Caixa cx = mapa.getCaixa(nl, nc);
        if (cx != null) {
            abrirCaixa(cx);
            mapa.removerCaixa(cx);
        }

        verificarPassagemDeFase();
    }

    public void atualizarCicloTempoReal() {
        if (estadoAtual == EstadoJogo.EXPLORACAO && jogador.isVivo() && !mapa.todosDinossaurosDerrotados()) {
            mapa.moverInimigos(jogador);

            for (int l = 0; l < 20; l++) {
                for (int c = 0; c < 20; c++) {
                    Dinossauro dino = mapa.getDinossauro(l, c);
                    if (dino != null && dino.isVivo() && l == jogador.getLinha() && c == jogador.getColuna()) {
                        
                        int esquiva = rand.nextInt(6) + 1; // d6
                        if (esquiva <= jogador.getPercepcao()) {
                            logMensagens = "Um " + dino.getNome() + " te encontrou! Você esquivou do golpe!";
                        } else {
                            jogador.sofrerDano(dino.getDano());
                            logMensagens = "⚠️ Um " + dino.getNome() + " te atacou e causou " + dino.getDano() + " de dano!";
                        }
                        iniciarCombate(dino);
                        break; 
                    }
                }
            }
        }

        if (!jogador.isVivo()) {
            estadoAtual = EstadoJogo.GAME_OVER;
            logMensagens = "GAME OVER! Pontuação Final: " + jogador.getPontos();
        }
    }

    private void abrirCaixa(Caixa cx) {
        if (cx.hasCompsognato()) {
            int teste = rand.nextInt(6) + 1;
            if (teste <= jogador.getPercepcao()) {
                logMensagens = "[!] Caixa: Sua percepção detectou um Compsognato e evitou a emboscada!";
            } else {
                jogador.sofrerDano(1);
                logMensagens = "[!] SURPRESA! Um Compsognato saltou da caixa e causou 1 de dano!";
            }
            return;
        }

        Item item = cx.getItem();
        if (item != null) {
            logMensagens = "[!] Encontrou uma caixa com: " + item.getNome();
            item.usar(jogador);
        }
    }

    private void iniciarCombate(Dinossauro dino) {
        this.estadoAtual = EstadoJogo.COMBATE;
        this.dinossauroEmCombate = dino;
        logMensagens = "COMBATE INICIADO contra " + dino.getNome().toUpperCase() + "! Escolha sua ação.";
    }

    public void processarAcaoCombate(String opcao) {
        if (estadoAtual != EstadoJogo.COMBATE || dinossauroEmCombate == null || !dinossauroEmCombate.isVivo()) return;

        boolean jogadorAtacou = false;

        switch (opcao) {
            case "1" -> { 
                if (jogador.hasBastao()) {
                    int dado = rand.nextInt(6) + 1;
                    if (dado > 5)       { logMensagens = "⚡ Crítico com bastão! Causou 2 de dano."; dinossauroEmCombate.sofrerDano(2); }
                    else if (dado == 1) { logMensagens = "Você errou o ataque com o bastão!"; }
                    else                { logMensagens = "Acertou com o bastão! Causou 1 de dano."; dinossauroEmCombate.sofrerDano(1); }
                } else {
                    if (!dinossauroEmCombate.podeReceberAtaqueSemArma()) {
                        logMensagens = "Impossível ferir um " + dinossauroEmCombate.getNome() + " sem armas!";
                    } else {
                        int dado = rand.nextInt(6) + 1;
                        if (dado == 6)      { logMensagens = "Crítico! Causou 2 de dano."; dinossauroEmCombate.sofrerDano(2); }
                        else if (dado <= 2) { logMensagens = "Você errou o ataque!"; }
                        else                { logMensagens = "Acertou! Causou 1 de dano."; dinossauroEmCombate.sofrerDano(1); }
                    }
                }
                jogadorAtacou = true;
            }
            case "2" -> { 
                if (jogador.getDardos() > 0) {
                    if (!dinossauroEmCombate.podeReceberDardo()) {
                        logMensagens = dinossauroEmCombate.getNome() + " é muito ágil para dardos!";
                    } else {
                        logMensagens = "Dardo certeiro! Causou 2 de dano.";
                        dinossauroEmCombate.sofrerDano(2);
                    }
                    jogador.gastarDardo();
                    jogadorAtacou = true;
                } else {
                    logMensagens = "Você não possui dardos tranquilizantes!";
                }
            }
            case "3" -> { 
                boolean fugiu = false;
                int[][] direcoes = {{-1,0},{1,0},{0,-1},{0,1}};
                for (int[] dir : direcoes) {
                    int fl = jogador.getLinha() + dir[0];
                    int fc = jogador.getColuna() + dir[1];
                    if (mapa.posicaoValida(fl, fc) && !mapa.isParede(fl, fc) && !mapa.temDinossauro(fl, fc)) {
                        jogador.setPosicao(fl, fc);
                        logMensagens = "Você fugiu da batalha!";
                        fugiu = true;
                        break;
                    }
                }
                if (!fugiu) logMensagens = "Sem saída! Você não conseguiu fugir.";
                estadoAtual = EstadoJogo.EXPLORACAO;
                dinossauroEmCombate = null;
                return;
            }
        }

        if (jogadorAtacou && dinossauroEmCombate != null && dinossauroEmCombate.isVivo()) {
            int esquiva = rand.nextInt(6) + 1;
            if (esquiva <= jogador.getPercepcao()) {
                logMensagens += " | Você esquivou do contra-ataque!";
            } else {
                int dano = dinossauroEmCombate.getDano();
                jogador.sofrerDano(dano);
                logMensagens += " | " + dinossauroEmCombate.getNome() + " contra-atacou! (" + dano + " de dano)";
            }
        }

        if (dinossauroEmCombate != null && !dinossauroEmCombate.isVivo()) {
            int pontos = dinossauroEmCombate.getDano() * 10;
            jogador.adicionarPontos(pontos);
            logMensagens = ">> " + dinossauroEmCombate.getNome() + " derrotado! (+" + pontos + " PTS)";
            mapa.removerDinossauro(dinossauroEmCombate);
            dinossauroEmCombate = null;
            estadoAtual = EstadoJogo.EXPLORACAO;
            verificarPassagemDeFase();
        }

        if (!jogador.isVivo()) {
            estadoAtual = EstadoJogo.GAME_OVER;
            logMensagens = "GAME OVER! Pontuação Final: " + jogador.getPontos();
        }
    }

    private void verificarPassagemDeFase() {
        if (jogador.isVivo() && mapa.todosDinossaurosDerrotados()) {
            faseAtual++;
            jogador.adicionarPontos(500);
            jogador.passarDeFase();
            mapa.gerarMapa();
            logMensagens = "PARABÉNS! FASE CONCLUÍDA! Preparando a Fase " + faseAtual + "...";
        }
    }

    public Mapa getMapa()                           { return this.mapa; }
    public Jogador getJogador()                     { return this.jogador; }
    public int getFaseAtual()                       { return faseAtual; }
    public EstadoJogo getEstadoAtual()              { return estadoAtual; }
    public Dinossauro getDinossauroEmCombate()      { return dinossauroEmCombate; }
    public String getLogMensagens()                 { return logMensagens; }
    public boolean isDebugMode()                    { return debugMode; }
    public void alternarDebugMode()                 { this.debugMode = !this.debugMode; }
}