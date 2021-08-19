package solution;

import Generated.ETTMutation;
import Generated.ETTMutations;
import evolution.configuration.MutationIFC;
import evolution.configuration.MutationsIFC;

import java.util.ArrayList;
import java.util.List;

public class Mutations implements MutationsIFC {
    private List<Mutation> mutationList;

    public Mutations(ETTMutations gen) {
        mutationList = new ArrayList<>();
        for (ETTMutation m : gen.getETTMutation()) {
            Mutation mutation = null;
            switch (m.getName()) {
                case "Flipping":
                    mutation = Mutation.Flipping;
                    break;
                case "Sizer":
                    mutation = Mutation.Sizer;
                    break;
            }
            if (mutation != null) {
                mutation.setProbability(m.getProbability());
                mutation.parseString(m.getConfiguration());
                mutationList.add(mutation);
            }
        }
    }

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder result = new StringBuilder();
        for (Mutation m : mutationList) {
            result.append(m.toString()).append(lineSeparator);
        }
        return result.toString();
    }

    public List<Mutation> getMutationList() {
        return mutationList;
    }
}
