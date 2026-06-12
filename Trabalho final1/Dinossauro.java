public abstract class Dinossauro extends Entidade {

    protected String nome;
    protected char simbolo;

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
}