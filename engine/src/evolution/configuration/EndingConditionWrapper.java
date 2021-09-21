package evolution.configuration;

public class EndingConditionWrapper {
    EndingCondition endingCondition;
    Number max;

    public EndingConditionWrapper(EndingCondition endingCondition, Number max) {
        this.endingCondition = endingCondition;
        this.max = max;
    }

    public boolean test(Number current) {
        return this.endingCondition.test(current,this.max);
    }

        public EndingCondition getEndingCondition() {
        return endingCondition;
    }

    public void setEndingCondition(EndingCondition endingCondition) {
        this.endingCondition = endingCondition;
    }

    public Number getMax() {
        return max;
    }

    public void setMax(Number max) {
        this.max = max;
    }
}
