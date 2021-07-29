package time_table;

import Util.Triplets;

import evolution.rules.*;
import Solution.*;
import Util.Pair;

import java.util.*;
import java.util.stream.Collectors;


public enum Rule {
    TEACHER_IS_HUMAN("TeacherIsHuman") {
        @Override
        public int test(TimeTableSolution timeTableSolution) {
            Map<Integer, Set<Pair<Integer, Integer>>> teacherDayHourMap = new HashMap<>();//Maps each teacher to all his assigned days/hours.
            for (Fifth fifth : timeTableSolution.getFifthsList()) {
                if (!teacherDayHourMap.containsKey(fifth.getTeacher())) {//If this is the first time we encounter this teacher, add him to the map.
                    teacherDayHourMap.put(fifth.getTeacher(), new HashSet<>());
                }
                Pair<Integer, Integer> pair = new Pair<>(fifth.getDay(), fifth.getHour());
                if (teacherDayHourMap.get(fifth.getTeacher()).contains(pair)) {//Check if current day/hour combo is already in the set.
                    return 0;
                } else {
                    teacherDayHourMap.get(fifth.getTeacher()).add(pair);//If the pair isn't in the set, we add it for the next checks.
                }
            }
            return 100;

        }
    },
    SINGULARITY("Singularity") {
        @Override
        public int test(TimeTableSolution timeTableSolution) {
            //Maps mapping Day/Hour/Class to their subject/teachers.
            Map<Triplets<Integer, Integer, Integer>, Integer> tripletsSubjectMap = new HashMap<>();
            Map<Triplets<Integer, Integer, Integer>, Integer> tripletsTeacherMap = new HashMap<>();

            for (Fifth fifth : timeTableSolution.getFifthsList()) {
                Triplets<Integer, Integer, Integer> integerTriplets = new Triplets<>(fifth.getDay(), fifth.getHour(), fifth.getSchoolClass());
                //If this is the first time we encounter this combo, add to the map:
                if (!tripletsSubjectMap.containsKey(integerTriplets)) {
                    tripletsSubjectMap.put(integerTriplets, fifth.getSubject());
                }
                if (!tripletsTeacherMap.containsKey(integerTriplets)) {
                    tripletsTeacherMap.put(integerTriplets, fifth.getTeacher());
                }

                //Check if the current subject/teacher is equal to the one currently saved in the map.
                if(!tripletsSubjectMap.get(integerTriplets).equals(fifth.getSubject())){
                    return 0;
                }
                if(!tripletsTeacherMap.get(integerTriplets).equals(fifth.getTeacher())){
                    return 0;
                }
            }
            return 100;
        }
    },
    KNOWLEDGEABLE("Knowledgeable") {
        @Override
        public int test(TimeTableSolution timeTableSolution) {
            for (Fifth fifth : timeTableSolution.getFifthsList()) {
                int teacherID = fifth.getTeacher();
                List<Teacher> singleTeacherList = timeTableSolution.getTimeTable().getTeachers().getTeacherList(). // Contains a single teacher with a matching id to the current fifth.
                        stream().filter(teacher -> teacher.getId() == teacherID).limit(1).collect(Collectors.toList());

                for (Teacher teacher : singleTeacherList) {
                    List<Integer> subjectIDList = teacher.getTeaching().getTeachesList().
                            stream().map(Teaches::getSubjectId).collect(Collectors.toList());//List of all the subject ids the teacher is allowed to teach.
                    if (!subjectIDList.contains(fifth.getSubject())) {//If the subject isn't taught by the teacher, the test failed.
                        return 0;
                    }
                }
            }
            return 100;
        }
    },
    SATISFACTORY("Satisfactory") {
        @Override
        public int test(TimeTableSolution timeTableSolution) {

            return 0;//TODO add test
        }
    };

    String ruleId;
    String Configuration;
    Type type;

    abstract public int test(TimeTableSolution timeTableSolution);

    Rule(String id) {
        ruleId = id;
    }

    public String getConfiguration() {
        return Configuration;
    }

    public void setConfiguration(String configuration) {
        Configuration = configuration;
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
