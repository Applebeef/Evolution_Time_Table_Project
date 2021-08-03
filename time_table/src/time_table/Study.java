package time_table;

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

    @Override
    public String toString() {
        return "Subject no. " + subjectId + ": " + hours + " hours.";//TODO add subject name?(optional)
    }
}
