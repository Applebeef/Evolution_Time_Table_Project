package Util;

import java.util.Objects;

public class Triplets<V1,V2,V3> {
    private V1 v1;
    private V2 v2;
    private V3 v3;

    public Triplets(V1 v1, V2 v2, V3 v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public V1 getV1() {
        return v1;
    }

    public void setV1(V1 v1) {
        this.v1 = v1;
    }

    public V2 getV2() {
        return v2;
    }

    public void setV2(V2 v2) {
        this.v2 = v2;
    }

    public V3 getV3() {
        return v3;
    }

    public void setV3(V3 v3) {
        this.v3 = v3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triplets<?, ?, ?> triplets = (Triplets<?, ?, ?>) o;
        return Objects.equals(v1, triplets.v1) && Objects.equals(v2, triplets.v2) && Objects.equals(v3, triplets.v3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(v1, v2, v3);
    }
}
