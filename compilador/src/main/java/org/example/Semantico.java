package org.example;

import java.util.*;

public class Semantico {

    public static Map<String, Simbolo> tabelaSimbolos = new LinkedHashMap<>();
    public static List<Instrucao> codigo = new ArrayList<>();
    public static EstadoSemantico estado = new EstadoSemantico();

    private static Map<Integer, String> poolStrings = new LinkedHashMap<>();
    private static Map<Integer, Double> poolReais = new LinkedHashMap<>();
    private static int nextStringId = 1;
    private static int nextRealId = 1;

    private static Simbolo ultimoSimbolo = null;

    private static int indiceCorrenteValoresVetor = -1;

    private static void gerarInstrucao(String instrucao, int parametro) {
        codigo.add(new Instrucao(estado.ponteiro++, instrucao, parametro));
    }

    private static void gerarInstrucao(String instrucao) {
        gerarInstrucao(instrucao, 0);
    }

    // #P1 — Inserir programa na TS
    public static void P1(String identificador) {
        if (tabelaSimbolos.containsKey(identificador)) {
            erro("Identificador de programa já declarado: " + identificador);
        } else {
            Simbolo s = new Simbolo(identificador, 0, 0, -1);
            tabelaSimbolos.put(identificador, s);
        }
    }

    // #P2 — fim do programa: gerar STP
    public static void P2() {
        gerarInstrucao("STP", 0);
    }

    // #D0 — início das declarações de uma linha
    public static void D0() {
        estado.resetLinha();
    }

    // #D1 — adicionar identificador na lista de linha (sem inserir na TS ainda)
    public static void D1(String id) {
        if (tabelaSimbolos.containsKey(id)) {
            erro("Identificador já declarado: " + id);
        } else {
            estado.listaDeIdentificadoresDaLinha.add(id);
                int base = estado.VT + 1; // base (endereço lógico)
                Simbolo s = new Simbolo(id, estado.categoriaAtual, base, estado.tamanhoDoUltimoVetor);
                tabelaSimbolos.put(id, s);
            }

        }

    // #T — define categoria atual (usa int conforme .jj: 1=num,2=real,3=text,4=flag)
    public static void T(int categoria) {
        estado.categoriaAtual = categoria;
    }

    // #V1 — define tamanho do vetor (validação)
    public static void V1(int valor) {
        if (valor <= 0) {
            erro("Tamanho inválido para vetor: " + valor);
        } else {
            estado.tamanhoDoUltimoVetor = valor;
        }
    }

    // #V2 — inserir vetores na TS (para cada identificador da linha)
    public static void V2() {
        for (String id : estado.listaDeIdentificadoresDaLinha) {
            int base = estado.VT + 1; // base (endereço lógico)
            Simbolo s = new Simbolo(id, estado.categoriaAtual, base, estado.tamanhoDoUltimoVetor);
            tabelaSimbolos.put(id, s);
            estado.listaBasesDaLinha.add(base);
            estado.VT += estado.tamanhoDoUltimoVetor;
            estado.VP += estado.tamanhoDoUltimoVetor;
            estado.baseDoUltimoVetor = base;
        }
    }

    // #E2 — inserir escalares na TS
    public static void E2() {
        for (String id : estado.listaDeIdentificadoresDaLinha) {
            int base = estado.VT + 1;
            Simbolo s = new Simbolo(id, estado.categoriaAtual, base, -1);
            tabelaSimbolos.put(id, s);
            estado.listaBasesDaLinha.add(base);
            estado.VT++;
            estado.VP++;
        }
    }

    // #D6 — ao terminar ';' na declaração: gerar instrucao de alocacao conforme categoriaAtual
    public static void D6() {
        String op;
        switch (estado.categoriaAtual) {
            case 1 -> op = "ALI";
            case 2 -> op = "ALR";
            case 3 -> op = "ALS";
            case 4 -> op = "ALB";
            default -> op = "ALI";
        }
        gerarInstrucao(op, estado.VP);

        if (estado.houveInitLinha) {
            for (int k = 1; k < estado.listaBasesDaLinha.size(); k++) {
                gerarInstrucao("LDV", estado.primeiroBaseInit);
                gerarInstrucao("STR", estado.listaBasesDaLinha.get(k));
            }
            estado.houveInitLinha = false;
            estado.primeiroBaseInit = -1;
        }
        estado.resetLinha();
    }

    public static void IV_End() {
        if (estado.baseDoUltimoVetor < 0) {
            erro("IV_End: base do último vetor inválida");
            return;
        }

        if (indiceCorrenteValoresVetor == 1 && estado.tamanhoDoUltimoVetor > 1) {
            int baseV = estado.baseDoUltimoVetor;
            for (int j = 2; j <= estado.tamanhoDoUltimoVetor; j++) {
                gerarInstrucao("LDV", baseV);
                gerarInstrucao("STR", baseV + (j - 1));
            }
        }

        indiceCorrenteValoresVetor = -1;
    }

    // #VAL — ao reconhecer um valor em lista completa de vetor
    public static void VAL() {
        if (estado.baseDoUltimoVetor < 0) {
            erro("Base do último vetor não definida para VAL()");
            return;
        }
        if (indiceCorrenteValoresVetor == -1) {
            indiceCorrenteValoresVetor = 0;
        }
        int destino = estado.baseDoUltimoVetor + indiceCorrenteValoresVetor;
        gerarInstrucao("STR", destino);
        indiceCorrenteValoresVetor++;
    }

    // #IE — inicialização escalar comum (um valor para todos da linha)
    public static void IE() {
        if (estado.listaBasesDaLinha.isEmpty()) {
            erro("lista de bases vazia");
            return;
        }
        estado.primeiroBaseInit = estado.listaBasesDaLinha.get(0);
        gerarInstrucao("STR", estado.primeiroBaseInit);
        estado.houveInitLinha = true;
    }

    // #C1..#C5 — constantes
    public static void C1(int k) {
        gerarInstrucao("LDI", k);
    }

    public static void C2(double r) {
        int id = nextRealId++;
        poolReais.put(id, r);
        gerarInstrucao("LDR", id);
    }

    public static void C3(String s) {
        int id = nextStringId++;
        poolStrings.put(id, s);
        gerarInstrucao("LDS", id);
    }

    public static void C4() {
        gerarInstrucao("LDB", 1);
    }

    public static void C5() {
        gerarInstrucao("LDB", 0);
    }

    // #A1 — buscar identificador (set identificador ...)
    public static void A1(String id) {
        if (!tabelaSimbolos.containsKey(id)) {
            erro("A1: identificador não declarado: " + id);
            ultimoSimbolo = null;
        } else {
            ultimoSimbolo = tabelaSimbolos.get(id);
            estado.temIndice = false;
        }
    }

    // #I1 — valida expressão índice
    public static void I1() {
        estado.temIndice = true;
    }

    // #A2 — validar se tem índice/escalar
    public static void A2() {
        if (ultimoSimbolo == null) return;
        if (ultimoSimbolo.getTamanho() == -1 && estado.temIndice) {
            erro("A2: Índice fornecido para escalar '" + ultimoSimbolo.getIdentificador() + "'");
        } else if (ultimoSimbolo.getTamanho() != -1 && !estado.temIndice) {
            erro("A2: Vetor '" + ultimoSimbolo.getIdentificador() + "' usado sem índice");
        }
    }

    // #A3 — geração do armazenamento (STR ou STX sequence)
    public static void A3() {
        if (ultimoSimbolo == null) return;
        if (ultimoSimbolo.getTamanho() == -1) {
            // escalar: STR base
            gerarInstrucao("STR", ultimoSimbolo.getBase());
        } else {
            // vetor com índice: LDI base-1 ; ADD 0 ; STX 0
            gerarInstrucao("LDI", ultimoSimbolo.getBase() - 1);
            gerarInstrucao("ADD", 0);
            gerarInstrucao("STX", 0);
        }
    }

    // #R1 — buscar identificador
    public static void R1(String id) {
        if (!tabelaSimbolos.containsKey(id)) {
            erro("R1: identificador não declarado: " + id);
            ultimoSimbolo = null;
        } else {
            ultimoSimbolo = tabelaSimbolos.get(id);
            estado.temIndice = false;
        }
    }

    // #R2 — valida índice em read
    public static void R2() {
        estado.temIndice = true;
    }

    // #R_Generate — gerar instruções de leitura apropriadas
    public static void R_Generate() {
        if (ultimoSimbolo == null) return;
        int cat = ultimoSimbolo.getCategoria();
        if (ultimoSimbolo.getTamanho() == -1) {
            // escalar
            gerarInstrucao("REA", cat);
            gerarInstrucao("STR", ultimoSimbolo.getBase());
        } else {
            // vetor (índice foi gerado antes)
            gerarInstrucao("REA", cat);
            gerarInstrucao("LDI", ultimoSimbolo.getBase() - 1);
            gerarInstrucao("ADD", 0);
            gerarInstrucao("STX", 0);
        }
        estado.temIndice = false;
    }

    // #S2 — buscar identificador para show
    public static void S2(String id) {
        if (!tabelaSimbolos.containsKey(id)) {
            erro("S2: identificador não declarado: " + id);
            ultimoSimbolo = null;
        } else {
            ultimoSimbolo = tabelaSimbolos.get(id);
            estado.temIndice = false;
        }
    }

    // #S3 — valida índice usado em show
    public static void S3() {
        estado.temIndice = true;
    }

    // #S_Generate — gerar instruções de saída para o item previamente preparado
    public static void S_Generate() {
        if (ultimoSimbolo == null) return;
        if (ultimoSimbolo.getTamanho() == -1) {
            gerarInstrucao("LDV", ultimoSimbolo.getBase());
            gerarInstrucao("WRT", 0);
        } else {
            // vetor: índice já gerado
            gerarInstrucao("LDI", ultimoSimbolo.getBase() - 1);
            gerarInstrucao("ADD", 0);
            gerarInstrucao("LDX", 0);
            gerarInstrucao("WRT", 0);
        }
        estado.temIndice = false;
    }

    // Literais na saída
    // #K1: inteiro literal
    public static void K1(int k) {
        gerarInstrucao("LDI", k);
        gerarInstrucao("WRT", 0);
    }

    // #K2: real literal
    public static void K2(double r) {
        int id = nextRealId++;
        poolReais.put(id, r);
        gerarInstrucao("LDR", id);
        gerarInstrucao("WRT", 0);
    }

    // #K3: literal string
    public static void K3(String s) {
        int id = nextStringId++;
        poolStrings.put(id, s);
        gerarInstrucao("LDS", id);
        gerarInstrucao("WRT", 0);
    }

    // #F1 — after condition: gerar JMF e empilhar endereço
    public static void F1() {
        gerarInstrucao("JMF", 0);
        // empilha o índice da instrução a ajustar (ponteiro-1)
        estado.pilhaDeDesvios.push(estado.ponteiro - 1);
    }

    // #F2 — before else: gerar JMP (saltar else), ajustar JMF para pular ao else, empilhar JMP
    public static void F2() {
        gerarInstrucao("JMP", 0);
        int jmfPos = estado.pilhaDeDesvios.pop();
        // ajustar JMF empilhada para saltar ao índice atual
        codigo.get(jmfPos - 1).setOperando(estado.ponteiro); // instrucao.ponteiro starts at 1; list index = ponteiro-1
        // empilha o novo JMP para ajuste posterior
        estado.pilhaDeDesvios.push(estado.ponteiro - 1);
    }

    // #F3 — ajuste final: ajustar JMP ou JMF pendente para ponteiro atual
    public static void F3() {
        if (estado.pilhaDeDesvios.isEmpty()) return;
        int pos = estado.pilhaDeDesvios.pop();
        // lista codigo index = pos-1
        if (pos - 1 >= 0 && pos - 1 < codigo.size()) {
            codigo.get(pos - 1).setOperando(estado.ponteiro);
        } else {
            erro("F3: posição de desvio inválida ao ajustar: " + pos);
        }
    }

    // #L1 — inicio do loop: guardar inicio e gerar JMF empilhando
    public static void L1() {
        int inicioLoop = estado.ponteiro;
        // empilhar inicio (usamos pilhaDeDesvios para guardar inicio e JMF pos)
        gerarInstrucao("JMF", 0);
        estado.pilhaDeDesvios.push(estado.ponteiro - 1); // posição do JMF para ajuste de saída
        // também empilhar inicio em pilha (colocamos num campo temporário usando baseDoUltimoVetor)
        // vamos usar baseDoUltimoVetor como storage temporário (não ideal, mas evita novas estruturas)
        estado.baseDoUltimoVetor = inicioLoop;
    }

    // #L2 — fechar loop: gerar JMP para inicio e ajustar JMF pendente
    public static void L2() {
        int inicioLoop = estado.baseDoUltimoVetor;
        gerarInstrucao("JMP", inicioLoop);
        if (estado.pilhaDeDesvios.isEmpty()) {
            erro("L2: sem JMF pendente para ajustar");
            return;
        }
        int jmfPos = estado.pilhaDeDesvios.pop();
        codigo.get(jmfPos - 1).setOperando(estado.ponteiro);
    }

    public static void REQ() { gerarInstrucao("EQL", 0); }
    public static void RNEQ() { gerarInstrucao("DIF", 0); }
    public static void RLT() { gerarInstrucao("SMR", 0); }
    public static void RGT() { gerarInstrucao("BGR", 0); }
    public static void RLE() { gerarInstrucao("SME", 0); }
    public static void RGE() { gerarInstrucao("BGE", 0); }

    public static void ADD() { gerarInstrucao("ADD", 0); }
    public static void SUB() { gerarInstrucao("SUB", 0); }
    public static void OR()  { gerarInstrucao("OR", 0); }

    public static void MUL() { gerarInstrucao("MUL", 0); }
    public static void DIV() { gerarInstrucao("DIV", 0); }
    public static void MOD() { gerarInstrucao("MOD", 0); }
    public static void REM() { gerarInstrucao("REM", 0); }
    public static void AND() { gerarInstrucao("AND", 0); }

    public static void POW() { gerarInstrucao("POW", 0); }
    public static void NOT() { gerarInstrucao("NOT", 0); }

    // #E1 — buscar identificador em contexto de expressão
    public static void E1(String id) {
        if (!tabelaSimbolos.containsKey(id)) {
            erro("E1: identificador não declarado: " + id);
            ultimoSimbolo = null;
        } else {
            ultimoSimbolo = tabelaSimbolos.get(id);
            estado.temIndice = false;
        }
    }

    // #E2 — valida índice em expressão
    public static void E2_Indice() {
        estado.temIndice = true;
    }

    // #E_Generate — gerar código de carregamento do identificador (LDV/LDX)
    public static void E_Generate() {
        if (ultimoSimbolo == null) return;
        if (ultimoSimbolo.getTamanho() == -1) {
            // escalar: LDV base
            gerarInstrucao("LDV", ultimoSimbolo.getBase());
        } else {
            // vetor: índice já gerado no topo => LDI base-1 ; ADD 0 ; LDX 0
            gerarInstrucao("LDI", ultimoSimbolo.getBase() - 1);
            gerarInstrucao("ADD", 0);
            gerarInstrucao("LDX", 0);
        }
        estado.temIndice = false;
    }

    public static void erro(String msg) {
        System.err.println("[ERRO SEMÂNTICO] " + msg);
    }

    public static void mostraTS() {
        System.out.println("=== TABELA DE SIMBOLOS ===");
        for (Simbolo s : tabelaSimbolos.values()) {
            System.out.println(s);
        }
    }

    public static void mostraCodigo() {
        System.out.println("=== CODIGO GERADO ===");
        for (Instrucao i : codigo) {
            System.out.println(i);
        }
        System.out.println("--- POOLS ---");
        System.out.println("Strings (id -> literal):");
        for (Map.Entry<Integer, String> e : poolStrings.entrySet()) {
            System.out.printf(" %d -> %s%n", e.getKey(), e.getValue());
        }
        System.out.println("Reais (id -> valor):");
        for (Map.Entry<Integer, Double> e : poolReais.entrySet()) {
            System.out.printf(" %d -> %s%n", e.getKey(), e.getValue());
        }
    }
}
