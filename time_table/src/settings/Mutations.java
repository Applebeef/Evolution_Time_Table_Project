package settings;

import evolution.configuration.MutationIFC;
import evolution.configuration.MutationsIFC;

import java.util.Arrays;
import java.util.List;

public class Mutations implements MutationsIFC {
    private List<MutationWrapper> mutationList;

//    public Mutations(ETTMutations gen) { //TODO fix
//        mutationList = Arrays.asList(Mutation.values());
//        for (ETTMutation m : gen.getETTMutation()) {
//            for (MutationIFC mutation : mutationList) {
//                if (mutation.getName().equals(m.getName())) {
//                    mutation.initFromXML(m);
//                }
//            }
//        }
//    }

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder result = new StringBuilder();
        for (MutationIFC m : mutationList) {
            result.append(m.toString()).append(lineSeparator);
        }
        return result.toString();
    }

    @Override
    public List<? extends MutationIFC> getMutationList() {
        return mutationList;
    }
}
