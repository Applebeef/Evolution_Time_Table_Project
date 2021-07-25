package Converted;

import Generated.ETTTeachers;

import java.util.List;
import java.util.stream.Collectors;

public class Teachers {
    List<Teacher> teacherList;

    Teachers(ETTTeachers gen){
        teacherList = gen.getETTTeacher().stream().map(Teacher::new).collect(Collectors.toList());
    }

    public List<Teacher> getTeacherList() {
        return teacherList;
    }
}
