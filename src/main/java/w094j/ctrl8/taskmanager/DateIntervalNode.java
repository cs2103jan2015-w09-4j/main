package w094j.ctrl8.taskmanager;

import java.util.Date;

//@author A0065517A
/**
 * Date Interval node to represent each interval on the tree.
 */
class DateIntervalNode {

    Date endDate;
    DateIntervalNode leftSubTree;
    Date maxEndDate;
    DateIntervalNode rightSubTree;
    Date startDate;

    DateIntervalNode(DateIntervalNode left, Date startDate, Date endDate,
            Date maxEndDate, DateIntervalNode right) {
        this.leftSubTree = left;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxEndDate = maxEndDate;
        this.rightSubTree = right;
    }

}