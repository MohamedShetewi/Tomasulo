package Model;

public class ReservationStation {
    private Operation op;
    private double V1;
    private double V2;
    private String Q1;
    private String Q2;
    private int id;

    public ReservationStation(Operation op, double v1, double v2, String q1, String q2, int id) {
        this.op = op;
        V1 = v1;
        V2 = v2;
        Q1 = q1;
        Q2 = q2;
        this.id = id;
    }

    @Override
    public String toString() {
        return "ReservationStation{" +
                "op=" + op +
                ", V1=" + V1 +
                ", V2=" + V2 +
                ", Q1='" + Q1 + '\'' +
                ", Q2='" + Q2 + '\'' +
                ", id=" + id +
                '}';
    }

    public Operation getOp() {
        return op;
    }

    public String[] getArray(){
        String[] arr;
        try {
           arr = new String[]{String.valueOf(id), String.valueOf(op), String.valueOf(V1), String.valueOf(V2), String.valueOf(Q1), String.valueOf(Q2)};
        }catch (Exception e){
            arr = new String[6];
        }
        return arr;

    }

    public void setOp(Operation op) {
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

    public int getId() {
        return id;
    }
}
