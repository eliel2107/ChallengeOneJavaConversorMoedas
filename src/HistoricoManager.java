import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Classe responsável por gerenciar o histórico de conversões
public class HistoricoManager {
    
    private static final String ARQUIVO_HISTORICO = "conversoes.txt";
    
    // Método que lê o histórico de conversões do arquivo
    public List<String> lerHistorico() throws IOException {
        List<String> historico = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_HISTORICO))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                historico.add(linha);
            }
        }
        
        return historico;
    }
    
    // Método que retorna as últimas N conversões
    public List<String> obterUltimasConversoes(int quantidade) throws IOException {
        List<String> todasConversoes = lerHistorico();
        
        int inicio = Math.max(0, todasConversoes.size() - quantidade);
        return todasConversoes.subList(inicio, todasConversoes.size());
    }
    
    // Método que busca conversões por moeda
    public List<String> buscarPorMoeda(String codigoMoeda) throws IOException {
        List<String> resultado = new ArrayList<>();
        List<String> todasConversoes = lerHistorico();
        
        for (String conversao : todasConversoes) {
            if (conversao.contains(" " + codigoMoeda + " ")) {
                resultado.add(conversao);
            }
        }
        
        return resultado;
    }
}
