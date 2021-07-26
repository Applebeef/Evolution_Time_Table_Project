package evolution.configuration;

import Generated.ETTSelection;
import java.util.regex.*;

public class Selection {
    protected String type;
    protected int configuration;

    public Selection(ETTSelection gen) {
        this.type = gen.getType();
        Pattern pattern = Pattern.compile("^TopPercent=(\\d+)$");
        Matcher m = pattern.matcher(gen.getConfiguration());
        configuration = Integer.parseInt(m.group(1));
    }

    public String getType() {
        return type;
    }

    public int getConfiguration() {
        return configuration;
    }
}
