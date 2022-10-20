import utils.FileReaderTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static final String INPUT_FILE_NAME = "resources\\reboot steps.txt";

    public static void main(String[] args) {
        String[] linesArray = FileReaderTools.readFileAsArray(INPUT_FILE_NAME);

        long turnedOnCubes = partOne(parseRebootSteps(linesArray));
        printLine("Part One - Total On Cubes: %d", turnedOnCubes);

        long turnedOnCubesNext = partTwo(parseRebootStepsTwo(linesArray));
        printLine("Part Two - Total On Cubes: %d", turnedOnCubesNext);
    }

    private static long partOne(List<RebootStep> rebootSteps) {
        RebootProcessor reboot = new RebootProcessorPartOne(rebootSteps);
        return reboot.getTurnedOnCount();
    }

    private static long partTwo(List<RebootStep> rebootSteps) {
        RebootProcessor reboot = new RebootProcessorPartTwo(rebootSteps);
        return reboot.getTurnedOnCount();
    }

    private static List<RebootStep> parseRebootSteps(String[] inputLines) {
        List<RebootStep> resultSteps = new ArrayList<>(inputLines.length);
        Arrays.stream(inputLines).forEach(
                line -> resultSteps.add(parseRebootStep(line))
        );
        return resultSteps;
    }

    private static RebootStep parseRebootStep(String parseLine) {
        String[] spaceSplit = parseLine.split(" ");
        String[] axisTokens = spaceSplit[1].split(",");

        List<Integer> axisValues = new ArrayList<>(6);
        Arrays.stream(axisTokens)
                .forEach(axisToken ->
                        Arrays.stream(axisToken.substring(2)
                                .split("\\.\\."))
                                .forEach(axisValue ->
                                        axisValues.add(Integer.valueOf(axisValue))
                                )
                );

        boolean turnOn = spaceSplit[0].equals("on");

        return new RebootStep(turnOn, new Cuboid(
                // Input has each range being INCLUSIVE of each axis' upper value, while Cuboid was coded to be EXCLUSIVE,
                // so we have to compensate here
                axisValues.get(0), axisValues.get(1),
                axisValues.get(2), axisValues.get(3),
                axisValues.get(4), axisValues.get(5)
        ));
    }

    private static List<RebootStep> parseRebootStepsTwo(String[] inputLines) {
        List<RebootStep> resultSteps = new ArrayList<>(inputLines.length);
        Arrays.stream(inputLines).forEach(
                line -> resultSteps.add(parseRebootStepTwo(line))
        );
        return resultSteps;
    }

    private static RebootStep parseRebootStepTwo(String parseLine) {
        String[] spaceSplit = parseLine.split(" ");
        String[] axisTokens = spaceSplit[1].split(",");

        List<Integer> axisValues = new ArrayList<>(6);
        Arrays.stream(axisTokens)
                .forEach(axisToken ->
                        Arrays.stream(axisToken.substring(2)
                                        .split("\\.\\."))
                                .forEach(axisValue ->
                                        axisValues.add(Integer.valueOf(axisValue))
                                )
                );

        boolean turnOn = spaceSplit[0].equals("on");

        return new RebootStep(turnOn, new Cuboid(
                // Input has each range being INCLUSIVE of each axis' upper value, while Cuboid was coded to be EXCLUSIVE,
                // so we have to compensate here
                axisValues.get(0), axisValues.get(1)+1,
                axisValues.get(2), axisValues.get(3)+1,
                axisValues.get(4), axisValues.get(5)+1
        ));
    }


    // ****** Utility code ********* //
    private static int dbg_indentationLevel = 0;
    private static void printLine(String formatString, Object... args) {
        String indentation = "\t".repeat(dbg_indentationLevel);
        System.out.printf(indentation+formatString+"\n", args);
    }
}
