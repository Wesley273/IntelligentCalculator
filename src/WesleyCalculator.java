import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class WesleyCalculator extends JFrame {
    private final Container container = this.getContentPane();
    private final JTextField textField = new JTextField();
    private final JMenu chooseMode = new JMenu("Standard");
    private final JMenuItem standard = new JMenuItem("Standard");
    private final JMenuItem alphaMode = new JMenuItem("Alpha Mode");
    private final JPanel cards = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel standardPanel = new JPanel();
    private final JPanel alphaPanel = new JPanel();
    private final AlphaAPI alphaAPI = new AlphaAPI();

    private WesleyCalculator() {
        //设置窗口基本属性和文本输入框
        setBasic();
        cards.setLayout(cardLayout);
        container.add(cards, BorderLayout.CENTER);
        //设置StandardPanel
        setStandardPanel();
        setAlphaPanel();
        cards.add("Standard", standardPanel);
        cards.add("Alpha", alphaPanel);
        cards.updateUI();
    }

    private void setBasic() {
        //设置窗体的标题
        setTitle("Wesley's Calculator");
        //设置窗体的显示位置及大小
        setBounds(200, 100, 340, 545);
        //设置窗体关闭按钮的动作为退出
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout calculatorLayout = new BorderLayout();
        calculatorLayout.setHgap(5);
        calculatorLayout.setVgap(5);
        container.setLayout(calculatorLayout);
        //加入菜单
        JMenuBar myMenuBar = new JMenuBar();
        myMenuBar.setBackground(Color.white);
        setJMenuBar(myMenuBar);
        chooseMode.setFont(new Font("Lao UI", Font.BOLD, 14));
        myMenuBar.add(chooseMode);
        standard.setBackground(Color.white);
        standard.setFont(new Font("Lao UI", Font.BOLD, 13));
        chooseMode.add(standard);
        alphaMode.setBackground(Color.white);
        alphaMode.setFont(new Font("Lao UI", Font.BOLD, 13));
        chooseMode.add(alphaMode);
        //加入一个输入框
        textField.setHorizontalAlignment(SwingConstants.TRAILING);
        textField.setBorder(new BorderUIResource.LineBorderUIResource(Color.LIGHT_GRAY));
        textField.setFont(new Font("Lao UI", Font.BOLD, 28));
        textField.setPreferredSize(new Dimension(12, 115));
        container.add(textField, BorderLayout.NORTH);
        //菜单栏事件
        standard.addActionListener(actionEvent -> {
            textField.setText("");
            chooseMode.setText("Standard");
            cardLayout.show(cards, "Standard");
        });
        alphaMode.addActionListener(actionEvent -> {
            textField.setText("");
            chooseMode.setText("Alpha Mode");
            cardLayout.show(cards, "Alpha");
        });
    }

    private void setStandardPanel() {
        //标准模式下的Panel
        //按键的布局格式为网状
        GridLayout buttonLayout = new GridLayout(7, 5);
        buttonLayout.setHgap(4);
        buttonLayout.setVgap(3);
        standardPanel.setLayout(buttonLayout);
        //设置按键
        JButton[][] buttons = new JButton[7][5];
        String[][] buttonNames = {
                {"sin", "cos", "Del", "CE", "AC"},
                {"<html>&int", "mod", "|x|", "(", ")"},
                {"Dif", "<html>x<sup>y</sup>", "ln", "π", "e"},
                {"lim", "1", "2", "3", "+"},
                {"x", "4", "5", "6", "-"},
                {"y", "7", "8", "9", "×"},
                {"n!", ".", "0", "/", "="}
        };
        for (int row = 0; row < buttonNames.length; row++) {
            for (int column = 0; column < buttonNames[row].length; column++) {
                buttons[row][column] = new JButton(buttonNames[row][column]);
                buttons[row][column].setFont(new Font("Lao UI", Font.BOLD, 15));
                buttons[row][column].setBackground(Color.white);
                buttons[row][column].setBorder(null);
                switch (buttonNames[row][column]) {
                    case "int":
                    case "sin":
                    case "cos":
                        setButton(buttons[row][column], buttonNames[row][column], "()", 4);
                        break;
                    case "|x|":
                        setButton(buttons[row][column], "abs", "()", 4);
                        break;
                    case "<html>x<sup>y</sup>":
                        setButton(buttons[row][column], "^", "", 1);
                        break;
                    case "<html>&int":
                        setButton(buttons[row][column], "int", "()", 4);
                        break;
                    case "Dif":
                        setButton(buttons[row][column], "d()/dx", "", 2);
                        break;
                    case "lim":
                        setButton(buttons[row][column], "limit(,x->)", "", 6);
                        break;
                    case "ln":
                        setButton(buttons[row][column], buttonNames[row][column], "()", 3);
                        break;
                    case "mod":
                        setButton(buttons[row][column], "%", "", 1);
                        break;
                    case "n!":
                        setButton(buttons[row][column], "!", "", 1);
                        break;
                    case "Del":
                        buttons[row][column].addActionListener(actionEvent -> {
                            textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
                            textField.setCaretPosition(textField.getText().length());
                            textField.requestFocus();
                        });
                        break;
                    case "CE":
                        buttons[row][column].addActionListener(actionEvent -> {
                            if (textField.getText().contains("=")) {
                                textField.setText(textField.getText().substring(0, textField.getText().indexOf("=")));
                            }
                            textField.setCaretPosition(textField.getText().length());
                            textField.requestFocus();
                        });
                        break;
                    case "AC":
                        buttons[row][column].addActionListener(actionEvent -> textField.setText(""));
                        buttons[row][column].setBackground(Color.decode("#DB5845"));
                        buttons[row][column].setForeground(Color.white);
                        break;
                    case "=":
                        buttons[row][column].addActionListener(actionEvent -> getStandardResult());
                        buttons[row][column].setBackground(Color.decode("#4998D3"));
                        buttons[row][column].setForeground(Color.white);
                        break;
                    default:
                        setButton(buttons[row][column], buttonNames[row][column], "", 1);
                        break;
                }
                //将按钮添加到面板中
                standardPanel.add(buttons[row][column]);
            }
        }
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == 10 & chooseMode.getText().equals("Standard")) {
                    getStandardResult();
                }
            }
        });
        standardPanel.updateUI();
    }

    private void setAlphaPanel() {
        //Alpha模式下的Panel
        //布局格式为网状
        BorderLayout buttonLayout = new BorderLayout(5, 5);
        alphaPanel.setLayout(buttonLayout);
        alphaPanel.setBackground(Color.white);
        //设置提醒标签
        JLabel remind = new JLabel("        Get Whatever You Want");
        remind.setFont(new Font("Lao UI", Font.BOLD, 22));
        alphaPanel.add(remind, BorderLayout.NORTH);
        //设置结果显示区域
        JTextArea result = new JTextArea();
        //设置自动换行
        result.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(result);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        result.setBackground(Color.white);
        result.setEditable(false);
        result.setFont(new Font("Lao UI", Font.BOLD, 14));
        alphaPanel.add(scroll, BorderLayout.CENTER);
        //添加事件
        //文本框的回车监听
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == 10 & chooseMode.getText().equals("Alpha Mode")) {
                    result.setText("Please wait a minute...");
                    Thread queryThread = new Thread(() -> result.setText(alphaAPI.fullResultQuery(textField.getText())));
                    queryThread.start();
                }
            }
        });
        alphaPanel.updateUI();
    }

    private void setButton(JButton button, String buttonName, String displayForm, int moveCursorPosition) {
        //这里用于控制输入一个运算符或者数字后的展示形式及光标位置
        button.addActionListener(actionEvent -> {
            StringBuffer showExpression = new StringBuffer(textField.getText());
            int cursorPosition = textField.getCaretPosition();
            showExpression.insert(cursorPosition, buttonName + displayForm);
            textField.setText(String.valueOf(showExpression));
            textField.setCaretPosition(cursorPosition + moveCursorPosition);
            textField.requestFocus();
        });
    }

    private void getStandardResult() {
        Thread resultThread = new Thread(() -> {
            try {
                getLocalResult();
            } catch (Exception e) {
                try {
                    e.printStackTrace();
                    textField.setText(textField.getText() + "=" + alphaAPI.fastQuery(textField.getText()));
                } catch (Exception exception) {
                    exception.printStackTrace();
                    textField.setText("Modify the expression or try Alpha Mode");
                }
            }
            textField.setCaretPosition(textField.getText().length());
            textField.requestFocus();
        });
        resultThread.start();
    }

    private void getLocalResult() throws Exception {
        String strExpression = textField.getText().replace("×", "*");
        ReversePolishNotation notation = new ReversePolishNotation(strExpression);
        String result = textField.getText() + "=" + notation.getResult();
        if (result.endsWith(".0")) {
            result = result.substring(0, result.length() - 2);
        }
        textField.setText(result);
        textField.requestFocus();
    }

    public static void main(String[] args) throws Exception {
        WesleyCalculator frame = new WesleyCalculator();
        frame.setResizable(false);
        frame.setVisible(true);
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
}