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
 * Network�ł̃N���C�A���g��Ŏ��s�����C����������擾���邽�߂�SVMP�R�}���h�N���X�ł��D
 */
public class UCCExchangeProfileNet extends UCExchangeProfileCore implements
    IClientCmdNet {

  /** �T�[�o�[����̓��̓X�g���[�� */
  private BufferedReader fIn;

  /** �T�[�o�[�ւ̏o�̓X�g���[�� */
  private PrintWriter fOut;

  /**
   * �R���X�g���N�^�ł��D
   */
  UCCExchangeProfileNet() {
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
      fOut.println(CMD_NAME);
      StringTokenizer st = new StringTokenizer(fIn.readLine());
      String token = st.nextToken();
      if (token.equals("+ACCEPT")) {
        fCommandStatus.setStatus(true);
        fData.put(UCExchangeProfileCore.LONG_CASH, Long.valueOf(fIn.readLine())); // �ۗL����
        fData.put(UCExchangeProfileCore.LONG_SELL_POSITION,
                  Long.valueOf(fIn.readLine())); // ������̊Ǘ����̔��|�W�V����
        fData.put(UCExchangeProfileCore.LONG_BUY_POSITION,
                  Long.valueOf(fIn.readLine())); // ������̊Ǘ����̔��|�W�V����
        fData.put(UCExchangeProfileCore.INT_NO_OF_MEMBERS,
                  Integer.valueOf(fIn.readLine())); // �����
        String okMsg = fIn.readLine();
        if (!okMsg.equals("+OK")) {
          System.err.println("Unexpected token in UCCExchangeProfileNet: " +
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
          System.err.println("Unexpected token in UCCExchangeProfileNet: " +
                             okMsg);
          System.exit(5);
        }
      } else {
        System.err.println("Unexpected token in UCCExchangeProfileNet: " +
                           token);
        System.exit(5);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(5);
    }
    return fCommandStatus;
  }

}
