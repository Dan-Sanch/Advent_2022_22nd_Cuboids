import java.util.*;
import java.util.stream.LongStream;

public class RebootProcessorPartOne implements RebootProcessor {
    private final List<RebootStep> rebootSteps;
    private Map<String, Boolean> individualPointsStatus;

    public RebootProcessorPartOne(List<RebootStep> rebootSteps) {
        this.rebootSteps = rebootSteps;
    }

    @Override
    public long getTurnedOnCount() {
        return getPointsMap().values().stream().filter(isTurnedOn -> isTurnedOn).count();
    }

    private Map<String, Boolean> getPointsMap() {
        if (individualPointsStatus == null) {
            individualPointsStatus = runRebootSteps();
        }

        return individualPointsStatus;
    }

    private Map<String, Boolean> runRebootSteps() {
        Map<String, Boolean> points = new HashMap<>();
        rebootSteps.forEach(step -> {
                    if (isWithinRange(step.cuboid())) {
                        updatePointsState(step, points);
                    }
                }
        );

        return points;
    }

    private boolean isWithinRange(Cuboid region) {
        return
                region.left >= -50
                && region.bottom >= -50
                && region.back >= -50
                && region.right <= 50
                && region.top <= 50
                && region.front <= 50
                ;
    }

    private void updatePointsState(RebootStep step, Map<String, Boolean> currentState) {
        Cuboid cuboid = step.cuboid();
        LongStream.rangeClosed(cuboid.left, cuboid.right).forEach(x ->
                LongStream.rangeClosed(cuboid.bottom, cuboid.top).forEach(y ->
                        LongStream.rangeClosed(cuboid.back, cuboid.front).forEach( z -> {
                                currentState.put(x + "," + y + "," + z, step.isTurnOn());
                            }
                        )
                )
        );
    }
}
