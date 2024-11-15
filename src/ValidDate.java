import java.util.Scanner;

public class ValidDate extends Question {
	private static final long serialVersionUID = 1L;

	public ValidDate(String prompt) {
		super(prompt);
	}

	@Override
	public void displayQuestion() {
		System.out.println(prompt + " (Enter date in YYYY-MM-DD format)");
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
	public void modifyQuestion() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Current prompt: " + prompt);

		while (true) {
			System.out.print("Enter new prompt (or press Enter to keep the current one): ");
			String newPrompt = scanner.nextLine().trim();

			if (newPrompt.isEmpty()) {
				break;
			}

			if (isValidPrompt(newPrompt)) {
				prompt = newPrompt;
				System.out.println("Date question updated.");
				break;
			} else {
				System.out.println("Please enter a valid prompt.");
			}
		}
	}

	@Override
	public String getResponse() {
		Scanner scanner = new Scanner(System.in);
		String response;
		do {
			System.out.print("Enter a date in YYYY-MM-DD format: ");
			response = scanner.nextLine().trim();
		} while (!isValidDate(response));
		return response;
	}

	private boolean isValidDate(String date) {
		String regex = "^\\d{4}-\\d{2}-\\d{2}$";
		if (!date.matches(regex)) {
			System.out.println("Error: Date must be in YYYY-MM-DD format.");
			return false;
		}

		String[] parts = date.split("-");
		int year;
		int month;
		int day;

		try {
			year = Integer.parseInt(parts[0]);
			month = Integer.parseInt(parts[1]);
			day = Integer.parseInt(parts[2]);
		} catch (NumberFormatException e) {
			System.out.println("Error: Year, month, and day must be valid numbers.");
			return false;
		}

		if (month < 1 || month > 12) {
			System.out.println("Error: Month must be between 1 and 12.");
			return false;
		}

		int[] daysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		if (isLeapYear(year)) {
			daysInMonth[1] = 29;
		}

		int maxDay = daysInMonth[month - 1];
		if (day < 1 || day > maxDay) {
			System.out.println("Error: Day must be between 1 and " + maxDay + " for month " + month + ".");
			return false;
		}

		return true;
	}

	private boolean isLeapYear(int year) {
		if (year % 4 != 0) {
			return false;
		} else if (year % 100 != 0) {
			return true;
		} else if (year % 400 != 0) {
			return false;
		} else {
			return true;
		}
	}

}
