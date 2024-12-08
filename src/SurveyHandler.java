import java.util.*;

public class SurveyHandler {
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
				tabulateSurvey(); // To be implemented in later steps
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
		scanner.nextLine();
		while (maxAnswers < 1) {
			System.out.println("Error: Number of options must be at least 1.");
			maxAnswers = getIntInput("Enter the number of options allowed: ");
			scanner.nextLine();
		}
		prompt = prompt + "(" + maxAnswers + " Options)";
		MultipleChoiceQuestion question = new MultipleChoiceQuestion(prompt);

		int numChoices = getIntInput("Enter the number of choices: ");
		scanner.nextLine();
		while (numChoices < maxAnswers) {
			System.out.println(
					"Error: Number of choices must be at least equal to the number of correct answers allowed.");
			numChoices = getIntInput("Enter the number of choices: ");
			scanner.nextLine();
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

		System.out.println("Short Answer question added with correct answer.");
	}

	private void addEssayQuestion(String Survey_or_Test) {
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
					// Reuse the isValidDate method to validate the date format and values
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
		String prompt;

		// Prompt input with validation
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

		// Input the number of pairs with validation
		int numPairs;
		while (true) {
			numPairs = getIntInput("Enter the number of pairs: ");
			scanner.nextLine();

			if (numPairs > 1) {
				break;
			} else {
				System.out.println(
						"Error: A matching question must have more than one pair. Please enter a valid number.");
			}
		}

		// Collect pairs
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

		// Survey or test-specific behavior
		if (Survey_or_Test.equals("Survey")) {
			currentSurvey.addQuestion(question);
		} else {
			System.out.println("Enter the correct matches for the question:");

			List<String> leftItems = new ArrayList<>(question.getPairs().keySet());
			List<String> rightItems = new ArrayList<>(question.getPairs().values());
			Map<String, String> correctMatches = new HashMap<>();
			Set<String> usedAnswers = new HashSet<>();

			for (String leftItem : leftItems) {
				while (true) {
					System.out.print("Enter the matching right-hand side item for '" + leftItem + "': ");
					String answer = scanner.nextLine().trim();

					if (rightItems.contains(answer) && !usedAnswers.contains(answer)) {
						correctMatches.put(leftItem, answer);
						usedAnswers.add(answer);
						break;
					} else if (usedAnswers.contains(answer)) {
						System.out.println("Error: This answer has already been used. Please select a different one.");
					} else {
						System.out.println("Error: Invalid answer. Please select from: " + rightItems);
					}
				}
			}

			currentTest.addQuestion(question, String.valueOf(correctMatches));
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
				System.out.println("Survey loaded successfully.");
			} else {
				System.out.println("No survey to load.");
			}
		} catch (IllegalStateException e) {
			if (e.getMessage() != null && e.getMessage().contains("Survey is empty.")) {
				System.out.println("No survey to load.");
			} else {
				System.out.println("No survey to load.");
			}
		}
	}

	private void saveSurvey() {
		if (currentSurvey == null) {
			System.out.println("You must have a survey loaded in order to save it.");
		} else {
			scanner.nextLine();
			currentSurvey.saveSurvey(currentSurvey, "Survey");
		}
	}

	private void takeSurvey() {
		if (currentSurvey == null) {
			System.out.println("You must have a survey loaded in order to take it.");
		} else {
			currentSurvey.takeSurvey();

			if (currentResponse == null) {
				currentResponse = new Response();
			}

			currentResponse.saveResponse(currentResponse, "Response");
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
			System.out.println("You must have a survey loaded in order to tabulate it.");
		} else {
			currentSurvey.tabulateSurvey();
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
				displayTest(false); // Display without correct answers
				break;
			case 3:
				displayTest(true); // Display with correct answers
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
				tabulateTest(); // To be implemented in later steps
				break;
			case 9:
				gradeTest(); // To be implemented in later steps
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
		// To be implemented in Step 3
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
			scanner.nextLine();
			currentTest.saveTest(currentTest, "Test");
		}
	}

	private void takeTest() {
		if (currentTest == null) {
			System.out.println("You must have a test loaded in order to take it.");
		} else {
			currentTest.takeTest();

			if (currentResponse == null) {
				currentResponse = new Response();
			}

			currentResponse.saveResponse(currentResponse, "Response");
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
		// To be implemented in Step 8
		System.out.println("Tabulate Test functionality is not yet implemented.");
	}

	private void gradeTest() {
		// To be implemented in Step 9
		System.out.println("Grade Test functionality is not yet implemented.");
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
