import java.util.*;

public class Matching extends Question {
	private static final long serialVersionUID = 1L;
	private static String correctAnswer = "";
	private Map<String, String> pairs;

	public Matching(String prompt) {
		super(prompt, correctAnswer);
		this.pairs = new HashMap<>();
	}

	public void addPair(String question, String answer) {
		pairs.put(question, answer);
	}

	@Override
	public void displayQuestion() {
		System.out.println(prompt);

		List<String> leftItems = new ArrayList<>(pairs.keySet());
		List<String> rightItems = new ArrayList<>(pairs.values());

		char leftLabel = 'A';
		int rightLabel = 1;

		for (int i = 0; i < leftItems.size(); i++) {
			String leftDisplay = String.format("%-15s", leftLabel + ") " + leftItems.get(i));
			String rightDisplay = String.format("%-15s", rightLabel + ") " + rightItems.get(i));
			System.out.println(leftDisplay + rightDisplay);

			leftLabel++;
			rightLabel++;
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

	@Override
	public void modifyQuestion(String Survey_or_Test) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Current prompt: " + prompt);
		if (Survey_or_Test.equals("Test")) {
			System.out.println("Currrent correct answer: " + correctAnswer);
		}

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

		System.out.println("Current pairs:");
		for (Map.Entry<String, String> entry : pairs.entrySet()) {
			System.out.println("Left-hand side: " + entry.getKey() + " -> Right-hand side: " + entry.getValue());
		}

		System.out.print("Do you want to modify existing pairs? (yes/no): ");
		String modifyPairs = scanner.nextLine().trim().toLowerCase();

		if (modifyPairs.equals("yes")) {
			for (String key : new ArrayList<>(pairs.keySet())) {
				System.out.print("Enter new text for question item '" + key + "' (or press Enter to keep): ");
				String newQuestionItem = scanner.nextLine().trim();
				String originalAnswer = pairs.get(key);

				if (!newQuestionItem.isEmpty()) {
					pairs.remove(key);
					key = newQuestionItem;
				}

				System.out.print("Enter new answer for question item '" + key + "' (or press Enter to keep): ");
				String newAnswer = scanner.nextLine().trim();
				if (!newAnswer.isEmpty()) {
					originalAnswer = newAnswer;
				}

				pairs.put(key, originalAnswer);
			}
		}

		System.out.print("Do you want to add or remove pairs? (add/remove/none): ");
		String action = scanner.nextLine().trim().toLowerCase();

		if (action.equals("add")) {
			System.out.print("How many pairs would you like to add? ");
			int numToAdd = getValidIntInput("", 1, Integer.MAX_VALUE);
			for (int i = 1; i <= numToAdd; i++) {
				String questionItem, answerItem;

				while (true) {
					System.out.print("Enter new question item #" + (pairs.size() + 1) + ": ");
					questionItem = scanner.nextLine().trim();
					if (!questionItem.isEmpty()) {
						break;
					} else {
						System.out.println("Question item cannot be empty.");
					}
				}

				while (true) {
					System.out.print("Enter answer for question item '" + questionItem + "': ");
					answerItem = scanner.nextLine().trim();
					if (!answerItem.isEmpty()) {
						break;
					} else {
						System.out.println("Answer item cannot be empty.");
					}
				}

				pairs.put(questionItem, answerItem);
			}
		} else if (action.equals("remove")) {
			int maxRemovable = pairs.size() - 2;
			if (maxRemovable <= 0) {
				System.out.println("Error: Cannot remove pairs. At least 2 pairs must remain.");
			} else {
				System.out.print("How many pairs would you like to remove? ");
				int numToRemove = getValidIntInput("", 1, maxRemovable);
				for (int i = 0; i < numToRemove; i++) {
					System.out.print("Enter the question item of the pair you wish to remove: ");
					String removeKey = scanner.nextLine().trim();
					if (pairs.containsKey(removeKey)) {
						pairs.remove(removeKey);
						System.out.println("Removed pair with question item: " + removeKey);
					} else {
						System.out.println("Error: Question item not found in pairs.");
					}
				}
			}
		}

		System.out.println("Matching question updated.");
	}

	@Override
	public String getResponse() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please match each question with the correct answer in the format 'A 1':");

		int numQuestions = pairs.size();
		List<String> responses = new ArrayList<>();

		for (int i = 0; i < numQuestions; i++) {
			while (true) {
				System.out.print("Match " + (i + 1) + ": ");
				String input = scanner.nextLine().trim();

				String[] parts = input.split("\\s+");
				if (parts.length == 2 && parts[0].matches("[A-Z]") && parts[1].matches("\\d+")) {
					responses.add(input);
					break;
				} else {
					System.out.println("Invalid input format. Please use 'A 1', 'B 2', etc.");
				}
			}
		}

		String combinedResponse = String.join(", ", responses); // Store as a single string
		System.out.println("Matching responses recorded: " + combinedResponse);
		return combinedResponse;
	}

	public Map<String, String> getPairs() {
		return new HashMap<>(pairs);
	}

}