package Model;

public class Instruction implements Cloneable{
    private Operation op;
    private String reg1;
    private String reg2;
    private String reg3;
    private int issued;
    private int startExec;
    private int endExec;
    private int writeBack;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Object clone = super.clone();
        Instruction i = new Instruction(op, reg1, reg2, reg3);
        i.issued = issued;
        i.startExec = startExec;
        i.endExec = endExec;
        i.writeBack = writeBack;
        return i;
    }

    public Instruction(Operation op, String reg1, String reg2, String reg3) {
        this.op = op;
        this.reg1 = reg1;
        this.reg2 = reg2;
        this.reg3 = reg3;
        issued = -1;
        startExec = -1;
        endExec = -1;
        writeBack = -1;
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "op=" + op +
                ", reg1='" + reg1 + '\'' +
                ", reg2='" + reg2 + '\'' +
                ", reg3='" + reg3 + '\'' +
                ", issued=" + issued +
                ", startExec=" + startExec +
                ", endExec=" + endExec +
                ", writeBack=" + writeBack +
                '}';
    }

    public Operation getOp() {
        return op;
    }

    public void setOp(Operation op) {
        this.op = op;
    }

    public String getReg1() {
        return reg1;
    }

    public void setReg1(String reg1) {
        this.reg1 = reg1;
    }

    public String getReg2() {
        return reg2;
    }

    public void setReg2(String reg2) {
        this.reg2 = reg2;
    }

    public String getReg3() {
        return reg3;
    }

    public void setReg3(String reg3) {
        this.reg3 = reg3;
    }

    public int getIssued() {
        return issued;
    }

    public void setIssued(int issued) {
        this.issued = issued;
    }

    public int getStartExec() {
        return startExec;
    }

    public void setStartExec(int startExec) {
        this.startExec = startExec;
    }

    public int getEndExec() {
        return endExec;
    }

    public void setEndExec(int endExec) {
        this.endExec = endExec;
    }

    public int getWriteBack() {
        return writeBack;
    }

    public void setWriteBack(int writeBack) {
        this.writeBack = writeBack;
    }
}
