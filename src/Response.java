import java.io.File;
import java.io.Serializable;

import utils.SerializationHelper;
import utils.TimeHelper;

public class Response implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String basePath = "Response" + File.separator;

	public Response() {
	}

	protected static String generateFileName() {
		return TimeHelper.getUniqueTimeStamp();
	}

	public void saveResponse(Response response, String name) {
		System.out.println("Reached here");
		if (response == null || name == null) {
			throw new IllegalArgumentException("Response or name cannot be null.");
		}

		File directory = new File(basePath);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		String fileName = generateFileName();
		SerializationHelper.serialize(Response.class, response, basePath, name + fileName);
	}
}
