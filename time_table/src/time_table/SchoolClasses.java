package time_table;

import Generated.ETTClasses;

import java.util.*;
import java.util.stream.Collectors;

public class SchoolClasses {
    List<SchoolClass> schoolClassList;

    SchoolClasses(ETTClasses gen) {
        schoolClassList = gen.getETTClass().stream().map(SchoolClass::new).collect(Collectors.toList());
        schoolClassList.sort(Comparator.comparingInt(SchoolClass::getId));
    }

    public List<SchoolClass> getSchoolClassList() {
        return schoolClassList;
    }

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder allClasses = new StringBuilder();
        for (SchoolClass schoolClass : schoolClassList) {
            allClasses.append(schoolClass.toString()).append(lineSeparator);
        }
        return allClasses.toString();
    }

    public String checkValidity() {
        List<Integer> list = new ArrayList<>();
        for (SchoolClass schoolClass : schoolClassList) {
            if (list.contains(schoolClass.getId())) {
                return "File contains 2 classes with the same ID." + System.lineSeparator();
            } else
                list.add(schoolClass.getId());
        }
        Collections.sort(list);
        for (int i = 0; i < list.size() - 1; i++) {
            if (!list.get(i).equals(list.get(i + 1) - 1)) {
                return "Class IDs aren't a running sequence." + System.lineSeparator();
            }
        }
        return "";
    }

    public String checkSubjectValidity(Subjects subjects) {
        List<String> errorList = new ArrayList<>();
        boolean found = false;
        for (SchoolClass schoolClass : schoolClassList) {
            for (Study study : schoolClass.requirements.studyList) {
                int id = study.getSubjectId();
                for (Subject subject : subjects.getSubjectList()) {
                    if (subject.getId() == id) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    errorList.add("Class " + schoolClass.getId() + "are required to study an invalid subject ID: " + id);
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

    public String checkHourValidity(int hours, int days) {
        int weeklyHours = hours * days;
        List<String> errorList = new ArrayList<>();
        for (SchoolClass schoolClass : schoolClassList) {
            int hoursSum = 0;
            for (Study study : schoolClass.getRequirements().getStudyList()) {
                hoursSum += study.getHours();
            }
            if (hoursSum > weeklyHours) {
                errorList.add("Class no. " + schoolClass.getId() + " requires more hours per week than possible.");
            }
        }
        if (errorList.isEmpty()) {
            return "";
        } else {
            StringBuilder str = new StringBuilder();
            for (String error : errorList) {
                str.append(error).append(System.getProperty("line.separator"));
            }
            return str.toString();
        }
    }
}
