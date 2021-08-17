package time_table;

import evolution.util.Triplets;

import evolution.rules.*;
import solution.*;
import evolution.util.Pair;

import java.util.*;
import java.util.stream.Collectors;


public enum Rule {
    //TODO add new EX2 rule
    TEACHER_IS_HUMAN("TeacherIsHuman") {
        @Override
        public double test(TimeTableSolution timeTableSolution) {
            double score = 100;
            double reduction = score / timeTableSolution.getFifthsList().size();
            //Maps each teacher to all his assigned days/hours.
            Map<Integer, Set<Pair<Integer, Integer>>> teacherDayHourMap = new HashMap<>();
            for (Fifth fifth : timeTableSolution.getFifthsList()) {
                //If this is the first time we encounter this teacher, add him to the map.
                if (!teacherDayHourMap.containsKey(fifth.getTeacher())) {
                    teacherDayHourMap.put(fifth.getTeacher(), new HashSet<>());
                }
                Pair<Integer, Integer> pair = new Pair<>(fifth.getDay(), fifth.getHour());
                //Check if current day/hour combo is already in the set.
                if (teacherDayHourMap.get(fifth.getTeacher()).contains(pair)) {
                    score -= reduction;
                } else {
                    //If the pair isn't in the set, we add it for the next checks.
                    teacherDayHourMap.get(fifth.getTeacher()).add(pair);
                }
            }
            return score;
        }
    },
    SINGULARITY("Singularity") {
        @Override
        public double test(TimeTableSolution timeTableSolution) {
            double score = 100;
            double reduction = score / timeTableSolution.getFifthsList().size();
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
                if (!tripletsSubjectMap.get(integerTriplets).equals(fifth.getSubject())) {
                    score -= reduction;
                } else if (!tripletsTeacherMap.get(integerTriplets).equals(fifth.getTeacher())) {
                    score -= reduction;
                }
            }
            return score;
        }
    },
    KNOWLEDGEABLE("Knowledgeable") {
        @Override
        public double test(TimeTableSolution timeTableSolution) {
            double score = 100;
            double reduction = score / timeTableSolution.getFifthsList().size();
            for (Fifth fifth : timeTableSolution.getFifthsList()) {
                int teacherID = fifth.getTeacher();
                List<Teacher> singleTeacherList = timeTableSolution.getTimeTable().getTeachers().getTeacherList(). // Contains a single teacher with a matching id to the current fifth.
                        stream().filter(teacher -> teacher.getId() == teacherID).limit(1).collect(Collectors.toList());

                for (Teacher teacher : singleTeacherList) {
                    //List of all the subject ids the teacher is allowed to teach:
                    List<Integer> subjectIDList = teacher.getTeaching().getTeachesList().
                            stream().map(Teaches::getSubjectId).collect(Collectors.toList());
                    //If the subject isn't taught by the teacher, the test failed:
                    if (!subjectIDList.contains(fifth.getSubject())) {
                        score -= reduction;
                    }
                }
            }
            return score;
        }
    },
    SATISFACTORY("Satisfactory") {
        @Override
        public double test(TimeTableSolution timeTableSolution) {
            double score = 100;
            double reduction;
            int requiredSubjectsToCheck = 0;
            for (SchoolClass schoolClass : timeTableSolution.getTimeTable().getSchoolClasses().getClassList()) {
                requiredSubjectsToCheck += schoolClass.requirements.studyList.size();
            }
            reduction = score / (double) requiredSubjectsToCheck;
            //Map mapping Classes to a map mapping subjects to hours learned:.
            Map<Integer, Map<Integer, Integer>> mapSchoolClassToSubjectHoursMapMap = new HashMap<>();

            for (Fifth fifth : timeTableSolution.getFifthsList()) {
                //If this is the first time we encounter this class, add the class to the map:.
                if (!mapSchoolClassToSubjectHoursMapMap.containsKey(fifth.getSchoolClass())) {
                    mapSchoolClassToSubjectHoursMapMap.put(fifth.getSchoolClass(), new HashMap<>());
                }
                //If this is the first time we encounter this subject, add the subject to the class map:.
                if (!mapSchoolClassToSubjectHoursMapMap.get(fifth.getSchoolClass()).containsKey(fifth.getSubject())) {
                    mapSchoolClassToSubjectHoursMapMap.get(fifth.getSchoolClass()).put(fifth.getSubject(), 0);
                }
                //Increment total amount of hours learned in subject:.
                Integer hours = mapSchoolClassToSubjectHoursMapMap.get(fifth.getSchoolClass()).get(fifth.getSubject());
                hours++;
                mapSchoolClassToSubjectHoursMapMap.get(fifth.getSchoolClass()).put(fifth.getSubject(), hours);
            }
            //Check if the total amount of learned hours is equal to the required amount of hours per subject:
            for (SchoolClass schoolClass : timeTableSolution.getTimeTable().getSchoolClasses().getClassList()) {//FIXME random crashing on some runs.
                for (Study study : schoolClass.requirements.studyList) {
                    int classID = schoolClass.getId();
                    int subjectID = study.getSubjectId();
                    int hours = study.getHours();
                    Map<Integer, Integer> subjectHoursMap = mapSchoolClassToSubjectHoursMapMap.get(classID);
                    if (subjectHoursMap != null) {
                        Integer studiedHours = subjectHoursMap.get(subjectID);
                        //!mapSchoolClassToSubjectHoursMapMap.get(schoolClass.getId()).get(study.subjectId).equals(study.hours)
                        if (studiedHours == null || studiedHours != hours) {
                            score -= reduction;
                        }
                    }
                }
            }
            return score;
        }
    },
    DAY_OFF_TEACHER("DayOffTeacher ") {
        @Override
        public double test(TimeTableSolution timeTableSolution) {
            double score = 100;
            double reduction = (double) 100 / timeTableSolution.getTimeTable().getAmountofTeachers();
            //Map with the key representing each teacher, containing maps with the key representing a certain day,
            //the map's value represents whether the teacher is teaching on that day or not.
            Map<Integer, Map<Integer, Boolean>> weeklyMapPerTeacher = new HashMap<>();
            for (Fifth fifth : timeTableSolution.getFifthsList()) {
                weeklyMapPerTeacher.put(fifth.getTeacher(), new HashMap<Integer, Boolean>());
            }
            return score;
        }
    },
    WORKING_HOURS_PREFERENCE("WorkingHoursPreference") {
        @Override
        public double test(TimeTableSolution timeTableSolution) {
            return 0;
        }
    };

    String ruleId;
    String Configuration;
    Type type;

    abstract public double test(TimeTableSolution timeTableSolution);

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
