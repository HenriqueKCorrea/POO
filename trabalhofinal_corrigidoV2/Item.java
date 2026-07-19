import java.io.Serializable;

public abstract class Item implements Usavel, Serializable {
    private static final long serialVersionUID = 1L;
    protected String nome;

    public Item(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}