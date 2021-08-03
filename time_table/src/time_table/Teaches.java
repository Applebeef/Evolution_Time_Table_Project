package time_table;

import Generated.ETTTeaches;

public class Teaches {
    int subjectId;

    Teaches(ETTTeaches gen){
        subjectId = gen.getSubjectId();
    }

    public int getSubjectId() {
        return subjectId;
    }

    @Override
    public String toString() {
        return String.valueOf(subjectId);
    }
}
