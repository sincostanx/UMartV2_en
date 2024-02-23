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
 * �P�ߕ��̖����̃��O�������N���X�ł��D
 */
public class UExecutionLog {

  /** ���t�������L�[ */
  public static final String INT_DATE = "INT_DATE";

  /** �߂������L�[ */
  public static final String INT_SESSION = "INT_SESSION";

  /** ���O�C�������������߂̃L�[ */
  public static final String STRING_LOGIN_NAME = "STRING_LOGIN_NAME";

  /** ���ID�������L�[ */
  public static final String LONG_CONTRACT_ID = "LONG_CONTRACT_ID";

  /** �������i�������L�[ */
  public static final String LONG_ORDER_PRICE = "LONG_ORDER_PRICE";

  /** �������ʂ������L�[ */
  public static final String LONG_ORDER_VOLUME = "LONG_ORDER_VOLUME";

  /** ����ID�������L�[ */
  public static final String LONG_ORDER_ID = "LONG_ORDER_ID";

  /** �� */
  private int fDate;

  /** �� */
  private int fSession;

  /** ��1�񕪂̖���� */
  private ArrayList fExecutionArray;

  /**
   * �R���X�g���N�^�ł��D
   * @param date ��
   * @param session ��
   */
  public UExecutionLog(int date, int session) {
    fDate = date;
    fSession = session;
    fExecutionArray = new ArrayList();
  }

  /**
   * ���ID�̔�r��N���X�ł��D
   * @author ����@��
   */
  private class UContractIDComparator implements Comparator {

  	/**
  	 * ��r���܂��D
  	 * @param o1 �����P
  	 * @param o2 �����Q
  	 * @return o1��ID��o2��ID���傫�����+1�C���������-1
  	 */
  	public int compare(Object o1, Object o2) {
      HashMap info1 = (HashMap)o1;
      HashMap info2 = (HashMap)o2;
      long id1 = ((Long)info1.get(UExecutionLog.LONG_CONTRACT_ID)).longValue();
      long id2 = ((Long)info2.get(UExecutionLog.LONG_CONTRACT_ID)).longValue();
      if (id1 > id2) {
        return 1;
      } else {
        return -1;
      }
    }

  }

  /**
   * �R���X�g���N�^�ł��D
   * @param date ��
   * @param session ��
   * @param om UOrderManager
   */
  public UExecutionLog(int date, int session, UOrderManager om) {
    this(date, session);
    Enumeration orderArrays = om.getOrderArrays();
    while (orderArrays.hasMoreElements()) {
      UOrderArray orderArray = (UOrderArray) orderArrays.nextElement();
      Enumeration uncontractedOrders = orderArray.getUncontractedOrders().
          elements();
      while (uncontractedOrders.hasMoreElements()) {
        UOrder o = (UOrder) uncontractedOrders.nextElement();
        checkAndAppendInfo(o);
      }
      Enumeration contractedOrders = orderArray.getContractedOrders().elements();
      while (contractedOrders.hasMoreElements()) {
        UOrder o = (UOrder) contractedOrders.nextElement();
        checkAndAppendInfo(o);
      }
    }
    Collections.sort(fExecutionArray, new UContractIDComparator());
  }

  /**
   * �����̒������������̒�����C���̖���񃍃O���������Ɛ߂�
   * �Y����������������āCfExecutionArray�ɏ���ǉ����܂��D
   * @param o ����
   */
  private void checkAndAppendInfo(UOrder o) {
    long orderID = o.getOrderID();
    String userName = o.getUserName();
    Enumeration infos = o.getContractInformationArray().elements();
    while (infos.hasMoreElements()) {
      UContractInformation info = (UContractInformation)infos.nextElement();
      if (info.getDate() == fDate && info.getSession() == fSession) {
        HashMap hash = makeExecutionInfo(info.getDate(), info.getSession(),
                                         userName,
                                         info.getContractID(), info.getPrice(),
                                         info.getVolume(), orderID);
        fExecutionArray.add(hash);
      }
    }
  }

  /**
   * �����𐶐����ĕԂ��܂��D
   * @param date ��
   * @param session ��
   * @param userName ���[�U��
   * @param contractID ���ID
   * @param contractPrice ��艿�i
   * @param contractVolume ��萔��
   * @param orderID ����ID
   * @return �����
   */
  private HashMap makeExecutionInfo(int date, int session, String userName,
                                    long contractID, long contractPrice,
                                    long contractVolume, long orderID) {
    HashMap info = new HashMap();
    info.put(UExecutionLog.INT_DATE, new Integer(date));
    info.put(UExecutionLog.INT_SESSION, new Integer(session));
    info.put(UExecutionLog.STRING_LOGIN_NAME, userName);
    info.put(UExecutionLog.LONG_CONTRACT_ID, new Long(contractID));
    info.put(UExecutionLog.LONG_ORDER_PRICE, new Long(contractPrice));
    info.put(UExecutionLog.LONG_ORDER_VOLUME, new Long(contractVolume));
    info.put(UExecutionLog.LONG_ORDER_ID, new Long(orderID));
    return info;
  }

  /**
   * �o�̓X�g���[���Ƀf�[�^�������o���܂��D
   * @param pw �o�̓X�g���[��
   * @throws IOException
   */
  public void writeTo(PrintWriter pw) throws IOException {
    pw.println("ContractDate,ContractSession,LoginName,ContractID"
               + ",ContractPrice,ContractVolume,OrderID");
    Iterator itr = fExecutionArray.iterator();
    while (itr.hasNext()) {
      HashMap info = (HashMap) itr.next();
      pw.print(info.get(UExecutionLog.INT_DATE).toString() + ",");
      pw.print(info.get(UExecutionLog.INT_SESSION).toString() + ",");
      pw.print(info.get(UExecutionLog.STRING_LOGIN_NAME).toString() + ",");
      pw.print(info.get(UExecutionLog.LONG_CONTRACT_ID).toString() + ",");
      pw.print(info.get(UExecutionLog.LONG_ORDER_PRICE).toString() + ",");
      pw.print(info.get(UExecutionLog.LONG_ORDER_VOLUME).toString() + ",");
      pw.println(info.get(UExecutionLog.LONG_ORDER_ID).toString());
    }
  }

  /**
   * ���̓X�g���[������f�[�^��ǂݍ��݂܂��D
   * @param br ���̓X�g���[��
   * @throws IOException
   */
  public void readFrom(BufferedReader br) throws IOException {
    fExecutionArray.clear();
    br.readLine(); // skip the header
    String line = null;
    while ( (line = br.readLine()) != null) {
      StringTokenizer st = new StringTokenizer(line, ",");
      int date = Integer.parseInt(st.nextToken());
      int session = Integer.parseInt(st.nextToken());
      String userName = st.nextToken();
      long contractID = Long.parseLong(st.nextToken());
      long contractPrice = Long.parseLong(st.nextToken());
      long contractVolume = Long.parseLong(st.nextToken());
      long orderID = Long.parseLong(st.nextToken());
      HashMap info = makeExecutionInfo(date, session, userName, contractID,
                                       contractPrice, contractVolume, orderID);
      fExecutionArray.add(info);
    }
  }

}
