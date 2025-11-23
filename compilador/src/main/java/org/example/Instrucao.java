package org.example;

public class Instrucao {

    private int ponteiro;
    private String instrucao;
    private Object parametro;

    public Instrucao(int ponteiro, String instrucao) {
        this.ponteiro = ponteiro;
        this.instrucao = instrucao;
        this.parametro = null;
    }

    public Instrucao(int ponteiro, String instrucao, Object parametro) {
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

    public Object getParametro() {
        return parametro;
    }

    public void setParametro(Object novoParametro) {
        this.parametro = novoParametro;
    }

    public void setInstrucao(String instrucao) {
        this.instrucao = instrucao;
    }

    public void setPonteiro(int ponteiro) {
        this.ponteiro = ponteiro;
    }

    @Override
    public String toString() {
        return String.format("%03d: %s %s",
                ponteiro,
                instrucao,
                parametro == null ? "" : parametro.toString());
    }
}
