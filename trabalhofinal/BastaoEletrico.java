public class BastaoEletrico extends Item {
    public BastaoEletrico() {
        super("Bastao Eletrico");
    }
    
    @Override
    public void usar(Jogador jogador){
        jogador.setBastao(true);
        System.out.println("Voce recebeu um Bastao Eletrico!");
    }
}