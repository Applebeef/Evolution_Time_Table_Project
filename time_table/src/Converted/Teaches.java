package Converted;

import Generated.ETTTeaches;

public class Teaches {
    int subjectId;

    Teaches(ETTTeaches gen){
        subjectId = gen.getSubjectId();
    }

    public int getSubjectId() {
        return subjectId;
    }
}
