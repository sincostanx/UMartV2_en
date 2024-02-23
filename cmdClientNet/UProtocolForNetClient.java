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
import java.net.*;
import java.util.*;

import cmdCore.*;

/**
 * �l�b�g���[�N�z���ɓ��삷��N���C�A���g�p�̃v���g�R���N���X�ł��D
 */
public class UProtocolForNetClient extends UProtocolCore {

  /** �ڑ���z�X�g�� */
  private String fHostname;

  /** �ڑ���|�[�g�ԍ� */
  private int fPort;

  /** ���̓X�g���[�� */
  private BufferedReader fIn;

  /** �o�̓X�g���[�� */
  private PrintWriter fOut;

  /**
   * �R���X�g���N�^�ł��D
   */
  public UProtocolForNetClient() {
    super();
    fHostname = null;
    fPort = -1;
    fIn = null;
    fOut = null;
    fCommandHash.put(UCLoginCore.CMD_NAME, new UCCLoginNet());
    fCommandHash.put(UCLogoutCore.CMD_NAME, new UCCLogoutNet());
    fCommandHash.put(UCServerDateCore.CMD_NAME, new UCCServerDateNet());
    fCommandHash.put(UCSpotPriceCore.CMD_NAME, new UCCSpotPriceNet());
    fCommandHash.put(UCFuturePriceCore.CMD_NAME, new UCCFuturePriceNet());
    fCommandHash.put(UCOrderRequestCore.CMD_NAME, new UCCOrderRequestNet());
    fCommandHash.put(UCHistoricalQuotesCore.CMD_NAME,
                     new UCCHistoricalQuotesNet());
    fCommandHash.put(UCBalancesCore.CMD_NAME, new UCCBalancesNet());
    fCommandHash.put(UCPositionCore.CMD_NAME, new UCCPositionNet());
    fCommandHash.put(UCSpotIntervalCore.CMD_NAME, new UCCSpotIntervalNet());
    fCommandHash.put(UCMarketStatusCore.CMD_NAME, new UCCMarketStatusNet());
    fCommandHash.put(UCScheduleCore.CMD_NAME, new UCCScheduleNet());
    fCommandHash.put(UCOrderStatusCore.CMD_NAME, new UCCOrderStatusNet());
    fCommandHash.put(UCExecutionsCore.CMD_NAME, new UCCExecutionsNet());
    fCommandHash.put(UCTodaysQuotesCore.CMD_NAME, new UCCTodaysQuotesNet());
    fCommandHash.put(UCBoardInformationCore.CMD_NAME,
                     new UCCBoardInformationNet());
    fCommandHash.put(UCBoardDataCore.CMD_NAME, new UCCBoardDataNet());
    fCommandHash.put(UCOrderCancelCore.CMD_NAME, new UCCOrderCancelNet());
    fCommandHash.put(UCAllBalancesCore.CMD_NAME, new UCCAllBalancesNet());
    fCommandHash.put(UCAllPositionsCore.CMD_NAME, new UCCAllPositionsNet());
    fCommandHash.put(UCServerTimeCore.CMD_NAME, new UCCServerTimeNet());
    fCommandHash.put(UCSetSpotDateCore.CMD_NAME, new UCCSetSpotDateNet());
    fCommandHash.put(UCOrderCheckCore.CMD_NAME, new UCCOrderCheckNet());
    fCommandHash.put(UCOrderIDHistoryCore.CMD_NAME, new UCCOrderIDHistoryNet());
    fCommandHash.put(UCServerStatusCore.CMD_NAME, new UCCServerStatusNet());
    fCommandHash.put(UCMemberProfileCore.CMD_NAME, new UCCMemberProfileNet());
    fCommandHash.put(UCExchangeProfileCore.CMD_NAME, new UCCExchangeProfileNet());
    fCommandHash.put(UCExchangeProfileCore.CMD_NAME, new UCCExchangeProfileNet());
    fCommandHash.put(UCAccountHistoryCore.CMD_NAME, new UCCAccountHistoryNet());
  }

  /**
   * ������ւ̒ʐM�o�H���m�ۂ��܂��D
   * @param hostname ����������삵�Ă���z�X�g��
   * @param port ������̃|�[�g�ԍ�
   * @return true�F�ڑ������Cfalse�F�ڑ����s
   */
  public boolean setConnection(String hostname, int port) {
    fHostname = hostname;
    fPort = port;
    try {
      Socket socket = new Socket(fHostname, fPort);
      socket.setSoTimeout(10000);
      fIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      fOut = new PrintWriter(socket.getOutputStream(), true);
      StringTokenizer tokens = new StringTokenizer(fIn.readLine());
      if (!tokens.nextToken().equals("+LOGIN")) {
        return false;
      }
      String version = tokens.nextToken();
      Collection c = fCommandHash.values();
      Iterator itr = c.iterator();
      while (itr.hasNext()) {
        IClientCmdNet isc = (IClientCmdNet) itr.next();
        isc.setConnection(fIn, fOut);
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * �e�X�g�p�̃��C�����\�b�h�ł��D
   * @param args ������̃z�X�g�� �|�[�g�ԍ�
   */
  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("usage: java UCProtocolNet hostname port");
      System.exit(1);
    }
    String hostname = args[0];
    int port = Integer.parseInt(args[1]);
    UProtocolForNetClient cp = new UProtocolForNetClient();
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    try {
      cp.setConnection(hostname, port);
      boolean loginFlag = false;
      while (true) {
        System.out.print("umart> ");
        String line = br.readLine();
        StringTokenizer st = new StringTokenizer(line);
        if (!st.hasMoreTokens()) {
          continue;
        }
        String cmdStr = st.nextToken();
        if (cmdStr.equals("exit")) {
          break;
        }
        if ( (cmdStr.equalsIgnoreCase("Login") && loginFlag)
            || (!cmdStr.equalsIgnoreCase("Login") && !loginFlag)) {
          continue;
        }
        ICommand cmd = (ICommand) cp.getCommand(cmdStr);
        if (cmd == null) {
          System.err.println("Command:" + cmdStr + " is not found!!");
        } else {
          if (!cmd.setArguments(st)) {
            System.err.println("Invalid arguments!!");
          } else {
            UCommandStatus status = cmd.doIt();
            if (status.getStatus()) {
              cmd.printOn();
              if (cmdStr.equalsIgnoreCase("Login")) {
                loginFlag = true;
              }
            } else {
              System.err.println(status.getErrorMessage());
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(5);
    }
  }

}
