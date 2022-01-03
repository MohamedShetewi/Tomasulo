package Model;

public class StoreInstruction implements Cloneable{
    private int address, id;
    private double V;
    private String Q;

    public StoreInstruction(int address, double v, String q, int id) {
        this.address = address;
        V = v;
        Q = q;
        this.id = id;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new StoreInstruction(address, V, Q, id);
    }

    @Override
    public String toString() {
        return "StoreInstruction{" +
                "address=" + address +
                ", id=" + id +
                ", V=" + V +
                ", Q='" + Q + '\'' +
                '}';
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

    public int getId() {
        return id;
    }
}
