package evolution.configuration;

import java.util.List;

public interface MutationsIFC {
    List<? extends MutationIFC> getMutationList();
}
