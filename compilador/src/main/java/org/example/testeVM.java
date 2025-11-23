package org.example;

import java.util.ArrayList;
import java.util.List;

public class testeVM {

    public static void main(String[] args) {

        //List<Instrucao> programa = programaAritmetica();
        //List<Instrucao> programa = programaBooleanos();
        //List<Instrucao> programa = programaComparacoes();
        //List<Instrucao> programa = programaControleFluxo();
        //List<Instrucao> programa = programaLoadStore();
        //List<Instrucao> programa = programaString();
        List<Instrucao> programa = programaREA();


        MaquinaVirtual vm = new MaquinaVirtual(100);
        vm.carregarPrograma(programa);
        vm.run();

        System.out.println("===== SAÍDA DA VM =====");
        System.out.println(vm.getSaida());
    }


    // ============================================================
    // 1. Teste: ARITMÉTICA
    // ============================================================
    private static List<Instrucao> programaAritmetica() {
        List<Instrucao> p = new ArrayList<>();
        p.add(null);

        p.add(new Instrucao(1, "LDI", 10));   // 10
        p.add(new Instrucao(2, "LDI", 20));   // 20
        p.add(new Instrucao(3, "ADD", 0));    // 30
        p.add(new Instrucao(4, "WRT", 0));
        p.add(new Instrucao(5, "STP", 0));

        return p;
    }


    // ============================================================
    // 2. Teste: BOOLEANOS
    // ============================================================
    private static List<Instrucao> programaBooleanos() {
        List<Instrucao> p = new ArrayList<>();
        p.add(null);

        p.add(new Instrucao(1, "LDI", 1));    // true
        p.add(new Instrucao(2, "LDI", 0));    // false
        p.add(new Instrucao(3, "OR", 0));     // true OR false = true
        p.add(new Instrucao(4, "NOT", 0));    // NOT true = false
        p.add(new Instrucao(5, "WRT", 0));
        p.add(new Instrucao(6, "STP", 0));

        return p;
    }


    // ============================================================
    // 3. Teste: COMPARAÇÕES
    // ============================================================
    private static List<Instrucao> programaComparacoes() {
        List<Instrucao> p = new ArrayList<>();
        p.add(null);

        p.add(new Instrucao(1, "LDI", 10));
        p.add(new Instrucao(2, "LDI", 20));
        p.add(new Instrucao(3, "BGR", 0));  // 10 > 20 = false
        p.add(new Instrucao(4, "WRT", 0));
        p.add(new Instrucao(5, "STP", 0));

        return p;
    }


    // ============================================================
    // 4. Teste: CONTROLE DE FLUXO
    // ============================================================
    private static List<Instrucao> programaControleFluxo() {
        List<Instrucao> p = new ArrayList<>();
        p.add(null);

        p.add(new Instrucao(1, "LDI", 1));    // true
        p.add(new Instrucao(2, "JMT", 4));    // pula
        p.add(new Instrucao(3, "LDI", 999));  // não deve executar
        p.add(new Instrucao(4, "LDI", 42));   // executa aqui
        p.add(new Instrucao(5, "WRT", 0));
        p.add(new Instrucao(6, "STP", 0));

        return p;
    }


    // ============================================================
    // 5. Teste: LOAD / STORE / AL*
    // ============================================================
    private static List<Instrucao> programaLoadStore() {
        List<Instrucao> p = new ArrayList<>();
        p.add(null);

        p.add(new Instrucao(1, "ALI", 3));  // reserva 3 posicoes: 0,1,2
        p.add(new Instrucao(2, "LDI", 50));
        p.add(new Instrucao(3, "STR", 0));  // var[0] = 50
        p.add(new Instrucao(4, "LDV", 0));  // empilha var[0]
        p.add(new Instrucao(5, "WRT", 0));
        p.add(new Instrucao(6, "STP", 0));

        return p;
    }


    // ============================================================
    // 6. Teste: STRINGS + WRT
    // ============================================================
    private static List<Instrucao> programaString() {
        List<Instrucao> p = new ArrayList<>();
        p.add(null);

        p.add(new Instrucao(1, "LDS", "Hello World"));
        p.add(new Instrucao(2, "WRT", 0));
        p.add(new Instrucao(3, "STP", 0));

        return p;
    }


    // ============================================================
    // 7. Teste: Entrada (REA)
    // ============================================================
    private static List<Instrucao> programaREA() {
        List<Instrucao> p = new ArrayList<>();
        p.add(null);

        p.add(new Instrucao(1, "REA", 3));  // lê string
        p.add(new Instrucao(2, "WRT", 0));
        p.add(new Instrucao(3, "STP", 0));

        return p;
    }
}
