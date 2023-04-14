import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Voting extends JFrame {

    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JButton selectBtn;
    private JButton sendBtn;
    private JTextField[] textFields;
    private JCheckBox[] checkBoxes;
    private String[] surnames = { "Хашаев М.А.", "Ткачева А.А.", "Гиевой Д.С.", "Шевченко П.A.", "Путин В.В." };

    private int keys[] = {5, 3, 4};

    public Voting() {
        setTitle("Протокол одновременной подписи");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout());
        leftPanel = new JPanel(new GridLayout(surnames.length, 1, 10, 10));
        rightPanel = new JPanel(new GridLayout(surnames.length, 2, 10, 10));
        selectBtn = new JButton("Выбрать");
        sendBtn = new JButton("Расшифровать входящее сообщение");
        textFields = new JTextField[surnames.length];
        checkBoxes = new JCheckBox[surnames.length];

        // Создаем чекбоксы и текстовые поля
        for (int i = 0; i < surnames.length; i++) {
            checkBoxes[i] = new JCheckBox(surnames[i]);
            leftPanel.add(checkBoxes[i]);
            textFields[i] = new JTextField(15);
            textFields[i].setEditable(false);
            rightPanel.add(textFields[i]);
        }

        selectBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                for (int i = 0; i < surnames.length; i++) {
                    if (checkBoxes[i].isSelected()) {
                        textFields[i].setEditable(true);
                    }
                    else {
                        checkBoxes[i].setEnabled(false);
                    }
                }

                selectBtn.setVisible(false);
                sendBtn.setVisible(true);
            }
        });

        sendBtn.setVisible(false);



        sendBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                StringBuilder sb = new StringBuilder("Проверка:\n");

                for (int i = 0; i < checkBoxes.length; i++) {
                    if (checkBoxes[i].isSelected()) {
                        sb.append(surnames[i] + ": " + decrypt(textFields[i].getText(), keys) + "\n");
                    }
                }

                JFrame resultFrame = new JFrame("Результат");
                resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                resultFrame.setLocationRelativeTo(null);
                JTextArea resultArea = new JTextArea(sb.toString());
                resultArea.setEditable(false);
                resultFrame.add(new JScrollPane(resultArea));
                resultFrame.pack();
                resultFrame.setVisible(true);

                dispose();
            }
        });

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonsPanel.add(selectBtn);
        buttonsPanel.add(sendBtn);

        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    private String decrypt(String text, int[] keys) {
        int A = keys[0];
        int B = keys[1];
        int C = keys[2];
        char[] openText = text.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < openText.length; i++) {
            int k = A * (i * i) + B * i + C;
            int tmp = ((openText[i]) - k);
            while (tmp < 0) {
                tmp += 65536;
            }
            int L = tmp % 65536;
            sb.append((char) L);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Voting frame = new Voting();
        frame.setVisible(true);
    }
}