package _2022._15;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * Advent of Code <a href="https://adventofcode.com/2022/day/15">Day 15: Beacon Exclusion Zone</a>
 */
public class BeaconExclusionZone {
    static final String FILE = "src/main/resources/_2022/_15/input.txt";

    static int findOpenPositions(List<SensorData> data) {
        return getCoverage(data, 2000000, Integer.MIN_VALUE, Integer.MAX_VALUE, true).stream()
                .mapToInt(i -> i.right - i.left + 1)
                .reduce(Integer::sum).orElseThrow();
    }

    static long findTuningFrequency(List<SensorData> data) {
        return IntStream.rangeClosed(0, 4_000_000).parallel().mapToLong(y -> {
            Interval i = getCoverage(data, y, 0, 4_000_000, false).get(0);
            if (i.left == 0 && i.right == 4_000_000) return 0;
            long x = i.left != 0 ? i.left : i.right + 1;
            return x * 4_000_000L + y;
        }).filter(f -> f > 0).findAny().orElseThrow();
    }

    static List<Interval> getCoverage(List<SensorData> dataList, int row, int floor, int ceiling, boolean removeHardwarePoints) {
        Set<Integer> hardwarePoints = new HashSet<>();
        TreeMap<Interval, Interval> intervalTreeMap = new TreeMap<>((a, b) -> {
            if (a.left > b.right) return 1;
            else if (a.right < b.left) return -1;
            else return 0;
        });

        for (SensorData data : dataList) {
            Point sensor = data.sensor, beacon = data.beacon;

            if (sensor.y == row) hardwarePoints.add(sensor.x);
            if (beacon.y == row) hardwarePoints.add(beacon.x);

            int beaconDist = Math.abs(sensor.x - beacon.x) + Math.abs(sensor.y - beacon.y);
            int rowDist = Math.abs(row - sensor.y);

            if (rowDist <= beaconDist) {
                int left = sensor.x - (beaconDist - rowDist), right = sensor.x + (beaconDist - rowDist);
                if (right < floor || left > ceiling) continue;
                left = Math.max(left, floor);
                right = Math.min(right, ceiling);

                addInterval(intervalTreeMap, left, right);
            }
        }
        if (removeHardwarePoints)
            hardwarePoints.forEach(x -> removeInterval(intervalTreeMap, x, x));

        return intervalTreeMap.keySet().stream().toList();
    }

    static void addInterval(TreeMap<Interval, Interval> map, int left, int right) {
        Interval i = new Interval(left, right);
        for (Interval e = map.remove(i); e != null; e = map.remove(i)) {
            left = Math.min(e.left, left);
            right = Math.max(e.right, right);
        }
        i = new Interval(left, right);
        map.put(i, i);
    }

    static void removeInterval(TreeMap<Interval, Interval> map, int left, int right) {
        Interval r = new Interval(left, right);
        for (Interval e = map.remove(r); e != null; e = map.remove(r)) {
            if (e.left == left && e.right == right) return;
            if (e.right >= left) {
                Interval i = new Interval(e.left, right - 1);
                map.put(i, i);
            }
            if (e.left <= right) {
                Interval i = new Interval(left + 1, e.right);
                map.put(i, i);
            }
        }
    }

    static List<SensorData> getSensorData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            return reader.lines().map(s -> {
                Matcher matcher = sensorDataPattern.matcher(s);
                if (matcher.find())
                    return new SensorData(
                            new Point(Integer.parseInt(matcher.group("sensorX")),
                                    Integer.parseInt(matcher.group("sensorY"))),
                            new Point(Integer.parseInt(matcher.group("beaconX")),
                                    Integer.parseInt(matcher.group("beaconY")))
                    );
                throw new RuntimeException("Unable to parse sensor data \"%s\"".formatted(s));
            }).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    record SensorData(Point sensor, Point beacon) {
    }

    record Point(int x, int y) {
    }

    record Interval(int left, int right) {
    }

    static final Pattern sensorDataPattern = Pattern.compile("Sensor at x=(?<sensorX>-?\\d+), y=(?<sensorY>-?\\d+): closest beacon is at x=(?<beaconX>-?\\d+), y=(?<beaconY>-?\\d+)");

    public static void main(String[] args) {
        List<SensorData> data = getSensorData();
        System.out.printf("Part One: %s%n", findOpenPositions(data));
        System.out.printf("Part Two: %s%n", findTuningFrequency(data));
    }
}
