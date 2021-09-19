package time_table;

import evolution.rules.Type;
import solution.TimeTableSolution;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleWrapper {
    Rule rule;
    int configuration;
    Type type;

    public RuleWrapper(Rule rule, String configuration, Type type) {
        this.rule = rule;
        if (configuration != null) {
            this.configuration = parseString(configuration);
        }
        this.type = type;
    }

    private int parseString(String configuration) {
        Pattern pattern = Pattern.compile("^TotalHours=(\\d+)$");
        Matcher m = pattern.matcher(configuration);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        } else
            return 0;
    }

    public Type getType() {
        return type;
    }

    public String getRuleId() {
        return rule.ruleId;
    }

    public double test(TimeTableSolution timeTableSolution) {
        return rule.test(timeTableSolution, configuration);
    }
}
