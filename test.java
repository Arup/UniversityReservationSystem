
public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String s = "000012345";
		String sPattern = "[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]";
		
		System.out.println(s.matches(sPattern));

	}

}
