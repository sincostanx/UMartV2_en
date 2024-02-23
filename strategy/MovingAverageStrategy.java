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
 * ���̃G�[�W�F���g�́C�敨���i�̒Z���ړ����ςƒ����ړ����ς����������Ƃ��ɔ����������o���܂��D
 * �܂��C�Z���ړ����ς��㏸�X���ɂ��鎞�͔�������,���~�X���ɂ��鎞�͔��蒍�����o���܂��D
 */
public class MovingAverageStrategy extends Strategy {

  /** �����ʂ̍ő�l�̃f�t�H���g�l */
  public static final int DEFAULT_MAX_QUANT = 50;

  /** �����ʂ̍ŏ��l�̃f�t�H���g�l */
  public static final int DEFAULT_MIN_QUANT = 15;

  /** �|�W�V�����̍ő�l�̃f�t�H���g�l */
  public static final int DEFAULT_MAX_POSITION = 30;

  /** ��������瓾����S�Ẳ��i��񂪖����̂Ƃ��Ɏg�����i�̃f�t�H���g�l */
  public static final int DEFAULT_NOMINAL_PRICE = 3000;

  /** �Z���ړ����ς��v�Z������Ԃ̃f�t�H���g�l */
  public static final int DEFAULT_SHORT_REFERENCE_TERM = 10;

  /** �����ړ����ς��v�Z������Ԃ̃f�t�H���g�l */
  public static final int DEFAULT_MEDIUM_REFERENCE_TERM = 20;

  /** ���������� */
  private Random fRandom;

  /** �����ʂ̍ő�l */
  private int fMaxQuant = DEFAULT_MAX_QUANT;

  /** �����ʂ̍ŏ��l */
  private int fMinQuant = DEFAULT_MIN_QUANT;

  /** �|�W�V�����̍ő�l */
  private int fMaxPosition = DEFAULT_MAX_POSITION;

  /** ��������瓾����S�Ẳ��i��񂪌������̂Ƃ��Ɏg�����i */
  private int fNominalPrice = DEFAULT_NOMINAL_PRICE;

  /** �Z���ړ����ς��v�Z������Ԃ̃f�t�H���g�l */
  private int fShortReferenceTerm = DEFAULT_SHORT_REFERENCE_TERM;

  /** �Z���ړ����ς��v�Z������Ԃ̃f�t�H���g�l */
  private int fMediumReferenceTerm = DEFAULT_MEDIUM_REFERENCE_TERM;

  /**
   * �R���X�g���N�^�ł��D
   * @param seed �����̎�
   */
  public MovingAverageStrategy(int seed) {
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
    double shortTermMA = calculateMovingAverage(futurePrices, fShortReferenceTerm, 0);
    if (shortTermMA < 0.0) {
      order.buysell = Order.NONE;
      return order;
    }
    double mediumTermMA = calculateMovingAverage(futurePrices, fMediumReferenceTerm, 0);
    if (mediumTermMA < 0.0) {
      order.buysell = Order.NONE;
      return order;
    }
    double previousShortTermMA = calculateMovingAverage(futurePrices, fShortReferenceTerm, 1);
    if (previousShortTermMA < 0.0) {
      order.buysell = Order.NONE;
      return order;
    }
    double previousMediumTermMA = calculateMovingAverage(futurePrices, fMediumReferenceTerm, 1);
    if (previousMediumTermMA < 0.0) {
      order.buysell = Order.NONE;
      return order;
    }
    // Decide BUY or SELL
    if (shortTermMA == previousShortTermMA) {
      order.buysell = Order.NONE;
      return order;
    } else if (previousShortTermMA < shortTermMA) {
      if (previousMediumTermMA > previousShortTermMA
          && mediumTermMA > shortTermMA) {
        order.buysell = Order.NONE;
        return order;
      } else if (previousMediumTermMA < previousShortTermMA &&
                 mediumTermMA < shortTermMA) {
        order.buysell = Order.NONE;
        return order;
      } else {
        order.buysell = Order.BUY;
      }
    } else if (previousShortTermMA > shortTermMA) {
      if (previousMediumTermMA > previousShortTermMA
          && mediumTermMA > shortTermMA) {
        order.buysell = Order.NONE;
        return order;
      } else if (previousMediumTermMA < previousShortTermMA &&
                 mediumTermMA < shortTermMA) {
        order.buysell = Order.NONE;
        return order;
      } else {
        order.buysell = Order.SELL;
      }
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
    /* Decide limit price */
    int latestPrice = getLatestPrice(futurePrices);
    boolean flag = false;
    int previousLatestPrice = -1;
    for (int i = futurePrices.length - 1; i >= 0; --i) {
      if (futurePrices[i] >= 0 && flag == false) {
        flag = true;
      } else if (futurePrices[i] >= 0 && flag == true) {
        previousLatestPrice = futurePrices[i];
        break;
      }
    }
    int widthOfPrice = Math.abs(latestPrice - previousLatestPrice);
    while (true) {
      if (order.buysell == Order.BUY) {
        order.price = latestPrice + widthOfPrice + (int)(widthOfPrice / 4 * fRandom.nextGaussian());
      } else if (order.buysell == Order.SELL) {
        order.price = latestPrice - widthOfPrice + (int)(widthOfPrice / 4 * fRandom.nextGaussian());
      }
      if (order.price > 0) {
        break;
      }
    }
    order.quant = fMinQuant + fRandom.nextInt(fMaxQuant - fMinQuant + 1);
    message(Order.buySellToString(order.buysell)
            + ", price = " + order.price
            + ", volume = " + order.quant
            + ", short term moving average = " + shortTermMA
            + ", medium term moving average = " + mediumTermMA
            + ", previous short term moving average = " + previousShortTermMA
            + ", previous medium term moving average = " + previousMediumTermMA);
    return order;
  }

  /* Calculate short term moving Average of Futures Prices */

  /**
   * �ړ����ϒl���v�Z���ĕԂ��܂��D
   * @param prices ���i�n��
   * @param referenceTerm �ړ����ς̊���
   * @param startOfIndex �ړ����ς̌v�Z���n�߂鉿�i�̓Y����
   * @return �ړ����ϒl
   */
  private double calculateMovingAverage(int[] prices, int referenceTerm,
                                        int startOfIndex) {
    double movingAverage = 0.0; //
    int count = 0; // consider only valid prices
    // Sum up futures prices
    for (int i = 0; i < referenceTerm; ++i) {
      // skip if the price is negative that means no contract
      if (prices[prices.length - 1 - startOfIndex - i] > 0) {
        movingAverage += prices[prices.length - 1 - startOfIndex - i];
        count++;
      }
    }
    if (count == 0) { // Make no order if all the prices are invalid
      return -1.0;
    }
    movingAverage = (int) (movingAverage / (double) count);
    return movingAverage;
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
      if (key.equals("MinQuant")) {
        fMinQuant = Integer.parseInt(value);
      } else if (key.equals("MaxQuant")) {
        fMaxQuant = Integer.parseInt(value);
      } else if (key.equals("MaxPosition")) {
        fMaxPosition = Integer.parseInt(value);
      } else if (key.equals("ShortReferenceTerm")) {
        fShortReferenceTerm = Integer.parseInt(value);
      } else if (key.equals("MediumReferenceTerm")) {
        fMediumReferenceTerm = Integer.parseInt(value);
      } else {
        message("Unknown parameter:" + key +
                " in MovingAverageStrategy.setParameters");
      }
    }
  }

}
