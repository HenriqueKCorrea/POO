import javax.swing.*;
import java.awt.*;

public class TelaDificuldade extends JFrame {

    public TelaDificuldade() {

        setTitle("Escolha a dificuldade");

        setSize(400,250);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new GridLayout(4,1));

        JLabel texto = new JLabel("Escolha a dificuldade");

        texto.setHorizontalAlignment(SwingConstants.CENTER);

        add(texto);

        JButton facil = new JButton("Fácil");
        JButton medio = new JButton("Médio");
        JButton dificil = new JButton("Difícil");

        add(facil);
        add(medio);
        add(dificil);

        facil.addActionListener(e -> iniciar(3));
        medio.addActionListener(e -> iniciar(2));
        dificil.addActionListener(e -> iniciar(1));

        setVisible(true);
    }

    private void iniciar(int percepcao) {

        dispose();

        Jogo jogo = new Jogo(percepcao);

        new TelaJogo(jogo);

    }

}