# ChallengeOneJavaConversorMoedas
Conversor de Moedas - Challenge BackEnd Java One Oracle.
# 💱 Conversor de Moedas Avançado (Java + API)

Este projeto é um conversor de moedas desenvolvido em Java com interação via console. Ele consome uma API pública para realizar conversões de câmbio em tempo real, salvando os resultados em arquivos .txt e mantendo um log de erros. O sistema inclui recursos avançados como cache de taxas, visualização de histórico e interface colorida.

## 🧾 Objetivo

Desenvolver um Conversor de Moedas que ofereça interação textual (via console) com os usuários, proporcionando múltiplas opções de conversões de moedas em um menu interativo. A taxa de conversão é obtida dinamicamente via API, garantindo dados atualizados e precisos, com sistema de cache para otimizar o desempenho.

## 📌 Funcionalidades

- **Menu interativo colorido** no terminal para escolha das moedas
- **Conversão de valores** com base em cotação atual
- **24 opções de conversão** entre 11 moedas diferentes
- **Sistema de cache** para reduzir chamadas à API (30 minutos de validade)
- **Visualização de histórico** de conversões diretamente no programa
- **Salvamento das conversões** em arquivo `conversoes.txt`
- **Registro de erros** no arquivo `log.txt` com timestamp
- **Integração com API** pública (ExchangeRate API)
- **Interface colorida** com códigos ANSI para melhor experiência do usuário

## 🌍 Moedas Suportadas

- USD - Dólar americano
- BRL - Real brasileiro
- EUR - Euro
- ARS - Peso argentino
- BOB - Boliviano boliviano
- CLP - Peso chileno
- COP - Peso colombiano
- JPY - Iene japonês
- GBP - Libra esterlina
- CAD - Dólar canadense
- AUD - Dólar australiano

## 🏗 Estrutura do Projeto

```
DesafioConversorDeMoedas/
├── src/
│   ├── Principal.java           # Classe principal com menu e interface do usuário
│   ├── RequestApi.java          # Classe para requisições à API com sistema de cache
│   ├── GerarArquivoConsulta.java # Classe para salvar conversões em arquivo
│   ├── GerarArquivoLog.java     # Classe para registrar erros em arquivo
│   └── HistoricoManager.java    # Classe para gerenciar o histórico de conversões
├── conversoes.txt               # Arquivo com histórico de conversões
├── log.txt                      # Arquivo de log de erros
└── README.md                    # Este arquivo
```

## 🧰 Requisitos

- Java 11 ou superior
- Biblioteca Gson (JSON)
- Terminal com suporte a cores ANSI (para melhor experiência)

## 📦 Dependência

Este projeto utiliza a biblioteca Gson para manipulação de JSON. O .jar deve ser adicionado manualmente ao projeto.

- **Arquivo**: gson-2.13.1.jar
- **Caminho**: File -> Project Structure -> Modules -> Dependencies -> Add.

## 🌐 APIs suportadas

- [ExchangeRate API](https://www.exchangerate-api.com/) – requer chave (gratuita)

## 🚀 Como Usar

1. **Iniciar o programa**:
   - Execute a classe `Principal.java`

2. **Menu Principal**:
   - Opção 1: Realizar conversão de moeda
   - Opção 2: Visualizar histórico de conversões
   - Opção 3: Limpar cache de taxas
   - Opção 0: Sair

3. **Realizar Conversão**:
   - Escolha entre as 24 combinações de moedas disponíveis
   - Informe o valor a ser convertido
   - O sistema mostrará o resultado e informará se usou cache ou API

4. **Visualizar Histórico**:
   - O sistema exibirá as últimas 10 conversões realizadas
   - Mostrará também o total de conversões registradas

## 🔍 Recursos Adicionais

### Sistema de Cache

O conversor implementa um sistema de cache inteligente que armazena as taxas de câmbio por 30 minutos, reduzindo significativamente o número de chamadas à API. O sistema informa ao usuário quando uma taxa é obtida do cache ou diretamente da API.

### Interface Colorida

A interface do usuário utiliza códigos ANSI para exibir texto colorido no terminal, melhorando a experiência do usuário e tornando a navegação mais intuitiva:
- Verde: Mensagens de sucesso
- Vermelho: Mensagens de erro
- Amarelo: Prompts e entradas
- Azul: Títulos de seção
- Roxo: Informações adicionais
- Ciano: Dados e estatísticas

### Tratamento de Erros

O sistema implementa tratamento robusto de erros para lidar com:
- Entradas inválidas do usuário
- Falhas na conexão com a API
- Moedas não suportadas
- Valores negativos ou inválidos

## 📝 Autor

Eliel de Mesquita Cunha
