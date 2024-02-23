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
import java.text.*;
import java.util.*;


import log.*;

/**
 * ���i�n��������N���X�ł��D
 * ���̃N���X�́C���݂��w�������|�C���^��ێ����Ă��܂��D
 * resources/csv/j30.csv�̃f�[�^�t�H�[�}�b�g���T�|�[�g���Ă��܂��D
 */

public class UPriceInfoDB {

  /** UPriceInfoElement�I�u�W�F�N�g���i�[���邽�߂̃x�N�^ */
  private Vector fPriceInfoArray;

  /** ���݂�UPriceInfoElement���w�������C���f�b�N�X�B
    �敨���i�����肵�Ă���̂�fPriceInfoArray[fCurrentPtr - 1]�܂ł�
    ���邱�Ƃɒ��ӁB*/
  private int fCurrentPtr;

  /** �s����ǂ�UPriceInfoElement����J�n���邩���w�������C���f�b�N�X */
  private int fInitialPtr;

  /** �T�[�o�[�^�p���� */
  private int fMaxDate;

  /** ���������̐ߐ� */
  private int fNoOfSessionsPerDay;

  /** �ő�X�e�b�v�� */
  private int fMaxSteps;

  /** �� */
  private int fDate;

  /** �� */
  private int fSession;

  /** ���߂̐敨���i */
  private long fLatestFuturePrice;
  
  /**
   * �R���X�g���N�^�ł��D
   */
  public UPriceInfoDB() {
    fPriceInfoArray = new Vector();
    fCurrentPtr = 0;
    fInitialPtr = 0;
    fMaxSteps = 0;
    fMaxDate = 0;
    fNoOfSessionsPerDay = 0;
    fDate = -1;
    fSession = -1;
  }

  /**
   * �J�n�X�e�b�v���|�C���^�ɐݒ肵�܂��D
   * @param ptr �J�n�X�e�b�v
   * @param maxDate �������
   * @param noOfSessionsPerDay �P��������̐ߐ�
   */
  public void initializePtr(int ptr, int maxDate, int noOfSessionsPerDay) {
    fMaxDate = maxDate;
    fNoOfSessionsPerDay = noOfSessionsPerDay;
    fMaxSteps = fMaxDate * fNoOfSessionsPerDay;
    if (ptr + fMaxSteps > fPriceInfoArray.size()) {
      System.err.println("ptr=" + ptr + ", maxSteps=" + fMaxSteps);
      System.err.println("fPriceInfoArray.size()=" + fPriceInfoArray.size());
      System.err.println("Error: initializePtr error in UPriceInfoDB");
      System.exit( -1);
    }
    fInitialPtr = ptr;
    fCurrentPtr = fInitialPtr;
    {
      for (int i = 0; i < ptr; i++) {
        UPriceInfo pi = (UPriceInfo) fPriceInfoArray.elementAt(i);
        pi.setFuturePrice(pi.getSpotPrice());
      }
    }
    {
      for (int i = ptr; i < fPriceInfoArray.size(); i++) {
        UPriceInfo pi = (UPriceInfo) fPriceInfoArray.elementAt(i);
        pi.setFuturePrice(UPriceInfo.INVALID_PRICE);
      }
    }
    fDate = 1;
    fSession = 1;
    fLatestFuturePrice = (getFuturePrices(1))[0];
    //System.err.println(fLatestFuturePrice);
  }
  
  /**
   * ���߂̐敨���i��Ԃ��D
   * @return ���߂̐敨���i
   */
  public long getLatestFuturePrice() {
  	return fLatestFuturePrice;
  }

  /**
   * �ߋ�steps���̌������i�n���Ԃ��܂��D
   * @param steps �K�v�ȃX�e�b�v��
   * @return �������i�n��D�v�f0�����߂̉��i�ł��D
   */
  public long[] getSpotPrices(int steps) {
    if ( (fCurrentPtr - steps) < 0) {
      System.out.println("getSpotPrices Error!: adjust arraysize "
                         + steps + " -> " + fCurrentPtr);
      steps = fCurrentPtr;
    }
    long result[] = new long[steps];
    for (int i = 0; i < steps; i++) {
      UPriceInfo pi = (UPriceInfo) fPriceInfoArray.elementAt(fCurrentPtr - i -
          1);
      result[i] = pi.getSpotPrice();
    }
    return result;
  }

  /**
   * �ߋ�steps���̐敨���i�n���Ԃ��܂��D
   * @param steps �K�v�ȃX�e�b�v��
   * @return �敨���i�n��D�v�f0�����߂̉��i�ł��D
   */
  public long[] getFuturePrices(int steps) {
    if ( (fCurrentPtr - steps) < 0) {
      System.out.println("getFuturePrices Error!: adjust arraysize "
                         + steps + " -> " + fCurrentPtr);
      steps = fCurrentPtr;
    }
    long result[] = new long[steps];
    for (int i = 0; i < steps; i++) {
      UPriceInfo pi = (UPriceInfo) fPriceInfoArray.elementAt(fCurrentPtr - i - 1);
      result[i] = pi.getFuturePrice();
    }
    return result;
  }

  /**
   * �敨���i��ݒ肵�C�|�C���^���P�X�e�b�v�i�߂܂��D
   * @param futurePrice �敨���i
   */
  public void addCurrentPriceAndIncrementPtr(long futurePrice) {
    UPriceInfo pi = (UPriceInfo) fPriceInfoArray.elementAt(fCurrentPtr);
    //long spotPrice = pi.getSpotPrice();
    pi.setFuturePrice(futurePrice);
    pi.setDate(fDate);
    pi.setSession(fSession);
    incrementPtr();
    ++fSession;
    if (fSession > fNoOfSessionsPerDay) {
      ++fDate;
      fSession = 1;
    }
    long currentFuturePrice = (getFuturePrices(1))[0];
    if (currentFuturePrice > 0) {
    	fLatestFuturePrice = currentFuturePrice;
    }
    //System.err.println(fLatestFuturePrice);
  }

  /**
   * ���Z���̐敨���i��ݒ肵�܂��D
   *
   */
  public void setSettlementPrice() {
    UPriceInfo prevPi = (UPriceInfo)fPriceInfoArray.elementAt(fCurrentPtr - 1);
    UPriceInfo curPi = (UPriceInfo)fPriceInfoArray.elementAt(fCurrentPtr);
    long spotPrice = prevPi.getSpotPrice();
    curPi.setSpotPrice(spotPrice);
    curPi.setFuturePrice(spotPrice);
    curPi.setDate(fDate);
    curPi.setSession(fSession);
    incrementPtr();
  }

  /**
   * ���̓X�g���[�����牿�i�n���ǂݍ��݂܂��D
   * @param br ���̓X�g���[��
   * @throws IOException
   * @throws ParseException
   */
  public void readFrom(BufferedReader br) throws IOException, ParseException {
    String s;
    br.readLine(); // skip the header
    int lineNo = 1;
    try {
      while ( (s = br.readLine()) != null) {
        ArrayList info = UCsvUtility.split(s);
        Iterator itr = info.iterator();
        int date = Integer.parseInt( (String) itr.next());
        int session = Integer.parseInt( (String) itr.next());
        long spotPrice = Long.parseLong( (String) itr.next());
        String tmp = (String) itr.next();
        long futurePrice = UPriceInfo.INVALID_PRICE;
        if (!tmp.equals("")) {
          futurePrice = Long.parseLong(tmp);
        }
        UPriceInfo pi = new UPriceInfo();
        pi.setDate(date);
        pi.setSession(session);
        pi.setSpotPrice(spotPrice);
        pi.setFuturePrice(session);
        fPriceInfoArray.addElement(pi);
        ++lineNo;
      }
    } catch (NumberFormatException nfe) {
      throw new ParseException("Error in UTimeSeriesDefinitionLog.readFrom",
                               lineNo);
    } catch (NoSuchElementException nsee) {
      throw new ParseException("Error in UTimeSeriesDefinitionLog.readFrom",
                               lineNo);
    }
  }

  /**
   * ���i�n����o�̓X�g���[���ɏ����o���܂��D
   * @param pw �o�̓X�g���[��
   * @throws IOException
   */
  public void writeTo(PrintWriter pw) throws IOException {
    pw.println("Date,Session,SpotPrice,FuturePrice");
    Enumeration e = fPriceInfoArray.elements();
    while (e.hasMoreElements()) {
      UPriceInfo pi = (UPriceInfo) e.nextElement();
      pw.print(pi.getDate() + ",");
      pw.print(pi.getSession() + ",");
      pw.print(pi.getSpotPrice() + ",");
      if (pi.getFuturePrice() == UPriceInfo.INVALID_PRICE) {
        pw.println();
      } else {
        pw.println(pi.getFuturePrice());
      }
    }
  }

  /**
   * ����܂ł̉��i�n����o�̓X�g���[���֏����o���܂��D
   * @param pw �o�̓X�g���[��
   * @throws IOException
   */
  public void writePriceInfoBeforeCurrentPtr(PrintWriter pw) throws IOException {
    pw.println("Date,Session,SpotPrice,FuturePrice");
    for (int i = 0; i < fCurrentPtr; ++i) {
      UPriceInfo pi = (UPriceInfo) fPriceInfoArray.get(i);
      pw.print(pi.getDate() + ",");
      pw.print(pi.getSession() + ",");
      pw.print(pi.getSpotPrice() + ",");
      if (pi.getFuturePrice() == UPriceInfo.INVALID_PRICE) {
        pw.println();
      } else {
        pw.println(pi.getFuturePrice());
      }
    }
  }

  /**
   * �ŐV�̉��i�n����o�̓X�g���[���֏����o���܂��D
   * @param pw �o�̓X�g���[��
   * @throws IOException
   */
  public void writeLatestPriceInfo(PrintWriter pw) throws IOException {
    UPriceInfo pi = (UPriceInfo) fPriceInfoArray.get(fCurrentPtr - 1);
    pw.print(pi.getDate() + ",");
    pw.print(pi.getSession() + ",");
    pw.print(pi.getSpotPrice() + ",");
    if (pi.getFuturePrice() == UPriceInfo.INVALID_PRICE) {
      pw.println();
    } else {
      pw.println(pi.getFuturePrice());
    }
  }

  /**
   * �|�C���^���P�X�e�b�v�i�߂܂��D
   *
   */
  private void incrementPtr() {
    if (fCurrentPtr > fInitialPtr + fMaxSteps) {
      System.err.println("Error: initializerPtr error in UPriceInfoDB");
      System.exit( -1);
    }
    ++fCurrentPtr;
  }

}
