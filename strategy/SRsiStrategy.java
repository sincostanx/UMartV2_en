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
 * ���̃G�[�W�F���g�́C�������i��RSI�iRelative Strength Index)�ɂ���Ĕ������s�Ȃ��܂��D
 * RSI�́C�L���ȃe�N�j�J�����͎�@�̈�ł��D�w�l�́C�ŐV�̌������i�𒆐S�Ƀ����_���Ɍ��肵�܂��D
 * �܂��C�������ʂ͗^����ꂽ�͈͂Ń����_���ɗ^���܂��D
 */
public class SRsiStrategy extends Strategy {

  /** �������i�̕��̃f�t�H���g�l */
  public static final int DEFAULT_WIDTH_OF_PRICE = 20;

  /** �����ʂ̍ő�l�̃f�t�H���g�l */
  public static final int DEFAULT_MAX_QUANT = 50;

  /** �����ʂ̍ŏ��l�̃f�t�H���g�l */
  public static final int DEFAULT_MIN_QUANT = 15;

  /** �|�W�V�����̍ő�l�̃f�t�H���g�l */
  public static final int DEFAULT_MAX_POSITION = 300;

  /** ��������瓾����S�Ẳ��i��񂪖����̂Ƃ��Ɏg�����i�̃f�t�H���g�l */
  public static final int DEFAULT_NOMINAL_PRICE = 3000;

  /** RSI���v�Z����ۂ̎Q�Ɗ��Ԃ̃f�t�H���g�l */
  public static final int DEFAULT_REFERENCE_TERM = 10;

  /** �G�b�W�o���h�̃f�t�H���g�l */
  public static final double DEFAULT_EDGE_BAND = 0.3;

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

  /** ��������瓾����S�Ẳ��i��񂪌������̂Ƃ��Ɏg�����i */
  private int fNominalPrice = DEFAULT_NOMINAL_PRICE;

  /** RSI���v�Z����ۂ̎Q�Ɗ��� */
  private int fReferenceTerm = DEFAULT_REFERENCE_TERM;

  /** �G�b�W�o���h */
  private double fEdgeBand = DEFAULT_EDGE_BAND;

  /**
   * �R���X�g���N�^�ł��D
   * @param seed �����̎�
   */
  public SRsiStrategy(int seed) {
    fRandom = new Random(seed); // Initialization of random sequence.
  }

  /*
   * (non-Javadoc)
   * @see strategy.Strategy#action(int[], int[], int, long, int)
   */
  public void action(int[] spotPrices, int[] futurePrices, int pos, long money,
                     int restDay) {
    Order order = getOrder(spotPrices, futurePrices, pos, money, restDay);
    orderRequest(order);
  }

  /*
   * (non-Javadoc)
   * @see strategy.Strategy#getOrder(int[], int[], int, long, int)
   */
  public Order getOrder(int[] spotPrices, int[] futurePrices, int pos,
                        long money, int restDay) {
    Order order = new Order(); // Object to return values.
    int oldPrice = -1;
    int scnt = spotPrices.length - fReferenceTerm; // go back scnt by referenceTerm
    while (true) { // Find the well defined oldest price in the time window
      if (scnt == spotPrices.length) { // All the prices are invalid, no order is made
        order.buysell = Order.NONE;
        return order;
      }
      oldPrice = spotPrices[scnt];
      if (oldPrice >= 0) {
        break;
      }
      ++scnt;
    }
    int upSum = 0; // accumulated upward price change
    int downSum = 0; // accumulated downward price change
    while (true) {
      if (scnt == spotPrices.length) {
        break;
      }
      if (spotPrices[scnt] >= 0) { // If price is valid, get up/downward price change
        if (spotPrices[scnt] - oldPrice > 0) {
          upSum += spotPrices[scnt] - oldPrice;
        } else if (spotPrices[scnt] - oldPrice < 0) {
          downSum += oldPrice - spotPrices[scnt];
        }
        oldPrice = spotPrices[scnt];
      }
      ++scnt;
    }
    if (upSum + downSum == 0) { // If there is no price change in the
      // time window, no order is made.
      order.buysell = Order.NONE;
      return order;
    }

    // Calculate RSI for spot prices
    double rsi = (double) upSum / (double) (upSum + downSum);

    // Decide buy or sell based on RSI
    if (rsi < fEdgeBand) {
      order.buysell = Order.BUY; // Make a buy order because upward change
      // is maturing.
    } else if (rsi > 1.0 - fEdgeBand) {
      order.buysell = Order.SELL; // Make a sell order because downward change
      // is maturing.
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
    int prevPrice = getLatestPrice(spotPrices);
    while (true) {
      order.price = prevPrice + (int) (fWidthOfPrice * fRandom.nextGaussian());
      if (order.price > 0) {
        break;
      }
    } // Add normal random number around prevPrice (the latest spot price)
    // with the standard deviation widthOfPrice.
    // If it gets negative, repeat again.

    // Decide quantity of order between [minQuant, maxQuant] randomly.
    order.quant = fMinQuant + fRandom.nextInt(fMaxQuant - fMinQuant + 1);
    // Message
    message(Order.buySellToString(order.buysell) + ", price = " + order.price +
            ", volume = " + order.quant + " (RSI of Spot = " + rsi + " )");
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
      } else if (key.equals("ReferenceTerm")) {
        fReferenceTerm = Integer.parseInt(value);
      } else if (key.equals("EdgeBand")) {
        fEdgeBand = Double.parseDouble(value);
      } else {
        message("Unknown parameter:" + key + " in RsiStrategy.setParameters");
      }
    }
  }

}
