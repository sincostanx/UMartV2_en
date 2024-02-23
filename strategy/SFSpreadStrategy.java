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
 * ���̃G�[�W�F���g�́C�������i�Ɛ敨���i�̊Ԃ�臒l�������������ꍇ�ɒ������o���܂��D
 * �敨���i�̕����������i����������Δ����������C������Δ��蒍�����o���܂��D
 */
public class SFSpreadStrategy extends Strategy {

  /** �������i�̕��̃f�t�H���g�l */
  public static final int DEFAULT_WIDTH_OF_PRICE = 20;

  /** �����ʂ̍ő�l�̃f�t�H���g�l */
  public static final int DEFAULT_MAX_QUANT = 50;

  /** �����ʂ̍ŏ��l�̃f�t�H���g�l */
  public static final int DEFAULT_MIN_QUANT = 10;

  /** �|�W�V�����̍ő�l�̃f�t�H���g�l */
  public static final int DEFAULT_MAX_POSITION = 300;

  /** �������臒l�̃f�t�H���g�l */
  public static final double DEFAULT_SPREAD_RATIO_THRESHOLD = 0.01;

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

  /** �������臒l */
  private double fSpreadRatioThreshold = DEFAULT_SPREAD_RATIO_THRESHOLD;

  /**
   * �R���X�g���N�^�ł��D
   * @param seed �����̎�
   */
  public SFSpreadStrategy(int seed) {
    fRandom = new Random(seed); // Initialization of random sequence.
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
    if (spreadRatio <= -fSpreadRatioThreshold) {
      order.buysell = Order.BUY; //
    } else if (spreadRatio >= fSpreadRatioThreshold) {
      order.buysell = Order.SELL; //
    } else {
      order.buysell = Order.NONE; //
      return order;
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
    double meanPrice = (double) (latestFuturePrice + latestSpotPrice) / 2.0;
    double sigma = Math.abs( (latestFuturePrice - latestSpotPrice) / 2.0);
    while (true) {
      order.price = (int) (meanPrice + (sigma * fRandom.nextGaussian()));
      if (order.price > 0.0) {
        break;
      }
    }
    order.quant = fMinQuant + fRandom.nextInt(fMaxQuant - fMinQuant + 1);
    message(Order.buySellToString(order.buysell)
            + ", price = " + order.price
            + ", volume = " + order.quant
            + " (spreadRatio = " + spreadRatio + " )");
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
      if (key.equals("WidthOfPrice")) {
        fWidthOfPrice = Integer.parseInt(value);
      } else if (key.equals("MinQuant")) {
        fMinQuant = Integer.parseInt(value);
      } else if (key.equals("MaxQuant")) {
        fMaxQuant = Integer.parseInt(value);
      } else if (key.equals("MaxPosition")) {
        fMaxPosition = Integer.parseInt(value);
      } else if (key.equals("SpreadRatioThreshold")) {
        fSpreadRatioThreshold = Double.parseDouble(value);
      } else {
        message("Unknown parameter:" + key + " in RandomStrategy.setParameters");
      }
    }
  }

}
