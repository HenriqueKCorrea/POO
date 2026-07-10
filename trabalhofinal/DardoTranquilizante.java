public class DardoTranquilizante extends Item {
    public DardoTranquilizante() {
        super("Dardo Tranquilizante");
    }
    
    @Override
    public void usar(Jogador jogador){
    jogador.addDardo();
    System.out.println("Voce recebeu um Dardo Tranquilizante!");
    }
}