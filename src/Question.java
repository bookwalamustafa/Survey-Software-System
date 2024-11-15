import java.io.Serializable;

public abstract class Question implements Serializable {
	private static final long serialVersionUID = 1L;
	protected String prompt;

	public Question(String prompt) {
		this.prompt = prompt;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public abstract void displayQuestion();

	public abstract void modifyQuestion();

	public abstract String getResponse();
}
