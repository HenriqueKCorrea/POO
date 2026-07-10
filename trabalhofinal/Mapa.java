import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mapa {
    // Atributos marcados como final e correção de estruturas ifs para Switch moderno
    private final int tamanho;
    private final Celula[][] grid;
    private final Jogador jogador;
    private final List<Dinossauro> dinossauros = new ArrayList<>();
    private final List<Caixa> caixas = new ArrayList<>();
    private final Random rand = new Random();

    public Mapa(int tamanho, Jogador jogador) {
        this.tamanho = tamanho;
        this.grid = new Celula[tamanho][tamanho];

        for(int i = 0; i < tamanho; i++){
        
            for(int j = 0; j < tamanho; j++){
        
                grid[i][j] = new Celula();
            }
        }
        
        this.jogador = jogador;
    }

    public void gerarMapa() {
        GerarMapa gerador = new GerarMapa(tamanho, grid, jogador, dinossauros, caixas, rand);
        gerador.gerarMapa();
    }

    public void imprimirMapa(boolean debugMode) {
        System.out.println("\n  " + "0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 ".substring(0, tamanho*2));
        for (int i = 0; i < tamanho; i++) {
            System.out.print((char)('A' + i) + " ");
            for (int j = 0; j < tamanho; j++) {
                if (jogador.getLinha() == i && jogador.getColuna() == j) {
                    System.out.print("P ");
                } else if (debugMode || isNaLinhaDeVisao(i, j)) {
                    if (grid[i][j].isParede()) {
                        System.out.print("# ");
                    } else if (temDinossauro(i, j)) {
                        System.out.print(getDinossauro(i, j).getSimbolo() + " ");
                    } else if (temCaixa(i, j)) {
                        System.out.print("X ");
                    } else {
                        System.out.print(". ");
                    }
                } else {
                    System.out.print("~ ");
                }
            }
            System.out.println();
        }
    }

    private boolean isNaLinhaDeVisao(int l, int c) {
        int jl = jogador.getLinha();
        int jc = jogador.getColuna();

        if (l == jl && c == jc) return true;

        if (l == jl) {
            int min = Math.min(c, jc);
            int max = Math.max(c, jc);
            for (int i = min + 1; i < max; i++) {
                if (isParede(l, i) || temDinossauro(l, i) || temCaixa(l, i)) return false;
            }
            return true;
        } else if (c == jc) {
            int min = Math.min(l, jl);
            int max = Math.max(l, jl);
            for (int i = min + 1; i < max; i++) {
                if (isParede(i, c) || temDinossauro(i, c) || temCaixa(i, c)) return false;
            }
            return true;
        }
        return false;
    }

    public boolean posicaoValida(int l, int c) { return l >= 0 && l < tamanho && c >= 0 && c < tamanho; }
    public boolean isParede(int l, int c) {
    return grid[l][c].isParede();
    }

    public boolean temDinossauro(int l, int c) {  return grid[l][c].getDinossauro() != null; }
    
    public Dinossauro getDinossauro(int l, int c) {

        return grid[l][c].getDinossauro();
    }
    
    public void removerDinossauro(Dinossauro d) {
        grid[d.getLinha()][d.getColuna()]
        .setDinossauro(null);

        dinossauros.remove(d);
    }

    public boolean temCaixa(int l, int c) { return grid[l][c].getCaixa() != null; }
    
    public Caixa getCaixa(int l, int c) {
         return grid[l][c].getCaixa();
        }
        
    
    public void removerCaixa(Caixa cx) {
            grid[cx.getLinha()][cx.getColuna()]
        .setCaixa(null);

        caixas.remove(cx);
    }

    public boolean todosDinossaurosDerrotados() { return dinossauros.isEmpty(); }

    public void moverInimigos(Jogador jogador) {
        for (Dinossauro d : dinossauros) {
            // Correção do "Null Pointer": Adicionado d == null para segurança externa
            if(d == null || !d.isVivo())
            continue;
            int movs = d.getMovimentos();
            if(movs ==0)
            continue;
            for (int i = 0; i < movs; i++) {
                int dir = rand.nextInt(4);
                int nl = d.getLinha();
                int nc = d.getColuna();
                
                // Correção do "Replace chain of ifs with switch" usando Rule Switch moderna (->)
                switch (dir) {
                    case 0 -> nl--;
                    case 1 -> nl++;
                    case 2 -> nc--;
                    case 3 -> nc++;
                }

                if (posicaoValida(nl, nc) && !isParede(nl, nc) && !temDinossauro(nl, nc)) {
                    int antigaL = d.getLinha();
                    int antigaC = d.getColuna();
                    
                    grid[antigaL][antigaC].setDinossauro(null);
                    
                    d.setPosicao(nl, nc);
                    
                    grid[nl][nc].setDinossauro(d);

                    if (d.getLinha() == jogador.getLinha() &&
                        d.getColuna() == jogador.getColuna()) {
                    
                        System.out.println(
                            "\n*** UM INIMIGO SURGE DAS SOMBRAS! ***"
                        );
                    
                        int esquiva = rand.nextInt(10) + 1;
                    
                        if(esquiva <= jogador.getPercepcao()){
                    
                            System.out.println(
                                "Voce desviou do ataque!"
                            );
                    
                        }else{
                    
                            jogador.sofrerDano(d.getDano());
                    
                            System.out.println(
                                "Voce sofreu "
                                + d.getDano()
                                + " de dano!"
                            );
                        }
                    }
                }
            }
        }
    }
}