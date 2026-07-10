public class TRex extends Dinossauro {

    public TRex() {
        super(3, "Tiranossauro Rex", 'R');
        dano = 2;
    }

    @Override
    public boolean podeReceberAtaqueSemArma() {
        return false;
    }
    
    
    @Override
    public int getMovimentos() {
        return 0;
    }
    
    
}