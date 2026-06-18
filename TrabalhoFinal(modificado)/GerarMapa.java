import java.util.List;
import java.util.Random;

public class GerarMapa {
    // Todos os atributos imutáveis agora são final
    private final int tamanho;
    private final Celula[][] grid;
    private final Jogador jogador;
    private final List<Dinossauro> dinossauros;
    private final List<Caixa> caixas;
    private final Random rand;

    public GerarMapa(int tamanho, Celula[][] grid, Jogador jogador, List<Dinossauro> dinossauros, List<Caixa> caixas, Random rand) {
        this.tamanho = tamanho;
        this.grid = grid;
        this.jogador = jogador;
        this.dinossauros = dinossauros;
        this.caixas = caixas;
        this.rand = rand;
    }

    public void gerarMapa() {
       for (int i = 0; i < tamanho; i++) {

            for (int j = 0; j < tamanho; j++) {
        
                grid[i][j].setParede(false);
                grid[i][j].setCaixa(null);
                grid[i][j].setDinossauro(null);
            }
        }

        jogador.setPosicao(0, 0);

        for (int i = 0; i < (tamanho * tamanho * 0.1); i++) {
            int l = rand.nextInt(tamanho);
            int c = rand.nextInt(tamanho);
            if ((l != 0 || c != 0)) grid[l][c].setParede(true);
        }

        spawnDinossauro(new Compsognato(), 2);
        spawnDinossauro(new Velociraptor(), 2);
        spawnDinossauro(new Troodonte(), 5);
        
        TRex trex = new TRex();
        trex.setPosicao(tamanho - 1, tamanho - 1);

        dinossauros.add(trex);

        grid[tamanho - 1][tamanho - 1].setDinossauro(trex);

        spawnCaixa(new Caixa(new KitMedico()));
        spawnCaixa(new Caixa(new BastaoEletrico()));
        spawnCaixa(new Caixa(new DardoTranquilizante()));
        spawnCaixa(new Caixa(new DardoTranquilizante())); 
        spawnCaixa(new Caixa(true)); 
    }

    private void spawnDinossauro(Dinossauro dinoProto, int qtd) {
        for (int i = 0; i < qtd; i++) {
            int l, c;
            do {
                l = rand.nextInt(tamanho);
                c = rand.nextInt(tamanho);
            } while (grid[l][c].isParede() || (l == 0 && c == 0) || temDinossauro(l, c));
            
            Dinossauro dino;
            if (dinoProto instanceof Compsognato) dino = new Compsognato();
            else if (dinoProto instanceof Velociraptor) dino = new Velociraptor();
            else dino = new Troodonte();

            dino.setPosicao(l, c);
            dinossauros.add(dino);
            grid[l][c].setDinossauro(dino);
        }
    }

    private void spawnCaixa(Caixa caixa) {
        int l, c;
        do {
            l = rand.nextInt(tamanho);
            c = rand.nextInt(tamanho);
        } while (grid[l][c].isParede() || (l == 0 && c == 0) || temDinossauro(l, c));
        caixa.setPosicao(l, c);
        caixas.add(caixa);
        grid[l][c].setCaixa(caixa);
    }

    private boolean temDinossauro(int l, int c) {
        for (Dinossauro d : dinossauros) {
            if (d != null && d.getLinha() == l && d.getColuna() == c && d.isVivo()) {
                return true;
            }
        }
        return false;
    }
}