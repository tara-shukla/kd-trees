import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StopwatchCPU;

public class KdTreeST<Value> {
    private int n; // number of points (size)
    private Node root; // top of the tree

    private class Node {
        private Point2D key; // key
        private Value val; // associated value
        private Node left; // link to left/bottom subtree
        private Node right; // link to right/top subtree
        private RectHV rect; // bounding box

        // Initializes a new node based on the parameters, with null links
        public Node(Point2D key, Value val, RectHV rect) {
            this.key = key;
            this.val = val;
            this.rect = rect;

            this.left = null;
            this.right = null;
        }

        // compares x or y value depending on node orientation
        public double compareTo(Point2D that, boolean isVertical) {
            if (isVertical) { // vertical nodes compare x-coordinate
                return that.x() - this.key.x();
            }
            else { // horizontal nodes compare y-coordinates
                return that.y() - this.key.y();
            }
        }
    }

    // construct an empty symbol table of points
    public KdTreeST() {
        n = 0;
        root = null;
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return (n == 0);
    }

    // number of points
    public int size() {
        return this.n;
    }

    // helper function to decide where to place the new node in the ST
    private Node put(Node current, Point2D p, Value val, RectHV rect,
                     boolean isVertical) {
        if (current == null) {
            n++; // update number of points in the tree
            return new Node(p, val, rect);
        }
        // compare point p with the point at the root
        double comp = current.compareTo(p, isVertical);

        // bounding box dimensions
        double xMin = rect.xmin();
        double xMax = rect.xmax();
        double yMin = rect.ymin();
        double yMax = rect.ymax();

        if (current.key.equals(p)) {
            current.val = val;
        }

        else if (comp < 0) {
            if (!isVertical) {
                // update yMin
                double y = current.key.y();
                rect = new RectHV(xMin, yMin, xMax, y);
            }

            else {
                // update xMin
                double x = current.key.x();
                rect = new RectHV(xMin, yMin, x, yMax);
            }

            current.left = put(current.left, p, val, rect, !isVertical);
        }
        else {
            if (!isVertical) {
                // update yMin
                double y = current.key.y();
                rect = new RectHV(xMin, y, xMax, yMax);
            }

            else {
                // update xMin
                double x = current.key.x();
                rect = new RectHV(x, yMin, xMax, yMax);
            }

            current.right = put(current.right, p, val, rect, !isVertical);
        }

        return current;
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null) {
            throw new IllegalArgumentException("Calls put() with null key");
        }
        if (val == null) {
            throw new IllegalArgumentException("calls put() with null val");
        }
        // axis-aligned rectangle of the root (entire plane)
        double posInf = Double.POSITIVE_INFINITY;
        double negInf = Double.NEGATIVE_INFINITY;
        RectHV bound = new RectHV(negInf, negInf, posInf, posInf);


        root = put(root, p, val, bound, true);
    }

    // traverses tree to find the value of the given point
    private Value get(Node current, Point2D p, boolean vert) {

        if (current == null || p == null) {
            throw new IllegalArgumentException("null argument");
        }
        double comp = current.compareTo(p, vert);

        // if found the key, return value
        if (current.key.x() == p.x() && current.key.y() == p.y()) {
            return current.val;
        }

        // recursively travel left and right to get p
        else if (comp < 0) {
            if (current.left != null) {
                return get(current.left, p, !vert);
            }
            else {
                return null;
            }

        }
        else {
            if (current.right != null) {
                return get(current.right, p, !vert);
            }
            else {
                return null;
            }
        }
    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Calls get() with null key");
        }

        if (root == null) return null;
        return (get(root, p, true));
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Argument to contains() is null");
        }
        return (get(p) != null);
    }

    // all points in the symbol table (adapted from Princeton LevelOrder.java)
    public Iterable<Point2D> points() {
        Queue<Point2D> allPoints = new Queue<Point2D>();
        Queue<Node> allNodes = new Queue<Node>();
        allNodes.enqueue(root);

        // add points to the queue
        while (!allNodes.isEmpty()) {
            Node currNode = allNodes.dequeue();
            if (currNode == null) continue;

            allPoints.enqueue(currNode.key); // enqueue the current point

            // add left and right node to node queue in that order
            allNodes.enqueue(currNode.left);
            allNodes.enqueue(currNode.right);
        }

        return allPoints;
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("rect can't be null");
        }
        Queue<Point2D> q = new Queue<Point2D>();
        findInRange(root, rect, q);
        return q;
    }

    // traverses the tree and updates the points that the rectangle contains
    private void findInRange(Node node, RectHV rect, Queue<Point2D> q) {
        if (node != null && node.rect.intersects(rect)) {
            if (rect.contains(node.key)) {
                q.enqueue(node.key);
            }
            findInRange(node.left, rect, q);
            findInRange(node.right, rect, q);
        }
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        // check for null arg
        if (p == null) {
            throw new IllegalArgumentException("Calls nearest() with null point.");
        }

        // check for when the tree is empty
        if (root == null) {
            return null;
        }
        // call recursive function findNearest() to traverse the tree
        Point2D pNear = findNearest(root, p, root.key, true);
        return pNear;
    }

    // recursively explore tree for point with the shortest distance
    private Point2D findNearest(Node node, Point2D p, Point2D closest, boolean vert) {
        // if node is null return closest
        if (node == null) {
            return closest;
        }

        // find current closest distance to point
        double shortest = closest.distanceSquaredTo(p);
        // find distance from current node to point
        double distance = node.key.distanceSquaredTo(p);
        // if this node is closer, make its point closest
        if (distance < shortest) {
            closest = node.key;
            shortest = distance;
        }

        // create ref to the subtree whose division the target point falls under
        Node closerTree;
        Node fartherTree;
        if (node.compareTo(p, vert) >= 0) {
            closerTree = node.right;
            fartherTree = node.left;
        }
        else {
            closerTree = node.left;
            fartherTree = node.right;
        }
        /*

        if (closerTree == null) {
            return closest;
        }

         */

        if (closerTree != null) {
            // prune the tree if it is impossible for the subtree to produce
            // a closer point
            if (closerTree.rect.distanceSquaredTo(p) <= shortest) {
                // recursively find the closer point by traversing the subtrees
                closest = findNearest(closerTree, p, closest, !vert);
                shortest = p.distanceSquaredTo(closest);

            }

        }


        if (fartherTree != null) {
            if (fartherTree.rect.distanceSquaredTo(p) <= shortest) {
                closest = findNearest(fartherTree, p, closest, !vert);
                // shortest = p.distanceSquaredTo(closest);
            }
        }

        return closest;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // timer calculation for readme
        // read file from args
        KdTreeST<Integer> timeTest = new KdTreeST<Integer>();
        In testFile = new In(args[0]);
        while (!testFile.isEmpty()) {
            // read point
            Point2D point = new Point2D(testFile.readDouble(), testFile.readDouble());
            // put point from file, random val
            timeTest.put(point, StdRandom.uniformInt(5));
        }


        Point2D nearest;
        int m = 10000000;
        StopwatchCPU timer = new StopwatchCPU();
        for (int i = 0; i <= m; i++) {
            Point2D randomTarget = new Point2D(StdRandom.uniformDouble(0.0, 1.0),
                                               StdRandom.uniformDouble(0.0, 1.0));
            nearest = timeTest.nearest(randomTarget);
        }
        StdOut.println("time elapsed: " + timer.elapsedTime());

        // unit testing
        KdTreeST<Integer> test = new KdTreeST<Integer>();
        Point2D point1 = new Point2D(0.0, 0.75);
        Point2D point2 = new Point2D(0.125, 1.0);
        Point2D point3 = new Point2D(0.5, 0.0);
        Point2D point4 = new Point2D(0.25, 0.875);
        Point2D point5 = new Point2D(0.9, 0.6);

        RectHV rect1 = new RectHV(0.2, 0.2, 0.7, 0.4);

        test.put(point1, 2);
        test.put(point2, 4);
        test.put(point3, 4);
        test.put(point4, 5);

        StdOut.println(test.get(point1)); // 2
        StdOut.println(test.contains(point3)); // true

        KdTreeST<Integer> test2 = new KdTreeST<Integer>();
        StdOut.println(test2.get(point5));
        StdOut.println(test2.contains(point5));

        StdOut.println(test.isEmpty()); // false
        StdOut.println(test.size()); // 4

        StdOut.println(test.points());

        StdOut.println(test.range(rect1)); // point1, point2, point3

        StdOut.println(test.nearest(point4)); // point4
        StdOut.println(test.nearest(point5)); // point2

    }

}
