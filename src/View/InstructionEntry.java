package View;

import javax.swing.*;

public class InstructionEntry {
    JComboBox jComboBox;
    JTextPane register1;
    JTextPane register2;
    JTextPane register3;


    public InstructionEntry(JComboBox jComboBox, JTextPane register1, JTextPane register2, JTextPane register3) {
        this.jComboBox = jComboBox;
        this.register1 = register1;
        this.register2 = register2;
        this.register3 = register3;
    }
}
