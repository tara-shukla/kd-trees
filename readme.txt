Programming Assignment 5: K-d Trees

/* *****************************************************************************
 *  First, fill out the mid-semester survey:
 *  https://forms.gle/LdhX4bGvaBYYYXs97
 *
 *  If you're working with a partner, please do this separately.
 *
 *  Type your initials below to confirm that you've completed the survey.
 **************************************************************************** */

TS NK

/* *****************************************************************************
 *  Describe the Node data type you used to implement the
 *  2d-tree data structure.
 **************************************************************************** */

Each node is associated with a key (Point2D var) and a value, and also with a
left/bottom and right/top node, and a rectangle for its bounding box.


/* *****************************************************************************
 *  Describe your method for range search in a k-d tree.
 **************************************************************************** */

In the method, we call a helper method with the root, the range, and the queue.
 This helper method takes a node, a range, and a queue, and queues the node
 if the node is in the range. It recursively calls itself for the node's left
 and right nodes so it traverses the whole tree until reaching null leaves.


/* *****************************************************************************
 *  Describe your method for nearest neighbor search in a k-d tree.
 **************************************************************************** */

In the method, we call a helper method with the root, the target, the root's
point (as an initial nearest neighbor) and the root's vertical/horizontal
boolean. This helper method will update the nearest neighbor if the node's point
is closer to the target. Then, it will choose the left/right subtree of the
node depending on which side the target point would fall on. The method uses a
reference to the closer subtree to make this choice. It traverses that tree and
updates whenever it finds a closer point. Then it traverses the
 further one (allowing more pruning) and finally it returns the closest
 point found.

/* *****************************************************************************
 *  How many nearest-neighbor calculations can your PointST implementation
 *  perform per second for input1M.txt (1 million points), where the query
 *  points are random points in the unit square?
 *
 *  Fill in the table below, rounding each value to use one digit after
 *  the decimal point. Use at least 1 second of CPU time. Do not use -Xint.
 *  (Do not count the time to read the points or to build the 2d-tree.)
 *
 *  Repeat the same question but with your KdTreeST implementation.
 *
 **************************************************************************** */


                 # calls to         /   CPU time     =   # calls to nearest()
                 client nearest()       (seconds)        per second
                ------------------------------------------------------
PointST:         100         /         10.688735   =      9.4 calls/sec

KdTreeST:       10000000          /        36.903403  =    270977.7 calls/sec

Note: more calls per second indicates better performance.


/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */
n/a

/* *****************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 **************************************************************************** */

Morgan office hours

/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */

n/a
/* *****************************************************************************
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 **************************************************************************** */

We partner coded the whole assignment, each of us coding while the other
debugged. We also did pseudocode together on a shared doc.


/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on  how helpful the class meeting was and on how much you learned
 * from doing the assignment, and whether you enjoyed doing it.
 **************************************************************************** */
n/a
