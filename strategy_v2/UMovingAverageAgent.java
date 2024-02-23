package strategy_v2;

import java.util.Random;

/**
 * The moving average agent referring futures prices.
 */
public class UMovingAverageAgent extends UAgent {

	/** The default value of the number of sessions of short term. */
	public static final int DEFAULT_SHORT_TERM = 8;

	/** The default value of the number of sessions of mid term. */
	public static final int DEFAULT_MEDIUM_TERM = 16;

  /** The default value of the maximum order volume. */
  public static final int DEFAULT_MAX_QUANT = 50;

  /** The default value of the minimum order volume. */
  public static final int DEFAULT_MIN_QUANT = 10;

  /** The default value of the maximum absolute value of position. */
  public static final int DEFAULT_MAX_POSITION = 300;

	/** The number of sessions of short term. */
	private int fShortTerm = DEFAULT_SHORT_TERM;

	/** The number of sessions of mid term. */
	private int fMediumTerm = DEFAULT_MEDIUM_TERM;

  /** The maximum order volume. */
  private int fMaxQuant = DEFAULT_MAX_QUANT;

  /** The minimum order volume. */
  private int fMinQuant = DEFAULT_MIN_QUANT;

  /** The maximum absolute value of position. */
  private int fMaxPosition = DEFAULT_MAX_POSITION;

  /**
   * Constructor.
   * @param loginName A login name.
   * @param passwd A password.
   * @param realName A real name.
   * @param seed A random seed.
   */
	public UMovingAverageAgent(String loginName, String passwd, String realName, int seed) {
		super(loginName, passwd, realName, seed);
	}

	/**
	 * Returns the number of sessions of short term.
	 * @return The number of sessions of short term.
	 */
	public int getShortTerm() {
		return fShortTerm;
	}

	/**
	 * Returns the number of sessions of mid term.
	 * @return The number of sessions of mid term.
	 */
	public int getMediumTerm() {
		return fMediumTerm;
	}

	/**
	 * Returns the minimum order volume.
	 * @return The minimum order volume.
	 */
	public int getMinQuant() {
		return fMinQuant;
	}

	/**
	 * Returns the maximum order volumeÅD
	 * @return The maximum order volume.
	 */
	public int getMaxQuant() {
		return fMaxQuant;
	}

	/**
	 * Returns the maximum absolute number of position.
	 * @return The maximum absolute number of position.
	 */
	public int getMaxPosition() {
		return fMaxPosition;
	}
	
	/**
	 A utility function for calculate moving average price
	 */
	private float calculateMovingAverage(int[] prices, int session, int term, int lag) {
		float moving_avg_price = 0;
		
		int window_size = Math.min(term, session);
		int start_session = Math.min(session - lag, prices.length - 1 - lag);
		for (int i=0; i < window_size; i++) {
			moving_avg_price += prices[start_session - i];
		}
		
		return moving_avg_price / window_size;
	}
	
	public int getSecondLatestPrice(int[] prices) {
		boolean found_latest = false;
    for (int i = prices.length - 1; i >= 0; --i) {
      if (prices[i] >= 0) {
      	if (!found_latest) {
      		found_latest = true;
      		continue;
      	}
        return prices[i];
      }
    }
    return UOrderForm.INVALID_PRICE;
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
                                      int[] spotPrices, int[] futuresPrices,
                                      int position, long money) {
  	Random rand = getRandom();
  	UOrderForm[] forms = new UOrderForm[1];
  	forms[0] = new UOrderForm();

  	// Write necessary code here.
  	
  	// calculate short-term moving average
  	int pastSession = (day - 1) * noOfSessionsPerDay + session;
  	float short_avg_price_prev = this.calculateMovingAverage(futuresPrices, pastSession, this.fShortTerm, 1);
  	float short_avg_price = this.calculateMovingAverage(futuresPrices, pastSession, this.fShortTerm, 0);
  	
  	// calculate medium-term moving average
  	float mid_avg_price_prev = this.calculateMovingAverage(futuresPrices, pastSession, this.fMediumTerm, 1);
  	float mid_avg_price = this.calculateMovingAverage(futuresPrices, pastSession, this.fMediumTerm, 0);
  	
  	// determine order division
  	int buysell = 0;
  	if ((short_avg_price_prev < mid_avg_price_prev) && (mid_avg_price < short_avg_price)){
			buysell = UOrderForm.BUY;
  	}
  	else if ((short_avg_price_prev > mid_avg_price_prev) && (mid_avg_price > short_avg_price)){
			buysell = UOrderForm.SELL;
  	}
  	else {
			buysell = UOrderForm.NONE;
  	}
  	
  	if ((buysell == UOrderForm.SELL) && (position < 0) && (Math.abs(position) > this.fMaxPosition)) {
  		buysell = UOrderForm.NONE;
  	}
  	
  	if ((buysell == UOrderForm.BUY) && (position > 0) && (Math.abs(position) > this.fMaxPosition)) {
  		buysell = UOrderForm.NONE;
  	}
  	
//  	println(short_avg_price_prev + " " + mid_avg_price_prev + " " + mid_avg_price + " " + short_avg_price);
  	forms[0].setBuySell(buysell);
  	if (buysell == UOrderForm.NONE) {
  		return forms;
  	}
  	
  	// determine order price
  	double latestPrice = this.getLatestPrice(futuresPrices);
  	double secondLatestPrice = this.getSecondLatestPrice(futuresPrices);
  	if (latestPrice == UOrderForm.INVALID_PRICE) {
  		latestPrice = this.getLatestPrice(spotPrices);
  		secondLatestPrice = this.getSecondLatestPrice(spotPrices);
  	}
  	
  	double mean = Math.abs(latestPrice - secondLatestPrice);
  	double std = 0.25 * mean;
  	double noisePrice = (std * rand.nextGaussian()) + mean; // convert from N(0, 1) to N(mean, std^2)
  	
  	int order_price = 0;
  	if (buysell == UOrderForm.BUY) {
  		order_price = (int)(latestPrice + noisePrice);
  	}
  	else if (buysell == UOrderForm.SELL) {
  		order_price = (int)(latestPrice - noisePrice);
  	}
  	if (order_price <= 0) {
  		order_price = 1;
  	}
  	forms[0].setPrice(order_price);
  	
  	// determine order volume
  	forms[0].setQuantity(fMinQuant + rand.nextInt(fMaxQuant - fMinQuant + 1));
  	
  	println ( " day =" + day + " , session = " + session + " , latestPrice =" + latestPrice
  			 + " , " + forms [0]. getBuySellByString () + " , price =" + forms [0]. getPrice ()
  			 + " , quantity = " + forms [0]. getQuantity ());

  	return forms;
  }

}
