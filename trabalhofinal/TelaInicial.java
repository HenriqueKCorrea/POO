import javax.swing.*;
import java.awt.*;

public class TelaInicial extends JFrame {

    public TelaInicial() {

        setTitle("Sobrevivência Jurássica");

        setSize(500,300);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("SOBREVIVÊNCIA JURÁSSICA");

        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        titulo.setFont(new Font("Arial", Font.BOLD,24));

        add(titulo,BorderLayout.NORTH);

        JPanel painel = new JPanel();
        getContentPane().setBackground(new Color(34, 139, 34));
        painel.setBackground(new Color(34, 139, 34));
        JButton jogar = new JButton("Jogar");

        JButton sair = new JButton("Sair");

        painel.add(jogar);

        painel.add(sair);

        add(painel,BorderLayout.CENTER);

        jogar.addActionListener(e -> {

            dispose();

            new TelaDificuldade();

        });

        sair.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

}
