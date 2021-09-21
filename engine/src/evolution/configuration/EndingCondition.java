package evolution.configuration;

public enum EndingCondition {
    FITNESS() {
        @Override
        boolean test(Number current, Number max) {
            if (max.doubleValue() != 0) {
                return current.doubleValue() > max.doubleValue();
            } else {
                return false;
            }

        }
    },
    GENERATIONS() {
        @Override
        boolean test(Number current, Number max) {
            if (max.intValue() != 0) {
                return current.intValue() > max.intValue();
            } else {
                return false;
            }

        }
    },
    TIME() {
        @Override
        boolean test(Number current, Number max) {
            //current = ChronoUnit.SECONDS.between(Instant.EPOCH, Instant.now());
            if (max.longValue() != 0) {
                return current.longValue() > max.longValue();
            } else
                return false;
        }
    };

    abstract boolean test(Number current, Number max);
}
