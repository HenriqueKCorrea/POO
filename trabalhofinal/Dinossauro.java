public abstract class Dinossauro extends Entidade {

    protected String nome;
    protected char simbolo;
    protected int dano;

    public Dinossauro(int saude, String nome, char simbolo) {
        super(saude);
        this.nome = nome;
        this.simbolo = simbolo;
    }

    public String getNome() {
        return nome;
    }

    public char getSimbolo() {
        return simbolo;
    }

    public int getDano() {
        return dano;
    }

    // Pode ser atingido por dardos?
    public boolean podeReceberDardo() {
        return true;
    }

    // Pode ser ferido sem bastão?
    public boolean podeReceberAtaqueSemArma() {
        return true;
    }
    
    
    public int getMovimentos() {
    return 1;
    }
    
}