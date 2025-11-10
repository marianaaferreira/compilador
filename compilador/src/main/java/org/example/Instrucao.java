package org.example;

public class Instrucao {
    private int ponteiro;
    private String instrucao;
    private int parametro;

    public Instrucao(int ponteiro, String instrucao, int parametro) {
        this.ponteiro = ponteiro;
        this.instrucao = instrucao;
        this.parametro = parametro;
    }

    public String getInstrucao() {
        return instrucao;
    }

    public int getPonteiro() {
        return ponteiro;
    }

    public int getParametro() {
        return parametro;
    }

    public void setOperando(int novoOperando) {
        this.parametro = novoOperando;
    }

    public void setInstrucao(String instrucao) {
        this.instrucao = instrucao;
    }

    public void setPonteiro(int ponteiro) {
        this.ponteiro = ponteiro;
    }

    public String toString() {
        return String.format("%03d: %s %d", ponteiro, instrucao, parametro);
    }
}