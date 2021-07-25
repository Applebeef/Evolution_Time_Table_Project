package Converted;

import Generated.ETTMutation;
import Generated.ETTMutations;

import java.util.List;

public class Mutations {
    List<Mutation> mutationList;
    Mutation mutation;
    Mutations(ETTMutations gen){
        for(ETTMutation m : gen.getETTMutation()){
            if(m.getName().equals("Flipping")){
                mutation = Mutation.Flipping;
                mutation.setProbability(m.getProbability());
                mutation.setConfig(m.getConfiguration());
                mutationList.add(mutation);
            }
        }
    }
}
