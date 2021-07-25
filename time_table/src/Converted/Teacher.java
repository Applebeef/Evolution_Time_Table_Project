package Converted;

import Generated.ETTTeacher;

public class Teacher {
    String name;
    int id;
    Teaching teaching;

    Teacher(ETTTeacher gen){
        name = gen.getETTName();
        id = gen.getId();
        teaching = new Teaching(gen.getETTTeaching());
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Teaching getTeaching() {
        return teaching;
    }
}
