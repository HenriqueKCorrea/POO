public class KitMedico extends Item {
    public KitMedico() {
        super("Kit Medico");
    }
    
    @Override
    public void usar(Jogador jogador){
        jogador.addKit();
        System.out.println("Kit Medico adicionado ao inventario!");
    }
}