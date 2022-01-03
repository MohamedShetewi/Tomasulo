package Model;

public class LoadInstruction implements Cloneable{
    private int address, id;

    public LoadInstruction(int address, int id) {
        this.address = address;
        this.id = id;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new LoadInstruction(address, id);
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
