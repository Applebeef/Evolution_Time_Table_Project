package evolution.configuration;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;

import java.sql.Time;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;

public enum EndingCondition {
    FITNESS() {
        @Override
        boolean test(Number current) {
            if (max.doubleValue() != 0) {
                return current.doubleValue() >= max.doubleValue();
            } else {
                return false;
            }

        }
    },
    GENERATIONS() {
        @Override
        boolean test(Number current) {
            if (max.intValue() != 0) {
                return current.intValue() >= max.intValue();
            } else {
                return false;
            }

        }
    },
    TIME() {
        @Override
        boolean test(Number current) {
            //current = ChronoUnit.SECONDS.between(Instant.EPOCH, Instant.now());
            if (max.longValue() != 0) {
                return current.longValue() >= max.longValue();
            } else
                return false;
        }
    };

    Number max;

    EndingCondition() {
        this.max = 0;
    }

    public Number getMax() {
        return max;
    }

    public void setMax(Number max) {
        this.max = max;
    }

    abstract boolean test(Number current);
}
