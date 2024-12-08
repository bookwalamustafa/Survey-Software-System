import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import utils.FileUtils;
import utils.SerializationHelper;
import utils.TimeHelper;

public class Test implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String basePath = "Test" + File.separator;
	private List<Question> questions = new ArrayList<>();
	private List<String> correctAnswers = new ArrayList<>();

	public Test() {
	}

	public static Test loadTest() {
		String testToLoad = FileUtils.listAndPickFileFromDir(basePath);
		Test loadedTest = SerializationHelper.deserialize(Test.class, testToLoad);
		return loadedTest;
	}

	protected static String generateFileName() {
		String simUUID = TimeHelper.getUniqueTimeStamp();
		return simUUID;
	}

	public void displayTest(boolean showAnswers) {
		if (questions.isEmpty()) {
			System.out.println("The test has no questions.");
		} else {
			System.out.println("Displaying Test:");
			for (int i = 0; i < questions.size(); i++) {
				System.out.print((i + 1) + ") ");
				questions.get(i).displayQuestion();
				if (showAnswers) {
					System.out.println("The correct answer is: " + correctAnswers.get(i));
				}
			}
		}
	}

	public void addQuestion(Question question, String correctAnswer) {
		questions.add(question);
		correctAnswers.add(correctAnswer);
	}

	public void modifyTest() {
		if (questions.isEmpty()) {
			System.out.println("No questions in the test to modify.");
			return;
		}

		Scanner scanner = new Scanner(System.in);
		displayTest(true);

		int questionNumber = getQuestionNumber(scanner);
		if (questionNumber == -1) {
			return;
		}

		Question question = questions.get(questionNumber - 1);
		question.modifyQuestion("Test");
	}

	private int getQuestionNumber(Scanner scanner) {
		System.out.print("Enter the question number you want to modify: ");
		while (!scanner.hasNextInt()) {
			System.out.print("Invalid input. Please enter a valid question number: ");
			scanner.next();
		}
		int questionNumber = scanner.nextInt();
		if (questionNumber < 1 || questionNumber > questions.size()) {
			System.out.println("Invalid question number.");
			return -1;
		}
		return questionNumber;
	}

	public void takeTest() {
		if (questions.isEmpty()) {
			System.out.println("The test has no questions.");
			return;
		}

		System.out.println("Starting the test. Please answer each question.");

		List<String> responses = new ArrayList<>();
		for (Question question : questions) {
			question.displayQuestion();
			String response = question.getResponse();
			responses.add(response);
		}

		System.out.println("Test completed. Thank you for your responses!");
	}

	// Additional methods for saving, taking, modifying, tabulating, and grading
	// Tests
	// To be implemented in subsequent steps

	public void saveTest(Test test, String name) {
		String fileName = generateFileName();
		System.out.println("Test has been saved!");
		SerializationHelper.serialize(Test.class, test, basePath, name + fileName);
	}
}
