package org.example;

import org.example.Linguagem20252;
import org.example.ParseException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;

public class Main {

    public static void main(String[] args) {
        Janela janela = new Janela();
        janela.setVisible(true);

        janela.addActionListenerCompilar(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                analisarSintatico(janela);
            }
        });

        janela.addActionListenerCompilarMenu(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                analisarSintatico(janela);
            }
        });
    }

    public static void analisarSintatico(Janela janela) {
        String codigo = janela.getCodigo();
        janela.limparMensagens();

        if (codigo.trim().isEmpty()) {
            janela.exibirMensagem("Nenhum código para analisar.", true);
            return;
        }

        try {
            Linguagem20252.clearParseErrors();
            Linguagem20252 parser = new Linguagem20252(new StringReader(codigo));
            parser.programa();

            if (Linguagem20252.parseErrors.isEmpty()) {
                janela.exibirMensagem("Análise sintática concluída sem erros.", false);
            } else {
                StringBuilder sb = new StringBuilder("Foram encontrados erros sintáticos:\n");
                for (String msg : Linguagem20252.parseErrors) {
                    sb.append("• ").append(msg).append("\n");
                }
                janela.exibirMensagem(sb.toString(), true);
            }

        } catch (ParseException e) {
            janela.exibirMensagem("Erro sintático: " + e.getMessage(), true);

        } catch (Exception e) {
            janela.exibirMensagem("Erro inesperado: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }
}
