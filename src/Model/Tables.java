package Model;

import java.io.*;
import java.util.*;

import Model.Instruction;

public class Tables {
    private ArrayList<Instruction> instructions;
    private HashMap<String, RegisterFileSlot> registerFile;
    private HashMap<Integer, Double> memory;
    private LoadInstruction[] loadBuffer;
    private StoreInstruction[] storeBuffer;
    private ReservationStation[] addSubStations;
    private ReservationStation[] mulDivStations;

    private int lastIssued;
    private int curCycle;
    private final int addLatency;
    private final int subLatency;
    private final int mulLatency;
    private final int divLatency;
    private final int ldLatency;
    private final int stLatency;

    private final ArrayList<Tables> history;

    public Tables(int addLatency, int subLatency, int mulLatency, int divLatency, int ldLatency, int stLatency) {
        this.instructions = new ArrayList<>();
        this.loadBuffer = new LoadInstruction[3];
        this.storeBuffer = new StoreInstruction[3];
        this.registerFile = new HashMap<>();
        this.addSubStations = new ReservationStation[3];
        this.mulDivStations = new ReservationStation[3];
        this.memory = new HashMap<>();
        this.addLatency = addLatency;
        this.subLatency = subLatency;
        this.mulLatency = mulLatency;
        this.divLatency = divLatency;
        this.ldLatency = ldLatency;
        this.stLatency = stLatency;
        lastIssued = -1;
        curCycle = 0;
        history = new ArrayList<>();
        for (int i = 0; i < 32; i++){
            registerFile.put("F" + i, new RegisterFileSlot(-1, ""));
        }
    }

    public void next() {
        curCycle++;
        history.add(getCurrentState());

        boolean finished = true;
        for (Instruction instruction: instructions){
            if (instruction.getWriteBack() == -1) {
                finished = false;
                break;
            }
        }

        if (finished)
            System.exit(0);

        if (lastIssued != instructions.size() - 1) {
            // Try to issue new instruction
            Instruction toBeIssued = instructions.get(lastIssued + 1);
            int positionInStation;
            if (toBeIssued.getOp() == Operation.ADD || toBeIssued.getOp() == Operation.SUB) {
                positionInStation = searchSpaceInAddSub();
            } else if (toBeIssued.getOp() == Operation.MUL || toBeIssued.getOp() == Operation.DIV) {
                positionInStation = searchSpaceInMulDiv();
            } else if (toBeIssued.getOp() == Operation.LD) {
                positionInStation = searchSpaceInLoad();
            } else {
                positionInStation = searchSpaceInStore();
            }
            if (positionInStation != -1) {
                String q1 = "", q2 = "";
                if (toBeIssued.getOp() != Operation.LD && toBeIssued.getOp() != Operation.ST) {
                    q1 = registerFile.get(toBeIssued.getReg2()).getQ();
                    q2 = registerFile.get(toBeIssued.getReg3()).getQ();
                } else if (toBeIssued.getOp() == Operation.ST) {
                    q1 = registerFile.get(toBeIssued.getReg1()).getQ();
                }
                switch (toBeIssued.getOp()) {
                    case ADD: {
                        addSubStations[positionInStation] = new ReservationStation(Operation.ADD,
                                registerFile.get(toBeIssued.getReg2()).getV(),
                                registerFile.get(toBeIssued.getReg3()).getV(),
                                q1, q2, lastIssued + 1);
                        registerFile.get(toBeIssued.getReg1()).setQ("A" + (positionInStation + 1));
                        registerFile.get(toBeIssued.getReg1()).setV(-1);
                        break;
                    }
                    case SUB: {
                        addSubStations[positionInStation] = new ReservationStation(Operation.SUB,
                                registerFile.get(toBeIssued.getReg2()).getV(),
                                registerFile.get(toBeIssued.getReg3()).getV(),
                                q1, q2, lastIssued + 1);
                        registerFile.get(toBeIssued.getReg1()).setQ("A" + (positionInStation + 1));
                        registerFile.get(toBeIssued.getReg1()).setV(-1);
                        break;
                    }
                    case MUL: {
                        mulDivStations[positionInStation] = new ReservationStation(Operation.MUL,
                                registerFile.get(toBeIssued.getReg2()).getV(),
                                registerFile.get(toBeIssued.getReg3()).getV(),
                                q1, q2, lastIssued + 1);
                        registerFile.get(toBeIssued.getReg1()).setQ("M" + (positionInStation + 1));
                        registerFile.get(toBeIssued.getReg1()).setV(-1);
                        break;
                    }
                    case DIV: {
                        mulDivStations[positionInStation] = new ReservationStation(Operation.DIV,
                                registerFile.get(toBeIssued.getReg2()).getV(),
                                registerFile.get(toBeIssued.getReg3()).getV(),
                                q1, q2, lastIssued + 1);
                        registerFile.get(toBeIssued.getReg1()).setQ("M" + (positionInStation + 1));
                        registerFile.get(toBeIssued.getReg1()).setV(-1);
                        break;
                    }
                    case LD: {
                        loadBuffer[positionInStation] = new LoadInstruction(Integer.parseInt(toBeIssued.getReg2()), lastIssued + 1);
                        registerFile.get(toBeIssued.getReg1()).setQ("L" + (positionInStation + 1));
                        registerFile.get(toBeIssued.getReg1()).setV(-1);
                        break;
                    }
                    case ST:
                        storeBuffer[positionInStation] = new StoreInstruction(Integer.parseInt(toBeIssued.getReg2()),
                                registerFile.get(toBeIssued.getReg1()).getV(),
                                q1, lastIssued + 1);
                }
//            if (toBeIssued.getOp() != Operation.ST) {
//
//            }
                toBeIssued.setIssued(curCycle);
                lastIssued++;
            }
        }

        //Try to start executing an instruction
        for (ReservationStation addSubStation : addSubStations) {
            if (addSubStation != null)
                if (addSubStation.getV1() != -1 &&
                        addSubStation.getV2() != -1 && instructions.get(addSubStation.getId()).getStartExec() == -1 &&
                        instructions.get(addSubStation.getId()).getIssued() < curCycle) {
                    instructions.get(addSubStation.getId()).setStartExec(curCycle);
                }
        }

        for (ReservationStation mulDivStation : mulDivStations) {
            if (mulDivStation != null)
                if (mulDivStation.getV1() != -1 && mulDivStation.getV2() != -1 &&
                        instructions.get(mulDivStation.getId()).getStartExec() == -1 &&
                        instructions.get(mulDivStation.getId()).getIssued() < curCycle) {
                    instructions.get(mulDivStation.getId()).setStartExec(curCycle);
                }
        }

        for (LoadInstruction loadInstruction : loadBuffer) {
            if (loadInstruction != null)
                if (instructions.get(loadInstruction.getId()).getStartExec() == -1 &&
                        instructions.get(loadInstruction.getId()).getIssued() < curCycle)
                    instructions.get(loadInstruction.getId()).setStartExec(curCycle);
        }

        for (StoreInstruction storeInstruction : storeBuffer) {
            if (storeInstruction != null)
                if (storeInstruction.getV() != -1 &&
                        instructions.get(storeInstruction.getId()).getStartExec() == -1 &&
                        instructions.get(storeInstruction.getId()).getIssued() < curCycle)
                    instructions.get(storeInstruction.getId()).setStartExec(curCycle);
        }

        //Finish executing an instruction
        for (ReservationStation addSubStation : addSubStations) {
            if (addSubStation != null) {
                if (addSubStation.getOp() == Operation.ADD
                        && instructions.get(addSubStation.getId()).getStartExec() != -1
                        && curCycle - instructions.get(addSubStation.getId()).getStartExec() + 1 == addLatency) {
                    instructions.get(addSubStation.getId()).setEndExec(curCycle);
                } else if (addSubStation.getOp() == Operation.SUB
                        && instructions.get(addSubStation.getId()).getStartExec() != -1
                        && curCycle - instructions.get(addSubStation.getId()).getStartExec() + 1 == subLatency) {
                    instructions.get(addSubStation.getId()).setEndExec(curCycle);
                }
            }
        }

        for (ReservationStation mulDivStation : mulDivStations) {
            if (mulDivStation != null) {
                if (mulDivStation.getOp() == Operation.MUL
                        && instructions.get(mulDivStation.getId()).getStartExec() != -1
                        && curCycle - instructions.get(mulDivStation.getId()).getStartExec() + 1 == mulLatency) {
                    instructions.get(mulDivStation.getId()).setEndExec(curCycle);
                } else if (mulDivStation.getOp() == Operation.DIV
                        && instructions.get(mulDivStation.getId()).getStartExec() != -1
                        && curCycle - instructions.get(mulDivStation.getId()).getStartExec() + 1 == divLatency) {
                    instructions.get(mulDivStation.getId()).setEndExec(curCycle);
                }
            }
        }

        for (LoadInstruction loadInstruction : loadBuffer) {
            if (loadInstruction != null) {
                if (curCycle - instructions.get(loadInstruction.getId()).getStartExec() + 1 == ldLatency
                        && instructions.get(loadInstruction.getId()).getStartExec() != -1) {
                    instructions.get(loadInstruction.getId()).setEndExec(curCycle);
                }
            }
        }

        for (StoreInstruction storeInstruction : storeBuffer) {
            if (storeInstruction != null) {
                if (curCycle - instructions.get(storeInstruction.getId()).getStartExec() + 1 == stLatency
                        && instructions.get(storeInstruction.getId()).getStartExec() != -1) {
                    instructions.get(storeInstruction.getId()).setEndExec(curCycle);
                }
            }
        }

        //Writing back results
        ArrayList<Instruction> readyToWriteBack = new ArrayList<>();
        for (ReservationStation addSubStation : addSubStations) {
            if (addSubStation != null)
                if (instructions.get(addSubStation.getId()).getWriteBack() == -1
                        && instructions.get(addSubStation.getId()).getEndExec() != -1
                        && instructions.get(addSubStation.getId()).getEndExec() < curCycle) {
//                    instructions.get(addSubStation.getId()).setWriteBack(curCycle);
                    readyToWriteBack.add(instructions.get(addSubStation.getId()));
                }
        }

        for (ReservationStation mulDivStation : mulDivStations) {
            if (mulDivStation != null)
                if (instructions.get(mulDivStation.getId()).getWriteBack() == -1
                        && instructions.get(mulDivStation.getId()).getEndExec() != -1
                        && instructions.get(mulDivStation.getId()).getEndExec() < curCycle) {
//                    instructions.get(mulDivStation.getId()).setWriteBack(curCycle);
                    readyToWriteBack.add(instructions.get(mulDivStation.getId()));
                }
        }

        for (LoadInstruction loadInstruction : loadBuffer) {
            if (loadInstruction != null)
                if (instructions.get(loadInstruction.getId()).getWriteBack() == -1
                        && instructions.get(loadInstruction.getId()).getEndExec() != -1
                        && instructions.get(loadInstruction.getId()).getEndExec() < curCycle) {
//                    instructions.get(loadInstruction.getId()).setWriteBack(curCycle);
                    readyToWriteBack.add(instructions.get(loadInstruction.getId()));
                }
        }

        for (StoreInstruction storeInstruction : storeBuffer) {
            if (storeInstruction != null)
                if (instructions.get(storeInstruction.getId()).getWriteBack() == -1
                        && instructions.get(storeInstruction.getId()).getEndExec() != -1
                        && instructions.get(storeInstruction.getId()).getEndExec() < curCycle) {
//                    instructions.get(loadInstruction.getId()).setWriteBack(curCycle);
                    readyToWriteBack.add(instructions.get(storeInstruction.getId()));
                }
        }

        if (readyToWriteBack.size() > 0) {
            Instruction toWriteBack = readyToWriteBack.get(0);
            for (Instruction instruction : readyToWriteBack) {
                if (instruction.getIssued() < toWriteBack.getIssued())
                    toWriteBack = instruction;
            }

            toWriteBack.setWriteBack(curCycle);
            int id = 0;
            for (int i = 0; i < instructions.size(); i++) {
                Instruction instruction = instructions.get(i);
                if (instruction == toWriteBack)
                    id = i;
            }
            if (toWriteBack.getOp() == Operation.ST){
                double result = -1;
                for (StoreInstruction storeInstruction: storeBuffer){
                    if (storeInstruction != null)
                        if (storeInstruction.getId() == id){
                            result = storeInstruction.getV();
                        }
                }
                memory.put(Integer.parseInt(toWriteBack.getReg2()), result);
            } else {
                String tag = getTag(id, toWriteBack.getOp() == Operation.ADD || toWriteBack.getOp() == Operation.SUB ? addSubStations
                                : toWriteBack.getOp() == Operation.MUL || toWriteBack.getOp() == Operation.DIV ? mulDivStations
                                : loadBuffer,
                        toWriteBack.getOp() == Operation.ADD || toWriteBack.getOp() == Operation.SUB ? "A"
                                : toWriteBack.getOp() == Operation.MUL || toWriteBack.getOp() == Operation.DIV ? "M"
                                : "L");
                double result = getResult(id, toWriteBack.getOp());
                writeResultsToBus(tag, result);
            }
        }

        //Remove finished instructions
        for (int i = 0; i < addSubStations.length; i++){
            ReservationStation station = addSubStations[i];
            if (station != null){
                if (instructions.get(station.getId()).getWriteBack() != -1
                && instructions.get(station.getId()).getWriteBack() < curCycle)
                    addSubStations[i] = null;
            }
        }

        for (int i = 0; i < mulDivStations.length; i++){
            ReservationStation station = mulDivStations[i];
            if (station != null){
                if (instructions.get(station.getId()).getWriteBack() != -1
                        && instructions.get(station.getId()).getWriteBack() < curCycle)
                    mulDivStations[i] = null;
            }
        }

        for (int i = 0; i < loadBuffer.length; i++){
            LoadInstruction loadInstruction = loadBuffer[i];
            if (loadInstruction != null){
                if (instructions.get(loadInstruction.getId()).getWriteBack() != -1
                        && instructions.get(loadInstruction.getId()).getWriteBack() < curCycle)
                    loadBuffer[i] = null;
            }
        }

        for (int i = 0; i < storeBuffer.length; i++){
            StoreInstruction storeInstruction = storeBuffer[i];
            if (storeInstruction != null){
                if (instructions.get(storeInstruction.getId()).getWriteBack() != -1
                        && instructions.get(storeInstruction.getId()).getWriteBack() < curCycle)
                    storeBuffer[i] = null;
            }
        }
    }

    private void writeResultsToBus(String tag, double result) {
        registerFile.forEach((k, v) -> {
            if (v.getQ().equals(tag)) {
                v.setQ("");
                v.setV(result);
            }
        });

        for (ReservationStation station : addSubStations) {
            if (station != null) {
                if (station.getQ1().equals(tag)) {
                    station.setQ1("");
                    station.setV1(result);
                }
                if (station.getQ2().equals(tag)) {
                    station.setQ2("");
                    station.setV2(result);
                }
            }
        }

        for (ReservationStation station : mulDivStations) {
            if (station != null) {
                if (station.getQ1().equals(tag)) {
                    station.setQ1("");
                    station.setV1(result);
                }
                if (station.getQ2().equals(tag)) {
                    station.setQ2("");
                    station.setV2(result);
                }
            }
        }

        for (StoreInstruction storeInstruction : storeBuffer) {
            if (storeInstruction != null) {
                if (storeInstruction.getQ().equals(tag)) {
                    storeInstruction.setQ("");
                    storeInstruction.setV(result);
                }
            }
        }
    }

    private String getTag(int id, Object[] station, String prefix) {
        for (int i = 0; i < station.length; i++) {
            if (prefix.equals("A") || prefix.equals("M")) {
                if (((ReservationStation) station[i]).getId() == id)
                    return prefix + "" + (i + 1);
            } else if (prefix.equals("L")) {
                if (((LoadInstruction) station[i]).getId() == id)
                    return prefix + "" + (i + 1);
            }
        }
        return "";
    }

    private double getResult(int id, Operation op) {
        if (op == Operation.ADD || op == Operation.SUB) {
            for (ReservationStation addSubStation : addSubStations) {
                if (addSubStation != null)
                    if (addSubStation.getId() == id)
                        return op == Operation.ADD ? addSubStation.getV1() + addSubStation.getV2()
                                : addSubStation.getV1() - addSubStation.getV2();
            }
        } else if (op == Operation.MUL || op == Operation.DIV) {
            for (ReservationStation mulDivStation : mulDivStations) {
                if (mulDivStation != null)
                    if (mulDivStation.getId() == id)
                        return op == Operation.MUL ? mulDivStation.getV1() * mulDivStation.getV2()
                                : mulDivStation.getV1() / mulDivStation.getV2();
            }
        } else {
            for (LoadInstruction loadInstruction : loadBuffer) {
                if (loadInstruction != null) {
                    if (loadInstruction.getId() == id)
                        return memory.get(loadInstruction.getAddress());
                }
            }
        }
        return -1;
    }

    private void printState(){
        System.out.println("Cycle " + curCycle);
        System.out.println();
        System.out.println("=========Instruction Queue=========");
        for (Instruction instruction: instructions){
            System.out.println(instruction);
        }
        System.out.println();

        System.out.println("=========ADD/SUB Station=========");
        for (ReservationStation reservationStation: addSubStations){
            System.out.println(reservationStation);
        }
        System.out.println();

        System.out.println("=========MUL/DIV Station=========");
        for (ReservationStation reservationStation: mulDivStations){
            System.out.println(reservationStation);
        }
        System.out.println();

        System.out.println("=========LD Buffer=========");
        for (LoadInstruction loadInstruction: loadBuffer){
            System.out.println(loadInstruction);
        }
        System.out.println();

        System.out.println("=========ST Buffer=========");
        for (StoreInstruction storeInstruction: storeBuffer){
            System.out.println(storeInstruction);
        }
        System.out.println();

        System.out.println("=========Register Buffer=========");
        registerFile.forEach((k, v) -> {
            System.out.println(k + ": " + v);
        });
        System.out.println();
    }

    private void function(int...x){

    }

    private int searchSpaceInLoad() {
        for (int i = 0; i < loadBuffer.length; i++) {
            if (loadBuffer[i] == null)
                return i;
        }
        return -1;
    }

    private int searchSpaceInStore() {
        for (int i = 0; i < storeBuffer.length; i++) {
            if (storeBuffer[i] == null)
                return i;
        }
        return -1;
    }

    private int searchSpaceInAddSub() {
        for (int i = 0; i < addSubStations.length; i++) {
            if (addSubStations[i] == null)
                return i;
        }
        return -1;
    }

    private int searchSpaceInMulDiv() {
        for (int i = 0; i < mulDivStations.length; i++) {
            if (mulDivStations[i] == null)
                return i;
        }
        return -1;
    }

    public Tables getCurrentState() {
        Tables state = new Tables(addLatency, subLatency, mulLatency, divLatency, ldLatency, stLatency);
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

    public ArrayList<Tables> getHistory() {
        return history;
    }

    public static void main(String[] args) throws IOException {
        Tables tables = new Tables(2, 2, 3, 3 ,1, 1);
        tables.registerFile.put("F20", new RegisterFileSlot(1.99, ""));
//        tables.registerFile.put("F0", new RegisterFileSlot(-1, ""));
//        tables.registerFile.put("F1", new RegisterFileSlot(-1, ""));
//        tables.registerFile.put("F2", new RegisterFileSlot(-1, ""));
        tables.memory.put(10, 1.2);
        tables.memory.put(11, 1.3);
        File file = new File("src/Model/instructions.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        while (br.ready()){
            String instruction = br.readLine();
            StringTokenizer st = new StringTokenizer(instruction);
            String floatOp = st.nextToken();
            String substring = floatOp.substring(0, floatOp.length() - 2);
            Operation op = substring.equals("ADD") ? Operation.ADD
                    : substring.equals("SUB") ? Operation.SUB
                    : substring.equals("MUL") ? Operation.MUL
                    : substring.equals("DIV") ? Operation.DIV
                    : substring.equals("L") ? Operation.LD
                    : Operation.ST;
            StringTokenizer st2 = new StringTokenizer(st.nextToken(), ",");
            String reg1 = "", reg2 = "", reg3 = "";
            if (op != Operation.LD && op != Operation.ST){
                reg1 = st2.nextToken();
                reg2 = st2.nextToken();
                reg3 = st2.nextToken();
            } else {
                reg1 = st2.nextToken();
                reg2 = st2.nextToken();
            }
            tables.instructions.add(new Instruction(op, reg1, reg2, reg3));
        }
        for (;;){
            tables.printState();
            tables.next();
        }

    }
}
