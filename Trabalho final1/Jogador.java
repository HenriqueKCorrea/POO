public class Jogador extends Entidade {

    private final  int percepcao;
    private int kitsMedicos;
    private boolean bastao;
    private int dardos;

    public Jogador(int saude, int percepcao) {
        super(saude);
        this.percepcao = percepcao;
    }

    public int getPercepcao() {
        return percepcao;
    }

    public int getKitsMedicos() {
        return kitsMedicos;
    }

    public void addKit() {
        kitsMedicos++;
    }

    public void usarKit() {
        if (kitsMedicos > 0) {
            saude = Math.min(5, saude + 3);
            kitsMedicos--;
        }
    }

    public boolean hasBastao() {
        return bastao;
    }

    public void setBastao(boolean bastao) {
        this.bastao = bastao;
    }

    public int getDardos() {
        return dardos;
    }

    public void addDardo() {
        dardos++;
    }

    public void gastarDardo() {
        if (dardos > 0)
            dardos--;
    }
}