package time_table;

import Generated.ETTSubjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Subjects {
    List<Subject> subjectList;

    Subjects(ETTSubjects gen) {
        subjectList = gen.getETTSubject().stream().map(Subject::new).collect(Collectors.toList());
        subjectList.sort(Comparator.comparingInt(Subject::getId));
    }

    public List<Subject> getSubjectList() {
        return subjectList;
    }

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder result = new StringBuilder();
        for (Subject subject : subjectList) {
            result.append(subject.toString()).append(lineSeparator);
        }
        return result.toString();
    }

    public String checkValidity() {
        List<Integer> list = new ArrayList<>();
        for (Subject subject : subjectList) {
            if (list.contains(subject.getId())) {
                return "File contains 2 subjects with the same ID." + System.lineSeparator();
            } else
                list.add(subject.getId());
        }
        Collections.sort(list);
        for (int i = 0; i < list.size() - 1; i++) {
            if (!list.get(i).equals(list.get(i + 1) - 1)) {
                return "Subject IDs aren't a running sequence." + System.lineSeparator();
            }
        }
        return "";
    }
}
