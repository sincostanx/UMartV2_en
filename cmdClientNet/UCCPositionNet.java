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
 * Network版のクライアント上で実行される，ポジション照会のためのSVMPコマンドクラスです．
 */
public class UCCPositionNet extends UCPositionCore implements IClientCmdNet {

  /** サーバーからの入力ストリーム */
  private BufferedReader fIn;

  /** サーバーへの出力ストリーム */
  private PrintWriter fOut;

  /**
   * コンストラクタです．
   */
  public UCCPositionNet() {
    super();
    fIn = null;
    fOut = null;
  }

  /**
   * @see cmdCore.ICommand#doIt()
   */
  public UCommandStatus doIt() {
    try {
      fPositionInfo.clear();
      fOut.println(CMD_NAME);
      StringTokenizer st = new StringTokenizer(fIn.readLine());
      String token = st.nextToken();
      if (token.equals("+ACCEPT")) {
        fStatus.setStatus(true);
        StringTokenizer hst = new StringTokenizer(fIn.readLine());
        fPositionInfo.put(LONG_TODAY_SELL, Long.valueOf(hst.nextToken()));
        fPositionInfo.put(LONG_TODAY_BUY, Long.valueOf(hst.nextToken()));
        hst = new StringTokenizer(fIn.readLine());
        fPositionInfo.put(LONG_YESTERDAY_SELL, Long.valueOf(hst.nextToken()));
        fPositionInfo.put(LONG_YESTERDAY_BUY, Long.valueOf(hst.nextToken()));
      } else if (token.equals("+ERROR")) {
        fStatus.setStatus(false);
        int errCode = Integer.parseInt(st.nextToken());
        fStatus.setErrorCode(errCode);
        fStatus.setErrorMessage(fIn.readLine());
      } else {
        System.err.println("Unexpected token in UCCPositionNet: " + token);
        System.exit(5);
      }
      String okMsg = fIn.readLine();
      if (!okMsg.equals("+OK")) {
        System.err.println("Unexpected token in UCCPositionNet: " + okMsg);
        System.exit(5);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(5);
    }
    return fStatus;
  }

  /**
   * @see cmdClientNet.IClientCmdNet#setConnection(BufferedReader, PrintWriter)
   */
  public void setConnection(BufferedReader br, PrintWriter pw) {
    fIn = br;
    fOut = pw;
  }
}
