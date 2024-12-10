import java.io.File;
import java.io.Serializable;
import java.util.*;

import utils.FileUtils;
import utils.SerializationHelper;
import utils.TimeHelper;

public class Survey implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String basePath = "Survey" + File.separator;
	private List<Question> questions = new ArrayList<>();
	private List<Response> responses = new ArrayList<>();
	private String userProvidedSurveyName;
	private String surveyTimestamp;

	public Survey() {

	}

	public static Survey loadSurvey() {
		String surveyToLoad = FileUtils.listAndPickFileFromDir(basePath);
		if (surveyToLoad == null || surveyToLoad.isEmpty()) {
			return null;
		}
		Survey loadedSurvey = SerializationHelper.deserialize(Survey.class, surveyToLoad);
		if (loadedSurvey != null) {
			String loadedFileName = new File(surveyToLoad).getName();
			loadedSurvey.setUserProvidedSurveyName(loadedFileName);
		}

		if (loadedSurvey != null) {
			loadedSurvey.surveyTimestamp = surveyToLoad.replace("Survey", "").replace(".ser", "");
			System.out.println("Loaded survey with timestamp: " + loadedSurvey.surveyTimestamp);
		}
		return loadedSurvey;
	}

	protected static String generateFileName() {
		return TimeHelper.getUniqueTimeStamp();
	}

	public String getUserProvidedSurveyName() {
		return userProvidedSurveyName;
	}

	public void setUserProvidedSurveyName(String userProvidedSurveyName) {
		this.userProvidedSurveyName = userProvidedSurveyName;
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
		question.modifyQuestion("Survey");
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

	public void takeSurvey(String surveyName) {
		if (questions.isEmpty()) {
			System.out.println("The survey has no questions.");
			return;
		}

		System.out.println("Starting the survey. Please answer each question.");

		Response response = new Response();
		for (Question question : questions) {
			question.displayQuestion();
			String userResponse = question.getResponse();
			response.addAnswer(userResponse);
		}

		addResponse(response);

		response.saveResponse(response, surveyName, "Survey");

		System.out.println("Survey completed. Thank you for your responses!");
	}

	public void saveSurvey(Survey survey, String userProvidedName) {
		String fileName = userProvidedName.isEmpty() ? generateFileName() : userProvidedName;
		System.out.println("Survey has been saved as: " + fileName);
		SerializationHelper.serialize(Survey.class, survey, basePath, fileName);
	}

	public void addResponse(Response response) {
		responses.add(response);
	}

	public List<Response> getResponses() {
		return responses;
	}

	public void tabulateSurvey(String surveyName) {
		System.out.println("SurveyName:" + surveyName);
		if (surveyName == null || surveyName.isEmpty()) {
			System.out.println("Survey name is not set. Cannot tabulate responses.");
			return;
		}
		responses.clear();

		String basePath = "Response";

		File responseDir = new File(basePath);
		if (!responseDir.exists() || !responseDir.isDirectory()) {
			System.out.println("No response directory found.");
			return;
		}

		File[] responseFiles = responseDir.listFiles();
		if (responseFiles == null || responseFiles.length == 0) {
			System.out.println("No responses found.");
			return;
		}

		for (File file : responseFiles) {
			String fileName = file.getName();
			if (fileName.startsWith("SurveyResponse_" + surveyName)) {
				Response response = SerializationHelper.deserialize(Response.class, file.getAbsolutePath());
				if (response != null) {
					responses.add(response);
				}
			}
		}

		if (responses.isEmpty()) {
			System.out.println("No responses found for the survey: " + surveyName);
			return;
		}

		System.out.println("\nTabulating Responses for Survey: " + surveyName);
		for (int i = 0; i < questions.size(); i++) {
			Question question = questions.get(i);
			System.out.println("\nQuestion " + (i + 1) + ":");
			question.displayQuestion();

			List<String> allAnswers = new ArrayList<>();
			for (Response response : responses) {
				if (i < response.getAnswers().size()) {
					allAnswers.add(response.getAnswers().get(i));
				}
			}

			if (question instanceof TrueFalse || question instanceof MultipleChoiceQuestion) {
				tabulateMultipleChoiceOrTrueFalse(allAnswers, question);
			} else if (question instanceof ShortAnswer || question instanceof ValidDate) {
				tabulateShortAnswerOrDate(allAnswers, question);
			} else if (question instanceof Essay) {
				tabulateEssay(allAnswers, question);
			} else if (question instanceof Matching) {
				tabulateMatching(allAnswers, (Matching) question);
			} else {
				System.out.println("Unknown question type. Cannot tabulate.");
			}
		}
	}

	private void tabulateMultipleChoiceOrTrueFalse(List<String> allAnswers, Question question) {
		Map<String, Integer> counts = new LinkedHashMap<>();
		for (String answer : allAnswers) {
			counts.put(answer, counts.getOrDefault(answer, 0) + 1);
		}

		System.out.println("Tabulation:");
		for (Map.Entry<String, Integer> entry : counts.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
	}

	private void tabulateShortAnswerOrDate(List<String> allAnswers, Question question) {
		Map<String, Integer> counts = new LinkedHashMap<>();
		for (String answer : allAnswers) {
			counts.put(answer, counts.getOrDefault(answer, 0) + 1);
		}

		System.out.println("Tabulation:");
		for (Map.Entry<String, Integer> entry : counts.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
	}

	private void tabulateEssay(List<String> allAnswers, Question question) {
		System.out.println("Tabulation:");
		for (String answer : allAnswers) {
			System.out.println(answer);
		}
	}

	private void tabulateMatching(List<String> allAnswers, Matching question) {
		Map<String, Integer> permutationCounts = new LinkedHashMap<>();
		for (String answer : allAnswers) {
			permutationCounts.put(answer, permutationCounts.getOrDefault(answer, 0) + 1);
		}

		System.out.println("Tabulation:");
		for (Map.Entry<String, Integer> entry : permutationCounts.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
	}
}
