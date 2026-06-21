import java.util.Scanner;
import java.util.ArrayList;

public class App {
    private Pilha_Humano pilhaVetor;
    private Pilha_Humano pilhaEncadeada;
    private Pilha pilha; 
    
    private int tipoAtual; 
    private Scanner scanner;

    public App() {
        // Inicializa as 3 implementações
        pilhaVetor = new PilhaComVetor(20);
        pilhaEncadeada = new PilhaEncadeada();
        pilha = new Pilha(20);
        scanner = new Scanner(System.in);
    }

    public void executar() {
        System.out.println("=====================");
        System.out.println("GERENCIADOR DE PILHAS");
        System.out.println("=====================");
        System.out.println("Para começar, escolha a implementação:");
        System.out.println("1. Pilha com Vetor");
        System.out.println("2. Pilha Encadeada");
        System.out.println("3. Pilha");
        System.out.print("Opção inicial: ");
        
        tipoAtual = lerInteiro();
        while (tipoAtual < 1 || tipoAtual > 3) {
            System.out.print("Opção inválida! Escolha 1, 2 ou 3: ");
            tipoAtual = lerInteiro();
        }

        int acao = 0;
        while (acao != 4) {
            System.out.println("\n--- IMPLEMENTAÇÃO ATUAL: " + getNomePilhaAtual() + " ---");
            
            if (tipoAtual == 1) System.out.println("Tamanho atual: " + pilhaVetor.tamanho());
            if (tipoAtual == 2) System.out.println("Tamanho atual: " + pilhaEncadeada.tamanho());

            // Chama o novo método que exibe a pilha visualmente [10, 20...]
            exibirEstadoPilha();

            System.out.println("\n1. Empilhar (Push)");
            System.out.println("2. Desempilhar (Pop)");
            System.out.println("3. Trocar de Implementação (Transferir dados)");
            System.out.println("4. Sair");
            System.out.print("Escolha uma ação: ");
            
            acao = lerInteiro();

            try {
                switch (acao) {
                    case 1:
                        System.out.print("Digite o valor (inteiro) a empilhar: ");
                        int valor = lerInteiro();
                        
                        if (tipoAtual == 1) {
                            pilhaVetor.empilha(valor);
                            System.out.println("[OK] " + valor + " empilhado.");
                        } else if (tipoAtual == 2) {
                            pilhaEncadeada.empilha(valor);
                            System.out.println("[OK] " + valor + " empilhado.");
                        } else {
                            pilha.push(valor);
                        }
                        break;
                        
                    case 2:
                        if (tipoAtual == 1) {
                            System.out.println("[OK] Valor desempilhado: " + pilhaVetor.desempilha());
                        } else if (tipoAtual == 2) {
                            System.out.println("[OK] Valor desempilhado: " + pilhaEncadeada.desempilha());
                        } else {
                            pilha.pop(); 
                        }
                        break;
                        
                    case 3:
                        escolherDestinoETrocar();
                        break;
                        
                    case 4:
                        System.out.println("Encerrando a demonstração prática...");
                        break;
                        
                    default:
                        System.out.println("[ERRO] Opção inválida!");
                }
            } catch (StackEmptyException e) {
                System.out.println("\n[ERRO] A pilha está vazia! Não há o que desempilhar.");
            } catch (StackFullException e) {
                System.out.println("\n[ERRO] A pilha atingiu o limite de capacidade!");
            }
        }
        scanner.close();
    }

    private void exibirEstadoPilha() {

        if (tipoAtual == 3) {
            pilha.exibir();
            return;
        }

        System.out.print("=> Estado atual da Pilha: [");
        if (isPilhaAtualVazia()) {
            System.out.println("]");
            return;
        }

        // Lista temporária para segurar os itens enquanto desempilhamos
        ArrayList<Integer> temp = new ArrayList<>();

        try {
            // 1. Esvazia a pilha e guarda os valores
            while (!isPilhaAtualVazia()) {
                if (tipoAtual == 1) {
                    temp.add((Integer) pilhaVetor.desempilha());
                } else if (tipoAtual == 2) {
                    temp.add((Integer) pilhaEncadeada.desempilha());
                }
            }

            // 2. Exibe os valores do fundo (últimos da lista temp) para o topo
            for (int i = temp.size() - 1; i >= 0; i--) {
                System.out.print(temp.get(i));
                if (i > 0) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");

            // 3. Devolve os valores para a pilha na ordem original
            for (int i = temp.size() - 1; i >= 0; i--) {
                if (tipoAtual == 1) {
                    pilhaVetor.empilha(temp.get(i));
                } else if (tipoAtual == 2) {
                    pilhaEncadeada.empilha(temp.get(i));
                }
            }
        } catch (StackEmptyException | StackFullException e) {
            System.out.println("] (Erro ao exibir estado)");
        }
    }

    private String getNomePilhaAtual() {
        if (tipoAtual == 1) return "Vetor (Documento)";
        if (tipoAtual == 2) return "Encadeada / Nó (Documento)";
        return "IA (Gemini)";
    }

    private boolean isPilhaAtualVazia() {
        if (tipoAtual == 1) return pilhaVetor.vazia();
        if (tipoAtual == 2) return pilhaEncadeada.vazia();
        return pilha.isEmpty();
    }

    private void escolherDestinoETrocar() {
        System.out.println("\nPara qual implementação você deseja transferir os dados?");
        System.out.println("1. Pilha com Vetor");
        System.out.println("2. Pilha Encadeada");
        System.out.println("3. Pilha");
        System.out.print("Escolha o destino: ");
        
        int tipoDestino = lerInteiro();

        if (tipoDestino < 1 || tipoDestino > 3) {
            System.out.println("[ERRO] Opção inválida!");
            return;
        }

        if (tipoAtual == tipoDestino) {
            System.out.println("[ERRO] Você já está utilizando esta implementação!");
            return;
        }

        realizarTransferencia(tipoDestino);
    }

    private void realizarTransferencia(int tipoDestino) {
        System.out.println("\n[!] Transferindo dados... (Isso inverterá a ordem)");

        int itensMovidos = 0;
        try {
            while (!isPilhaAtualVazia()) {
                int valorExtraido = 0;
                
                if (tipoAtual == 1) {
                    valorExtraido = (Integer) pilhaVetor.desempilha();
                } else if (tipoAtual == 2) {
                    valorExtraido = (Integer) pilhaEncadeada.desempilha();
                } else {
                    valorExtraido = pilha.pop(); 
                }
                
                if (tipoDestino == 1) {
                    pilhaVetor.empilha(valorExtraido);
                } else if (tipoDestino == 2) {
                    pilhaEncadeada.empilha(valorExtraido);
                } else {
                    pilha.push(valorExtraido);
                }
                
                itensMovidos++;
            }
            
            tipoAtual = tipoDestino;
            System.out.println("\n[OK] Troca concluída! " + itensMovidos + " itens transferidos para " + getNomePilhaAtual());
            
        } catch (StackFullException e) {
            System.out.println("\n[ERRO] A pilha de destino encheu durante a transferência!");
            tipoAtual = tipoDestino; 
        }
    }

    private int lerInteiro() {
        while (!scanner.hasNextInt()) {
            System.out.print("[ERRO] Entrada inválida! Digite um número inteiro: ");
            scanner.next();
        }
        int valor = scanner.nextInt();
        scanner.nextLine(); 
        return valor;
    }

    public static void main(String[] args) {
        App app = new App();
        app.executar();
    }
}