public class Velociraptor extends Dinossauro {

    public Velociraptor() {
        super(2, "Velociraptor", 'V');
        dano = 1;
    }

    @Override
    public boolean podeReceberDardo() {
        return false;
    }
    
    @Override
    public int getMovimentos() {
        return 2;
    }
    
}