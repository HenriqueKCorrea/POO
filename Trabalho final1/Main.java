import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("========================================");
        System.out.println("   BEM-VINDO A SOBREVIVENCIA JURASSICA  ");
        System.out.println("========================================");
        System.out.println("1 - Jogar");
        System.out.println("2 - Sair");
        System.out.print("Escolha uma opcao: ");

        String opcao = scanner.nextLine();

        if (opcao.equals("1")) {
            iniciarJogo(scanner);
        } else {
            System.out.println("Saindo...");
        }
    }

    private static void iniciarJogo(Scanner scanner) {

        System.out.println("Escolha a dificuldade:");
        System.out.println("1 - Facil (3 de percepcao)");
        System.out.println("2 - Medio (2 de percepcao)");
        System.out.println("3 - Dificil (1 de percepcao)");

        int percepcao = 3;

        String dif = scanner.nextLine();

        if (dif.equals("2"))
            percepcao = 2;
        else if (dif.equals("3"))
            percepcao = 1;

        Jogo jogo = new Jogo(percepcao);
        jogo.loopPrincipal(scanner);
    }
}