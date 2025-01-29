
// TODO: delete improts
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * FibonacciHeap
 *
 * An implementation of Fibonacci heap over positive integers.
 *
 */
public class FibonacciHeap {

    public static void main(String[] args) {
        FibonacciHeap heap1;
        FibonacciHeap heap2;
        FibonacciHeap heap3;

        ArrayList<Integer> order;

        int[][] details = new int[5][3]; // Rows are {time, size, links, cuts, trees}, and the columns are the different tests.

        StringBuilder test1 = new StringBuilder();
        test1.append("TEST 1:\n").append(String.format("%-10s %-15s %-15s %-15s %-15s %-15s%n",
                "i", "Runtime", "Heap Size", "Links", "Cuts", "Trees"));
        StringBuilder test2 = new StringBuilder();
        test2.append("TEST 2:\n").append(String.format("%-10s %-15s %-15s %-15s %-15s %-15s%n",
                "i", "Runtime", "Heap Size", "Links", "Cuts", "Trees"));
        StringBuilder test3 = new StringBuilder();
        test3.append("TEST 3:\n").append(String.format("%-10s %-15s %-15s %-15s %-15s %-15s%n",
                "i", "Runtime", "Heap Size", "Links", "Cuts", "Trees"));

        for (int i = 1; i <= 5; i++) { // 5 different sizes
            int n = (int) Math.pow(3, i + 7) - 1;

            for (int row[] : details) { // Initializing matrix
                for (int k = 0; k < 3; k++) {
                    row[k] = 0;
                }
            }

            for (int j = 1; j <= 20; j++) { // avaraging out over 20 identical experiments
                heap1 = new FibonacciHeap();
                heap2 = new FibonacciHeap();
                heap3 = new FibonacciHeap();

                order = new ArrayList<>();
                for (int k = 1; k <= n; k++) {
                    order.add(k);
                }

                Collections.shuffle(order);

                // TEST 1
                long startTime = System.currentTimeMillis();
                for (int item : order) {
                    heap1.insert(item, "key " + item);
                }
                heap1.deleteMin();
                long totalTime = System.currentTimeMillis() - startTime;
                details[0][0] += totalTime;
                details[1][0] += heap1.size();
                details[2][0] += heap1.totalLinks();
                details[3][0] += heap1.totalCuts();
                details[4][0] += heap1.numTrees();

                // TEST 2
                startTime = System.currentTimeMillis();
                for (int item : order) {
                    heap2.insert(item, "key " + item);
                }
                for (int k = 0; k <= n / 2; k++) {
                    heap2.deleteMin();
                }
                totalTime = System.currentTimeMillis() - startTime;
                details[0][1] += totalTime;
                details[1][1] += heap2.size();
                details[2][1] += heap2.totalLinks();
                details[3][1] += heap2.totalCuts();
                details[4][1] += heap2.numTrees();

                // TEST 3
                startTime = System.currentTimeMillis();
                HeapNode[] pointers = new HeapNode[n];
                for (int item : order) {
                    pointers[item - 1] = heap3.insert(item, "key " + item);
                }
                heap3.deleteMin();
                for (int k = n - 1; k > 31; k--) {
                    heap3.delete(pointers[k]);
                }
                totalTime = System.currentTimeMillis() - startTime;
                details[0][2] += totalTime;
                details[1][2] += heap3.size();
                details[2][2] += heap3.totalLinks();
                details[3][2] += heap3.totalCuts();
                details[4][2] += heap3.numTrees();
            }

            test1.append(String.format(
                    "%-10d %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f%n",
                    i,
                    details[0][0] / 20.0,
                    details[1][0] / 20.0,
                    details[2][0] / 20.0,
                    details[3][0] / 20.0,
                    details[4][0] / 20.0
            ));
            test2.append(String.format(
                    "%-10d %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f%n",
                    i,
                    details[0][1] / 20.0,
                    details[1][1] / 20.0,
                    details[2][1] / 20.0,
                    details[3][1] / 20.0,
                    details[4][1] / 20.0
            ));
            test3.append(String.format(
                    "%-10d %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f%n",
                    i,
                    details[0][2] / 20.0,
                    details[1][2] / 20.0,
                    details[2][2] / 20.0,
                    details[3][2] / 20.0,
                    details[4][2] / 20.0
            ));

        }

        System.out.println("\n" + test1);
        System.out.println("\n" + test2);
        System.out.println("\n" + test3);

    }

    public HeapNode min;
    public int size; //new
    public int totalLinks;//new
    public int totalCuts;//new
    public int numTrees;//new

    /**
     *
     * Constructor to initialize an empty heap.
     *
     */
    public FibonacciHeap() // Shalev- when creating Fibo heap, it will be empty, than we will use the insert methods to insert nodes.
    {
        this.min = null;
        this.size = 0;
        this.totalLinks = 0;
        this.totalCuts = 0;
        this.numTrees = 0;
    }

    /**
     *
     * pre: key > 0
     *
     * Insert (key,info) into the heap and return the newly generated HeapNode.
     *
     */
    public HeapNode insert(int key, String info) { // Shalev-Works.
        HeapNode x = new HeapNode();
        x.key = key;
        x.info = info;
        x.rank = 0;
        x.mark = false;

        // Now, if the size of our tree is 0, we have an empty tree so x will point to himself, the size is 1, his min his the the new node
        if (this.size() == 0) {

            x.next = x;
            x.prev = x;
            this.min = x;

        } else { // if we already have a tree that is not empty

            x.next = this.min;
            x.prev = this.min.prev;
            this.min.prev.next = x;
            this.min.prev = x;

            if (x.key < this.min.key) {
                this.min = x;
            }
        }
        this.size += 1;
        this.numTrees += 1;

        return x;
    }

    /**
     *
     * Return the minimal HeapNode, null if empty.
     *
     */
    public HeapNode findMin() //Shalev
    {
        return this.min;
    }

    /**
     *
     * Delete the minimal item
     *
     */
    private void consolidate(HeapNode node) {
        //gather current roots into a list and remove them from the ring
        List<HeapNode> rootList = new ArrayList<>();
        {
            HeapNode current = node;
            boolean done = false;
            while (!done) {
                rootList.add(current);
                current = current.next;
                if (current == node) {
                    done = true;
                }
            }
        }

        //create array A for merging roots of equal rank
        int arraySize = (int) (Math.floor(Math.log(this.size) / Math.log(2))) + 2;
        HeapNode[] A = new HeapNode[arraySize];
        Arrays.fill(A, null);

        //merge roots with the same rank
        for (HeapNode w : rootList) {
            //remove w from circular list so we can re-link it from scratch
            w.prev.next = w.next;
            w.next.prev = w.prev;
            w.parent = null;
            w.next = w;
            w.prev = w;

            int d = w.rank;
            HeapNode x = w;
            while (A[d] != null) {
                HeapNode y = A[d];
                // make sure x is the smaller-key node
                if (y.key < x.key) {
                    HeapNode temp = x;
                    x = y;
                    y = temp;
                }
                // link bigger key y under x
                link(y, x);
                A[d] = null;
                d++;
            }
            A[d] = x;
        }

        // rebuild the root list and find new min
        this.min = null;
        this.numTrees = 0; // re-count numTrees
        for (HeapNode A1 : A) {
            if (A1 != null) {
                // If we have no current min, A[i] becomes min
                if (this.min == null) {
                    this.min = A1;
                    A1.prev = A1;
                    A1.next = A1;
                } else {
                    // insert A[i] into the min's circular root list
                    A1.prev = this.min;
                    A1.next = this.min.next;
                    this.min.next.prev = A1;
                    this.min.next = A1;
                    // update min pointer if neccessary
                    if (A1.key < this.min.key) {
                        this.min = A1;
                    }
                }
                this.numTrees++;
            }
        }
    }

    /**
     * Moves all children of node z into the heap's root list, making them new
     * roots. Assumes z.child is non-null.
     */
    private void addChildrenToRootList(HeapNode z) { //Shalev- works Important -In the while loop we relay on the rank for the number of iteration so we need to maintain this filed correctly.

        // z is a root
        // If z has no children, nothing to do
        if (z.child == null) {
            return;
        }

        // iterate over the entire child ring exactly once
        int counter = 0;
        HeapNode startChild = z.child;
        HeapNode current = startChild;
        boolean done = false;
        while (!done) {
            // Store next sibling before we alter pointers
            HeapNode nextC = current.next;

            // 1. Remove 'current' from child ring
            current.prev.next = current.next;
            current.next.prev = current.prev;

            // 2. Make 'current' a new root by splicing it into the circular list headed by this.min
            current.parent = null;
            current.next = this.min;
            current.prev = this.min.prev;
            this.min.prev.next = current;
            this.min.prev = current;

            // Move on to the next sibling 
            current = nextC;
            // Stop if we've looped back to the start
            counter++;
            if (counter == z.rank) {
                done = true;
            }
        }

        // z no longer has children
        z.child = null;
    }

    private void link(HeapNode y, HeapNode x) {
        this.totalLinks++;
        this.numTrees--;
        if (y.key < x.key) {
            link_help(x, y);
        } else {
            link_help(y, x);
        }

    }

    private void link_help(HeapNode y, HeapNode x) {// Making y the child of X, here we assume x and y has the same rank. 
        // Here we make sure the other nodes in the root list will not point to y.
        y.prev.next = y.next;
        y.next.prev = y.prev;

        // Make y a child of x.
        y.parent = x;

        // if x has no child yet
        if (x.child == null) {
            x.child = y;
            y.next = y;
            y.prev = y;
        } else {
            // insert y to the child list of x.
            y.next = x.child;
            y.prev = x.child.prev;
            x.child.prev.next = y;
            x.child.prev = y;
        }

        x.rank++;
        y.mark = false;
    }

    public void deleteMin() { // maybe test it, idk
        // If heap is empty, nothing to delete
        if (this.min == null) {
            return;
        }

        // We'll call the current min 'z'
        HeapNode z = this.min;
        int rank = z.rank;

        // If z has children, promote them all to the root list
        if (z.child != null) {
            // addChildrenToRootList deals with the pointers:
            //      Each child => parent's pointer becomes null
            //      Moves each child into the top-level ring
            this.addChildrenToRootList(z);
            // Clear z's child pointer now that they're top-level roots
            z.child = null;
        }

        // Remove z from the root list
        z.prev.next = z.next;
        z.next.prev = z.prev;

        // if z was the only node in the root ring, set min to null
        //    else pick z.next as a new 'min' placeholder
        if (z == z.next) {
            this.min = null;
            this.numTrees = 0;
        } else {
            this.min = z.next;
        }

        // Decrease the heap size by 1
        this.size--;
        this.totalCuts += rank;

        // If the heap is not empty, run 'consolidate' to merge roots and update min
        if (this.min != null) {
            this.consolidate(this.min);
        }
        // else, the heap is empty => min is null, numTrees=0
    }

    /**
     *
     * pre: 0<diff<x.key
     *
     * Decrease the key of x by diff and fix the heap.
     *
     */
    public void decreaseKey(HeapNode x, int diff) { //Yoad    
        if (x == null || diff < 0 || diff > x.key) {
            return;
        }
        x.key = x.key - diff;
        HeapNode y = x.parent;
        if (y != null && x.key < y.key) {
            cut(x);
            cascadingCut(y);
        }
        if (x.key < this.min.key) {
            this.min = x;
        }
    }

    private void cut(HeapNode x) { //Yoad. assumes x is not a root.
        this.totalCuts++;
        this.numTrees++;
        // Remove x from the child list of its parent
        if (x.next == x) {
            x.parent.child = null;
        } else {
            x.next.prev = x.prev;
            x.prev.next = x.next;
            x.parent.child = x.next;
        }
        x.parent.rank--;
        x.parent = null;
        x.prev = x;
        x.next = x;
        x.mark = false;
        // Add x to the root list
        x.prev = this.min;
        x.next = this.min.next;
        this.min.next = x;
        x.next.prev = x;
    }

    private void cascadingCut(HeapNode y) { //Yoad - after cutting y's child we check if y is marked, if it is we cut it as well.
        HeapNode z = y.parent;
        if (z == null) { // if y is a root
            return;
        }
        if (!y.mark) {
            y.mark = true;
        } else {
            cut(y);
            cascadingCut(z);
        }
    }


    /**
     *
     * Delete the x from the heap.
     *
     */
    public void delete(HeapNode x) { //Yoad
        if (x == null) {
            return;
        }
        if (x.key == this.min.key) {
            deleteMin();
            return;
        }
        // detach children from x and add them to the root list
        HeapNode child = x.child;
        if (child != null) {
            child.prev.next = this.min.next;
            this.min.next.prev = child.prev;
            child.prev = this.min;
            this.min.next = child;
            this.numTrees += x.rank;
            this.totalCuts += x.rank;
            // update their parent and mark
            while (child.parent != null) {
                child.parent = null;
                child.mark = false;
                child = child.next;
            }
        }
        // delete x
        this.size--;
        x.prev.next = x.next;
        x.next.prev = x.prev;
        if (x.parent != null) {
            x.parent.rank--;
            if (x.next == x) {
                x.parent.child = null;
            } else {
                x.parent.child = x.next;
            }
            totalCuts++;
            cascadingCut(x.parent);
        } else {
            numTrees--;
        }
    }

    /**
     *
     * Return the total number of links.
     *
     */
    public int totalLinks() //Shalev
    {
        return this.totalLinks;
    }

    /**
     *
     * Return the total number of cuts.
     *
     */
    public int totalCuts() //Shalev
    {
        return this.totalCuts;
    }

    /**
     *
     * Meld the heap with heap2
     *
     */
    public void meld(FibonacciHeap heap2) { //Yoad
        if (heap2 == null || heap2.size() == 0) { // if the fibo heap we are melding with is empty, than there is nothing to do.
            return;
        }
        if (this.size() == 0) { // if our fibo heap is empty than lets turn our fibo heap to heap2.
            this.min = heap2.min;
            this.size = heap2.size;
            this.totalCuts = heap2.totalCuts;
            this.totalLinks = heap2.totalLinks;
            this.numTrees = heap2.numTrees;
            return;
        }
        // if both heaps are not empty, than we need to connect the two heaps.
        this.min.prev.next = heap2.min.next;
        heap2.min.next.prev = this.min.prev;
        this.min.prev = heap2.min.prev;
        heap2.min.prev.next = this.min;
        if (heap2.min.key < this.min.key) {
            this.min = heap2.min;
        }
        this.size += heap2.size;
        this.totalCuts += heap2.totalCuts;
        this.totalLinks += heap2.totalLinks;
        this.numTrees += heap2.numTrees;
    }

    /**
     *
     * Return the number of elements in the heap
     *
     */
    public int size() //Shalev
    {
        return this.size;
    }

    /**
     *
     * Return the number of trees in the heap.
     *
     */
    public int numTrees() { //Yoad
        return this.numTrees;
    }

    public boolean empty() {
        return this.size == 0;
    }

    /**
     * Class implementing a node in a Fibonacci Heap.
     *
     */
    public static class HeapNode {

        public int key;
        public String info;
        public HeapNode child;
        public HeapNode next;
        public HeapNode prev;
        public HeapNode parent;
        public int rank;
        public boolean mark;

        public HeapNode() {
        }

        public HeapNode(int key, String info) {
            this.key = key;
            this.info = info;
            this.child = null;
            this.next = this;
            this.prev = this;
            this.parent = null;
            this.rank = 0;
            this.mark = false;
        }

        public int getKey() {
            return this.key;
        }
    }

    // ***************** METHODS FOR TESTING ****************
    // TODO: Delete tests when done
    public void printHeap() {
        if (this.min == null) {
            System.out.println("Heap is empty.");
            return;
        }

        System.out.println("---------------------------------------");
        System.out.println("FibonacciHeap (size=" + this.size
                + ", numTrees=" + this.numTrees
                + ", min.key=" + this.min.key + "):");

        // Traverse the top-level ring starting from 'min'
        HeapNode current = this.min;
        boolean done = false;
        int treeCount = 0;

        while (!done) {
            treeCount++;
            System.out.println("Tree #" + treeCount + ":");
            // Recursively print this root and its children
            printSubtree(current, "  ");

            current = current.next;
            if (current == this.min) {
                done = true;
            }
        }
        System.out.println("---------------------------------------");
    }

    /**
     * Recursively prints a node's details, then prints its children (if any).
     * 'indent' is just a string (e.g. " " or " ") used for indentation.
     */
    private void printSubtree(HeapNode node, String indent) {
        // Print this node's basic info
        System.out.println(indent + "- key=" + node.key
                + ", rank=" + node.rank
                + ", mark=" + node.mark);

        // If it has children, recursively print each child in its circular list
        if (node.child != null) {
            HeapNode child = node.child;
            boolean done = false;
            while (!done) {
                printSubtree(child, indent + "  ");
                child = child.next;
                if (child == node.child) {
                    done = true;
                }
            }
        }
    }

    public int potential() {
        int t = 0; // Number of trees (roots)
        int m = 0; // Number of marked nodes
        HeapNode current = min;

        if (current != null) {
            // Traverse the circular linked list of roots
            do {
                t++; // Each root is a tree
                m += countMarkedNodes(current); // Count marked nodes in the tree
                current = current.next;
            } while (current != min);
        }

        return t + 2 * m; // Potential is t + 2 * m
    }

    // Helper method to count marked nodes in a tree
    private int countMarkedNodes(HeapNode node) {
        int count = 0;
        while (node != null) {
            if (node.mark) {
                count++; // Increment if the node is marked

            }
            node = node.child;
        }
        return count;
    }

    public int[] countersRep() {
        int[] counters = new int[calculateMaxRank()]; // Array to store the number of trees of each rank
        HeapNode current = min;

        // Traverse the root list and count trees by rank
        if (current != null) {
            do {
                int rank = current.rank;
                counters[rank]++; // Increment the count of trees of this rank
                current = current.next;
            } while (current != min);
        }

        return counters;
    }

    // Helper method to calculate the maximum possible rank (based on the number of nodes in the heap)
    private int calculateMaxRank() {
        return (int) Math.ceil(Math.log(size) / Math.log(2)) + 1;
    }

    // ***************** END OF TESTING METHODS ****************
}
