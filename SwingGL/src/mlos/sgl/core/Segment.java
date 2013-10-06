package mlos.sgl.core;

import static com.google.common.base.Preconditions.checkNotNull;

public class Segment {
    
    public final Vec2d a;
    public final Vec2d b;

    public Segment(Vec2d a, Vec2d b) {
        this.a = checkNotNull(a);
        this.b = checkNotNull(b);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Segment) {
            Segment other = (Segment) o;
            return a.equals(other.a) && b.equals(other.b);
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return String.format("[%s, %s]", a, b);
    }

}
