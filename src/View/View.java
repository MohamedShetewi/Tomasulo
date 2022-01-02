package View;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class View extends JFrame {

    String[] instructions = {"ADD", "SUB", "MUL", "DIV", "LD", "SW"};

    private final JPanel instructionSection;
    private final ArrayList<InstructionEntry> instructionEntries;


    public View() {
        super();
        setResizable(false);
        setSize(1200, 1200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        instructionEntries = new ArrayList<InstructionEntry>();
        instructionSection = new JPanel();
        GridLayout gridLayout = new GridLayout(0,4);
        gridLayout.setHgap(7);
        gridLayout.setVgap(7);
        instructionSection.setLayout(gridLayout);
        JScrollPane jsp = new JScrollPane(instructionSection);
        setComponentView(jsp, 50, 50, 600, 100, false);



        JButton addInstructionButton = new JButton("Add Instruction");
        addInstructionButton.addActionListener(e -> {
            addInstruction();
        });
        JButton printButton = new JButton("print");
        printButton.addActionListener(e -> {
            print();
        });

        setComponentView(addInstructionButton, 700, 50, 20, 10, false);
        setComponentView(printButton, 700, 90, 20, 10, false);

        setVisible(true);
        repaint();
        revalidate();
    }

    private void setComponentView(JComponent component, int x, int y, int width, int height, boolean right) {
        Dimension dim = component.getPreferredSize();
        Insets insets = component.getInsets();
        component.setBounds((right ? insets.right : insets.left) + x, insets.top + y, dim.width + width, dim.height + height);
        add(component);
    }

    private void addInstruction() {
        JComboBox jComboBox = new JComboBox(instructions);
        JTextPane reg1 = new JTextPane();
        JTextPane reg2 = new JTextPane();
        JTextPane reg3 = new JTextPane();
        instructionSection.add(jComboBox);
        instructionSection.add(reg1);
        instructionSection.add(reg2);
        instructionSection.add(reg3);
        instructionEntries.add(new InstructionEntry(jComboBox, reg1, reg2,reg3));

        repaint();
        revalidate();
    }

    private void print(){
        for(InstructionEntry i : instructionEntries)
        {
            System.out.println(instructions[i.jComboBox.getSelectedIndex()]+" "+i.register1.getText()+" "+i.register2.getText()+" "+i.register3.getText());
        }
    }
}
