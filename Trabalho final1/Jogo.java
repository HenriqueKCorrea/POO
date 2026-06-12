import java.util.Random;
import java.util.Scanner;

public class Jogo {
    // Todos os atributos agora seguem a recomendação 'can be final'
    private final Mapa mapa;
    private final Jogador jogador;
    private final Random rand = new Random();
    private boolean debugMode = false;

    public Jogo(int percepcao) {
        this.jogador = new Jogador(5, percepcao);
        this.mapa = new Mapa(20, jogador);
        this.mapa.gerarMapa();
    }

    public void loopPrincipal(Scanner scanner) {
        boolean rodando = true;
        
        while (rodando && jogador.isVivo() && !mapa.todosDinossaurosDerrotados()) {
            mapa.imprimirMapa(debugMode);
            
            System.out.println("\n--- STATUS ---");
            System.out.println("Saude: " + jogador.getSaude() + " | Percepcao: " + jogador.getPercepcao());
            System.out.println("Inventario: Kits Medicos (" + jogador.getKitsMedicos() + ") | Bastao Eletrico (" + (jogador.hasBastao() ? "Sim" : "Nao") + ") | Dardos (" + jogador.getDardos() + ")");
            
            System.out.println("\n--- ACOES ---");
            System.out.println("W/A/S/D - Movimentar");
            System.out.println("C - Curar (Gasta 1 Kit Medico)");
            System.out.println("X - DEBUG MODE (" + (debugMode ? "ATIVADO" : "DESATIVADO") + ")");
            System.out.println("SAIR - Encerrar o jogo");
            System.out.print("Escolha uma acao: ");
            
            String acao = scanner.nextLine().toUpperCase();
            int nl = jogador.getLinha();
            int nc = jogador.getColuna();
            boolean moveu = false;

            // Correção: Switch tradicional convertido para Rule Switch moderno (->)
            switch (acao) {
                case "W" -> { nl--; moveu = true; }
                case "S" -> { nl++; moveu = true; }
                case "A" -> { nc--; moveu = true; }
                case "D" -> { nc++; moveu = true; }
                case "C" -> jogador.usarKit();
                case "X" -> debugMode = !debugMode;
                case "SAIR" -> rodando = false;
                default  -> System.out.println("Opcao invalida.");
            }

            if (moveu) {
                if (mapa.posicaoValida(nl, nc) && !mapa.isParede(nl, nc)) {
                    jogador.setPosicao(nl, nc);
                    
                    // Tratamento de Caixas no local visitado
                    Caixa cx = mapa.getCaixa(nl, nc);
                    if (cx != null) {
                        abrirCaixa(cx);
                        mapa.removerCaixa(cx);
                    }
                    
                    // Tratamento de Combate no local visitado
                    Dinossauro dino = mapa.getDinossauro(nl, nc);
                    if (dino != null && dino.isVivo()) {
                        iniciarCombate(dino, scanner);
                    }

                    // Turno de movimentação dos dinossauros do mapa
                    if (jogador.isVivo() && !mapa.todosDinossaurosDerrotados()) {
                        mapa.moverInimigos(jogador);
                    }
                } else {
                    System.out.println("Movimento invalido! Alvo fora dos limites ou bloqueado por parede (#).");
                }
            }
        }

        // Mensagens de encerramento
        if (!jogador.isVivo()) {
            System.out.println("\n========================================");
            System.out.println("   VOCE MORREU! FIM DE JOGO JURASSICO.  ");
            System.out.println("========================================");
        } else if (mapa.todosDinossaurosDerrotados()) {
            System.out.println("\n========================================");
            System.out.println(" PARABENS! TODOS OS DINOSSAUROS FORAM  ");
            System.out.println(" DERROTADOS. VOCE ELEVOU A SOBREVIVENCIA ");
            System.out.println("========================================");
        }
    }

    // Correção: Extraído para evitar poluição visual e chain de if/else usando Switch moderno
    private void abrirCaixa(Caixa cx) {
        System.out.println("\n[!] Voce encontrou uma caixa de suprimentos!");
        
        if (cx.hasCompsognato()) {
            System.out.println("SURPRESA! Era uma armadilha! Um Compsognato salta de dentro da caixa!");
            jogador.sofrerDano(1);
            System.out.println("Voce nao reagiu a tempo do susto e sofreu 1 de dano!");
            return;
        }

        Item item = cx.getItem();
        if (item != null) {
            System.out.println("Voce abriu a caixa e encontrou: " + item.getNome());
            
            // Substitui a antiga corrente de 'if (item instanceof ...)' por Switch de String
            switch (item.getNome()) {
                case "Kit Medico"          -> jogador.addKit();
                case "Bastao Eletrico"     -> jogador.setBastao(true);
                case "Dardo Tranquilizante" -> jogador.addDardo();
                default                    -> System.out.println("Item desconhecido no inventario.");
            }
        }
    }

    // Lógica interna do combate isolada e protegida com Rule Switches
    private void iniciarCombate(Dinossauro dino, Scanner scanner) {
        System.out.println("\n========================================");
        System.out.println("  COMBATE INICIADO EM CHEIO: " + dino.getNome().toUpperCase());
        System.out.println("========================================");
        
        boolean emCombate = true;
        while (emCombate && dino.isVivo() && jogador.isVivo()) {
            System.out.println("\nVida do Inimigo [" + dino.getNome() + "]: " + dino.getSaude());
            System.out.println("Sua Saude Atual: " + jogador.getSaude());
            System.out.println("1 - Atacar com Bastao Eletrico (" + (jogador.hasBastao() ? "Equipado" : "Nao Possui") + ")");
            System.out.println("2 - Atirar Dardo Tranquilizante (" + jogador.getDardos() + " restantes)");
            System.out.println("3 - Fugir");
            System.out.print("Escolha sua acao de combate: ");
            
            String acaoCombate = scanner.nextLine();
            boolean atacou = false;

            switch (acaoCombate) {
                case "1" -> {
                    if (jogador.hasBastao()) {
                        System.out.println("Voce desfere um golpe energizado! O " + dino.getNome() + " sofreu 1 de dano.");
                        dino.sofrerDano(1);
                        atacou = true;
                    } else {
                        System.out.println("Voce nao possui um Bastao Eletrico!");
                    }
                }
                case "2" -> {
                    if (jogador.getDardos() > 0) {
                        if (rand.nextInt(4) == 0) { 
                            System.out.println("Voce errou o dardo!");
                        } else {
                            System.out.println("Dardo atingiu o alvo em cheio! Dano massivo (2)!");
                            dino.sofrerDano(2);
                        }
                        jogador.gastarDardo();
                        atacou = true;
                    } else {
                        System.out.println("Voce nao tem dardos!");
                    }
                }
                case "3" -> {
                    System.out.println("Voce recua um passo e foge do combate!");
                    emCombate = false;
                }
                default -> System.out.println("Opcao invalida.");
            }

            // Contra-ataque do Dinossauro caso ele continue de pé
            if (atacou && dino.isVivo()) {
                System.out.println("\n-> O " + dino.getNome() + " ataca!");
                int esquiva = rand.nextInt(3) + 1;
                
                if (esquiva <= jogador.getPercepcao()) {
                    System.out.println("Ufa! Voce se esquivou rapidamente do golpe.");
                } else {
                    int danoInimigo = (dino instanceof TRex) ? 2 : 1;
                    jogador.sofrerDano(danoInimigo);
                    System.out.println("Voce foi atingido! Sofreu " + danoInimigo + " de dano.");
                }
            }
        }

        if (!dino.isVivo()) {
            System.out.println("\n>> O " + dino.getNome() + " foi completamente derrotado!");
            mapa.removerDinossauro(dino);
        }
    }
}