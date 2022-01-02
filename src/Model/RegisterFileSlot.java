package Model;

public class RegisterFileSlot {
    private double V;
    private String Q;

    public RegisterFileSlot(double v, String q) {
        V = v;
        Q = q;
    }

    @Override
    public String toString() {
        return "RegisterFileSlot{" +
                "V=" + V +
                ", Q='" + Q + '\'' +
                '}';
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
