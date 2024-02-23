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
 * The order form class.
 */
public class UOrderForm {
	
	/** The constant representing to do nothing. */
	public static final int NONE = 0;
	
	/** The constant representing to place sell orders. */
	public static final int SELL = 1;
	
	/** The constant representing to place buy orders. */
	public static final int BUY = 2;
	
	/** The order division. This variable takes NONE(=0), SELL(=1) or BUY(=2). */
	private int fBuySell;
	
	/** The constant representing that the order price is not initialized appropriately. */
	public static final int INVALID_PRICE = -1;
	
	/** The order price. */
	private int fPrice;
	
	/** The constant representing that the order volume is not initialized appropriately. */
	public static final int INVALID_QUANTITY = -1;

	/** The order volume. */
	private int fQuantity;
	
	/**
	 * Constructor.
	 */
	public UOrderForm() {
		fBuySell = UOrderForm.NONE;
		fPrice = UOrderForm.INVALID_PRICE;
		fQuantity = UOrderForm.INVALID_QUANTITY;
	}
	
	/**
	 * Copy constructor.
	 * @param src The source object to be copied to this object.
	 */
	public UOrderForm(UOrderForm src) {
		fBuySell = src.fBuySell;
		fPrice = src.fPrice;
		fQuantity = src.fQuantity;
	}
	
	/**
	 * Copy method.
	 * @param The source object to be copied to this object.
	 * @return This object.
	 */
	public UOrderForm copyFrom(UOrderForm src) {
		fBuySell = src.fBuySell;
		fPrice = src.fPrice;
		fQuantity = src.fQuantity;
		return this;
	}

	/**
	 * Returns the order division.
	 * @return buySell NONE, SELL or BUY.
	 */
	public int getBuySell() {
		return fBuySell;
	}
	
	/**
	 * Sets the order division.
	 * @param buySell NONE, SELL or BUY.
	 */
	public void setBuySell(int buySell) {
		fBuySell = buySell;
	}

	/**
	 * Returns the order price.
	 * @return price The order price.
	 */
	public int getPrice() {
		return fPrice;
	}

	/**
	 * Sets the order price.
	 * @param price The order price.
	 */
	public void setPrice(int price) {
		fPrice = price;
	}

	/**
	 * Returns the order volume.
	 * @return quantity The order volume.
	 */
	public int getQuantity() {
		return fQuantity;
	}

	/**
	 * Sets the order volume.
	 * @param quantity The order volume.
	 */
	public void setQuantity(int quantity) {
		fQuantity = quantity;
	}
	
	/**
	 * Returns the order division as String type.
	 * @return A string of the order division.
	 */
	public String getBuySellByString() {
		if (fBuySell == UOrderForm.BUY) {
			return "Buy";
		} else if (fBuySell == UOrderForm.SELL) {
			return "Sell";
		} else {
			return "None";
		}
	}
	
}
