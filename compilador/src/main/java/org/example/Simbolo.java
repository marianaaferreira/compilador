package org.example;

public class Simbolo {
    private String identificador;
    private int categoria; // 0=programa, 1=num, 2=real, 3=text, 4=flag
    private int base;      // endereço lógico (base - 1)
    private int tamanho;   // -1 se escalar, N>0 se vetor

    public Simbolo(String identificador, int categoria, int base, int tamanho) {
        this.identificador = identificador;
        this.categoria = categoria;
        this.base = base;
        this.tamanho = tamanho;
    }

    public String getIdentificador() { return identificador; }
    public int getCategoria() { return categoria; }
    public int getBase() { return base; }
    public int getTamanho() { return tamanho; }

    @Override
    public String toString() {
        return "(" + identificador + ", " + categoria + ", " + base + ", " +
                (tamanho == -1 ? "-" : tamanho) + ")";
    }
}
