// Importa classes para escrita de arquivos e manipulação de data/hora
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Classe responsável por registrar mensagens de erro em um arquivo de log
public class GerarArquivoLog {

    // Método que registra uma mensagem de erro no arquivo "log.txt"
    public void registrarErro(String mensagem) {
        // Formata a data e hora atual
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String dataHoraFormatada = LocalDateTime.now().format(formatter);
        
        // Bloco try-with-resources para garantir o fechamento adequado do FileWriter
        try (FileWriter writer = new FileWriter("log.txt", true)) {
            // Escreve no arquivo o horário atual e a mensagem de erro
            writer.write(dataHoraFormatada + " - ERRO: " + mensagem + "\n");
        } catch (IOException e) {
            // Se houver falha ao gravar o log, exibe mensagem no console
            System.out.println("Erro ao registrar log: " + e.getMessage());
        }
    }
}
