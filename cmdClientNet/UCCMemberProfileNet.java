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
package cmdClientNet;

import java.io.*;
import java.util.*;

import cmdCore.*;

/**
 * Network�ł̃N���C�A���g��Ŏ��s�����C��������擾���邽�߂�SVMP�R�}���h�N���X�ł��D
 */
public class UCCMemberProfileNet extends UCMemberProfileCore implements
    IClientCmdNet {

  /** �T�[�o�[����̓��̓X�g���[�� */
  private BufferedReader fIn;

  /** �T�[�o�[�ւ̏o�̓X�g���[�� */
  private PrintWriter fOut;

  /**
   * �R���X�g���N�^�ł��D
   */
  UCCMemberProfileNet() {
    super();
    fIn = null;
    fOut = null;
  }

  /*
   * (non-Javadoc)
   * @see cmdClientNet.IClientCmdNet#setConnection(java.io.BufferedReader, java.io.PrintWriter)
   */
  public void setConnection(BufferedReader br, PrintWriter pw) {
    fIn = br;
    fOut = pw;
  }

  /*
   * (non-Javadoc)
   * @see cmdCore.ICommand#doIt()
   */
  public UCommandStatus doIt() {
    try {
      fData.clear();
      fOut.println(CMD_NAME + " " + fTargetUserId);
      fOut.flush();
      StringTokenizer st = new StringTokenizer(fIn.readLine());
      String token = st.nextToken();
      if (token.equals("+ACCEPT")) {
        fCommandStatus.setStatus(true);
        fData.put(UCMemberProfileCore.STRING_LOGIN_NAME, fIn.readLine()); // �����
        fData.put(UCMemberProfileCore.STRING_PASSWORD, fIn.readLine()); // �p�X���[�h
        fData.put(UCMemberProfileCore.STRING_ATTRIBUTE, fIn.readLine()); // �G�[�W�F���g����(Human or Machine)
        fData.put(UCMemberProfileCore.STRING_CONNECTION, fIn.readLine()); // �R�l�N�V����(Remote or Local)
        fData.put(UCMemberProfileCore.ARRAY_LIST_ACCESS,
                  UCMemberProfileCore.stringToArrayList(fIn.readLine())); // �A�N�Z�X����
        fData.put(UCMemberProfileCore.STRING_REAL_NAME, fIn.readLine()); // ���ۂ̖��O
        fData.put(UCMemberProfileCore.ARRAY_LIST_SYSTEM_PARAMETERS,
                  UCMemberProfileCore.stringToArrayList(fIn.readLine())); // �V�X�e���p�����[�^
        fData.put(UCMemberProfileCore.INT_SEED, Integer.valueOf(fIn.readLine())); // �����̎�
        fData.put(UCMemberProfileCore.LONG_INITIAL_CASH,
                  Long.valueOf(fIn.readLine())); // �������Y
        fData.put(UCMemberProfileCore.LONG_TRADING_UNIT,
                  Long.valueOf(fIn.readLine())); // ����P��
        fData.put(UCMemberProfileCore.LONG_FEE_PER_UNIT,
                  Long.valueOf(fIn.readLine())); // �P�ʎ��������̎萔��
        fData.put(UCMemberProfileCore.LONG_MARGIN_RATE,
                  Long.valueOf(fIn.readLine())); // �؋�����
        fData.put(UCMemberProfileCore.LONG_MAX_LOAN, Long.valueOf(fIn.readLine())); // �؂������x�z
        fData.put(UCMemberProfileCore.DOUBLE_INTEREST,
                  Double.valueOf(fIn.readLine())); // �؂�������
        fData.put(UCMemberProfileCore.INT_STATUS, Integer.valueOf(fIn.readLine())); // ����\(+1),����s�\(-1)
        fData.put(UCMemberProfileCore.INT_NO_OF_LOGIN_AGENTS,
                  Integer.valueOf(fIn.readLine())); // ���O�C�����̃G�[�W�F���g��
        HashMap yesterdayBalance = new HashMap(); // �O�����x
        yesterdayBalance.put(UCMemberProfileCore.LONG_INITIAL_CASH,
                             Long.valueOf(fIn.readLine())); // ����������
        yesterdayBalance.put(UCMemberProfileCore.LONG_LOAN,
                             Long.valueOf(fIn.readLine())); // �ؓ���
        yesterdayBalance.put(UCMemberProfileCore.LONG_UNREALIZED_PROFIT,
                             Long.valueOf(fIn.readLine())); // ���������v
        yesterdayBalance.put(UCMemberProfileCore.LONG_MARGIN,
                             Long.valueOf(fIn.readLine())); // �a���؋���
        yesterdayBalance.put(UCMemberProfileCore.LONG_SUM_OF_FEE,
                             Long.valueOf(fIn.readLine())); // ���x�����萔��
        yesterdayBalance.put(UCMemberProfileCore.LONG_SUM_OF_INTEREST,
                             Long.valueOf(fIn.readLine())); // ���x��������
        yesterdayBalance.put(UCMemberProfileCore.LONG_CASH,
                             Long.valueOf(fIn.readLine())); // �ۗL����
        yesterdayBalance.put(UCMemberProfileCore.LONG_PROFIT,
                             Long.valueOf(fIn.readLine())); // �������v
        fData.put(HASHMAP_YESTERDAY_BALANCE, yesterdayBalance);
        HashMap todayBalance = new HashMap(); // �������x
        todayBalance.put(UCMemberProfileCore.LONG_INITIAL_CASH,
                         Long.valueOf(fIn.readLine())); // ����������
        todayBalance.put(UCMemberProfileCore.LONG_LOAN,
                         Long.valueOf(fIn.readLine())); // �ؓ���
        todayBalance.put(UCMemberProfileCore.LONG_UNREALIZED_PROFIT,
                         Long.valueOf(fIn.readLine())); // ���������v
        todayBalance.put(UCMemberProfileCore.LONG_MARGIN,
                         Long.valueOf(fIn.readLine())); // �a���؋���
        todayBalance.put(UCMemberProfileCore.LONG_SUM_OF_FEE,
                         Long.valueOf(fIn.readLine())); // ���x�����萔��
        todayBalance.put(UCMemberProfileCore.LONG_SUM_OF_INTEREST,
                         Long.valueOf(fIn.readLine())); // ���x��������
        todayBalance.put(UCMemberProfileCore.LONG_CASH,
                         Long.valueOf(fIn.readLine())); // �ۗL����
        todayBalance.put(UCMemberProfileCore.LONG_PROFIT,
                         Long.valueOf(fIn.readLine())); // �������v
        fData.put(HASHMAP_TODAY_BALANCE, todayBalance);
        HashMap position = new HashMap(); // �|�W�V����
        position.put(UCMemberProfileCore.
                     LONG_SUM_OF_SELL_POSITIONS_UNTIL_YESTERDAY,
                     Long.valueOf(fIn.readLine())); // �O���܂ł̔��|�W�V�����̍��v
        position.put(UCMemberProfileCore.
                     LONG_SUM_OF_BUY_POSITIONS_UNTIL_YESTERDAY,
                     Long.valueOf(fIn.readLine())); // �O���܂ł̔��|�W�V�����̍��v
        position.put(UCMemberProfileCore.LONG_TODAY_SELL_POSITIONS,
                     Long.valueOf(fIn.readLine())); // �����̔��|�W�V�����̍��v
        position.put(UCMemberProfileCore.LONG_TODAY_BUY_POSITIONS,
                     Long.valueOf(fIn.readLine())); // �����̔��|�W�V�����̍��v
        fData.put(HASHMAP_POSITION, position);
        String okMsg = fIn.readLine();
        if (!okMsg.equals("+OK")) {
          System.err.println("Unexpected token in UCCMemberProfileNet: " +
                             okMsg);
          System.exit(5);
        }
      } else if (token.equals("+ERROR")) {
        fCommandStatus.setStatus(false);
        int errCode = Integer.parseInt(st.nextToken());
        fCommandStatus.setErrorCode(errCode);
        fCommandStatus.setErrorMessage(fIn.readLine());
        String okMsg = fIn.readLine();
        if (!okMsg.equals("+OK")) {
          System.err.println("Unexpected token in UCCMemberProfileNet: " +
                             okMsg);
          System.exit(5);
        }
      } else {
        System.err.println("Unexpected token in UCCMemberProfileNet: " + token);
        System.exit(5);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(5);
    }
    return fCommandStatus;
  }

}
