import java.util.*;

public class MultipleChoiceQuestion extends Question {
	private static final long serialVersionUID = 1L;
	private List<String> choices;
	private int maxAnswers;

	public MultipleChoiceQuestion(String prompt) {
		super(prompt);
		this.choices = new ArrayList<>();
	}

	public void setMaxAnswers(int maxAnswers) {
		this.maxAnswers = maxAnswers;
	}

	public void addChoice(String choice) {
		choices.add(choice);
	}

	@Override
	public void displayQuestion() {
		System.out.println("\n" + prompt);

		char option = 'A';
		for (String choice : choices) {
			System.out.println(option + ") " + choice);
			option++;
		}
	}

	private boolean isValidPrompt(String prompt) {
		if (prompt.isEmpty()) {
			System.out.println("Error: Question cannot be empty.");
			return false;
		} else if (prompt.matches("[0-9]+")) {
			System.out.println("Error: Question cannot contain only numbers.");
			return false;
		} else if (prompt.matches("[-+*/%=]")) {
			System.out.println("Error: Question cannot be '-', '+', '*', '/', '%', or '=' character.");
			return false;
		}
		return true;
	}

	private int getValidIntInput(String message, int minValue, int maxValue) {
		Scanner scanner = new Scanner(System.in);
		int value;
		while (true) {
			System.out.print(message);
			try {
				value = Integer.parseInt(scanner.nextLine().trim());
				if (value >= minValue && value <= maxValue) {
					break;
				} else {
					System.out.println("Error: Value must be between " + minValue + " and " + maxValue + ".");
				}
			} catch (NumberFormatException e) {
				System.out.println("Error: Please enter a valid integer.");
			}
		}
		return value;
	}

	private boolean isValidMaxAnswers(int maxAnswers, int numChoices) {
		if (maxAnswers < 1 || maxAnswers > numChoices) {
			System.out.println("Error: Number of correct answers must be between 1 and " + numChoices + ".");
			return false;
		}
		return true;
	}

	@Override
	public void modifyQuestion() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Current prompt: " + prompt);

		while (true) {
			System.out.print("Enter new prompt (or press Enter to keep the current one): ");
			String newPrompt = scanner.nextLine().trim();
			if (newPrompt.isEmpty() || isValidPrompt(newPrompt)) {
				if (!newPrompt.isEmpty()) {
					prompt = newPrompt;
				}
				break;
			}
		}

		while (true) {
			System.out.print("Current number of correct answers allowed: " + maxAnswers
					+ ". Enter new value (or press Enter to keep): ");
			String input = scanner.nextLine().trim();
			if (input.isEmpty()) {
				break;
			}
			try {
				int newMaxAnswers = Integer.parseInt(input);
				if (isValidMaxAnswers(newMaxAnswers, choices.size())) {
					maxAnswers = newMaxAnswers;
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("Error: Please enter a valid integer.");
			}
		}

		System.out.println("Current options:");
		for (int i = 0; i < choices.size(); i++) {
			System.out.println((i + 1) + ") " + choices.get(i));
		}
		System.out.print("Do you want to modify the text of existing options? (yes/no): ");
		String modifyChoices = scanner.nextLine().trim().toLowerCase();

		if (modifyChoices.equals("yes")) {
			for (int i = 0; i < choices.size(); i++) {
				System.out.print("Enter new text for option #" + (i + 1) + " (or press Enter to keep): ");
				String newChoice = scanner.nextLine().trim();
				if (!newChoice.isEmpty()) {
					choices.set(i, newChoice);
				}
			}
		}

		System.out.print("Do you want to add or remove options? (add/remove/none): ");
		String choiceAction = scanner.nextLine().trim().toLowerCase();

		if (choiceAction.equals("add")) {
			System.out.print("How many options would you like to add? ");
			int numToAdd = getValidIntInput("", 1, Integer.MAX_VALUE);
			for (int i = 0; i < numToAdd; i++) {
				System.out.print("Enter text for new option #" + (choices.size() + 1) + ": ");
				String newOption = scanner.nextLine().trim();
				if (!newOption.isEmpty()) {
					choices.add(newOption);
				} else {
					System.out.println("Choice cannot be empty.");
				}
			}
		} else if (choiceAction.equals("remove")) {
			int maxRemovable = choices.size() - 2;
			if (maxRemovable <= 0) {
				System.out.println("Error: Cannot remove options. At least 2 options must remain.");
			} else {
				System.out.print("How many options would you like to remove? ");
				int numToRemove = getValidIntInput("", 1, maxRemovable);
				for (int i = 0; i < numToRemove; i++) {
					System.out.print("Enter the number of the option to remove: ");
					int removeIndex = getValidIntInput("", 1, choices.size()) - 1;
					choices.remove(removeIndex);
				}
			}
		}

		if (maxAnswers > choices.size()) {
			System.out.println("Adjusting number of correct answers to match available options.");
			maxAnswers = choices.size();
		}

		System.out.println("Multiple Choice question updated.");
	}

	@Override
	public String getResponse() {
		Scanner scanner = new Scanner(System.in);
		String response;
		Set<Character> validOptions = new HashSet<>();
		char optionChar = 'A';
		for (int i = 0; i < choices.size(); i++) {
			validOptions.add(optionChar);
			optionChar++;
		}

		do {
			if (maxAnswers == 1) {
				System.out.print("Enter the letter of your choice: ");
			} else {
				System.out.print("Enter your choices (up to " + maxAnswers + " letters, e.g., A B C): ");
			}
			response = scanner.nextLine().trim().toUpperCase();
		} while (!isValidChoice(response, validOptions));
		return response;
	}

	private boolean isValidChoice(String response, Set<Character> validOptions) {
		String[] inputs = response.split("[,\\s]+");
		Set<Character> selectedOptions = new HashSet<>();

		if (inputs.length > maxAnswers) {
			System.out.println("Error: You can select up to " + maxAnswers + " option(s).");
			return false;
		}

		for (String input : inputs) {
			if (input.length() != 1) {
				System.out.println("Error: Invalid input '" + input
						+ "'. Please enter single letters corresponding to the options.");
				return false;
			}
			char choice = input.charAt(0);
			if (!validOptions.contains(choice)) {
				System.out.println("Error: Invalid option '" + choice + "'. Please select from the available options.");
				return false;
			}
			if (selectedOptions.contains(choice)) {
				System.out.println("Error: Duplicate option '" + choice + "'. Please select each option only once.");
				return false;
			}
			selectedOptions.add(choice);
		}
		return true;
	}
}
