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
 * ���[�U�̃��X�g���Ǘ�����N���X�ł��D
 * resources/csv/MembersNet.csv�Cresources/csv/MembersSA.csv�ɑΉ�������������܂��D */
public class UMemberList {

  /** ���O�C�������������߂̃L�[ */
  public static final String STRING_LOGIN_NAME = "STRING_LOGIN_NAME";

  /** �p�X���[�h���������߂̃L�[ */
  public static final String STRING_PASSWORD = "STRING_PASSWORD";

  /** �G�[�W�F���g����(Human or Machine)���������߂̃L�[ */
  public static final String STRING_ATTRIBUTE = "STRING_ATTRIBUTE";

  /** �R�l�N�V����(Remote or Local)���������߂̃L�[ */
  public static final String STRING_CONNECTION = "STRING_CONNECTION";

  /** �A�N�Z�X�������������߂̃L�[ */
  public static final String ARRAY_LIST_ACCESS = "ARRAY_LIST_ACCESS";

  /** ���ۂ̖��O���������߂̃L�[ */
  public static final String STRING_REAL_NAME = "STRING_REAL_NAME";

  /** �V�X�e���p�����[�^���i�[����ArrayList���������߂̃L�[ */
  public static final String ARRAY_LIST_SYSTEM_PARAMETERS =
      "ARRAY_LIST_SYSTEM_PARAMETERS";

  /** �����̎���������߂̃L�[ */
  public static final String INT_SEED = "INT_SEED";

  /** �������Y���������߂̃L�[ */
  public static final String LONG_INITIAL_CASH = "LONG_INITIAL_CASH";

  /** ����P�ʂ��������߂̃L�[ */
  public static final String LONG_TRADING_UNIT = "LONG_TRADING_UNIT";

  /** �P�ʎ��������̎萔�����������߂̃L�[ */
  public static final String LONG_FEE_PER_UNIT = "LONG_FEE_PER_UNIT";

  /** �؋��������������߂̃L�[ */
  public static final String LONG_MARGIN_RATE = "LONG_MARGIN_RATE";

  /** �؂������x�z���������߂̃L�[ */
  public static final String LONG_MAX_LOAN = "LONG_MAX_LOAN";

  /** �؂����������������߂̃L�[ */
  public static final String DOUBLE_INTEREST = "DOUBLE_INTEREST";

  /** �w�b�_�[��� */
  public static final String[] HEADER_ARRAY = {"LoginName",
      "Password",
      "Attribute",
      "Connection",
      "Access",
      "RealName",
      "SystemParameters",
      "Seed",
      "InitialCash",
      "TradingUnit",
      "FeePerUnit",
      "MarginRate",
      "MaxLoan",
      "Interest"};

  /** �f�t�H���g�̃��[�U�[���(HashMap�^�j�̊i�[���ꂽ�z�� */
  private static ArrayList fDefaultMemberArray = new ArrayList();

  /** ���[�U�[���(HashMap�^)�̊i�[���ꂽ�z�� */
  private ArrayList fMemberArray;

  /**
   * �f�t�H���g���[�U�[����ǉ����܂��D
   * @param loginName ���O�C����
   * @param passwd �p�X���[�h
   * @param attribute �G�[�W�F���g����(Human or Machine)
   * @param connection �R�l�N�V����(Remote or Local)
   * @param access �A�N�Z�X�������(�R������؂�)
   * @param realName ���ۂ̖��O
   * @param systemParameters �G�[�W�F���g�ւ̈����i�R������؂�j
   * @param seed �����̎�(�V�X�e���Ɍ��肵�Ăق����Ƃ��ɂ�-1)
   * @param initialCash �������Y
   * @param tradingUnit ����P��
   * @param feePerUnit �P�ʎ��������̎萔��
   * @param marginRate �؋�����
   * @param maxLoan �؂������x�z
   * @param interest �؂�������
   */
  public static void appendDefaultMember(String loginName, String passwd,
                                         String attribute, String connection,
                                         ArrayList access, String realName,
                                         ArrayList systemParameters, int seed,
                                         long initialCash, long tradingUnit,
                                         long feePerUnit, long marginRate,
                                         long maxLoan, double interest) {
    HashMap info = new HashMap();
    info.put(UMemberList.STRING_LOGIN_NAME, loginName);
    info.put(UMemberList.STRING_PASSWORD, passwd);
    info.put(UMemberList.STRING_ATTRIBUTE, attribute);
    info.put(UMemberList.STRING_CONNECTION, connection);
    info.put(UMemberList.ARRAY_LIST_ACCESS, access);
    info.put(UMemberList.STRING_REAL_NAME, realName);
    info.put(UMemberList.ARRAY_LIST_SYSTEM_PARAMETERS, systemParameters);
    info.put(UMemberList.INT_SEED, new Integer(seed));
    info.put(UMemberList.LONG_INITIAL_CASH, new Long(initialCash));
    info.put(UMemberList.LONG_TRADING_UNIT, new Long(tradingUnit));
    info.put(UMemberList.LONG_FEE_PER_UNIT, new Long(feePerUnit));
    info.put(UMemberList.LONG_MARGIN_RATE, new Long(marginRate));
    info.put(UMemberList.LONG_MAX_LOAN, new Long(maxLoan));
    info.put(UMemberList.DOUBLE_INTEREST, new Double(interest));
    fDefaultMemberArray.add(info);
  }

  /**
   * �f�t�H���g���[�U�[�����N���A���܂��D
   */
  public static void clearDefaultMembers() {
    fDefaultMemberArray.clear();
  }

  /**
   * �R���X�g���N�^�ł��D
   */
  public UMemberList() {
    fMemberArray = new ArrayList();
    Iterator itr = fDefaultMemberArray.iterator();
    while (itr.hasNext()) {
      fMemberArray.add(itr.next());
    }
  }

  /** ���[�U�[����ǂݍ��݂܂��D
   * @param br BufferedReader
   * @throws IOException
   */
  public void readFrom(BufferedReader br) throws IOException, ParseException {
    fMemberArray.clear();
    {
      Iterator itr = fDefaultMemberArray.iterator();
      while (itr.hasNext()) {
        fMemberArray.add(itr.next());
      }
    }
    br.readLine(); // skip header
    String line = null;
    int lineNo = 1;
    try {
      while ( (line = br.readLine()) != null) {
        ArrayList info = UCsvUtility.split(line);
        Iterator itr = info.iterator();
        String loginName = (String) itr.next();
        String passwd = (String) itr.next();
        String attribute = (String) itr.next();
        String connection = (String) itr.next();
        ArrayList access = UMemberList.stringToArrayList( (String) itr.next());
        String realName = (String) itr.next();
        ArrayList systemParameters = UMemberList.stringToArrayList( (String) itr.
            next());
        String nextString = (String) itr.next();
        int seed = -1;
        if (!nextString.equals("")) {
          seed = Integer.parseInt(nextString);
        }
        long initialCash = Long.parseLong( (String) itr.next());
        long tradingUnit = Long.parseLong( (String) itr.next());
        long feePerUnit = Long.parseLong( (String) itr.next());
        long marginRate = Long.parseLong( (String) itr.next());
        long maxLoan = Long.parseLong( (String) itr.next());
        double interest = Double.parseDouble( (String) itr.next());
        appendMember(loginName, passwd, attribute, connection, access, realName,
                     systemParameters, seed, initialCash, tradingUnit,
                     feePerUnit, marginRate, maxLoan, interest);
        ++lineNo;
      }
    } catch (NoSuchElementException nsee) {
      throw new ParseException("Error in UMemberLog.readFrom", lineNo);
    } catch (NumberFormatException nfe) {
      throw new ParseException("Error in UMemberLog.readFrom", lineNo);
    }
  }

  private void writeHeaderTo(PrintWriter pw) throws IOException {
    for (int i = 0; i < UMemberList.HEADER_ARRAY.length; ++i) {
      pw.print(UMemberList.HEADER_ARRAY[i]);
      if (i == UMemberList.HEADER_ARRAY.length - 1) {
        pw.println();
      } else {
        pw.print(",");
      }
    }
  }

  /** ���[�U�[���������o���܂��D
   * @param pw PrintWriter
   * @throws IOException
   */
  public void writeTo(PrintWriter pw) throws IOException {
    writeHeaderTo(pw);
    Iterator itr = getMembers();
    while (itr.hasNext()) {
      HashMap info = (HashMap) itr.next();
      pw.print(info.get(UMemberList.STRING_LOGIN_NAME) + ",");
      pw.print(info.get(UMemberList.STRING_PASSWORD) + ",");
      pw.print(info.get(UMemberList.STRING_ATTRIBUTE) + ",");
      pw.print(info.get(UMemberList.STRING_CONNECTION) + ",");
      pw.print(UMemberList.arrayListToString( (ArrayList) info.get(UMemberList.
          ARRAY_LIST_ACCESS)) + ",");
      pw.print(info.get(UMemberList.STRING_REAL_NAME) + ",");
      pw.print(UMemberList.arrayListToString( (ArrayList) info.get(UMemberList.
          ARRAY_LIST_SYSTEM_PARAMETERS)) + ",");
      int seed = Integer.parseInt(info.get(UMemberList.INT_SEED).toString());
      if (seed < 0) {
        pw.print(",");
      } else {
        pw.print(seed + ",");
      }
      pw.print(info.get(UMemberList.LONG_INITIAL_CASH) + ",");
      pw.print(info.get(UMemberList.LONG_TRADING_UNIT) + ",");
      pw.print(info.get(UMemberList.LONG_FEE_PER_UNIT) + ",");
      pw.print(info.get(UMemberList.LONG_MARGIN_RATE) + ",");
      pw.print(info.get(UMemberList.LONG_MAX_LOAN) + ",");
      pw.println(info.get(UMemberList.DOUBLE_INTEREST));
    }
  }

  /**
   * ���[�U�[����ǉ����܂��D
   * @param loginName ���O�C����
   * @param passwd �p�X���[�h
   * @param attribute �G�[�W�F���g����(Human or Machine)
   * @param access �A�N�Z�X�������(�R������؂�)
   * @param connection �R�l�N�V����(Remote or Local)
   * @param realName ���ۂ̖��O
   * @param systemParameters �G�[�W�F���g�ւ̈����i�R������؂�j
   * @param seed �����̎�(�V�X�e���Ɍ��肵�Ăق����Ƃ��ɂ�-1)
   * @param initialCash �������Y
   * @param tradingUnit ����P��
   * @param feePerUnit �P�ʎ��������̎萔��
   * @param marginRate �؋�����
   * @param maxLoan �؂������x�z
   * @param interest �؂�������
   */
  public void appendMember(String loginName, String passwd,
                           String attribute, String connection,
                           ArrayList access, String realName,
                           ArrayList systemParameters, int seed,
                           long initialCash, long tradingUnit,
                           long feePerUnit, long marginRate,
                           long maxLoan, double interest) {
    HashMap info = new HashMap();
    info.put(UMemberList.STRING_LOGIN_NAME, loginName);
    info.put(UMemberList.STRING_PASSWORD, passwd);
    info.put(UMemberList.STRING_ATTRIBUTE, attribute);
    info.put(UMemberList.STRING_CONNECTION, connection);
    info.put(UMemberList.ARRAY_LIST_ACCESS, access);
    info.put(UMemberList.STRING_REAL_NAME, realName);
    info.put(UMemberList.ARRAY_LIST_SYSTEM_PARAMETERS, systemParameters);
    info.put(UMemberList.INT_SEED, new Integer(seed));
    info.put(UMemberList.LONG_INITIAL_CASH, new Long(initialCash));
    info.put(UMemberList.LONG_TRADING_UNIT, new Long(tradingUnit));
    info.put(UMemberList.LONG_FEE_PER_UNIT, new Long(feePerUnit));
    info.put(UMemberList.LONG_MARGIN_RATE, new Long(marginRate));
    info.put(UMemberList.LONG_MAX_LOAN, new Long(maxLoan));
    info.put(UMemberList.DOUBLE_INTEREST, new Double(interest));
    fMemberArray.add(info);
  }

  /**
   * ���[�U�[����ǉ����܂��D
   * @param loginName ���O�C����
   * @param passwd �p�X���[�h
   * @param attribute �G�[�W�F���g����(Human or Machine)
   * @param access �A�N�Z�X�������(�R������؂�)
   * @param connection �R�l�N�V����(Remote or Local)
   * @param realName ���ۂ̖��O
   * @param systemParameters �G�[�W�F���g�ւ̈����i�R������؂�j
   * @param seed �����̎�(�V�X�e���Ɍ��肵�Ăق����Ƃ��ɂ�-1)
   * @param initialCash �������Y
   * @param tradingUnit ����P��
   * @param feePerUnit �P�ʎ��������̎萔��
   * @param marginRate �؋�����
   * @param maxLoan �؂������x�z
   * @param interest �؂�������
   */
  public void appendMemberAtHead(String loginName, String passwd,
                                 String attribute, String connection,
                                 ArrayList access, String realName,
                                 ArrayList systemParameters, int seed,
                                 long initialCash, long tradingUnit,
                                 long feePerUnit, long marginRate,
                                 long maxLoan, double interest) {
    ArrayList oldMemberArray = fMemberArray;
    fMemberArray = new ArrayList();
    appendMember(loginName, passwd, attribute, connection, access,
                 realName, systemParameters, seed, initialCash,
                 tradingUnit, feePerUnit, marginRate, maxLoan, interest);
    Iterator itr = oldMemberArray.iterator();
    while (itr.hasNext()) {
      fMemberArray.add(itr.next());
    }
  }

  /**
   * �S�Ẵ��[�U�[���(HashMap�^)�ɃA�N�Z�X���邽�߂̔����q��Ԃ��܂��D
   * @return �����q
   */
  public Iterator getMembers() {
    return fMemberArray.iterator();
  }

  /**
   * �S�Ẵ��[�U�[�����N���A���܂��D
   */
  public void clear() {
    fMemberArray.clear();
  }

  /**
   * �����o�[����Ԃ��܂��D
   * @return �����o�[��
   */
  public int getNoOfMembers() {
    return fMemberArray.size();
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return fMemberArray.toString();
  }

  /**
   * ArrayList�^�̏���String�i�R������؂�j�ɂ��ĕԂ��܂��D
   * @param list ArrayList
   * @return�@String
   */
  static public String arrayListToString(ArrayList list) {
    String result = "";
    Iterator itr = list.iterator();
    while (itr.hasNext()) {
      result += (String) itr.next();
      if (itr.hasNext()) {
        result += ":";
      }
    }
    return result;
  }

  /**
   * String�i�R������؂�j�̏���ArrayList�^�ɂ��ĕԂ��܂��D
   * @param str �R������؂�̏��
   * @return�@ArrayList
   */
  static public ArrayList stringToArrayList(String str) {
    ArrayList result = new ArrayList();
    StringTokenizer st = new StringTokenizer(str, ":");
    while (st.hasMoreTokens()) {
      String parameter = st.nextToken();
      result.add(parameter);
    }
    return result;
  }

  /**
   * �e�X�g�p���C�����\�b�h�ł��D
   * @param args 0:inputFile
   */
  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("Usage: java UMemberLog inputFile output");
      System.exit(1);
    }
    String inputFile = args[0];
    String outputFile = args[1];
    UMemberList memberLog = new UMemberList();
    try {
      BufferedReader br = new BufferedReader(new FileReader(inputFile));
      memberLog.readFrom(br);
      br.close();
      PrintWriter pw = new PrintWriter(new FileWriter(outputFile));
      memberLog.writeTo(pw);
      pw.close();
      System.out.println(memberLog);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(5);
    }
  }

}
