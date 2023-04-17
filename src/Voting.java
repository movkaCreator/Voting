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

    private String[][] shifr = {{"УъђѼҒӅԊՄ\u0590פ٤ʮ̘\u07BFࡄࣔՔ\u0A29ૐஈ࠰അ෪\u0EE6ྼႹᆸາᏄჶᘂፔ"},
            {"ЮѭқӹՑהٷܝߣࢿ\u09D5ۃߟെນစ൯ጼᓩᚵᑿ᪁Ჱỗℶ⎄◹⢇✟\u2DCFⳅ㎗㊽"},
            {"ЧюњҌҮӱՊ֜\u0604ٸܜΎФࣻ\u09B4\u0A7C్ܸസุବဿᆒዋᐒᕨ᛭ᐲᦨᝒ᳣\u1A9C"},
            {"ФѠҗԊփس\u070Fߺऑ\u0A4A\u0BC9बૉႽኩᒺዕᥟᯕṶᴡ␖✴⩦ⷔㄿ㓨㢪㲗㲆䒶䔚䵵丰"},
            {"ЮўѶwҼÛԻօאؽږ܌ޔࠑࢠष৮ધܴఔ\u0CDCෂຑེၺ൛ኁᎌ႔ᗘᜏᠽᦉ\u1AD9"}};

    private int keys[][] = {{5, 3, 4}, {12, 20, 13}, {7, 1, 8}, {18, 11, 5}, {5, 10, 12}};

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
                JPanel buttonPanel = new JPanel(new FlowLayout());
                JButton confirmBtn = new JButton("Подтвердить");
                JButton rejectBtn = new JButton("Отклонить");
                buttonPanel.add(confirmBtn);
                buttonPanel.add(rejectBtn);

                StringBuilder sb = new StringBuilder("Проверка:\n");
                for (int i = 0; i < checkBoxes.length; i++) {
                    if (checkBoxes[i].isSelected()) {
                        sb.append(surnames[i] + ": " + decrypt(textFields[i].getText(), keys[i]) + "\n");
                    }
                }

                JFrame frame = new JFrame("Подтверждение");

                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                // Чтобы сделать поле подтверждение нередактируемым - заменить этот блок закомменченым
                frame.add(new JScrollPane(new JTextArea(sb.toString())),
                        BorderLayout.CENTER);
                //
                /*
                JTextArea text = new JTextArea(sb.toString());
                text.disable();
                frame.add(text, BorderLayout.CENTER)
                 */
                frame.add(buttonPanel, BorderLayout.SOUTH);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                dispose();

                confirmBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        JFrame resultFrame = new JFrame("Результат");
                        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        resultFrame.setLocationRelativeTo(null);
                        resultFrame.add(new JScrollPane(new JTextArea("Сообщения подтверждены")));
                        resultFrame.pack();
                        resultFrame.setVisible(true);
                        frame.dispose();
                    }
                });
                rejectBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        JFrame resultFrame = new JFrame("Результат");
                        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        resultFrame.setLocationRelativeTo(null);
                        resultFrame.add(new JScrollPane(new JTextArea("Сообщения отклонены")));
                        resultFrame.pack();
                        resultFrame.setVisible(true);
                        frame.dispose();
                    }
                });
            }
        });


        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonsPanel.add(selectBtn, BorderLayout.SOUTH);
        buttonsPanel.add(sendBtn, BorderLayout.SOUTH);

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