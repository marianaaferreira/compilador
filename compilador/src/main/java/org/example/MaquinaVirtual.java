package org.example;

import java.util.List;
import java.util.Scanner;

public class MaquinaVirtual {

    private Object[] pilha;
    private int topo;
    private int pc;
    private List<Instrucao> codigo;
    private boolean halted;
    private final StringBuilder saida = new StringBuilder();

    public MaquinaVirtual(int tamanhoPilha) {
        this.pilha = new Object[tamanhoPilha];
        this.topo = 0;
        this.pc = 0;
        this.halted = false;
        this.saida.setLength(0);
    }

    public void carregarPrograma(List<Instrucao> codigo) {
        this.codigo = codigo;
        this.pc = 1;
        this.topo = 0;
        this.halted = false;
    }

    public void run() {
        while (!halted && pc <= codigo.size()) {
            Instrucao inst = codigo.get(pc);
            executar(inst);
        }
    }

    private double toNumber(Object o) {
        if (o instanceof Number n) return n.doubleValue();
        throw new RuntimeException("Valor não numérico na pilha: " + o);
    }

    private boolean toBool(Object o) {
        if (o instanceof Boolean b) return b;
        if (o instanceof Number n) return n.doubleValue() != 0;
        throw new RuntimeException("Valor não booleano na pilha: " + o);
    }

    private void executar(Instrucao inst) {
        String op = inst.getInstrucao();
        Object paramObj = inst.getParametro();

        int param = (paramObj instanceof Number) ? ((Number)paramObj).intValue() : 0;

        switch (op) {
            case "ADD":
                pilha[topo - 2] = toNumber(pilha[topo - 2]) + toNumber(pilha[topo - 1]);
                topo--;
                pc++;
                break;

            case "SUB":
                pilha[topo - 2] = toNumber(pilha[topo - 2]) - toNumber(pilha[topo - 1]);
                topo--;
                pc++;
                break;

            case "MUL":
                pilha[topo - 2] = toNumber(pilha[topo - 2]) * toNumber(pilha[topo - 1]);
                topo--;
                pc++;
                break;

            case "DIV":
                double b = toNumber(pilha[topo - 1]);
                if (b == 0) {
                    saida.append("ERRO: divisão por zero\n");
                    halted = true;
                    return;
                }
                pilha[topo - 2] = toNumber(pilha[topo - 2]) / b;
                topo--;
                pc++;
                break;

            case "ALB":
            case "ALI":
            case "ALR":
            case "ALS":
                topo += param;
                pc++;
                break;

            case "LDB":
            case "LDI":
            case "LDR":
            case "LDS":
                pilha[topo++] = inst.getParametro();
                pc++;
                break;

            case "LDV":
                pilha[topo++] = pilha[param];
                pc++;
                break;

            case "STR":
                pilha[param] = pilha[topo - 1];
                topo--;
                pc++;
                break;

            case "AND":
                pilha[topo - 2] = (toBool(pilha[topo - 2]) && toBool(pilha[topo - 1]));
                topo--;
                pc++;
                break;

            case "OR":
                pilha[topo - 2] = (toBool(pilha[topo - 2]) || toBool(pilha[topo - 1]));
                topo--;
                pc++;
                break;

            case "NOT":
                pilha[topo - 1] = !toBool(pilha[topo - 1]);
                pc++;
                break;

            case "EQL":
                pilha[topo - 2] = toNumber(pilha[topo - 2]) == toNumber(pilha[topo - 1]);
                topo--;
                pc++;
                break;

            case "DIF":
                pilha[topo - 2] = toNumber(pilha[topo - 2]) != toNumber(pilha[topo - 1]);
                topo--;
                pc++;
                break;

            case "BGR":
                pilha[topo - 2] = toNumber(pilha[topo - 2]) > toNumber(pilha[topo - 1]);
                topo--;
                pc++;
                break;

            case "BGE":
                pilha[topo - 2] = toNumber(pilha[topo - 2]) >= toNumber(pilha[topo - 1]);
                topo--;
                pc++;
                break;

            case "SME":
                pilha[topo - 2] = toNumber(pilha[topo - 2]) < toNumber(pilha[topo - 1]);
                topo--;
                pc++;
                break;

            case "SMR":
                pilha[topo - 2] = toNumber(pilha[topo - 2]) <= toNumber(pilha[topo - 1]);
                topo--;
                pc++;
                break;

            case "JMP":
                pc = param;
                break;

            case "JMT":
                if (toBool(pilha[topo - 1]))
                    pc = param;
                else
                    pc++;
                topo--;
                break;

            case "JMF":
                if (!toBool(pilha[topo - 1]))
                    pc = param;
                else
                    pc++;
                topo--;
                break;

            case "STP":
                halted = true;
                break;

            case "REA":
                Scanner sc = new Scanner(System.in);
                //System.out.print("Entrada: ");

                switch (param) {
                    case 1:
                        pilha[topo++] = sc.nextInt();
                        break;

                    case 2:
                        pilha[topo++] = sc.nextDouble();
                        break;

                    case 3:
                        String s = sc.nextLine();
                        if (s.isEmpty()) s = sc.nextLine();
                        pilha[topo++] = s;
                        break;
                }

                pc++;
                break;

            case "WRT":
                saida.append(String.valueOf(pilha[topo - 1]));
                topo--;
                pc++;
                break;

            default:
                throw new RuntimeException("Instrução inválida: " + op);
        }
    }

    public String getSaida() {
        return saida.toString();
    }
}
