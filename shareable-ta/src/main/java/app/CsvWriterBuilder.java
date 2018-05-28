package app;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.Employee;
import domain.TaskAssagnmentSolution;
import domain.TaskPart;

public class CsvWriterBuilder {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(CsvWriterBuilder.class);

    /** The Constant DELIMITER. */
    private static final String SEMICOLON_DELIMITER = ";";

    /** The Constant NEW_LINE_SEPARATOR. */
    private static final String NEW_LINE_SEPARATOR = "\n";

    /** The Constant SCHEDULE_TASK_FILE_HEADER. */
    private static final String SCHEDULE_TASK_FILE_HEADER = "Tâche;Employé;Heure de début;Heure de fin";

    /** The Constant SCHEDULE_EMPLOYEE_FILE_HEADER. */
    private static final String SCHEDULE_EMPLOYEE_FILE_HEADER = "Employé;Tâches - Heures de début - Heures de fin";

    /** The Constant EMPTY_STRING. */
    private static final String EMPTY_STRING = "";

    /**
     * Create a schedule by task csv file.
     * 
     * @param fileName
     *            the file name
     */
    public static void writeScheduleByTaskCsvFile(final TaskAssagnmentSolution solution, final String fileName) {
        System.out.println("Write a schedule by task csv file");

        FileWriter fileWriter = null;

        try {
            // Construct a FileWriter object given the file name.
            fileWriter = new FileWriter(fileName);

            // Write the csv file header
            fileWriter.append(SCHEDULE_TASK_FILE_HEADER);

            // Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            final Map<String, List<String>> result = getTaskByEmployeeTaskPart(solution.getEmployeeList());

            // Iteration on
            for (final String key : result.keySet()) {
                // The key is the name of the task
                fileWriter.append(key);
                fileWriter.append(SEMICOLON_DELIMITER);

                final List<String> values = result.get(key);
                for (int i = 0; i < values.size(); i++) {
                    // If there are several employees who perform the same task, the task name is displayed once
                    if (i > 0) {
                        fileWriter.append(EMPTY_STRING);
                        fileWriter.append(SEMICOLON_DELIMITER);
                        fileWriter.append(values.get(i));
                        fileWriter.append(NEW_LINE_SEPARATOR);
                    } else {
                        fileWriter.append(values.get(i));
                        fileWriter.append(NEW_LINE_SEPARATOR);
                    }
                }
            }
            

            fileWriter.append("Score :");
            fileWriter.append(SEMICOLON_DELIMITER);
            fileWriter.append(solution.getScore().toString());
            fileWriter.append(NEW_LINE_SEPARATOR);

            System.out.println("Schedule by task csv file was created succefully !!!");
        } catch (final IOException e) {
            LOG.debug("Error in CsvFileWriter: " + fileName, e);
            throw new IllegalStateException("Writing in file (" + fileName + ") failed.", e);
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (final IOException e) {
                LOG.debug("Error while flushing/closing filewriter: " + fileName, e);
                throw new IllegalStateException("Writing in file (" + fileName + ") failed.", e);
            }
        }
    }

    /**
     * Create a schedule by employee csv file.
     * 
     * @param fileName
     *            the file name
     */
    public static void writeScheduleByEmployeeCsvFile(final TaskAssagnmentSolution solution, final String fileName) {
        System.out.println("Write a schedule by employee csv file");

        FileWriter fileWriter = null;

        try {
            // Construct a FileWriter object given the file name.
            fileWriter = new FileWriter(fileName);

            // Write the csv file header
            fileWriter.append(SCHEDULE_EMPLOYEE_FILE_HEADER);

            // Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            final List<Employee> employees = solution.getEmployeeList();

            for (final Employee employee : employees) {
                // Retrieve the first taskPart of the employee
                TaskPart taskPart = employee.getNextTaskPart();

                // Retrieve an employee's name
                fileWriter.append(employee.getFullName());

                final StringBuffer tasksStr = new StringBuffer();

                while (taskPart != null) {
                    tasksStr.append(SEMICOLON_DELIMITER);
                    tasksStr.append(taskPart.getId());
                    tasksStr.append(SEMICOLON_DELIMITER);
                    tasksStr.append(taskPart.getStartTime());
                    tasksStr.append(SEMICOLON_DELIMITER);
                    tasksStr.append(taskPart.getReadyTime());

                    taskPart = taskPart.getNextTaskPart();
                }
                fileWriter.append(tasksStr);
                fileWriter.append(NEW_LINE_SEPARATOR);
            }

            
            fileWriter.append("Score :");
            fileWriter.append(SEMICOLON_DELIMITER);
            fileWriter.append(solution.getScore().toString());
            fileWriter.append(NEW_LINE_SEPARATOR);
            

            System.out.println("Schedule by employee csv file was created succefully !!!");
        } catch (final IOException e) {
            LOG.debug("Error in CsvFileWriter: " + fileName, e);
            throw new IllegalStateException("Writing in file (" + fileName + ") failed.", e);
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (final IOException e) {
                LOG.debug("Error while flushing/closing filewriter: " + fileName, e);
                throw new IllegalStateException("Writing in file (" + fileName + ") failed.", e);
            }
        }
    }

    /**
     * Returns an treemap containing aisle and category of a task as keys and employees's names and tasks's duration as values
     * 
     * @param employees
     *            the employees
     * @return tasks by employee's taskPart
     */
    private static Map<String, List<String>> getTaskByEmployeeTaskPart(final List<Employee> employees) {
        final Map<String, List<String>> result = new TreeMap<>(new Comparator<String>() {

            @Override
            public int compare(final String s1, final String s2) {
                if (s1.startsWith("R") && !s2.startsWith("R")) {
                    return -1;
                } else if (!s1.startsWith("R") && s2.startsWith("R")) {
                    return 1;
                } else {
                    return s1.compareToIgnoreCase(s2);
                }
            }
        });

        for (final Employee employee : employees) {
            // Retrieve the first taskPart of the employee
            TaskPart taskPart = employee.getNextTaskPart();

            // Fill the map
            while (taskPart != null) {
                // Build the key from the aisle name and the category of the task
                final String key = taskPart.getTask().getAisle() + " > " + taskPart.getTask().getCategory();
                // The value contains the employee's name, the start time and the end time of the task part
                final String value = employee.getFullName() + ";" + taskPart.getStartTime() + ";" + taskPart.getReadyTime();
                if (result.get(key) != null) {
                    result.get(key).add(value);
                } else {
                    final List<String> employeeTaskPart = new ArrayList<>();
                    employeeTaskPart.add(value);
                    result.put(key, employeeTaskPart);
                }

                taskPart = taskPart.getNextTaskPart();
            }
        }
        return result;
    }

}
