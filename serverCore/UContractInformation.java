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
 * �����������N���X�ł��D
 * �����ɂ�, ���ID, ��莞��, ��艿�i, ��萔��, ����,
 * ���񂹉񐔂��܂܂�܂��D
 */
public class UContractInformation {

  /** ���ID */
  private long fContractID;

  /** ��莞��(������) */
  private Date fTime;

  /** ��艿�i */
  private long fPrice;

  /** ��萔�� */
  private long fVolume;

  /** ���� */
  private int fDate;

  /** ���� */
  private int fSession;

  /**
   * �����Ŏw�肳�ꂽ���ID�C��莞��(������)�C��萔�ʁC�����C
   * ���񂹉񐔂ŏ��������܂��D
   * �����ł́C��艿�i��-1�ɏ���������邽�߁C
   * ���setPrice���\�b�h��p���ēK�؂ɐݒ肷��K�v������܂��D
   * @param contractID ���ID
   * @param time ��莞��(������)
   * @param volume ��萔��
   * @param date ����
   * @param session ����
   */
  public UContractInformation(long contractID, Date time, long volume,
                              int date, int session) {
    fContractID = contractID;
    fTime = time;
    fVolume = volume;
    fPrice = -1;
    fDate = date;
    fSession = session;
  }

  /**
   * �������쐬���܂��D
   * @return UContractInformation�I�u�W�F�N�g�̕���
   */
  public Object clone() {
    UContractInformation info = new UContractInformation(fContractID, fTime, fVolume, fDate, fSession);
    info.setPrice(fPrice);
    return info;
  }

  /**
   * ���������o�͂��܂��D
   * @param pw �o�͐���w��
   */
  void printOn(PrintWriter pw) {
    pw.println("ContractID: " + fContractID);
    pw.println("Time: " + fTime.toString());
    pw.println("Price: " + fPrice);
    pw.println("Date: " + fDate);
    pw.println("Session: " + fSession);
  }

  /**
   * ���ID��Ԃ��܂��D
   * @return ���ID
   */
  public long getContractID() {
    return fContractID;
  }

  /**
   * ��莞����Ԃ��܂��D
   * @return ��莞��(������)
   */
  public Date getTime() {
    return fTime;
  }

  /**
   * ��萔�ʂ�Ԃ��܂��D
   * @return ��萔��
   */
  public long getVolume() {
    return fVolume;
  }

  /**
   * ��艿�i��Ԃ��܂��D
   * @return ��艿�i
   */
  public long getPrice() {
    return fPrice;
  }

  /**
   * ������Ԃ��܂��D
   * @return ����
   */
  public int getDate() {
    return fDate;
  }

  /**
   * ��肵���Ƃ��̐߂�Ԃ��܂��D
   * @return ����
   */
  public int getSession() {
    return fSession;
  }

  /**
   * ��艿�i��ݒ肵�܂��D
   * @param price ��艿�i
   */
  public void setPrice(long price) {
    fPrice = price;
  }

  /**
   * �e�X�g�p���C�����\�b�h�ł��D
   */
  public static void main(String args[]) {
    UContractInformation ui = new UContractInformation(1, new Date(), 10, 3, 2);
    ui.setPrice(3000);
    PrintWriter pw = new PrintWriter(System.out, true);
    ui.printOn(pw);
  }

}
