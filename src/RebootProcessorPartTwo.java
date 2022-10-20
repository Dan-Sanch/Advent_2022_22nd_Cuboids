import java.util.*;

public class RebootProcessorPartTwo implements RebootProcessor {
    private final List<RebootStep> rebootSteps;
    static long totalCuboid = 0;

    public RebootProcessorPartTwo(List<RebootStep> rebootSteps) {
        this.rebootSteps = rebootSteps;
    }

    @Override
    public long getTurnedOnCount() {
        long total = 0;
//        for (int i=rebootSteps.size()-1; i>=0; i--) {
        for (int i=0; i<rebootSteps.size(); i++) {
            total += getVolumeAddedByStep(i);
        }

        System.out.println("Total cuboids: " + totalCuboid);

        return total;
    }

    private long getVolumeAddedByStep(int stepNumber) {
        long subTotal = 0;

        RebootStep step = rebootSteps.get(stepNumber);
        if(!step.isTurnOn())
            // "off" instructions don't contribute to volume, they're only good to constrain the "on" steps
            return subTotal;

        Collection<Cuboid> applicableRegions =
            getNonOverriddenRegions(step.cuboid(), getOverridingStepsCuboids(stepNumber)
        );

        totalCuboid += applicableRegions.size();

        return sumVolumes(applicableRegions);
    }

    private Iterable<Cuboid> getOverridingStepsCuboids(int stepNumber) {
        return rebootSteps
                .subList(stepNumber + 1, rebootSteps.size())
                .stream().map(RebootStep::cuboid)
                .toList()
            ;
    }

    private List<Cuboid> getNonOverriddenRegions(Cuboid initialRegion, Iterable<Cuboid> overrideRegions) {
        List<Cuboid> nonOverriddenRegions = new ArrayList<>();
        nonOverriddenRegions.add(initialRegion);

        for (Cuboid override : overrideRegions) {
            nonOverriddenRegions = getRegionsOutsideOverride(nonOverriddenRegions, override);
            if (nonOverriddenRegions.isEmpty())
                break;
        }

        return nonOverriddenRegions;
    }

    private List<Cuboid> getRegionsOutsideOverride(Collection<Cuboid> regionsToOverride, Cuboid overrideRegion) {
        List<Cuboid> nonOverridden = new ArrayList<>();

        regionsToOverride.stream()
                .map(region -> region.regionsNotOverlappingWith(overrideRegion))
                .forEach(nonOverridden::addAll);

        return nonOverridden;
    }

    private long sumVolumes(Collection<Cuboid> cuboids) {
        return cuboids.stream()
                .mapToLong(Cuboid::getVolume)
                .reduce(0, Long::sum);
    }
}
