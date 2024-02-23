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
package serverCore;

import java.io.*;
import java.util.*;

/**
 * �������������N���X�ł��D�������Ƃ��ẮC
 * ���[�U�[ID�C�������C����ID�C��������(������)�C�������C
 * �����߁C����/�����C���s/�w�l�C�V�K/�ԍρC�������i�C
 * �������ʁC��萔�ʁC������ʁC�����������Ă��܂��D
 */
public class UOrder {

  /** �u����v��\���萔 */
  public static final int SELL = 1;

  /** �u�����v��\���萔 */
  public static final int BUY = 2;

  /** �u���s�v��\���萔 */
  public static final int MARKET = 1;

  /** �u�w�l�v��\���萔 */
  public static final int LIMIT = 2;

  /** �u�V�K�v��\���萔 */
  public static final int NEW = 1;

  /** �u�ԍρv��\���萔 */
  public static final int REPAY = 2;

  /** ���[�U�[ID */
  private int fUserID;

  /** ���O�C���� */
  private String fUserName;

  /** ������ */
  private String fBrandName;

  /** ����ID */
  private long fOrderID;

  /** ��������(������) */
  private Date fTime;

  /** ������ */
  private int fDate;

  /** ������ */
  private int fSession;

  /** �����敪 */
  private int fSellBuy;

  /** ���s�w�l�敪 */
  private int fMarketLimit;

  /** �V�K�ԍϋ敪 */
  private int fNewRepay;

  /** �������i */
  private long fPrice;

  /** �������� */
  private long fVolume;

  /** ����� */
  private Vector fContractInformationArray;

  /** ��萔�� */
  private long fContractVolume;

  /** ������� */
  private long fCancelVolume;

  /** ����񂹊��ԓ��̒������\�[�g���邽�߂ɗ��p����闐�� */
  private int fRandomNumber;

  /**
   * �R���X�g���N�^�ł��D
   */
  public UOrder() {
    fUserID = 0;
    fBrandName = new String();
    fOrderID = 0;
    fUserName = "";
    fTime = new Date();
    fDate = 0;
    fSession = 0;
    fSellBuy = SELL;
    fMarketLimit = MARKET;
    fNewRepay = NEW;
    fPrice = 0;
    fVolume = 0;
    fContractInformationArray = new Vector();
    fContractVolume = 0;
    fCancelVolume = 0;
    fRandomNumber = 0;
  }

  /**
   * ���[�U�[ID��ݒ肵�܂��D
   * @param userID ���[�U�[ID
   */
  public final void setUserID(int userID) {
    fUserID = userID;
  }

  /**
   * ���O�C������ݒ肵�܂��D
   * @param userName ���O�C����
   */
  public final void setUserName(String userName) {
    fUserName = userName;
  }

  /**
   * ���O�C������Ԃ��܂��D
   * @return ���O�C����
   */
  public final String getUserName() {
    return fUserName;
  }

  /**
   * ��������ݒ肵�܂��D
   * @param name ������
   */
  public final void setBrandName(String name) {
    fBrandName = name;
  }

  /**
   * ����ID��ݒ肵�܂��D
   * @param orderID ����ID
   */
  public final void setOrderID(long orderID) {
    fOrderID = orderID;
  }

  /**
   * ��������(������)��ݒ肵�܂��D
   * @param time ��������
   */
  public final void setTime(Date time) {
    fTime.setTime(time.getTime());
  }

  /**
   * ��������ݒ肵�܂��D
   * @param date ������
   */
  public final void setDate(int date) {
    fDate = date;
  }

  /**
   * �����߂�ݒ肵�܂��D
   * @param session ������
   */
  public final void setSession(int session) {
    fSession = session;
  }

  /**
   * �����敪��ݒ肵�܂��D
   * @param sellBuy ��: UOrder.SELL, ��: UOrder.BUY
   */
  public final void setSellBuy(int sellBuy) {
    if (sellBuy == SELL || sellBuy == BUY) {
      fSellBuy = sellBuy;
    } else {
      String str = "UOrder.setSellBuy: sellBuy = " + sellBuy;
      throw new IllegalArgumentException(str);
    }
  }

  /**
   * ���s�w�l�敪��ݒ肵�܂��D
   * @param marketLimit ���s: UOrder.MARKET, �w�l: UOrder.LIMIT
   */
  public final void setMarketLimit(int marketLimit) {
    if (marketLimit == MARKET || marketLimit == LIMIT) {
      fMarketLimit = marketLimit;
    } else {
      String str = "UOrder.setMarketLimit: marketLimit = " + marketLimit;
      throw new IllegalArgumentException(str);
    }
  }

  /**
   * �V�K�ԍϋ敪��ݒ肵�܂��D
   * @param newRepay �V�K: UOrder.NEW, �ԍ�: UOrder.REPAY (�����UMart�ł�, �V�K�̂�)
   */
  public final void setNewRepay(int newRepay) {
    if (newRepay == NEW || newRepay == REPAY) {
      fNewRepay = newRepay;
    } else {
      String str = "UOrder.setNewRepay: newRepay = " + newRepay;
      throw new IllegalArgumentException(str);
    }
  }

  /**
   * �������i��ݒ肵�܂��D
   * @param price �������i
   */
  public final void setPrice(long price) {
    if (price >= 0) {
      fPrice = price;
    } else {
      String str = "UOrder.setPrice: price = " + price;
      throw new IllegalArgumentException(str);
    }
  }

  /**
   * �������ʂ�ݒ肵�܂��D
   * @param volume ��������
   */
  public final void setVolume(long volume) {
    if (volume >= 0) {
      fVolume = volume;
    } else {
      String str = "UOrder.setVolume: volume = " + volume;
      throw new IllegalArgumentException(str);
    }
  }

  /**
   * ������ǉ����܂��D
   * @param contractID ���ID
   * @param time ��莞��(������)
   * @param volume ��萔��
   * @param date ����
   * @param session ����
   */
  public final void addContractInformation(long contractID, Date time,
                                           long volume, int date, int session) {
    if (time != null && volume > 0 && date >= 1 && session >= 1) {
      fContractVolume += volume;
      UContractInformation info = new UContractInformation(contractID, time,
          volume, date, session);
      fContractInformationArray.addElement(info);
    } else {
      String str = "Error in UOrder.addContractInformation";
      System.out.println("Contract ID: " + contractID + " Time: " + time
                         + " volume: " + volume + " Date: " + date
                         + " Session: " + session);
      throw new IllegalArgumentException(str);
    }
  }

  /**
   * �܂���艿�i���ݒ肳��Ă��Ȃ������ɖ�艿�i��ݒ肵�܂��D
   * @param price ��艿�i
   */
  public final void setLatestContractPrice(long price) {
    if (fContractInformationArray.size() > 0) {
      UContractInformation info =
          (UContractInformation) fContractInformationArray.lastElement();
      if (info.getPrice() == -1) {
        info.setPrice(price);
      }
    }
  }

  /**
   * ��萔�ʂ�ݒ肵�܂��D
   * @param volume ��萔��
   */
  public final void setContractVolume(long volume) {
    fContractVolume = volume;
  }

  /**
   * ������ʂ�ݒ肵�܂��D
   * @param volume �������
   */
  public final void setCancelVolume(long volume) {
    fCancelVolume = volume;
  }

  /**
   * �\�[�g�̂��߂̗�����ݒ肵�܂��D
   * @param r ����
   */
  public final void setRandomInteger(int r) {
    fRandomNumber = r;
  }

  /**
   * ����\�Ȓ������������܂��D
   */
  public final void cancel() {
    fCancelVolume = fVolume - fContractVolume;
  }

  /**
   * ���[�U�[ID��Ԃ��܂��D
   * @return ���[�U�[ID
   */
  public final int getUserID() {
    return fUserID;
  }

  /**
   * ��������Ԃ��܂��D
   * @return ������
   */
  public final String getBrandName() {
    return fBrandName;
  }

  /**
   * ����ID�Ԃ��܂��D
   * @return ����ID
   */
  public final long getOrderID() {
    return fOrderID;
  }

  /**
   * ��������(������)��Ԃ��܂��D
   * @return ��������(������)
   */
  public final Date getTime() {
    return fTime;
  }

  /**
   * ��������Ԃ��܂��D
   * @return ������
   */
  public final int getDate() {
    return fDate;
  }

  /**
   * �����߂�Ԃ��܂��D
   * @return ������
   */
  public final int getSession() {
    return fSession;
  }

  /**
   * �����敪��Ԃ��܂��D
   * @return ��: UOrder.SELL, ��: UOrder.BUY
   */
  public final int getSellBuy() {
    return fSellBuy;
  }

  /**
   * ���s�w�l�敪��Ԃ��܂��D
   * @return ���s: UOrder.MARKET, �w�l: UOrder.LIMIT
   */
  public final int getMarketLimit() {
    return fMarketLimit;
  }

  /**
   * �V�K�ԍϋ敪��Ԃ��܂��D
   * @return �V�K: UOrder.NEW, �ԍ�: UOrder.REPAY
   */
  public final int getNewRepay() {
    return fNewRepay;
  }

  /**
   * �������i��Ԃ��܂��D
   * @return �������i
   */
  public final long getPrice() {
    return fPrice;
  }

  /**
   * �������ʂ�Ԃ��܂��D
   * @return ��������
   */
  public final long getVolume() {
    return fVolume;
  }

  /**
   * ������Ԃ��܂��D
   * @return �����̖����
   */
  public final Vector getContractInformationArray() {
    return fContractInformationArray;
  }

  /**
   * ��萔�ʂ�Ԃ��܂��D
   * @return ��萔��
   */
  public final long getContractVolume() {
    return fContractVolume;
  }

  /**
   * ����萔�ʂ�Ԃ��܂��D
   * @return ����萔��
   */
  public final long getUncontractOrderVolume() {
    return fVolume - fContractVolume - fCancelVolume;
  }

  /**
   * ������ʂ�Ԃ��܂��D
   * @return �������
   */
  public final long getCancelVolume() {
    return fCancelVolume;
  }

  /**
   * �\�[�g�̂��߂̗�����Ԃ��܂��D
   * @return ����
   */
  public final int getRandomNumber() {
    return fRandomNumber;
  }

  /**
   * ���������o�͂��܂��D
   * @param pw �o�͐�
   */
  public void printOn(PrintStream pw) {
    pw.println("userID: " + fUserID);
    pw.println("userName: " + fUserName);
    pw.println("brandName: " + fBrandName);
    pw.println("serialNO: " + fOrderID);
    pw.println("time-stump: " + fTime);
    pw.println("date: " + fDate);
    pw.println("session: " + fSession);
    pw.println("sellBuy: " + fSellBuy);
    pw.println("marketLimit: " + fMarketLimit);
    pw.println("newRepay: " + fNewRepay);
    pw.println("price: " + fPrice);
    pw.println("volume: " + fVolume);
    pw.println("contract volume: " + fContractVolume);
    pw.println("cancel volume: " + fCancelVolume);
    Enumeration e = fContractInformationArray.elements();
    while (e.hasMoreElements()) {
      UContractInformation x = (UContractInformation) e.nextElement();
      pw.print("contract(id, time, price, volume, date, session)=");
      pw.print(Long.toString(x.getContractID()) + ", ");
      pw.print(x.getTime().toString() + ", ");
      pw.print(Long.toString(x.getPrice()) + ", ");
      pw.print(Long.toString(x.getVolume()) + ", ");
      pw.println(Integer.toString(x.getDate()) + ", ");
      pw.println(Integer.toString(x.getSession()) + ")");
    }
    pw.println("randomInteger: " + fRandomNumber);
  }

  /**
   * UOrder�̕������쐬���ĕԂ��܂��D
   * @return UOrder�̕���
   */
  public Object clone() {
    UOrder cl = new UOrder();
    cl.fUserID = fUserID;
    cl.fUserName = fUserName;
    cl.fBrandName = fBrandName;
    cl.fOrderID = fOrderID;
    cl.fTime = (Date) fTime.clone();
    cl.fDate = fDate;
    cl.fSession = fSession;
    cl.fSellBuy = fSellBuy;
    cl.fMarketLimit = fMarketLimit;
    cl.fNewRepay = fNewRepay;
    cl.fPrice = fPrice;
    cl.fVolume = fVolume;
    cl.fContractVolume = fContractVolume;
    Enumeration e = fContractInformationArray.elements();
    while (e.hasMoreElements()) {
      UContractInformation x = (UContractInformation) e.nextElement();
      fContractInformationArray.addElement(x.clone());
    }
    cl.fContractVolume = fContractVolume;
    cl.fCancelVolume = fCancelVolume;
    cl.fRandomNumber = fRandomNumber;
    return cl;
  }

  /**
   * �e�X�g�p���C�����\�b�h�ł��D
   */
  public static void main(String[] args) {
    UOrder u = new UOrder();
    Date d = new Date(253300);
    u.setUserID(1);
    u.setBrandName("j300119991200000");
    u.setOrderID(1);
    u.setTime(d);
    u.setDate(2);
    u.setSession(3);
    u.setSellBuy(UOrder.SELL);
    u.setMarketLimit(UOrder.LIMIT);
    u.setNewRepay(UOrder.REPAY);
    u.setPrice(2000);
    u.setVolume(50);
    u.setRandomInteger(1234);
    u.setContractVolume(5);
    u.setCancelVolume(12);
    u.printOn(System.out);
    System.out.println(u.getUncontractOrderVolume());
  }
}
