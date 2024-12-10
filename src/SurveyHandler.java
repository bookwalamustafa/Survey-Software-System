import java.io.File;
import java.util.*;

import utils.FileUtils;
import utils.SerializationHelper;

public class SurveyHandler {
	public static String userProvidedSurveyName;
	public static String userProvidedTestName;
	private Survey currentSurvey;
	private Test currentTest;
	private Response currentResponse;
	private Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		SurveyHandler handler = new SurveyHandler();
		handler.run();
	}

	private void showMainMenu() {
		System.out.println("\nMain Menu:");
		System.out.println("1) Survey");
		System.out.println("2) Test");
		System.out.println("3) Quit");
	}

	public void run() {
		boolean exit = false;
		while (!exit) {
			showMainMenu();
			int choice = getIntInput("Enter your choice: ");
			switch (choice) {
			case 1:
				surveyMenu();
				break;
			case 2:
				testMenu();
				break;
			case 3:
				exit = true;
				System.out.println("Thank you for using the Survey/Test System!");
				break;
			default:
				System.out.println("Invalid option. Please try again.");
			}
		}
	}

	private void showSurveyMenu() {
		System.out.println("\nSurvey Menu:");
		System.out.println("1) Create a new Survey");
		System.out.println("2) Display an existing Survey");
		System.out.println("3) Load an existing Survey");
		System.out.println("4) Save the current Survey");
		System.out.println("5) Take the current Survey");
		System.out.println("6) Modify the current Survey");
		System.out.println("7) Tabulate a Survey");
		System.out.println("8) Return to previous menu");
	}

	private void surveyMenu() {
		boolean returnToMain = false;
		while (!returnToMain) {
			showSurveyMenu();
			int choice = getIntInput("Enter your choice: ");
			switch (choice) {
			case 1:
				createSurvey();
				break;
			case 2:
				displaySurvey();
				break;
			case 3:
				loadSurvey();
				break;
			case 4:
				saveSurvey();
				break;
			case 5:
				takeSurvey();
				break;
			case 6:
				modifySurvey();
				break;
			case 7:
				tabulateSurvey();
				break;
			case 8:
				returnToMain = true;
				break;
			default:
				System.out.println("Invalid option. Please try again.");
			}
		}
	}

	private void createSurvey() {
		currentSurvey = new Survey();
		System.out.println("Creating a survey...");

		boolean addingQuestions = true;
		while (addingQuestions) {
			showQuestionTypeMenu();
			int choice = getIntInput("Select a question type: ");
			switch (choice) {
			case 1:
				addTrueFalseQuestion("Survey");
				break;
			case 2:
				addMultipleChoiceQuestion("Survey");
				break;
			case 3:
				addShortAnswerQuestion("Survey");
				break;
			case 4:
				addEssayQuestion("Survey");
				break;
			case 5:
				addDateQuestion("Survey");
				break;
			case 6:
				addMatchingQuestion("Survey");
				break;
			case 7:
				addingQuestions = false;
				System.out.println("Returning to the main menu.");
				break;
			default:
				System.out.println("Invalid option. Please try again.");
			}
		}
	}

	private void showQuestionTypeMenu() {
		System.out.println("\nAdd a New Question:");
		System.out.println("1) True/False");
		System.out.println("2) Multiple Choice");
		System.out.println("3) Short Answer");
		System.out.println("4) Essay");
		System.out.println("5) Date");
		System.out.println("6) Matching");
		System.out.println("7) Return to previous menu");
	}

	private void addTrueFalseQuestion(String Survey_or_Test) {
		Scanner scanner = new Scanner(System.in);
		String prompt;

		while (true) {
			System.out.print("Enter the prompt for your True/False question: ");
			prompt = scanner.nextLine();

			if (prompt.isEmpty()) {
				System.out.println("Error: Question cannot be empty.");
			} else if (prompt.matches("[0-9]+")) {
				System.out.println("Error: Question cannot contain only numbers.");
			} else if (prompt.matches("[-+*/%=]")) {
				System.out.println("Error: Question cannot contain '-', '+', '*', '/', '%', or '=' characters.");
			} else {
				break;
			}
		}

		TrueFalse question = new TrueFalse(prompt);

		if (Survey_or_Test.equals("Survey")) {
			currentSurvey.addQuestion(question);
		} else {
			String correctAnswer;
			while (true) {
				System.out.print("Enter the correct response (T/F): ");
				correctAnswer = scanner.nextLine().trim();

				if (correctAnswer.equalsIgnoreCase("T") || correctAnswer.equalsIgnoreCase("F")) {
					currentTest.addQuestion(question, correctAnswer);
					break;
				} else {
					System.out.println("Error: Invalid input. Please enter 'T' for True or 'F' for False.");
				}
			}
		}

		System.out.println("True/False question added.");
	}

	private void addMultipleChoiceQuestion(String Survey_or_Test) {
		Scanner scanner = new Scanner(System.in);
		String prompt;

		while (true) {
			System.out.print("Enter the prompt for your Multiple Choice question: ");
			prompt = scanner.nextLine();

			if (prompt.isEmpty()) {
				System.out.println("Error: Question cannot be empty.");
			} else if (prompt.matches("[0-9]+")) {
				System.out.println("Error: Question cannot contain only numbers.");
			} else if (prompt.matches("[-+*/%=]")) {
				System.out.println("Error: Question cannot be '-', '+', '*', '/', '%', or '=' character.");
			} else {
				break;
			}
		}

		int maxAnswers = getIntInput("Enter the number of options allowed: ");
		while (maxAnswers < 1) {
			System.out.println("Error: Number of options must be at least 1.");
			maxAnswers = getIntInput("Enter the number of options allowed: ");
		}
		prompt = prompt + "(" + maxAnswers + " Options)";
		MultipleChoiceQuestion question = new MultipleChoiceQuestion(prompt);

		int numChoices = getIntInput("Enter the number of choices: ");
		while (numChoices < maxAnswers) {
			System.out.println(
					"Error: Number of choices must be at least equal to the number of correct answers allowed.");
			numChoices = getIntInput("Enter the number of choices: ");
		}

		question.setMaxAnswers(maxAnswers);

		for (int i = 1; i <= numChoices; i++) {
			String choice;
			while (true) {
				System.out.print("Enter choice #" + i + ": ");
				choice = scanner.nextLine();
				if (choice.trim().isEmpty()) {
					System.out.println("Choice cannot be empty. Please enter a valid choice.");
				} else {
					break;
				}
			}
			question.addChoice(choice);
		}

		if (Survey_or_Test.equals("Survey")) {
			currentSurvey.addQuestion(question);
		} else {
			String correctAnswers;
			while (true) {
				if (maxAnswers == 1) {
					System.out.print("Enter the correct answer (e.g., A): ");
				} else {
					System.out.print("Enter the correct answers (up to " + maxAnswers
							+ " letters, separated by space, e.g., A B): ");
				}
				correctAnswers = scanner.nextLine().trim().toUpperCase();
				Set<Character> validOptions = new HashSet<>();
				char optionChar = 'A';
				for (int i = 0; i < numChoices; i++) {
					validOptions.add(optionChar);
					optionChar++;
				}
				if (question.isValidChoice(correctAnswers, validOptions)) {
					break;
				}
			}
			question.setCorrectAnswers(correctAnswers);
			currentTest.addQuestion(question, correctAnswers);
		}
		System.out.println("Multiple Choice question added.");
	}

	private void addShortAnswerQuestion(String Survey_or_Test) {
		Scanner scanner = new Scanner(System.in);
		String prompt;

		while (true) {
			System.out.print("Enter the prompt for your Short Answer question: ");
			prompt = scanner.nextLine();

			if (prompt.isEmpty()) {
				System.out.println("Error: Question cannot be empty.");
			} else if (prompt.matches("[0-9]+")) {
				System.out.println("Error: Question cannot contain only numbers.");
			} else if (prompt.matches("[-+*/%=]")) {
				System.out.println("Error: Question cannot be '-', '+', '*', '/', '%', or '=' character.");
			} else {
				break;
			}
		}

		int maxChars;

		while (true) {
			maxChars = getIntInput("Enter the maximum allowed characters: ");

			if (maxChars > 0) {
				break;
			} else {
				System.out.println("Error: Maximum allowed characters must be a positive integer.");
			}
		}

		ShortAnswer question = new ShortAnswer(prompt, maxChars);

		if (Survey_or_Test.equals("Survey")) {
			currentSurvey.addQuestion(question);
		} else {
			String correctAnswer;
			while (true) {
				System.out.print("Enter the correct answer (Max " + maxChars + " characters): ");
				correctAnswer = scanner.nextLine().trim();

				if (correctAnswer.isEmpty()) {
					System.out.println("Error: Correct answer cannot be empty.");
				} else if (correctAnswer.length() > maxChars) {
					System.out.println(
							"Error: Correct answer exceeds the maximum allowed characters (" + maxChars + ").");
				} else {
					break;
				}
			}

			currentTest.addQuestion(question, correctAnswer);
		}

		System.out.println("Short Answer question added.");
	}

	private void addEssayQuestion(String Survey_or_Test) {
		Scanner scanner = new Scanner(System.in);
		String prompt;

		while (true) {
			System.out.print("Enter the prompt for your Essay question: ");
			prompt = scanner.nextLine();

			if (prompt.isEmpty()) {
				System.out.println("Error: Question cannot be empty.");
			} else if (prompt.matches("[0-9]+")) {
				System.out.println("Error: Question cannot contain only numbers.");
			} else if (prompt.matches("[-+*/%=]")) {
				System.out.println("Error: Question cannot be '-', '+', '*', '/', '%', or '=' character.");
			} else {
				break;
			}
		}

		Essay question = new Essay(prompt);
		if (Survey_or_Test == "Survey") {
			currentSurvey.addQuestion(question);
		} else {
			String correctAnswer;
			while (true) {
				System.out.print("Enter the correct answer: ");
				correctAnswer = scanner.nextLine().trim();

				if (correctAnswer.isEmpty()) {
					System.out.println("Error: Correct answer cannot be empty.");
				} else {
					break;
				}
			}

			currentTest.addQuestion(question, correctAnswer);
		}
		System.out.println("Essay question added.");
	}

	private void addDateQuestion(String Survey_or_Test) {
		Scanner scanner = new Scanner(System.in);
		String prompt;

		while (true) {
			System.out.print("Enter the prompt for your Date question: ");
			prompt = scanner.nextLine();

			if (prompt.isEmpty()) {
				System.out.println("Error: Question cannot be empty.");
			} else if (prompt.matches("[0-9]+")) {
				System.out.println("Error: Question cannot contain only numbers.");
			} else if (prompt.matches("[-+*/%=]")) {
				System.out.println("Error: Question cannot be '-', '+', '*', '/', '%', or '=' character.");
			} else {
				break;
			}
		}

		ValidDate question = new ValidDate(prompt);

		if (Survey_or_Test.equals("Survey")) {
			currentSurvey.addQuestion(question);
		} else {
			String correctDate;
			while (true) {
				System.out.print("Enter the correct date (in YYYY-MM-DD format): ");
				correctDate = scanner.nextLine().trim();

				if (correctDate.isEmpty()) {
					System.out.println("Error: Correct date cannot be empty.");
				} else if (!question.isValidDate(correctDate)) {
					System.out.println("Error: Please enter a valid date in YYYY-MM-DD format.");
				} else {
					break;
				}
			}

			currentTest.addQuestion(question, correctDate);
		}
		System.out.println("Date question added.");
	}

	private void addMatchingQuestion(String Survey_or_Test) {
		Scanner scanner = new Scanner(System.in);
		String prompt;

		while (true) {
			System.out.print("Enter the prompt for your Matching question: ");
			prompt = scanner.nextLine();

			if (prompt.isEmpty()) {
				System.out.println("Error: Question cannot be empty.");
			} else if (prompt.matches("[0-9]+")) {
				System.out.println("Error: Question cannot contain only numbers.");
			} else if (prompt.matches("[-+*/%=]")) {
				System.out.println("Error: Question cannot be '-', '+', '*', '/', '%', or '=' character.");
			} else {
				break;
			}
		}

		Matching question = new Matching(prompt);

		int numPairs;
		while (true) {
			numPairs = getIntInput("Enter the number of pairs: ");

			if (numPairs > 1) {
				break;
			} else {
				System.out.println(
						"Error: A matching question must have more than one pair. Please enter a valid number.");
			}
		}

		for (int i = 1; i <= numPairs; i++) {
			String questionItem;
			while (true) {
				System.out.print("Enter item #" + i + " for the left-hand side: ");
				questionItem = scanner.nextLine().trim();

				if (!questionItem.isEmpty()) {
					break;
				} else {
					System.out.println("Input cannot be empty. Please enter a valid question prompt.");
				}
			}

			String answerItem;
			while (true) {
				System.out.print("Enter item #" + i + " for the right-hand side: ");
				answerItem = scanner.nextLine().trim();

				if (!answerItem.isEmpty()) {
					break;
				} else {
					System.out.println("Input cannot be empty. Please enter a valid answer prompt.");
				}
			}

			question.addPair(questionItem, answerItem);
		}

		if (Survey_or_Test.equals("Survey")) {
			currentSurvey.addQuestion(question);
		} else {
			System.out.println("Enter the correct matches for the question:");
			String correctMatches = question.getResponse();

			currentTest.addQuestion(question, correctMatches);
		}

		System.out.println("Matching question added.");
	}

	private void displaySurvey() {
		if (currentSurvey == null) {
			System.out.println("You must have a survey loaded in order to display it.");
		} else {
			currentSurvey.displaySurvey();
		}
	}

	private void loadSurvey() {
		try {
			currentSurvey = Survey.loadSurvey();

			if (currentSurvey != null) {
				userProvidedSurveyName = currentSurvey.getUserProvidedSurveyName();
				System.out.println("Survey loaded successfully: " + userProvidedSurveyName);
			} else {
				System.out.println("No survey to load.");
			}
		} catch (IllegalStateException e) {
			System.out.println("Error loading survey: " + e.getMessage());
		}
	}

	private void saveSurvey() {
		if (currentSurvey == null) {
			System.out.println("You must have a survey loaded in order to save it.");
		} else {
			System.out.print("Enter a name to save the survey (leave blank for auto-generated name): ");
			scanner.nextLine();
			userProvidedSurveyName = scanner.nextLine().trim();
			currentSurvey.saveSurvey(currentSurvey, userProvidedSurveyName);
		}
	}

	private void takeSurvey() {
		if (currentSurvey == null) {
			System.out.println("You must have a survey loaded in order to take it.");
		} else {
			currentSurvey.takeSurvey(userProvidedSurveyName);
		}
	}

	private void modifySurvey() {
		if (currentSurvey == null) {
			System.out.println("You must have a survey loaded in order to modify it.");
		} else {
			currentSurvey.modifySurvey();
		}
	}

	private void tabulateSurvey() {
		if (currentSurvey == null) {
			System.out.println("You must have a survey loaded to tabulate responses.");
		} else {
			currentSurvey.tabulateSurvey(userProvidedSurveyName);
		}
	}

	private void showTestMenu() {
		System.out.println("\nTest Menu:");
		System.out.println("1) Create a new Test");
		System.out.println("2) Display an existing Test without correct answers");
		System.out.println("3) Display an existing Test with correct answers");
		System.out.println("4) Load an existing Test");
		System.out.println("5) Save the current Test");
		System.out.println("6) Take the current Test");
		System.out.println("7) Modify the current Test");
		System.out.println("8) Tabulate a Test");
		System.out.println("9) Grade a Test");
		System.out.println("10) Return to the previous menu");
	}

	private void testMenu() {
		boolean returnToMain = false;
		while (!returnToMain) {
			showTestMenu();
			int choice = getIntInput("Enter your choice: ");
			switch (choice) {
			case 1:
				createTest();
				break;
			case 2:
				displayTest(false);
				break;
			case 3:
				displayTest(true);
				break;
			case 4:
				loadTest();
				break;
			case 5:
				saveTest();
				break;
			case 6:
				takeTest();
				break;
			case 7:
				modifyTest();
				break;
			case 8:
				tabulateTest();
				break;
			case 9:
				gradeTest();
				break;
			case 10:
				returnToMain = true;
				break;
			default:
				System.out.println("Invalid option. Please try again.");
			}
		}
	}

	private void createTest() {
		currentTest = new Test();
		System.out.println("Creating a Test...");
		boolean addingQuestions = true;

		while (addingQuestions) {
			showQuestionTypeMenu();
			int choice = getIntInput("Select a question type: ");
			switch (choice) {
			case 1:
				addTrueFalseQuestion("Test");
				break;
			case 2:
				addMultipleChoiceQuestion("Test");
				break;
			case 3:
				addShortAnswerQuestion("Test");
				break;
			case 4:
				addEssayQuestion("Test");
				break;
			case 5:
				addDateQuestion("Test");
				break;
			case 6:
				addMatchingQuestion("Test");
				break;
			case 7:
				addingQuestions = false;
				System.out.println("Returning to the main menu.");
				break;
			default:
				System.out.println("Invalid option. Please try again.");
			}
		}
	}

	private void displayTest(boolean showAnswers) {
		if (currentTest == null) {
			System.out.println("You must have a test loaded in order to display it.");
		} else {
			currentTest.displayTest(showAnswers);
		}
	}

	private void loadTest() {
		try {
			currentTest = Test.loadTest();

			if (currentTest != null) {
				System.out.println("Test loaded successfully.");
				userProvidedTestName = currentTest.getUserProvidedTestName();
				System.out.println("Test file name: " + userProvidedTestName);
			} else {
				System.out.println("No test to load.");
			}
		} catch (IllegalStateException e) {
			if (e.getMessage() != null && e.getMessage().contains("Test is empty.")) {
				System.out.println("No test to load.");
			} else {
				System.out.println("No test to load.");
			}
		}
	}

	private void saveTest() {
		if (currentTest == null) {
			System.out.println("You must have a test loaded in order to save it.");
		} else {

			System.out.print("Enter a name to save the test (leave blank for auto-generated name): ");
			scanner.nextLine();
			userProvidedTestName = scanner.nextLine().trim();
			currentTest.saveTest(currentTest, userProvidedTestName);
		}
	}

	private void takeTest() {
		if (currentTest == null) {
			System.out.println("You must have a test loaded in order to take it.");
		} else {
			currentTest.takeTest(userProvidedTestName);
		}
	}

	private void modifyTest() {
		if (currentTest == null) {
			System.out.println("You must have a test loaded in order to modify it.");
		} else {
			currentTest.modifyTest();
		}
	}

	private void tabulateTest() {
		if (currentTest == null) {
			System.out.println("You must have a test loaded to tabulate responses.");
		} else {
			currentTest.tabulateTest(userProvidedTestName);
		}
	}

	private void gradeTest() {
		System.out.println("Select an existing test to grade:");
		String testFile = FileUtils.listAndPickFileFromDir("Test" + File.separator);
		if (testFile == null || testFile.isEmpty()) {
			System.out.println("No test selected or available.");
			return;
		}

		String testName = new File(testFile).getName();
		if (testName.contains("_")) {
			testName = testName.split("_")[0];
		}
		userProvidedTestName = testName;
		loadTest();

		Test testToGrade = SerializationHelper.deserialize(Test.class, testFile);
		if (testToGrade == null) {
			System.out.println("Unable to load the selected test.");
			return;
		}

		String basePath = "Response";
		File responseDir = new File(basePath);

		if (!responseDir.exists() || !responseDir.isDirectory()) {
			System.out.println("No response directory found.");
			return;
		}

		File[] responseFiles = responseDir.listFiles();
		if (responseFiles == null || responseFiles.length == 0) {
			System.out.println("No responses found.");
			return;
		}

		List<Response> responses = new ArrayList<>();
		for (File file : responseFiles) {
			String fileName = file.getName();
			if (fileName.startsWith("TestResponse_" + testName)) {
				Response response = SerializationHelper.deserialize(Response.class, file.getAbsolutePath());
				if (response != null) {
					responses.add(response);
				}
			}
		}

		if (responses.isEmpty()) {
			System.out.println("No responses found for the test: " + testName);
			return;
		}

		System.out.println("Select a response to grade for test: " + testName);
		for (int i = 0; i < responses.size(); i++) {
			System.out.println((i + 1) + ". " + (currentTest.getRelatedResponseFiles()).get(i));
		}

		int selectedIndex = -1;
		while (selectedIndex < 1 || selectedIndex > responses.size()) {
			System.out.print("Enter the number of the response you want to grade: ");
			try {
				String input = new Scanner(System.in).nextLine();
				selectedIndex = Integer.parseInt(input);
			} catch (NumberFormatException e) {
				selectedIndex = -1;
			}
			if (selectedIndex < 1 || selectedIndex > responses.size()) {
				System.out.println("Invalid selection. Please try again.");
			}
		}

		Response responseToGrade = responses.get(selectedIndex - 1);

		List<Question> questions = testToGrade.getQuestions();
		List<String> correctAnswers = testToGrade.getCorrectAnswers();
		List<String> userAnswers = responseToGrade.getAnswers();

		if (questions.size() != correctAnswers.size() || questions.size() != userAnswers.size()) {
			System.out.println("Mismatch in number of questions, correct answers, or user responses.");
			return;
		}

		int totalQuestions = questions.size();
		int essayCount = 0;
		int autoGradableCorrect = 0;
		int autoGradableCount = 0;

		List<Integer> incorrectIndices = new ArrayList<>();

		for (int i = 0; i < totalQuestions; i++) {
			Question q = questions.get(i);
			String correctAns = correctAnswers.get(i);
			String userAns = userAnswers.get(i);

			if (q instanceof Essay) {
				essayCount++;
			} else {
				autoGradableCount++;
				if (userAns.trim().equalsIgnoreCase(correctAns.trim())) {
					autoGradableCorrect++;
				} else {
					incorrectIndices.add(i);
				}
			}
		}

		double pointsPerQuestion = 100.0 / totalQuestions;
		double autoGradablePoints = autoGradableCount * pointsPerQuestion;

		double finalGrade = 0.0;
		if (autoGradableCount > 0) {
			double fraction = (double) autoGradableCorrect / autoGradableCount;
			finalGrade = fraction * autoGradablePoints;
		}

		System.out.println("\nGrading Response: " + currentTest.getUserProvidedTestName());
		if (essayCount > 0) {
			System.out.println("You received a " + (int) finalGrade + " on the test. The test was worth 100 points, "
					+ "but only " + (int) autoGradablePoints + " of those points could be auto graded because there "
					+ (essayCount == 1 ? "was one essay question." : "were " + essayCount + " essay questions."));
		} else {
			System.out.println("You received a " + (int) finalGrade + " on the test out of 100 points.");
		}

		if (!incorrectIndices.isEmpty()) {
			System.out.println("\nIncorrect Answers:");
			for (int idx : incorrectIndices) {
				Question q = questions.get(idx);
				String correctAns = correctAnswers.get(idx);
				String userAns = userAnswers.get(idx);
				System.out.println("Question " + (idx + 1) + ":");
				System.out.println("   Your answer: " + userAns);
				System.out.println("   Correct answer: " + correctAns);
			}
		}

	}

	private int getIntInput(String prompt) {
		System.out.print(prompt);
		while (!scanner.hasNextInt()) {
			System.out.print("Please enter a valid number: ");
			scanner.next();
		}
		return scanner.nextInt();
	}

}
