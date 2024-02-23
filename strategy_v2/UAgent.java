/**
 * Copyright (c) 2001-2008 U-Mart Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * ---------------------------------------------------------------------
 */
package strategy_v2;

import java.util.ArrayList;
import java.util.HashMap;

import serverCore.UOrder;
import serverCore.UServerStatus;
import strategyCore.UBaseAgent;
import cmdCore.UCBalancesCore;
import cmdCore.UCFuturePriceCore;
import cmdCore.UCOrderRequestCore;
import cmdCore.UCPositionCore;
import cmdCore.UCSpotPriceCore;
import cmdCore.UCommandStatus;

/**
 * The agent class.
 */
public class UAgent extends UBaseAgent {

	/** The length of the spot price series. */
  public static final int NO_OF_SPOT_PRICES = 120;

  /** The length of the futures price series. */
  public static final int NO_OF_FUTURE_PRICES = 60;

  /**
   * Constructor.
   * @param loginName A login name.
   * @param passwd A password.
   * @param realName A real name.
   * @param seed A random seed.
   */
  public UAgent(String loginName, String passwd, String realName, int seed) {
	  super(loginName, passwd, realName, seed);
	}

  /**
   * Returns the latest spot price series.
   * @return The latest spot price series.
   */
  private int[] getSpotPrices() {
    UCSpotPriceCore cmd = (UCSpotPriceCore)fUmcp.getCommand(UCSpotPriceCore.CMD_NAME);
    cmd.setArguments("j30", UAgent.NO_OF_SPOT_PRICES);
    UCommandStatus status = cmd.doIt();
    if (status.getStatus() == false) {
      System.err.println("UserName:" + fLoginName + ", UserID:" + fUserID);
      System.err.println(status.getErrorMessage() + " in UAgent.getSpotPrices");
      System.exit(5);
    }
    ArrayList spotList = cmd.getResults();
    if (spotList.size() != UAgent.NO_OF_SPOT_PRICES) {
      System.err.println("spotList.size() != this.NO_OF_SPOT_PRICES in UAgent.doActions");
      System.exit(5);
    }
    int[] spotPrices = new int[UAgent.NO_OF_SPOT_PRICES];
    for (int i = 0; i < UAgent.NO_OF_SPOT_PRICES; ++i) {
      HashMap elem = (HashMap) spotList.get(i);
      spotPrices[UAgent.NO_OF_SPOT_PRICES - i - 1] = (int)((Long)elem.get(UCSpotPriceCore.LONG_PRICE)).longValue();
    }
    return spotPrices;
  }

  /**
   * Returns the latest futures price series.
   * @return The latest futures price series.
   */
  private int[] getFuturePrices() {
    UCFuturePriceCore cmd = (UCFuturePriceCore)fUmcp.getCommand(UCFuturePriceCore.CMD_NAME);
    cmd.setArguments("j30", UAgent.NO_OF_FUTURE_PRICES);
    UCommandStatus status = cmd.doIt();
    if (status.getStatus() == false) {
      System.err.println("UserName:" + fLoginName + ", UserID:" + fUserID);
      System.err.println(status.getErrorMessage() +
                         " in UAgent.getFuturePrices");
      System.exit(5);
    }
    ArrayList futureList = cmd.getResults();
    if (futureList.size() != UAgent.NO_OF_FUTURE_PRICES) {
      System.err.println("futureList.size():" + futureList.size() +
                         " != this.NO_OF_FUTURE_PRICES" + NO_OF_FUTURE_PRICES +
                         " in UAgent.getFuturePrices");
      System.exit(5);
    }
    int[] futurePrices = new int[UAgent.NO_OF_FUTURE_PRICES];
    for (int i = 0; i < UAgent.NO_OF_FUTURE_PRICES; ++i) {
      HashMap elem = (HashMap)futureList.get(i);
      futurePrices[UAgent.NO_OF_FUTURE_PRICES - i - 1] = (int)((Long)elem.get(UCFuturePriceCore.LONG_PRICE)).longValue();
    }
    return futurePrices;
  }

  /**
   * Returns the current position.
   * @return The current position.
   */
  private int getPosition() {
    UCPositionCore cmd = (UCPositionCore)fUmcp.getCommand(UCPositionCore.CMD_NAME);
    UCommandStatus status = cmd.doIt();
    if (status.getStatus() == false) {
      System.err.println("UserName:" + fLoginName + ", UserID:" + fUserID +
                         " in UAgent.getPosition");
      System.err.println(status.getErrorMessage() + " in UAgent.getPosition");
      System.exit(5);
    }
    HashMap result = cmd.getResults();
    long todayBuy = ( (Long) result.get(UCPositionCore.LONG_TODAY_BUY)).
        longValue();
    long todaySell = ( (Long) result.get(UCPositionCore.LONG_TODAY_SELL)).
        longValue();
    long yesterdayBuy = ( (Long) result.get(UCPositionCore.LONG_YESTERDAY_BUY)).
        longValue();
    long yesterdaySell = ( (Long) result.get(UCPositionCore.LONG_YESTERDAY_SELL)).
        longValue();
    long buy = todayBuy + yesterdayBuy;
    long sell = todaySell + yesterdaySell;
    return (int) (buy - sell);
  }

  /**
   * Returns the current cash.
   * @return The current cash.
   */
  private long getMoney() {
    UCBalancesCore cmd = (UCBalancesCore)fUmcp.getCommand(UCBalancesCore.CMD_NAME);
    UCommandStatus status = cmd.doIt();
    if (status.getStatus() == false) {
      System.err.println("UserName:" + fLoginName + ", UserID:" + fUserID);
      System.err.println(status.getErrorMessage() + " in UAgent.getMoney");
      System.exit(5);
    }
    HashMap bal = cmd.getTodayResults();
    return ( (Long) bal.get(UCBalancesCore.LONG_SURPLUS)).longValue();
  }

  /**
   * Sends the order forms returned by the makeOrderForms method to the exchange server.
   * @param day The current date.
   * @param session The current session
   * @param serverState The state of the server.
   * @param maxDays The transaction period.
   * @param noOfSessionsPerDay The number of sessions per day.
   */
  public void doActions(int day, int session, int serverState,
                         int maxDays, int noOfSessionsPerDay) {
    if (serverState != UServerStatus.ACCEPT_ORDERS) {
      return;
    }
    int[] spotPrices = getSpotPrices();
    int[] futurePrices = getFuturePrices();
    int position = getPosition();
    long money = getMoney();
    UOrderForm[] forms = makeOrderForms(day, session, maxDays, noOfSessionsPerDay,
                                        spotPrices, futurePrices, position, money);
    for (int i = 0; i < forms.length; ++i) {
    	orderRequest(forms[i]);
    }
  }

  /**
   * Sends an order form to the exchange server.
   * @param form An order form
   */
  protected void orderRequest(UOrderForm form) {
    UCOrderRequestCore cmd = (UCOrderRequestCore)fUmcp.getCommand(UCOrderRequestCore.CMD_NAME);
    int sellBuy;
    if (form.getBuySell() == UOrderForm.BUY) {
      sellBuy = UOrder.BUY;
    } else if (form.getBuySell() == UOrderForm.SELL) {
      sellBuy = UOrder.SELL;
    } else {
      return;
    }
    if (form.getPrice() <= 0 || form.getQuantity() <= 0) {
    	return;
    }
    cmd.setArguments("j30", UOrder.NEW, sellBuy, UOrder.LIMIT, form.getPrice(), form.getQuantity());
    UCommandStatus status = cmd.doIt();
    if (status.getStatus() == false) {
      System.err.println("UserName:" + fLoginName + ", UserID:" + fUserID);
      System.err.println(status.getErrorMessage() + " in UAgent.orderRequest");
    }
  }

  /**
   * Returns the number of sessions to the due date.
   * @param day The date.
   * @param session The session.
   * @param maxDays The transaction period.
   * @param noOfSessionsPerDay The number of sessions per day.
   * @returnÅ@The number of sessions to the due date.
   */
  public int calculateRestSessions(int day, int session,
                               int maxDays, int noOfSessionsPerDay) {
    return (maxDays - day) * noOfSessionsPerDay + noOfSessionsPerDay - session + 1;
  }

  /**
   * Returns the latest contracted price. Note that the price is -1 when uncontracted.
   * @param prices A price series. Note that prices[prices.length - 1] is the latest price.
   * @return The latest contracted price.
   */
  public int getLatestPrice(int[] prices) {
    for (int i = prices.length - 1; i >= 0; --i) {
      if (prices[i] >= 0) {
        return prices[i];
      }
    }
    return UOrderForm.INVALID_PRICE;
  }

  /**
   * Makes one or more order forms to return them.
   * Make sure to override this method in the child class because it returns just an order form of doing nothing as default.
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
  	UOrderForm[] forms = new UOrderForm [1];
  	forms[0] = new UOrderForm();
  	return forms;
  }

}
