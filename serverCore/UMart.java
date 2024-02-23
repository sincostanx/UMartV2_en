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
import java.net.*;
import java.text.*;
import java.util.*;


import cmdClientSA.*;
import cmdCore.*;
import log.*;
import strategy.*;
import strategyCore.UBaseAgent;

/**
 * ������S�̂̊Ǘ����s���N���X�ł��D
 * �l�b�g���[�N������уX�^���h�A�������ɋ��ʂ̑�������у��\�b�h����`����Ă��܂��D
 * �l�b�g���[�N���ŗ��p����UMartNetwork�N���X�C����сC�X�^���h�A�������ŗ��p����UMartStandAlone�N���X�̐e�N���X�ƂȂ��Ă��܂��D
 *
 * ���s�������󂯕t������悤��doOrderReqest���\�b�h���C�������D2020/07/08 by isao
 */
public abstract class UMart {
	
	/** ���߂̉��i�ɑ΂���ő�̕ϓ��䗦 */
	public static double MAX_RATE = 0.2;

  /** �X�[�p�[���[�U�[��ID */
  public static final int SU_ID = 0;

  /** �T�[�o�[�^�p�����̃f�t�H���g�l */
  public static final int DEFAULT_MAX_DATE = 30;

  /** ���������̐ߐ��̃f�t�H���g�l */
  public static final int DEFAULT_NO_OF_SESSIONS_PER_DAY = 8;

  /** �P�ʎ�����������̎萔���̃f�t�H���g�l */
  public static final long DEFAULT_FEE_PER_UNIT = 0;

  /** �P�ʎ�����������̏؋����̃f�t�H���g�l */
  public static final long DEFAULT_MARGIN_RATE = 300000;

  /** �ؓ�����x�z�̃f�t�H���g�l */
  public static final long DEFAULT_MAX_LOAN = 30000000;

  /** �ؓ�������̃f�t�H���g�l */
  public static final double DEFAULT_INTEREST = 0.1;

  /** ����P�ʂ̃f�t�H���g�l */
  public static final long DEFAULT_TRADING_UNIT = 1000;

  /** �������Y�̃f�t�H���g�l */
  public static final long DEFAULT_INITIAL_CASH = 1000000000;

  /** �������i�n��t�@�C�����̃f�t�H���g�l */
  public static final String DEFAULT_SPOT_PRICE_FILE = "j30";

  /** �������i�f�[�^�̍X�V�p�x(�񂹉���)�̃f�t�H���g�l */
  public static final int DEFAULT_SPOT_INTERVAL = 1;

  /** �����˗��E����R�}���h�̃��O��ۑ�����f�B���N�g���� */
  public final static String ORDER_LOG_DIR = "order";

  /** �����̃��O��ۑ�����f�B���N�g���� */
  public final static String EXECUTION_LOG_DIR = "execution";

  /** ���̃��O��ۑ�����f�B���N�g���� */
  public final static String BOARD_LOG_DIR = "order_book";

  /** �������̃��O��ۑ�����f�B���N�g���� */
  public final static String ACCOUNT_LOG_DIR = "account";

  /** �N���C�A���g�̃��O�C���Ǘ� */
  protected ULoginManager fLoginManager;

  /** �����Ǘ� */
  protected UOrderManager fOrderManager;

  /** �T�[�o�[��� */
  protected UServerStatus fStatus;

  /** �T�[�o�[��ԗpRead-Write Lock */
  protected UReadWriteLock fStateLock;

  /** ���i�n��f�[�^�x�[�X */
  protected UPriceInfoDB fPriceInfoDB;

  /** ���i�n��f�[�^�x�[�X�ɂ�����J�n�|�C���g */
  protected int fStartPoint;

  /** �T�[�o�[�^�p���� */
  protected int fMaxDate = UMart.DEFAULT_MAX_DATE;

  /** 1��������̔񂹉� */
  protected int fNoOfSessionsPerDay = UMart.DEFAULT_NO_OF_SESSIONS_PER_DAY;

  /** �����Ǘ� */
  protected UAccountManager fAccountManager;

  /** �� */
  protected UBoard fBoard;

  /** ���i���ڃf�[�^�x�[�X */
  protected UQuoteDB fQuoteDB;

  /** ��� */
  protected UBoardInformation fBoardInformation;

  /** �G�[�W�F���g(UBaseStrategy)�̔z�� */
  protected ArrayList fStrategyArray;

  /** 1�ߕ��̒����˗��E����̗��� */
  protected UOrderCommandLog fOrderCommandLog;

  /** ������ */
  protected UMemberList fMemberLog;

  /** ���O�t�@�C���𐶐�����f�B���N�g���� */
  protected String fLogDir;

  /** ���O�𐶐����Ȃ� */
  public static final int NO_LOG = 0;

  /** �ڍ׃��O�𐶐����� */
  public static final int DETAILED_LOG = 1;

  /** �ȈՃ��O�i���������C���̗����C��藚���C�����������ȗ��j�𐶐����� */
  public static final int SIMPLE_LOG = 2;

  /** ���O�̐������@(NO_LOG or DETAILED_LOG or SIMPLE_LOG)�̎w�� */
  protected int fLogFlag;

  /** �R�}���h���s�\�`�F�b�N�p�I�u�W�F�N�g */
  protected UCmdExecutableChecker fCmdExecutableChecker;

  /**
   * UMart�I�u�W�F�N�g�̐�������я��������s���܂��F<br>
   * - �G�[�W�F���g�͒N�����O�C�����Ă��Ȃ����<br>
   * - ���t��1�C�񂹉񐔂�1<br>
   * - ��Ԃ�BEFORE_TRADING<br>
   * @param members ������X�g
   * @param priceInfoDB ���i�n��f�[�^�x�[�X
   * @param startPoint ���i�n��̉��Ԗڂ̃f�[�^�������̃f�[�^�Ƃ��邩
   * @param seed �����̎�
   * @param maxDate �T�[�o�[�^�p����
   * @param noOfSessionsPerDay ���������̐ߐ�
   */
  public UMart(UMemberList members, UPriceInfoDB priceInfoDB, int startPoint,
               long seed, int maxDate, int noOfSessionsPerDay) {
    fPriceInfoDB = priceInfoDB;
    fStartPoint = startPoint + Strategy.NO_OF_SPOT_PRICES - 1;
    URandom.setSeed(seed);
    fMaxDate = maxDate;
    fNoOfSessionsPerDay = noOfSessionsPerDay;
    // fInterest = interest / 365.0;
    fAccountManager = new UAccountManager(members, fNoOfSessionsPerDay);
    fBoard = new UBoard();
    fQuoteDB = new UQuoteDB();
    fBoardInformation = new UBoardInformation();
    fOrderManager = new UOrderManager();
    setupOrderManager();
    fPriceInfoDB.initializePtr(fStartPoint, fMaxDate, fNoOfSessionsPerDay);
    fStatus = new UServerStatus();
    fStateLock = new UReadWriteLock();
    fStrategyArray = new ArrayList();
    fOrderCommandLog = new UOrderCommandLog();
    fStatus.setState(UServerStatus.BEFORE_TRADING);
    setupLoginManager();
    fMemberLog = members;
    fLogFlag = UMart.NO_LOG;
    try {
      fCmdExecutableChecker = new UCmdExecutableChecker();
      URL dataURL = getClass().getResource("/cmdCore/SVMP.csv");
      BufferedReader br = new BufferedReader(new InputStreamReader(dataURL.openStream()));
      fCmdExecutableChecker.readFrom(br);
      br.close();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(5);
    }
  }

  /**
   * ���[�J���z�X�g�œ��삷��G�[�W�F���g��o�^���܂��D
   * @param agent UBaseStrategy���p�������G�[�W�F���g
   */
  public boolean appendStrategy(UBaseAgent agent) {
    String name = agent.getLoginName();
    String passwd = agent.getPasswd();
    ULoginStatus loginStatus = doLogin(name, passwd);
    if (loginStatus == null) {
      System.err.println("Can't find the office with (" + name + "," + passwd +
                         ")");
      return false;
    }
    if (loginStatus.getNoOfLoginAgents() > 0) {
      System.err.println("The agent has already logined!!");
      return false;
    }
    int id = loginStatus.getUserID();
    UProtocolForLocalClient umcp = new UProtocolForLocalClient();
    umcp.setConnection(this, id);
    agent.setCProtocol(umcp);
    agent.setUserID(id);
    fStrategyArray.add(agent);
    loginStatus.incrementNoOfLoginAgents();
    return true;
  }

  /**
   * ������̌��������n�b�V���}�b�v���Ɋi�[���܂��D
   * @param hash �e�����i�[���邽�߂̃n�b�V���}�b�v
   * @param ea ������̌���
   */
  private void setExchangeBalanceToHashMap(HashMap hash, UExchangeAccount ea) {
    hash.put(UCAllBalancesCore.STRING_NAME, "su");
    hash.put(UCAllBalancesCore.LONG_CASH, new Long(ea.getCash()));
    hash.put(UCAllBalancesCore.LONG_MARGIN, "0");
    hash.put(UCAllBalancesCore.LONG_UNREALIZED_PROFIT, "0");
    hash.put(UCAllBalancesCore.LONG_SETTLED_PROFIT, "0");
    hash.put(UCAllBalancesCore.LONG_FEE, "0");
    hash.put(UCAllBalancesCore.LONG_INTEREST, "0");
    hash.put(UCAllBalancesCore.LONG_LOAN, "0");
    hash.put(UCAllBalancesCore.LONG_SURPLUS, "0");
    hash.put(UCAllBalancesCore.LONG_SELL_POSITION, new Long(ea.getSellPosition()));
    hash.put(UCAllBalancesCore.LONG_BUY_POSITION, new Long(ea.getBuyPosition()));
  }

  /**
   * UBalance�̏����n�b�V���}�b�v�Ɋi�[���܂��D
   * @param hash �e�����i�[���邽�߂̃n�b�V���}�b�v
   * @param balance ���x���
   */
  private void setBalanceToHashMap(HashMap hash, UBalance balance) {
    balance.updateCash();
    hash.put(UCBalancesCore.LONG_CASH,
             new Long(balance.getCash() + balance.getMargin()));
    hash.put(UCBalancesCore.LONG_MARGIN, new Long(balance.getMargin()));
    hash.put(UCBalancesCore.LONG_UNREALIZED_PROFIT,
             new Long(balance.getUnrealizedProfit()));
    hash.put(UCBalancesCore.LONG_SETTLED_PROFIT, new Long(balance.getProfit()));
    hash.put(UCBalancesCore.LONG_FEE, new Long(balance.getSumOfFee()));
    hash.put(UCBalancesCore.LONG_INTEREST, new Long(balance.getSumOfInterest()));
    hash.put(UCBalancesCore.LONG_LOAN, new Long(balance.getLoan()));
    hash.put(UCBalancesCore.LONG_SURPLUS, new Long(balance.getCash()));
  }

  /**
   * Balances�R�}���h�ւ̉������������܂��D
   * ������t���ԑ�, ����㎞�ԑ�, ���ό㎞�ԑшȊO�̓G���[��Ԃ��܂��D
   * @param todayHash �������̎c���Ɖ���
   * @param yesterdayHash �O�����̎c���Ɖ���
   * @param userID ���[�UID
   */
  public UCommandStatus doBalances(HashMap todayHash,
                                   HashMap yesterdayHash, int userID) {
    UCommandStatus ucs = new UCommandStatus();
    try {
      fStateLock.readLock();
      if (!fCmdExecutableChecker.isExecutable(UCBalancesCore.CMD_NAME, fStatus,
                                              userID)) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.UNACCEPTABLE_COMMAND);
        ucs.setErrorMessage(fCmdExecutableChecker.getErrorMessage());
        return ucs;
      }
      ucs.setStatus(true);
      UAccount account = fAccountManager.getAccount(userID);
      if (account == null) {
        System.err.println("Account not found in UMart.doBalances: UserID=" +
                           userID);
        throw new Exception();
        // System.exit( -5);
        // return ucs;
      }
      setBalanceToHashMap(todayHash, account.getTodayBalance());
      setBalanceToHashMap(yesterdayHash, account.getYesterdayBalance());
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Exception: " + e + " in UMart.doBalances");
      System.exit(5);
    } finally {
      fStateLock.readUnlock();
      return ucs;
    }
  }

  /**
   * AllBalances�R�}���h�ւ̉������������܂��D
   * @param arrayList �n�b�V���}�b�v���i�[���邽�߂̃��X�g
   * @param userID ���[�UID
   */
  public UCommandStatus doAllBalances(ArrayList arrayList, int userID) {
    UCommandStatus ucs = new UCommandStatus();
    try {
      fStateLock.readLock();
      if (!fCmdExecutableChecker.isExecutable(UCAllBalancesCore.CMD_NAME,
                                              fStatus, userID)) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.UNACCEPTABLE_COMMAND);
        ucs.setErrorMessage(fCmdExecutableChecker.getErrorMessage());
        return ucs;
      }
      ucs.setStatus(true);
      UExchangeAccount ea = fAccountManager.getExchangeAccount();
      HashMap hash = new HashMap();
      setExchangeBalanceToHashMap(hash, ea);
      arrayList.add(hash);
      Enumeration accounts = fAccountManager.getAccounts();
      while (accounts.hasMoreElements()) {
        hash = new HashMap();
        UAccount account = (UAccount) accounts.nextElement();
        hash.put(UCAllBalancesCore.STRING_NAME, account.getUserName());
        setBalanceToHashMap(hash, account.getTodayBalance());
        UPosition pos = account.getPosition();
        long sell = pos.getSumOfSellPositionUntilYesterday();
        long buy = pos.getSumOfBuyPositionUntilYesterday();
        hash.put(UCAllBalancesCore.LONG_SELL_POSITION, new Long(sell));
        hash.put(UCAllBalancesCore.LONG_BUY_POSITION, new Long(buy));
        arrayList.add(hash);
      }
    } catch (Exception e) {
      System.err.println("Exception: " + e + " in UMart.doAllBalances");
      System.exit(5);
    } finally {
      fStateLock.readUnlock();
      return ucs;
    }
  }

  /**
   * AllPositions�R�}���h�ւ̉������������܂��D
   * ������t���ԑ�, ����㎞�ԑ�, ���ό㎞�ԑшȊO�̓G���[��Ԃ��܂��D
   * @param arrayList �n�b�V���}�b�v���i�[���邽�߂̃��X�g
   * @param userID ���[�UID
   */
  public UCommandStatus doAllPositions(ArrayList arrayList, int userID) {
    UCommandStatus ucs = new UCommandStatus();
    try {
      fStateLock.readLock();
      if (!fCmdExecutableChecker.isExecutable(UCAllPositionsCore.CMD_NAME,
                                              fStatus, userID)) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.UNACCEPTABLE_COMMAND);
        ucs.setErrorMessage(fCmdExecutableChecker.getErrorMessage());
        return ucs;
      }
      ucs.setStatus(true);
      {
        HashMap hashOfSu = new HashMap();
        UExchangeAccount ea = fAccountManager.getExchangeAccount();
        String name = ea.getUserName();
        hashOfSu.put(UCAllPositionsCore.STRING_NAME, name);
        long sell = ea.getSellPosition();
        hashOfSu.put(UCAllPositionsCore.LONG_SELL_POSITION, new Long(sell));
        long buy = ea.getBuyPosition();
        hashOfSu.put(UCAllPositionsCore.LONG_BUY_POSITION, new Long(buy));
        arrayList.add(hashOfSu);
      }
      Enumeration accounts = fAccountManager.getAccounts();
      while (accounts.hasMoreElements()) {
        UAccount account = (UAccount) accounts.nextElement();
        HashMap hash = new HashMap();
        hash.put(UCAllPositionsCore.STRING_NAME, account.getUserName());
        UPosition pos = account.getPosition();
        long sell = pos.getSumOfTodaySellPosition();
        long buy = pos.getSumOfTodayBuyPosition();
        hash.put(UCAllPositionsCore.LONG_SELL_POSITION, new Long(sell));
        hash.put(UCAllPositionsCore.LONG_BUY_POSITION, new Long(buy));
        arrayList.add(hash);
      }
    } catch (Exception e) {
      System.err.println("Exception: " + e + " in UMart.doAllPositions");
      System.exit(5);
    } finally {
      fStateLock.readUnlock();
      return ucs;
    }
  }

  /**
   * ��ʃ��[�U�[�̎��x����hash�Ɋi�[���܂��DUMart.doMemberProfile����Ă΂�܂��D
   * @param hash ��ʃ��[�U�[�̎��x�����i�[���邽�߂�HashMap
   * @param bal ��ʃ��[�U�[�̎��x���
   */
  private void setBalanceToHashMapForMemberProfile(HashMap hash, UBalance bal) {
    long initialCash = bal.getInitialCash();
    hash.put(UCMemberProfileCore.LONG_INITIAL_CASH, new Long(initialCash)); // ����������
    long loan = bal.getLoan();
    hash.put(UCMemberProfileCore.LONG_LOAN, new Long(loan)); // �ؓ���
    long unrealizedProfit = bal.getUnrealizedProfit();
    hash.put(UCMemberProfileCore.LONG_UNREALIZED_PROFIT,
             new Long(unrealizedProfit)); // ���������v
    long margin = bal.getMargin();
    hash.put(UCMemberProfileCore.LONG_MARGIN, new Long(margin)); // �a���؋���
    long sumOfFee = bal.getSumOfFee();
    hash.put(UCMemberProfileCore.LONG_SUM_OF_FEE, new Long(sumOfFee)); // ���x�����萔��
    long sumOfInterest = bal.getSumOfInterest();
    hash.put(UCMemberProfileCore.LONG_SUM_OF_INTEREST, new Long(sumOfInterest)); // ���x��������
    long cash = bal.getCash();
    hash.put(UCMemberProfileCore.LONG_CASH, new Long(cash)); // �ۗL����
    long profit = bal.getProfit();
    hash.put(UCMemberProfileCore.LONG_PROFIT, new Long(profit)); // �������v
  }

  /**
   * ��ʃ��[�U�[�̃|�W�V��������hash�Ɋi�[���܂��DUMart.doMemberProfile����Ă΂�܂��D
   * @param hash ��ʃ��[�U�[�̃|�W�V���������i�[���邽�߂�HashMap
   * @param pos ��ʃ��[�U�[�̃|�W�V�������
   */
  private void setPositionToHashMapForMemberProfile(HashMap hash, UPosition pos) {
    long sumOfSellPositionsUntilYesterday = pos.
        getSumOfSellPositionUntilYesterday();
    hash.put(UCMemberProfileCore.LONG_SUM_OF_SELL_POSITIONS_UNTIL_YESTERDAY,
             new Long(sumOfSellPositionsUntilYesterday)); // �O���܂ł̔��|�W�V�����̍��v
    long sumOfBuyPositionsUntilYesterday = pos.
        getSumOfBuyPositionUntilYesterday();
    hash.put(UCMemberProfileCore.LONG_SUM_OF_BUY_POSITIONS_UNTIL_YESTERDAY,
             new Long(sumOfBuyPositionsUntilYesterday)); // �O���܂ł̔��|�W�V�����̍��v
    long todaySellPositions = pos.getSumOfTodaySellPosition();
    hash.put(UCMemberProfileCore.LONG_TODAY_SELL_POSITIONS,
             new Long(todaySellPositions)); // �����̔��|�W�V�����̍��v
    long todayBuyPositions = pos.getSumOfTodayBuyPosition();
    hash.put(UCMemberProfileCore.LONG_TODAY_BUY_POSITIONS,
             new Long(todayBuyPositions)); // �����̔��|�W�V�����̍��v
  }

  /**
   * MemberProfile�R�}���h�ւ̉������������܂��D
   * @param data targetUserID�Ŏw�肳�ꂽ���[�U�[�̏����i�[���邽�߂�HashMap
   * @param userID ���̃R�}���h�����s�������[�U�[�̃��[�U�[ID
   * @param targetUserId ���ׂ������[�U�[�̃��[�U�[ID�i-1�̏ꍇ�CuserID����������j
   * @return ���s���
   */
  public UCommandStatus doMemberProfile(HashMap data, int userID,
                                        int targetUserId) {
    UCommandStatus ucs = new UCommandStatus();
    try {
      fStateLock.readLock();
      if (!fCmdExecutableChecker.isExecutable(UCMemberProfileCore.CMD_NAME,
                                              fStatus, userID)) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.UNACCEPTABLE_COMMAND);
        ucs.setErrorMessage(fCmdExecutableChecker.getErrorMessage());
        return ucs;
      }
      if (targetUserId < 0) {
        targetUserId = userID;
      }
      UAccount account = fAccountManager.getAccount(targetUserId);
      ULoginStatus office = fLoginManager.findLoginStatus(targetUserId);
      if (account == null || office == null) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.INVALID_ARGUMENTS);
        ucs.setErrorMessage("TARGET_USER_ID(=" + targetUserId +
                            ") IS OUT OF RANGE.");
        return ucs;
      }
      data.put(UCMemberProfileCore.STRING_LOGIN_NAME, account.getUserName()); // �����
      data.put(UCMemberProfileCore.STRING_PASSWORD, account.getPasswd()); // �p�X���[�h
      data.put(UCMemberProfileCore.STRING_ATTRIBUTE, account.getAttribute()); // �G�[�W�F���g����(Human or Machine)
      data.put(UCMemberProfileCore.STRING_CONNECTION, account.getConnection()); // �R�l�N�V����(Remote or Local)
      data.put(UCMemberProfileCore.ARRAY_LIST_ACCESS, account.getAccess()); // �A�N�Z�X����
      data.put(UCMemberProfileCore.STRING_REAL_NAME, account.getRealName()); // ���ۂ̖��O
      data.put(UCMemberProfileCore.ARRAY_LIST_SYSTEM_PARAMETERS,
               account.getSystemParameters()); // �V�X�e���p�����[�^
      data.put(UCMemberProfileCore.INT_SEED, new Integer(account.getSeed())); // �����̎�
      data.put(UCMemberProfileCore.LONG_INITIAL_CASH,
               new Long(account.getInitialCash())); // �������Y
      data.put(UCMemberProfileCore.LONG_TRADING_UNIT,
               new Long(account.getTradingUnit())); // ����P��
      data.put(UCMemberProfileCore.LONG_FEE_PER_UNIT,
               new Long(account.getFeePerUnit())); // �P�ʎ��������̎萔��
      data.put(UCMemberProfileCore.LONG_MARGIN_RATE,
               new Long(account.getMarginRate())); // �؋�����
      data.put(UCMemberProfileCore.LONG_MAX_LOAN, new Long(account.getMaxLoan())); // �؂������x�z
      data.put(UCMemberProfileCore.DOUBLE_INTEREST,
               new Double(account.getInterestPerYear())); // �؂�������
      data.put(UCMemberProfileCore.INT_STATUS, new Integer(account.getStatus())); // ����\(+1),����s�\(-1)
      data.put(UCMemberProfileCore.INT_NO_OF_LOGIN_AGENTS,
               new Integer(office.getNoOfLoginAgents())); // ���O�C�����̃G�[�W�F���g��
      HashMap yesterdayBalanceHash = new HashMap();
      UBalance yesterdayBalancel = account.getYesterdayBalance();
      setBalanceToHashMapForMemberProfile(yesterdayBalanceHash,
                                          yesterdayBalancel);
      data.put(UCMemberProfileCore.HASHMAP_YESTERDAY_BALANCE,
               yesterdayBalanceHash); // �O�����x
      HashMap todayBalanceHash = new HashMap();
      UBalance todayBalance = account.getTodayBalance();
      setBalanceToHashMapForMemberProfile(todayBalanceHash, todayBalance);
      data.put(UCMemberProfileCore.HASHMAP_TODAY_BALANCE, todayBalanceHash); // �������x
      HashMap positionHash = new HashMap();
      UPosition pos = account.getPosition();
      setPositionToHashMapForMemberProfile(positionHash, pos);
      data.put(UCMemberProfileCore.HASHMAP_POSITION, positionHash); // �|�W�V����
      ucs.setStatus(true);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(5);
    } finally {
      fStateLock.readUnlock();
      return ucs;
    }
  }

  /**
   * ExchangeProfile�R�}���h���������܂��D
   * @param data ����������i�[���邽�߂�HashMap
   * @param userID ���̃R�}���h�����s�������[�U�[�̃��[�U�[ID
   * @return ���s���
   */
  public UCommandStatus doExchangeProfile(HashMap data, int userID) {
    UCommandStatus ucs = new UCommandStatus();
    try {
      fStateLock.readLock();
      if (!fCmdExecutableChecker.isExecutable(UCExchangeProfileCore.CMD_NAME,
                                              fStatus, userID)) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.UNACCEPTABLE_COMMAND);
        ucs.setErrorMessage(fCmdExecutableChecker.getErrorMessage());
        return ucs;
      }
      UExchangeAccount ea = fAccountManager.getExchangeAccount();
      long cash = ea.getCash();
      data.put(UCExchangeProfileCore.LONG_CASH, new Long(cash)); // �ۗL����
      long sellPosition = ea.getSellPosition();
      data.put(UCExchangeProfileCore.LONG_SELL_POSITION, new Long(sellPosition)); // ������̊Ǘ����̔��|�W�V����
      long buyPosition = ea.getBuyPosition();
      data.put(UCExchangeProfileCore.LONG_BUY_POSITION, new Long(buyPosition)); // ������̊Ǘ����̔��|�W�V����
      int noOfMembers = fAccountManager.getNoOfAccounts();
      data.put(UCExchangeProfileCore.INT_NO_OF_MEMBERS, new Integer(noOfMembers)); // �����
      ucs.setStatus(true);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(5);
    } finally {
      fStateLock.readUnlock();
      return ucs;
    }
  }

  /**
   * BoardData�R�}���h(�ߋ��̔񂹂��s��������̔𓾂邽�߂̃R�}���h)�ւ̉������������܂��D
   * ������t���ԑ�, �񂹌㎞�ԑсC����㎞�ԑшȊO�̓G���[��Ԃ��܂��D
   * ������, 1���ڂ̂P��ڂ̒������Ԃ̓G���[��Ԃ��܂��D
   * @param hash �e�����i�[���邽�߂̃n�b�V���}�b�v
   * @param arrayList �n�b�V���}�b�v���i�[���邽�߂̃��X�g
   */
  public UCommandStatus doBoardData(HashMap hash, ArrayList arrayList) {
    UCommandStatus ucs = new UCommandStatus();
    try {
      fStateLock.readLock();
      if (!fCmdExecutableChecker.isExecutable(UCBoardDataCore.CMD_NAME, fStatus,
                                              UMart.SU_ID)) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.UNACCEPTABLE_COMMAND);
        ucs.setErrorMessage(fCmdExecutableChecker.getErrorMessage());
        return ucs;
      }
      if (fStatus.getDate() == 1 && fStatus.getSession() == 1
          && fStatus.getState() == UServerStatus.ACCEPT_ORDERS) { // �P���ڂ̂P��ڂ̒�������
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.UNACCEPTABLE_COMMAND);
        ucs.setErrorMessage("THE BOARD HAS NEVER UPDATED");
        return ucs;
      }
      ucs.setStatus(true);
      hash.put(UCBoardDataCore.LONG_TOTAL_BUY_VOLUME,
               new Long(fBoardInformation.getTotalBuyVolume()));
      hash.put(UCBoardDataCore.LONG_MIN_PRICE,
               new Long(fBoardInformation.getMinPrice()));
      hash.put(UCBoardDataCore.LONG_MAX_PRICE,
               new Long(fBoardInformation.getMaxPrice()));
      hash.put(UCBoardDataCore.LONG_CONTRACT_PRICE,
               new Long(fBoardInformation.getContractPrice()));
      hash.put(UCBoardDataCore.LONG_CONTRACT_VOLUME,
               new Long(fBoardInformation.getContractVolume()));
      Enumeration elements = fBoardInformation.getBoardInfoElements().elements();
      // �ȉ�,���s�̏��
      UBoardInfoElement info = (UBoardInfoElement) elements.nextElement();
      if (info.getSellVolume() > 0) {
        HashMap marketSell = new HashMap();
        marketSell.put(UCBoardDataCore.LONG_PRICE,
                       new Long(fBoardInformation.getMinPrice()));
        marketSell.put(UCBoardDataCore.STRING_SELL_BUY, "sell");
        marketSell.put(UCBoardDataCore.LONG_VOLUME, new Long(info.getSellVolume()));
        arrayList.add(marketSell);
      }
      long marketBuyVolume = info.getBuyVolume();
      // �ȉ�, �w�l�̏��
      while (elements.hasMoreElements()) {
        info = (UBoardInfoElement) elements.nextElement();
        if (info.getSellVolume() > 0) {
          HashMap limitSell = new HashMap();
          limitSell.put(UCBoardDataCore.LONG_PRICE, new Long(info.getPrice()));
          limitSell.put(UCBoardDataCore.STRING_SELL_BUY, "sell");
          limitSell.put(UCBoardDataCore.LONG_VOLUME,
                        new Long(info.getSellVolume()));
          arrayList.add(limitSell);
        }
        if (info.getBuyVolume() > 0) {
          HashMap limitBuy = new HashMap();
          limitBuy.put(UCBoardDataCore.LONG_PRICE, new Long(info.getPrice()));
          limitBuy.put(UCBoardDataCore.STRING_SELL_BUY, "buy");
          limitBuy.put(UCBoardDataCore.LONG_VOLUME, new Long(info.getBuyVolume()));
          arrayList.add(limitBuy);
        }
      }
      if (marketBuyVolume > 0) {
        HashMap marketBuy = new HashMap();
        marketBuy.put(UCBoardDataCore.LONG_PRICE,
                      new Long(fBoardInformation.getMaxPrice()));
        marketBuy.put(UCBoardDataCore.STRING_SELL_BUY, "buy");
        marketBuy.put(UCBoardDataCore.LONG_VOLUME, new Long(marketBuyVolume));
        arrayList.add(marketBuy);
      }
    } catch (Exception e) {
      System.err.println("Exception: " + e + " in UMart.doBoardData");
      System.exit(5);
    } finally {
      fStateLock.readUnlock();
      return ucs;
    }
  }

  /**
   * BoardInformation�R�}���h�i�ŐV�̔��𓾂邽�߂̃R�}���h�j�ւ̉������������܂��D
   * ������t���ԑ�, �񂹌㎞�ԑсC����㎞�ԑшȊO�̓G���[��Ԃ��܂��D
   * ������, 1���ڂ̂P��ڂ̒������Ԃ̓G���[��Ԃ��܂��D
   * @param data �̍ŏI�X�V�����C���s�������ʁC�e���i���Ƃ̒�������
   */
  public UCommandStatus doBoardInformation(HashMap data) {
    UCommandStatus ucs = new UCommandStatus();
    try {
      fStateLock.readLock();
      if (!fCmdExecutableChecker.isExecutable(UCBoardInformationCore.CMD_NAME,
                                              fStatus, SU_ID)) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.UNACCEPTABLE_COMMAND);
        ucs.setErrorMessage(fCmdExecutableChecker.getErrorMessage());
        return ucs;
      }
      ucs.setStatus(true);
      UBoardInformation boardInfo = new UBoardInformation();
      fBoard.setBoardInfo(boardInfo);
//			synchronized ( fBoard ) {
//				boardInfo.setLastUpdateTime(fBoard.getLastUpdateTime());
//				Iterator orders = fBoard.getOrderArray();
//				while (orders.hasNext()) {
//					UOrder o = (UOrder) orders.next();
//					boardInfo.addInformation( o.getSellBuy(), o.getMarketLimit(),
//				                            o.getPrice(), o.getVolume() );
//				}
//			}
      String lastUpdateTime = extractTime(boardInfo.getLastUpdateTime());
      data.put(UCBoardInformationCore.STRING_LAST_UPDATE_TIME, lastUpdateTime);
      ArrayList arrayList = new ArrayList();
      Enumeration elements = boardInfo.getBoardInfoElements().elements();
      while (elements.hasMoreElements()) {
        UBoardInfoElement info = (UBoardInfoElement) elements.nextElement();
        HashMap hash = new HashMap();
        hash.put(UCBoardInformationCore.LONG_PRICE, new Long(info.getPrice()));
        hash.put(UCBoardInformationCore.LONG_SELL_VOLUME,
                 new Long(info.getSellVolume()));
        hash.put(UCBoardInformationCore.LONG_BUY_VOLUME,
                 new Long(info.getBuyVolume()));
        arrayList.add(hash);
      }
      data.put(UCBoardInformationCore.ARRAYLIST_BOARD, arrayList);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(5);
    } finally {
      fStateLock.readUnlock();
      return ucs;
    }
  }

  /**
   * date��"hh:mm:ss"�`���ɕϊ����܂��D
   * @param date ������
   */
  private String extractTime(Date date) {
    return new SimpleDateFormat("HH:mm:ss").format(date);
  }

  /**
   * OrderCancel�R�}���h�ւ̉������������܂��D
   * ������t���ԑшȊO�̓G���[��Ԃ��B
   * @param hash �e�����i�[���邽�߂̃n�b�V���}�b�v
   * @param userID ���[�UID
   * @param orderID ����������������ID
   */
  public UCommandStatus doOrderCancel(HashMap hash, int userID, long orderID) {
    UCommandStatus ucs = new UCommandStatus();
    try {
      fStateLock.readLock();
      if (!fCmdExecutableChecker.isExecutable(UCOrderCancelCore.CMD_NAME,
                                              fStatus, userID)) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.UNACCEPTABLE_COMMAND);
        ucs.setErrorMessage(fCmdExecutableChecker.getErrorMessage());
        return ucs;
      }
      synchronized (fOrderManager) {
        boolean flag = fBoard.removeOrder(userID, orderID);
        UOrder o = fOrderManager.cancelOrder(userID, orderID);
        if (o != null) {
          if (!flag) {
            System.err.println("Found inconsistent in UMartStandAlone.doOrderCancel");
            System.exit(5);
          }
          long uncontractVolume = o.getCancelVolume();
          long contractVolume = o.getContractVolume();
          String time = extractTime(new Date());
          ucs.setStatus(true);
          hash.put(UCOrderCancelCore.LONG_ORDER_ID, new Long(orderID));
          hash.put(UCOrderCancelCore.STRING_CANCEL_TIME, time);
          hash.put(UCOrderCancelCore.LONG_CANCEL_VOLUME,
                   Long.toString(uncontractVolume));
          hash.put(UCOrderCancelCore.LONG_UNCANCEL_VOLUME,
                   Long.toString(contractVolume));
          fOrderCommandLog.registerOrderCancel(fStatus.getDate(),
                                               fStatus.getSession(),
                                               o.getUserName(),
                                               orderID, uncontractVolume);
        } else {
          ucs.setStatus(false);
          ucs.setErrorCode(ICommand.INVALID_ARGUMENTS);
          ucs.setErrorMessage("THE ORDER WITH ID:" + orderID +
                              " CANNOT BE FOUND");
        }
      }
    } catch (Exception e) {
      System.err.println("Exception: " + e + " in UMart.doOrderCancel");
      System.exit(5);
    } finally {
      fStateLock.readUnlock();
      return ucs;
    }
  }

  /**
   * CheckOrder�R�}���h�ւ̉������������܂��D
   * @param orderID ����ID
   * @param hash �e�����i�[���邽�߂̃n�b�V���}�b�v
   * @return ������������
   */
  public UCommandStatus doOrderCheck(long orderID, HashMap hash) {
    UCommandStatus ucs = new UCommandStatus();
    try {
      fStateLock.readLock();
      if (!fCmdExecutableChecker.isExecutable(UCOrderCheckCore.CMD_NAME,
                                              fStatus, SU_ID)) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.UNACCEPTABLE_COMMAND);
        ucs.setErrorMessage(fCmdExecutableChecker.getErrorMessage());
        return ucs;
      }
      UOrder o = null;
      synchronized (fOrderManager) {
        o = fOrderManager.getOrderFromHistory(orderID);
      }
      if (o == null) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.INVALID_ARGUMENTS);
        ucs.setErrorMessage("orderID is invalid.");
        return ucs;
      }
      ucs.setStatus(true);
      synchronized (o) {
        hash.put(UCOrderCheckCore.INT_USER_ID, new Integer(o.getUserID()));
        hash.put(UCOrderCheckCore.STRING_BRAND_NAME, o.getBrandName());
        hash.put(UCOrderCheckCore.LONG_ORDER_ID, new Long(o.getOrderID()));
        hash.put(UCOrderCheckCore.STRING_ORDER_TIME, this.extractTime(o.getTime()));
        hash.put(UCOrderCheckCore.INT_ORDER_DATE, new Integer(o.getDate()));
        hash.put(UCOrderCheckCore.INT_ORDER_BOARD_NO, new Integer(o.getSession()));
        hash.put(UCOrderCheckCore.INT_SELL_BUY, new Integer(o.getSellBuy()));
        hash.put(UCOrderCheckCore.INT_MARKET_LIMIT,
                 new Integer(o.getMarketLimit()));
        hash.put(UCOrderCheckCore.INT_NEW_REPAY, new Integer(o.getNewRepay()));
        hash.put(UCOrderCheckCore.LONG_ORDER_PRICE, new Long(o.getPrice()));
        hash.put(UCOrderCheckCore.LONG_ORDER_VOLUME, new Long(o.getVolume()));
        hash.put(UCOrderCheckCore.LONG_SUM_OF_CONTRACT_VOLUME,
                 new Long(o.getContractVolume()));
        hash.put(UCOrderCheckCore.LONG_CANCEL_VOLUME,
                 new Long(o.getCancelVolume()));
        ArrayList contractInfoArray = new ArrayList();
        Enumeration enumeration = o.getContractInformationArray().elements();
        while (enumeration.hasMoreElements()) {
          UContractInformation info = (UContractInformation) enumeration.
              nextElement();
          HashMap infoHash = new HashMap();
          infoHash.put(UCOrderCheckCore.LONG_CONTRACT_ID,
                       new Long(info.getContractID()));
          infoHash.put(UCOrderCheckCore.STRING_CONTRACT_TIME,
                       this.extractTime(info.getTime()));
          infoHash.put(UCOrderCheckCore.LONG_CONTRACT_PRICE,
                       new Long(info.getPrice()));
          infoHash.put(UCOrderCheckCore.LONG_CONTRACT_VOLUME,
                       new Long(info.getVolume()));
          infoHash.put(UCOrderCheckCore.INT_CONTRACT_DATE,
                       new Integer(info.getDate()));
          infoHash.put(UCOrderCheckCore.INT_CONTRACT_BOARD_NO,
                       new Integer(info.getSession()));
          contractInfoArray.add(infoHash);
        }
        hash.put(UCOrderCheckCore.ARRAYLIST_CONTRACT_INFORMATION_ARRAY,
                 contractInfoArray);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(5);
    } finally {
      fStateLock.readUnlock();
      return ucs;
    }
  }

  /**
   * OrderRequest�R�}���h�ւ̉������������܂��D
   * ������t���ԑшȊO�̓G���[��Ԃ��B
   * @param hash �e�����i�[���邽�߂̃n�b�V���}�b�v
   * @param userID ���[�UID
   * @param brandName ������
   * @param newRepay �V�K�ԍϋ敪
   * @param sellBuy �����敪
   * @param marketLimit ���s�w�l�敪
   * @param price ��]������i
   * @param volume ��]�������
   */
  public UCommandStatus doOrderRequest(HashMap hash, int userID,
                                       String brandName, int newRepay,
                                       int sellBuy, int marketLimit,
                                       long price, long volume) {
    UCommandStatus ucs = new UCommandStatus();
    try {
      fStateLock.readLock();
      if (!fCmdExecutableChecker.isExecutable(UCOrderRequestCore.CMD_NAME,
                                              fStatus, userID)) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.UNACCEPTABLE_COMMAND);
        ucs.setErrorMessage(fCmdExecutableChecker.getErrorMessage());
        return ucs;
      }
      UAccount account = fAccountManager.getAccount(userID);
      if (account.getStatus() == UAccount.UNAVAILABLE) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.UNACCEPTABLE_COMMAND);
        ucs.setErrorMessage("YOU ARE BANKRUPTED!!");
        return ucs;
      }
      long latestFuturePrice = fPriceInfoDB.getLatestFuturePrice();
      long delta = (long)((double)latestFuturePrice * MAX_RATE);
      if (marketLimit == UOrder.LIMIT && Math.abs(price - latestFuturePrice) >= delta) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.UNACCEPTABLE_COMMAND);
        ucs.setErrorMessage("Your order price exceeds the limit!!");
        return ucs;      	
      }
      ucs.setStatus(true);
      String userName = account.getUserName();
      int date = fStatus.getDate();
      int boardNo = fStatus.getSession();
      UOrder o;
      synchronized (fOrderManager) {
      	o = fOrderManager.createOrder(userID, userName,	brandName, 
    		sellBuy, marketLimit, newRepay, price, volume, date, boardNo);
        fOrderManager.addOrder(o);
        fBoard.appendOrder(o);
        fOrderManager.registerOrderToHistory(o);
        fOrderCommandLog.registerOrderRequest(o);
      }
      hash.put(UCOrderRequestCore.LONG_ORDER_ID, Long.toString(o.getOrderID()));
      hash.put(UCOrderRequestCore.STRING_ORDER_TIME, extractTime(o.getTime()));
    } catch (Exception e) {
      System.err.println("Exception: " + e + " in UMart.doOrderRequest");
      System.exit(5);
    } finally {
      fStateLock.readUnlock();
      return ucs;
    }
  }

  /**
   * ServerTime�R�}���h�ւ̉������������܂��D
   * @param hash �e�����i�[���邽�߂̃n�b�V���}�b�v
   */
  public UCommandStatus doServerTime(HashMap hash) {
    UCommandStatus ucs = new UCommandStatus();
    ucs.setStatus(true);
    hash.put(UCServerTimeCore.STRING_SERVER_TIME, extractTime(new Date()));
    return ucs;
  }

  /**
   * TodaysQuotes�R�}���h�ւ̉�������������B
   * ������t���ԑ�, ����㎞�ԑ�, ���ٌ㎞�ԑшȊO�̓G���[��Ԃ��B
   * @param arrayList �n�b�V���}�b�v���i�[���邽�߂̃��X�g
   * @param brandName ������
   * @param noOfBoards �ߋ��񂹉��񕪂̂̏�񂪕K�v��?
   */
  public UCommandStatus doTodaysQuotes(ArrayList arrayList, String brandName,
                                       int noOfBoards) {
    // noOfBoards < 0 �őS�Ă̔񂹂ɂ��Ă̏���Ԃ�
    UCommandStatus ucs = new UCommandStatus();
    try {
      fStateLock.readLock();
      if (!fCmdExecutableChecker.isExecutable(UCTodaysQuotesCore.CMD_NAME,
                                              fStatus, SU_ID)) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.UNACCEPTABLE_COMMAND);
        ucs.setErrorMessage(fCmdExecutableChecker.getErrorMessage());
        return ucs;
      }
      ucs.setStatus(true);
      int startIndex = fQuoteDB.getNoOfTodaysQuotes() - 1;
      if (startIndex < 0) {
        // agent.sendMessage( "j30 -1 00:00:00:+09 -1 -1 -1 -1" );
        return ucs;
      }
      int endIndex = startIndex;
      if (noOfBoards < 0) {
        endIndex = 0;
      } else {
        endIndex -= (noOfBoards - 1);
      }
      if (endIndex < 0) {
        endIndex = 0;
      }
      for (int i = startIndex; i >= endIndex; --i) {
        UTodaysQuote tq = fQuoteDB.getTodaysQuote(i);
        HashMap hash = new HashMap();
        hash.put(UCTodaysQuotesCore.STRING_BRAND_NAME, tq.getBrandName());
        hash.put(UCTodaysQuotesCore.INT_DATE, new Integer(tq.getDate()));
        hash.put(UCTodaysQuotesCore.INT_BOARD_NO, new Integer(tq.getSession()));
        hash.put(UCTodaysQuotesCore.STRING_TIME, extractTime(new Date()));
        hash.put(UCTodaysQuotesCore.LONG_PRICE, new Long(tq.getPrice()));
        hash.put(UCTodaysQuotesCore.LONG_VOLUME, new Long(tq.getVolume()));
        hash.put(UCTodaysQuotesCore.LONG_ASKED_QUOTATION,
                 new Long(tq.getAskedQuotation()));
        hash.put(UCTodaysQuotesCore.LONG_BID_QUOTATION,
                 new Long(tq.getBidQuotation()));
        arrayList.add(hash);
      }
    } catch (Exception e) {
      System.err.println("Exception: " + e + " in UMart.doTodaysQuotes");
      System.exit(5);
    } finally {
      fStateLock.readUnlock();
      return ucs;
    }
  }

  /**
   * �n�b�V���}�b�vhash�ɒ������o�̊e�f�[�^���i�[���܂��D
   * @param hash �����i�[���邽�߂̃n�b�V���}�b�v
   * @param o ����
   */
  private void sendOrderMessage(HashMap hash, UOrder o) {
    hash.put(UCOrderStatusCore.LONG_ORDER_ID, Long.toString(o.getOrderID()));
    hash.put(UCOrderStatusCore.STRING_ORDER_TIME, extractTime(o.getTime()));
    hash.put(UCOrderStatusCore.STRING_BRAND_NAME, o.getBrandName());
    hash.put(UCOrderStatusCore.INT_NEW_REPAY, Integer.toString(o.getNewRepay()));
    hash.put(UCOrderStatusCore.INT_SELL_BUY, Integer.toString(o.getSellBuy()));
    hash.put(UCOrderStatusCore.INT_MARKET_LIMIT,
             Integer.toString(o.getMarketLimit()));
    hash.put(UCOrderStatusCore.LONG_PRICE, Long.toString(o.getPrice()));
    hash.put(UCOrderStatusCore.LONG_VOLUME, Long.toString(o.getVolume()));
    hash.put(UCOrderStatusCore.LONG_CONTRACTED_VOLUME,
             Long.toString(o.getContractVolume()));
    hash.put(UCOrderStatusCore.LONG_CANCEL_VOLUME,
             Long.toString(o.getCancelVolume()));
  }

  /**
   * OrderStatus�R�}���h�ւ̉������������܂��D
   * ������t���ԑ�, ����㎞�ԑшȊO�̓G���[��Ԃ��܂��D
   * @param arrayList �n�b�V���}�b�v���i�[���邽�߂̃��X�g
   * @param userID ���[�UID
   */
  public UCommandStatus doOrderStatus(ArrayList arrayList, int userID) {
    UCommandStatus ucs = new UCommandStatus();
    try {
      fStateLock.readLock();
      if (!fCmdExecutableChecker.isExecutable(UCOrderStatusCore.CMD_NAME,
                                              fStatus, userID)) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.UNACCEPTABLE_COMMAND);
        ucs.setErrorMessage(fCmdExecutableChecker.getErrorMessage());
        return ucs;
      }
      ucs.setStatus(true);
      synchronized (fOrderManager) {
        UOrderArray array = fOrderManager.getOrderArray(userID);
        if (array == null) {
          System.err.print("OrderArray not found ");
          System.err.println("in UMart.doOrderStatus");
          System.exit( -5);
        }
        for (int i = 0; i < array.getNoOfContractedOrders(); ++i) {
          UOrder o = array.getContractedOrderAt(i);
          HashMap hash = new HashMap();
          sendOrderMessage(hash, o);
          arrayList.add(hash);
        }
        for (int i = 0; i < array.getNoOfUncontractedOrders(); ++i) {
          UOrder o = array.getUncontractedOrderAt(i);
          HashMap hash = new HashMap();
          sendOrderMessage(hash, o);
          arrayList.add(hash);
        }
      }
    } catch (Exception e) {
      System.err.println("Exception: " + e + " in UMart.doOrderStatus");
      System.exit(5);
    } finally {
      fStateLock.readUnlock();
      return ucs;
    }
  }

  /**
   * OrderStatus�R�}���h�ւ̉������������܂��D
   * ������t���ԑ�, ����㎞�ԑшȊO�̓G���[��Ԃ��܂��D
   * @param hash �e�����i�[���邽�߂̃n�b�V���}�b�v
   * @param o �I�[�_�[
   * @param info �����
   */
  private void setContractInfoToHash(HashMap hash, UOrder o,
                                     UContractInformation info) {
    hash.put(UCExecutionsCore.LONG_CONTRACT_ID, new Long(info.getContractID()));
    hash.put(UCExecutionsCore.STRING_CONTRACT_TIME, extractTime(info.getTime()));
    hash.put(UCExecutionsCore.LONG_ORDER_ID, new Long(o.getOrderID()));
    hash.put(UCExecutionsCore.STRING_BRAND_NAME, o.getBrandName());
    hash.put(UCExecutionsCore.INT_NEW_REPAY, new Integer(o.getNewRepay()));
    hash.put(UCExecutionsCore.INT_SELL_BUY, new Integer(o.getSellBuy()));
    hash.put(UCExecutionsCore.LONG_PRICE, new Long(info.getPrice()));
    hash.put(UCExecutionsCore.LONG_VOLUME, new Long(info.getVolume()));
  }

  /**
   * Executions�R�}���h�ւ̉������������܂��D
   * ������t���ԑ�, ����㎞�ԑшȊO�̓G���[��Ԃ��܂��D
   * @param arrayList �n�b�V���}�b�v���i�[���邽�߂̃��X�g
   * @param userID ���[�UID
   */
  public UCommandStatus doExecutions(ArrayList arrayList, int userID) {
    UCommandStatus ucs = new UCommandStatus();
    try {
      fStateLock.readLock();
      if (!fCmdExecutableChecker.isExecutable(UCExecutionsCore.CMD_NAME,
                                              fStatus, userID)) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.UNACCEPTABLE_COMMAND);
        ucs.setErrorMessage(fCmdExecutableChecker.getErrorMessage());
        return ucs;
      }
      ucs.setStatus(true);
      synchronized (fOrderManager) {
        Vector orderArray = fOrderManager.getAllOrders(userID);
        Enumeration orders = orderArray.elements();
        while (orders.hasMoreElements()) {
          UOrder o = (UOrder) orders.nextElement();
          Enumeration infos = o.getContractInformationArray().elements();
          while (infos.hasMoreElements()) {
            UContractInformation info = (UContractInformation) infos.
                nextElement();
            HashMap hash = new HashMap();
            setContractInfoToHash(hash, o, info);
            arrayList.add(hash);
          }
        }
      }
    } catch (Exception e) {
      System.err.println("Exception: " + e + " in UMart.doExecutions");
      System.exit(5);
    } finally {
      fStateLock.readUnlock();
      return ucs;
    }
  }

  /**
   * Login�R�}���h�ւ̉������������܂��D
   * @param loginName ���[�U�[��
   * @param passwd �p�X���[�h
   * @return �Ή����郍�O�C�����(ULoginStatus)
   */
  public ULoginStatus doLogin(String loginName, String passwd) {
    int userID = fAccountManager.getUserID(loginName, passwd);
    if (userID < 0) {
      return null;
    }
    return fLoginManager.findLoginStatus(userID);
  }

  /**
   * FuturePrice�R�}���h�ւ̉������������܂��D
   * ����O���ԑ�, ������t���ԑ�, ����㎞�ԑ�, ���ό㎞�ԑшȊO��
   * �G���[��Ԃ��B
   * @param arrayList �n�b�V���}�b�v���i�[���邽�߂̃��X�g
   * @param brandName ������
   * @param noOfSteps �ߋ����X�e�b�v���̐敨���i���K�v��?
   */
  public UCommandStatus doFuturePrice(ArrayList arrayList, String brandName,
                                      int noOfSteps) {
    UCommandStatus ucs = new UCommandStatus();
    try {
      fStateLock.readLock();
      if (!fCmdExecutableChecker.isExecutable(UCFuturePriceCore.CMD_NAME,
                                              fStatus, SU_ID)) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.UNACCEPTABLE_COMMAND);
        ucs.setErrorMessage(fCmdExecutableChecker.getErrorMessage());
        return ucs;
      }
      ucs.setStatus(true);
      long[] prices = fPriceInfoDB.getFuturePrices(noOfSteps);
      int date = fStatus.getDate();
      int boardNo = fStatus.getSession();
      int steps = (date - 1) * fNoOfSessionsPerDay + boardNo;
      for (int i = 0; i < prices.length; ++i) {
        HashMap elem = new HashMap();
        elem.put(UCFuturePriceCore.STRING_BRAND_NAME, brandName);
        if (steps > 0) {
          date = (steps - 1) / fNoOfSessionsPerDay + 1;
          boardNo = steps - (date - 1) * fNoOfSessionsPerDay;
        } else {
          date = - (Math.abs(steps) / fNoOfSessionsPerDay);
          boardNo = fNoOfSessionsPerDay + steps - (date * fNoOfSessionsPerDay);
        }
        elem.put(UCFuturePriceCore.INT_DAY, new Integer(date));
        elem.put(UCFuturePriceCore.INT_BOARD_NO, new Integer(boardNo));
        elem.put(UCFuturePriceCore.INT_STEP, new Integer(steps));
        elem.put(UCFuturePriceCore.LONG_PRICE, new Long(prices[i]));
        arrayList.add(elem);
        --steps;
//				arrayList.add(brandName + " YYYY-MM-DD " + prices[i]);
      }
    } catch (Exception e) {
      System.err.println("Exception: " + e + " in UMart.doFuturePrice");
      System.exit(5);
    } finally {
      fStateLock.readUnlock();
      return ucs;
    }
  }

  /**
   * HistoricalQuotes�R�}���h�ւ̉������������܂��D
   * ������t���ԑ�, ����㎞�ԑ�, ���ٌ㎞�ԑшȊO�̓G���[��Ԃ��܂��D
   * @param arrayList �n�b�V���}�b�v���i�[���邽�߂̃��X�g
   * @param brandName ������
   * @param noOfDays �ߋ��������̏�񂪕K�v��?
   */
  public UCommandStatus doHistoricalQuotes(ArrayList arrayList,
                                           String brandName, int noOfDays) {
    // noOfDays < 0 �ŉߋ��S�Ă�Ԃ�
    UCommandStatus ucs = new UCommandStatus();
    try {
      fStateLock.readLock();
      if (!fCmdExecutableChecker.isExecutable(UCHistoricalQuotesCore.CMD_NAME,
                                              fStatus, SU_ID)) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.UNACCEPTABLE_COMMAND);
        ucs.setErrorMessage(fCmdExecutableChecker.getErrorMessage());
        return ucs;
      }
      ucs.setStatus(true);
      int startIndex = fQuoteDB.getNoOfHistoricalQuotes() - 1;
      if (startIndex < 0) {
        // agent.sendMessage( "j30 -1 00:00:00:+09 -1 -1 -1 -1 -1" );
        return ucs;
      }
      int endIndex = startIndex;
      if (noOfDays < 0) {
        endIndex = 0;
      } else {
        endIndex -= (noOfDays - 1);
      }
      if (endIndex < 0) {
        endIndex = 0;
      }
      for (int i = startIndex; i >= endIndex; --i) {
        UHistoricalQuote hq = fQuoteDB.getHistoricalQuote(i);
        HashMap hash = new HashMap();
        hash.put(UCHistoricalQuotesCore.STRING_BRAND_NAME, hq.getBrandName());
        hash.put(UCHistoricalQuotesCore.INT_DATE, new Integer(hq.getDate()));
        hash.put(UCHistoricalQuotesCore.LONG_START_PRICE,
                 new Long(hq.getStartPrice()));
        hash.put(UCHistoricalQuotesCore.LONG_HIGHEST_PRICE,
                 new Long(hq.getHighestPrice()));
        hash.put(UCHistoricalQuotesCore.LONG_LOWEST_PRICE,
                 new Long(hq.getLowestPrice()));
        hash.put(UCHistoricalQuotesCore.LONG_END_PRICE, new Long(hq.getEndPrice()));
        hash.put(UCHistoricalQuotesCore.LONG_VOLUME, new Long(hq.getVolume()));
        arrayList.add(hash);
      }
    } catch (Exception e) {
      System.err.println(
          "Exception: " + e + " in UMart.doHistoricalQuotes");
      System.exit(5);
    } finally {
      fStateLock.readUnlock();
      return ucs;
    }
  }

  /**
   * MarketStatus�R�}���h�ւ̉������������܂��D
   * @param hash �e�����i�[���邽�߂̃n�b�V���}�b�v
   */
  public UCommandStatus doMarketStatus(HashMap hash) {
    UCommandStatus ucs = new UCommandStatus();
    try {
      fStateLock.readLock();
      ucs.setStatus(true);
      hash.put(UCMarketStatusCore.INT_MARKET_STATUS,
               new Integer(fStatus.getState()));
    } catch (Exception e) {
      System.err.println("Exception: " + e + " in UMart.doMarketStatus");
      System.exit(5);
    } finally {
      fStateLock.readUnlock();
      return ucs;
    }
  }

  /**
   * OrderIDHistory�R�}���h�ւ̉������������܂��D
   * @param selfUserID ���̃R�}���h�����s�������[�U�[��ID
   * @param arrayList �w�肳�ꂽ���[�U�[ID�̉ߋ�noOfSteps���̒����˗���ID���i�[���邽�߂�ArrayList
   * @param targetUserID �w�胆�[�U�[ID (-1�̏ꍇ�CselfUserID����������j
   * @param noOfSteps �ߋ��񂹉��񕪂̏�񂪂ق������H
   * @return �R�}���h�̏�������
   */
  public UCommandStatus doOrderIDHistory(int selfUserID,
                                         ArrayList arrayList, int targetUserID,
                                         int noOfSteps) {
    arrayList.clear();
    UCommandStatus ucs = new UCommandStatus();
    try {
      fStateLock.readLock();
      if (targetUserID < 0) {
        targetUserID = selfUserID;
      }
      if ( (selfUserID != targetUserID && selfUserID != UMart.SU_ID)
          || !fCmdExecutableChecker.isExecutable(UCOrderIDHistoryCore.CMD_NAME,
                                                 fStatus, targetUserID)) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.UNACCEPTABLE_COMMAND);
        ucs.setErrorMessage(fCmdExecutableChecker.getErrorMessage());
        return ucs;
      }
      int sumOfSteps = (fStatus.getDate() - 1) * fNoOfSessionsPerDay +
          fStatus.getSession();
      sumOfSteps -= (noOfSteps - 1);
      int date = (sumOfSteps - 1) / fNoOfSessionsPerDay + 1;
      int boardNo = sumOfSteps - (date - 1) * fNoOfSessionsPerDay;
      if (date < 1) {
        date = 1;
      }
      if (boardNo < 1) {
        boardNo = 1;
        // System.err.println( "Greater than Day" + date + " Board" + boardNo );
      }
      synchronized (fOrderManager) {
        UOrderArray orderArray = fOrderManager.getOrderArray(targetUserID);
        ArrayList history = orderArray.getHistory();
        // System.err.println(history.toString());
        int index = 0;
        for (int i = history.size() - 1; i >= 0; --i) {
          UOrder o = (UOrder) history.get(i);
          if (o.getDate() >= date && o.getSession() >= boardNo) {
            arrayList.add(new Long(o.getOrderID()));
          }
        }
      }
      ucs.setStatus(true);
    } catch (Exception e) {
      System.err.println("Exception: " + e + " in UMart.doOrderIDHistory");
      System.exit(5);
    } finally {
      fStateLock.readUnlock();
      return ucs;
    }
  }

  /**
   * Position�R�}���h�ւ̉������������܂��D
   * ������t���ԑ�, ����㎞�ԑ�, ���ό㎞�ԑшȊO�̓G���[��Ԃ��܂��D
   * @param hash �e�����i�[���邽�߂̃n�b�V���}�b�v
   * @param userID ���[�UID
   */
  public UCommandStatus doPosition(HashMap hash, int userID) {
    UCommandStatus ucs = new UCommandStatus();
    try {
      fStateLock.readLock();
      if (!fCmdExecutableChecker.isExecutable(UCPositionCore.CMD_NAME, fStatus,
                                              userID)) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.UNACCEPTABLE_COMMAND);
        ucs.setErrorMessage(fCmdExecutableChecker.getErrorMessage());
        return ucs;
      }
      ucs.setStatus(true);
      UAccount account = fAccountManager.getAccount(userID);
      if (account == null) {
        System.err.println("Account not found in UMart.doPosition");
        System.exit( -5);
      }
      UPosition pos = account.getPosition();
      long todaySell = 0;
      long todayBuy = 0;
      for (int boardNo = 1; boardNo <= fStatus.getSession(); ++boardNo) {
        todaySell += pos.getTodaySellPosition(boardNo);
        todayBuy += pos.getTodayBuyPosition(boardNo);
      }
      hash.put(UCPositionCore.LONG_TODAY_SELL, new Long(todaySell));
      hash.put(UCPositionCore.LONG_TODAY_BUY, new Long(todayBuy));
      long yesterdaySell = pos.getSumOfSellPositionUntilYesterday();
      long yesterdayBuy = pos.getSumOfBuyPositionUntilYesterday();
      hash.put(UCPositionCore.LONG_YESTERDAY_SELL, new Long(yesterdaySell));
      hash.put(UCPositionCore.LONG_YESTERDAY_BUY, new Long(yesterdayBuy));
    } catch (Exception e) {
      System.err.println("Exception: " + e + " in UMart.doPosition");
      System.exit(5);
    } finally {
      fStateLock.readUnlock();
      return ucs;
    }
  }

  /**
   * Schedule�R�}���h�ւ̉������������܂��D
   * @param hash �e�����i�[���邽�߂̃n�b�V���}�b�v
   */
  public UCommandStatus doSchedule(HashMap hash) {
    UCommandStatus ucs = new UCommandStatus();
    ucs.setStatus(true);
    hash.put(UCScheduleCore.INT_MAX_DAY, new Integer(fMaxDate));
    hash.put(UCScheduleCore.INT_NO_OF_BOARDS, new Integer(fNoOfSessionsPerDay));
    return ucs;
  }

  /**
   * ServerDate�R�}���h�ւ̉������������܂��D
   * @param hash �e�����i�[���邽�߂̃n�b�V���}�b�v
   */
  public UCommandStatus doServerDate(HashMap hash) {
    UCommandStatus ucs = new UCommandStatus();
    try {
      fStateLock.readLock();
      int date = fStatus.getDate();
      int boardNo = fStatus.getSession();
      ucs.setStatus(true);
      hash.put(UCServerDateCore.INT_DAY, new Integer(date));
      hash.put(UCServerDateCore.INT_BOARD_NO, new Integer(boardNo));
    } catch (Exception e) {
      System.err.println("Exception: " + e + " in UMart.doServerDate");
      System.exit(5);
    } finally {
      fStateLock.readUnlock();
      return ucs;
    }
  }

  /**
   * SetSpotDate�R�}���h�ւ̉������������܂��D
   * ����O���ԑшȊO�̓G���[��Ԃ��B
   * @param userID ���[�UID
   * @param startPoint �������i�J�n�ʒu
   */
  public UCommandStatus doSetSpotDate(int userID, int startPoint) {
    UCommandStatus ucs = new UCommandStatus();
    try {
      fStateLock.readLock();
      if (!fCmdExecutableChecker.isExecutable(UCSetSpotDateCore.CMD_NAME,
                                              fStatus, userID)) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.UNACCEPTABLE_COMMAND);
        ucs.setErrorMessage(fCmdExecutableChecker.getErrorMessage());
        return ucs;
      }
      ucs.setStatus(true);
      fPriceInfoDB.initializePtr(startPoint, fMaxDate, fNoOfSessionsPerDay);
    } catch (Exception e) {
      System.err.println(
          "Exception:SetSchedule " + e + " in UMart.doSetSpotDate");
      System.exit(5);
    } finally {
      fStateLock.readUnlock();
      return ucs;
    }
  }

  /**
   * SpotInterval�R�}���h�ւ̉������������܂��D
   * @param hash �e�����i�[���邽�߂̃n�b�V���}�b�v
   */
  public UCommandStatus doSpotInterval(HashMap hash) {
    UCommandStatus ucs = new UCommandStatus();
    try {
      fStateLock.readLock();
      if (!fCmdExecutableChecker.isExecutable(UCSpotIntervalCore.CMD_NAME,
                                              fStatus, SU_ID)) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.UNACCEPTABLE_COMMAND);
        ucs.setErrorMessage(fCmdExecutableChecker.getErrorMessage());
        return ucs;
      }
      ucs.setStatus(true);
      hash.put(UCSpotIntervalCore.INT_SPOT_INTERVAL,
               new Integer(UMart.DEFAULT_SPOT_INTERVAL));
      return ucs;
    } catch (Exception e) {
      System.err.println("Exception: " + e + " in UMart.doSpotInterval");
      System.exit(5);
      return ucs;
    } finally {
      fStateLock.readUnlock();
      return ucs;
    }
  }

  /**
   * SpotPrice�R�}���h�ւ̉������������܂��D
   * ����O���ԑ�, ������t���ԑ�, ����㎞�ԑ�, ���ό㎞�ԑшȊO��
   * �G���[��Ԃ��܂��D
   * @param arrayList �n�b�V���}�b�v���i�[���邽�߂̃��X�g�D�擪�̗v�f�͒��߂̉��i���D
   * @param brandName ������
   * @param noOfSteps �ߋ����X�e�b�v���̌������i���K�v��?
   */
  public UCommandStatus doSpotPrice(ArrayList arrayList, String brandName,
                                    int noOfSteps) {
    UCommandStatus ucs = new UCommandStatus();
    try {
      fStateLock.readLock();
      if (!fCmdExecutableChecker.isExecutable(UCSpotPriceCore.CMD_NAME, fStatus,
                                              SU_ID)) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.UNACCEPTABLE_COMMAND);
        ucs.setErrorMessage(fCmdExecutableChecker.getErrorMessage());
        return ucs;
      }
      ucs.setStatus(true);
      long[] prices = fPriceInfoDB.getSpotPrices(noOfSteps);
      int date = fStatus.getDate();
      int boardNo = fStatus.getSession();
      int steps = (date - 1) * fNoOfSessionsPerDay + boardNo;
      for (int i = 0; i < prices.length; ++i) {
        HashMap elem = new HashMap();
        elem.put(UCSpotPriceCore.STRING_BRAND_NAME, brandName);
        if (steps > 0) {
          date = (steps - 1) / fNoOfSessionsPerDay + 1;
          boardNo = steps - (date - 1) * fNoOfSessionsPerDay;
        } else {
          date = - (Math.abs(steps) / fNoOfSessionsPerDay);
          boardNo = fNoOfSessionsPerDay + steps - (date * fNoOfSessionsPerDay);
        }
        elem.put(UCSpotPriceCore.INT_DAY, new Integer(date));
        elem.put(UCSpotPriceCore.INT_BOARD_NO, new Integer(boardNo));
        elem.put(UCSpotPriceCore.INT_STEP, new Integer(steps));
        elem.put(UCSpotPriceCore.LONG_PRICE, new Long(prices[i]));
        arrayList.add(elem);
        --steps;
        // arrayList.add(brandName + " YYYY-MM-DD " + prices[i]);
      }
    } catch (Exception e) {
      System.err.println("Exception: " + e + " in UMart.doSpotPrice");
      System.exit(5);
    } finally {
      fStateLock.readUnlock();
      return ucs;
    }
  }

  /**
   * ServerStatus�R�}���h���������܂��D
   * @param data �T�[�o�[��Ԃ��i�[���邽�߂�HashMap
   * @param userID ���̃R�}���h�����s�������[�U�[�̃��[�U�[ID
   * @return ���s���
   */
  public UCommandStatus doServerStatus(HashMap data, int userID) {
    UCommandStatus ucs = new UCommandStatus();
    try {
      fStateLock.readLock();
      if (!fCmdExecutableChecker.isExecutable(UCServerStatusCore.CMD_NAME,
                                              fStatus, userID)) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.UNACCEPTABLE_COMMAND);
        ucs.setErrorMessage(fCmdExecutableChecker.getErrorMessage());
        return ucs;
      }
      int date = fStatus.getDate();
      data.put(UCServerStatusCore.INT_DATE, new Integer(date));
      // ���݂̓��t�iU-Mart��)
      int boardNo = fStatus.getSession();
      data.put(UCServerStatusCore.INT_BOARD_NO, new Integer(boardNo));
      // ���݂̔񂹉�
      int state = fStatus.getState();
      data.put(UCServerStatusCore.INT_STATE, new Integer(state)); // ���ԑ�
      ucs.setStatus(true);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(5);
    } finally {
      fStateLock.readUnlock();
      return ucs;
    }
  }

  /**
   * AccountHistory�R�}���h���������܂��D
   * @param array ��������v�f(HashMap)���i�[�����z��
   * @param userID ���̃R�}���h�����s�������[�U�[�̃��[�U�[ID
   * @param targetUserId ���̃��[�U�[ID�i-1�̏ꍇ�CuserID����������j�������[�U�[�̌���������array�Ɋi�[���܂��D
   * @param noOfSteps �w�肳�ꂽ�X�e�b�v�����̒��߂̌�������Ԃ��܂��D
   * @return ���s���
   */
  public UCommandStatus doAccountHistory(ArrayList array, int userID,
                                         int targetUserId, long noOfSteps) {
    UCommandStatus ucs = new UCommandStatus();
    try {
      fStateLock.readLock();
      if (!fCmdExecutableChecker.isExecutable(UCAccountHistoryCore.CMD_NAME,
                                              fStatus, userID)) {
        ucs.setStatus(false);
        ucs.setErrorCode(ICommand.UNACCEPTABLE_COMMAND);
        ucs.setErrorMessage(fCmdExecutableChecker.getErrorMessage());
        return ucs;
      }
      if (targetUserId < 0) {
        targetUserId = userID;
      }
      ArrayList history = fAccountManager.getAccount(targetUserId).
          getAccountHistory();
      if (noOfSteps < 0) {
        for (int i = history.size() - 1; i >= 0; --i) {
          array.add(history.get(i));
        }
      } else {
        for (int i = 0; i < noOfSteps; ++i) {
          int index = history.size() - i - 1;
          if (index < 0) {
            break;
          }
          array.add(history.get(index));
        }
      }
      ucs.setStatus(true);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(5);
    } finally {
      fStateLock.readUnlock();
      return ucs;
    }
  }

  /**
   * ���݂̃T�[�o�[��Ԃ�Ԃ��܂��D
   * @return �T�[�o�[���
   */
  public UServerStatus getStatus() {
    UServerStatus result = null;
    try {
      fStateLock.readLock();
      result = (UServerStatus) fStatus.clone();
    } catch (Exception ex) {
      System.err.println("Exception: " + ex
                         + " in UMart.getMarketStatus");
      System.exit(5);
    } finally {
      fStateLock.readUnlock();
      return result;
    }
  }

  /**
   * U-Mart�T�[�o�̐ݒ��W���o�͂��܂��D
   */
  public void printOn() {
    System.out.println("UMart is ready");
    System.out.println("No of boards per day: " + fNoOfSessionsPerDay);
  }

  /**
   * ���̏�Ԃ֐i�߂܂��D
   * ��ԑJ�ڂ͈ȉ��̂Ƃ���ł�:<BR>
   * ��1�C������t(UServerStatus.ACCEPT_ORDERS)�@�E�E�E(1)<br>
   * ��1, ��(UServerStatus.ITAYOSE) �E�E�E�������<br>
   * ��1, �񂹌�(UServerStatus.AFTER_ITAYOSE)<br>
   * �F<br>
   * �F<br>
   * ��n�C������t(UServerStatus.ACCEPT_ORDERS)
   * ��n, ��(UServerStatus.ITAYOSE) �E�E�E�������
   * ��n, �񂹌�(UServerStatus.AFTER_ITAYOSE)<br>
   * ��n, ���Z(UServerStatus.MARKING_TO_MARKET) �E�E�E�������<br>
   * ��n, ���Z��(UServerStatus.AFTER_MARKING_TO_MARKET)<br>
   * �ŏI��(MaxDate)�łȂ����(1)�ցD<br>
   * ���tMaxDate+1, ��1, ���Z(UServerStatus.SETTLEMENT) �E�E�E�������<br>
   * ���tMaxDate+1, ��1, ���Z(UServerStatus.AFTER_SETTLEMENT)<br>
   * @return �T�[�o�[���
   */
  public UServerStatus nextStatus() {
    try {
      fStateLock.writeLock();
      switch (fStatus.getState()) {
        case UServerStatus.BEFORE_TRADING:
          fStatus.setDate(1);
          fStatus.setSession(1);
          fStatus.setState(UServerStatus.ACCEPT_ORDERS);
          break;
        case UServerStatus.ACCEPT_ORDERS:
          fStatus.setState(UServerStatus.ITAYOSE);
          makeContracts();
          updateLog(fStatus.getDate(), fStatus.getSession(), fStatus.getState());
          if (fStatus.getSession() == fNoOfSessionsPerDay) {
            fStatus.setState(UServerStatus.MARKING_TO_MARKET);
            markToMarket();
            fAccountManager.backupAllBalances( (fStatus.getDate() - 1) *
                                              fNoOfSessionsPerDay +
                                              fStatus.getSession());
            fStatus.setState(UServerStatus.AFTER_MARKING_TO_MARKET);
            updateLog(fStatus.getDate(), fStatus.getSession(), fStatus.getState());
          } else {
            fAccountManager.backupAllBalances( (fStatus.getDate() - 1) *
                                              fNoOfSessionsPerDay +
                                              fStatus.getSession());
            fStatus.incrementSession();
            fStatus.setState(UServerStatus.ACCEPT_ORDERS);
          }
          break;
        case UServerStatus.AFTER_MARKING_TO_MARKET:
          fBoard.clear();
          fOrderManager.removeAllOrders();
          fAccountManager.updateYesterdayClosingPrice();
          fQuoteDB.update();
          fStatus.incrementDate();
          fStatus.resetSession();
          if (fStatus.getDate() == fMaxDate + 1) {
            settlement();
            fStatus.setState(UServerStatus.AFTER_SETTLEMENT);
            updateLog(fStatus.getDate(), fStatus.getSession(), fStatus.getState());
          } else {
            fStatus.setState(UServerStatus.ACCEPT_ORDERS);
          }
          break;
        case UServerStatus.AFTER_SETTLEMENT:
          break;
        default:
          System.err.println("Unknown status in UMartStandAlone.nextStatus");
          System.exit(5);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(5);
    } finally {
      fStateLock.writeUnlock();
      // System.out.println(fStatus.getStateString());
      return (UServerStatus) fStatus.clone();
    }
  }

  /**
   * ������̏�Ԃ��u�񂹎��ԑсv�ɐݒ肵, �񂹏������s���܂��D
   * ��̓I�Ȏ菇�͈ȉ��̂Ƃ���ł��D�F<br>
   * 1. �S����(UOrder)��UOrderManager����UBoard�ֈړ�����B<br>
   * 2. UBoard.makeContracts���\�b�h�ɂ���菈�����s���B<br>
   * 3. UQuoteDB��TodaysQuote��o�^����B<br>
   * 4. UAccountManager�ɖ�艿�i��o�^����B<br>
   * 5. UAccountManager�֖�����o�^����B<br>
   * 6. �j�Y�`�F�b�N���s���B<br>
   * 7. �����X�V����B<br>
   * 8. �S����(UOrder)��UBoard����UOrderManager�ֈړ�����B<br>
   * 9. �j�Y��������̒�����S�ăL�����Z������B<br>
   * 10. UPriceInfoDB�֖�艿�i��o�^����B<br>
   */
  private void makeContracts() {
    int date = fStatus.getDate();
    int boardNo = fStatus.getSession();
    removeUncontractedOrdersFromOrderManager();
    saveBoardShape();
    fBoard.makeContracts(date, boardNo);
    long price = fBoard.getContractPrice();
    // System.out.println("Contracted price is " + price);
    long volume = fBoard.getContractVolume();
    // System.out.println("Contracted volume is " + volume);
    long askedQuotation = fBoard.getAskedQuotation();
    long bidQuotation = fBoard.getAskedQuotation();
    fQuoteDB.addTodaysQuote("j30", date, boardNo, price, volume,
                            askedQuotation, bidQuotation);
    fAccountManager.setContractedPrice(price, boardNo);
    registerContractsToAccountManager();
    fAccountManager.checkBankruptForAllAccounts();
    if (!fAccountManager.checkConsistency()) {
      System.err.print("Error: Account consistency violation ");
      System.err.println("in UMart.makeContracts");
      System.err.println("Random Seed:" + URandom.getSeed());
      System.exit(5);
    }
    updateBoardInformation(price, volume);
    moveOrdersFromBoardToOrderManager();
    cancelOrdersOfBankruptedMembers();
    registerUncontractedOrdersToBoard();
    fPriceInfoDB.addCurrentPriceAndIncrementPtr(price);
  }

  /**
   * UOrderManager�̖���蒍���z��(UncontractedOrderArray)����
   * �S����(UOrder)�̎Q�Ƃ��O���܂��D
   */
  private void removeUncontractedOrdersFromOrderManager() {
    Enumeration arrays = fOrderManager.getOrderArrays();
    while (arrays.hasMoreElements()) {
      UOrderArray orderArray = (UOrderArray) arrays.nextElement();
      Vector uncontractedOrderArray = orderArray.getUncontractedOrders();
      uncontractedOrderArray.removeAllElements();
    }
  }

  /**
   * ���݂̔̌`��(�������Ɣ��������̏��)��UBoardInformation�ɕۑ����܂��D
   */
  private void saveBoardShape() {
    fBoardInformation.clear();
    Iterator orders = fBoard.getOrderArray();
    while (orders.hasNext()) {
      UOrder o = (UOrder) orders.next();
      fBoardInformation.addInformation(o.getSellBuy(), o.getMarketLimit(),
                                       o.getPrice(), o.getUncontractOrderVolume());
    }
  }

  /**
   * UAccountManager�֑S������o�^���܂��D
   */
  private void registerContractsToAccountManager() {
    Iterator orders = fBoard.getOrderArray();
    while (orders.hasNext()) {
      UOrder o = (UOrder) orders.next();
      int userID = o.getUserID();
      int sellBuy = o.getSellBuy();
      Vector contractInformationArray = o.getContractInformationArray();
      Enumeration infos = contractInformationArray.elements();
      while (infos.hasMoreElements()) {
        UContractInformation info = (UContractInformation) infos.nextElement();
        if (info.getDate() != fStatus.getDate()) {
          System.err.println("Error: info.getDate() != fDate ");
          System.err.println("in UMart.markToMarket");
          System.exit(5);
        }
        if (info.getSession() == fStatus.getSession()) {
          long volume = info.getVolume();
          if (!fAccountManager.registerContract(userID, sellBuy, volume,
                                                fStatus.getSession())) {
            System.err.print("Error: Can't append the ");
            System.err.print("contract information for ");
            System.err.println("Member" + userID);
            System.exit(5);
          }
        }
      }
    }
  }

  /**
   * ���(UBoardInformation)���X�V���܂��D
   * @param contractPrice ��艿�i
   * @param contractVolume ��萔��
   */
  private void updateBoardInformation(long contractPrice,
                                      long contractVolume) {
    fBoardInformation.setLastUpdateTime(new Date());
    fBoardInformation.setContractPrice(contractPrice);
    fBoardInformation.setContractVolume(contractVolume);
  }

  /**
   * �S����(UOrder)��UBoard����UOrderManager�ֈړ����܂��D
   */
  private void moveOrdersFromBoardToOrderManager() {
    Iterator orders = fBoard.getOrderArray();
    while (orders.hasNext()) {
      UOrder o = (UOrder) orders.next();
      fOrderManager.addOrder(o);
    }
    fBoard.clear();
  }

  /**
   * �j�Y��������̒�����S�ăL�����Z�����܂��D
   */
  private void cancelOrdersOfBankruptedMembers() {
    Enumeration accounts = fAccountManager.getBankruptedAccounts();
    while (accounts.hasMoreElements()) {
      UAccount account = (UAccount) accounts.nextElement();
      int userID = account.getUserID();
      fOrderManager.cancelAllOrdersOfMember(userID);
    }
  }

  /**
   * UOrderManager���̂��ׂĂ̖���蒍����UBoard�ɓo�^���܂��D
   */
  private void registerUncontractedOrdersToBoard() {
    Enumeration arrays = fOrderManager.getOrderArrays();
    while (arrays.hasMoreElements()) {
      UOrderArray orderArray = (UOrderArray) arrays.nextElement();
      Vector uncontractedOrderArray = orderArray.getUncontractedOrders();
      Enumeration orders = uncontractedOrderArray.elements();
      while (orders.hasMoreElements()) {
        UOrder o = (UOrder) orders.nextElement();
        fBoard.appendOrder(o);
      }
    }
  }

  /**
   * ���i�n��̃��O�t�@�C���̓��e���X�V���܂��D
   * @param path String ���i�n��̃��O�t�@�C����
   * @throws IOException
   */
  private void updatePriceInfoDBFile(String path) throws IOException {
    PrintWriter pw = new PrintWriter(new FileOutputStream(path, true));
    fPriceInfoDB.writeLatestPriceInfo(pw);
    pw.close();
  }

  /**
   * ����/����R�}���h�̗������t�@�C���ɏ����o���܂��D
   * @param path String �t�@�C����
   * @throws IOException
   */
  private void writeOrderCommandLog(String path) throws IOException {
    PrintWriter pw = new PrintWriter(new FileOutputStream(path));
    fOrderCommandLog.writeTo(pw);
    pw.close();
  }

  /**
   * ���̗������t�@�C���ɏ����o���܂��D
   * @param path String �t�@�C����
   * @param date int ��
   * @param session int ��
   * @param orderManager UOrderManager �����Ǘ���
   * @throws IOException
   */
  private void writeExecutionLog(String path, int date, int session,
                                 UOrderManager orderManager) throws IOException {
    PrintWriter pw = new PrintWriter(new FileOutputStream(path));
    UExecutionLog log = new UExecutionLog(date, session, orderManager);
    log.writeTo(pw);
    pw.close();
  }

  /**
   * �̗������t�@�C���ɏ����o���܂��D
   * @param path String �t�@�C����
   * @param date int ��
   * @param session int ��
   * @param boardInfo UBoardInformation ���
   * @throws IOException
   */
  private void writeBoardLog(String path, int date, int session,
                             UBoardInformation boardInfo) throws IOException {
    PrintWriter pw = new PrintWriter(new FileOutputStream(path));
    UBoardLog log = new UBoardLog(date, session, boardInfo);
    log.writeTo(pw);
    pw.close();
  }

  /**
   * �����̗������t�@�C���ɏ����o���܂��D
   * @param path String �t�@�C����
   * @param date int ��
   * @param session int ��
   * @param accountManager UAccountManager �����Ǘ���
   * @throws IOException
   */
  private void writeAccountLog(String path, int date, int session,
                               UAccountManager accountManager) throws IOException {
    PrintWriter pw = new PrintWriter(new FileOutputStream(path));
    UAccountLog log = new UAccountLog(date, session, accountManager);
    log.writeTo(pw);
    pw.close();
  }

  /**
   * ���O���X�V���܂��D
   * @param date ���݂̓��t
   * @param session ���݂̐�
   * @param state �}�[�P�b�g���
   */
  private void updateLog(int date, int session, int state) throws IOException {
    if (fLogFlag == UMart.NO_LOG) {
      return;
    }
    switch (state) {
      case UServerStatus.ITAYOSE:
        updatePriceInfoDBFile(fLogDir + File.separator + "price.csv");
        if (fLogFlag == UMart.DETAILED_LOG) {
          writeOrderCommandLog(fLogDir + File.separator + UMart.ORDER_LOG_DIR
                               + File.separator + "Day" + date + "Session" +
                               session
                               + "_" + UMart.ORDER_LOG_DIR + ".csv");
          fOrderCommandLog.clear();
          writeExecutionLog(fLogDir + File.separator + UMart.EXECUTION_LOG_DIR
                            + File.separator + "Day" + date + "Session" + session
                            + "_" + UMart.EXECUTION_LOG_DIR + ".csv",
                            date, session, fOrderManager);
          writeBoardLog(fLogDir + File.separator + UMart.BOARD_LOG_DIR
                        + File.separator + "Day" + date + "Session" + session
                        + "_" + UMart.BOARD_LOG_DIR + ".csv",
                        date, session, fBoardInformation);
          writeAccountLog(fLogDir + File.separator + UMart.ACCOUNT_LOG_DIR
                          + File.separator + "Day" + date + "Session" + session
                          + "_" + UMart.ACCOUNT_LOG_DIR + ".csv",
                          date, session, fAccountManager);
        } else {
          fOrderCommandLog.clear();
        }
        break;
      case UServerStatus.AFTER_MARKING_TO_MARKET:
        if (fLogFlag == UMart.DETAILED_LOG) {
          writeAccountLog(fLogDir + File.separator + UMart.ACCOUNT_LOG_DIR
                          + File.separator + "Day" + date
                          + "_MM" + "_" + UMart.ACCOUNT_LOG_DIR + ".csv",
                          date, session, fAccountManager);
        }
        break;
      case UServerStatus.AFTER_SETTLEMENT:
        updatePriceInfoDBFile(fLogDir + File.separator + "price.csv");
        writeAccountLog(fLogDir + File.separator + "Settlement_" +
                        UMart.ACCOUNT_LOG_DIR + ".csv",
                        date, session, fAccountManager);
        if (fLogFlag == UMart.DETAILED_LOG) {
          writeAccountLog(fLogDir + File.separator + UMart.ACCOUNT_LOG_DIR
                          + File.separator + "Settlement_" +
                          UMart.ACCOUNT_LOG_DIR + ".csv",
                          date, session, fAccountManager);
        }
        break;
      default:
        System.err.println("Invalid state:" + state +
                           " in UMartStandAlone.updateLog");
    }
  }

  /**
   * ������̏�Ԃ��u�l�􂢎��ԑсv�ɐݒ肵, �l�􂢏������s���܂��D
   */
  private void markToMarket() {
    fAccountManager.markToMarket();
    if (!fAccountManager.checkConsistency()) {
      System.err.print("Error: Account consistency violation ");
      System.err.println("in UMart.markToMarket");
      System.exit(5);
    }
  }

  /**
   * ������̏�Ԃ��u���Z���ԑсv�ɐݒ肵, ���Z�������s���܂��D
   * ���t��, �ŏI���Z��+1�ɐݒ肳���B<br>
   */
  private void settlement() {
    long[] spotPrice = fPriceInfoDB.getSpotPrices(1);
    fAccountManager.settlement(spotPrice[0]);
    if (!fAccountManager.checkConsistency()) {
      System.err.print("Error: Account consistency violation ");
      System.err.println("in UMart.markToMarket");
      System.exit(5);
    }
//		fPriceInfoDB.addCurrentPriceAndIncrementPtr(spotPrice[0]);
    fPriceInfoDB.setSettlementPrice();
    fQuoteDB.addTodaysQuote("j30", fMaxDate + 1, 1, spotPrice[0], 1, 0, 0);
  }

  /**
   * �ڍ׃��O�̏��������s���܂��D
   * ��̓I�ɂ́C���O���o�͂���f�B���N�g�����쐬���Cprice.csv, TimeSeriesDefinitions.csv, Member.csv���쐬����D
   * @throws FileNotFoundException
   * @throws IOException
   */
  public void initLog() throws FileNotFoundException, IOException {
    initLog(null, false);
  }

  /**
   * �o�͐�f�B���N�g�����w�肵�āC�ڍ׃��O�̏��������s���܂��D
   * @param baseDir String �o�͐�f�B���N�g����
   * @param simpleFlag boolean �ȈՃ��O�Ȃ��true, �ڍ׃��O�Ȃ��false
   * @throws FileNotFoundException
   * @throws IOException
   */
  public void initLog(String baseDir, boolean simpleFlag) throws FileNotFoundException, IOException {
    if (!makeLogDirectory(baseDir, simpleFlag)) {
      throw new IOException();
    }
    writePriceInfoDB(fLogDir + File.separator + "price.csv");
    writeTimeSeriesDefinitionLog(fLogDir + File.separator +
                                 "TimeSeriesDefinitions.csv");
    writeMemberLog(fLogDir + File.separator + "Member.csv");
    if (simpleFlag) {
      fLogFlag = UMart.SIMPLE_LOG;
    } else {
      fLogFlag = UMart.DETAILED_LOG;
    }
  }

  /**
   * PrinceInfoDB���t�@�C���ɏ����o���܂��D
   * @param path String �o�̓t�@�C����
   * @throws IOException
   */
  private void writePriceInfoDB(String path) throws IOException {
    PrintWriter pw = new PrintWriter(new FileOutputStream(path));
    fPriceInfoDB.writePriceInfoBeforeCurrentPtr(pw);
    pw.close();
  }

  /**
   * TimeSeriesDefinitionLog���t�@�C���ɏ����o���܂��D
   * @param path String �o�̓t�@�C����
   * @throws IOException
   */
  private void writeTimeSeriesDefinitionLog(String path) throws IOException {
    PrintWriter pw = new PrintWriter(new FileWriter(path));
    UTimeSeriesDefinitions timeSeriesDefinitionLog = new UTimeSeriesDefinitions();
    timeSeriesDefinitionLog.addDefinition("Default", "price.csv", "j30",
                                          fStartPoint -
                                          (Strategy.NO_OF_SPOT_PRICES - 1),
                                          fMaxDate, fNoOfSessionsPerDay);
    timeSeriesDefinitionLog.writeTo(pw);
    pw.close();
  }

  /**
   * MemberLog���t�@�C���ɏ����o���܂��D
   * @param path String �o�̓t�@�C����
   * @throws IOException
   */
  private void writeMemberLog(String path) throws IOException {
    PrintWriter pw = new PrintWriter(new FileOutputStream(path));
    fMemberLog.writeTo(pw);
    pw.close();
  }

  /**
   * ���O���o�͂���f�t�H���g�̃f�B���N�g�������쐬���ĕԂ��܂��D
   * @return String �f�B���N�g�����iUMART�{�N�{���{���{���{���{�b�{�~���b�j
   */
  private String makeDefaultDirectoryName() {
    String result = "./UMART";
    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    result += year;
    int month = cal.get(Calendar.MONTH) + 1;
    if (month < 10) {
      result += "0";
    }
    result += month;
    int day = cal.get(Calendar.DATE);
    if (day < 10) {
      result += "0";
    }
    result += day;
    int hour = cal.get(Calendar.HOUR_OF_DAY);
    if (hour < 10) {
      result += "0";
    }
    result += hour;
    int minute = cal.get(Calendar.MINUTE);
    if (minute < 10) {
      result += "0";
    }
    result += minute;
    int second = cal.get(Calendar.SECOND);
    if (second < 10) {
      result += "0";
    }
    result += second;
    int millisecond = cal.get(Calendar.MILLISECOND);
    if (millisecond < 100) {
      result += "0";
    }
    if (millisecond < 10) {
      result += "0";
    }
    result += millisecond;
    return result;
  }

  /**
   * ���O�o�͗p�̃f�B���N�g�����쐬���܂��D
   * @param baseDir String ���O�o�͗p�̃f�B���N�g�����Dnull�̏ꍇ�C�f�t�H���g�̃f�B���N�g������������܂��D
   * @param simpleFlag boolean �ȈՃ��O�Ȃ��true, �ڍ׃��O�Ȃ��false
   */
  private boolean makeLogDirectory(String baseDir, boolean simpleFlag) {
    if (baseDir == null) {
      fLogDir = makeDefaultDirectoryName();
    } else {
      fLogDir = baseDir;
    }
    File directory = new File(fLogDir);
    directory.mkdir();
    if (simpleFlag) {
      return true;
    }
    if (!makeSubDirectory(UMart.ORDER_LOG_DIR)) {
      return false;
    }
    if (!makeSubDirectory(UMart.EXECUTION_LOG_DIR)) {
      return false;
    }
    if (!makeSubDirectory(UMart.BOARD_LOG_DIR)) {
      return false;
    }
    if (!makeSubDirectory(UMart.ACCOUNT_LOG_DIR)) {
      return false;
    }
    return true;
  }

  /**
   * fLogDir�ŕ\����郍�O�o�͗p�f�B���N�g���̉��ɃT�u�f�B���N�g��name���쐬���܂��D
   * @param name �T�u�f�B���N�g�����i���΁j
   * @return �T�u�f�B���N�g���̐����ɐ��������ꍇ�Ftrue, ���s�����ꍇ�Ffalse
   */
  private boolean makeSubDirectory(String name) {
    String path = fLogDir + File.separator + name;
    File directory = new File(path);
    return directory.mkdir();
  }

  /**
   * �������ɂ���������, fOrderManager�̏����ݒ���s���܂��D
   * ����������, ���̃��\�b�h���Ăяo���O��, fAccountManager��
   * ����������������Ă���K�v������܂��D
   */
  private void setupOrderManager() {
    Enumeration accounts = fAccountManager.getAccounts();
    while (accounts.hasMoreElements()) {
      UAccount account = (UAccount) accounts.nextElement();
      fOrderManager.createOrderArray(account.getUserID());
    }
  }

  /**
   * fLoginManger�̏��������s���܂��D
   * ��̓I�ɂ́C���O�C���}�l�[�W���[�𐶐����܂��D
   * ���������āC���̃��\�b�h���Ăяo���O�ɁCfAccountManager��������
   * ����������Ă���K�v������܂��D
   */
  private void setupLoginManager() {
    fLoginManager = this.createLoginManager();
    fLoginManager.creatLoginStatus(UMart.SU_ID);
    Enumeration accounts = fAccountManager.getAccounts();
    while (accounts.hasMoreElements()) {
      UAccount account = (UAccount) accounts.nextElement();
      fLoginManager.creatLoginStatus(account.getUserID());
    }
  }

  /**
   * �l�b�g���[�N���p�܂��̓X�^���h�A�������p�̃��O�C���}�l�[�W���𐶐����܂��D
   * @return ���O�C���}�l�[�W��
   */
  protected abstract ULoginManager createLoginManager();

  /**
   * ������̏�Ԃ��u������t���ԑсv�ɐݒ肵, �G�[�W�F���g���Ƃ�
   * �������󂯂��܂��D
   */
  public void recieveOrdersFromLocalAgents() {
    Iterator itr = fStrategyArray.iterator();
    while (itr.hasNext()) {
      UBaseAgent strategy = (UBaseAgent) itr.next();
      // System.err.println(strategy.getLoginName());
      int date = fStatus.getDate();
      int boardNo = fStatus.getSession();
      int state = fStatus.getState();
      strategy.doActions(date, boardNo, state, fMaxDate, fNoOfSessionsPerDay);
    }
  }

}
