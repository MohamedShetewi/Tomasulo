package Controller;

import Model.Instruction;
import Model.Tables;
import View.View;

import java.util.ArrayList;

public class Controller {

    public Controller(){
        View v = new View();
        Tables table = new Tables();
        ArrayList<Instruction> instructions = new ArrayList<>();
        v.getSubmitButton().addActionListener(e -> {
            v.setSubmitted(true);
            table.setAddLatency(v.getAddLatency());
            table.setDivLatency(v.getDivLatency());
            table.setMulLatency(v.getMulLatency());
            table.setStLatency(v.getSwLatency());
            table.setLdLatency(v.getLwLatency());
            table.setSubLatency(v.getSubLatency());

        });

    }

    public static void main(String[] args) {

    }
}
