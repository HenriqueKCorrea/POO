public abstract class Entidade {

    protected int saude;
    protected int linha;
    protected int coluna;

    public Entidade(int saude) {
        this.saude = saude;
    }

    public boolean isVivo() {
        return saude > 0;
    }

    public void sofrerDano(int dano) {
        saude -= dano;
    }

    public int getSaude() {
        return saude;
    }

    public void setPosicao(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }
}