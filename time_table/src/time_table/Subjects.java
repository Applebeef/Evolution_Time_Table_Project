package time_table;

import Generated.ETTSubjects;

import java.util.List;
import java.util.stream.Collectors;

public class Subjects {
    List<Subject> subjectList;

    Subjects(ETTSubjects gen){
        subjectList = gen.getETTSubject().stream().map(Subject::new).collect(Collectors.toList());
    }

    public List<Subject> getSubjectList() {
        return subjectList;
    }

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder result = new StringBuilder();
        for(Subject subject : subjectList){
            result.append(subject.toString()).append(lineSeparator);
        }
        return result.toString();
    }
}
