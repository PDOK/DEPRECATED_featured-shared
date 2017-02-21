package pdok.featured;

public class GMLParserException extends Exception {

	private static final long serialVersionUID = -473005243079207681L;

	public GMLParserException() {
		super();
	}

	public GMLParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public GMLParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public GMLParserException(String message) {
		super(message);
	}

	public GMLParserException(Throwable cause) {
		super(cause);
	}
}
