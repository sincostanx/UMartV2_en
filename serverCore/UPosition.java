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

/**
 * �|�W�V�������������N���X�ł��D
 * �|�W�V�������Ƃ��ẮC�O���܂ł̔���|�W�V�����̍��v�C
 * �O���܂ł̔����|�W�V�����̍��v�C���������̔񂹉񐔁C
 * �����̊e�񂹂��Ƃ̔���|�W�V����, �����̊e�񂹂��Ƃ̔����|�W�V������
 * �����Ă��܂��D
 */
public class UPosition {

  /** �O���܂ł̔���|�W�V�����̍��v */
  private long fSumOfSellPositionUntilYesterday;

  /** �O���܂ł̔����|�W�V�����̍��v */
  private long fSumOfBuyPositionUntilYesterday;

  /** ���������̐ߐ� */
  private int fNoOfSessionsPerDay;

  /** �����̊e�߂��Ƃ̔���|�W�V���� */
  private long fTodaySellPositions[];

  /** �����̊e�߂��Ƃ̔����|�W�V���� */
  private long fTodayBuyPositions[];

  /**
   * ��̃|�W�V�������𐶐����܂��D��ŁCsetNoOfBoards���\�b�h��
   * �Ăяo���Đ���������������K�v������܂��D
   */
  public UPosition() {
    fNoOfSessionsPerDay = 0;
    fTodaySellPositions = null;
    fTodayBuyPositions = null;
    fSumOfSellPositionUntilYesterday = 0;
    fSumOfBuyPositionUntilYesterday = 0;
  }

  /**
   * �|�W�V�����������������܂��D
   * @param noOfSessionsPerDay ���������̐ߐ�
   */
  public UPosition(int noOfSessionsPerDay) {
    fNoOfSessionsPerDay = noOfSessionsPerDay;
    fTodaySellPositions = new long[fNoOfSessionsPerDay];
    fTodayBuyPositions = new long[fNoOfSessionsPerDay];
    fSumOfSellPositionUntilYesterday = 0;
    fSumOfBuyPositionUntilYesterday = 0;
    clear();
  }

  /**
   * ������Ԃ��܂��D
   * @return ����
   */
  public Object clone() {
    UPosition result = new UPosition(fNoOfSessionsPerDay);
    for (int i = 1; i <= fNoOfSessionsPerDay; ++i) {
      result.fTodaySellPositions[i - 1] = fTodaySellPositions[i - 1];
      result.fTodayBuyPositions[i - 1] = fTodayBuyPositions[i - 1];
    }
    result.fSumOfSellPositionUntilYesterday = fSumOfSellPositionUntilYesterday;
    result.fSumOfBuyPositionUntilYesterday = fSumOfBuyPositionUntilYesterday;
    return result;
  }

  /**
   * �O���܂ł̔���|�W�V�����̍��v��Ԃ��܂��D
   * @return �O���܂ł̔���|�W�V�����̍��v
   */
  public long getSumOfSellPositionUntilYesterday() {
    return fSumOfSellPositionUntilYesterday;
  }

  /**
   * �O���܂ł̔����|�W�V�����̍��v��Ԃ��܂��D
   * @return �O���܂ł̔����|�W�V�����̍��v
   */
  public long getSumOfBuyPositionUntilYesterday() {
    return fSumOfBuyPositionUntilYesterday;
  }

  /**
   * ���������̔񂹉񐔂�Ԃ��܂��D
   * @return ���������̔񂹉�
   */
  public int getNoOfSessionsPerDay() {
    return fNoOfSessionsPerDay;
  }

  /**
   * �����Ŏw�肳�ꂽ�߂̔���|�W�V������Ԃ��܂��D
   * @param session ��
   * @return ����|�W�V����
   */
  public long getTodaySellPosition(int session) {
    return fTodaySellPositions[session - 1];
  }

  /**
   * �����Ŏw�肳�ꂽ�߂̔����|�W�V������Ԃ��܂��D
   * @param session ��
   * @return �����|�W�V����
   */
  public long getTodayBuyPosition(int session) {
    return fTodayBuyPositions[session - 1];
  }

  /**
   * �O���܂ł̔���|�W�V�����̍��v��ݒ肵�܂��D
   * @param sellPosition �O���܂ł̔���|�W�V����
   */
  public void setSumOfSellPositionUntilYesterday(long sellPosition) {
    fSumOfSellPositionUntilYesterday = sellPosition;
  }

  /**
   * �O���܂ł̔����|�W�V�����̍��v��ݒ肵�܂��D
   * @param buyPosition �O���܂ł̔����|�W�V����
   */
  public void setSumOfBuyPositionUntilYesterday(long buyPosition) {
    fSumOfBuyPositionUntilYesterday = buyPosition;
  }

  /**
   * ���������̐ߐ���ݒ肵�܂��D
   * @param noOfSessionsPerDay ���������̐ߐ�
   */
  public void setNoOfSessionsPerDay(int noOfSessionsPerDay) {
    fNoOfSessionsPerDay = noOfSessionsPerDay;
    fTodaySellPositions = new long[fNoOfSessionsPerDay];
    fTodayBuyPositions = new long[fNoOfSessionsPerDay];
    clear();
  }

  /**
   * �����̔���|�W�V������ݒ肵�܂��D
   * @param sellPosition ����|�W�V����
   * @param session ��
   */
  public void setTodaySellPosition(long sellPosition, int session) {
    fTodaySellPositions[session - 1] = sellPosition;
  }

  /**
   * �����̔����|�W�V������ݒ肵�܂��D
   * @param buyPosition �����|�W�V����
   * @param session ��
   */
  public void setTodayBuyPosition(long buyPosition, int session) {
    fTodayBuyPositions[session - 1] = buyPosition;
  }

  /**
   * ������session�߂̔����|�W�V������buyPosition�����Z���܂��D
   * @param buyPosition �����|�W�V�����̑���
   * @param session ��
   */
  public void addToTodayBuyPosition(long buyPosition, int session) {
    fTodayBuyPositions[session - 1] += buyPosition;
  }

  /**
   * ������session�߂̔���|�W�V������sellPosition�����Z���܂��D
   * @param sellPosition ����|�W�V�����̑���
   * @param session ��
   */
  public void addToTodaySellPosition(long sellPosition, int session) {
    fTodaySellPositions[session - 1] += sellPosition;
  }

  /**
   * �����̔���|�W�V�����̍��v��Ԃ��܂��D
   * @return �����̔���|�W�V�����̍��v
   */
  public long getSumOfTodaySellPosition() {
    long result = 0;
    for (int i = 1; i <= fNoOfSessionsPerDay; ++i) {
      result += getTodaySellPosition(i);
    }
    return result;
  }

  /**
   * �����̔����|�W�V�����̍��v��Ԃ��܂��D
   * @return �����̔����|�W�V�����̍��v
   */
  public long getSumOfTodayBuyPosition() {
    long result = 0;
    for (int i = 1; i <= fNoOfSessionsPerDay; ++i) {
      result += getTodayBuyPosition(i);
    }
    return result;
  }

  /**
   * ���݂̔���|�W�V�����̍��v��Ԃ��܂��D
   * @return ���݂̔���|�W�V�����̍��v
   */
  public long getTotalSellPosition() {
    return getSumOfSellPositionUntilYesterday() + getSumOfTodaySellPosition();
  }

  /**
   * ���݂̔����|�W�V�����̍��v��Ԃ��܂��D
   * @return ���݂̔����|�W�V�����̍��v
   */
  public long getTotalBuyPosition() {
    return getSumOfBuyPositionUntilYesterday() + getSumOfTodayBuyPosition();
  }

  /**
   * �����̔���/�����|�W�V������O���܂ł̔���/�����|�W�V������
   * ���Z������, �[���ɃN���A���܂��D
   */
  public void collect() {
    long totalSell = getSumOfSellPositionUntilYesterday();
    long totalBuy = getSumOfBuyPositionUntilYesterday();
    for (int i = 1; i <= fNoOfSessionsPerDay; i++) {
      totalSell += getTodaySellPosition(i);
      totalBuy += getTodayBuyPosition(i);
      setTodaySellPosition(0, i);
      setTodayBuyPosition(0, i);
    }
    setSumOfSellPositionUntilYesterday(totalSell);
    setSumOfBuyPositionUntilYesterday(totalBuy);
  }

  /**
   * �O���܂ł̔���/�����|�W�V�����̍��v�Ɠ����̔���/�����|�W�V������
   * �[���ɃN���A���܂��D
   */
  public void clear() {
    setSumOfSellPositionUntilYesterday(0);
    setSumOfBuyPositionUntilYesterday(0);
    for (int i = 1; i <= fNoOfSessionsPerDay; ++i) {
      setTodaySellPosition(0, i);
      setTodayBuyPosition(0, i);
    }
  }

  /**
   * ���������o�͂��܂��D
   * @param w �o�͐�
   */
  public void printOn(Writer w) {
    try {
      w.write("fSumOfSellPositionUntilYesterday = "
              + fSumOfSellPositionUntilYesterday + "\n");
      w.write("fSumOfBuyPositionUntilYesterday = "
              + fSumOfBuyPositionUntilYesterday + "\n");
      w.write("fNoOfBoards = " + fNoOfSessionsPerDay + "\n");
      for (int i = 1; i <= fNoOfSessionsPerDay; i++) {
        w.write("fTodaySellPosition[" + i + "] = "
                + fTodaySellPositions[i - 1] + ", ");
        w.write("fTodayBuyPosition[" + i + "] = "
                + fTodayBuyPositions[i - 1] + "\n");
      }
    } catch (Exception e) {
      System.out.println("Exception: " + e);
    }
  }

}
