package Converted;

import Generated.ETTRequirements;

import java.util.List;
import java.util.stream.Collectors;

public class Requirements {
    List<Study> studyList;

    Requirements(ETTRequirements gen){
        studyList = gen.getETTStudy().stream().map(Study::new).collect(Collectors.toList());
    }

    public List<Study> getStudyList() {
        return studyList;
    }
}
