package evolution.configuration;

import Generated.ETTMutation;
import Generated.ETTMutations;

import java.util.ArrayList;
import java.util.List;

public class Mutations {
    private List<Mutation> mutationList;

    public Mutations(ETTMutations gen){
        mutationList = new ArrayList<>();
        Mutation mutation;
        for(ETTMutation m : gen.getETTMutation()){
            if(m.getName().equals("Flipping")){
                mutation = Mutation.Flipping;
                mutation.setProbability(m.getProbability());
                mutation.setConfig(m.getConfiguration());
                mutationList.add(mutation);
            }
        }
    }

    public List<Mutation> getMutationList() {
        return mutationList;
    }
}