package ThreeLayerCS;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client extends JFrame {
    private BusinessLogic businessLogic = new BusinessLogic();

    public Client() {
        initFrame();

        initSearch();

        initButton();

        personList(null);

        this.setVisible(true);
    }

    private void initSearch() {
        JLabel nameLabel = new JLabel("姓名:");
        JTextField nameField = new JTextField(200);
        JLabel addressLabel = new JLabel("地址:");
        JTextField addressField = new JTextField(200);
        JLabel phoneLabel = new JLabel("电话:");
        JTextField phoneField = new JTextField(11);
        JLabel cue = new JLabel("支持模糊查询");
        JButton searchButton = new JButton("查询");

        nameLabel.setBounds(150, 10, 30, 20);
        nameField.setBounds(180, 10, 100, 20);
        addressLabel.setBounds(290, 10, 30, 20);
        addressField.setBounds(320, 10, 100, 20);
        phoneLabel.setBounds(430, 10, 30, 20);
        phoneField.setBounds(460, 10, 100, 20);
        cue.setBounds(250, 40, 300, 20);
        searchButton.setBounds(520, 40, 100, 20);

        ((AbstractDocument) phoneField.getDocument()).setDocumentFilter(new DocumentFilter() {
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

        searchButton.addActionListener(e -> {
            String nameKeyword = nameField.getText();
            String addressKeyword = addressField.getText();
            String phoneKeyword = phoneField.getText();
            if (!isNumeric(phoneKeyword)) {
                JOptionPane.showMessageDialog(this, "电话号码只能为数字！", "提示", JOptionPane.WARNING_MESSAGE);
            } else {
                List<String> mes = new ArrayList<>();
                mes.add(nameKeyword); mes.add(addressKeyword); mes.add(phoneKeyword);
                personList(mes);
            }
        });

        this.getContentPane().setLayout(null);
        this.getContentPane().add(nameLabel);
        this.getContentPane().add(nameField);
        this.getContentPane().add(addressLabel);
        this.getContentPane().add(addressField);
        this.getContentPane().add(phoneLabel);
        this.getContentPane().add(phoneField);
        this.getContentPane().add(cue);
        this.getContentPane().add(searchButton);
    }

    private void initButton() {
        JButton addPersonButton = new JButton("添加联系人");
        addPersonButton.setBounds(10, 10, 100, 40);

        addPersonButton.addActionListener(e -> {
            try {
                new OperatePersonFrame(this, "添加", null);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        this.getContentPane().setLayout(null);
        this.getContentPane().add(addPersonButton);
    }

    DefaultTableModel tableModel = new DefaultTableModel();
    JTable table = new JTable(tableModel) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 4 || column == 5;  // 仅"删除"和"修改列可以点击"
        }
    };
    JScrollPane jScrollPane = new JScrollPane(table);

    public void personList(List<String> mes) {
        String[][] data;
        if (mes == null) data = businessLogic.getContacts(null, null, null);
        else data = businessLogic.getContacts(mes.get(0), mes.get(1), mes.get(2));
        String[] title = {"序号", "姓名", "地址", "电话", "修改", "删除"};
        tableModel.setDataVector(data, title);
        table.getColumn("修改").setCellRenderer(new ButtonRenderer());
        table.getColumn("修改").setCellEditor(new ButtonEditor(table, this));
        table.getColumn("删除").setCellRenderer(new ButtonRenderer());
        table.getColumn("删除").setCellEditor(new ButtonEditor(table, this));
        jScrollPane.setBounds(0, 70, 700, 400);
        this.getContentPane().add(jScrollPane);
    }

    private void initFrame() {
        this.setSize(700, 500);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setTitle("个人通讯录(三层C/S)");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private boolean isNumeric(String str) {
        if (str.isEmpty())
            return true;
        else return str.matches("\\d+"); // 匹配只包含数字的字符串
    }

    static class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        protected JButton button;
        private final JTable table;

        public ButtonEditor(JTable table, Client client) {
            this.table = table;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                String value = table.getValueAt(row, column).toString();
                if ("删除".equals(value)) {
                    int choose = JOptionPane.showConfirmDialog(null, "确定删除该联系人吗", "确认删除", JOptionPane.YES_NO_OPTION);
                    if (choose == JOptionPane.YES_OPTION) {
                        try {
                            String id = table.getValueAt(row, 0).toString();
                            client.businessLogic.deleteContact(id);
                            client.personList(null);  // 更新联系人表
                            JOptionPane.showMessageDialog(null, "删除成功", "提示", JOptionPane.PLAIN_MESSAGE);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                } else if ("修改".equals(value)) {
                    String id = table.getValueAt(row, 0).toString();
                    String name = table.getValueAt(row, 1).toString();
                    String address = table.getValueAt(row, 2).toString();
                    String phone = table.getValueAt(row, 3).toString();
                    List<String> mes = Arrays.asList(id, name, address, phone);
                    new OperatePersonFrame(client, "修改", mes);
                }
                fireEditingStopped();
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText((value == null) ? "" : value.toString());
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            return button;
        }

        public Object getCellEditorValue() {
            return button.getText();
        }

        public boolean stopCellEditing() {
            fireEditingStopped();
            return true;
        }

        public void cancelCellEditing() {
            fireEditingCanceled();
        }
    }

    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            setText((value == null) ? "" : value.toString());
            setEnabled(table.isEnabled());
            setFont(table.getFont());
            setHorizontalAlignment(JLabel.CENTER);
            return this;
        }
    }

    public static void main(String[] args) throws Exception {
        new Client();
    }
}
