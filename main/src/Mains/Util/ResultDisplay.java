package Mains.Util;

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
    },
    RAW {
        @Override
        String getDisplay(Fifth fifth) {
            return fifth.toString();
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
