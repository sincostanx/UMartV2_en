package strategy_v2;

import java.util.Arrays;
import java.util.Random;

import strategy.*;

/**
 * A modified SFSpreadStrategy
 */
class CustomSFSpreadStrategy extends SFSpreadStrategy {
	// same as the original strategy
	private int custom_fWidthOfPrice = DEFAULT_WIDTH_OF_PRICE;
  private int custom_fMaxQuant = DEFAULT_MAX_QUANT;
  private int custom_fMinQuant = DEFAULT_MIN_QUANT;
  private int custom_fMaxPosition = DEFAULT_MAX_POSITION;
  private double custom_fSpreadRatioThreshold = DEFAULT_SPREAD_RATIO_THRESHOLD;
  private Random custom_fRandom;
  
  // max threshold of SpreadRatio for calculating order price (R_max) in the report
  public static final double DEFAULT_MAX_SPREAD_QUANT = 0.05;
  private double max_spread_quant = DEFAULT_MAX_SPREAD_QUANT;
  
  // constructor, same as the original strategy
  public CustomSFSpreadStrategy(int seed) {
  	super(seed);
  	custom_fRandom = new Random(seed);
  }
  
  // for changing hyperparameters 
	public void setParameters_manual(int min_quant, int max_quant, int max_position, double spread_ratio_threshold) {
		custom_fMinQuant = min_quant;
		custom_fMaxQuant = max_quant;
		custom_fMaxPosition = max_position;
		custom_fSpreadRatioThreshold = spread_ratio_threshold;
		println("set parameters successfully: " + "custom_fMinQuant = " + min_quant);
	}
	
	@Override
  public Order getOrder(int[] spotPrices, int[] futurePrices,
      int pos, long money, int restDay) {
			Order order = new Order(); // Object to return values.
			int latestSpotPrice = spotPrices[119];
			int latestFuturePrice = futurePrices[59];
			if (latestSpotPrice <= 0 || latestFuturePrice < 0) {
				order.buysell = Order.NONE;
				return order;
			}
			
			// Calculate spread ratio
			double spreadRatio = (double) (latestFuturePrice - latestSpotPrice) /
			(double) latestSpotPrice;
			// Decide to sell or to buy
			if (spreadRatio <= -custom_fSpreadRatioThreshold) {
				order.buysell = Order.BUY; //
			} else if (spreadRatio >= custom_fSpreadRatioThreshold) {
				order.buysell = Order.SELL; //
			} else {
			order.buysell = Order.NONE; //
				return order;
			}
			
			// Cancel decision if it may increase absolute value of the position
			if (order.buysell == Order.BUY) {
				if (pos > custom_fMaxPosition) {
				order.buysell = Order.NONE;
				return order;
				}
			} else if (order.buysell == Order.SELL) {
				if (pos < -custom_fMaxPosition) {
				order.buysell = Order.NONE;
				return order;
				}
			}
			
			// Calculate order price
			double meanPrice = (double) (latestFuturePrice + latestSpotPrice) / 2.0;
			double sigma = Math.abs( (latestFuturePrice - latestSpotPrice) / 2.0);
			while (true) {
				order.price = (int) (meanPrice + (sigma * custom_fRandom.nextGaussian()));
				if (order.price > 0.0) {
					break;
				}
			}

			// Calculate order volume using new formula (Equation 3 in the report)
			double scale = Math.min(1.0, Math.abs(spreadRatio) / max_spread_quant);
			order.quant = (int)(custom_fMinQuant + scale * (custom_fMaxQuant - custom_fMinQuant));
	    message(Order.buySellToString(order.buysell)
	            + ", price = " + order.price
	            + ", volume = " + order.quant
	            + " (spreadRatio = " + spreadRatio + " )");
	    return order;
	}
}

/**
 * A RandomStrategy
 */
class CustomRandomStrategy extends RandomStrategy {
	//same as the original strategy
	private int custom_fWidthOfPrice = DEFAULT_WIDTH_OF_PRICE;
  private int custom_fMaxQuant = DEFAULT_MAX_QUANT;
  private int custom_fMinQuant = DEFAULT_MIN_QUANT;
  private int custom_fMaxPosition = DEFAULT_MAX_POSITION;
  private int custom_fNominalPrice = DEFAULT_NOMINAL_PRICE;
  private Random custom_fRandom;
  
  // constructor, same as the original strategy
  public CustomRandomStrategy(int seed) {
  	super(seed);
  	custom_fRandom = new Random(seed);
  }
  
  //for changing hyperparameters 
	public void setParameters_manual(int min_quant, int max_quant, int max_position) {
		custom_fMinQuant = min_quant;
		custom_fMaxQuant = max_quant;
		custom_fMaxPosition = max_position;
		println("set parameters successfully: " + "custom_fMinQuant = " + min_quant);
	}
	
	@Override
  public Order getOrder(int[] spotPrices, int[] futurePrices,
      int pos, long money, int restDay) {
			Order order = new Order();
	    // Scan a well defined latest futures price. If it is not available,
	    // a nominal value is used.
	    int prevPrice = getLatestPrice(futurePrices);
	    if (prevPrice == -1) {
	      prevPrice = getLatestPrice(spotPrices);
	    }
	    if (prevPrice == -1) {
	      prevPrice = custom_fNominalPrice;
	      // Submit a random order with a random volume and a random price
	      // around the latest futures price.
	    }
	    order.buysell = custom_fRandom.nextInt(2) + 1;
	    // Cancel decision if it may increase absolute value of the position
	    if (order.buysell == Order.BUY) {
	      if (pos > custom_fMaxPosition) {
	        order.buysell = Order.NONE;
	        return order;
	      }
	    } else if (order.buysell == Order.SELL) {
	      if (pos < -custom_fMaxPosition) {
	        order.buysell = Order.NONE;
	        return order;
	      }
	    }
	    while (true) {
	      order.price = prevPrice + (int) (custom_fWidthOfPrice * custom_fRandom.nextGaussian());
	      if (order.price > 0) {
	        break;
	      }
	    }
	    order.quant = custom_fMinQuant + custom_fRandom.nextInt(custom_fMaxQuant - custom_fMinQuant + 1);
	    // Message
	    message(Order.buySellToString(order.buysell) + ", price = " + order.price
	            + ", volume = " + order.quant + " (prevPrice = " + prevPrice
	            + "  )");
	    return order;
	  }
}

public class U22M51878 extends UAgent {

	// list of sub-strategies
	public Strategy[] strategies;
	
	// list of (contributions) coefficients of sub-strategies
	public double[] weights;
	
	// previous orders of each sub-strategy
	public UOrderForm[] prev_orders;
	
	// (contributions) coefficients decay rate for adjustment algorithm
	public double decay_rate;

  /**
   * Constructor.
   * @param loginName A login name.
   * @param passwd A password.
   * @param realName A real name.
   * @param seed A random seed.
   */
	public U22M51878(String loginName, String passwd, String realName, int seed) {
		super(loginName, passwd, realName, seed);

		this.strategies = initializeStrategies(seed);
		
		// initialize coefficients of all sub-strategies to be the same
		this.weights = new double[this.strategies.length];
		Arrays.fill(this.weights, 1.0 / this.strategies.length);
		
		// initialize previous order as null
		this.prev_orders = null;
		this.decay_rate = 0.97;
		println("Successfully load strategies");
	}
	
	// initialize sub-strategies 
	public Strategy[] initializeStrategies(int seed) {
		Strategy[] strategies = new Strategy[5];
		
		for (int i=0;i<strategies.length;i++) {
			if (i < (strategies.length)) {
				// get 5 modified SFSpreadStrategy with different SpreadRatio
				strategies[i] = new CustomSFSpreadStrategy(seed + i);
				((CustomSFSpreadStrategy) strategies[i]).setParameters_manual(50, 400, 2000, 0.002*(i+1));
			}
			else {
				// get 5 RandomStrategy with different random seeds
				strategies[i] = new CustomRandomStrategy(seed + i);
				((CustomRandomStrategy) strategies[i]).setParameters_manual(10, 20, 2000);
			}
		}
		
		return strategies;
	}
	
	// get the second latest values in a price series
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
	
	// Coefficient adjustment algorithm
	public void update_weights(int[] futurePrices) {
		int latestPrice = this.getLatestPrice(futurePrices);
  	int secondLatestPrice = this.getSecondLatestPrice(futurePrices);
  	
  	/*
  	 * latestPrice > secondLatestPrice --> Future price increased, so we made the right choice if we sold it
  	 * latestPrice < secondLatestPrice --> Future price decreased, so we made the right choice if we bought it
  	 */
  	double total_weight = 0;
  	for (int i=0;i<this.prev_orders.length;i++) {
  		int buysell = this.prev_orders[i].getBuySell();
  		if (latestPrice > secondLatestPrice) {
    		if (buysell == UOrderForm.BUY) this.weights[i] = this.weights[i] / this.decay_rate;
    		if (buysell == UOrderForm.SELL) this.weights[i] = this.weights[i] * this.decay_rate;
    	}
    	else if (latestPrice < secondLatestPrice) {
    		if (buysell == UOrderForm.BUY) this.weights[i] = this.weights[i] * this.decay_rate;
    		if (buysell == UOrderForm.SELL) this.weights[i] = this.weights[i] / this.decay_rate;
    	}
  		// Ensure that no sub-strategy totally dominates others
  		this.weights[i] = Math.min(0.33, Math.max(0.0, this.weights[i]));
  		total_weight += this.weights[i];
  	}
  	
  	// Normalize weights to ensure upper limit for total order volume from all sub-strategies
  	for (int i=0;i<this.prev_orders.length;i++) {
  		this.weights[i] /= total_weight;
  	}
  	
	}

  /**
   * Makes one or more order forms to return them.
   * @param day The current date.
   * @param session The current session.
   * @param maxDays@The transaction period.
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

		// Prints the current date, the current session and the current spot price.
		println("day=" + day + ", session=" + session + ", spot=" +  spotPrices[spotPrices.length - 1]);
		
		// Update weights
		if (this.prev_orders != null) {
			this.update_weights(futurePrices);
		}
		
		// Print weights
		print("Current weight: ");
		for (int i = 0; i < this.weights.length; i++) {
        print(String.format("%.3f", this.weights[i]) + " ");
    }
		println("");

		// Execute all sub-strategies
		int restDay = this.calculateRestSessions(day, session, maxDays, noOfSessionsPerDay);
		UOrderForm[] forms = new UOrderForm[this.strategies.length];
		for (int i=0;i<this.strategies.length;i++) {
			forms[i] = new UOrderForm();
			Order current_order = this.strategies[i].getOrder(spotPrices, futurePrices, position, money, restDay);
			
			// If sub-strategy gives buy/sell order
			if (current_order != null) {
				forms[i].setBuySell(current_order.buysell);
				forms[i].setPrice(current_order.price);
				
				// Determine order volume according to (contribution) coefficient
				int quant = (int)(this.weights[i] * current_order.quant);
				forms[i].setQuantity(quant);
				if (quant == 0) {
					forms[i].setBuySell(UOrderForm.NONE);
				}
			}
		}
		
		// Saving the latest orders from sub-strategies for adjustment algorithm
		this.prev_orders = forms;
		return forms;
  }

}
