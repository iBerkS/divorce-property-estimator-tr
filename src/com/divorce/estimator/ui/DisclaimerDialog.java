package com.divorce.estimator.ui;

import javax.swing.*;
import java.awt.*;

public class DisclaimerDialog extends JDialog {

    private boolean accepted = false;

    public DisclaimerDialog(Frame parent) {
        super(parent, "Önemli Uyarı", true); // modal

        setSize(600, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        initUI();
    }

    private void initUI() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        textArea.setText(
                "ÖNEMLİ HUKUKİ UYARI\n\n" +
                        "Bu uygulama Türk Medeni Kanunu'ndaki edinilmiş mallara katılma rejimine\n" +
                        "ilişkin genel prensiplere dayalı yaklaşık hesaplama yapmaktadır.\n\n" +
                        "Bu yazılım hukuki danışmanlık hizmeti sunmaz ve hiçbir şekilde\n" +
                        "avukat-müvekkil ilişkisi oluşturmaz.\n\n" +
                        "Gerçek mal paylaşımı hesaplamaları; mahkeme takdiri,\n" +
                        "deliller, değer artış payı, katkı payı, kişisel mal istisnaları,\n" +
                        "borç durumları ve özel sözleşmeler gibi birçok faktöre bağlıdır.\n\n" +
                        "Bu uygulamanın ürettiği sonuçlar bağlayıcı değildir.\n" +
                        "Kesin hukuki değerlendirme için bir avukata danışmanız gerekir."
        );

        JScrollPane scrollPane = new JScrollPane(textArea);

        JCheckBox acceptBox = new JCheckBox("Yukarıdaki uyarıyı okudum ve kabul ediyorum.");

        JButton acceptButton = new JButton("Devam Et");
        acceptButton.setEnabled(false);

        acceptBox.addActionListener(e -> {
            acceptButton.setEnabled(acceptBox.isSelected());
        });

        acceptButton.addActionListener(e -> {
            accepted = true;
            dispose();
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(acceptBox, BorderLayout.CENTER);
        bottomPanel.add(acceptButton, BorderLayout.SOUTH);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);
    }

    public boolean isAccepted() {
        return accepted;
    }
}
