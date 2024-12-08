import java.io.File;
import java.io.Serializable;

import utils.FileUtils;
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

	public static Response loadResponse() {
		String responseToLoad = FileUtils.listAndPickFileFromDir(basePath);
		Response loadedResponse = SerializationHelper.deserialize(Response.class, responseToLoad);
		return loadedResponse;
	}

//	// Deserialize the saved Response object
//	public static Response loadResponse(String fileName) {
//		return (Response) SerializationHelper.deserialize(Response.class, basePath, fileName);
//	}

	public void saveResponse(Response response, String name) {
		File directory = new File(basePath);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		String fileName = name + generateFileName();
		SerializationHelper.serialize(Response.class, response, basePath, fileName);
		System.out.println("Response has been saved!");

		// Deserialize and display the saved response
		Response deserializedResponse = loadResponse();
		System.out.println("Deserialized Response: " + deserializedResponse);
	}

	@Override
	public String toString() {
		return "Response{}"; // Customize this to include fields if there are any.
	}

}
