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


/**
 * A template class for your own agent.
 * Make a new agent class by copying this class and rename it to "U[Your student ID]".
 */
public class UTemplateAgent extends UAgent {

	// Write necessary field variables here.

  /**
   * Constructor.
   * @param loginName A login name.
   * @param passwd A password.
   * @param realName A real name.
   * @param seed A random seed.
   */
	public UTemplateAgent(String loginName, String passwd, String realName, int seed) {
		super(loginName, passwd, realName, seed);

		// Initialize the field variables here.

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

		// Prints the current date, the current session and the current spot price.
		println("day=" + day + ", session=" + session + ", spot=" +  spotPrices[spotPrices.length - 1]);

		// Makes an order form of doing nothing and returns it.
		UOrderForm[] forms = new UOrderForm[1];
		forms[0] = new UOrderForm();
    forms[0].setBuySell(UOrderForm.NONE);
    return forms;
  }

}
