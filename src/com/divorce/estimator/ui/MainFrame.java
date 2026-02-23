package com.divorce.estimator.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainFrame extends JFrame {

    private JTable assetTable;
    private DefaultTableModel tableModel;
    private JTextArea resultArea;

    public MainFrame() {

        DisclaimerDialog dialog = new DisclaimerDialog(this);
        dialog.setVisible(true);

        if (!dialog.isAccepted()) {
            System.exit(0);
        }

        setTitle("Boşanma Mal Tasfiye Hesaplayıcı");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initUI();

        setVisible(true);
    }

    private void initUI() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // === TABLO ===
        String[] columns = {"Ad", "Tür", "Sahip", "Değer (TL)", "Borç (TL)"};
        tableModel = new DefaultTableModel(columns, 0);
        assetTable = new JTable(tableModel);

        JScrollPane tableScroll = new JScrollPane(assetTable);
        mainPanel.add(tableScroll, BorderLayout.CENTER);

        // === BUTONLAR ===
        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Mal Ekle");
        JButton deleteButton = new JButton("Mal Sil");
        JButton calculateButton = new JButton("Hesapla");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(calculateButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        // === SONUÇ ===
        resultArea = new JTextArea(5, 20);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        JScrollPane resultScroll = new JScrollPane(resultArea);
        mainPanel.add(resultScroll, BorderLayout.SOUTH);

        add(mainPanel);

        // === ACTION LISTENER ===

        addButton.addActionListener(e -> {
            AssetDialog dialog = new AssetDialog(this);
            dialog.setVisible(true);

            var asset = dialog.getCreatedAsset();
            if (asset != null) {
                tableModel.addRow(new Object[]{
                        asset.getName(),
                        toTurkish(asset.getPropertyType()),
                        toTurkish(asset.getOwner()),
                        asset.getValue(),
                        asset.getDebt()
                });
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = assetTable.getSelectedRow();
            if (selectedRow >= 0) {
                tableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Silmek için bir mal seçin.");
            }
        });
    }

    private String toTurkish(Object o) {
        if (o instanceof com.divorce.estimator.model.PropertyType pt) {
            return pt == com.divorce.estimator.model.PropertyType.ACQUIRED ? "Edinilmiş" : "Kişisel";
        }
        if (o instanceof com.divorce.estimator.model.Spouse s) {
            return s == com.divorce.estimator.model.Spouse.A ? "Eş A" : "Eş B";
        }
        return String.valueOf(o);
    }
}