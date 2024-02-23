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
package clientNet;

import java.util.*;
import cmdClientNet.*;
import cmdCore.*;
import serverCore.UAgentFactory;
import strategyCore.UBaseAgent;

/**
 * �l�b�g���[�N�E�}�V���E�N���C�A���g�̖{�̂̃N���X�ł��D
 */
public class UNetClient {

  /** �헪�I�u�W�F�N�g */
  private UBaseAgent fStrategy;

  /** CProtocol�I�u�W�F�N�g */
  private UProtocolForNetClient fCProtocol;

  /** �^�p���� */
  private int fMaxDate;

  /** ���������̔񂹉� */
  private int fNoOfBoardsPerDay;

  /** ���݂̓��t */
  private int fDate;

  /** ���݂̔񂹉� */
  private int fBoardNo;

  /** ���݂̃T�[�o�[��� */
  private int fServerState;
  
  /** �T�[�o�ւ̖₢���킹�Ԋu [sec] */
  private long fInterval;

  /**
   * �R���X�g���N�^�ł��D
   * @param hostname �T�[�o�[�z�X�g���iIP�A�h���X�ł�OK�j
   * @param port �T�[�o�[�|�[�g�ԍ�
   * @param strategy �헪�I�u�W�F�N�g
   * @param interval �T�[�o�ւ̖₢���킹�Ԋu
   */
  public UNetClient(String hostname, int port, UBaseAgent strategy, long interval) {
    super();
    fStrategy = strategy;
    fCProtocol = new UProtocolForNetClient();
    fCProtocol.setConnection(hostname, port);
    fDate = 0;
    fBoardNo = 0;
    fServerState = -1;
    fInterval = interval;
  }

  /**
   * ���C�����[�v�ł��D
   */
  public void mainLoop() {
    fStrategy.setCProtocol(fCProtocol);
    login();
    setupSchedule();
    while (true) {
      waitNextBoard();
      if (fDate > fMaxDate) {
        logout();
        System.out.println("Normally terminated...");
        break;
      }
      System.out.println("Date:" + fDate + ", Board:" + fBoardNo + ", State:" +
                         fServerState);
      fStrategy.doActions(fDate, fBoardNo, fServerState, fMaxDate,
                          fNoOfBoardsPerDay);
    }
  }

  private void login() {
    String userName = fStrategy.getLoginName();
    String passwd = fStrategy.getPasswd();
    UCLoginCore loginCmd = (UCLoginCore) fCProtocol.getCommand(UCLoginCore.
        CMD_NAME);
    loginCmd.setArguments(userName, passwd);
    UCommandStatus cmdStatus = loginCmd.doIt();
    if (!cmdStatus.getStatus()) {
      System.err.println("Can't login with (" + userName + ", " + passwd + ")");
      System.exit(5);
    }
  }

  private void logout() {
    UCLogoutCore logoutCmd = (UCLogoutCore) fCProtocol.getCommand(UCLogoutCore.
        CMD_NAME);
    logoutCmd.doIt();
  }

  private void setupSchedule() {
    UCScheduleCore schCmd = (UCScheduleCore) fCProtocol.getCommand(
        UCScheduleCore.CMD_NAME);
    doCommand(schCmd);
    HashMap result = schCmd.getResults();
    fMaxDate = ( (Integer) result.get(UCScheduleCore.INT_MAX_DAY)).intValue();
    fNoOfBoardsPerDay = ( (Integer) result.get(UCScheduleCore.INT_NO_OF_BOARDS)).
        intValue();
  }

  private void doCommand(ICommand cmd) {
    UCommandStatus cmdStatus = cmd.doIt();
    while (!cmdStatus.getStatus()) {
      System.err.println("Error in doing " + cmd.getName());
      System.err.println(cmdStatus.getErrorMessage());
      System.err.println("Retrying...");
      cmdStatus = cmd.doIt();
    }
  }

  private void waitNextBoard() {
    ICommand cmd = null;
    while (true) {
      cmd = fCProtocol.getCommand(UCServerDateCore.CMD_NAME);
      doCommand(cmd);
      HashMap result = ((UCServerDateCore)cmd).getResults();
      int date = ((Integer)result.get(UCServerDateCore.INT_DAY)).intValue();
      int boardNo = ((Integer)result.get(UCServerDateCore.INT_BOARD_NO)).intValue();
      cmd = fCProtocol.getCommand(UCMarketStatusCore.CMD_NAME);
      doCommand(cmd);
      result = ((UCMarketStatusCore)cmd).getMarketInfo();
      int serverState = ((Integer)result.get(UCMarketStatusCore.INT_MARKET_STATUS)).intValue();
      if (date != fDate || boardNo != fBoardNo || fServerState != serverState) {
        fDate = date;
        fBoardNo = boardNo;
        fServerState = serverState;
        return;
      }
      try {
      	Thread.sleep(fInterval * 1000);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * ���C�����\�b�h�ł��D
   * @param args �T�[�o�� �|�[�g�ԍ� ���O�C���� �p�X���[�h �헪�N���X�� �����̎� ����Ԋu(�b) [�헪�N���X�ւ̈���1, ...]
   */
  public static void main(String[] args) {
    if (args.length != 7 && args.length != 8) {
      System.err.print("usage: java UNetClient <serverName> <port> <userName> <passwd> ");
      System.err.println("<strategy> <seed> <interval[sec]> [<args1:args2:...>]");
      System.exit(1);
    }
    String serverName = args[0];
    int port = Integer.parseInt(args[1]);
    String userName = args[2];
    String passwd = args[3];
    String strategyName = args[4];
    int seed = Integer.parseInt(args[5]);
    long interval = Long.parseLong(args[6]);
    String paramsStr = "";
    if (args.length == 8) {
      paramsStr = args[7];
    }
    UBaseAgent strategy = UAgentFactory.makeAgent(userName, passwd, strategyName, paramsStr, seed);
    UNetClient client = new UNetClient(serverName, port, strategy, interval);
    client.mainLoop();
  }
}
