package structures;

import java.util.*;

/**
 * Encapsulates an interval tree.
 * 
 * @author runb-cs112
 */
public class IntervalTree {
	
	/**
	 * The root of the interval tree
	 */
	IntervalTreeNode root;
	
	/**
	 * Constructs entire interval tree from set of input intervals. Constructing the tree
	 * means building the interval tree structure and mapping the intervals to the nodes.
	 * 
	 * @param intervals Array list of intervals for which the tree is constructed
	 */
	public IntervalTree(ArrayList<Interval> intervals) {
		
		// make a copy of intervals to use for right sorting
		ArrayList<Interval> intervalsRight = new ArrayList<Interval>(intervals.size());
		for (Interval iv : intervals) { 
			intervalsRight.add(iv);
		}
		
		// rename input intervals for left sorting
		ArrayList<Interval> intervalsLeft = intervals;
		
		// sort intervals on left and right end points
		Sorter.sortIntervals(intervalsLeft, 'l');
		Sorter.sortIntervals(intervalsRight,'r');
		System.out.println();
		System.out.println("INTERVALS LEFT IS: " +intervalsLeft);
		System.out.println("INTERVALS RIGHT IS: " + intervalsRight);
		System.out.println();
		
		// get sorted list of end points without duplicates
		ArrayList<Integer> sortedEndPoints = Sorter.getSortedEndPoints(intervalsLeft, intervalsRight);
		
		// build the tree nodes
		root = buildTreeNodes(sortedEndPoints);
		System.out.println();
		System.out.println("the tree is: " + root);
		System.out.println("INTERVALS LEFT IS: " +intervalsLeft);
		System.out.println("INTERVALS RIGHT IS: " + intervalsRight);
		System.out.println("moving on to MAPPING INTEVALS");
		System.out.println();

		// map intervals to the tree nodes
		mapIntervalsToTree(intervalsLeft, intervalsRight);
		System.out.println("INTERVALS LEFT IS: " +intervalsLeft);
		System.out.println("INTERVALS RIGHT IS: " + intervalsRight);
	}
	
	private static float minSplitT1(IntervalTreeNode T1){
		if (T1.leftChild == null){
			return T1.splitValue;
			}
		else { // if T1.leftChild != null
			return minSplitT1(T1.leftChild);
		}
	}
		
	private static float maxSplitT1(IntervalTreeNode T1){
		if (T1.rightChild == null){
			return T1.splitValue;
			}
		else {
			return maxSplitT1(T1.rightChild);
		}
	}
		
	private static float minSplitT2(IntervalTreeNode T2){
		if (T2.leftChild == null){
			return T2.splitValue;
			}
		else {
			return minSplitT2(T2.leftChild);
		}
	}
		
	private static float maxSplitT2(IntervalTreeNode T2){
		if (T2.rightChild == null){
			return T2.splitValue;
			}
		else {
			return maxSplitT2(T2.rightChild);
		}
	}
	
	/**
	 * Builds the interval tree structure given a sorted array list of end points.
	 * 
	 * @param endPoints Sorted array list of end points
	 * @return Root of the tree structure
	 */
	public static IntervalTreeNode buildTreeNodes(ArrayList<Integer> endPoints) {
		Queue<IntervalTreeNode> Q = new Queue<IntervalTreeNode>();
		System.out.println();
		System.out.println();
		System.out.println("TREE BUILDING BEGINS");
		IntervalTreeNode T = null;
		//make individual trees for each endpoint and enqueue them into queue Q
		for(int index = 0; index != endPoints.size(); index++){
			T = new IntervalTreeNode(endPoints.get(index), endPoints.get(index), endPoints.get(index));
			Q.enqueue(T);
			System.out.println("the endpoint in the queue: " + Q.rear.data.maxSplitValue);
		}
		
		while (true){
			int s = Q.size();
			if (s == 1){
				T = Q.dequeue();
				//mapIntervalsToTree(intervalsLeft,intervalsRight);
				return T; /** check at end if correct*/ // complete tree gets returned
			}
			
			int temps = s;
			while (temps > 1){
				IntervalTreeNode T1 = Q.dequeue();
				IntervalTreeNode T2 = Q.dequeue();
				float v1 = maxSplitT1(T1); 
				float v2 = minSplitT2(T2); 
				float N = (v1+v2)/2;
				T = new IntervalTreeNode(N, minSplitT1(T1), maxSplitT2(T2));
				T.leftChild = T1;
				T.rightChild = T2;
				System.out.println("the tree T constructed is: " + T);
				Q.enqueue(T);
				temps = temps - 2;
			}
			if (temps == 1){
				Q.enqueue(Q.dequeue());
			}
		}
		
	}
	
	/** fills in the leftIntervals arraylist for each node */
	
	private IntervalTreeNode leftList(IntervalTreeNode T, ArrayList<Interval> leftSortedIntervals){
		if (T.leftChild == null && T.rightChild == null){ // leaf node
			System.out.println("reached a leaf, T is: " + T);
			return T;}
		else {
			float N = T.splitValue;
			System.out.println("the splitValue now is: " + N);
			int index = 0;
			while (leftSortedIntervals.isEmpty() != true && index != leftSortedIntervals.size()){
				System.out.println("inside while-loop, index is: " + index);
				if (leftSortedIntervals.get(index).contains(N) == true){
					System.out.println("splitVal IS in interval " + leftSortedIntervals.get(index));
					if(T.leftIntervals == null){
						T.leftIntervals = new ArrayList<Interval>();
					}
					T.leftIntervals.add(leftSortedIntervals.get(index));
					System.out.println("the interval added to left side of splitVal is: " + leftSortedIntervals.get(index));
					System.out.println("the LEFT SIDE of splitVal is: " + T.leftIntervals);
					System.out.println("index unchanged, every thing in Lsort moved to the left");
					leftSortedIntervals.remove(index);} //remove the interval, shifts everything to left, don't change index because new interval is at that index
				else { // if splitVal isn't in interval
					System.out.println("splitVal is NOT in interval " + leftSortedIntervals.get(index));
					index++; //go on to next interval
				}
			}
			// went through entire Lsort for splitVal at the node, go on to next nodes to left and right of splitVal
			
			//if (leftSortedIntervals.isEmpty() == true){
				//return T;}
			//else {
				if (T.leftChild != null){
					System.out.println("went left");
					leftList(T.leftChild, leftSortedIntervals);
				}
				
				if (T.rightChild != null){
					System.out.println("went right");
					leftList(T.rightChild, leftSortedIntervals);
				}
				
			return T;
		}
	}
	
	/** fills in the rightIntervals arraylist for each node */
	
	private IntervalTreeNode rightList(IntervalTreeNode T, ArrayList<Interval> rightSortedIntervals){
		if (T.leftChild == null && T.rightChild == null){ // leaf node
			System.out.println("reached a leaf, T is: " + T);
			return T;}
		else {
			float N = T.splitValue;
			System.out.println("the splitValue now is: " + N);
			int index = 0;
			while (rightSortedIntervals.isEmpty() != true && index != rightSortedIntervals.size()){
				System.out.println("inside while-loop, index is: " + index);
				if (rightSortedIntervals.get(index).contains(N) == true){
					System.out.println("splitVal IS in interval " +rightSortedIntervals.get(index));
					if(T.rightIntervals == null){
						T.rightIntervals = new ArrayList<Interval>();
					}
					T.rightIntervals.add(rightSortedIntervals.get(index));
					System.out.println("the interval added to right side of splitVal is: " + rightSortedIntervals.get(index));
					System.out.println("the RIGHT SIDE of splitVal is: " + T.rightIntervals);
					System.out.println("index unchanged, every thing in Rsort moved to the left");
					rightSortedIntervals.remove(index);} //remove the interval, shifts everything to left, don't change index because new interval is at that index
				else { // if splitVal isn't in interval
					System.out.println("splitVal is NOT in interval " + rightSortedIntervals.get(index));
					index++; //go on to next interval
				}
			}
			// went through entire Rsort for splitVal at the node, go on to next nodes to left and right of splitVal
			
			//if (rightSortedIntervals.isEmpty() == true){
				//return T;}
			//else {
				if (T.leftChild != null){
					System.out.println("went left");
					rightList(T.leftChild, rightSortedIntervals);
				}
				
				if (T.rightChild != null){
					System.out.println("went right");
					rightList(T.rightChild, rightSortedIntervals);
				}
				
			return T;
		}

	}
	
	/**
	 * Maps a set of intervals to the nodes of this interval tree. 
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 */
	public void mapIntervalsToTree(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) {
		Queue<Interval> L = new Queue<Interval>();
		Queue<Interval> R = new Queue<Interval>();
		for(int index = 0; index != leftSortedIntervals.size(); index++){
			L.enqueue(leftSortedIntervals.get(index));
		} // stored Lsort intervals into queue
		for(int index = 0; index != rightSortedIntervals.size(); index++){
			R.enqueue(rightSortedIntervals.get(index));
		} // stored Rsort intervals into queue
		
		IntervalTreeNode T = root;
		leftList(T, leftSortedIntervals); 
		System.out.println();
		System.out.println("leftIntervals stored for each node");
		System.out.println("now storing rightIntervals for each node");
		System.out.println();
		rightList(T, rightSortedIntervals);
		
		// refill Rsort and Lsort arraylists
		while(L.isEmpty() != true){
			leftSortedIntervals.add(L.dequeue());
		}
		
		while(R.isEmpty() != true){
			rightSortedIntervals.add(R.dequeue());
		}
	}
	
	/** 
	 * Gets all intervals in this interval tree that intersect with a given interval.
	 * 
	 * @param q The query interval for which intersections are to be found
	 * @return Array list of all intersecting intervals; size is 0 if there are no intersections
	 */
	
	private ArrayList<Interval> recursingForIntersection(Interval q, IntervalTreeNode R, ArrayList<Interval> ResultList){
		
		float SplitVal = R.splitValue;
		ArrayList<Interval> Llist = R.leftIntervals;
		ArrayList<Interval> Rlist = R.rightIntervals;
		IntervalTreeNode Lsub = R.leftChild;
		IntervalTreeNode Rsub = R.rightChild;
		
		if (R.leftChild == null && R.rightChild == null){
			System.out.println("returning, reached a leaf: " + R);
			return ResultList;
		}
		
		if (q.contains(SplitVal)){ // SplitVal is in q
			System.out.println("ResultList is: " + ResultList);
			System.out.println("q is to the left of SplitVal " + SplitVal);
			R.matchLeft(q,ResultList);
			System.out.println("ResultList is: " + ResultList);
			recursingForIntersection(q, Lsub, ResultList);
			System.out.println("After running through Lsub of " + SplitVal + ", ResultList is: " + ResultList);
			recursingForIntersection(q, Rsub, ResultList);
			return ResultList;}
		else if (SplitVal < q.leftEndPoint){ // SplitVal falls to the left of q
			System.out.println("ResultList is: " + ResultList);
			System.out.println("SplitVal is to the left of q: SplitVal = " + SplitVal);
			System.out.println("going through rightIntervals of node");
			R.matchRight(q,ResultList);
			System.out.println("going to the right of R, which is " + Rsub);
			recursingForIntersection(q,Rsub,ResultList);
			System.out.println("ResultList = " + ResultList);
			return ResultList;}		
		else if (SplitVal > q.rightEndPoint){ // SplitVal falls to the right of q
			System.out.println("ResultList is: " + ResultList);
			System.out.println("SplitVal is to the right of q: SplitVal = " + SplitVal);
			System.out.println("going through leftIntervals of node");
			R.matchLeft(q, ResultList);
			System.out.println("going to left subtree of " + R + ", which is " + Lsub);
			recursingForIntersection(q,Lsub,ResultList);
			System.out.println("ResultList = " + ResultList);
			return ResultList;}
		
	
		return ResultList;
	}
	
	public ArrayList<Interval> findIntersectingIntervals(Interval q) {
		ArrayList<Interval> ResultList = new ArrayList<Interval>();
		IntervalTreeNode R = root;
		return recursingForIntersection(q,R,ResultList);
	}
	
	/**
	 * Returns the root of this interval tree.
	 * 
	 * @return Root of interval tree.
	 */
	public IntervalTreeNode getRoot() {
		return root;
	}
}

