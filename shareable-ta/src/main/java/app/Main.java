package app;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

import domain.Interval;
import domain.Skill;
import domain.TaskAssagnmentSolution;

import domain.Employee;
import domain.Task;

public class Main {
	public void solve(final String employees, final String tasks, final String configFile, final boolean save) {
//		SolverFactory<TaskAssagnmentSolution> solverFactory = SolverFactory.createFromXmlResource("main/resources/solver/taskAssignmentSolverConfig.xml");
		//SolverFactory<TaskAssagnmentSolution> solverFactory = SolverFactory.createFromXmlResource("taskAssignmentSolverConfig.xml");
		SolverFactory<TaskAssagnmentSolution> solverFactory = SolverFactory.createFromXmlResource(configFile);
		
		Solver<TaskAssagnmentSolution> solver = solverFactory.buildSolver();

		TaskAssagnmentSolution unsolved = ProblemBuilder.readProblemFacts(String.format("data/employees-%s.csv", employees), String.format("data/tasks-%s.csv", tasks));
		
		
		
		
		printEmployees(unsolved.getEmployeeList());
		printTasks(unsolved.getTaskList());
		
		// Define the a new interval of time where no task can be assigned to an employee => the morning brief 
		Set<Interval> gaps = new HashSet<>();
		Interval brief = new Interval(LocalTime.parse("08:00"), LocalTime.parse("08:30"));
		gaps.add(brief);
		unsolved.setGaps(gaps);

		//Once the CSV file has been loaded, it is saved as XML. We will be able to use it in the benchmark
		ProblemBuilder.save(unsolved, String.format("data/emp-%s-task-%s.xml",employees,tasks));

		
		TaskAssagnmentSolution solved = solver.solve(unsolved);
		ProblemBuilder.printSolution(solved);

        ProblemBuilder.save(solved, String.format("data/emp-%s-task-%s-solved.xml", employees, tasks));
        
        
        

        CsvWriterBuilder.writeScheduleByTaskCsvFile(solved, String.format("data/scheduleTask-emp-%s-task-%s-conf-%s.csv", employees, tasks,configFile));
        CsvWriterBuilder.writeScheduleByEmployeeCsvFile(solved, String.format("data/scheduleEmployee-emp-%s-task-%s-conf-%s.csv", employees, tasks,configFile));
        
    }

	public static void main(String[] args) {
		Main main = new Main();
		boolean save = true;
		if (args.length == 4)
			save=args[3].equalsIgnoreCase("save");
		
		if (args.length >= 2)
			main.solve(args[0], args[1],args[2], save);
		else
			//If no parameter is passed, we solve the biggest problem
			main.solve("26", "149", "solver/taskAssignmentSolverConfig-30-secondes.xml", save);

	}
	
	 /**
     * Affichage dans la sortie standard de la liste des employés passée en paramètres.
     * 
     * @param employees
     *            liste d'employés
     */
    private static void printEmployees(final List<Employee> employees) {
        System.out.println("-----------------------------------------------------------------");
        System.out.println("--------------------------EMPLOYEES------------------------------");
        System.out.println("-----------------------------------------------------------------");
        for (final Employee employee : employees) {
            System.out.print(employee.getFullName() + " | " + employee.getStartTime() + " | " + employee.getReadyTime());
            final Set<Skill> skills = employee.getSkillSet();
            
            for (Skill skill : skills) {
            	System.out.print(" | " + skill.getCategory() + " > " + skill.getPriority());
			} 
            
            System.out.println("");
        }
        System.out.println("-----------------------------------------------------------------");
    }

    /**
     * Affichage dans la sortie standard de la liste des tâches passée en paramètres.
     * 
     * @param tasks
     *            liste de tâches
     */
    private static void printTasks(final List<Task> tasks) {
        System.out.println("-----------------------------------------------------------------");
        System.out.println("-----------------------------TASKS-------------------------------");
        System.out.println("-----------------------------------------------------------------");
        for (final Task task : tasks) {
            System.out.println(task.getAisle() + " | " + task.getCategory() + " | " + task.getEffort() + " | " + task.getPriority() + " | "
                    + task.getCompletionTime() + " | " + task.getMaxParts() );
        }
        System.out.println("-----------------------------------------------------------------");
    }

}
