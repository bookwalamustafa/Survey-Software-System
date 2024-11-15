import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import utils.FileUtils;
import utils.SerializationHelper;
import utils.TimeHelper;

public class Survey implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String basePath = "Survey" + File.separator;
	private List<Question> questions = new ArrayList<>();

	public Survey() {
	}

	public static Survey loadSurvey() {
		String surveyToLoad = FileUtils.listAndPickFileFromDir(basePath);
		Survey loadedSurvey = SerializationHelper.deserialize(Survey.class, surveyToLoad);
		return loadedSurvey;
	}

	protected static String generateFileName() {
		String simUUID = TimeHelper.getUniqueTimeStamp();
		return simUUID;
	}

	public void displaySurvey() {
		if (questions.isEmpty()) {
			System.out.println("The survey has no questions.");
		} else {
			System.out.println("Displaying Survey Questions:");
			for (int i = 0; i < questions.size(); i++) {
				System.out.print((i + 1) + ") ");
				questions.get(i).displayQuestion();
			}
		}
	}

	public void addQuestion(Question question) {
		questions.add(question);
	}

	public void modifySurvey() {
		if (questions.isEmpty()) {
			System.out.println("No questions in the survey to modify.");
			return;
		}

		Scanner scanner = new Scanner(System.in);
		displaySurvey();

		int questionNumber = getQuestionNumber(scanner);
		if (questionNumber == -1) {
			return;
		}

		Question question = questions.get(questionNumber - 1);
		question.modifyQuestion();
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

	public void takeSurvey() {
		if (questions.isEmpty()) {
			System.out.println("The survey has no questions.");
			return;
		}

		System.out.println("Starting the survey. Please answer each question.");

		List<String> responses = new ArrayList<>();
		for (Question question : questions) {
			question.displayQuestion();
			String response = question.getResponse();
			responses.add(response);
		}

		System.out.println("Survey completed. Thank you for your responses!");
	}

	public void saveSurvey(Survey survey, String name) {
		String fileName = generateFileName();
		System.out.println("Survey has been saved!");
		SerializationHelper.serialize(Survey.class, survey, basePath, name + fileName);
	}
}
