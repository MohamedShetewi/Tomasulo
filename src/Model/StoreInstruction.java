package Model;

public class StoreInstruction {
    private int address;
    private double V;
    private String Q;

    public StoreInstruction(int address, double v, String q) {
        this.address = address;
        V = v;
        Q = q;
    }

    public int getAddress() {
        return address;
    }

    public double getV() {
        return V;
    }

    public void setV(double v) {
        V = v;
    }

    public String getQ() {
        return Q;
    }

    public void setQ(String q) {
        Q = q;
    }
}
