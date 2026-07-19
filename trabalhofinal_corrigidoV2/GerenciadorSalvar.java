import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorSalvar {

    private static final String ARQUIVO_SAVE = "savegame.dat";
    private static final String ARQUIVO_RANKING = "ranking.txt";

    // --- SISTEMA DE SALVAMENTO DO JOGO ---

    public static void salvarJogo(Jogo jogo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_SAVE))) {
            oos.writeObject(jogo);
            System.out.println("Progresso salvo com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar o jogo: " + e.getMessage());
        }
    }

    public static Jogo carregarJogo() {
        File arquivo = new File(ARQUIVO_SAVE);
        if (!arquivo.exists()) {
            System.out.println("Nenhum arquivo de save encontrado.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (Jogo) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar o jogo: " + e.getMessage());
            return null;
        }
    }

    // --- SISTEMA DE RANKING ---

    public static void registrarPontuacao(String nomeJogador, int pontos) {
        // Salva no formato "Nome:Pontos"
        try (FileWriter fw = new FileWriter(ARQUIVO_RANKING, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            
            out.println(nomeJogador + ":" + pontos);
            System.out.println("Pontuação registrada no ranking!");
        } catch (IOException e) {
            System.err.println("Erro ao registrar ranking: " + e.getMessage());
        }
    }

    public static List<String> obterRanking() {
        List<String> listaRanking = new ArrayList<>();
        File arquivo = new File(ARQUIVO_RANKING);

        if (!arquivo.exists()) return listaRanking;

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                listaRanking.add(linha);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler ranking: " + e.getMessage());
        }

        // Ordena o ranking do maior ponto para o menor
        listaRanking.sort((o1, o2) -> {
            int pontos1 = Integer.parseInt(o1.split(":")[1]);
            int pontos2 = Integer.parseInt(o2.split(":")[1]);
            return Integer.compare(pontos2, pontos1); // Decrescente
        });

        return listaRanking;
    }
}   ;
