package evolutionaryApp.utils.resultUtils;

import solution.Fifth;

public enum ResultDisplay {
    TEACHER {
        @Override
        String getDisplay(Fifth fifth) {
            return "Class: " +
                    fifth.getSchoolClass() + System.lineSeparator() + "Subject: " +
                    fifth.getSubject();
        }
    },
    CLASS {
        @Override
        String getDisplay(Fifth fifth) {
            return "Teacher: " + fifth.getTeacher() + System.lineSeparator() + "Subject: " + fifth.getSubject();
        }
    };

    int id = 1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    abstract String getDisplay(Fifth fifth);
}
