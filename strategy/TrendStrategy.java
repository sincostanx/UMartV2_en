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
package strategy;

import java.util.*;

/**
 * ���̃G�[�W�F���g�́C�O�߂ƑO�X�߂̐敨���i���r���ď㏸�X���ɂ��鎞�ɔ����������C
 * ���~�X���ɂ��鎞�ɔ��蒍�����o���܂��D�����w�l�́C�O�߂̐敨���i�𒆐S�Ƀ����_���Ɍ��߂܂��D
 * �������ʂ́C���O�Ɏw�肵���͈͓��Ń����_���Ɍ��߂܂��D�����̃|�W�V���������ʂ𒴂���ƁC
 * ����ȏ�|�W�V�����𑝂₷�����͏o���܂���D
 */
public class TrendStrategy extends Strategy {

	/** �������i�̕��̃f�t�H���g�l */
	public static final int DEFAULT_WIDTH_OF_PRICE = 20;

	/** �����ʂ̍ő�l�̃f�t�H���g�l */
	public static final int DEFAULT_MAX_QUANT = 50;

	/** �����ʂ̍ŏ��l�̃f�t�H���g�l */
	public static final int DEFAULT_MIN_QUANT = 10;

	/** �|�W�V�����̍ő�l�̃f�t�H���g�l */
	public static final int DEFAULT_MAX_POSITION = 300;

	/** ��������瓾����S�Ẳ��i��񂪖����̂Ƃ��Ɏg�����i�̃f�t�H���g�l */
	public static final int DEFAULT_NOMINAL_PRICE = 3000;

	/** ���������� */
	private Random fRandom;

	/** �������i�̕� */
	private int fWidthOfPrice = DEFAULT_WIDTH_OF_PRICE;

	/** �����ʂ̍ő�l */
	private int fMaxQuant = DEFAULT_MAX_QUANT;

	/** �����ʂ̍ŏ��l */
	private int fMinQuant = DEFAULT_MIN_QUANT;

	/** �|�W�V�����̍ő�l */
	private int fMaxPosition = DEFAULT_MAX_POSITION;
	 
	/** ��������瓾����S�Ẳ��i��񂪖����̂Ƃ��Ɏg�����i */
	private int fNominalPrice = DEFAULT_NOMINAL_PRICE;

	/**
	 * �R���X�g���N�^�ł��D
	 * @param seed �����̎�
	 */
  public TrendStrategy(int seed) {
    fRandom = new Random(seed);
  }

  /*
   * (non-Javadoc)
   * @see strategy.Strategy#action(int[], int[], int, long, int)
   */
  public void action(int[] spotPrices, int[] futurePrices,
                     int pos, long money, int restDay) {
    Order order = getOrder(spotPrices, futurePrices, pos, money, restDay);
    orderRequest(order);
  }

  /*
   * (non-Javadoc)
   * @see strategy.Strategy#getOrder(int[], int[], int, long, int)
   */
  public Order getOrder(int[] spotPrices, int[] futurePrices,
                        int pos, long money, int restDay) {
    Order order = new Order();
    // Decide buy or sell based on difference of the last two futures prices.
    int price1 = futurePrices[futurePrices.length - 1];
    int price2 = futurePrices[futurePrices.length - 2];
    if (price1 > 0 && price2 > 0) {
      if (price1 > price2) {
        order.buysell = Order.BUY; // upward trend
      } else if (price1 < price2) {
        order.buysell = Order.SELL; // downward trend
      } else {
        order.buysell = Order.NONE; // constant
        return order;
      }
    } else {
      order.buysell = fRandom.nextInt(2) + 1; // randomly by or sell
      // if price is not well defined.
    }
    // Cancel decision if it may increase absolute value of the position
    if (order.buysell == Order.BUY) {
      if (pos > fMaxPosition) {
        order.buysell = Order.NONE;
        return order;
      }
    } else if (order.buysell == Order.SELL) {
      if (pos < -fMaxPosition) {
        order.buysell = Order.NONE;
        return order;
      }
    }
    int prevPrice = getLatestPrice(futurePrices);
    if (prevPrice == -1) {
      prevPrice = getLatestPrice(spotPrices); // use spot price instead
    }
    if (prevPrice == -1) {
      prevPrice = fNominalPrice; // use nominal value instead
    } while (true) {
      order.price = prevPrice + (int) (fWidthOfPrice * fRandom.nextGaussian());
      if (order.price > 0) {
        break;
      }
    }
    order.quant = fMinQuant + fRandom.nextInt(fMaxQuant - fMinQuant + 1);
    // Message
    message(Order.buySellToString(order.buysell)
            + ", price = " + order.price
            + ", volume = " + order.quant
            + " (trend = " + (price1 - price2) + "  )");
    return order;
  }

  /* (non-Javadoc)
   * @see strategy.UBaseStrategy#setParameters(java.lang.String[])
   */
  public void setParameters(String[] args) {
    super.setParameters(args);
    for (int i = 0; i < args.length; ++i) {
      StringTokenizer st = new StringTokenizer(args[i], "= ");
      String key = st.nextToken();
      String value = st.nextToken();
      // System.err.println(key + "=" + value);
      if (key.equals("WidthOfPrice")) {
        fWidthOfPrice = Integer.parseInt(value);
      } else if (key.equals("MinQuant")) {
        fMinQuant = Integer.parseInt(value);
      } else if (key.equals("MaxQuant")) {
        fMaxQuant = Integer.parseInt(value);
      } else if (key.equals("MaxPosition")) {
        fMaxPosition = Integer.parseInt(value);
      } else {
        message("Unknown parameter:" + key + " in RandomStrategy.setParameters");
      }
    }
  }

}
