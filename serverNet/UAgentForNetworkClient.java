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
package serverNet;

import java.io.*;
import java.net.*;
import java.util.*;

import cmdCore.*;
import cmdServer.*;
import serverCore.*;

/**
 * �l�b�g���[�N�z���̃N���C�A���g�ƈ�Έ�̒ʐM���s��, �K���ȃR�}���h�I�u�W�F�N�g�����s����N���X�ł��D
 */
public class UAgentForNetworkClient implements Runnable {

  /** �N���C�A���g�Ƃ̒ʐM�p�\�P�b�g */
  private Socket fSocket;

  /** �N���C�A���g�Ƃ̒ʐM�p�̏o�̓X�g���[�� */
  private PrintWriter fOut;

  /** �N���C�A���g�Ƃ̒ʐM�p�̓��̓X�g���[�� */
  private BufferedReader fIn;

  /** �X���b�h�I�u�W�F�N�g */
  private Thread fThread;

  /** �v���g�R���I�u�W�F�N�g */
  private UProtocolForServer fProtocol;

  /** ���̃G�[�W�F���g�̃��O�C����� */
  private ULoginStatus fLoginStatus;

  /** U-Mart�I�u�W�F�N�g�ւ̎Q�� */
  private UMartNetwork fUMartNetwork;

  /** �X���b�h�̏I���t���O */
  private boolean fDoneFlag;

  /**
   * �R���X�g���N�^�ł��D
   * �K�v�ȃR�}���h�I�u�W�F�N�g�̓o�^, �X���b�h�̐������s���Ă��܂��D
   * @param s �\�P�b�g
   * @param umart �l�b�g���[�N��UMart�I�u�W�F�N�g
   */
  public UAgentForNetworkClient(Socket s, UMartNetwork umart) {
    try {
      fSocket = s;
      fSocket.setTcpNoDelay(true);
      fProtocol = new UProtocolForServer();
      fProtocol.setConnection(this, umart);
      fLoginStatus = null;
      fUMartNetwork = umart;
      OutputStream os = s.getOutputStream();
      fOut = new PrintWriter(os);
      InputStream is = s.getInputStream();
      fIn = new BufferedReader(new InputStreamReader(is));
      fThread = new Thread(this);
      fThread.start();
    } catch (IOException e) {
      System.out.println("Exception: " + e + " in UAgent");
      System.exit(5);
    }
  }

  /**
   * ���O�C����Ԃ�Ԃ��܂��D
   * @return ���O�C�����
   */
  public ULoginStatus getLoginStatus() {
    return fLoginStatus;
  }

  /**
   * �����Ŏw�肳�ꂽ������s���N���C�A���g�֑���܂��D
   * @param s �]��������
   */
  public void sendMessage(String s) {
    fOut.println(s);
  }

  /**
   * �N���C�A���g�ւ̏o�̓X�g���[�����t���b�V�����܂��D
   *
   */
  public void flushMessage() {
    fOut.flush();
  }

  /**
   * ���O�A�E�g�������s���B��̓I�ɂ�, �X�g���[������у\�P�b�g�����,
   * �I���t���O�𗧂Ă܂��D
   */
  public void logout() {
    try {
      fDoneFlag = true;
      fOut.close();
      fIn.close();
      fSocket.close();
      fSocket = null;
      fThread = null;
      System.out.println("Logout: ID=" + fLoginStatus.getUserID());
    } catch (Exception e) {
      sendMessage("+ERROR");
      flushMessage();
    }
  }

  /**
   * ���O�C���������s���܂��D
   * @param st ���̓X�g���[��
   * @return
   */
  private boolean handleLogin(StringTokenizer st) {
    try {
      String name = "NotInitialized";
      String passwd = "NotInitialized";
      name = st.nextToken();
      passwd = st.nextToken();
      fLoginStatus = fUMartNetwork.doLogin(name, passwd);
      if (fLoginStatus != null) {
        System.out.println("Login Success: " + name);
        sendMessage("+ACCEPT");
        flushMessage();
        return true;
      } else {
        System.err.println("Login Failiar: " + name);
        UServerStatus status = fUMartNetwork.getStatus();
        if (status.getState() == UServerStatus.SU_LOGIN) {
          sendMessage("+ERROR " + ICommand.UNACCEPTABLE_COMMAND);
          sendMessage("MARKET STATE IS " + status.getStateString());
        } else {
          sendMessage("+ERROR " + ICommand.INVALID_ARGUMENTS);
          sendMessage("USAGE: LOGIN <MEMBERID> <PASSWD> [<NICKNAME> <CLASSNAME>  <SEED> [<PARAM1:PARAM2:...>]]");
        }
        flushMessage();
        return false;
      }
    } catch (NoSuchElementException nsee) {
      nsee.printStackTrace();
      sendMessage("+ERROR " + ICommand.INVALID_ARGUMENTS);
      sendMessage("USAGE: LOGIN <MEMBERID> <PASSWD> [<NICKNAME> <CLASSNAME> <SEED> [<PARAM1:PARAM2:...>]]");
      flushMessage();
      return false;
    } catch (Exception e) {
      System.out.println("Exception: " + e + " in UAgent.handleLogin");
      System.exit(5);
      return false;
    }
  }

  /**
   * �ڑ��������s���܂��D
   * @return true:�����Cfalse�F���s
   */
  private boolean handleConnection() {
    try {
      sendMessage("+LOGIN SVMP(0.01)");
      while (true) {
        flushMessage();
        StringTokenizer st = new StringTokenizer(fIn.readLine());
        if (!st.hasMoreTokens()) {
          sendMessage("+ERROR " + ICommand.INVALID_COMMAND);
          sendMessage("USAGE: LOGIN <MEMBERID> <PASSWD> [<NICKNAME> <CLASSNAME> <SEED> [<PARAM1:PARAM2:...>]]");
          flushMessage();
          continue;
        }
        String cmd = st.nextToken();
        if (cmd.equalsIgnoreCase(UCLoginCore.CMD_NAME) ||
            cmd.equals(UCLoginCore.CMD_ALIAS)) {
          if (handleLogin(st)) {
            return true;
          } else {
            continue;
          }
        } else if (cmd.equalsIgnoreCase(UCLogoutCore.CMD_NAME) ||
                   cmd.equals(UCLogoutCore.CMD_ALIAS)) {
          System.err.println("Logout command is performed.");
          return false;
        } else {
          sendMessage("+ERROR " + ICommand.INVALID_COMMAND);
          sendMessage("USAGE: LOGIN <MEMBERID> <PASSWD> [<NICKNAME> <CLASSNAME> <SEED> [<PARAM1:PARAM2:...>]]");
          flushMessage();
          continue;
        }
      }
    } catch (NullPointerException ne) {
      System.out.println("Connection seems to be closed by peer ...");
      return false;
    } catch (Exception e) {
      System.out.println("Exception: " + e + " in UAgent.handleConnection");
      return false;
    }
  }

  /**
   * �N���C�A���g���烁�b�Z�[�W���󂯕t����, �K���ȃR�}���h�I�u�W�F�N�g��
   * ���s���郁�C�����[�v�ł��D
   */
  public void run() {
    try {
      if (!handleConnection()) {
        fIn.close();
        fOut.close();
        fSocket.close();
        return;
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(5);
    }
    try {
      fDoneFlag = false;
      fLoginStatus.incrementNoOfLoginAgents();
      while (!fDoneFlag) {
        sendMessage("+OK");
        flushMessage();
        String line = fIn.readLine();
        StringTokenizer st = new StringTokenizer(line);
        if (!st.hasMoreTokens()) {
          sendMessage("+ERROR " + ICommand.INVALID_COMMAND);
          sendMessage("UNKNOWN COMMAND");
          flushMessage();
          continue;
        }
        String str = st.nextToken();
        ICommand c = fProtocol.getCommand(str);
        if (c != null) {
          c.setArguments(st);
          c.doIt();
        } else {
          System.out.println("Unknown command: " + line);
          sendMessage("+ERROR " + ICommand.INVALID_COMMAND);
          sendMessage("UNKNOWN COMMAND");
          flushMessage();
        }
      }
    } catch (NullPointerException ne) {
      System.out.println("Logout : ID=" + fLoginStatus.getUserID());
    } catch (SocketException se) {
      System.err.println("socket exception");
      System.out.println("Logout : ID=" + fLoginStatus.getUserID());
    } catch (Exception e) {
      System.err.println(e);
    } finally {
      fLoginStatus.decrementNoOfLoginAgents();
    }
  }
}
