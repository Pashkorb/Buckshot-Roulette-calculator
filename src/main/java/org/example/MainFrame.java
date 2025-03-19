package org.example;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame{

    private Game currentGame;
    private Integer countRound;

    private JButton buttonCalculate;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textFieldBlank;
    private JTextField textFieldLiveAround;
    private JTextField textField7;
    private JTextField textField8;
    private JCheckBox checkBoxGameRound8;
    private JCheckBox checkBoxGameRound7;
    private JCheckBox checkBoxGameRound1;
    private JCheckBox checkBoxGameRound2;
    private JCheckBox checkBoxGameRound3;
    private JCheckBox checkBoxGameRound4;
    private JCheckBox checkBoxGameRound5;
    private JCheckBox checkBoxGameRound6;
    private JComboBox comboBox1;
    private JTextField textField9;
    private JTextField textField10;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JComboBox comboBox4;
    private JComboBox comboBox5;
    private JComboBox comboBox6;
    private JComboBox comboBox7;
    private JComboBox comboBox8;
    private JPanel mainPanel;
    private JButton buttonClear;
    private JSlider sliderLive;
    private JSlider sliderBlank;

    private JCheckBox[] roundCheckBoxes;
    private JTextField[] roundTextFields;

    public MainFrame()  {

        add(mainPanel);

        countRound=0;
        // Инициализация слайдеров
        sliderLive.setMinimum(0);
        sliderLive.setMaximum(8);
        sliderLive.setValue(0);

        sliderBlank.setMinimum(0);
        sliderBlank.setMaximum(8);
        sliderBlank.setValue(0);
        setSize(1400, 300); // Задаем ширину 800 и высоту 600 пикселей

// Связывание слайдеров с текстовыми полями
        sliderLive.addChangeListener(e -> {
            textFieldLiveAround.setText(String.valueOf(sliderLive.getValue()));
            calculateTotalRounds();
        });

        sliderBlank.addChangeListener(e -> {
            textFieldBlank.setText(String.valueOf(sliderBlank.getValue()));
            calculateTotalRounds();
        });

        // Инициализация массивов для удобного доступа к компонентам
        roundCheckBoxes = new JCheckBox[]{
                checkBoxGameRound1, checkBoxGameRound2, checkBoxGameRound3, checkBoxGameRound4,
                checkBoxGameRound5, checkBoxGameRound6, checkBoxGameRound7, checkBoxGameRound8
        };

        roundTextFields = new JTextField[]{
                textField1, textField2, textField3, textField4,
                textField5, textField6, textField7, textField8
        };

    buttonClear.addActionListener(e->clearAll());
    buttonCalculate.addActionListener(e->calculateRaund());
    checkBoxGameRound1.addActionListener(e->calculateCountRound(checkBoxGameRound1.isSelected()));
    checkBoxGameRound2.addActionListener(e->calculateCountRound(checkBoxGameRound2.isSelected()));
    checkBoxGameRound3.addActionListener(e->calculateCountRound(checkBoxGameRound3.isSelected()));
    checkBoxGameRound4.addActionListener(e->calculateCountRound(checkBoxGameRound4.isSelected()));
    checkBoxGameRound5.addActionListener(e->calculateCountRound(checkBoxGameRound5.isSelected()));
    checkBoxGameRound6.addActionListener(e->calculateCountRound(checkBoxGameRound6.isSelected()));
    checkBoxGameRound7.addActionListener(e->calculateCountRound(checkBoxGameRound7.isSelected()));
    checkBoxGameRound8.addActionListener(e->calculateCountRound(checkBoxGameRound8.isSelected()));

}
    private void calculateTotalRounds() {
        try {
            int live = sliderLive.getValue();
            int blank = sliderBlank.getValue();
            textFieldLiveAround.setText(String.valueOf(live));
            textFieldBlank.setText(String.valueOf(blank));
        } catch (Exception e) {
            // Обработка ошибок
        }
    }
    private void calculateCountRound(boolean operation) {
        if (operation=true){countRound++;}else{countRound--;}
    }

    private void calculateRaund() {
        try {
            int liveAround = Integer.parseInt(textFieldLiveAround.getText());
            int blank = Integer.parseInt(textFieldBlank.getText());
            int fullCount = liveAround + blank;

            Integer[] knowsRound = getRound(fullCount);
            int liveAroundCountResult = getRoundResult(knowsRound);

            calculateRoundResult(knowsRound, liveAroundCountResult, liveAround, blank);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Пожалуйста, введите корректные числа в поля 'Боевые' и 'Холостые'.");
        }
    }


    private void highlightCurrentRound(int index) {
        for (int i = 0; i < roundTextFields.length; i++) {
            if (i == index) {
                roundTextFields[i].setBackground(Color.YELLOW); // Выделяем желтым цветом
            } else {
                roundTextFields[i].setBackground(null); // Снимаем выделение
            }
        }
    }
    private void calculateRoundResult(Integer[] knowsRound, int liveAroundCountResult, int liveAround, int blank) {
        int remainingLive = liveAround - liveAroundCountResult;
        int remainingBlank = blank - (knowsRound.length - liveAroundCountResult);
        remainingLive = Math.max(remainingLive, 0);
        remainingBlank = Math.max(remainingBlank, 0);

        // Обновляем оставшиеся патроны
        textField9.setText(String.valueOf(remainingLive));
        textField10.setText(String.valueOf(remainingBlank));

        int remainingTotal = remainingLive + remainingBlank;
        double probability = 0.0;
        if (remainingTotal > 0) {
            probability = ((double) remainingLive / remainingTotal) * 100;
        }

        DecimalFormat df = new DecimalFormat("0.00%");
        String probabilityString = remainingTotal == 0 ? "0.00%" : df.format(probability / 100);

        // Очищаем все текстовые поля перед расчетом
        for (JTextField textField : roundTextFields) {
            textField.setText("");
        }

        // Находим первый неотыгранный раунд
        int totalRounds = liveAround + blank; // Общее количество раундов
        int nextRoundIndex = -1;

        for (int i = 0; i < roundCheckBoxes.length; i++) {
            if (!roundCheckBoxes[i].isSelected()) {
                nextRoundIndex = i;
                break;
            }
        }

        // Проверяем, что индекс следующего раунда валиден и не превышает общее количество раундов
        if (nextRoundIndex != -1 && nextRoundIndex < totalRounds) {
            roundTextFields[nextRoundIndex].setText(probabilityString); // Вставляем процент
            highlightCurrentRound(nextRoundIndex); // Выделяем текущий раунд
        } else {
            JOptionPane.showMessageDialog(null, "Все раунды отыграны или превышено количество доступных раундов.");
        }
    }




    private int getRoundResult(Integer[] knowsRound) {
        int result=0;
        for (int i :knowsRound
             ) {
            switch (i){
                case 1:
                    if(comboBox1.getSelectedItem().equals("Боевой")){result++;}
                    break;
                case 2:
                    if(comboBox2.getSelectedItem().equals("Боевой")){result++;}
                    break;
                case 3:
                    if(comboBox3.getSelectedItem().equals("Боевой")){result++;}
                    break;
                case 4:
                    if(comboBox4.getSelectedItem().equals("Боевой")){result++;}
                    break;
                case 5:
                    if(comboBox5.getSelectedItem().equals("Боевой")){result++;}
                    break;
                case 6:
                    if(comboBox6.getSelectedItem().equals("Боевой")){result++;}
                    break;
                case 7:
                    if(comboBox7.getSelectedItem().equals("Боевой")){result++;}
                    break;
                case 8:
                    if(comboBox8.getSelectedItem().equals("Боевой")){result++;}
                    break;
            }

        }
        return result;
    }

    private Integer[] getRound(int fullCount) {
        List<Integer> result=new ArrayList<>();
        int i=0;

        while (i<9) {

            switch (i){
                case 1:
                    if(checkBoxGameRound1.isSelected()){result.add(1);}
                    break;
                case 2:
                    if(checkBoxGameRound2.isSelected()){result.add(2);}
                    break;
                case 3:
                    if(checkBoxGameRound3.isSelected()){result.add(3);}
                    break;
                case 4:
                    if(checkBoxGameRound4.isSelected()){result.add(4);}
                    break;
                case 5:
                    if(checkBoxGameRound5.isSelected()){result.add(5);}
                    break;
                case 6:
                    if(checkBoxGameRound6.isSelected()){result.add(6);}
                    break;
                case 7:
                    if(checkBoxGameRound7.isSelected()){result.add(7);}
                    break;
                case 8:
                    if(checkBoxGameRound8.isSelected()){result.add(8);}
                    break;
            }

            i++;
        }
        return result.toArray(new Integer[countRound]);

    }

    public void clearAll() {
        countRound = 0;

        // Очищаем текстовые поля
        textFieldLiveAround.setText("");
        textFieldBlank.setText("");
        textField9.setText("");
        textField10.setText("");

        // Очищаем чекбоксы
        for (JCheckBox checkBox : roundCheckBoxes) {
            checkBox.setSelected(false);
        }

        // Очищаем комбобоксы
        comboBox1.setSelectedIndex(-1);
        comboBox2.setSelectedIndex(-1);
        comboBox3.setSelectedIndex(-1);
        comboBox4.setSelectedIndex(-1);
        comboBox5.setSelectedIndex(-1);
        comboBox6.setSelectedIndex(-1);
        comboBox7.setSelectedIndex(-1);
        comboBox8.setSelectedIndex(-1);

        // Очищаем текстовые поля раундов
        for (JTextField textField : roundTextFields) {
            textField.setText("");
            textField.setBackground(null); // Снимаем выделение
        }
    }



    class Game {
        int gameId;
        String result; // "win" или "lose"
        int initialLive;
        int initialBlank;
        int finalLive;
        int finalBlank;
        List<Round> rounds = new ArrayList<>();

        public Game(int initialLive, int initialBlank) {
            this.initialLive = initialLive;
            this.initialBlank = initialBlank;
        }
    }

    class Round {
        int roundNumber;
        String currentPlayer; // "self" или "opponent"
        String outcome; // "live" или "blank"
        boolean handcuffsUsed;
        int remainingLive;
        int remainingBlank;
        double probability;
        boolean additionalTurn;
        String turnOrder; // "normal" или "handcuffed"

        public Round(int roundNumber, String currentPlayer, String outcome, boolean handcuffsUsed,
                     int remainingLive, int remainingBlank, double probability, boolean additionalTurn, String turnOrder) {
            this.roundNumber = roundNumber;
            this.currentPlayer = currentPlayer;
            this.outcome = outcome;
            this.handcuffsUsed = handcuffsUsed;
            this.remainingLive = remainingLive;
            this.remainingBlank = remainingBlank;
            this.probability = probability;
            this.additionalTurn = additionalTurn;
            this.turnOrder = turnOrder;
        }
    }
}
