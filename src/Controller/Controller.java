package Controller;

import Model.Tables;
import View.View;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Controller {

    private ArrayList<Tables> states;
    private int curState = 0;

    public Controller() {
        View v = new View();
        Tables table = new Tables();

        v.getSubmitButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (!v.isSubmitted()) {
                    v.setSubmitted(true);
                    v.getSubmitButton().setText("Next");
                    table.setAddLatency(v.getAddLatency());
                    table.setDivLatency(v.getDivLatency());
                    table.setMulLatency(v.getMulLatency());
                    table.setStLatency(v.getSwLatency());
                    table.setLdLatency(v.getLwLatency());
                    table.setSubLatency(v.getSubLatency());
                    table.setInstructions(v.getInstructionsNames());
                    try {
                        table.runTomasulo();
                    } catch (CloneNotSupportedException ex) {
                        ex.printStackTrace();
                    }
                    states = table.getHistory();

                    v.setViewAfterSubmit();
                }else{
                    Tables thisState = states.get(curState);

                    v.setTables(thisState.getRegisterFile(), thisState.getAddSubStations(), thisState.getMulDivStations(),
                            thisState.getStoreBuffer(), thisState.getLoadBuffer(), curState,thisState.getInstructions());
                    if(curState == 0){
                        v.getPrevButton().setEnabled(false);
                    }
                    if(curState== states.size()-1)
                        v.getSubmitButton().setEnabled(false);

                    if(curState != 0 && !v.getPrevButton().isEnabled())
                        v.getPrevButton().setEnabled(true);
                    if(curState + 1 < states.size() && !v.getSubmitButton().isEnabled())
                        v.getSubmitButton().setEnabled(true);
                    curState=Math.min(states.size()-1,curState+1);
//                    if(!table.isFinished) {
//                        table.next();
//                        //Tables thisState = table.getCurrentState();
//                        v.setTables(thisState.getRegisterFile(), thisState.getAddSubStations(), thisState.getMulDivStations(),
//                                thisState.getStoreBuffer(), thisState.getLoadBuffer(), table.getCurCycle(), thisState.getInstructions());
//                    }
                }
            }
        });

        v.getPrevButton().addActionListener(e -> {
            curState=Math.max(curState-1,0);
            Tables thisState = states.get(curState);
            v.setTables(thisState.getRegisterFile(), thisState.getAddSubStations(), thisState.getMulDivStations(),
                    thisState.getStoreBuffer(), thisState.getLoadBuffer(), curState,thisState.getInstructions());
            if(curState == 0){
                v.getPrevButton().setEnabled(false);
            }
            if(curState + 1 == states.size())
                v.getSubmitButton().setEnabled(false);

            if(curState != 0 && !v.getPrevButton().isEnabled())
                v.getPrevButton().setEnabled(true);
            if(curState + 1 < states.size() && !v.getSubmitButton().isEnabled())
                v.getSubmitButton().setEnabled(true);
        });

    }


    public static void main(String[] args) {
        new Controller();
    }
}
