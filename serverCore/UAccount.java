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

import cmdCore.*;

/**
 * ��ʂ̃����o�[�̌������������N���X�ł��D
 * �������Ƃ��Ă�, ���[�U�[ID, ���[�U�[��, �������x, �O�����x,
 * ����\/�s�\�̏��, �|�W�V�����������Ă��܂��D
 */
public class UAccount {

  /** �u������\�v��Ԃ�\���萔 */
  public static final int AVAILABLE = 1;

  /** �u������s�\�v��Ԃ�\���萔 */
  public static final int UNAVAILABLE = -1;

  /** ���[�U�[ID (0: �����(su), 1�`: ��ʃ��[�U�[) */
  private int fUserID;

  /** ���[�U�[��� (UMemberLog�Œ�`���ꂽ����) */
  private HashMap fUserInfo;

  /** �����̎��x */
  private UBalance fTodayBalance;

  /** �O���̎��x */
  private UBalance fYesterdayBalance;

  /** ������\/�s�\��� (UAccount.AVAILABLE / UAccount.UNAVAILABLE) */
  private int fStatus;

  /** �|�W�V���� */
  private UPosition fPosition;

  /** �������� */
  private ArrayList fAccountHistory;

  /**
   * �����Ŏw�肳�ꂽ���ŏ��������ꂽ�������쐬���܂��D
   * ��ɁAinitialize���\�b�h���Ăяo���ď���������K�v������܂��D
   * @param userID ���[�U�[ID
   * @param userInfo ���[�U�[��� (UMemberLog�Œ�`���ꂽ����)
   * @param noOfSessionsPerDay ���������̐ߐ�
   */
  public UAccount(int userID, HashMap userInfo, int noOfSessionsPerDay) {
    fUserID = userID;
    fUserInfo = userInfo;
    long initialCash = ( (Long) fUserInfo.get(UMemberList.LONG_INITIAL_CASH)).
        longValue();
    fTodayBalance = new UBalance();
    fTodayBalance.setInitialCash(initialCash);
    fYesterdayBalance = new UBalance();
    fStatus = UAccount.AVAILABLE;
    fPosition = new UPosition(noOfSessionsPerDay);
    fAccountHistory = new ArrayList();
  }

  /**
   * UAccount�I�u�W�F�N�g�̕����𐶐����܂��D
   * @return UAccount�I�u�W�F�N�g�̕���
   */
  public Object clone() {
    UAccount result = new UAccount(fUserID, fUserInfo, fPosition.getNoOfSessionsPerDay());
    result.fTodayBalance = (UBalance) fTodayBalance.clone();
    result.fYesterdayBalance = (UBalance) fYesterdayBalance.clone();
    result.fStatus = fStatus;
    result.fPosition = (UPosition) fPosition.clone();
    return result;
  }

  /**
   * ���[�U�[ID��Ԃ��܂��D
   * @return ���[�U�[ID
   */
  public int getUserID() {
    return fUserID;
  }

  /**
   * ���[�U�[����Ԃ��܂��D
   * @return ���[�U�[��
   */
  public String getUserName() {
    return (String) fUserInfo.get(UMemberList.STRING_LOGIN_NAME);
  }

  /**
   * �G�[�W�F���g�����iHuman or Machine�j��Ԃ��܂��D
   * @return �G�[�W�F���g�����iHuman or Machine�j
   */
  public String getAttribute() {
    return (String) fUserInfo.get(UMemberList.STRING_ATTRIBUTE);
  }

  /**
   * �ڑ��̎�ށiRemote or Local�j��Ԃ��܂��D
   * @return �ڑ��̎�ށiRemote or Local�j
   */
  public String getConnection() {
    return (String) fUserInfo.get(UMemberList.STRING_CONNECTION);
  }

  /**
   * �A�N�Z�X������Ԃ��܂��D
   * @return �A�N�Z�X����
   */
  public ArrayList getAccess() {
    return (ArrayList) fUserInfo.get(UMemberList.ARRAY_LIST_ACCESS);
  }

  /**
   * ���ۂ̖��O��Ԃ��܂��D
   * @return ���ۂ̖��O
   */
  public String getRealName() {
    return (String) fUserInfo.get(UMemberList.STRING_REAL_NAME);
  }

  /**
   * �G�[�W�F���g�̃V�X�e���p�����[�^��Ԃ��܂��D
   * @return �G�[�W�F���g�̃V�X�e���p�����[�^
   */
  public ArrayList getSystemParameters() {
    return (ArrayList) fUserInfo.get(UMemberList.ARRAY_LIST_SYSTEM_PARAMETERS);
  }

  /**
   * �����̎��Ԃ��܂��D
   * @return �����̎�
   */
  public int getSeed() {
    return ( (Integer) fUserInfo.get(UMemberList.INT_SEED)).intValue();
  }

  /**
   * �������Y��Ԃ��܂��D
   * @return �������Y
   */
  public long getInitialCash() {
    return ( (Long) fUserInfo.get(UMemberList.LONG_INITIAL_CASH)).longValue();
  }

  /**
   * �؂���������Ԃ��܂��D
   * @return �؂��������i�P��������j
   */
  public double getInterest() {
    return ( (Double) fUserInfo.get(UMemberList.DOUBLE_INTEREST)).doubleValue() /
        365.0;
  }

  /**
   * ������Ԃ��܂��D
   * @return ����
   */
  public double getInterestPerYear() {
    return ( (Double) fUserInfo.get(UMemberList.DOUBLE_INTEREST)).doubleValue();
  }

  /**
   * �P�ʎ��������̏؋�����Ԃ��܂��D
   * @return �؋���
   */
  public long getMarginRate() {
    return ( (Long) fUserInfo.get(UMemberList.LONG_MARGIN_RATE)).longValue();
  }

  /**
   * �P�ʎ��������̎萔����Ԃ��܂��D
   * @return �P�ʎ��������̎萔��
   */
  public long getFeePerUnit() {
    return ( (Long) fUserInfo.get(UMemberList.LONG_FEE_PER_UNIT)).longValue();
  }

  /**
   * �ő�ؓ�����Ԃ��܂��D
   * @return �ő�ؓ���
   */
  public long getMaxLoan() {
    return ( (Long) fUserInfo.get(UMemberList.LONG_MAX_LOAN)).longValue();
  }

  /**
   * ����P�ʂ�Ԃ��܂��D
   * @return ����P��
   */
  public long getTradingUnit() {
    return ( (Long) fUserInfo.get(UMemberList.LONG_TRADING_UNIT)).longValue();
  }

  /**
   * ���[�U�[���(UMemberLog�Œ�`���ꂽ����)��Ԃ��܂��D
   * @return ���[�U�[���
   */
  public HashMap getUserInfo() {
    return fUserInfo;
  }

  /**
   * �p�X���[�h��Ԃ��܂��D
   * @return �p�X���[�h
   */
  public String getPasswd() {
    return (String) fUserInfo.get(UMemberList.STRING_PASSWORD);
  }

  /**
   * �������x�����܂��D
   * @return �������x
   */
  public UBalance getTodayBalance() {
    return fTodayBalance;
  }

  /**
   * �O�����x�����܂��D
   * @return �O�����x
   */
  public UBalance getYesterdayBalance() {
    return fYesterdayBalance;
  }

  /**
   * ������Ԃ����܂��D
   * @return ������\�Ȃ��UAccount.AVAILABLE,
   *         ������s�\�Ȃ��UAccount.UNAVAILABLE
   */
  public int getStatus() {
    return fStatus;
  }

  /**
   * �|�W�V���������܂��D
   * @return �|�W�V����
   */
  public UPosition getPosition() {
    return fPosition;
  }

  /**
   * �������x��ݒ肵�܂��D
   * @param todayBalance �������x
   */
  public void setTodayBalance(UBalance todayBalance) {
    fTodayBalance = todayBalance;
  }

  /**
   * �O�����x��ݒ肵�܂��D
   * @param yesterdayBalance �O�����x
   */
  public void setYesterdayBalance(UBalance yesterdayBalance) {
    fYesterdayBalance = yesterdayBalance;
  }

  /**
   * ������Ԃ�ݒ肵�܂��D
   * @param status ������\: UAccount.AVAILABLE,
   *               ������s�\: UAccount.UNAVAILABLE
   */
  public void setStatus(int status) {
    fStatus = status;
  }

  /**
   * �|�W�V������ݒ肵�܂��D
   * @param position �|�W�V����
   */
  public void setPosition(UPosition position) {
    fPosition = position;
  }

  /**
   * ���������ւ̃o�b�N�A�b�v�ƑO�����x�ɓ������x�̃o�b�N�A�b�v���s���܂��D
   * ���̑���ɂ��, �O�����x�̏��͏�����̂Œ��ӂ��Ă��������D
   * @param step �X�e�b�v��
   */
  public void backup(int step) {
    HashMap data = new HashMap();
    String name = getUserName();
    data.put(UCAccountHistoryCore.STRING_NAME, name); // ���[�U�[��
    int userId = fUserID;
    data.put(UCAccountHistoryCore.INT_USER_ID, new Integer(userId)); // ���[�U�[ID
    data.put(UCAccountHistoryCore.INT_STEP, new Integer(step)); // �X�e�b�v��
    long unrealizedProfit = fTodayBalance.getUnrealizedProfit();
    data.put(UCAccountHistoryCore.LONG_UNREALIZED_PROFIT,
             new Long(unrealizedProfit)); // ���������v
    long sellPosition = fPosition.getTotalSellPosition();
    data.put(UCAccountHistoryCore.LONG_SELL_POSITION, new Long(sellPosition)); // ���|�W�V�����̍��v
    long buyPosition = fPosition.getTotalBuyPosition();
    data.put(UCAccountHistoryCore.LONG_BUY_POSITION, new Long(buyPosition)); // ���|�W�V�����̍��v
    fAccountHistory.add(data); // �������
    fYesterdayBalance = (UBalance) fTodayBalance.clone();
  }

  /**
   * �|�W�V���������������܂��D
   */
  public void clearPosition() {
    fPosition.clear();
  }

  /**
   * ���������o�͂��܂��D
   * @param pw �o�͐�
   */
  public void printOn(PrintWriter pw) {
    try {
      pw.println("fUserID = " + fUserID);
      pw.println("fUserInfo = " + fUserInfo);
      pw.println("fStatus = " + fStatus);
      pw.println("[Position]");
      fPosition.printOn(pw);
      pw.println("[Today's Balance]");
      fTodayBalance.printOn(pw);
      pw.println("[Yesterday's Balance]");
      fYesterdayBalance.printOn(pw);
    } catch (Exception e) {
      System.out.println("Exception: " + e);
    }
  }

  /**
   * ����������Ԃ��܂��D
   * @return ��������
   */
  public ArrayList getAccountHistory() {
    return fAccountHistory;
  }

}
