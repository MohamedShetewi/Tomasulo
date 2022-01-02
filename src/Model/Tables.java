package Model;

import java.util.ArrayList;
import java.util.HashMap;

public class Tables {
    private ArrayList<Instruction> instructions;
    private HashMap<String,RegisterFileSlot> registerFile;
    private HashMap<Integer,Double> memory;
    private LoadInstruction[] loadBuffer;
    private StoreInstruction[] storeBuffer;
    private ReservationStation[] addSubStations;
    private ReservationStation[] mulDivStations;

    public Tables() {
        this.instructions = new ArrayList<>();
        this.loadBuffer = new LoadInstruction[3];
        this.storeBuffer = new StoreInstruction[3];
        this.registerFile = new HashMap<>();
        this.addSubStations = new ReservationStation[3];
        this.mulDivStations = new ReservationStation[3];
        this.memory = new HashMap<>();
    }

    public Tables getCurrentState(){
        Tables state = new Tables();
        state.setAddSubStations(this.addSubStations.clone());
        state.setInstructions((ArrayList<Instruction>) this.instructions.clone());
        state.setMemory((HashMap<Integer, Double>) this.memory.clone());
        state.setLoadBuffer(this.loadBuffer.clone());
        state.setMulDivStations(this.mulDivStations.clone());
        state.setRegisterFile((HashMap<String, RegisterFileSlot>) this.registerFile.clone());
        state.setStoreBuffer(this.getStoreBuffer().clone());

        return state;
    }

    public void setInstructions(ArrayList<Instruction> instructions) {
        this.instructions = instructions;
    }

    public void setRegisterFile(HashMap<String, RegisterFileSlot> registerFile) {
        this.registerFile = registerFile;
    }

    public void setMemory(HashMap<Integer, Double> memory) {
        this.memory = memory;
    }

    public void setLoadBuffer(LoadInstruction[] loadBuffer) {
        this.loadBuffer = loadBuffer;
    }

    public void setStoreBuffer(StoreInstruction[] storeBuffer) {
        this.storeBuffer = storeBuffer;
    }

    public void setAddSubStations(ReservationStation[] addSubStations) {
        this.addSubStations = addSubStations;
    }

    public void setMulDivStations(ReservationStation[] mulDivStations) {
        this.mulDivStations = mulDivStations;
    }

    public ArrayList<Instruction> getInstructions() {
        return instructions;
    }

    public LoadInstruction[] getLoadBuffer() {
        return loadBuffer;
    }

    public StoreInstruction[] getStoreBuffer() {
        return storeBuffer;
    }

    public HashMap<String, RegisterFileSlot> getRegisterFile() {
        return registerFile;
    }

    public ReservationStation[] getAddSubStations() {
        return addSubStations;
    }

    public ReservationStation[] getMulDivStations() {
        return mulDivStations;
    }

    public HashMap<Integer, Double> getMemory() {
        return memory;
    }
}
