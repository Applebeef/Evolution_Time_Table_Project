package time_table;

import Generated.ETTTeachers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Teachers {
    List<Teacher> teacherList;

    Teachers(ETTTeachers gen) {
        teacherList = gen.getETTTeacher().stream().map(Teacher::new).collect(Collectors.toList());
    }

    public List<Teacher> getTeacherList() {
        return teacherList;
    }

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder result = new StringBuilder();
        for (Teacher teacher : teacherList) {
            result.append(teacher.toString()).append(lineSeparator);
        }
        return result.toString();
    }

    public String checkIDValidity() {
        List<Integer> list = new ArrayList<>();
        for (Teacher teacher : teacherList) {
            if (list.contains(teacher.getId())) {
                return "File contains 2 teachers with the same ID.";
            } else
                list.add(teacher.getId());
        }
        Collections.sort(list);
        for (int i = 0; i < list.size() - 1; i++) {
            if (!list.get(i).equals(list.get(i + 1) - 1)) {
                return "Teacher IDs aren't a running sequence.";
            }
        }
        return "";
    }

    public String checkSubjectValidity(Subjects subjects) {
        List<String> errorList = new ArrayList<>();
        boolean found = false;
        for (Teacher teacher : teacherList) {
            for (Teaches teaches : teacher.getTeaching().getTeachesList()) {
                int id = teaches.getSubjectId();
                for (Subject subject : subjects.getSubjectList()) {
                    if (subject.getId() == id) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    errorList.add("Teacher " + teacher.getId() + " is teaching an invalid subject ID: " + id);
                }
                found = false;
            }
        }
        StringBuilder res = new StringBuilder();
        for (String str : errorList) {
            res.append(str).append(System.getProperty("line.separator"));
        }
        return res.toString();
    }
}
