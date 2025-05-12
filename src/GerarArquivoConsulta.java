// Importa classes necessárias para escrita de arquivos e manipulação de data/hora
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Classe responsável por salvar os registros de conversões realizadas
public class GerarArquivoConsulta {

    // Método que salva uma conversão no arquivo "conversoes.txt"
    public void salvarConversao(String registro) {

        // Define o formato da data e hora (ex: 04/05/2025 15:00:00)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        // Pega a data e hora atual e formata como string
        String dataHora = LocalDateTime.now().format(formatter);

        // Monta a linha que será gravada no arquivo, incluindo data/hora e o conteúdo da conversão
        String linha = "[" + dataHora + "] " + registro;

        // Bloco try-with-resources para garantir que o arquivo seja fechado corretamente
        try (FileWriter writer = new FileWriter("conversoes.txt", true)) {
            // Escreve a linha no final do arquivo (modo append = true)
            writer.write(linha + "\n");
            System.out.println("Conversão salva no arquivo.");
        } catch (IOException e) {
            // Em caso de erro na escrita, exibe a mensagem correspondente
            System.out.println("Erro ao salvar conversão: " + e.getMessage());
        }
    }
}
