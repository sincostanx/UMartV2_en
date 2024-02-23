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
package log;

import java.io.*;
import java.util.*;

import serverCore.*;

/**
 * �P�ߕ��̑S�Ă̒������̃��O����舵���N���X�ł��D
 */
public class UOrderCommandLog {

  /** ���������L�[ */
  public static final String INT_DATE = "INT_DATE";

  /** �߂������L�[ */
  public static final String INT_SESSION = "INT_SESSION";

  /** ��������(������)�������L�[ */
  public static final String STRING_REAL_TIME = "STRING_REAL_TIME";

  /** ���O�C�������������߂̃L�[ */
  public static final String STRING_LOGIN_NAME = "STRING_LOGIN_NAME";

  /** �R�}���h���������L�[ */
  public static final String STRING_COMMAND_NAME = "STRING_COMMAND_NAME";

  /** ����ID�������L�[ */
  public static final String LONG_ORDER_ID = "LONG_ORDER_ID";

  /** �������������L�[ */
  public static final String STRING_BRAND_NAME = "STRING_BRAND_NAME";

  /** �V�K�ԍϋ敪�������L�[ */
  public static final String INT_NEW_REPAY = "INT_NEW_REPAY";

  /** �����敪�������L�[ */
  public static final String INT_SELL_BUY = "INT_SELL_BUY";

  /** ���s�w�l�敪�������L�[ */
  public static final String INT_MARKET_LIMIT = "INT_MARKET_LIMIT";

  /** �������i�������L�[ */
  public static final String LONG_ORDER_PRICE = "LONG_ORDER_PRICE";

  /** �������ʂ������L�[ */
  public static final String LONG_ORDER_VOLUME = "LONG_ORDER_VOLUME";

  /** ����񂹊��ԓ��̒������\�[�g���邽�߂ɗ��p����闐�����������߂̃L�[ */
  public static final String INT_RANDOM_NUMBER = "INT_RANDOM_NUMBER";

  /** �������̔z�� */
  private ArrayList fOrderCommandArray;

  /**
   * �R���X�g���N�^�ł��D
   */
  public UOrderCommandLog() {
    fOrderCommandArray = new ArrayList();
  }

  /**
   * ��������o�^���܂��D
   * @param o ����
   */
  public synchronized void registerOrderRequest(UOrder o) {
    HashMap hash = new HashMap();
    hash.put(UOrderCommandLog.INT_DATE, new Integer(o.getDate()));
    hash.put(UOrderCommandLog.INT_SESSION, new Integer(o.getSession()));
    hash.put(UOrderCommandLog.STRING_REAL_TIME, o.getTime());
    hash.put(UOrderCommandLog.STRING_LOGIN_NAME, o.getUserName());
    hash.put(UOrderCommandLog.STRING_COMMAND_NAME, "Request");
    hash.put(UOrderCommandLog.LONG_ORDER_ID, new Long(o.getOrderID()));
    hash.put(UOrderCommandLog.STRING_BRAND_NAME, o.getBrandName());
    hash.put(UOrderCommandLog.INT_NEW_REPAY, new Integer(o.getNewRepay()));
    hash.put(UOrderCommandLog.INT_SELL_BUY, new Integer(o.getSellBuy()));
    hash.put(UOrderCommandLog.INT_MARKET_LIMIT, new Integer(o.getMarketLimit()));
    hash.put(UOrderCommandLog.LONG_ORDER_PRICE, new Long(o.getPrice()));
    hash.put(UOrderCommandLog.LONG_ORDER_VOLUME, new Long(o.getVolume()));
    hash.put(UOrderCommandLog.INT_RANDOM_NUMBER, new Integer(o.getRandomNumber()));
    fOrderCommandArray.add(hash);
  }

  /**
   * �L�����Z������o�^���܂��D
   * @param date ��
   * @param session ��
   * @param userName ���[�U��
   * @param orderID ����ID
   * @param cancelVolume �L�����Z������
   */
  public synchronized void registerOrderCancel(int date, int session,
                                               String userName, long orderID,
                                               long cancelVolume) {
    HashMap hash = new HashMap();
    hash.put(UOrderCommandLog.INT_DATE, new Integer(date));
    hash.put(UOrderCommandLog.INT_SESSION, new Integer(session));
    hash.put(UOrderCommandLog.STRING_REAL_TIME, new Date());
    hash.put(UOrderCommandLog.STRING_LOGIN_NAME, userName);
    hash.put(UOrderCommandLog.STRING_COMMAND_NAME, "Cancel");
    hash.put(UOrderCommandLog.LONG_ORDER_ID, new Long(orderID));
    hash.put(UOrderCommandLog.LONG_ORDER_VOLUME, new Long(cancelVolume));
    fOrderCommandArray.add(hash);
  }

  /**
   * �������̃C�e���[�^��Ԃ��܂��D
   * @return �������̃C�e���[�^
   */
  public Iterator getOrderCommands() {
    return fOrderCommandArray.iterator();
  }

  /**
   * �S�Ă̒��������N���A���܂��D
   */
  public void clear() {
    fOrderCommandArray.clear();
  }

  /**
   * �o�̓X�g���[���փf�[�^�������o���܂��D
   * @param pw �o�̓X�g���[��
   * @throws IOException
   */
  public synchronized void writeTo(PrintWriter pw) throws IOException {
    pw.println(
        "Date,Session,RealTime,LoginName,Command,OrderID,BrandName,NewRepay,"
        + "SellBuy,MarketLimit,Price,Volume,RandomNumber");
    Iterator itr = fOrderCommandArray.iterator();
    while (itr.hasNext()) {
      HashMap hash = (HashMap) itr.next();
      pw.print(hash.get(UOrderCommandLog.INT_DATE).toString() + ",");
      pw.print(hash.get(UOrderCommandLog.INT_SESSION).toString() + ",");
      pw.print(hash.get(UOrderCommandLog.STRING_REAL_TIME) + ",");
      pw.print(hash.get(UOrderCommandLog.STRING_LOGIN_NAME).toString() + ",");
      String cmdName = (String) hash.get(UOrderCommandLog.STRING_COMMAND_NAME);
      pw.print(cmdName + ",");
      pw.print(hash.get(UOrderCommandLog.LONG_ORDER_ID).toString() + ",");
      if (cmdName.equals("Request")) {
        pw.print(hash.get(UOrderCommandLog.STRING_BRAND_NAME).toString() + ",");
        pw.print(hash.get(UOrderCommandLog.INT_NEW_REPAY).toString() + ",");
        pw.print(hash.get(UOrderCommandLog.INT_SELL_BUY).toString() + ",");
        pw.print(hash.get(UOrderCommandLog.INT_MARKET_LIMIT).toString() + ",");
        pw.print(hash.get(UOrderCommandLog.LONG_ORDER_PRICE).toString() + ",");
        pw.print(hash.get(UOrderCommandLog.LONG_ORDER_VOLUME).toString() + ",");
        pw.println(hash.get(UOrderCommandLog.INT_RANDOM_NUMBER).toString());
      } else {
        pw.print(",,,,,");
        pw.println("-" + hash.get(UOrderCommandLog.LONG_ORDER_VOLUME).toString() +
                   ",");
      }
    }
  }

  /**
   * ���̓X�g���[������f�[�^��ǂݍ��݂܂��D
   * @param br ���̓X�g���[��
   * @throws IOException
   */
  public void readFrom(BufferedReader br) throws IOException {
    br.readLine(); // skip the header
    String line = null;
    while ( (line = br.readLine()) != null) {
      StringTokenizer st = new StringTokenizer(line, ",");
      HashMap hash = new HashMap();
      hash.put(UOrderCommandLog.INT_DATE, Integer.valueOf(st.nextToken()));
      hash.put(UOrderCommandLog.INT_SESSION, Integer.valueOf(st.nextToken()));
      hash.put(UOrderCommandLog.STRING_REAL_TIME, st.nextToken());
      hash.put(UOrderCommandLog.STRING_LOGIN_NAME, Integer.valueOf(st.nextToken()));
      String cmdName = st.nextToken();
      hash.put(UOrderCommandLog.STRING_COMMAND_NAME, cmdName);
      hash.put(UOrderCommandLog.LONG_ORDER_ID, Long.valueOf(st.nextToken()));
      if (cmdName.equals("Request")) {
        hash.put(UOrderCommandLog.STRING_BRAND_NAME, st.nextToken());
        hash.put(UOrderCommandLog.INT_NEW_REPAY, Integer.valueOf(st.nextToken()));
        hash.put(UOrderCommandLog.INT_SELL_BUY, Integer.valueOf(st.nextToken()));
        hash.put(UOrderCommandLog.INT_MARKET_LIMIT,
                 Integer.valueOf(st.nextToken()));
        hash.put(UOrderCommandLog.LONG_ORDER_PRICE, Long.valueOf(st.nextToken()));
        hash.put(UOrderCommandLog.LONG_ORDER_VOLUME, Long.valueOf(st.nextToken()));
        hash.put(UOrderCommandLog.INT_RANDOM_NUMBER,
                 Integer.valueOf(st.nextToken()));
      }
      fOrderCommandArray.add(hash);
    }
  }

}
