package Converted;

import Generated.ETTSubject;

public class Subject {
    String name;
    int id;

    Subject(ETTSubject gen){
        name = gen.getName();
        id = gen.getId();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "    Name: " + name + ", ID: " + id;
    }
}
