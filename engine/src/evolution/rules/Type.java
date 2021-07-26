package evolution.rules;

public enum Type {
    HARD("Hard"),
    SOFT("Soft");

    String type;

    Type(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
