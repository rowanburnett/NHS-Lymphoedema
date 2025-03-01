package uk.ac.cf.spring.nhs.UserTask.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.cf.spring.nhs.Common.util.BitmaskUtility;
import uk.ac.cf.spring.nhs.Common.util.DateUtils;
import uk.ac.cf.spring.nhs.Task.Model.Task;
import uk.ac.cf.spring.nhs.UserTask.Model.UserTask;
import uk.ac.cf.spring.nhs.UserTask.Repository.JpaUserTaskRepository;
import uk.ac.cf.spring.nhs.UserTaskLog.Model.UserTaskLog;
import uk.ac.cf.spring.nhs.UserTaskLog.Service.UserTaskLogService;

@Service
public class UserTaskService {

    @Autowired
    private JpaUserTaskRepository userTaskRepository;

    @Autowired
    private UserTaskLogService userTaskLogService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void toggleTaskCompletion(UserTask userTask) {
        System.out.println("toggleTaskCompletion called for UserTask ID: " + userTask.getId() + " from "
                + new Exception().getStackTrace()[1]);

        int currentDay = DateUtils.getCurrentDayOfMonth();
        System.out.println("Current day according to toggleTaskCompletion: " + currentDay);
        int updatedBitmask;

        if (BitmaskUtility.isBitSet(userTask.getBitmask(), currentDay)) {
            updatedBitmask = BitmaskUtility.clearBit(userTask.getBitmask(), currentDay);
            System.out.println("Marked task as incomplete. UserTask ID: " + userTask.getId() + ", Updated Bitmask: "
                    + Integer.toBinaryString(updatedBitmask));
        } else {
            updatedBitmask = BitmaskUtility.setBit(userTask.getBitmask(), currentDay);
            System.out.println("Marked task as completed. UserTask ID: " + userTask.getId() + ", Updated Bitmask: "
                    + Integer.toBinaryString(updatedBitmask));
        }

        userTask.setBitmask(updatedBitmask);
        userTaskRepository.save(userTask);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public boolean isTaskCompleted(UserTask userTask) {
        int currentDay = DateUtils.getCurrentDayOfMonth();
        System.out.println("Current day according to isTaskCompleted: " + currentDay);
        return BitmaskUtility.isBitSet(userTask.getBitmask(), currentDay);
    }

    /**
     * Archives the current month's bitmask for a specific {@link UserTask} and
     * resets the bitmask for the new month.
     * 
     * <p>
     * This method is intended to be called at the end of each month. It first
     * archives the current bitmask of the given
     * {@link UserTask} by saving it to a new {@link UserTaskLog} entry, which
     * stores the task's completion history for that month.
     * The method then resets the bitmask to zero (0) to prepare the
     * {@link UserTask} for tracking task completions in the new month.
     * </p>
     * 
     * <p>
     * For example, if the bitmask for a task at the end of June is
     * "00000000000000000000000000011111" (binary for 31),
     * this bitmask will be saved in the {@link UserTaskLog} with the associated
     * month and year (e.g., "2024-06").
     * After archiving, the bitmask in the {@link UserTask} is reset to
     * "00000000000000000000000000000000" (binary for 0),
     * ready to track task completions in July.
     * </p>
     * 
     * @param userTask The {@link UserTask} entity whose bitmask is to be archived
     *                 and reset.
     * 
     *                 <h3>Example Usage:</h3>
     * 
     *                 <pre>{@code
     * // Retrieve the UserTask object using the service
     * UserTask userTask = userTaskService.getUserTaskById(1L);
     * 
     * // Archive the current month's bitmask and reset it for the new month
     * userTaskService.archiveAndResetMonthlyBitmask(userTask);
     * 
     * // The UserTask's bitmask will now be archived, and the bitmask will be reset to zero (0) for the new month.
     * }</pre>
     */
    @Transactional
    public void archiveAndResetMonthlyBitmask(UserTask userTask) {
        String monthYear = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        UserTaskLog log = new UserTaskLog();
        log.setUserID(userTask.getUserID());
        log.setUserTask(userTask);
        log.setBitmask(userTask.getBitmask());
        log.setMonthYear(monthYear);
        log.setCreatedAt(LocalDateTime.now());

        userTaskLogService.createUserTaskLog(log);
        userTask.setBitmask(BitmaskUtility.resetBitmask());
        userTaskRepository.save(userTask);
    }

    /**
     * Counts the number of completed tasks for a specific day for a given user.
     *
     * @param userId the ID of the user
     * @param day    the day of the month (0-indexed)
     * @return the number of completed tasks for the specified day
     */
    @Transactional(readOnly = true)
    public int countCompletedTasksForday(Long userId, int day) {
        List<UserTask> userTasks = userTaskRepository.findByUserID(userId);
        int count = 0;
        for (UserTask userTask : userTasks) {
            if (BitmaskUtility.isBitSet(userTask.getBitmask(), day)) {
                count++;
            }
        }
        System.out.println("Count of completed tasks for user " + userId + " on day " + day + " is: " + count);
        return count;
    }

    // CRUD functions

    /**
     * Assigns a task to a user by saving the user task details in the database.
     *
     * @param userTask the user task details to be saved
     * @return the saved user task details
     */

    public UserTask assignTaskToUser(Task task, Long userId) {
        UserTask userTask = new UserTask();
        userTask.setTask(task);
        userTask.setUserID(userId);
        return userTaskRepository.save(userTask);
    }

    /**
     * 
     * Retrieves a list of user tasks associated with the specified user ID.
     *
     * @param userID the ID of the user to retrieve tasks for
     * @return a list of user tasks for the specified user
     */
    @Transactional(readOnly = true)
    public List<UserTask> getTasksForUser(Long userID) {
        return userTaskRepository.findByUserID(userID); // TODO: add exception handling
    }

    /**
     * Retrieves a user task by its ID from the database.
     *
     * @param userTaskID the ID of the user task to retrieve
     * @return the user task with the specified ID, or throws an exception if not
     *         found
     */
    @Transactional(readOnly = true)
    public UserTask getUserTaskById(Long userTaskID) {
        return userTaskRepository.findById(userTaskID)
                .orElseThrow(() -> new NoSuchElementException("UserTask not found"));
    }

    /**
     * Updates a user task with the specified task details.
     *
     * @param userTaskID      the ID of the user task to update
     * @param userTaskDetails the updated task details
     * @return the updated user task
     */
    public UserTask updateUserTask(Long userTaskID, UserTask userTaskDetails) {
        UserTask userTask = getUserTaskById(userTaskID);
        userTask.setTask(userTaskDetails.getTask());
        userTask.setUserID(userTaskDetails.getUserID());
        userTask.setBitmask(userTaskDetails.getBitmask());
        return userTaskRepository.save(userTask); // TODO: should this return anything?
    }

    @Transactional
    public List<UserTask> updateUserTasksBatch(List<UserTask> userTasks) {
        for (UserTask userTask : userTasks) {
            userTaskRepository.save(userTask);
        }
        return userTasks;
    }

    /**
     * Deletes a user task from the database.
     *
     * @param userTaskID the ID of the user task to delete
     */
    public void deleteUserTask(Long userTaskID) {
        UserTask userTask = getUserTaskById(userTaskID);
        userTaskRepository.delete(userTask); // TODO: add exception handling
    }

    public void deleteAllTasksForUser(Long userId){
        List<UserTask> allTasks = getTasksForUser(userId);
        for(UserTask task: allTasks){
            deleteUserTask(task.getId());
        }
    }
}
