package Converted;

import Generated.ETTStudy;

public class Study {
    int subjectId;
    int hours;

    Study(ETTStudy gen){
        this.subjectId= gen.getSubjectId();
        this.hours = gen.getHours();
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getHours() {
        return hours;
    }
}
