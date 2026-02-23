package com.divorce.estimator.ui;

import com.divorce.estimator.model.Asset;
import com.divorce.estimator.model.PropertyType;
import com.divorce.estimator.model.Spouse;

import javax.swing.*;
import java.awt.*;

public class AssetDialog extends JDialog {

    private Asset createdAsset = null;

    private final JTextField nameField = new JTextField(20);
    private final JComboBox<PropertyType> propertyTypeBox = new JComboBox<>(PropertyType.values());
    private final JComboBox<Spouse> ownerBox = new JComboBox<>(Spouse.values());
    private final JTextField valueField = new JTextField(10);
    private final JTextField debtField = new JTextField(10);

    public AssetDialog(Frame parent) {
        super(parent, "Mal Ekle", true); // modal
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(420, 260);
        setLocationRelativeTo(parent);

        initUI();
    }

    private void initUI() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int r = 0;

        // Ad
        gbc.gridx = 0; gbc.gridy = r; gbc.weightx = 0;
        form.add(new JLabel("Mal Adı:"), gbc);
        gbc.gridx = 1; gbc.gridy = r; gbc.weightx = 1;
        form.add(nameField, gbc);
        r++;

        // Tür
        gbc.gridx = 0; gbc.gridy = r; gbc.weightx = 0;
        form.add(new JLabel("Tür:"), gbc);
        gbc.gridx = 1; gbc.gridy = r; gbc.weightx = 1;
        form.add(propertyTypeBox, gbc);
        r++;

        // Sahip
        gbc.gridx = 0; gbc.gridy = r; gbc.weightx = 0;
        form.add(new JLabel("Sahip:"), gbc);
        gbc.gridx = 1; gbc.gridy = r; gbc.weightx = 1;
        form.add(ownerBox, gbc);
        r++;

        // Değer
        gbc.gridx = 0; gbc.gridy = r; gbc.weightx = 0;
        form.add(new JLabel("Değer (TL):"), gbc);
        gbc.gridx = 1; gbc.gridy = r; gbc.weightx = 1;
        form.add(valueField, gbc);
        r++;

        // Borç
        gbc.gridx = 0; gbc.gridy = r; gbc.weightx = 0;
        form.add(new JLabel("Borç (TL):"), gbc);
        gbc.gridx = 1; gbc.gridy = r; gbc.weightx = 1;
        form.add(debtField, gbc);
        r++;

        // Butonlar
        JButton saveBtn = new JButton("Kaydet");
        JButton cancelBtn = new JButton("İptal");

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(cancelBtn);
        buttons.add(saveBtn);

        cancelBtn.addActionListener(e -> dispose());
        saveBtn.addActionListener(e -> onSave());

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);

        // Varsayılanlar
        propertyTypeBox.setSelectedItem(PropertyType.ACQUIRED);
        ownerBox.setSelectedItem(Spouse.A);
        debtField.setText("0");

        //Enum->turkce verison edinilmis kisisel
        propertyTypeBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof PropertyType pt) {
                    setText(pt == PropertyType.ACQUIRED ? "Edinilmiş" : "Kişisel");
                }
                return this;
            }
        });

        ownerBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof Spouse s) {
                    setText(s == Spouse.A ? "Eş A" : "Eş B");
                }
                return this;
            }
        });
    }

    private void onSave() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mal adı boş olamaz.", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double value;
        double debt;

        try {
            value = parseDouble(valueField.getText().trim());
            debt = parseDouble(debtField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Değer/Borç sayısal olmalı. Örn: 150000 veya 150000.50", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (value < 0 || debt < 0) {
            JOptionPane.showMessageDialog(this, "Değer ve borç negatif olamaz.", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (debt > value) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Borç, değerden büyük görünüyor. Yine de kaydedilsin mi?",
                    "Uyarı",
                    JOptionPane.YES_NO_OPTION
            );
            if (choice != JOptionPane.YES_OPTION) return;
        }

        PropertyType type = (PropertyType) propertyTypeBox.getSelectedItem();
        Spouse owner = (Spouse) ownerBox.getSelectedItem();

        createdAsset = new Asset(name, type, owner, value, debt);
        dispose();
    }

    private double parseDouble(String s) {
        // TR klavyede virgül alışkanlığı için destek
        return Double.parseDouble(s.replace(',', '.'));
    }

    public Asset getCreatedAsset() {
        return createdAsset;
    }
}