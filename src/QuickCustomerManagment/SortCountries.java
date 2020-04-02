package QuickCustomerManagment;

import java.util.Comparator;

public class SortCountries implements Comparator<String> {

	/**
	 * Ascending order of Strings without any whitespace
	 */
	@Override
	public int compare(String o1, String o2) {
		String o1Clean = o1.replaceAll(" ", "");
		return o1Clean.compareTo((o2.replaceAll(" ", "")));
	}

}
