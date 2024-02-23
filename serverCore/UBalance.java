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
 * ���x���������N���X�ł��D
 * ���x���Ƃ��ẮC�����������C�ؓ����C���������v�C�a���؋����C
 * ���x�����萔���C���x���������C�ۗL�����C�������v�������Ă��܂��D
 */
public class UBalance {

  /** ���������� */
  private long fInitialCash;

  /** �ؓ��� */
  private long fLoan;

  /** ���������v */
  private long fUnrealizedProfit;

  /** �a���؋��� */
  private long fMargin;

  /** ���x�����萔�� */
  private long fSumOfFee;

  /** ���x�������� */
  private long fSumOfInterest;

  /** �ۗL���� */
  private long fCash;

  /** �������v */
  private long fProfit;

  /**
   * ���x�����쐬���܂��D
   */
  public UBalance() {
    fInitialCash = 0;
    fLoan = 0;
    fUnrealizedProfit = 0;
    fMargin = 0;
    fSumOfFee = 0;
    fSumOfInterest = 0;
    fCash = 0;
    fProfit = 0;
  }

  /**
   * ������Ԃ��܂��D
   * @return ����
   */
  public Object clone() {
    UBalance result = new UBalance();
    result.fInitialCash = fInitialCash;
    result.fLoan = fLoan;
    result.fUnrealizedProfit = fUnrealizedProfit;
    result.fMargin = fMargin;
    result.fSumOfFee = fSumOfFee;
    result.fSumOfInterest = fSumOfInterest;
    result.fCash = fCash;
    result.fProfit = fProfit;
    return result;
  }

  /**
   * ���������o�͂��܂��D
   * @param pw �o�͐�
   */
  public void printOn(PrintWriter pw) {
    try {
      pw.println("fInitialCash = " + fInitialCash);
      pw.println("fCash = " + fCash);
      pw.println("fProfit = " + fProfit);
      pw.println("fUnrealizedProfit = " + fUnrealizedProfit);
      pw.println("fMargin = " + fMargin);
      pw.println("fSumOfFee = " + fSumOfFee);
      pw.println("fLoan = " + fLoan);
      pw.println("fSumOfInterest = " + fSumOfInterest);
    } catch (Exception e) {
      System.out.println("Exception: " + e);
    }
  }

  /**
   * ������������Ԃ��܂��D
   * @return ����������
   */
  public long getInitialCash() {
    return fInitialCash;
  }

  /**
   * �ؓ�����Ԃ��܂��D
   * @return �ؓ���
   */
  public long getLoan() {
    return fLoan;
  }

  /**
   * ���������v��Ԃ��܂��D
   * @return ���������v
   */
  public long getUnrealizedProfit() {
    return fUnrealizedProfit;
  }

  /**
   * �a���؋�����Ԃ��܂��D
   * @return �a���؋���
   */
  public long getMargin() {
    return fMargin;
  }

  /**
   * ���x�����萔����Ԃ��܂��D
   * @return ���x�����萔��
   */
  public long getSumOfFee() {
    return fSumOfFee;
  }

  /**
   * ���x��������Ԃ��܂��D
   * @return ���x��������
   */
  public long getSumOfInterest() {
    return fSumOfInterest;
  }

  /**
   * �ۗL������Ԃ��܂��D
   * @return �ۗL����
   */
  public long getCash() {
    return fCash;
  }

  /**
   * �������v��Ԃ��܂��D
   * @return �������v
   */
  public long getProfit() {
    return fProfit;
  }

  /**
   * ������������ݒ肵�܂��D
   * @param initialCash ����������
   */
  public void setInitialCash(long initialCash) {
    fInitialCash = initialCash;
  }

  /**
   * �ؓ�����ݒ肵�܂��D
   * @param loan �ؓ���
   */
  public void setLoan(long loan) {
    fLoan = loan;
  }

  /**
   * ���������v��ݒ肵�܂��D
   * @param unrealizedProfit ���������v
   */
  public void setUnrealizedProfit(long unrealizedProfit) {
    fUnrealizedProfit = unrealizedProfit;
  }

  /**
   * �a���؋�����ݒ肵�܂��D
   * @param margin �a���؋���
   */
  public void setMargin(long margin) {
    fMargin = margin;
  }

  /**
   * ���x�����萔����ݒ肵�܂��D
   * @param sumOfFee ���x�����萔��
   */
  public void setSumOfFee(long sumOfFee) {
    fSumOfFee = sumOfFee;
  }

  /**
   * ���x����������ݒ肵�܂��D
   * @param interest ���x��������
   */
  public void setSumOfInterest(long interest) {
    fSumOfInterest = interest;
  }

  /**
   * �������v��ݒ肵�܂��D
   * @param profit �������v
   */
  public void setProfit(long profit) {
    fProfit = profit;
  }

  /**
   * �ۗL�������X�V���܂��D
   */
  public void updateCash() {
    fCash = fInitialCash + fUnrealizedProfit + fProfit + fLoan
        - fMargin - fSumOfFee - fSumOfInterest;
  }

}
