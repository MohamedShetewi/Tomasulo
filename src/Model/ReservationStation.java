package Model;

public class ReservationStation {
    private String op;
    private double V1;
    private double V2;
    private String Q1;
    private String Q2;

    public ReservationStation(String op, double v1, double v2, String q1, String q2) {
        this.op = op;
        V1 = v1;
        V2 = v2;
        Q1 = q1;
        Q2 = q2;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public double getV1() {
        return V1;
    }

    public void setV1(double v1) {
        V1 = v1;
    }

    public double getV2() {
        return V2;
    }

    public void setV2(double v2) {
        V2 = v2;
    }

    public String getQ1() {
        return Q1;
    }

    public void setQ1(String q1) {
        Q1 = q1;
    }

    public String getQ2() {
        return Q2;
    }

    public void setQ2(String q2) {
        Q2 = q2;
    }
}
