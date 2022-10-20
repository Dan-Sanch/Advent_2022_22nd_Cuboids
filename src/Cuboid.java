import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Cuboid {

    final int left;
    final int right;
    final int bottom;
    final int top;
    final int back;
    final int front;
    final Point3D corner1; // "left-bottom-back"
    final Point3D corner2; // "right-top-front"


    public Cuboid(int startX, int endX, int startY, int endY, int startZ, int endZ) {
        if (endX < startX
                || endY < startY
                || endZ < startZ)
            throw new RuntimeException(String.format("Invalid dimensions: [%d,%d] [%d,%d] [%d,%d]",
                    startX, endX, startY, endY, startZ, endZ)
            );

        left = startX;
        right = endX;
        bottom = startY;
        top = endY;
        back = startZ;
        front = endZ;
        corner1 = new Point3D(left, bottom, back);
        corner2 = new Point3D(right, top, front);
    }

    public long getVolume() {
        return (long)
                (right - left) *
                (top - bottom) *
                (front - back);
    }

    public List<Cuboid> regionsNotOverlappingWith(Cuboid excludeRegion) {
        List<Cuboid> spilledCuboids = new ArrayList<>();

        if (!this.doesOverlapWith(excludeRegion)) {
            spilledCuboids.add(this);
            return spilledCuboids;
        }

        spilledCuboids.addAll(
                cuboidsSpilledHorizontallyOf(excludeRegion)
        );

        // We already did regions where X spilled, so avoid those
        int innerLeft = Math.max(this.left, excludeRegion.left);
        int innerRight = Math.min(this.right, excludeRegion.right);
        spilledCuboids.addAll(
                cuboidsSpilledVerticallyOf(excludeRegion, innerLeft, innerRight)
        );

        // Because we also did regions where Y spilled, avoid those as well
        int innerBottom = Math.max(this.bottom, excludeRegion.bottom);
        int innerTop = Math.min(this.top, excludeRegion.top);
        spilledCuboids.addAll(
                cuboidsSpilledDepthwiseOf(excludeRegion, innerLeft, innerRight, innerBottom, innerTop)
        );

        return spilledCuboids;
    }

    private Collection<Cuboid> cuboidsSpilledHorizontallyOf(Cuboid excludeRegion) {
        Collection<Cuboid> spilledToTheSides = new ArrayList<>(2);

        if (this.left < excludeRegion.left) {
            Cuboid spillToLeft = new Cuboid(
                    this.left, excludeRegion.left,
                    this.bottom, this.top,
                    this.back, this.front
            );
            spilledToTheSides.add(spillToLeft);
        }

        if (this.right > excludeRegion.right) {
            Cuboid spillToRight = new Cuboid(
                    excludeRegion.right, this.right,
                    this.bottom, this.top,
                    this.back, this.front
            );
            spilledToTheSides.add(spillToRight);
        }

        return spilledToTheSides;
    }

    private Collection<Cuboid> cuboidsSpilledVerticallyOf(Cuboid excludeRegion, int useLeft, int useRight) {
        Collection<Cuboid> spilledVertically = new ArrayList<>(2);

        if (this.bottom < excludeRegion.bottom) {
            Cuboid spillToBottom = new Cuboid(
                    useLeft, useRight,
                    this.bottom, excludeRegion.bottom,
                    this.back, this.front
            );
            spilledVertically.add(spillToBottom);
        }

        if (this.top > excludeRegion.top) {
            Cuboid spillToTop = new Cuboid(
                    useLeft, useRight,
                    excludeRegion.top, this.top,
                    this.back, this.front
            );
            spilledVertically.add(spillToTop);
        }
        return spilledVertically;
    }

    private Collection<Cuboid> cuboidsSpilledDepthwiseOf(Cuboid excludeRegion, int useLeft, int useRight, int useBottom, int useTop) {
        Collection<Cuboid> spilledDepth = new ArrayList<>(2);
        if (this.back < excludeRegion.back) {
            Cuboid spillToBack = new Cuboid(
                    useLeft, useRight,
                    useBottom, useTop,
                    this.back, excludeRegion.back
            );
            spilledDepth.add(spillToBack);
        }

        if (this.front > excludeRegion.front) {
            Cuboid spillToFront = new Cuboid(
                    useLeft, useRight,
                    useBottom, useTop,
                    excludeRegion.front, this.front
            );
            spilledDepth.add(spillToFront);
        }
        return spilledDepth;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != Cuboid.class)
            return false;

        Cuboid other = (Cuboid) obj;
        return this.corner1.equals(other.corner1)
                && this.corner2.equals(other.corner2);
    }

    public boolean doesOverlapWith(Cuboid otherCuboid) {
        if (this.getVolume() == 0 || otherCuboid.getVolume() == 0)
            return false;

        // If one cuboid is on left side of another
        if (this.left >= otherCuboid.right || otherCuboid.left >= this.right)
            return false;

        // If one cuboid is above another
        if (this.bottom >= otherCuboid.top || otherCuboid.bottom >= this.top)
            return false;

        // If one cuboid is in front of another
        if (this.back >= otherCuboid.front || otherCuboid.back >= this.front)
            return false;

        return true;
    }

    private record Point3D(
            int x,
            int y,
            int z
    ) {}
}
