import java.util.Scanner;

public class SurveyHandler {
	private Survey currentSurvey;
	private Response currentResponse;
	private Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		SurveyHandler handler = new SurveyHandler();
		handler.run();
	}

	public void run() {
		boolean exit = false;
		while (!exit) {
			showMainMenu();
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
				exit = true;
				System.out.println("Thank you for using the Survey System!");
				break;
			default:
				System.out.println("Invalid option. Please try again.");
			}
		}
	}

	private void showMainMenu() {
		System.out.println("\nMain Menu:");
		System.out.println("1) Create a new Survey");
		System.out.println("2) Display an existing Survey");
		System.out.println("3) Load an existing Survey");
		System.out.println("4) Save the current Survey");
		System.out.println("5) Take the current Survey");
		System.out.println("6) Modify the current Survey");
		System.out.println("7) Quit");
	}

	private int getIntInput(String prompt) {
		System.out.print(prompt);
		while (!scanner.hasNextInt()) {
			System.out.print("Please enter a valid number: ");
			scanner.next();
		}
		return scanner.nextInt();
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
				addTrueFalseQuestion();
				break;
			case 2:
				addMultipleChoiceQuestion();
				break;
			case 3:
				addShortAnswerQuestion();
				break;
			case 4:
				addEssayQuestion();
				break;
			case 5:
				addDateQuestion();
				break;
			case 6:
				addMatchingQuestion();
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

	private void addTrueFalseQuestion() {
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
				System.out.println("Error: Question cannot be '-', '+', '*', '/', '%', or '=' character.");
			} else {
				break;
			}
		}

		TrueFalse question = new TrueFalse(prompt);
		currentSurvey.addQuestion(question);
		System.out.println("True/False question added.");
	}

	private void addMultipleChoiceQuestion() {
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

		int maxAnswers = getIntInput("Enter the number of correct answers allowed: ");
		scanner.nextLine();
		while (maxAnswers < 1) {
			System.out.println("Error: Number of correct answers must be at least 1.");
			maxAnswers = getIntInput("Enter the number of correct answers allowed: ");
			scanner.nextLine();
		}
		prompt = prompt + "(" + maxAnswers + " Correct Answers)";
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

		currentSurvey.addQuestion(question);
		System.out.println("Multiple Choice question added.");
	}

	private void addShortAnswerQuestion() {
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
		currentSurvey.addQuestion(question);
		System.out.println("Short Answer question added.");
	}

	private void addEssayQuestion() {
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
		currentSurvey.addQuestion(question);
		System.out.println("Essay question added.");
	}

	private void addDateQuestion() {
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
		currentSurvey.addQuestion(question);
		System.out.println("Date question added.");
	}

	private void addMatchingQuestion() {
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
			scanner.nextLine();

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

		currentSurvey.addQuestion(question);
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
}
