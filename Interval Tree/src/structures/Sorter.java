package structures;

import java.util.ArrayList;

/**
 * This class is a repository of sorting methods used by the interval tree.
 * It's a utility class - all methods are static, and the class cannot be instantiated
 * i.e. no objects can be created for this class.
 * 
 * @author runb-cs112
 */
public class Sorter {

	private Sorter() { }	
	
	/**
	 * Sorts a set of intervals in place, according to left or right endpoints.  
	 * At the end of the method, the parameter array list is a sorted list. 
	 * 
	 * @param intervals Array list of intervals to be sorted.
	 * @param lr If 'l', then sort is on left endpoints; if 'r', sort is on right endpoints
	 */
	public static void sortIntervals(ArrayList<Interval> intervals, char lr) {
		// if intervals is empty
		if(intervals.isEmpty() == true){ 
			return; // hence, do nothing
		}
		
		//intervals isn't empty 
		//now checking for duplicates in intervals
		int i = 0; 
		int j = 0;
		while(i != intervals.size()){
			Interval temp = intervals.get(i);
			System.out.println();
			System.out.println("index of temp: " + i + ", temp: " + temp);
			while (j != intervals.size()){
				System.out.println("j is: " + j + ", i is: " + i);
				System.out.println("interval at j is: " + intervals.get(j));
				if (i == j){ // if comparing the same object at the same index
					System.out.println("i == j, moving on to the next interval");
					j++; // go on to next index in Intervals
				} else if (temp.leftEndPoint == intervals.get(j).leftEndPoint && temp.rightEndPoint == intervals.get(j).rightEndPoint){
					System.out.println("duplicate found at a different index, every thing in arraylist pushed to left, not incrementing");
					intervals.remove(j);
				} else { // if temp doesn't find a duplicate in intervals
					System.out.println("no duplicate found at this index, moving on to next interval");
					j++; // go on to comparing the next Interval in intervals
				}
			}
			System.out.println("incrementing i, resetting j to 0");
			j = i+1;
			i++; // temp is going to be the next interval
		}
		
		System.out.println();
		System.out.println("duplicates taken care off");
		System.out.println();
		
		// duplicate have been taken care of
		if (lr == 'l'){
			for (int index = 1; index != intervals.size(); index++){
				Interval temp = intervals.get(index);
				int curr = index - 1;
				
				while (curr >= 0 && intervals.get(curr).leftEndPoint > temp.leftEndPoint){
					intervals.set(curr+1,intervals.get(curr));
					curr = curr - 1;
				}
				
				intervals.set(curr+1,temp);
			}
			
		System.out.println("Lsort is: " + intervals);
		System.out.println();
		}
		
		if (lr == 'r'){
			for (int index = 1; index != intervals.size(); index++){
				Interval temp = intervals.get(index);
				int curr = index - 1;
				
				while (curr >= 0 && intervals.get(curr).rightEndPoint > temp.rightEndPoint){
					intervals.set(curr+1,intervals.get(curr));
					curr = curr - 1;
				}
				
				intervals.set(curr+1,temp);
			}
			
		System.out.println("Rsort is: " + intervals);
		System.out.println();
		}
	}
	
	/**
	 * Given a set of intervals (left sorted and right sorted), extracts the left and right end points,
	 * and returns a sorted list of the combined end points without duplicates.
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 * @return Sorted array list of all endpoints without duplicates
	 */
	public static ArrayList<Integer> getSortedEndPoints(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) {
		
		//getting the UNIQUE left endpoints
		ArrayList<Integer> points = new ArrayList<Integer>();
		int index = 0;
		Integer rearOfPoints = null;
		System.out.println("Lsort is: " + leftSortedIntervals);
		while (index != leftSortedIntervals.size()){ 
			Integer temp = new Integer(leftSortedIntervals.get(index).leftEndPoint);	
			System.out.println("the new left endpoint from Lsort is: " + temp);
			if (points.isEmpty() == false){
				rearOfPoints = new Integer(points.get(points.size() - 1));
				System.out.println("the last endpoint in points is: " + rearOfPoints);
			}
			
			if (points.isEmpty() == true){
				System.out.println("points is empty");
				points.add(temp);
				index++;}
			else if (temp.equals(rearOfPoints)){
				System.out.println("duplicate left endpoint found: temp = " + temp + ", rearOfPoints = " + rearOfPoints);
				index++;}
			else { // if temp is not equal to the last endpoint in points
				System.out.println("different end point found: temp = " + temp + ", rearOfPoints = " + rearOfPoints);
				points.add(temp);
				index++;
			}
		}
		System.out.println("After getting unique LEFT endPoints, points is: " + points);
		System.out.println();
		
		//now adding UNIQUE right endpoints
		Integer newEndpt = null;
		int j = 0;
		index = 0;
		System.out.println("Rsort is: " + rightSortedIntervals);
		while (index != rightSortedIntervals.size()){
			int index2 = 0; // for points, resets index2 every time you move onto a new interval in Rsort
			while (index2 != points.size()){
				System.out.println("the endpt in points is: " + points.get(index2) + ", the endpt in Rsort is: " + rightSortedIntervals.get(index).rightEndPoint);
				if (points.get(index2).equals((Integer) rightSortedIntervals.get(index).rightEndPoint)){
					index++;
					System.out.println("moved on to the next interval in Rsort");
					break;}
				else { //if the two aren't equal
					index2++;
					System.out.println("moved on to next endpt in points");}
			}
			/*at this point, when through all of points array list to see if there was
			 * match for Rsort, BUT NO MATCH!
			 */
			if (index2 == points.size()){
				System.out.println("went through all of points");
				newEndpt = new Integer(rightSortedIntervals.get(index).rightEndPoint);
				System.out.println("the newEndpt is: " + newEndpt);
				System.out.println("points BEFORE newEndpt added: " + points);
				for(j = 0; j != points.size(); j++){
					if (newEndpt <  points.get(j)){
						points.add(j,newEndpt); // adds newEndpt in location of the integer that is bigger than it, thus pushing the rest of the values in points over
						System.out.println("1. points AFTER newEndpt added: " + points);
						break; 
					}
				}
				//if no value in points is less than newEndpt, add it to the end of points
				if (j == points.size()){
					points.add(newEndpt);
					System.out.println("2. points AFTER newEndpt added: " + points);
				}
				//go on to next interval in Rsort
				index++;
			}
		}
		System.out.println("After getting the unique RIGHT endpoints, points is: " + points);
		
		return points;
	}
}
