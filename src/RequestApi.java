// Importa classes do Gson para manipulação de JSON
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

// Importa classes modernas para HTTP do pacote java.net.http
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// Classe responsável por fazer a requisição à API de câmbio
public class RequestApi {
    // Cliente HTTP reutilizável para todas as requisições
    private final HttpClient httpClient;
    
    // Chave de API para autenticação no serviço
    private final String apiKey = "090e1715799e2fcfbf7fbe8c";
    
    // Cache para armazenar taxas de câmbio e reduzir chamadas à API
    private final Map<String, CacheItem> cacheRates = new HashMap<>();
    
    // Tempo de expiração do cache em minutos
    private final int CACHE_EXPIRY_MINUTES = 30;
    
    // Construtor que inicializa o HttpClient com configurações padrão
    public RequestApi() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    // Método que retorna a taxa de câmbio entre duas moedas (origem e destino)
    public double obterTaxaCambio(String moedaOrigem, String moedaDestino) throws Exception {
        // Verifica se a taxa está em cache e ainda é válida
        String cacheKey = moedaOrigem + "-" + moedaDestino;
        
        if (cacheRates.containsKey(cacheKey)) {
            CacheItem cacheItem = cacheRates.get(cacheKey);
            if (!cacheItem.isExpired()) {
                return cacheItem.getRate();
            }
        }
        
        // Se não estiver em cache ou estiver expirado, faz a requisição à API
        double taxa = consultarTaxaAPI(moedaOrigem, moedaDestino);
        
        // Armazena a taxa no cache
        cacheRates.put(cacheKey, new CacheItem(taxa));
        
        return taxa;
    }
    
    // Método que verifica se uma taxa está em cache e é válida
    public boolean verificarCache(String moedaOrigem, String moedaDestino) {
        String cacheKey = moedaOrigem + "-" + moedaDestino;
        
        if (cacheRates.containsKey(cacheKey)) {
            CacheItem cacheItem = cacheRates.get(cacheKey);
            return !cacheItem.isExpired();
        }
        
        return false;
    }
    
    // Método que limpa todo o cache de taxas
    public void limparCache() {
        cacheRates.clear();
    }
    
    // Método que faz a requisição à API e retorna a taxa de câmbio
    private double consultarTaxaAPI(String moedaOrigem, String moedaDestino) throws Exception {
        // Monta a URL para obter as taxas de câmbio a partir da moeda de origem
        String urlStr = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + moedaOrigem;

        // Cria uma requisição HTTP usando HttpRequest
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlStr))
                .header("Content-Type", "application/json")
                .GET()
                .timeout(Duration.ofSeconds(10))
                .build();

        // Envia a requisição e obtém a resposta como String
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        // Verifica se a resposta foi bem-sucedida (código 200)
        if (response.statusCode() != 200) {
            throw new Exception("Erro na API: Código de status " + response.statusCode());
        }

        // Converte a resposta JSON em um objeto JsonObject usando Gson
        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
        
        // Verifica se a resposta contém o campo "result" e se é "success"
        String result = json.get("result").getAsString();
        if (!"success".equals(result)) {
            throw new Exception("Erro na API: " + json.get("error-type").getAsString());
        }
        
        // Acessa o objeto que contém as taxas de conversão (conversion_rates)
        JsonObject rates = json.getAsJsonObject("conversion_rates");
        
        // Verifica se a moeda de destino existe na resposta
        if (!rates.has(moedaDestino)) {
            throw new Exception("Moeda de destino não encontrada: " + moedaDestino);
        }

        // Retorna a taxa de câmbio da moeda de destino
        return rates.get(moedaDestino).getAsDouble();
    }
    
    // Classe interna para armazenar itens do cache com tempo de expiração
    private class CacheItem {
        private final double rate;
        private final LocalDateTime timestamp;
        
        public CacheItem(double rate) {
            this.rate = rate;
            this.timestamp = LocalDateTime.now();
        }
        
        public double getRate() {
            return rate;
        }
        
        public boolean isExpired() {
            return LocalDateTime.now().isAfter(timestamp.plusMinutes(CACHE_EXPIRY_MINUTES));
        }
    }
}
