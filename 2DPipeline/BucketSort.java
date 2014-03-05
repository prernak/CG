import java.util.Comparator;

/* This class is used for sorting of buckets */

public class BucketSort implements Comparator<Bucket>{

	@Override
	public int compare(Bucket bucket1, Bucket bucket2) {
	    int startComparison = compare((double)bucket1.getX(), (double)bucket2.getX());
	    return startComparison != 0 ? startComparison
	         : compare((double)(bucket1.getDx())/(bucket1.getDy()), (double)(bucket2.getDx())/(bucket2.getDy()));
	  }

	  private static int compare(double first, double second) {
	    return first < second ? -1
	         : first > second ? 1
	         : 0;
	  }
}
