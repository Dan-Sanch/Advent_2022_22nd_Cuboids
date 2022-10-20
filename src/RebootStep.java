
public record RebootStep(
        boolean isTurnOn,
        Cuboid cuboid
)
{
    @Override
    public String toString() {
        String onOffString = isTurnOn ?"ON":"OFF";
        return
                onOffString+ "; "
                + "[" + cuboid.left + "," + cuboid.right + "] "
                + "[" + cuboid.bottom + "," + cuboid.top + "] "
                + "[" + cuboid.back + "," + cuboid.front + "] "
        ;
    }
}
