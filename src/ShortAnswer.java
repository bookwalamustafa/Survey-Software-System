import java.util.Scanner;

public class ShortAnswer extends Question {
	private static final long serialVersionUID = 1L;
	private static String correctAnswer = "";
	private int maxAllowedCharacters;

	public ShortAnswer(String prompt, int maxAllowedCharacters) {
		super(prompt, correctAnswer);
		this.maxAllowedCharacters = maxAllowedCharacters;
	}

	@Override
	public void displayQuestion() {
		System.out.println(prompt + " (Max " + maxAllowedCharacters + " characters)");
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

	private int getValidMaxChars(String message) {
		Scanner scanner = new Scanner(System.in);
		int maxChars;
		while (true) {
			System.out.print(message);
			try {
				maxChars = Integer.parseInt(scanner.nextLine().trim());
				if (maxChars > 0) {
					break;
				} else {
					System.out.println("Error: Maximum allowed characters must be a positive integer.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Error: Please enter a valid integer.");
			}
		}
		return maxChars;
	}

	@Override
	public void modifyQuestion(String Survey_or_Test) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Current prompt: " + prompt);
		if (Survey_or_Test.equals("Test")) {
			System.out.println("Current correct answer: " + correctAnswer);
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
		System.out.println("Short Answer question updated.");

		int maxChars = getValidMaxChars("Enter the maximum number of allowed characters: ");
		// maxAllowedCharacters = getValidMaxChars("Enter the maximum number of allowed
		// characters: ");
		System.out.println("Maximum number of characters updated to :" + maxChars);

		if (Survey_or_Test.equals("Test")) {
			while (true) {
				System.out.println("Update current correct answer (or press Enter to keep the current answer): ");
				String newCorrectAnswer = scanner.nextLine().trim();

				if (newCorrectAnswer.isEmpty()) {
					break;
				} else {
					correctAnswer = getResponse();
					break;
				}
			}
			System.out.println("Correct Answer updated.");
		}

	}

	@Override
	public String getResponse() {
		Scanner scanner = new Scanner(System.in);
		String response;
		do {
			System.out.print("Enter your answer (Max " + maxAllowedCharacters + " characters): ");
			response = scanner.nextLine();
			if (response.length() > maxAllowedCharacters) {
				System.out.println("Error: Your response exceeds the maximum allowed characters ("
						+ maxAllowedCharacters + "). Please try again.");
			} else if (response.length() == 0) {
				System.out.println("Error: Your response cannot be empty. Please try again.");
			}
		} while (response.length() > maxAllowedCharacters || response.length() == 0);
		return response;
	}

	public int getMaxAllowedCharacters() {
		return maxAllowedCharacters;
	}

	public void setMaxAllowedCharacters(int maxAllowedCharacters) {
		this.maxAllowedCharacters = maxAllowedCharacters;
	}
}
