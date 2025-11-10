package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class JanelaCodigoObjeto extends JFrame {

    private JTable tabela;
    private DefaultTableModel modelo;

    public JanelaCodigoObjeto(List<String[]> linhas) {
        super("Código objeto");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        String[] colunas = {"Endereço", "Código", "Parâmetro"};
        modelo = new DefaultTableModel(colunas, 0);

        if (linhas != null) {
            for (String[] linha : linhas) {
                modelo.addRow(linha);
            }
        }

        tabela = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabela);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scroll, BorderLayout.CENTER);
    }

    public void atualizar(List<String[]> linhas) {
        modelo.setRowCount(0);
        for (String[] linha : linhas) {
            modelo.addRow(linha);
        }
    }
}
