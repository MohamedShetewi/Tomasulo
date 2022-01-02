package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;


public class View extends JFrame {

    String[] instructions = {"ADD", "SUB", "MUL", "DIV", "LD", "SW"};

    private final JPanel instructionSection;
    private final ArrayList<InstructionEntry> instructionEntries;


    private int divLatency = 0;
    private int mulLatency = 0;
    private int addLatency = 0;
    private int subLatency = 0;
    private int lwLatency = 0;
    private int swLatency = 0;


    public View() {
        super();
        setResizable(false);
        setSize(1200, 1200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        instructionEntries = new ArrayList<InstructionEntry>();
        instructionSection = new JPanel();

        setInstructionSection();


        setLatenciesSection();






        setVisible(true);
        repaint();
        revalidate();
    }

    private void setInstructionSection(){
        GridLayout gridLayout = new GridLayout(0,4);
        gridLayout.setHgap(7);
        gridLayout.setVgap(7);
        instructionSection.setLayout(gridLayout);
        JScrollPane jsp = new JScrollPane(instructionSection);
        setComponentView(jsp, 50, 50, 500, 200, false);
        JLabel instructionLabel = new JLabel("Instructions");
        setComponentView(instructionLabel, 50, 8, 50, 50, false);

        JButton addInstructionButton = new JButton("Add Instruction");
        addInstructionButton.setBackground(new Color(255,255,255));
        addInstructionButton.addActionListener(e -> {
            addInstruction();
        });
        setComponentView(addInstructionButton, 575, 75, 20, 10, false);
    }


    private void setLatenciesSection(){
        GridLayout g = (new GridLayout(0,2));
        g.setVgap(5);
        JPanel latenciesPanel = new JPanel(g);

        JTextArea divLatencyField = new JTextArea();
        JTextPane mulLatencyField = new JTextPane();
        JTextPane addLatencyField = new JTextPane();
        JTextPane subLatencyField = new JTextPane();
        JTextPane lwLatencyField = new JTextPane();
        JTextPane swLatencyField = new JTextPane();


        divLatencyField.setSize(new Dimension(1,1));

        JLabel divLabel = new JLabel("Division Latency");
        JLabel mulLabel = new JLabel("Multiplication Latency");
        JLabel addLabel = new JLabel("Addition Latency");
        JLabel subLabel = new JLabel("Subtraction Latency");
        JLabel swLabel = new JLabel("Store Latency");
        JLabel ldLabel = new JLabel("Load Latency");

        latenciesPanel.add(divLabel);
        latenciesPanel.add(divLatencyField);

        latenciesPanel.add(mulLabel);
        latenciesPanel.add(mulLatencyField);

        latenciesPanel.add(addLabel);
        latenciesPanel.add(addLatencyField);

        latenciesPanel.add(subLabel);
        latenciesPanel.add(subLatencyField);

        latenciesPanel.add(swLabel);
        latenciesPanel.add(swLatencyField);

        latenciesPanel.add(ldLabel);
        latenciesPanel.add(lwLatencyField);


        divLatencyField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                try {
                    divLatency = Integer.parseInt(divLatencyField.getText());
                    System.out.println(divLatency);
                }catch (NumberFormatException ignored){

                }
            }
        });
        mulLatencyField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                try {
                    mulLatency = Integer.parseInt(mulLatencyField.getText());
                }catch (NumberFormatException ignored){

                }
            }
        });
        addLatencyField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                try {
                    addLatency = Integer.parseInt(addLatencyField.getText());
                    System.out.println(addLatency);
                }catch (NumberFormatException ignored){

                }
            }
        });

        subLatencyField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                try {
                    subLatency = Integer.parseInt(subLatencyField.getText());
                    System.out.println(subLatency);
                }catch (NumberFormatException ignored){

                }
            }
        });

        swLatencyField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                try {
                    swLatency = Integer.parseInt(swLatencyField.getText());
                    System.out.println(swLatency);
                }catch (NumberFormatException ignored){

                }
            }
        });

        lwLatencyField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                try {
                    lwLatency = Integer.parseInt(lwLatencyField.getText());
                    System.out.println(lwLatency);
                }catch (NumberFormatException ignored){

                }
            }
        });



        setComponentView(latenciesPanel, 750, 50, 100, 50, false);
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
