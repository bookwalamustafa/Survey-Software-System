import java.io.Serializable;

public abstract class Question implements Serializable {
	private static final long serialVersionUID = 1L;
	protected String prompt;
	private String correctAnswers;

	public Question(String prompt, String correctAnswer) {
		this.prompt = prompt;
		this.correctAnswers = "";
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public String getCorrectAnswers() {
		return correctAnswers;
	}

	public void setCorrectAnswers(String correctAnswers) {
		this.correctAnswers = correctAnswers;
	}

	public abstract void displayQuestion();

	public abstract void modifyQuestion(String Survey_or_Test);

	public abstract String getResponse();
}
