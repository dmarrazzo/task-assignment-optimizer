package app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.Employee;
import domain.Skill;
import domain.Task;

public class CsvReaderBuilder {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(CsvReaderBuilder.class);

	@SuppressWarnings("resource")
	public static List<Employee> readEmployees(final String employeeFile) {
		// We recover the path where the file is stored from its name
		final Path inputFile = Paths.get(employeeFile);

		try {
			// Reading all the lines of the file
			final Stream<String> stream = Files.lines(inputFile);

			// We filter all lines in order to retrieve a line that is not empty
			// and whose first character is equivalent to '!'
			final List<String[]> categories = stream.filter((line) -> !line.isEmpty() && line.startsWith("!"))
					.map((line) -> {
						// We break a line, represented in a string, around the
						// matches of the given regular expression
						final String[] tokens = line.split(";");
						// Initialization of the size of the array that must
						// contain categories
						final String[] categoryArr = new String[tokens.length - 3];

						// We recover all the categories contained in the
						// treated line
						// The first three elements of the line are not
						// categories, hence the recovery of categories starts
						// from the 4th element
						for (int i = 3, j = 0; i < tokens.length; i++, j++) {
							categoryArr[j] = tokens[i];
						}
						return categoryArr;
					}).collect(Collectors.toList());

			// Replay all lines in the file
			final Stream<String> stream1 = Files.lines(inputFile);

			// We filter all lines and we recover those which are not empty and
			// which do not begin with the character '#' or '!'
			final List<Employee> employees = stream1
					.filter((line) -> !line.isEmpty() && !line.startsWith("#") && !line.startsWith("!")).map((line) -> {
						// We break a line, represented in a string, around the
						// matches of the given regular expression
						final String[] tokens = line.split(";");
						if (tokens.length < 3) {
							throw new IllegalStateException("The line (" + line + ") has less than 3 tokens.");
						}

						final Set<Skill> skills = new TreeSet<Skill>();

						// Skills objects are built from the categories
						// retrieved above with the priorities contained in the
						// current row
						// Categories are those stored in the categories
						// variable
						// Priorities are extracted in the row being processed.
						// The first three data in the row do not match the
						// priorities.
						for (int i = 3, j = 0; i < tokens.length && j < categories.get(0).length; i++, j++) {
							// If the employee has the skill (ie, the skill
							// priority is not 0 )
							if (!"0".equals(tokens[i])) {
								skills.add(new Skill(categories.get(0)[j], Integer.valueOf(tokens[i])));
							}
						}
						return new Employee(tokens[0], LocalTime.parse(tokens[1]), LocalTime.parse(tokens[2]), skills);

					}).collect(Collectors.toList());

			return employees;
		} catch (final IOException e) {
			throw new IllegalStateException("Reading inputFile (" + inputFile + ") failed.", e);
		}
	}

	@SuppressWarnings("resource")
	public static List<Task> readTasks(final String taskFile) {
		// We recover the path where the file is stored from its name
		final Path inputFile = Paths.get(taskFile);

		try {
			// Reading all the lines of the file
			final Stream<String> stream = Files.lines(inputFile);

			final List<Task> tasks = stream.filter((line) -> !line.isEmpty() && !line.startsWith("#")).map((line) -> {
				try {
					// We break a line, represented in a string, around the
					// matches of the given regular expression
					final String[] tokens = line.split(";", -1);
					if (tokens.length < 4) {
						throw new IllegalStateException("The line (" + line + ") has less than 4 tokens.");
					}

					// Retrieving the taskDuration value
					Float taskDuration = null;
					if (StringUtils.isNotBlank(tokens[3])) {
						taskDuration = floatConverter(tokens[3]);
					}

					// Retrieving the priority value
					Integer priority = 0;
					if (StringUtils.isNotBlank(tokens[4])) {
						priority = Integer.valueOf(tokens[4]);
					}

					// Retrieving the time constraint value
					LocalTime timeConstraint = null;
					if (StringUtils.isNotBlank(tokens[5])) {
						timeConstraint = LocalTime.parse(tokens[5]);
					}

					// Max number of parts the task can be divided into
					Integer maxParts = 1;
					if (StringUtils.isNotBlank(tokens[7])) {
						maxParts = Integer.valueOf(tokens[7]);
					}

					return new Task(tokens[0], tokens[1], taskDuration, priority, timeConstraint, maxParts);
				} catch (NumberFormatException | ParseException e) {
					LOG.debug("NumberFormatException or ParseException ", e);
				}
				return new Task();
			}).collect(Collectors.toList());

			return tasks;
		} catch (final IOException e) {
			throw new IllegalStateException("Reading inputFile (" + inputFile + ") failed.", e);
		}
	}

	/**
	 * Returns a float from a string containing comma like decimal separator.
	 * 
	 * @param data
	 *            data
	 * @return a float
	 * @throws ParseException
	 *             parseException
	 */
	private static Float floatConverter(final String data) throws ParseException {
		try {
			final DecimalFormatSymbols dfs = new DecimalFormatSymbols();
			dfs.setDecimalSeparator(',');
			final DecimalFormat df = new DecimalFormat("");
			df.setDecimalFormatSymbols(dfs);
			Number number;
			number = df.parse(data);
			return number.floatValue();
		} catch (final ParseException e) {
			LOG.error("Unparseable data: " + data, e);
			throw new ParseException("ParseException", e.getErrorOffset());
		}
	}

}
