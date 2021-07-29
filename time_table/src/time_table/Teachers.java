package time_table;

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

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder result = new StringBuilder();
        for(Teacher teacher : teacherList){
            result.append(teacher.toString()).append(lineSeparator);
        }
        return result.toString();
    }
}
