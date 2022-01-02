package Model;

public class Instruction {
    private String op;
    private String reg1;
    private String reg2;
    private String reg3;
    private int issued;
    private int startExec;
    private int endExec;
    private int writeBack;

    public Instruction(String op, String reg1, String reg2, String reg3) {
        this.op = op;
        this.reg1 = reg1;
        this.reg2 = reg2;
        this.reg3 = reg3;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
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
