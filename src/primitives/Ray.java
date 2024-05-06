package primitives;

import java.util.Objects;

public class Ray
{
    private final Double3 head;
    private final Vector _dir;

    public Ray(Double3 head, Vector dir) {
        this.head = head;
        _dir = dir;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ray ray = (Ray) o;
        return head.equals(ray.head) && _dir.equals(ray._dir);
    }

    @Override
    public String toString() {
        return "Ray{" +
                head +
                _dir +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, _dir);
    }
}
