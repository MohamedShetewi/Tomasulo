package View;

import Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;


public class View extends JFrame {

    private DefaultTableModel addTableModel;
    private DefaultTableModel mulTableModel;
    String[] instructionsNames = {"ADD", "SUB", "MUL", "DIV", "LD", "SW"};

    private final JPanel instructionSection;
    private final ArrayList<InstructionEntry> instructionEntries;
    private JTable loadBufferTable;

    private boolean isSubmitted = false;

    private JButton submitButton;
    private JButton prevButton;
    private int divLatency = 0;
    private int mulLatency = 0;
    private int addLatency = 0;
    private int subLatency = 0;
    private int lwLatency = 0;
    private int swLatency = 0;
    private JTable mulRes;
    private JTable addRes;
    private JPanel addReservationPanel;
    private JPanel mulReservationPanel;


    public View() {
        super();
        setResizable(false);
        setSize(1200, 1200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        instructionEntries = new ArrayList<InstructionEntry>();
        instructionSection = new JPanel();
        submitButton = new JButton("SUBMIT");
        prevButton = new JButton("Previous");
        cycleLabel = new JLabel();
        mulRes = new JTable();
        addRes = new JTable();

        String[][] addData = new String[3][6];
        String[] columns = {"ID", "Operation", "V1", "V2", "Q1", "Q2",};

        addTableModel = new DefaultTableModel(addData, columns);
        mulTableModel = new DefaultTableModel(addData, columns);

        SwingUtilities.invokeLater(() -> {
            setInstructionSection(new ArrayList<>());
            setLatenciesSection();

            setSubmitButtons();

            setReservationPanel();
            setRegFilePanel();
            setStoreBufferPanel();
            setLoadBufferPanel();

        });

        setVisible(true);
        repaint();
        revalidate();
    }


    private void setReservationPanel() {
        addRes.setModel(addTableModel);
        mulRes.setModel(mulTableModel);
        JTableHeader addHeader = addRes.getTableHeader();
        JTableHeader mulHeader = mulRes.getTableHeader();


        addReservationPanel = new JPanel(new BorderLayout());
        addReservationPanel.add(addHeader, BorderLayout.NORTH);
        addReservationPanel.add(addRes, BorderLayout.CENTER);

        mulReservationPanel = new JPanel(new BorderLayout());
        mulReservationPanel.add(mulHeader, BorderLayout.NORTH);
        mulReservationPanel.add(mulRes, BorderLayout.CENTER);

        mulReservationPanel.setVisible(false);
        addReservationPanel.setVisible(false);
        setComponentView(addReservationPanel, 500, 600, 100, 3, false);
        setComponentView(mulReservationPanel, 500, 750, 100, 3, false);


    }

    public void setViewAfterSubmit() {

        JLabel addLabel = new JLabel("Addition and Subtraction Reservation");
        JLabel mulLabel = new JLabel("Multiplication and Division and  Reservation");

        SwingUtilities.invokeLater(() -> {
            registerFilePanel.setVisible(true);
            registerFileLabel.setVisible(true);
            setComponentView(addLabel, 500, 500, 100, 100, false);
            setComponentView(mulLabel, 500, 675, 100, 100, false);
            addReservationPanel.setVisible(true);
            mulReservationPanel.setVisible(true);

            setStoreBufferVisible();
            setLoadBufferVisible();
            prevButton.setVisible(true);
        });


    }

    public void setTables(HashMap<String, RegisterFileSlot> registerFileSlotHashMap, ReservationStation[] addResStation,
                          ReservationStation[] mulResStation, StoreInstruction[] storeInstructions,
                          LoadInstruction[] loadInstructions, int cycle, ArrayList<Instruction> instructionArrayList) {
        setRegFile(registerFileSlotHashMap);
        setReservationStations(addResStation, mulResStation);
        setStoreBuffer(storeInstructions);
        setLoadBuffer(loadInstructions);
        setCycles(cycle);
        setInstructionTable(instructionArrayList);
    }

    public void setSubmitted(boolean submitted) {
        isSubmitted = submitted;
    }

    public boolean isSubmitted() {
        return isSubmitted;
    }

    public JButton getSubmitButton() {
        return submitButton;
    }

    public JButton getPrevButton() {
        return prevButton;
    }

    public int getDivLatency() {
        return divLatency;
    }

    public int getMulLatency() {
        return mulLatency;
    }

    public int getAddLatency() {
        return addLatency;
    }

    public int getSubLatency() {
        return subLatency;
    }

    public int getLwLatency() {
        return lwLatency;
    }

    public int getSwLatency() {
        return swLatency;
    }

    private void setSubmitButtons() {
        submitButton.setBackground(new Color(255, 255, 255));
        prevButton.setBackground(new Color(255, 255, 255));
        new SwingWorker<>() {
            @Override
            protected Object doInBackground() throws Exception {
                setComponentView(prevButton, 700, 300, 50, 10, false);
                setComponentView(submitButton, 900, 300, 50, 10, false);
                return null;
            }
        }.execute();
        prevButton.setVisible(false);
    }

    private void setReservationStations(ReservationStation[] addReservationStation, ReservationStation[] mulReservationStation) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < 3; i++) {
                if (addReservationStation[i] != null) {
                    addTableModel.setValueAt(addReservationStation[i].getId(), i, 0);
                    addTableModel.setValueAt(addReservationStation[i].getOp(), i, 1);
                    addTableModel.setValueAt(addReservationStation[i].getV1(), i, 2);
                    addTableModel.setValueAt(addReservationStation[i].getV2(), i, 3);
                    addTableModel.setValueAt(addReservationStation[i].getQ1(), i, 4);
                    addTableModel.setValueAt(addReservationStation[i].getQ2(), i, 5);
                } else {
                    for (int j = 0; j < 6; j++)
                        addTableModel.setValueAt("", i, j);
                }
                if (mulReservationStation[i] != null) {
                    mulTableModel.setValueAt(mulReservationStation[i].getId(), i, 0);
                    mulTableModel.setValueAt(mulReservationStation[i].getOp(), i, 1);
                    mulTableModel.setValueAt(mulReservationStation[i].getV1(), i, 2);
                    mulTableModel.setValueAt(mulReservationStation[i].getV2(), i, 3);
                    mulTableModel.setValueAt(mulReservationStation[i].getQ1(), i, 4);
                    mulTableModel.setValueAt(mulReservationStation[i].getQ2(), i, 5);
                } else {
                    for (int j = 0; j < 6; j++)
                        mulTableModel.setValueAt("", i, j);
                }
            }

        });

        repaint();
        revalidate();
    }

    JLabel cycleLabel;

    private void setCycles(int cycle) {
        cycleLabel.setText("Cycle " + cycle);
        new SwingWorker<>() {

            @Override
            protected Object doInBackground() throws Exception {
                cycleLabel.setFont(new Font("", Font.BOLD, 25));
                cycleLabel.setBounds(500, -70,
                        400, 200);
                add(cycleLabel);
                return null;
            }
        }.execute();

        repaint();
        revalidate();
    }

    private DefaultTableModel regFileModel;
    private JScrollPane registerFilePanel;
    private JLabel registerFileLabel;

    private void setRegFilePanel() {
        String[] col = {"Name", "Q", "V"};
        String[][] data = new String[32][3];
        for (int i = 0; i < data.length; i++)
            data[i][0] = "F" + i;
        regFileModel = new DefaultTableModel(data, col);
        JTable jTable = new JTable(regFileModel);

        JTableHeader jTableHeader = jTable.getTableHeader();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(jTableHeader, BorderLayout.NORTH);
        panel.add(jTable, BorderLayout.CENTER);
        registerFilePanel = new JScrollPane(panel);

        registerFilePanel.setVisible(false);
        registerFileLabel = new JLabel("RegisterFile");
        registerFileLabel.setVisible(false);
        new SwingWorker<>() {

            @Override
            protected Object doInBackground() {
                registerFilePanel.setBounds(50, 450, 300, 350);
                add(registerFilePanel);
                return null;
            }
        }.execute();

        new SwingWorker<>() {

            @Override
            protected Object doInBackground() {
                setComponentView(registerFileLabel, 50, 400, 35, 56, false);
                return null;
            }
        }.execute();
    }

    private void setRegFile(HashMap<String, RegisterFileSlot> regFile) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < 32; i++) {
                regFileModel.setValueAt(regFile.get("F" + i).getQ(), i, 1);
                regFileModel.setValueAt(regFile.get("F" + i).getV(), i, 2);
            }
        });

    }


    private DefaultTableModel loadBufferModel;
    private JPanel loadBufferPanel;
    private JLabel loadBufferLabel;


    private void setLoadBufferPanel() {
        String[] columns = {"ID", "Address"};
        String[][] data = new String[3][2];

        loadBufferModel = new DefaultTableModel(data, columns);
        loadBufferTable = new JTable(loadBufferModel);
        JTableHeader header = loadBufferTable.getTableHeader();
        loadBufferPanel = new JPanel(new BorderLayout());
        loadBufferPanel.add(header, BorderLayout.NORTH);
        loadBufferPanel.add(loadBufferTable, BorderLayout.CENTER);

        loadBufferLabel = new JLabel("Load Buffers");


        loadBufferPanel.setVisible(false);
        loadBufferLabel.setVisible(false);
        new SwingWorker<>() {
            @Override
            protected Object doInBackground() {
                setComponentView(loadBufferPanel, 50, 350, 100, 3, false);
                setComponentView(loadBufferLabel, 50, 325, 100, 3, false);
                return null;
            }
        }.execute();
    }

    private void setLoadBufferVisible() {
        loadBufferPanel.setVisible(true);
        loadBufferLabel.setVisible(true);
    }

    private void setLoadBuffer(LoadInstruction[] loadBuffer) {
        for (int i = 0; i < 3; i++) {
            if (loadBuffer[i] != null) {
                loadBufferModel.setValueAt(loadBuffer[i].getId(), i, 0);
                loadBufferModel.setValueAt(loadBuffer[i].getAddress(), i, 1);
            } else {
                loadBufferModel.setValueAt("", i, 0);
                loadBufferModel.setValueAt("", i, 1);
            }
        }
    }

    private DefaultTableModel storeBufferModel;
    private JPanel storeBufferPanel;
    private JTable storeBufferTable;
    private JLabel storeBufferLabel;

    private void setStoreBufferVisible() {
        new SwingWorker<>() {
            @Override
            protected Object doInBackground() {
                storeBufferPanel.setVisible(true);
                storeBufferLabel.setVisible(true);
                return null;
            }
        }.execute();

    }

    private void setStoreBufferPanel() {
        String[] columns = {"ID", "Address", "V", "Q"};
        String[][] data = new String[3][4];

        storeBufferModel = new DefaultTableModel(data, columns);
        storeBufferTable = new JTable(storeBufferModel);

        JTableHeader header = storeBufferTable.getTableHeader();
        storeBufferPanel = new JPanel(new BorderLayout());
        storeBufferPanel.add(header, BorderLayout.NORTH);
        storeBufferPanel.add(storeBufferTable, BorderLayout.CENTER);
        storeBufferPanel.setVisible(false);


        storeBufferLabel = new JLabel("Store Buffers");
        setComponentView(storeBufferLabel, 500, 425, 100, 3, false);
        storeBufferLabel.setVisible(false);
        setComponentView(storeBufferPanel, 500, 450, 100, 3, false);

//        new SwingWorker<>() {
//            @Override
//            protected Object doInBackground() {
//                return null;
//            }
//        }.execute();

    }

    private void setStoreBuffer(StoreInstruction[] storeBuffer) {

        for (int i = 0; i < 3; i++) {
            if (storeBuffer[i] != null) {
                storeBufferModel.setValueAt(storeBuffer[i].getId(), i, 0);
                storeBufferModel.setValueAt(storeBuffer[i].getAddress(), i, 1);
                storeBufferModel.setValueAt(storeBuffer[i].getV(), i, 2);
                storeBufferModel.setValueAt(storeBuffer[i].getQ(), i, 3);
            } else {
                for (int j = 0; j < 4; j++)
                    storeBufferModel.setValueAt("", i, j);
            }
        }
    }

    private void setInstructionTable(ArrayList<Instruction> instructionArrayList) {
        for (int i = 0; i < instructionArrayList.size(); i++) {
            Instruction curInstruction = instructionArrayList.get(i);
            if (curInstruction != null) {
                instructionTableModel.setValueAt(curInstruction.getOp(), i, 0);
                instructionTableModel.setValueAt(curInstruction.getReg1(), i, 1);
                instructionTableModel.setValueAt(curInstruction.getReg2(), i, 2);
                instructionTableModel.setValueAt(curInstruction.getReg3(), i, 3);
                instructionTableModel.setValueAt(curInstruction.getIssued(), i, 4);
                instructionTableModel.setValueAt(curInstruction.getStartExec(), i, 5);
                instructionTableModel.setValueAt(curInstruction.getEndExec(), i, 6);
                instructionTableModel.setValueAt(curInstruction.getWriteBack(), i, 7);

//                for(int j = 0; j < 8; j ++)
//                    if(instructionTableModel.getValueAt(i, j).equals("-1"))
//                        instructionTableModel.setValueAt("", i, j);
            }
        }
    }

    DefaultTableModel instructionTableModel;

    private void setInstructionSection(ArrayList<Instruction> instructionArrayList) {
        JLabel instructionLabel = new JLabel("Instructions");
        setComponentView(instructionLabel, 50, 8, 50, 50, false);

        String[] columns = {"Operation", "Register1", "Register 2", "Register 3", "Issue Time", "Start Time", "End Time", "Write Back"};
        String[][] data = new String[instructionArrayList.size()][8];


        instructionTableModel = new DefaultTableModel(data, columns);
        JTable jTable = new JTable(instructionTableModel);
        JTableHeader header = jTable.getTableHeader();
        TableColumn comboColumn = jTable.getColumnModel().getColumn(0);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(header, BorderLayout.NORTH);
        panel.add(jTable, BorderLayout.CENTER);


        new SwingWorker<>() {

            @Override
            protected Object doInBackground() throws Exception {
                setComponentView(panel, 50, 50, 50, 200, false);
                JComboBox jComboBox = new JComboBox(instructionsNames);
                comboColumn.setCellEditor(new DefaultCellEditor(jComboBox));
                comboColumn.setMaxWidth(100);
                return null;
            }
        }.execute();
        JScrollPane jsp = new JScrollPane(panel);
        setComponentView(jsp, 50, 50, 50, 200, false);

        JButton addInstructionButton = new JButton("+Add Instruction");
        addInstructionButton.setBackground(new Color(255, 255, 255));
        addInstructionButton.addActionListener(e -> addInstruction());

        setComponentView(addInstructionButton, 300, 300, 20, 10, false);


    }

    private void addInstruction() {
        JComboBox jComboBox = new JComboBox(instructionsNames);
        JTextPane reg1 = new JTextPane();
        JTextPane reg2 = new JTextPane();


        JTextPane reg3 = new JTextPane();
/*        instructionSection.add(jComboBox);
        instructionSection.add(reg1);
        instructionSection.add(reg2);
        instructionSection.add(reg3);*/
        instructionEntries.add(new InstructionEntry(jComboBox, reg1, reg2, reg3));
        Vector<Object> vector = new Vector<>();
//        vector.add(jComboBox);
//        vector.add(reg1);
//        vector.add(reg2);
//        vector.add(reg3);
        instructionTableModel.addRow(vector);
        repaint();
        revalidate();
    }


    private void setLatenciesSection() {
        GridLayout g = (new GridLayout(0, 2));
        g.setVgap(5);
        JPanel latenciesPanel = new JPanel(g);

        JTextArea divLatencyField = new JTextArea();
        JTextPane mulLatencyField = new JTextPane();
        JTextPane addLatencyField = new JTextPane();
        JTextPane subLatencyField = new JTextPane();
        JTextPane lwLatencyField = new JTextPane();
        JTextPane swLatencyField = new JTextPane();


        divLatencyField.setSize(new Dimension(1, 1));

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


                } catch (NumberFormatException ignored) {

                }
            }
        });
        mulLatencyField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                try {
                    mulLatency = Integer.parseInt(mulLatencyField.getText());
                } catch (NumberFormatException ignored) {

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
                } catch (NumberFormatException ignored) {

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
                } catch (NumberFormatException ignored) {

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
                } catch (NumberFormatException ignored) {

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
                } catch (NumberFormatException ignored) {

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

        repaint();
        revalidate();
    }


    private void print() {
        for (InstructionEntry i : instructionEntries) {
            System.out.println(instructionsNames[i.jComboBox.getSelectedIndex()] + " " + i.register1.getText() + " " + i.register2.getText() + " " + i.register3.getText());
        }
    }


    @SuppressWarnings("EnhancedSwitchMigration")
    public ArrayList<Instruction> getInstructionsNames() {
        ArrayList<Instruction> instructionArrayList = new ArrayList<>();

        for (int i = 0; i < instructionTableModel.getRowCount(); i++) {
            String possibleInstruction = (String) instructionTableModel.getValueAt(i, 0);
            Operation a;
            switch (possibleInstruction) {

                case "ADD":
                    a = Operation.ADD;
                    break;
                case "SUB":
                    a = Operation.SUB;
                    break;
                case "MUL":
                    a = Operation.MUL;
                    break;
                case "DIV":
                    a = Operation.DIV;
                    break;
                case "SW":
                    a = Operation.ST;
                    break;
                case "LD":
                    a = Operation.LD;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + possibleInstruction);
            }
            instructionArrayList.add(new Instruction(a, (String) instructionTableModel.getValueAt(i, 1),
                    (String) instructionTableModel.getValueAt(i, 2), (String) instructionTableModel.getValueAt(i, 3)));
            System.out.println((String) instructionTableModel.getValueAt(i, 1) + " "+
                    (String) instructionTableModel.getValueAt(i, 2) + " " +(String) instructionTableModel.getValueAt(i, 3));
        }

        return instructionArrayList;
    }


}
