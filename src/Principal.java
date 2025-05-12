import java.util.Scanner;
import java.util.List;
import java.io.IOException;

public class Principal {
    // Códigos ANSI para cores no terminal
    public static final String RESET = "\u001B[0m";
    public static final String VERDE = "\u001B[32m";
    public static final String AMARELO = "\u001B[33m";
    public static final String AZUL = "\u001B[34m";
    public static final String ROXO = "\u001B[35m";
    public static final String CIANO = "\u001B[36m";
    public static final String VERMELHO = "\u001B[31m";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RequestApi api = new RequestApi();
        GerarArquivoConsulta arquivoConsulta = new GerarArquivoConsulta();
        GerarArquivoLog logger = new GerarArquivoLog();
        HistoricoManager historicoManager = new HistoricoManager();

        // Array com todas as opções de conversão disponíveis
        // Incluindo as moedas requeridas e adicionais
        String[][] opcoes = {
                {"USD", "BRL"}, // Dólar para Real
                {"BRL", "USD"}, // Real para Dólar
                {"EUR", "BRL"}, // Euro para Real
                {"BRL", "EUR"}, // Real para Euro
                {"USD", "EUR"}, // Dólar para Euro
                {"EUR", "USD"}, // Euro para Dólar
                {"USD", "ARS"}, // Dólar para Peso Argentino
                {"ARS", "USD"}, // Peso Argentino para Dólar
                {"USD", "BOB"}, // Dólar para Boliviano
                {"BOB", "USD"}, // Boliviano para Dólar
                {"USD", "CLP"}, // Dólar para Peso Chileno
                {"CLP", "USD"}, // Peso Chileno para Dólar
                {"USD", "COP"}, // Dólar para Peso Colombiano
                {"COP", "USD"}, // Peso Colombiano para Dólar
                {"BRL", "ARS"}, // Real para Peso Argentino
                {"ARS", "BRL"}, // Peso Argentino para Real
                {"USD", "JPY"}, // Dólar para Iene Japonês
                {"JPY", "USD"}, // Iene Japonês para Dólar
                {"USD", "GBP"}, // Dólar para Libra Esterlina
                {"GBP", "USD"}, // Libra Esterlina para Dólar
                {"USD", "CAD"}, // Dólar para Dólar Canadense
                {"CAD", "USD"}, // Dólar Canadense para Dólar
                {"USD", "AUD"}, // Dólar para Dólar Australiano
                {"AUD", "USD"}  // Dólar Australiano para Dólar
        };

        while (true) {
            limparTela();
            exibirCabecalho();
            
            System.out.println(AZUL + "\n--- MENU PRINCIPAL ---" + RESET);
            System.out.println("1 - Realizar conversão de moeda");
            System.out.println("2 - Visualizar histórico de conversões");
            System.out.println("3 - Limpar cache de taxas");
            System.out.println("0 - Sair");
            System.out.print(AMARELO + "\nEscolha uma opção: " + RESET);
            
            int opcaoMenu;
            try {
                opcaoMenu = scanner.nextInt();
                scanner.nextLine(); // Limpa o buffer
            } catch (Exception e) {
                System.out.println(VERMELHO + "Entrada inválida. Por favor, digite um número." + RESET);
                scanner.nextLine(); // Limpa o buffer
                aguardarTecla(scanner);
                continue;
            }
            
            switch (opcaoMenu) {
                case 0:
                    System.out.println(VERDE + "\nPrograma finalizado. Obrigado por utilizar o Conversor de Moedas!" + RESET);
                    scanner.close();
                    return;
                    
                case 1:
                    realizarConversao(scanner, api, arquivoConsulta, logger, opcoes);
                    break;
                    
                case 2:
                    visualizarHistorico(historicoManager);
                    aguardarTecla(scanner);
                    break;
                    
                case 3:
                    api.limparCache();
                    System.out.println(VERDE + "Cache de taxas limpo com sucesso!" + RESET);
                    aguardarTecla(scanner);
                    break;
                    
                default:
                    System.out.println(VERMELHO + "Opção inválida. Por favor, escolha uma opção válida." + RESET);
                    aguardarTecla(scanner);
            }
        }
    }
    
    private static void realizarConversao(Scanner scanner, RequestApi api, 
                                         GerarArquivoConsulta arquivoConsulta, 
                                         GerarArquivoLog logger, 
                                         String[][] opcoes) {
        limparTela();
        exibirCabecalho();
        
        System.out.println(AZUL + "\n--- CONVERSOR DE MOEDAS ---" + RESET);
        System.out.println(CIANO + "Moedas disponíveis: ARS, BOB, BRL, CLP, COP, USD, EUR, JPY, GBP, CAD, AUD" + RESET);
        System.out.println(AMARELO + "\nOpções de conversão:" + RESET);
        
        // Exibe as opções em colunas para melhor visualização
        int colunas = 2;
        int linhasPorColuna = (int) Math.ceil(opcoes.length / (double) colunas);
        
        for (int i = 0; i < linhasPorColuna; i++) {
            for (int j = 0; j < colunas; j++) {
                int index = i + j * linhasPorColuna;
                if (index < opcoes.length) {
                    System.out.printf("%-3d - %-3s para %-3s     ", 
                                     (index + 1), opcoes[index][0], opcoes[index][1]);
                }
            }
            System.out.println();
        }
        
        System.out.println("0  - Voltar ao menu principal");
        System.out.print(AMARELO + "\nEscolha uma opção: " + RESET);
        
        // Tratamento de entrada inválida
        int escolha;
        try {
            escolha = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer
        } catch (Exception e) {
            System.out.println(VERMELHO + "Entrada inválida. Por favor, digite um número." + RESET);
            scanner.nextLine(); // Limpa o buffer
            aguardarTecla(scanner);
            return;
        }

        if (escolha == 0) return;
        if (escolha < 1 || escolha > opcoes.length) {
            System.out.println(VERMELHO + "Opção inválida. Por favor, escolha uma opção entre 1 e " + 
                              opcoes.length + "." + RESET);
            aguardarTecla(scanner);
            return;
        }

        String origem = opcoes[escolha - 1][0];
        String destino = opcoes[escolha - 1][1];

        System.out.print(AMARELO + "\nInforme o valor em " + origem + ": " + RESET);
        double valor;
        
        // Tratamento de entrada inválida para o valor
        try {
            valor = scanner.nextDouble();
            scanner.nextLine(); // Limpa o buffer
            if (valor < 0) {
                System.out.println(VERMELHO + "Por favor, informe um valor positivo." + RESET);
                aguardarTecla(scanner);
                return;
            }
        } catch (Exception e) {
            System.out.println(VERMELHO + "Valor inválido. Por favor, digite um número válido." + RESET);
            scanner.nextLine(); // Limpa o buffer
            aguardarTecla(scanner);
            return;
        }

        try {
            System.out.println(CIANO + "\nConsultando taxa de câmbio..." + RESET);
            
            // Verifica se a taxa está em cache antes de fazer a requisição
            boolean usouCache = api.verificarCache(origem, destino);
            double taxa = api.obterTaxaCambio(origem, destino);
            double convertido = valor * taxa;

            String resultado = String.format("%.2f %s = %.2f %s", valor, origem, convertido, destino);
            
            System.out.println(VERDE + "\nResultado: " + resultado + RESET);
            if (usouCache) {
                System.out.println(ROXO + "(Taxa obtida do cache)" + RESET);
            } else {
                System.out.println(ROXO + "(Taxa obtida da API)" + RESET);
            }

            // Salva a conversão no histórico
            arquivoConsulta.salvarConversao(resultado);
            System.out.println(VERDE + "Conversão salva no histórico." + RESET);
        } catch (Exception e) {
            System.out.println(VERMELHO + "Erro ao realizar conversão: " + e.getMessage() + RESET);
            logger.registrarErro(e.getMessage());
            System.out.println(VERMELHO + "O erro foi registrado no arquivo de log." + RESET);
        }
        
        aguardarTecla(scanner);
    }
    
    private static void visualizarHistorico(HistoricoManager historicoManager) {
        limparTela();
        exibirCabecalho();
        
        System.out.println(AZUL + "\n--- HISTÓRICO DE CONVERSÕES ---" + RESET);
        
        try {
            List<String> historico = historicoManager.lerHistorico();
            
            if (historico.isEmpty()) {
                System.out.println(AMARELO + "Nenhuma conversão registrada ainda." + RESET);
            } else {
                System.out.println(CIANO + "Últimas conversões realizadas:" + RESET);
                
                // Exibe as últimas 10 conversões (ou menos, se houver menos registros)
                int inicio = Math.max(0, historico.size() - 10);
                for (int i = inicio; i < historico.size(); i++) {
                    System.out.println(VERDE + historico.get(i) + RESET);
                }
                
                System.out.println(AMARELO + "\nTotal de conversões: " + historico.size() + RESET);
            }
        } catch (IOException e) {
            System.out.println(VERMELHO + "Erro ao ler o histórico: " + e.getMessage() + RESET);
        }
    }
    
    private static void limparTela() {
        // Tenta limpar a tela (funciona em alguns terminais)
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    private static void exibirCabecalho() {
        System.out.println(ROXO + "╔══════════════════════════════════════════════╗");
        System.out.println("║             CONVERSOR DE MOEDAS               ║");
        System.out.println("╚══════════════════════════════════════════════╝" + RESET);
    }
    
    private static void aguardarTecla(Scanner scanner) {
        System.out.println(AMARELO + "\nPressione ENTER para continuar..." + RESET);
        scanner.nextLine();
    }
}
