package time_table;

import days_hours.Day;
import days_hours.Hour;
import days_hours.SchoolClassTeacherSubject;
import evolution.engine.problem_solution.Solution;
import evolution.rules.*;

import java.util.ArrayList;
import java.util.List;


public enum Rule implements Testable {
    TEACHER_IS_HUMAN("TeacherIsHuman") {
        @Override
        public int test(Solution solution) {
            TimeTableSolution timeTableSolution = (TimeTableSolution) solution;
            List<Boolean> teacherIsTeaching = new ArrayList<>();
            timeTableSolution.getTimeTable().getTeachers().getTeacherList().forEach(teacher -> teacherIsTeaching.add(false));//initializing boolean array representing teachers.

            for (Day day : timeTableSolution.getDayList()) {
                for (Hour hour : day.getHourList()) {
                    for (SchoolClassTeacherSubject schoolClassTeacherSubject : hour.getSchoolClassTeacherSubjectList()) {
                        int id = schoolClassTeacherSubject.getTeacher().getId() - 1;
                        if (teacherIsTeaching.get(id)) {
                            return 0;
                        } else
                            teacherIsTeaching.set(id, true);
                    }
                }
                teacherIsTeaching.forEach(teacher -> teacher = false);
            }
            return 100;
        }
    },
    SINGULARITY("Singularity") {
        @Override
        public int test(Solution solution) {
            TimeTableSolution timeTableSolution = (TimeTableSolution) solution;

            return 0;
            //TODO add test
        }
    },
    KNOWLEDGEABLE("Knowledgeable") {
        @Override
        public int test(Solution solution) {
            TimeTableSolution timeTableSolution = (TimeTableSolution) solution;
            for(Day day : timeTableSolution.getDayList()){
                for(Hour hour : day.getHourList()){
                    for(SchoolClassTeacherSubject schoolClassTeacherSubject : hour.getSchoolClassTeacherSubjectList()){
                        for(Teaches teaches : schoolClassTeacherSubject.getTeacher().getTeaching().getTeachesList()){
                            if(teaches.getSubjectId()!=schoolClassTeacherSubject.getSubject().getId())
                                return 0;
                        }
                    }
                }
            }
            return 100;
        }
    },
    SATISFACTORY("Satisfactory") {
        @Override
        public int test(Solution solution) {
            return 0;//TODO add test
        }
    };

    String ruleId;
    String Configuration;
    Type type;

    Rule(String id) {
        ruleId = id;
    }

    public String getConfiguration() {
        return Configuration;
    }

    public void setConfiguration(String configuration) {
        Configuration = configuration;//TODO ask aviad about configuration handling
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return ruleId + " is a " + type + " rule.";
    }
}
