package TwoLayerCS;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.util.List;

public class OperatePersonFrame extends JFrame {
    String operate;
    List<String> mes;
    JButton operateButton;

    JTextField IName = new JTextField();
    JTextField IAddress = new JTextField();
    JTextField IPhone = new JTextField();

    Client mainFrame;  // 保存主窗口对象
    private DataManager dataManager = new DataManager();

    public OperatePersonFrame(Client mainFrame, String operate, List<String> mes) {
        this.mainFrame = mainFrame;
        this.operate = operate;
        this.mes = mes;
        operateButton = new JButton(operate);
        initFrame();

        initLabel();

        initTextFrame();

        initButton();

        this.setVisible(true);
    }

    private void initFrame() {
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setTitle(operate + "联系人");
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);  // 修改为隐藏窗口而不是退出
    }

    private void initLabel() {
        JLabel text1 = new JLabel("姓名");
        JLabel text2 = new JLabel("地址");
        JLabel text3 = new JLabel("电话号码");

        text1.setFont(new Font("Dialog", Font.BOLD, 20));
        text2.setFont(new Font("Dialog", Font.BOLD, 20));
        text3.setFont(new Font("Dialog", Font.BOLD, 20));

        text1.setBounds(150, 20, 200, 50);
        text2.setBounds(150, 100, 200, 50);
        text3.setBounds(150, 180, 200, 50);

        this.getContentPane().setLayout(null);
        this.getContentPane().add(text1);
        this.getContentPane().add(text2);
        this.getContentPane().add(text3);
    }

    private void initTextFrame() {
        IName.setBounds(150, 60, 300, 25);
        IAddress.setBounds(150, 140, 300, 25);
        IPhone.setBounds(150, 220, 300, 25);

        // 设置最大字符数为11
        ((AbstractDocument) IPhone.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= 11) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() + text.length() - length) <= 11) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        if (mes != null) {
            IName.setText(mes.get(1));
            IAddress.setText(mes.get(2));
            IPhone.setText(mes.get(3));
        }

        this.getContentPane().setLayout(null);
        this.getContentPane().add(IName);
        this.getContentPane().add(IAddress);
        this.getContentPane().add(IPhone);
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+"); // 匹配只包含数字的字符串
    }

    private void initButton() {
        operateButton.setBounds(250, 280, 100, 40);

        operateButton.addActionListener(e -> {
            if(IName.getText().isEmpty() || IAddress.getText().isEmpty() || IPhone.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,"请输入完整的联系人信息","提示",JOptionPane.WARNING_MESSAGE);
            }
            else if(!isNumeric(IPhone.getText())) {
                JOptionPane.showMessageDialog(this,"电话号码只能为数字！","提示",JOptionPane.WARNING_MESSAGE);
            }
            else{
                try {
                    boolean res = false;
                    if (mes == null) {
                        res = dataManager.addContact(IName.getText(), IAddress.getText(), IPhone.getText());
                    } else {
                        res = dataManager.updateContact(mes.get(0), IName.getText(), IAddress.getText(), IPhone.getText());
                    }
                    if(res){
                        JOptionPane.showMessageDialog(this, operate + "成功", "提示", JOptionPane.PLAIN_MESSAGE);
                        mainFrame.personList(null);  // 更新联系人列表
                        this.setVisible(false);
                    }
                    else{
                        JOptionPane.showMessageDialog(this,"电话号码已存在，请使用其他电话号码!","提示",JOptionPane.WARNING_MESSAGE);
                    }

                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        this.getContentPane().add(operateButton);
    }
}
