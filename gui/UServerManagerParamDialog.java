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
package gui;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import serverCore.*;
import strategyCore.UBaseAgent;

/**
 * �p�����[�^���͗p�_�C�A���O.
 */
public class UServerManagerParamDialog extends JDialog {

  JPanel contentPane;
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel fBasePanel = new JPanel();
  TitledBorder titledBorder1;
  JButton fAgentButton = new JButton();
  JButton fOKButton = new JButton();
  JButton fQuitButton = new JButton();
  protected UParameters fParam = UParameters.getInstance();
  ResourceBundle fRb = UParameters.fRb;
  JCheckBox fLogCheckBox = new JCheckBox();
  JButton fDefaultButton = new JButton();
  boolean fStatus = false;
  UExperimentalSettingPanel fExperimentalSettingPanel = new
      UExperimentalSettingPanel();
  URandomSeedPanel fRandomSeedPanel = new URandomSeedPanel();
  JLabel fPortLabel = new JLabel();
  JTextField fPortTextField = new JTextField();

  public UServerManagerParamDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
  }

  public UServerManagerParamDialog() {
    this(null, UParameters.fRb.getString("SETTING"), true);
    try {
      jbInit();
      pack();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  //�R���|�[�l���g�̏�����
  private void jbInit() throws Exception {
    // �����ݒ�p�l���̐ݒ�
    fExperimentalSettingPanel.setBounds(new Rectangle(31, 3, 277, 186));

    // �����V�[�h�ݒ�p�l���̐ݒ�
    fRandomSeedPanel.setBounds(new Rectangle(56, 191, 224, 109));

    // �G�[�W�F���g�ݒ�{�^���̐ݒ�
    fAgentButton.setBounds(new Rectangle(33, 308, 124, 27));
    fAgentButton.setBorder(BorderFactory.createRaisedBevelBorder());
    fAgentButton.setText(fRb.getString("AGENT_SETTING"));
    fAgentButton.setForeground(Color.black);
    fAgentButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        UAgentDialog jd = new UAgentDialog();
        jd.setLocationRelativeTo(UServerManagerParamDialog.this);
        jd.pack();
        jd.setVisible(true);
      }
    });

    // ���O�쐬CheckBox�̐ݒ�
    fLogCheckBox.setText(fRb.getString("CREATE_LOGS"));
    fLogCheckBox.setBounds(new Rectangle(174, 309, 113, 25));

    // OK�{�^���̐ݒ�
    fOKButton.setBounds(new Rectangle(18, 385, 79, 27));
    fOKButton.setBorder(BorderFactory.createRaisedBevelBorder());
    fOKButton.setText("OK");
    fOKButton.setForeground(Color.black);
    fOKButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (!setResults()) {
          return;
        }
        fStatus = true;
        setVisible(false);
        dispose();
      }
    });

    // ���~�{�^���̐ݒ�
    fQuitButton.setBounds(new Rectangle(119, 385, 79, 27));
    fQuitButton.setBorder(BorderFactory.createRaisedBevelBorder());
    fQuitButton.setText(fRb.getString("QUIT"));
    fQuitButton.setForeground(Color.black);
    fQuitButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fStatus = false;
        setVisible(false);
        dispose();
      }
    });

    // �����l�ݒ�{�^���̐ݒ�
    fDefaultButton.setForeground(Color.black);
    fDefaultButton.setText(fRb.getString("DEFAULT"));
    fDefaultButton.setBorder(BorderFactory.createRaisedBevelBorder());
    fDefaultButton.setBounds(new Rectangle(220, 386, 79, 27));
    fDefaultButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setDefault();
      }
    });

    // Port�ԍ�
    fPortLabel.setText("Port:");
    fPortLabel.setBounds(new Rectangle(80, 345, 34, 23));
    fPortTextField.setText("");
    fPortTextField.setBounds(new Rectangle(128, 345, 93, 22));
    fPortTextField.setText(UParametersNet.getInstance().getPort() + "");
    fPortTextField.setHorizontalAlignment(SwingConstants.RIGHT);

    // BasePanel�̐ݒ�
    fBasePanel.setBorder(BorderFactory.createEtchedBorder());
    fBasePanel.setLayout(null);
    fBasePanel.add(fAgentButton, null);
    fBasePanel.add(fLogCheckBox, null);
    fBasePanel.add(fRandomSeedPanel, null);
    fBasePanel.add(fExperimentalSettingPanel, null);
    fBasePanel.add(fDefaultButton, null);
    fBasePanel.add(fOKButton, null);
    fBasePanel.add(fQuitButton, null);
    fBasePanel.add(fPortTextField, null);
    fBasePanel.add(fPortLabel, null);

    // Dialog�S�̂̐ݒ�
    contentPane = (JPanel)this.getContentPane();
    contentPane.setLayout(borderLayout1);
    contentPane.setPreferredSize(new Dimension(340, 430));
    contentPane.add(fBasePanel, BorderLayout.CENTER);
    this.setModal(true);
    this.setResizable(false);
    this.setTitle(fRb.getString("SETTING"));

    // �f�t�H���g�ɐݒ�
    setDefault();
  }

  /**
   * ServerManagerParamDialog���őI�����ꂽ���e��UParameters�ɔ��f������D
   * @return �G���[���N�������ꍇ�Ftrue, �N����Ȃ������ꍇ:false
   */
  private boolean setResults() {
    String experimentName = fExperimentalSettingPanel.getSelectedExperimentName();
    UTimeSeriesDefinitions timeSeriesDefinitions = fExperimentalSettingPanel.
        getTimeSeriesDefinition();
    HashMap timeSeriesDef = timeSeriesDefinitions.getDefinition(experimentName);
    fParam.setBrand( (String) timeSeriesDef.get(UTimeSeriesDefinitions.
                                                STRING_BRAND_NAME));
    fParam.setPriceFile( (String) timeSeriesDef.get(UTimeSeriesDefinitions.
        STRING_FILENAME));
    if (!readPriceInfoDB(fParam.getPriceFile(), fParam.getPriceInfoDB())) {
      return false;
    }
    fParam.setDays(fExperimentalSettingPanel.getDays());
    fParam.setStartPoint(fExperimentalSettingPanel.getStartPoint());
    fParam.setBoardPerDay(fExperimentalSettingPanel.getBoardPerDay());
    fParam.setLogCreate(fLogCheckBox.isSelected());
    fParam.setSeed(fRandomSeedPanel.getSeed());
    UParametersNet.getInstance().setPort(Integer.parseInt(fPortTextField.
        getText()));
    if (!setupMachineAgentArray(fParam.getMemberLog(),
                                fParam.getMachineAgentArray())) {
      return false;
    }
    return true;
  }

  /**
   * �^����ꂽmembers�ɓo�^����Ă���}�V���G�[�W�F���g��񂩂�}�V���G�[�W�F���g�𐶐����CmachineAgentArray�ɓo�^����D
   * @param members ������
   * @param machineAgentArray �}�V���G�[�W�F���g�̓o�^��
   * @return �S�Ẵ}�V���G�[�W�F���g�̐����ɐ����Ftrue, ���s�Ffalse
   */
  private boolean setupMachineAgentArray(UMemberList members,
                                         ArrayList machineAgentArray) {
    machineAgentArray.clear();
    Iterator iter = members.getMembers();
    while (iter.hasNext()) {
      HashMap memberInfo = (HashMap) iter.next();
      String loginName = (String) memberInfo.get(UMemberList.STRING_LOGIN_NAME);
      String passwd = (String) memberInfo.get(UMemberList.STRING_PASSWORD);
      String attribute = (String) memberInfo.get(UMemberList.STRING_ATTRIBUTE);
      String connection = (String) memberInfo.get(UMemberList.STRING_CONNECTION);
      String accessString = UMemberList.arrayListToString( (ArrayList)
          memberInfo.get(UMemberList.ARRAY_LIST_ACCESS));
      String realName = (String) memberInfo.get(UMemberList.STRING_REAL_NAME);
      String paramString = UMemberList.arrayListToString( (ArrayList) memberInfo.
          get(UMemberList.ARRAY_LIST_SYSTEM_PARAMETERS));
      int seed = ( (Integer) memberInfo.get(UMemberList.INT_SEED)).intValue();
      if (attribute.equals("Machine") && connection.equals("Local")) {
        try {
          UByteArrayOutputStream os = new UByteArrayOutputStream();
          os.setName(loginName);
          UBaseAgent strategy = UAgentFactory.makeAgent(loginName, passwd,
              realName, paramString, seed, os);
          machineAgentArray.add(strategy);
        } catch (IllegalArgumentException iae) {
          JOptionPane.showMessageDialog(null,
                                        fRb.getString("ERROR_CANNOT_INITIALIZE_AGENT") +
                                        loginName,
                                        fRb.getString("ERROR_DIALOG_TITLE"),
                                        JOptionPane.ERROR_MESSAGE);
          return false;
        }
      }
    }
    return true;
  }

  /**
   * ���i���t�@�C����ǂݍ���
   * @param filename ���i���t�@�C����
   * @throws java.lang.Exception
   */
  private boolean readPriceInfoDB(String filename, UPriceInfoDB priceInfoDB) {
    try {
      StringTokenizer st = new StringTokenizer(filename, ":");
      String type = st.nextToken();
      String name = st.nextToken();
      if (type.equals("resource")) {
        URL dataURL = getClass().getResource("/resources/csv/" + name);
        BufferedReader br = new BufferedReader(new InputStreamReader(dataURL.
            openStream()));
        priceInfoDB.readFrom(br);
        br.close();
      } else {
        BufferedReader br = new BufferedReader(new FileReader(name));
        priceInfoDB.readFrom(br);
        br.close();
      }
    } catch (NoSuchElementException nse) {
      JOptionPane.showMessageDialog(null,
                                    fRb.getString(
          "ERROR_INVALID_PRICE_FILE_NAME") + filename,
                                    fRb.getString("ERROR_DIALOG_TITLE"),
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    } catch (FileNotFoundException fnfe) {
      JOptionPane.showMessageDialog(null,
                                    fRb.getString("ERROR_FILE_NOT_FOUND") +
                                    fnfe.getMessage(),
                                    fRb.getString("ERROR_DIALOG_TITLE"),
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    } catch (IOException ioe) {
      JOptionPane.showMessageDialog(null, fRb.getString("ERROR_IO"),
                                    fRb.getString("ERROR_DIALOG_TITLE"),
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    } catch (ParseException pe) {
      JOptionPane.showMessageDialog(null,
                                    fRb.getString("ERROR_FILE_FORMAT") +
                                    pe.getErrorOffset(),
                                    fRb.getString("ERROR_DIALOG_TITLE"),
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  public boolean getStatus() {
    return fStatus;
  }

  public void setDefault() {
    fExperimentalSettingPanel.setDefault();
    fRandomSeedPanel.setDefault();
    try {
      URL membersURL = getClass().getResource(UParameters.MEMBERS_NETWORK);
      BufferedReader br = new BufferedReader(new InputStreamReader(membersURL.
          openStream()));
      fParam.getMemberLog().readFrom(br);
      br.close();
    } catch (Exception ex) {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      ex.printStackTrace(pw);
      JOptionPane.showMessageDialog(null,
                                    fRb.getString("ERROR_FATAL") + "\n" +
                                    sw.toString(),
                                    fRb.getString("ERROR_DIALOG_TITLE"),
                                    JOptionPane.ERROR_MESSAGE);
      System.exit(5);
    }
    fLogCheckBox.setSelected(true);
  }

  //�E�B���h�E������ꂽ�Ƃ��ɏI������悤�ɃI�[�o�[���C�h
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      setVisible(false);
      dispose();
    }
  }

  public static void main(String[] args) {
    new UServerManagerParamDialog();
  }
}
