import java.util.Scanner;

public class TrueFalse extends Question {
	private static final long serialVersionUID = 1L;
	private static String correctAnswer = "";

	public TrueFalse(String prompt) {
		super(prompt, correctAnswer);
	}

	@Override
	public void displayQuestion() {
		System.out.println(prompt + " (T/F)");
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

			if (newPrompt.isEmpty()) {
				break;
			}

			if (isValidPrompt(newPrompt)) {
				prompt = newPrompt;
				System.out.println("True/False question updated.");
				break;
			} else {
				System.out.println("Please enter a valid prompt.");
			}
		}

		if (Survey_or_Test.equals("Test")) {
			while (true) {
				System.out.print("Update current correct answer (or press Enter to keep the current answer): ");
				String newCorrectAnswer = scanner.nextLine().trim();

				if (newCorrectAnswer.isEmpty()) {
					break;
				} else {
					correctAnswer = getResponse();
					break;
				}
			}
			System.out.println("True/False correct answer updated.");
		}
	}

	@Override
	public String getResponse() {
		Scanner scanner = new Scanner(System.in);
		String response;
		do {
			System.out.print("Enter T or F: ");
			response = scanner.nextLine().trim().toUpperCase();
		} while (!response.equals("T") && !response.equals("F"));
		return response;
	}

}
