package com.divorce.estimator.ui;

import com.divorce.estimator.model.Asset;
import com.divorce.estimator.model.PropertyType;
import com.divorce.estimator.model.Spouse;
import com.divorce.estimator.service.CalculationResult;
import com.divorce.estimator.service.Calculator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainFrame extends JFrame {

    private JTable assetTable;
    private DefaultTableModel tableModel;
    private JTextArea resultArea;

    private final List<Asset> assetList = new ArrayList<>();
    private final Calculator calculator = new Calculator();
    private final NumberFormat currencyFmt = NumberFormat.getNumberInstance(new Locale("tr", "TR"));

    public MainFrame() {

        DisclaimerDialog dialog = new DisclaimerDialog(this);
        dialog.setVisible(true);

        if (!dialog.isAccepted()) {
            System.exit(0);
        }

        currencyFmt.setMinimumFractionDigits(2);
        currencyFmt.setMaximumFractionDigits(2);

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
        resultArea = new JTextArea(7, 20);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        JScrollPane resultScroll = new JScrollPane(resultArea);
        mainPanel.add(resultScroll, BorderLayout.SOUTH);

        add(mainPanel);

        // === ACTION LISTENERS ===

        addButton.addActionListener(e -> {
            AssetDialog assetDialog = new AssetDialog(this);
            assetDialog.setVisible(true);

            Asset asset = assetDialog.getCreatedAsset();
            if (asset != null) {
                assetList.add(asset);
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
                assetList.remove(selectedRow);
                tableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Silmek için bir mal seçin.");
            }
        });

        calculateButton.addActionListener(e -> {
            if (assetList.isEmpty()) {
                resultArea.setText("Hesaplanacak mal bulunamadı. Lütfen önce mal ekleyin.");
                return;
            }

            CalculationResult result = calculator.calculate(assetList);
            resultArea.setText(formatResult(result));
        });
    }

    private String formatResult(CalculationResult r) {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("Eş A — Edinilmiş Net : %s TL%n", fmt(r.getANet())));
        sb.append(String.format("Eş B — Edinilmiş Net : %s TL%n", fmt(r.getBNet())));
        sb.append("─".repeat(42)).append("\n");
        sb.append(String.format("Toplam Havuz         : %s TL%n", fmt(r.getTotalNet())));
        sb.append(String.format("Eşit Pay             : %s TL%n", fmt(r.getEqualShare())));
        sb.append("─".repeat(42)).append("\n");

        if (r.getTotalNet() == 0) {
            sb.append("Paylaşılacak edinilmiş mal bulunmuyor.");
        } else if (r.getAReceivable() > 0) {
            sb.append(String.format("► Eş B, Eş A'ya %s TL öder.", fmt(r.getAReceivable())));
        } else if (r.getBReceivable() > 0) {
            sb.append(String.format("► Eş A, Eş B'ye %s TL öder.", fmt(r.getBReceivable())));
        } else {
            sb.append("► Eşit paylaşım sağlanmış, ödeme gerekmez.");
        }

        return sb.toString();
    }

    private String fmt(double value) {
        return currencyFmt.format(value);
    }

    private String toTurkish(Object o) {
        if (o instanceof PropertyType pt) {
            return pt == PropertyType.ACQUIRED ? "Edinilmiş" : "Kişisel";
        }
        if (o instanceof Spouse s) {
            return s == Spouse.A ? "Eş A" : "Eş B";
        }
        return String.valueOf(o);
    }
}
