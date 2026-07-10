import javax.swing.*;

public class TelaJogo extends JFrame{

    private Jogo jogo;

    public TelaJogo(Jogo jogo){

        this.jogo = jogo;

        setTitle("Sobrevivência Jurássica");

        setSize(900,700);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel texto = new JLabel("Aqui ficará o jogo.");

        texto.setHorizontalAlignment(SwingConstants.CENTER);

        add(texto);

        setVisible(true);

    }

}
