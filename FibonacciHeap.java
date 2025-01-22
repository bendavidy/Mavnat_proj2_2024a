
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * FibonacciHeap
 *
 * An implementation of Fibonacci heap over positive integers.
 *
 */
public class FibonacciHeap {

    public static void main(String[] args) {
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
        // should be replaced by student code
    }

    /**
     *
     * pre: key > 0
     *
     * Insert (key,info) into the heap and return the newly generated HeapNode.
     *
     */
    public HeapNode insert(int key, String info) // Shalev
    {  //first, let's insert the relevant information for the new node.

        //public int key;
        //public String info;
        //public HeapNode child;
        //public HeapNode next;
        //public HeapNode prev;
        //public HeapNode parent;
        //public int rank;
        //public boolean mark;
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
        return this.min; // should be replaced by student code
    }

    /**
     *
     * Delete the minimal item
     *
     */
    public void consolidate(HeapNode node) { // Need to implement for DeleteMin() 
        // Here, we created a list of all the roots
        List<HeapNode> rootList = new ArrayList<>();
        HeapNode current = node;
        boolean done = false;
        while (!done) {
            rootList.add(current);
            current = current.next;
            if (current == node) {
                done = true;
            }
        }

        // Here, we created an array of the comaptible size- the possible number of ranks...
        int arraySize = (int) (Math.floor(Math.log(this.size) / Math.log(2))) + 2;
        HeapNode[] A = new HeapNode[arraySize];
        Arrays.fill(A, null);

        for (HeapNode R : rootList) {
            HeapNode x = R;
            int r = x.rank;

            if (A[r] == null) {
                A[r] = x;
            } else {
                done = true;
                while (!done) {
                    this.link(A[r], x);
                    if (x.key < A[r].key) {
                        x.rank++;
                    } else {
                        A[r].rank++;
                    }
                    HeapNode merged = A[r];

                    // TODO: continue from here, update numTrees, min, etc.
                }
            }
        }

    }

    /**
     * Moves all children of node z into the heap's root list, making them new
     * roots. Assumes z.child is non-null.
     */
    private void addChildrenToRootList(HeapNode z) {
        // If z has no children, nothing to do
        if (z.child == null) {
            return;
        }

        // We'll iterate over the entire child ring exactly once
        HeapNode startChild = z.child;
        HeapNode current = startChild;
        boolean done = false;
        while (!done) {
            // Store next sibling before we alter pointers
            HeapNode nextC = current.next;

            // 1. Remove 'current' from child ring
            current.prev.next = current.next;
            current.next.prev = current.prev;

            // 2. Make 'current' a new root by splicing it into 
            //    the circular list headed by this.min
            current.parent = null;
            current.next = this.min;
            current.prev = this.min.prev;
            this.min.prev.next = current;
            this.min.prev = current;

            // Move on to the next sibling 
            current = nextC;
            // Stop if we've looped back to the start
            if (current == startChild) {
                done = true;
            }
        }

        // z no longer has children
        z.child = null;
    }

    private void link(HeapNode y, HeapNode x) {
        if (y.key < x.key) {
            link_help(y, x);
        } else {
            link_help(x, y);
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

        // Increase x's rank because it now has an additional child
        x.rank++;

        // y is a fresh child, so reset mark
        y.mark = false;
    }

    public void deleteMin() //Shalev
	// TODO: update numTrees if needed
    {
        HeapNode fakeMin;
        // if the tree is empty
        if (this.min == null) {
            return;

        }
        HeapNode minChild = this.min.child;
        if (minChild == null) {
            if (this.size == 1) {
                this.min = null;
                this.size--;
            } else {
                this.min.prev.next = this.min.next;
                this.min.next.prev = this.min.prev;
                this.size--;
                fakeMin = this.min.next;
                this.min = fakeMin;
                this.consolidate(this.min); // here we will update for real the min field
            }
        } else {
			this.addChildrenToRootList(this.min);
			this.min.prev.next = this.min.next;
			this.min.next.prev = this.min.prev;
			fakeMin = this.min.next;
			this.min = fakeMin;
			this.size--;
			this.consolidate(this.min); // here we will update for real the min field
		}

    }

    /**
     *
     * pre: 0<diff<x.key
     *
     * Decrease the key of x by diff and fix the heap.
     *
     */
    public void decreaseKey(HeapNode x, int diff) { //Yoad    
        x.key = x.key - diff;
        if (x.parent != null && x.key < x.parent.key) {
            cut(x);
            cascadingCut(x.parent);
        }
        if (x.key < this.min.key) {
            this.min = x;
        }
    }

    private void cut(HeapNode x) { //Yoad
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

    private void cascadingCut(HeapNode y) { //Yoad
        HeapNode z = y.parent;
        if (z != null) {
            if (!y.mark) {
                y.mark = true;
            } else {
                cut(y);
                cascadingCut(z);
            }
        }
    }

    /**
     *
     * Delete the x from the heap.
     *
     */
    public void delete(HeapNode x) { //Yoad
        decreaseKey(x, Math.abs(x.key) + Math.abs(this.min.key) + 1);
        deleteMin();
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
    public void meld(FibonacciHeap heap2) //Shalev- Stopped at the middle, need to continue
    {
        if (heap2.size() == 0) { // if the fibo heap we are melding with is empty, than there is nothing to do.
            return;
        }
        if (this.size() == 0) { // if our fibo heap is empty than lets turn our fibo heap to heap2.
            this.min = heap2.min;
            this.size = heap2.size;
            this.totalCuts = heap2.totalCuts;
            this.totalLinks = heap2.totalLinks;
            return;
        }
        return; // should be replaced by student code   		
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
    }

}
