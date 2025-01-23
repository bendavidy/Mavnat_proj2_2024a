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
        testDeleteMinWithRootAndChildren();
    }
    private static void testDeleteMinWithRootAndChildren() {
        System.out.println("=== testDeleteMinWithRootAndChildren ===");
        
        FibonacciHeap h = new FibonacciHeap();
    
        // 1) Insert the min root (key=1)
        FibonacciHeap.HeapNode minRoot = h.insert(1, "root-min");
    
        // 2) Insert another separate root (key=3)
        FibonacciHeap.HeapNode otherRoot = h.insert(11, "root-other");
    
        // 3) Insert more nodes (keys=5,7,10), each starts as a root
        FibonacciHeap.HeapNode childA = h.insert(5, "childA");
        FibonacciHeap.HeapNode childB = h.insert(7, "childB");
        FibonacciHeap.HeapNode childC = h.insert(10, "childC");
    
        // 4) Force them to be children of the root with key=1
        //    (We do this artificially; in a normal Fibonacci heap,
        //     'link(...)' is typically used in consolidate for equal-rank roots.
        //     But this is just a test setup.)
        h.link(childA, minRoot);
        h.link(childB, minRoot);
        h.link(childC, minRoot);
        h.printHeap();
    
        // Now the heap has 2 root-level nodes:
        //   - key=1 with children = {5,7,10}
        //   - key=3 (no children)
        // min should be 1, size=5, numTrees=2
    
        // Print the heap structure if you have a printHeap() or do manual checks
        System.out.println("Before deleteMin:");
        // If you have a printHeap() method, uncomment:
        // h.printHeap(); 
        System.out.println("  size=" + h.size() +
                           ", min=" + h.findMin().key +
                           ", numTrees=" + h.numTrees());
    
        // 5) Now delete the min (which is the node with key=1)
        h.deleteMin();
    
        System.out.println("After deleteMin (removing key=1):");
        // If you have a printHeap() method, uncomment:
        // h.printHeap();
        System.out.println("  size=" + h.size() +
                           ", min=" + (h.findMin() == null ? "null" : h.findMin().key) +
                           ", numTrees=" + h.numTrees());
    
        System.out.println("=== END testDeleteMinWithRootAndChildren ===\n");
    }
    

    
public void printHeap() {
    if (this.min == null) {
        System.out.println("Heap is empty.");
        return;
    }

    System.out.println("---------------------------------------");
    System.out.println("FibonacciHeap (size=" + this.size + 
                       ", numTrees=" + this.numTrees +
                       ", min.key=" + this.min.key + "):");

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
 * 'indent' is just a string (e.g. "  " or "    ") used for indentation.
 */
private void printSubtree(HeapNode node, String indent) {
    // Print this node's basic info
    System.out.println(indent + "- key=" + node.key +
                       ", rank=" + node.rank +
                       ", mark=" + node.mark);

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
    public HeapNode insert(int key, String info) // Shalev-Works.
    {  //first, let's insert the relevant information for the new node.
        //public int key;
        //public String info;
       // public HeapNode child;
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
    private void consolidate(HeapNode node) {
        // 1) Gather current roots into a list and remove them from the ring
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
    
        // 2) Create array A for merging roots of equal rank
        int arraySize = (int) (Math.floor(Math.log(this.size) / Math.log(2))) + 2;
        HeapNode[] A = new HeapNode[arraySize];
        Arrays.fill(A, null);
    
        // 3) Merge roots with the same rank
        for (HeapNode w : rootList) {
            // Remove w from circular list so we can re-link it from scratch
            w.prev.next = w.next;
            w.next.prev = w.prev;
            w.parent = null;
            w.next = w;
            w.prev = w;
    
            int d = w.rank;
            HeapNode x = w;
            while (A[d] != null) {
                HeapNode y = A[d];
                // Make sure x is the smaller-key node
                if (y.key < x.key) {
                    HeapNode temp = x;
                    x = y;
                    y = temp;
                }
                // Link bigger key y under x
                link(y, x);
                A[d] = null;
                d++;
            }
            A[d] = x;
        }
    
        // 4) Rebuild the root list and find new min
        this.min = null;
        this.numTrees = 0; // we'll re-count them now
    
        for (int i = 0; i < A.length; i++) {
            if (A[i] != null) {
                // If we have no current min, A[i] becomes min
                if (this.min == null) {
                    this.min = A[i];
                    A[i].prev = A[i];
                    A[i].next = A[i];
                } else {
                    // Insert A[i] into the min's circular root list
                    A[i].prev = this.min;
                    A[i].next = this.min.next;
                    this.min.next.prev = A[i];
                    this.min.next = A[i];
    
                    // Update min pointer if necessary
                    if (A[i].key < this.min.key) {
                        this.min = A[i];
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
    private void addChildrenToRootList(HeapNode z) { //Shalev- works
        // z is one of the roots
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

        // Increase x's rank because it now has an additional child
        x.rank++;

        // y is a fresh child, so reset mark
        y.mark = false;
    }

    public void deleteMin() 
{
    // 1) If heap is empty, nothing to delete
    if (this.min == null) {
        return;
    }

    // We'll call the current min 'z'
    HeapNode z = this.min;

    // 2) If z has children, promote them all to the root list
    
    if (z.child != null) {
        // 'addChildrenToRootList' does the pointer surgery:
        //   * Each child => parent's pointer becomes null
        //   * Moves each child into the top-level ring
        this.addChildrenToRootList(z);
        // Clear z's child pointer now that they're top-level roots
        if(this.min.key == 3){
        //System.out.println(this.min.child.key);
       
        
        }

        z.child = null;
    }

    // 3) Remove z from the root list
    z.prev.next = z.next;
    z.next.prev = z.prev;

    // 4) If z was the ONLY node in the root ring, set min to null
    //    else pick z.next as a new 'min' placeholder
    if (z == z.next) {
        this.min = null;
        // If you maintain 'numTrees' in real time, you can set it to 0:
        this.numTrees = 0;
    } else {
        this.min = z.next;
    }

    // 5) Decrease the heap size by 1
    this.size--;

    // 6) If the heap is not empty, run 'consolidate' to merge roots and update min
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

        return;   		
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