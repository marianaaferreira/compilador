package org.example;

import org.example.Linguagem20252;
import org.example.ParseException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

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
        Semantico.codigo.clear();
        Semantico.tabelaSimbolos.clear();
        Semantico.estado = new EstadoSemantico();
        Linguagem20252.clearParseErrors();
        Linguagem20252 parser = new Linguagem20252(new StringReader(codigo));

        try {
            // ETAPA 1: Análise Léxica
            Token t;
            while (true) {
                t = parser.getNextToken();
                if (t.kind == 0) break; // EOF

                switch (t.kind) {
                    case Linguagem20252Constants.SIMBOLO_INVALIDO:
                        Linguagem20252.lexicalErrors.add("Erro léxico na linha " + t.beginLine + ", coluna " + t.beginColumn + ": símbolo inválido '" + t.image + "'");
                        break;
                    case Linguagem20252Constants.LITERAL_NAO_FECHADO:
                        Linguagem20252.lexicalErrors.add("Erro léxico na linha " + t.beginLine + ", coluna " + t.beginColumn + ": constante literal não finalizada");
                        break;
                    case Linguagem20252Constants.COMENTARIO_NAO_FECHADO:
                        Linguagem20252.lexicalErrors.add("Erro léxico na linha " + t.beginLine + ", coluna " + t.beginColumn + ": comentário não finalizado");
                        break;
                    case Linguagem20252Constants.CONST_INT_INVALIDA:
                        Linguagem20252.lexicalErrors.add("Erro léxico na linha " + t.beginLine + ", coluna " + t.beginColumn + ": constante inteira inválida");
                        break;
                    case Linguagem20252Constants.CONST_REAL_INVALIDA:
                        Linguagem20252.lexicalErrors.add("Erro léxico na linha " + t.beginLine + ", coluna " + t.beginColumn + ": constante real inválida");
                        break;
                    case Linguagem20252Constants.IDENTIFICADOR_INVALIDO:
                        Linguagem20252.lexicalErrors.add("Erro léxico na linha " + t.beginLine + ", coluna " + t.beginColumn + ": identificador inválido '" + t.image + "'");
                        break;
                    default:
                        break;
                }
            }

            // Se houver erros léxicos, exibe e para
            if (!Linguagem20252.lexicalErrors.isEmpty()) {
                StringBuilder sb = new StringBuilder("Foram encontrados erros léxicos:\n");
                for (String msg : Linguagem20252.lexicalErrors) {
                    sb.append("• ").append(msg).append("\n");
                }
                janela.exibirMensagem(sb.toString(), true);
                return;
            }

            // ETAPA 2: Análise Sintática
            parser.ReInit(new StringReader(codigo));
            parser.programa();

            if (!Linguagem20252.parseErrors.isEmpty()) {
                StringBuilder sb = new StringBuilder("Foram encontrados erros sintáticos:\n");
                for (String msg : Linguagem20252.parseErrors) {
                    sb.append("• ").append(msg).append("\n");
                }
                janela.exibirMensagem(sb.toString(), true);
            } else {
            janela.exibirMensagem("Análise concluída sem erros.", false);

            // ETAPA 3: Analise semantica e codigo intermediario
            List<String[]> linhasTabela = new ArrayList<>();

            for (Instrucao instrucao : Semantico.codigo) {
                linhasTabela.add(new String[]{
                        String.valueOf(instrucao.getPonteiro()),
                        instrucao.getInstrucao(),
                        String.valueOf(instrucao.getParametro())
                });
            }

            JanelaCodigoObjeto janelaCodigo = new JanelaCodigoObjeto(linhasTabela);
            janelaCodigo.setVisible(true);

                // ETAPA 4: Execução na Máquina Virtual
                try {
                    MaquinaVirtual vm = new MaquinaVirtual(1000);
                    vm.carregarPrograma(Semantico.codigo);

                    janela.exibirMensagem(
                            "Análise concluída sem erros.\n" +
                                    "Iniciando execução da Máquina Virtual...\n",
                            false
                    );

                    vm.run();

                    janela.exibirMensagem(vm.getSaida(), false);

                } catch (Exception ex) {
                    janela.exibirMensagem("Erro durante execução da Máquina Virtual: " + ex.getMessage(), true);
                }
        }

        } catch (ParseException e) {
            parser.reportParseError(e);
            StringBuilder sb = new StringBuilder("Foram encontrados erros sintáticos:\n");
            for (String msg : Linguagem20252.parseErrors) {
                sb.append("• ").append(msg).append("\n");
            }
            janela.exibirMensagem(sb.toString(), true);

        } catch (Exception e) {
            janela.exibirMensagem("Erro inesperado: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }
}
