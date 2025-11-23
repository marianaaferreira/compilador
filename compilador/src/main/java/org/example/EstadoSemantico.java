package org.example;

import java.util.*;

public class EstadoSemantico {
    public String contextoAtual = "";
    public int VT = 0; // total de posições já alocadas
    public int VP = 0; // soma das posições da linha atual
    public List<String> listaDeIdentificadoresDaLinha = new ArrayList<>();
    public List<Integer> listaBasesDaLinha = new ArrayList<>();
    public int categoriaAtual = -1;
    public int ponteiro = 1;
    public Stack<Integer> pilhaDeDesvios = new Stack<>();
    public boolean temIndice = false;
    public int baseDoUltimoVetor = -1;
    public int tamanhoDoUltimoVetor = -1;
    public boolean houveInitLinha = false;
    public int primeiroBaseInit = -1;
    public Stack<String> pilhaTipos = new Stack<>();

    public void resetLinha() {
        VP = 0;
        listaDeIdentificadoresDaLinha.clear();
        listaBasesDaLinha.clear();
        houveInitLinha = false;
        temIndice = false;
        baseDoUltimoVetor = -1;
        tamanhoDoUltimoVetor = -1;
        primeiroBaseInit = -1;
    }
}
