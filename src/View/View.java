package View;

import Model.*;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class View extends JFrame {

    String[] instructions = {"ADD", "SUB", "MUL", "DIV", "LD", "SW"};

    private final JPanel instructionSection;
    private final ArrayList<InstructionEntry> instructionEntries;
    private JTable loadBufferTable;
    private JTable storeBufferTable;
    private boolean isSubmitted = false;

    private JButton submitButton;
    private JButton nextButton;
    private JButton prevButton;
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
        submitButton = new JButton("SUBMIT");
        nextButton = new JButton("Next");
        prevButton = new JButton("Previous");
        SwingUtilities.invokeLater(() -> {
            setInstructionSection(new ArrayList<>());
            setLatenciesSection();

            setSubmitButtons();
//            //testing
//            LoadInstruction l = new LoadInstruction(5, 1);
//            LoadInstruction l1 = new LoadInstruction(5, 1);
//            LoadInstruction l2 = new LoadInstruction(5, 1);
//            setLoadBuffer(new LoadInstruction[]{l, l1, l2});
//
//            //testing
//            StoreInstruction s1 = null;
//            StoreInstruction s2 = null;
//            StoreInstruction s3 = null;
//
//            setStoreBuffer(new StoreInstruction[]{s1, s2, s3});
//
//            //testing
//            ReservationStation r1 = new ReservationStation(Operation.ADD, 1.5, 1.3, "A5", "A+", 7);
//            ReservationStation r2 = new ReservationStation(Operation.ADD, 1.5, 1.3, "A5", "A+", 6);
//            ReservationStation r3 = null;
//
//            setReservationStations(new ReservationStation[]{r1, r2, r3}, new ReservationStation[]{null, null, null});
//
//
//            //testing
//            HashMap<String, RegisterFileSlot> registerFile = new HashMap<>();
//            for (int i = 0; i < 32; i++) {
//                registerFile.put("F" + i, new RegisterFileSlot(-1, ""));
//            }
//
//            setRegFile(registerFile);
//            setCycles(55);
        });

        setVisible(true);
        repaint();
        revalidate();
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

    public JButton getNextButton() {
        return nextButton;
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
        if (isSubmitted) {
            nextButton.setBackground(new Color(255,255,255));
            prevButton.setBackground(new Color(255,255,255));
            new SwingWorker<>(){

                @Override
                protected Object doInBackground() throws Exception {
                    setComponentView(prevButton, 700, 300, 50, 10, false);
                    setComponentView(nextButton, 900, 300, 50, 10, false);
                    return null;
                }
            }.execute();
        } else {
            submitButton.setBackground(new Color(255,255,255));
            new SwingWorker<>(){

                @Override
                protected Object doInBackground() throws Exception {
                    setComponentView(submitButton, 900, 300, 50, 10, false);
                    return null;
                }
            }.execute();
        }
    }

    private void setReservationStations(ReservationStation[] addReservationStation, ReservationStation[] mulReservationStation) {

        String[] columns = {"ID", "Operation", "V1", "V2", "Q1", "Q2",};

        String[][] addData = new String[3][6];
        String[][] mulData = new String[3][6];

        for (int i = 0; i < 3; i++) {
            if (addReservationStation[i] != null)
                addData[i] = addReservationStation[i].getArray();
            else
                for (int j = 0; j < 6; j++)
                    addData[i][j] = "";
            if (mulReservationStation[i] != null)
                mulData[i] = mulReservationStation[i].getArray();
            else
                for (int j = 0; j < 6; j++)
                    mulData[i][j] = "";
        }

        new SwingWorker<>() {
            @Override
            protected Object doInBackground() {


                JTable addRes = new JTable(addData, columns);
                JTable mulRes = new JTable(mulData, columns);
                JTableHeader addHeader = addRes.getTableHeader();
                JTableHeader mulHeader = mulRes.getTableHeader();

                JPanel addPanel = new JPanel(new BorderLayout());
                addPanel.add(addHeader, BorderLayout.NORTH);
                addPanel.add(addRes, BorderLayout.CENTER);
                setComponentView(addPanel, 500, 600, 100, 3, false);

                JPanel mulPanel = new JPanel(new BorderLayout());
                mulPanel.add(mulHeader, BorderLayout.NORTH);
                mulPanel.add(mulRes, BorderLayout.CENTER);
                setComponentView(mulPanel, 500, 750, 100, 3, false);


                return null;
            }
        }.execute();


        new SwingWorker<>() {

            @Override
            protected Object doInBackground() {
                JLabel addLabel = new JLabel("Addition and Subtraction Label");
                JLabel mulLabel = new JLabel("Multiplication and Division Label");

                setComponentView(mulLabel, 500, 725, 100, 3, false);
                setComponentView(addLabel, 500, 575, 100, 3, false);
                return null;
            }
        }.execute();

        repaint();
        revalidate();
    }

    private void setCycles(int cycle) {
        JLabel cycleLabel = new JLabel("Cycle " + cycle);
        new SwingWorker<>() {

            @Override
            protected Object doInBackground() throws Exception {
                cycleLabel.setFont(new Font("", Font.BOLD, 25));
                cycleLabel.setBounds(500, -70,
                        400, 200);
                add(cycleLabel);
                /*setComponentView(cycleLabel, 500, 350, 100,100, false);*/
                return null;
            }
        }.execute();
//        SwingUtilities.invokeLater(() -> setComponentView(cycleLabel, 500, 350, 100,100, false));
    }

    private void setRegFile(HashMap<String, RegisterFileSlot> regFile) {
        String[] col = {"Name", "Q", "V"};
        String[][] data = new String[32][3];

        for (int i = 0; i < 32; i++) {
            data[i][0] = "F" + i;
            data[i][1] = regFile.get("F" + i).getQ();
            data[i][2] = String.valueOf(regFile.get("F" + i).getV());
        }

        new SwingWorker<>() {

            @Override
            protected Object doInBackground() {

                JTable jTable = new JTable(data, col);

                JTableHeader jTableHeader = jTable.getTableHeader();
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(jTableHeader, BorderLayout.NORTH);
                panel.add(jTable, BorderLayout.CENTER);

                JScrollPane jScrollPane = new JScrollPane(panel);
                jScrollPane.setBounds(50, 450, 300, 350);
                add(jScrollPane);

                return null;
            }
        }.execute();

        new SwingWorker<>() {

            @Override
            protected Object doInBackground() {

                JLabel label = new JLabel("RegisterFile");
                setComponentView(label, 50, 400, 35, 56, false);

                return null;
            }
        }.execute();

    }

    private void setLoadBuffer(LoadInstruction[] loadBuffer) {
        String[] columns = {"ID", "Address"};
        String[][] data = new String[loadBuffer.length][2];


        for (int i = 0; i < 3; i++) {
            if (loadBuffer[i] != null) {
                data[i][0] = String.valueOf(loadBuffer[i].getId());
                data[i][1] = String.valueOf(loadBuffer[i].getAddress());
            } else {
                data[i][0] = "";
                data[i][1] = "";
            }
        }
        JLabel loadBufferLabel = new JLabel("Load Buffers");
        setComponentView(loadBufferLabel, 50, 325, 100, 3, false);

        new SwingWorker<>() {
            @Override
            protected Object doInBackground() {

                loadBufferTable = new JTable(data, columns);
                JTableHeader header = loadBufferTable.getTableHeader();
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(header, BorderLayout.NORTH);
                panel.add(loadBufferTable, BorderLayout.CENTER);
                setComponentView(panel, 50, 350, 100, 3, false);

                return null;
            }
        }.execute();


        repaint();
        revalidate();
    }


    private void setStoreBuffer(StoreInstruction[] storeBuffer) {
        String[] columns = {"ID", "Address", "V", "Q"};
        String[][] data = new String[storeBuffer.length][4];


        for (int i = 0; i < 3; i++) {

            if (storeBuffer[i] != null) {
                data[i][0] = String.valueOf(storeBuffer[i].getId());
                data[i][1] = String.valueOf(storeBuffer[i].getAddress());
                data[i][2] = String.valueOf(storeBuffer[i].getV());
                data[i][3] = String.valueOf(storeBuffer[i].getQ());
            } else {
                data[i][0] = "";
                data[i][1] = "";
                data[i][2] = "";
                data[i][3] = "";
            }
        }

        JLabel storeBufferLabel = new JLabel("Store Buffers");
        setComponentView(storeBufferLabel, 500, 425, 100, 3, false);

        new SwingWorker<>() {
            @Override
            protected Object doInBackground() {
                storeBufferTable = new JTable(data, columns);
                JTableHeader header = storeBufferTable.getTableHeader();
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(header, BorderLayout.NORTH);
                panel.add(storeBufferTable, BorderLayout.CENTER);
                setComponentView(panel, 500, 450, 100, 3, false);
                return null;
            }
        }.execute();


        repaint();
        revalidate();
    }

    private void setInstructionSection(ArrayList<Instruction> instructionArrayList) {
        JLabel instructionLabel = new JLabel("Instructions");
        setComponentView(instructionLabel, 50, 8, 50, 50, false);
        if (isSubmitted) {
            String[] columns = {"Operation", "Register1", "Register 2", "Register 3", "Issue Time", "Start Time", "End Time", "Write Back"};
            String[][] data = new String[instructionArrayList.size()][8];

            for (int i = 0; i < instructionArrayList.size(); i++) {
                Instruction curInstruction = instructionArrayList.get(i);
                Arrays.fill(data[i], "-");
                data[i][0] = String.valueOf(curInstruction.getOp());
                data[i][1] = String.valueOf(curInstruction.getReg1());
                data[i][2] = String.valueOf(curInstruction.getReg2());
                data[i][3] = String.valueOf(curInstruction.getReg3());
                data[i][4] = String.valueOf(curInstruction.getIssued());
                data[i][5] = String.valueOf(curInstruction.getStartExec());
                data[i][6] = String.valueOf(curInstruction.getEndExec());
                data[i][7] = String.valueOf(curInstruction.getWriteBack());
                for (int j = 0; j < 8; j++)
                    if (data[i][j].equals("-1")) data[i][j] = "-";
            }

            JTable jTable = new JTable(data, columns);
            JTableHeader header = jTable.getTableHeader();

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(header, BorderLayout.NORTH);
            panel.add(jTable, BorderLayout.CENTER);

            new SwingWorker<>() {

                @Override
                protected Object doInBackground() throws Exception {
                    setComponentView(panel, 50, 50, 500, 200, false);
                    return null;
                }
            }.execute();

        } else {
            int columns = 4;
            GridLayout gridLayout = new GridLayout(0, columns);
            gridLayout.setHgap(7);
            gridLayout.setVgap(7);
            instructionSection.setLayout(gridLayout);

            JScrollPane jsp = new JScrollPane(instructionSection);
            setComponentView(jsp, 50, 50, 500, 200, false);


            JButton addInstructionButton = new JButton("+Add Instruction");
            addInstructionButton.setBackground(new Color(255, 255, 255));
            addInstructionButton.addActionListener(e -> addInstruction());


            setComponentView(addInstructionButton, 575, 75, 20, 10, false);
        }

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
        instructionEntries.add(new InstructionEntry(jComboBox, reg1, reg2, reg3));

        repaint();
        revalidate();
    }

    private void print() {
        for (InstructionEntry i : instructionEntries) {
            System.out.println(instructions[i.jComboBox.getSelectedIndex()] + " " + i.register1.getText() + " " + i.register2.getText() + " " + i.register3.getText());
        }
    }


    

    public ArrayList<Instruction> getInstructions(){
        ArrayList<Instruction> instructionArrayList  = new ArrayList<>();

        for(InstructionEntry i : instructionEntries);
//            instructionArrayList.add(new Instruction());

        return instructionArrayList;
    }


}
