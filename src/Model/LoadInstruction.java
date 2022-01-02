package Model;

public class LoadInstruction {
    private int address, id;

    public LoadInstruction(int address, int id) {
        this.address = address;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "LoadInstruction{" +
                "address=" + address +
                ", id=" + id +
                '}';
    }

    public int getAddress() {
        return address;
    }
}
