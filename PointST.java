import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StopwatchCPU;

public class PointST<Value> {
    private int n; // number of points
    private RedBlackBST<Point2D, Value> points; // symbol table of points

    // construct an empty symbol table of points
    public PointST() {
        n = 0;
        points = new RedBlackBST<Point2D, Value>();
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points
    public int size() {
        return n;
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null) {
            throw new IllegalArgumentException("Calls put() with null point");
        }
        if (val == null) {
            throw new IllegalArgumentException("Calls put() with null val");
        }

        // check for duplicate keys
        if (!points.contains(p)) {
            n++;
        }
        points.put(p, val);

    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Calls get() with null point");
        }
        return points.get(p);
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Calls contains() with null point");
        }
        return points.contains(p);
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        return points.keys();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Calls range() with null rect");
        }
        Stack<Point2D> rangePoints = new Stack<Point2D>();

        // add points inside rectangle to the stack
        for (Point2D p : points()) {
            if (rect.contains(p)) {
                rangePoints.push(p);
            }
        }
        return rangePoints;

    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Calls nearest() with null point");
        }
        if (n == 0) {
            return null;
        }

        // set initial distance to be the greatest possible value
        double shortestDistance = Double.POSITIVE_INFINITY;

        // nearest point to be updated
        Point2D neighbour = null;

        for (Point2D a : points()) {
            double distance = p.distanceSquaredTo(a);

            if (distance < shortestDistance) {
                shortestDistance = distance; // update shortest distance
                neighbour = a; // update current point to be the nearest neighbour
            }
        }
        return neighbour;

    }

    // unit testing (required)
    public static void main(String[] args) {
        // timer calculation for readme
        // read file from args
        PointST<Integer> timeTest = new PointST<Integer>();
        In testFile = new In(args[0]);
        while (!testFile.isEmpty()) {
            // read point
            Point2D point = new Point2D(testFile.readDouble(), testFile.readDouble());
            // put point from file, random val
            timeTest.put(point, StdRandom.uniformInt(5));
        }


        Point2D nearest;
        int m = 10;
        StopwatchCPU timer = new StopwatchCPU();
        for (int i = 0; i <= m; i++) {
            Point2D randomTarget = new Point2D(StdRandom.uniformDouble(0.0, 1.0),
                                               StdRandom.uniformDouble(0.0, 1.0));
            nearest = timeTest.nearest(randomTarget);
        }
        StdOut.println("time elapsed: " + timer.elapsedTime());

        PointST<Integer> test = new PointST<Integer>();

        Point2D point1 = new Point2D(0, 0);
        Point2D point2 = new Point2D(0.1, 0.4);
        Point2D point3 = new Point2D(0.6, 0.5);
        Point2D point4 = new Point2D(0.8, 0.6);
        RectHV rect1 = new RectHV(0.4, 0.3, 0.8, 0.6);

        test.put(point1, 1);
        test.put(point2, 2);
        test.put(point3, 3);
        test.put(point3, 5);

        StdOut.println(test.isEmpty()); // false
        StdOut.println(test.size()); // 3

        StdOut.println(test.contains(point2)); // true
        StdOut.println(test.contains(point4)); // false

        StdOut.println(test.get(point1)); // value = 1

        StdOut.println(test.points()); // point1, point2, point3
        StdOut.println(test.range(rect1)); // point3
        StdOut.println(test.nearest(point4)); // point3

    }

}
