import java.util.Scanner;

public class Essay extends Question {
	private static final long serialVersionUID = 1L;
	private static String correctAnswer = "";

	public Essay(String prompt) {
		super(prompt, correctAnswer);
	}

	@Override
	public void displayQuestion() {
		System.out.println(prompt + " (Essay response)");
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
				break;
			}
		}
		System.out.println("Essay question updated.");
	}

	@Override
	public String getResponse() {
		Scanner scanner = new Scanner(System.in);
		String response;
		do {
			System.out.print("Enter your essay response: ");
			response = scanner.nextLine();
			if (response.trim().isEmpty()) {
				System.out.println("Error: Response cannot be empty. Please enter your essay response.");
			}
		} while (response.trim().isEmpty());
		return response;
	}

}
