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

import log.UCsvUtility;


/**
 * ���i�n��̒�`���Ǘ�����N���X�ł��D
 * resources/csv/TimeSeriesDefinitions.csv�ɑΉ���������Ǘ����܂��D
 */
public class UTimeSeriesDefinitions {

  /** ���n��̃j�b�N�l�[�����������߂̃L�[ */
  public static final String STRING_NICKNAME = "STRING_NICKNAME";

  /** ���n��f�[�^�̃t�@�C���l�[�����������߂̃L�[ */
  public static final String STRING_FILENAME = "STRING_FILENAME";

  /** ���������������߂̃L�[ */
  public static final String STRING_BRAND_NAME = "STRING_BRAND_NAME";

  /** �J�n�X�e�b�v���������߂̃L�[ */
  public static final String INT_START_STEP = "INT_START_STEP";

  /** �ő����������������߂̃L�[ */
  public static final String INT_MAX_DATE = "INT_MAX_DATE";

  /** �P��������̐ߐ����������߂̃L�[ */
  public static final String INT_NO_OF_SESSIONS_PER_DAY = "INT_NO_OF_SESSIONS_PER_DAY";

  /** ���n���`�f�[�^���i�[���邽�߂̔z�� */
  private ArrayList fArray = new ArrayList();

  /**
   * �R���X�g���N�^�ł��D
   */
  public UTimeSeriesDefinitions() {
  }

  /**
   * ���n���`�f�[�^��ǂݍ��݂܂��D
   * @param br BufferedReader
   * @throws IOException
   */
  public void readFrom(BufferedReader br) throws IOException, ParseException {
    clear();
    br.readLine(); // Skip the header
    String line = null;
    int lineNo = 1;
    try {
      while ( (line = br.readLine()) != null) {
        ArrayList list = UCsvUtility.split(line);
        Iterator itr = list.iterator();
        addDefinition( (String) itr.next(), (String) itr.next(),
                      (String) itr.next(),
                      Integer.parseInt( (String) itr.next()),
                      Integer.parseInt( (String) itr.next()),
                      Integer.parseInt( (String) itr.next()));
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
   * ���n���`�f�[�^�������o���܂��D
   * @param pw PrintWriter
   * @throws IOException
   */
  public void writeTo(PrintWriter pw) throws IOException {
    pw.println("Nickname,Filename,BrandName,StartStep,MaxDate,NoOfSessionsPerDay");
    Iterator itr = fArray.iterator();
    while (itr.hasNext()) {
      HashMap info = (HashMap) itr.next();
      pw.print(info.get(UTimeSeriesDefinitions.STRING_NICKNAME) + ",");
      pw.print(info.get(UTimeSeriesDefinitions.STRING_FILENAME) + ",");
      pw.print(info.get(UTimeSeriesDefinitions.STRING_BRAND_NAME) + ",");
      pw.print(info.get(UTimeSeriesDefinitions.INT_START_STEP) + ",");
      pw.print(info.get(UTimeSeriesDefinitions.INT_MAX_DATE) + ",");
      pw.println(info.get(UTimeSeriesDefinitions.INT_NO_OF_SESSIONS_PER_DAY) + "");
    }
  }

  /**
   * ���i�n���`�̃C�e���[�^��Ԃ��܂��D
   * @return ���i�n���`�̃C�e���[�^
   */
  public Iterator getTimeSeriesDefinitions() {
    return fArray.iterator();
  }

  /**
   * ���i�n��̒�`��S�ăN���A���܂��D
   *
   */
  public void clear() {
    fArray.clear();
  }

  /**
   * ���i�n��̒�`��ǉ����܂��D
   * @param nickname ���i�n��̃j�b�N�l�[��
   * @param filename ���i�n��̃t�@�C����
   * @param brandName ������
   * @param startStep �J�n�X�e�b�v
   * @param maxDate �������
   * @param noOfSessionsPerDay �P��������̐ߐ�
   */
  public void addDefinition(String nickname, String filename, String brandName,
                            int startStep, int maxDate, int noOfSessionsPerDay) {
    HashMap info = new HashMap();
    info.put(UTimeSeriesDefinitions.STRING_NICKNAME, nickname);
    info.put(UTimeSeriesDefinitions.STRING_FILENAME, filename);
    info.put(UTimeSeriesDefinitions.STRING_BRAND_NAME, brandName);
    info.put(UTimeSeriesDefinitions.INT_START_STEP, new Integer(startStep));
    info.put(UTimeSeriesDefinitions.INT_MAX_DATE, new Integer(maxDate));
    info.put(UTimeSeriesDefinitions.INT_NO_OF_SESSIONS_PER_DAY,
             new Integer(noOfSessionsPerDay));
    fArray.add(info);
  }

  /**
   * �j�b�N�l�[����nickname�ł��鉿�i�n���`��Ԃ��܂��D
   * @param nickname
   * @return �j�b�N�l�[����nickname�ł��鉿�i�n���`
   */
  public HashMap getDefinition(String nickname) {
    Iterator itr = fArray.iterator();
    while (itr.hasNext()) {
      HashMap info = (HashMap) itr.next();
      if (nickname.equals( (String) info.get(UTimeSeriesDefinitions.STRING_NICKNAME))) {
        return info;
      }
    }
    return null;
  }

  /**
   * �e�X�g�p���C�����\�b�h�ł��D
   * @param args
   */
  public static void main(String[] args) {
    UTimeSeriesDefinitions timeSeriesDefinitionLog = new UTimeSeriesDefinitions();
    try {
      BufferedReader br = new BufferedReader(new FileReader("resources/csv/TimeSeriesDefinitions.csv"));
      timeSeriesDefinitionLog.readFrom(br);
      br.close();
      PrintWriter pw = new PrintWriter(System.out, true);
      timeSeriesDefinitionLog.writeTo(pw);
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(5);
    }
  }

}
