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

import cmdCore.*;
import serverCore.*;
import strategyCore.UBaseAgent;

/**
 * �헪�N���X�ł��D
 */
public class Strategy extends UBaseAgent {

	/** �擾���錻�����i�n��̒��� */
  public static final int NO_OF_SPOT_PRICES = 120;

  /** �擾����敨���i�n��̒��� */
  public static final int NO_OF_FUTURE_PRICES = 60;

  /**
   * �R���X�g���N�^�ł��D
   */
  public Strategy() {
    super("NotInitialized", "NotInitialized", "NotInitialized", 1);
  }

  /**
   * �������i�n���Ԃ��܂��D
   * @return �������i�n��
   */
  private int[] getSpotPrices() {
    UCSpotPriceCore cmd = (UCSpotPriceCore) fUmcp.getCommand("SpotPrice");
    cmd.setArguments("j30", Strategy.NO_OF_SPOT_PRICES);
    UCommandStatus status = cmd.doIt();
    if (status.getStatus() == false) {
      System.err.println("UserName:" + fLoginName + ", UserID:" + fUserID);
      System.err.println(status.getErrorMessage() +
                         " in Strategy.getSpotPrices");
      System.exit(5);
    }
    ArrayList spotList = cmd.getResults();
    if (spotList.size() != Strategy.NO_OF_SPOT_PRICES) {
      System.err.println(
          "spotList.size() != this.NO_OF_SPOT_PRICES in Strategy.doActions");
      System.exit(5);
    }
    int[] spotPrices = new int[Strategy.NO_OF_SPOT_PRICES];
    for (int i = 0; i < Strategy.NO_OF_SPOT_PRICES; ++i) {
      HashMap elem = (HashMap) spotList.get(i);
      spotPrices[Strategy.NO_OF_SPOT_PRICES - i -
          1] = (int) ( (Long) elem.get(UCSpotPriceCore.LONG_PRICE)).longValue();
    }
    return spotPrices;
  }

  /**
   * �敨���i�n���Ԃ��܂��D
   * @return �敨���i�n��
   */
  private int[] getFuturePrices() {
    UCFuturePriceCore cmd =
        (UCFuturePriceCore) fUmcp.getCommand("FuturePrice");
    cmd.setArguments("j30", Strategy.NO_OF_FUTURE_PRICES);
    UCommandStatus status = cmd.doIt();
    if (status.getStatus() == false) {
      System.err.println("UserName:" + fLoginName + ", UserID:" + fUserID);
      System.err.println(status.getErrorMessage() +
                         " in Strategy.getFuturePrices");
      System.exit(5);
    }
    ArrayList futureList = cmd.getResults();
    if (futureList.size() != Strategy.NO_OF_FUTURE_PRICES) {
      System.err.println("futureList.size():" + futureList.size() +
                         " != this.NO_OF_FUTURE_PRICES" + NO_OF_FUTURE_PRICES +
                         " in Strategy.getFuturePrices");
      System.exit(5);
    }
    int[] futurePrices = new int[Strategy.NO_OF_FUTURE_PRICES];
    for (int i = 0; i < Strategy.NO_OF_FUTURE_PRICES; ++i) {
      HashMap elem = (HashMap) futureList.get(i);
      futurePrices[Strategy.NO_OF_FUTURE_PRICES - i -
          1] = (int) ( (Long) elem.get(UCFuturePriceCore.LONG_PRICE)).longValue();
    }
    return futurePrices;
  }

  /**
   * �|�W�V������Ԃ��܂��D
   * @return �|�W�V����
   */
  private int getPosition() {
    UCPositionCore cmd = (UCPositionCore) fUmcp.getCommand("Position");
    UCommandStatus status = cmd.doIt();
    if (status.getStatus() == false) {
      System.err.println("UserName:" + fLoginName + ", UserID:" + fUserID +
                         " in Strategy.getPosition");
      System.err.println(status.getErrorMessage() + " in Strategy.getPosition");
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
   * �����c����Ԃ��܂��D
   * @return �����c��
   */
  private long getMoney() {
    UCBalancesCore cmd = (UCBalancesCore) fUmcp.getCommand("Balances");
    UCommandStatus status = cmd.doIt();
    if (status.getStatus() == false) {
      System.err.println("UserName:" + fLoginName + ", UserID:" + fUserID);
      System.err.println(status.getErrorMessage() + " in Strategy.getMoney");
      System.exit(5);
    }
    HashMap bal = cmd.getTodayResults();
    return ( (Long) bal.get(UCBalancesCore.LONG_SURPLUS)).longValue();
  }

  /**
   * ���Z���܂ł̐ߐ���Ԃ��܂��D
   * @param date�@��
   * @param boardNo �ߐ�
   * @param maxDate �������
   * @param noOfBoardsPerDay �P��������̐ߐ�
   * @return
   */
  private int getRestDay(int date, int boardNo,
                         int maxDate, int noOfBoardsPerDay) {
    return (maxDate - date) * noOfBoardsPerDay + noOfBoardsPerDay - boardNo + 1;
  }

  /**
   * ���߂̉��i��Ԃ��܂��D
   * @param prices ���i�n��
   * @return ���߂̉��i
   */
  public int getLatestPrice(int[] prices) {
    for (int i = prices.length - 1; i >= 0; --i) {
      if (prices[i] >= 0) {
        return prices[i];
      }
    }
    return -1;
  }

  /**
   * @see strategyCore.UBaseAgent#doActions(int, int, int, int, int)
   */
  public void doActions(int date, int session, int serverState,
                        int maxDate, int noOfSessionsPerDay) {
    if (serverState != UServerStatus.ACCEPT_ORDERS) {
      return;
    }
    int[] spotPrices = getSpotPrices();
    int[] futurePrices = getFuturePrices();
    int pos = getPosition();
    long money = getMoney();
    int restDay = getRestDay(date, session, maxDate, noOfSessionsPerDay);
    action(spotPrices, futurePrices, pos, money, restDay);
  }

  /**
   * �P��̒��������肷�郁�\�b�h�ł��D��������������Ď���헪���������܂��D���̃��\�b�h�́Caction���\�b�h�����������Ȃ��ꍇ�C1�߂�����1��R�[������܂��D
   * @param spotPrices �������i�̌n��D�z��v�f��0�Ԗڂ���119�Ԗڂ܂ł�120�������i�[����Ă���D119�Ԗڂ��ŐV�̌������i�D
   * @param futurePrices �敨���i�̌n��D�z��v�f��0�Ԗڂ���59�Ԗڂ܂ł�60�������i�[����Ă���D59�Ԗڂ��ŐV�̉��i�D�s�ꂪ�J���O�͌������i���ς��ɑ������Ă���D�܂��C�s��Ŏ�������������ɉ��i�����肵�Ȃ������ꍇ�͒l'-1'���������Ă���D
   * @param position �G�[�W�F���g�̃|�W�V�����D���Ȃ�Δ����z���C���Ȃ�Δ���z����\���D
   * @param money �G�[�W�F���g�ۗ̕L���錻���c���D�^��long�ł��邱�Ƃɒ��ӂ��邱�ƁD
   * @param restSessions �s�ꂪ����܂ł̎c���Ă���ߐ��i�񂹂̉񐔁j
   * @return ����
   */
  public Order getOrder(int[] spotPrices, int[] futurePrices, int position,
                        long money, int restSessions) {
    return null;
  }

  /**
   * ���������邽�߂̃��\�b�h�ł��D
   * �����P��łP�����̒���������̂Ȃ�΁C���̂܂܂ɂ��Ă����܂��D
   * �����̒������s�������̂Ȃ�΁CgetOrder�ȊO�̃��\�b�h��p�ӂ��CorderRequest���\�b�h�𕡐���Ăׂ΂悢�D
   * @param spotPrices �������i�̌n��D�z��v�f��0�Ԗڂ���119�Ԗڂ܂ł�120�������i�[����Ă���D119�Ԗڂ��ŐV�̌������i�D
   * @param futurePrices �敨���i�̌n��D�z��v�f��0�Ԗڂ���59�Ԗڂ܂ł�60�������i�[����Ă���D59�Ԗڂ��ŐV�̉��i�D�s�ꂪ�J���O�͌������i���ς��ɑ������Ă���D�܂��C�s��Ŏ�������������ɉ��i�����肵�Ȃ������ꍇ�͒l'-1'���������Ă���D
   * @param position �G�[�W�F���g�̃|�W�V�����D���Ȃ�Δ����z���C���Ȃ�Δ���z����\���D
   * @param money �G�[�W�F���g�ۗ̕L���錻���c���D�^��long�ł��邱�Ƃɒ��ӂ��邱�ƁD
   * @param restSessions �s�ꂪ����܂ł̎c���Ă���ߐ��i�񂹂̉񐔁j
   */
  public void action(int[] spotPrices, int[] futurePrices, int position, long money, int restSessions) {
    Order o = getOrder(spotPrices, futurePrices, position, money, restSessions);
    if (o == null) {
      System.err.println("Strategy.getOrder returned null in Strategy.action");
      return;
    }
    orderRequest(o);
  }

  /**
   * �T�[�o�֒����𑗂�܂��D
   * @param o ����
   */
  public void orderRequest(Order o) {
    UCOrderRequestCore cmd = (UCOrderRequestCore) fUmcp.getCommand("OrderRequest");
    int sellBuy;
    if (o.buysell == Order.BUY) {
      sellBuy = UOrder.BUY;
    } else if (o.buysell == Order.SELL) {
      sellBuy = UOrder.SELL;
    } else {
      return;
    }
    if (o.price <= 0 || o.quant <= 0) {
      // System.err.println("ERORR!!!");
      // System.err.println("price=" + o.price + ", quant=" + o.quant);
      try {
        throw new Exception();
      } catch (Exception e) {
        // TODO Auto-generated catch block
        // e.printStackTrace();
        return;
      }
    }
    cmd.setArguments("j30", UOrder.NEW, sellBuy, UOrder.LIMIT, o.price, o.quant);
    UCommandStatus status = cmd.doIt();
    if (status.getStatus() == false) {
      System.err.println("UserName:" + fLoginName + ", UserID:" + fUserID);
      System.err.println(status.getErrorMessage() + " in Strategy.orderRequest");
    }
  }

}
