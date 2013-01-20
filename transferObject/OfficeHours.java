package transferObject;

class OfficeHours{
	private int pk;
	private String location;
	private int start;
	private int end;
	private String DOW;
	public int getPk() {
		return pk;
	}
	public void setPk(int pk) {
		this.pk = pk;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public String getDOW() {
		return DOW;
	}
	public void setDOW(String dOW) {
		DOW = dOW;
	}
	
	
}
