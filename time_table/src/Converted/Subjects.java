package Converted;

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
}
