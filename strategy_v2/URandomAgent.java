package strategy_v2;

import java.util.Random;

/**
 * The random agent class referring futures prices.
 * @author isao
 *
 */
public class URandomAgent extends UAgent { //Inherits UAgent class.
	
	// Write necessary field variables, i.e. attributes, here if necessary.

  /**
   * Constructor.
   * @param loginName A login name.
   * @param passwd A password.
   * @param realName A real name.
   * @param seed A random seed.
   */
	public URandomAgent(String loginName, String passwd, String realName, int seed) {
		super(loginName, passwd, realName, seed); //Calls the constructor of UAgent class.
	}

  /**
   * Makes one or more order forms to return them.
   * @param day The current date.
   * @param session The current session.
   * @param maxDaysÅ@The transaction period.
   * @param noOfSessionsPerDay The number of sessions per day.
   * @param spotPrices The spot price series. It consists of the latest spot prices for 120 days. Note that spotPrices[119] is the latest spot price and that a price is set to -1 when uncontracted.
   * @param futurePrices The futures price series. It consists of the latest futures prices for 60 days. Note that spotPrices[59] is the latest futures price and that a price is set to -1 when uncontracted.
   * @param position The current position. If it is positive, it means a long position. If it is negative, it means a short position.
   * @param money The cash. Note that the type is 'long'.
   * @return UOrderForm[] The array of order forms.
   */
	public UOrderForm[] makeOrderForms(int day, int session,
                                      int maxDays, int noOfSessionsPerDay,
                                      int[] spotPrices, int[] futurePrices,
                                      int position, long money) {
  	Random rand = getRandom(); //Gets a pseudo-random number generator from UAgent class.
  	UOrderForm[] forms = new UOrderForm[1]; //Makes an array of UOrderFrom class.
  	forms[0] = new UOrderForm(); //Makes an object of UOrderFrom class.

  	// Write necessary code here.

  	return forms; //Returns the order form.
  }

}
