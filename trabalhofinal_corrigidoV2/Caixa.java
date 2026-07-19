import java.io.Serializable;

public class Caixa implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int linha, coluna;
    private Item item; 
    private boolean temCompsognato; 

    public Caixa(Item item) {
        this.item = item;
        this.temCompsognato = false;
    }

    public Caixa(boolean temCompsognato) {
        this.item = null;
        this.temCompsognato = temCompsognato;
    }

    public Item getItem() { return item; }
    public boolean hasCompsognato() { return temCompsognato; }
    
    public void setPosicao(int l, int c) {
        this.linha = l;
        this.coluna = c;
    }
    
    public int getLinha() { return linha; }
    public int getColuna() { return coluna; }
}