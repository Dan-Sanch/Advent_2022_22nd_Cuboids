import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.*;


public class CuboidTest {

    // 1 * 1 * 1 cuboid
    public static final Cuboid VOLUME_1_CUBOID = new Cuboid(
            0, 1,
            0, 1,
            0, 1
    );

    @Test
    public void getZeroVolume() {
        int x1 = 0;
        int x2 = 0;
        int y1 = 0;
        int y2 = 0;
        int z1 = 0;
        int z2 = 0;

        assertEquals(0, new Cuboid(x1,x2,y1,y2,z1,z2).getVolume());

        y2 = 1;
        z2 = 1;
        assertEquals(0, new Cuboid(x1,x2,y1,y2,z1,z2).getVolume());
    }

    @Test
    public void getVolume() {
        int x1 = 0;
        int x2 = 1;
        int y1 = 0;
        int y2 = 1;
        int z1 = 0;
        int z2 = 1;

        Cuboid cuboid = new Cuboid(x1,x2,y1,y2,z1,z2);
        assertEquals(1, cuboid.getVolume());

        x2 = 3;
        y2 = 3;
        z2 = 3;
        assertEquals(27, new Cuboid(x1,x2,y1,y2,z1,z2).getVolume());

        x2 = 5;
        y2 = 6;
        z2 = 7;
        assertEquals(210, new Cuboid(x1,x2,y1,y2,z1,z2).getVolume());

        x1 = -5;
        y1 = -6;
        z1 = -7;
        assertEquals(1680, new Cuboid(x1,x2,y1,y2,z1,z2).getVolume());
    }

    @Test
    public void failNegativeLength() {
        int x1 = 0;
        int y1 = 0;
        int z1 = 0;
        assertThrows(RuntimeException.class, () ->
                new Cuboid(
                        x1,-1,
                        y1,1,
                        z1,1
                )
        );

        assertThrows(RuntimeException.class, () ->
                new Cuboid(
                        x1,1,
                        y1,-1,
                        z1,1
                )
        );

        assertThrows(RuntimeException.class, () ->
                new Cuboid(
                        x1,1,
                        y1,1,
                        z1,-1
                )
        );
    }

    @Test
    public void equalsTest() {
        Cuboid cuboid1 = new Cuboid(0,0,0,0,0,0);
        Cuboid cuboid2 = new Cuboid(0,0,0,0,0,0);
        assertEquals(cuboid1, cuboid2);

        // same volume, different places, must fail
        Cuboid cuboid3 = new Cuboid(10,10,10,10,10,10);
        assertNotEquals(cuboid1, cuboid3);
    }

    @Test
    public void nonOverlappingCuboids() {
        Cuboid cuboid = new Cuboid(0,10,0,10,0,10);
        Cuboid nonOverlapCuboid = new Cuboid(100, 200,100, 200,100, 200);

        assertFalse(cuboid.doesOverlapWith(nonOverlapCuboid));
    }

    @Test
    public void zeroVolumeCuboidsDontOverlap() {
        Cuboid cuboid1 = new Cuboid(0,0,0,0,0,0);
        // cuboid1 is a point within cuboid2. They don't overlap because cuboid1 has volume 0
        Cuboid cuboid2 = new Cuboid(-10,10,-10,10,-10,10);

        assertCuboidsDontOverlap(cuboid1, cuboid2);

        // cuboid3 is the same as cuboid1, both are points with zero volume occupying the same space
        Cuboid cuboid3 = new Cuboid(0,0,0,0,0,0);
        assertCuboidsDontOverlap(cuboid1, cuboid3);
    }

    @Test
    public void equalCuboidsOverlap() {
        Cuboid cuboid1 = new Cuboid(0,10,0,10,0,10);
        Cuboid cuboid2 = new Cuboid(0,10,0,10,0,10);

        assertEquals(cuboid1, cuboid2);
        assertTrue(cuboid1.doesOverlapWith(cuboid2));
        assertTrue(cuboid2.doesOverlapWith(cuboid1));
    }

    @Test
    public void cuboidInsideAnotherOverlap() {
        Cuboid cuboid1 = new Cuboid(0,11,0,11,0,11);
        // cuboid2 is entirely contained within the "upper right corner" of cuboid1
        Cuboid cuboid2 = new Cuboid(5,11,5,11,5,11);

        assertTrue(cuboid1.doesOverlapWith(cuboid2));
        assertTrue(cuboid2.doesOverlapWith(cuboid1));
    }

    @Test
    public void cuboidsPartiallyOverlap() {
        Cuboid cuboid1 = new Cuboid(
                0,11,
                0,11,
                0,11
        );
        Cuboid cuboid2 = new Cuboid(
                10,20,
                10,20,
                10,20
        );

        assertTrue(cuboid1.doesOverlapWith(cuboid2));
        assertTrue(cuboid2.doesOverlapWith(cuboid1));
    }

    @Test
    public void cuboidInFrontOfAnotherDontOverlap() {
        Cuboid cuboid1 = new Cuboid(0,11,0,11,0,11);
        // cuboid2 is "in front" of cuboid1. Only Z axis positioning is different, otherwise it's the same
        Cuboid cuboid2 = new Cuboid(0,11,0,11,11,22);

        assertCuboidsDontOverlap(cuboid1, cuboid2);
    }

    @Test
    public void cuboidAboveAnotherDontOverlap() {
        Cuboid cuboid1 = new Cuboid(0,11,0,11,0,11);
        // cuboid2 is "above" cuboid1. Only Y axis positioning is different, otherwise it's the same
        Cuboid cuboid2 = new Cuboid(0,11,11,22,0,11);

        assertCuboidsDontOverlap(cuboid1, cuboid2);
    }

    @Test
    public void cuboidBesidesAnotherDontOverlap() {
        Cuboid cuboid1 = new Cuboid(0,11,0,11,0,11);
        // cuboid2 is "besides" cuboid1. Only X axis positioning is different, otherwise it's the same
        Cuboid cuboid2 = new Cuboid(11,22,11,22,0,11);

        assertCuboidsDontOverlap(cuboid1, cuboid2);
    }

    @Test
    public void nonOverlappingCuboidsReturnWholeCuboid() {
        Cuboid cuboid1 = new Cuboid(0,10,0,10,0,10);
        Cuboid cuboid2 = new Cuboid(100, 200,100, 200,100, 200);

        List<Cuboid> resultCuboids = cuboid1.regionsNotOverlappingWith(cuboid2);
        assertEquals(1, resultCuboids.size());
        assertEquals(cuboid1, resultCuboids.get(0));
    }

    @Test
    public void allEnvelopingCuboidReturnsZeroNonOverlappingCuboids() {
        Cuboid cuboid1 = new Cuboid(0,10,0,10,0,10); // 10 x 10 x 10
        Cuboid cuboid2 = new Cuboid(-10, 20,-10, 20,-10, 20); // 30 x 30 x 30

        List<Cuboid> resultCuboids = cuboid1.regionsNotOverlappingWith(cuboid2);
        assertTrue(resultCuboids.isEmpty());
    }

    @Test
    public void getNonOverlappingSpillToTheLeft() {
        // cuboid1 is nearly contained within exclusionZone, except for a small region "to the side" (non overlapping X axis)
        Cuboid cuboid1 = new Cuboid(-1,1,0,1,0,1); // 2 * 1 * 1
        Cuboid exclusionZone = VOLUME_1_CUBOID;
        assertEquals(2, cuboid1.getVolume());

        List<Cuboid> nonOverlaps = cuboid1.regionsNotOverlappingWith(exclusionZone);
        assertEquals(1, nonOverlaps.size());
        assertEquals(new Cuboid(-1, 0, 0, 1, 0, 1), nonOverlaps.get(0));
        assertEquals(1, nonOverlaps.get(0).getVolume());
    }

    @Test
    public void getNonOverlappingSpillToTheRight() {
        // cuboid1 is nearly contained within exclusionZone, except for a small region "to the side" (non overlapping X axis)
        Cuboid cuboid1 = new Cuboid(0,2,0,1,0,1); // 2 * 1 * 1
        Cuboid exclusionZone = VOLUME_1_CUBOID;
        assertEquals(2, cuboid1.getVolume());

        List<Cuboid> nonOverlaps = cuboid1.regionsNotOverlappingWith(exclusionZone);
        assertEquals(1, nonOverlaps.size());
        assertEquals(new Cuboid(1, 2, 0, 1, 0, 1), nonOverlaps.get(0));
        assertEquals(1, nonOverlaps.get(0).getVolume());
    }

    @Test
    public void getNonOverlappingSpillToLeftAndRight() {
        // cuboid1 is nearly contained within exclusionZone, except for a small region "to the sides" (non overlapping X axis)
        Cuboid cuboid1 = new Cuboid(-1,2,0,1,0,1); // 3 * 1 * 1
        Cuboid exclusionZone = VOLUME_1_CUBOID;
        assertEquals(3, cuboid1.getVolume());

        List<Cuboid> nonOverlaps = cuboid1.regionsNotOverlappingWith(exclusionZone);
        assertEquals(2, nonOverlaps.size());
        assertTrue(nonOverlaps.contains(
                new Cuboid(1, 2, 0, 1, 0, 1)));
        assertTrue(nonOverlaps.contains(
                new Cuboid(-1, 0, 0, 1, 0, 1)));
        assertEquals(2,
                sumVolumes(nonOverlaps));
    }

    @Test
    public void getNonOverlappingSpillToTheBottom() {
        // cuboid1 is nearly contained within exclusionZone, except for a small region "above" (non overlapping on Y axis)
        Cuboid cuboid1 = new Cuboid(0,1,-1,1,0,1); // 1 * 2 * 1
        Cuboid exclusionZone = VOLUME_1_CUBOID;
        assertEquals(2, cuboid1.getVolume());

        List<Cuboid> nonOverlaps = cuboid1.regionsNotOverlappingWith(exclusionZone);
        assertEquals(1, nonOverlaps.size());
        assertEquals(new Cuboid(0, 1, -1, 0, 0, 1), nonOverlaps.get(0));
        assertEquals(1, nonOverlaps.get(0).getVolume());
    }

    @Test
    public void getNonOverlappingSpillsToTheTopAndBottom() {
        // cuboid1 is nearly contained within exclusionZone, except for a small regions "above" and "below" (non overlapping Y axis)
        Cuboid cuboid1 = new Cuboid(0,1,-1,2,0,1); // 1 * 3 * 1
        Cuboid exclusionZone = VOLUME_1_CUBOID;
        assertEquals(3, cuboid1.getVolume());

        List<Cuboid> resultCuboids = cuboid1.regionsNotOverlappingWith(exclusionZone);
        assertEquals(2, resultCuboids.size());
        assertTrue(resultCuboids.contains(
                new Cuboid(0, 1,1, 2, 0, 1)));
        assertTrue(resultCuboids.contains(
                new Cuboid(0,1, -1, 0, 0, 1)));
        assertEquals(2,
                sumVolumes(resultCuboids));
    }

    @Test
    public void getNonOverlappingSpillsToFrontAndBack() {
        // cuboid1 is nearly contained within exclusionZone, except for a small regions "above" and "below" (non overlapping Y axis)
        Cuboid cuboid1 = new Cuboid(0,1,0,1,-1,2); // 1 * 1 * 3
        Cuboid exclusionZone = VOLUME_1_CUBOID;
        assertEquals(3, cuboid1.getVolume());

        List<Cuboid> resultCuboids = cuboid1.regionsNotOverlappingWith(exclusionZone);
        assertEquals(2, resultCuboids.size());
        assertTrue(resultCuboids.contains(
                new Cuboid(0, 1,0,1,1,2)));
        assertTrue(resultCuboids.contains(
                new Cuboid(0, 1,0,1, -1, 0)));
        assertEquals(2, sumVolumes(resultCuboids));
    }

    @Test
    public void getNonOverlappingSpillsToRightAndAbove() {
        // cuboid1 spills to both right and above. The spills are NOT mutually exclusive, but the results must be
        Cuboid cuboid1 = new Cuboid(
                0,2,
                0,2,
                0,1
        ); // 2 * 2 * 1 cuboid
        Cuboid exclusionZone = VOLUME_1_CUBOID;
        assertEquals(4, cuboid1.getVolume());

        List<Cuboid> resultCuboids = cuboid1.regionsNotOverlappingWith(exclusionZone);
        assertEquals(3, sumVolumes(resultCuboids));
        // Check that no 2 cuboids overlap
        List<Cuboid> allCuboids = new ArrayList<>(resultCuboids);
        allCuboids.add(exclusionZone);
        assertCuboidsDontOverlap(allCuboids.toArray(new Cuboid[0]));
    }

    @Test
    public void getNonOverlappingSpillsToLeftAndBelow() {
        // cuboid1 spills to both right and above. The spills are NOT mutually exclusive, but the results must be
        Cuboid cuboid1 = new Cuboid(
                -1,1,
                -1,1,
                0,1
        ); // 2 * 2 * 1 cuboid
        Cuboid exclusionZone = VOLUME_1_CUBOID;
        assertEquals(4, cuboid1.getVolume());

        List<Cuboid> resultCuboids = cuboid1.regionsNotOverlappingWith(exclusionZone);
        assertEquals(3, sumVolumes(resultCuboids));
        // Check that no 2 cuboids overlap
        List<Cuboid> allCuboids = new ArrayList<>(resultCuboids);
        allCuboids.add(exclusionZone);
        assertCuboidsDontOverlap(allCuboids.toArray(new Cuboid[0]));
    }

    @Test
    public void getNonOverlappingSpillsAllAround2DExclusionRegion() {
        // cuboid1 spills to both right and above. The spills are NOT mutually exclusive, but the results must be
        Cuboid cuboid1 = new Cuboid(
                -1,2,
                -1,2,
                0,1
        ); // 3 * 3 * 1 cuboid
        Cuboid exclusionZone = VOLUME_1_CUBOID;
        assertEquals(9, cuboid1.getVolume());

        List<Cuboid> resultCuboids = cuboid1.regionsNotOverlappingWith(exclusionZone);
        assertEquals(8, sumVolumes(resultCuboids));
        // Check that no 2 cuboids overlap
        List<Cuboid> allCuboids = new ArrayList<>(resultCuboids);
        allCuboids.add(exclusionZone);
        assertCuboidsDontOverlap(allCuboids.toArray(new Cuboid[0]));
    }

    @Test
    public void getNonOverlappingSpillsToRightAndFront() {
        // cuboid1 spills to both right and above. The spills are NOT mutually exclusive, but the results must be
        Cuboid cuboid1 = new Cuboid(
                0,2,
                0,1,
                0,2
        );
        Cuboid exclusionZone = VOLUME_1_CUBOID;
        assertEquals(4, cuboid1.getVolume());

        List<Cuboid> resultCuboids = cuboid1.regionsNotOverlappingWith(exclusionZone);
        assertEquals(3, sumVolumes(resultCuboids));
        // Check that no 2 cuboids overlap
        List<Cuboid> allCuboids = new ArrayList<>(resultCuboids);
        allCuboids.add(exclusionZone);
        assertCuboidsDontOverlap(allCuboids.toArray(new Cuboid[0]));
    }

    @Test
    public void getNonOverlappingSpillsToBelowAndBack() {
        // cuboid1 spills to both right and above. The spills are NOT mutually exclusive, but the results must be
        Cuboid cuboid1 = new Cuboid(
                0,1,
                -1,1,
                -1,1
        );
        Cuboid exclusionZone = VOLUME_1_CUBOID;
        assertEquals(4, cuboid1.getVolume());

        List<Cuboid> resultCuboids = cuboid1.regionsNotOverlappingWith(exclusionZone);
        assertEquals(3, sumVolumes(resultCuboids));
        // Check that no 2 cuboids overlap
        List<Cuboid> allCuboids = new ArrayList<>(resultCuboids);
        allCuboids.add(exclusionZone);
        assertCuboidsDontOverlap(allCuboids.toArray(new Cuboid[0]));
    }

    @Test
    public void getNonOverlappingSpillsAllAround3DExclusionRegion() {
        Cuboid cuboid1 = new Cuboid(
                -1,2,
                -1,2,
                -1,2
        ); // 3 * 3 * 1 cuboid
        Cuboid exclusionZone = VOLUME_1_CUBOID;
        assertEquals(27, cuboid1.getVolume());

        List<Cuboid> resultCuboids = cuboid1.regionsNotOverlappingWith(exclusionZone);
        assertEquals(26, sumVolumes(resultCuboids));
        // Check that no 2 cuboids overlap
        List<Cuboid> allCuboids = new ArrayList<>(resultCuboids);
        allCuboids.add(exclusionZone);
        assertCuboidsDontOverlap(allCuboids.toArray(new Cuboid[0]));
    }

    @Test
    public void cuboidGoesTransversallyThroughZOfExcludeRegion() {
        Cuboid cuboid1 = new Cuboid(
                0,1,
                0,1,
                -2,3
        );
        Cuboid exclusionZone = new Cuboid(
                -1,2,
                -1,2,
                -1,2
        );
        assertEquals(5, cuboid1.getVolume());
        assertEquals(27, exclusionZone.getVolume());

        List<Cuboid> resultCuboids = cuboid1.regionsNotOverlappingWith(exclusionZone);
        // Check that no 2 cuboids overlap
        List<Cuboid> allCuboids = new ArrayList<>(resultCuboids);
        allCuboids.add(exclusionZone);
        assertCuboidsDontOverlap(allCuboids.toArray(new Cuboid[0]));

        assertEquals(2, sumVolumes(resultCuboids));
    }

    @Test
    public void cuboidGoesTransversallyThroughYOfExcludeRegion() {
        Cuboid cuboid1 = new Cuboid(
                0,1,
                -2,3,
                0,1
        );
        Cuboid exclusionZone = new Cuboid(
                -1,2,
                -1,2,
                -1,2
        );
        assertEquals(5, cuboid1.getVolume());
        assertEquals(27, exclusionZone.getVolume());

        List<Cuboid> resultCuboids = cuboid1.regionsNotOverlappingWith(exclusionZone);
        // Check that no 2 cuboids overlap
        List<Cuboid> allCuboids = new ArrayList<>(resultCuboids);
        allCuboids.add(exclusionZone);
        assertCuboidsDontOverlap(allCuboids.toArray(new Cuboid[0]));

        assertEquals(2, sumVolumes(resultCuboids));
    }

    @Test
    public void cuboidGoesTransversallyThroughXOfExcludeRegion() {
        Cuboid cuboid1 = new Cuboid(
                -2,3,
                0,1,
                0,1
        );
        Cuboid exclusionZone = new Cuboid(
                -1,2,
                -1,2,
                -1,2
        );
        assertEquals(5, cuboid1.getVolume());
        assertEquals(27, exclusionZone.getVolume());

        List<Cuboid> resultCuboids = cuboid1.regionsNotOverlappingWith(exclusionZone);
        // Check that no 2 cuboids overlap
        List<Cuboid> allCuboids = new ArrayList<>(resultCuboids);
        allCuboids.add(exclusionZone);
        assertCuboidsDontOverlap(allCuboids.toArray(new Cuboid[0]));

        assertEquals(2, sumVolumes(resultCuboids));
    }

    private void assertCuboidsDontOverlap(Cuboid... cuboids) {
        IntStream.range(0, cuboids.length).forEach(idx -> {
            Cuboid current = cuboids[idx];
            IntStream.range(idx+1, cuboids.length).forEach(next -> {
                assertCuboidsDontOverlap(current, cuboids[next]);
            });
        });
    }

    private void assertCuboidsDontOverlap(Cuboid cuboid1, Cuboid cuboid2) {
        assertFalse(cuboid1.doesOverlapWith(cuboid2));
        assertFalse(cuboid2.doesOverlapWith(cuboid1));
    }

    private long sumVolumes(List<Cuboid> resultCuboids) {
        return resultCuboids.stream().mapToLong(Cuboid::getVolume).reduce(0, Long::sum);
    }
}