package w094j.ctrl8.taskmanager;

import java.util.Date;

import w094j.ctrl8.pojo.Task;

//@author A0065517A
/**
 * Tree to sort out the intervals.
 */
public class DateIntervalTree {

    private DateIntervalNode rootNode;

    /**
     * Initializes an empty tree.
     */
    public DateIntervalTree() {
    }

    /**
     * Initializes a tree with the task array.
     *
     * @param taskArray
     */
    public DateIntervalTree(Task[] taskArray) {
        this.addAll(taskArray);
    }

    /**
     * Add all tasks to the interval tree, if there is an interval to add in
     * each task.
     *
     * @param taskArray
     */
    public void addAll(Task[] taskArray) {
        // Filter the timed tasks
        for (Task eaTask : taskArray) {
            if (eaTask.getTaskType().equals(Task.TaskType.TIMED)) {
                this.addInterval(eaTask);
            }
        }
    }

    /**
     * Traverse the tree and update the maxEndDate and also appending the task
     * with the range to the tree when needed.
     *
     * @param task
     */
    public void addInterval(Task task) {

        Date startDate = task.getStartDate();
        Date endDate = task.getEndDate();

        DateIntervalNode newLeaf = new DateIntervalNode(null, startDate,
                endDate, endDate, null);

        DateIntervalNode currentNode = this.rootNode;
        while (currentNode != null) {

            // Update the max along the way
            if (endDate.after(currentNode.maxEndDate)) {
                currentNode.maxEndDate = endDate;
            } else {
                currentNode.maxEndDate = currentNode.maxEndDate;
            }

            // Update the subTree with the new leaf
            if (startDate.before(currentNode.startDate)) {
                if (currentNode.leftSubTree == null) {
                    currentNode.leftSubTree = newLeaf;
                    return;
                } else {
                    currentNode = currentNode.leftSubTree;
                }
            } else {
                if (currentNode.rightSubTree == null) {
                    currentNode.rightSubTree = newLeaf;
                    return;
                } else {
                    currentNode = currentNode.rightSubTree;
                }
            }
        }

        // Can only reach here if rootNode is null
        this.rootNode = newLeaf;
    }

    /**
     * Check if the current date range in task is overlapped in the tree.
     *
     * @param task
     * @return <code>true</code> if there is an overlap, <code>false</code>
     *         otherwise.
     */
    public boolean isOverlap(Task task) {

        Date startDate = task.getStartDate();
        Date endDate = task.getEndDate();

        DateIntervalNode currentNode = this.rootNode;
        while (currentNode != null) {

            // If the dates are in-between the ranges
            if ((startDate.before(currentNode.endDate))
                    && (endDate.after(currentNode.startDate))) {
                return true;
            }

            // update the current node, traverse down the right sub tree
            if ((currentNode.leftSubTree != null)
                    && (currentNode.leftSubTree.maxEndDate.after(startDate))) {
                currentNode = currentNode.leftSubTree;
            } else {
                currentNode = currentNode.rightSubTree;
            }
        }
        return false;
    }

}