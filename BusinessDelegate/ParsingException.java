package BusinessDelegate;

public class ParsingException extends Exception
{
	private int errorCode;
	private String requestID;
	
	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public ParsingException(int _errorCode)
	{
		this.errorCode=_errorCode;
	}
	public ParsingException(int _errorCode,String _requestID) 
	{
		this.errorCode = _errorCode;
		this.requestID =_requestID;
	}
	//GETTER SETTERS
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int _errorCode) 
	{
		this.errorCode = _errorCode;
	}

}
