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
 * �f�C�E�g���[�f�C���O�̏����헪�ł��D
 * ���蒍���Ɣ��������𓯎��ɏo���܂��D
 * ���蒍���̎w�l�͒��O�̐敨���i���������C ���������̎w�l�͒��O�̐敨���i�����������܂��D
 */
public class DayTradeStrategy extends Strategy {

  /** �������i�̕��̃f�t�H���g�l */
  public static final int DEFAULT_WIDTH_OF_PRICE = 20;

  /** �����ʂ̍ő�l�̃f�t�H���g�l */
  public static final int DEFAULT_MAX_QUANT = 50;

  /** �����ʂ̍ŏ��l�̃f�t�H���g�l */
  public static final int DEFAULT_MIN_QUANT = 10;

  /** �|�W�V�����̍ő�l�̃f�t�H���g�l */
  public static final int DEFAULT_MAX_POSITION = 300;

  /** �����̘�����̃f�t�H���g�l */
  public static final double DEFAULT_SPREAD_RATIO = 0.01;

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

  /** �����̘����� */
  private double fSpreadRatio = DEFAULT_SPREAD_RATIO;

  /**
   * �R���X�g���N�^�ł��D
   * @param seed �����̎�
   */
  public DayTradeStrategy(int seed) {
    fRandom = new Random(seed); // Initialization of random sequence.
  }

  /*
   * (non-Javadoc)
   * @see strategy.Strategy#action(int[], int[], int, long, int)
   */
  public void action(int[] spotPrices, int[] futurePrices, int position,
                     long money, int restSessions) {
    Order buyOrder = getBuyOrder(spotPrices, futurePrices, position, money, restSessions);
    Order sellOrder = getSellOrder(spotPrices, futurePrices, position, money, restSessions);
    orderRequest(buyOrder);
    orderRequest(sellOrder);
  }

  /**
   * ����������Ԃ��D
   * @param spotPrices �������i�n��
   * @param futurePrices �敨���i�n��
   * @param position �|�W�V����
   * @param money �����c��
   * @param restSessions �c��̐ߐ�
   * @return�@��������
   */
  public Order getBuyOrder(int[] spotPrices, int[] futurePrices, int position,
                           long money, int restSessions) {
    Order order = new Order(); // Object to return values.
    order.buysell = Order.BUY;
    // Cancel decision if it may increase absolute value of the position
    if (position > fMaxPosition) {
      order.buysell = Order.NONE;
      return order;
    }
    int latestFuturePrice = futurePrices[futurePrices.length - 1];
    if (latestFuturePrice < 0) {
      order.buysell = Order.NONE;
      return order;
    } // If previous price is invalid, it makes no order.
    order.price = (int) ( (double) latestFuturePrice * (1.0 - fSpreadRatio));
    if (order.price < 1) {
      order.price = 1;
    }
    order.quant = fMinQuant + fRandom.nextInt(fMaxQuant - fMinQuant + 1);
    // Message
    message("Buy" + ", price = " + order.price + ", volume = " + order.quant + "  )");
    return order;
  }

  /**
   * ���蒍����Ԃ��D
   * @param spotPrices �������i�n��
   * @param futurePrices �敨���i�n��
   * @param position �|�W�V����
   * @param money �����c��
   * @param restSessions �c��̐ߐ�
   * @return�@���蒍��
   */
  public Order getSellOrder(int[] spotPrices, int[] futurePrices, int position,
                            long money, int restSessions) {
    Order order = new Order(); // Object to return values.
    order.buysell = Order.SELL;
    // Cancel decision if it may increase the position
    if (position < -fMaxPosition) {
      order.buysell = Order.NONE;
      return order;
    }
    int latestFuturePrice = futurePrices[futurePrices.length - 1];
    if (latestFuturePrice < 0) {
      order.buysell = Order.NONE;
      return order;
    } // If previous price is invalid, it makes no order.
    order.price = (int) ( (double) latestFuturePrice * (1.0 + fSpreadRatio));
    order.quant = fMinQuant + fRandom.nextInt(fMaxQuant - fMinQuant + 1);
    // Message
    message("Sell" + ", price = " + order.price + ", volume = " + order.quant + "  )");
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
      } else if (key.equals("SpreadRatio")) {
        fSpreadRatio = Double.parseDouble(value);
      } else {
        message("Unknown parameter:" + key + " in RandomStrategy.setParameters");
      }
    }
  }

}
