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
import java.util.*;

/**
 * ������ƑS�Ă̈�ʃ��[�U�[�̌������Ǘ�����N���X�ł��D
 */
public class UAccountManager {

  /** �񂹂ŉ��i�����܂�Ȃ������ꍇ�Ɏg����萔 */
  public static final long UNCONTRACTED_PRICE = -1;

  /** ������̌��� */
  private UExchangeAccount fExchangeAccount;

  /** ��ʃ��[�U�[�̌����̊i�[����Ă���x�N�^ */
  private Vector fAccountArray;

  /** �j�Y�������[�U�[�̌����̊i�[����Ă���x�N�^ */
  private Vector fBankruptedAccountArray;

  /** ���������̔񂹉� */
  private int fNoOfBoardsPerDay;

  /** �O���̏I�l */
  private long fYesterdayClosingPrice;

  /** �ŐV�̖�艿�i */
  private long fLatestContractedPrice;

  /** �����̖�艿�i (�񂹉񐔕��̖�艿�i���i�[����Ă��� */
  private long fContractedPrice[];

  /**
   * UAccountManager�����������܂��D
   * @param members �S�Ẳ�����
   * @param noOfBoardsPerDay ���������̔񂹉�
   */
  public UAccountManager(UMemberList members, int noOfBoardsPerDay) {
    fExchangeAccount = new UExchangeAccount();
    fAccountArray = new Vector();
    fBankruptedAccountArray = new Vector();
    fYesterdayClosingPrice = fLatestContractedPrice = -1;
    fNoOfBoardsPerDay = noOfBoardsPerDay;
    fContractedPrice = new long[fNoOfBoardsPerDay];
    for (int i = 1; i <= fNoOfBoardsPerDay; ++i) {
      fContractedPrice[i - 1] = -1;
    }
    int userID = 1;
    Iterator memberItr = members.getMembers();
    while (memberItr.hasNext()) {
      HashMap memberInfo = (HashMap)memberItr.next();
      UAccount account = new UAccount(userID, memberInfo, noOfBoardsPerDay);
      fAccountArray.add(account);
      ++userID;
    }
  }

  /**
   * �����Ŏw�肳�ꂽ���[�U�[���ƃp�X���[�h�������[�U�[(�������
   * �܂�)�̃��[�U�[ID��Ԃ��܂��D����, �Y�����郆�[�U�[�����݂��Ȃ����
   * -1��Ԃ��܂��D
   * @param userName ���[�U�[��
   * @param passwd �p�X���[�h
   * @return ���[�U�[ID�B�����Y�����郆�[�U�[�����݂��Ȃ����, -1�B
   */
  public int getUserID(String userName, String passwd) {
    if (userName.equals(fExchangeAccount.getUserName()) &&
        passwd.equals(fExchangeAccount.getPasswd())) {
      return fExchangeAccount.getUserID();
    }
    Enumeration accounts = fAccountArray.elements();
    while (accounts.hasMoreElements()) {
      UAccount account = (UAccount) accounts.nextElement();
      if (userName.equals(account.getUserName()) &&
          passwd.equals(account.getPasswd())) {
        return account.getUserID();
      }
    }
    return -1;
  }

  /**
   * �����Ŏw�肳�ꂽ���[�U�[ID�������[�U�[�̌�����Ԃ��܂��D
   * ������, �w�肳�ꂽ���[�U�[ID����������������Ȃ����null��Ԃ��܂��D
   * @param id ���[�U�[ID
   * @return �����I�u�W�F�N�g�B�w�肳�ꂽ���[�U�[ID�����������Ȃ��ꍇ��
   *         null�B
   */
  public UAccount getAccount(int id) {
    Enumeration e = fAccountArray.elements();
    while (e.hasMoreElements()) {
      UAccount account = (UAccount) e.nextElement();
      if (id == account.getUserID()) {
        return account;
      }
    }
    return null;
  }

  /**
   * �S�Ẵ��[�U�[�̌����I�u�W�F�N�g(UAccount)��񋓂��邽�߂�
   * Enumeration��Ԃ��܂��D
   * @return �S�Ẵ��[�U�[�̌����I�u�W�F�N�g(UAccount)��񋓂��邽�߂�
   *         Enumeration
   */
  public Enumeration getAccounts() {
    return fAccountArray.elements();
  }

  /**
   * ������̌�����Ԃ��܂��D
   * @return ������̌���
   */
  public UExchangeAccount getExchangeAccount() {
    return fExchangeAccount;
  }

  /**
   * �j�Y�������[�U�[�̌���(UAccount)��񋓂��邽�߂�
   * Enumeration��Ԃ��܂��D
   * @return �j�Y�������[�U�[�̌���(UAccount)��񋓂��邽�߂�Enumeration
   */
  public Enumeration getBankruptedAccounts() {
    return fBankruptedAccountArray.elements();
  }

  /**
   * ��������Ԃ��܂��D
   * @return ������
   */
  public int getNoOfAccounts() {
    return fAccountArray.size();
  }

  /**
   * ���������o�͂��܂��D
   * @param pw �o�͐�
   */
  public void printOn(PrintWriter pw) {
    try {
      pw.println("fNoOfBoards = " + fNoOfBoardsPerDay);
      pw.println("fYesterdayClosingPrice = " + fYesterdayClosingPrice);
      pw.println("fLatestContractedPrice = " + fLatestContractedPrice);
      for (int i = 1; i <= fNoOfBoardsPerDay; i++) {
        pw.println("fContractedPrice[" + i + "] = " + fContractedPrice[i - 1]);
      }
      pw.println("< Acount of echange >");
      fExchangeAccount.printOn(pw);
      pw.println("Size of fAccountArray = " + fAccountArray.size());
      Enumeration e = fAccountArray.elements();
      while (e.hasMoreElements()) {
        UAccount account = (UAccount) e.nextElement();
        pw.println("< Acount of member >");
        account.printOn(pw);
      }
      pw.print("\n");
    } catch (Exception e) {
      System.out.println("Exception: " + e);
    }
  }

  /**
   * ������o�^����B�o�^�Ɠ�����, �萔���̌v�Z�Ǝx����,
   * �|�W�V�����̍Čv�Z, �ؓ����̍Čv�Z���s����B�����Ŏw�肵��
   * ���[�U�[ID�̌��������łɔj�Y���Ă���ꍇ�͉���������false��Ԃ��܂��D
   * ������, ���̃��\�b�h���Ăяo���O��, setContractedPrice���\�b�h��
   * �Ăяo���Ė�艿�i��ݒ肵�Ă����K�v������܂��D
   * �܂�, ���̃��\�b�h�͔j�Y�̃`�F�b�N�͍s���Ȃ��̂�,
   * �S�Ă̖�����o�^�������, checkBankruptForAllAccounts���\�b�h��
   * �Ăяo���Ĕj�Y�̃`�F�b�N����я������s���K�v������܂��D
   * @param userID ��肵�������̃��[�U�[ID
   * @param sellbuy �����敪
   * @param volume ��萔��
   * @param boardNo ��肵���Ƃ��̔񂹉�
   * @return true: �w�肵�����[�U�[ID�̌���������������̓o�^��
   *         ���������ꍇ, false: �w�肵�����[�U�[ID�̌����������炸
   *         �����̓o�^�Ɏ��s�����ꍇ
   */
  public boolean registerContract(int userID, int sellbuy,
                                  long volume, int boardNo) {
    UAccount account = getAccount(userID);
    if (account.getStatus() == UAccount.UNAVAILABLE) {
      return false;
    }
    UPosition userPosition = account.getPosition();
    returnMarginToMember(account);
    payFeeToExchange(account, volume);
    if (sellbuy == UOrder.SELL) {
      userPosition.addToTodaySellPosition(volume, boardNo);
    } else if (sellbuy == UOrder.BUY) {
      userPosition.addToTodayBuyPosition(volume, boardNo);
    } else {
      System.err.println("Error in Contract! ");
      System.exit( -1);
    }
    payMarginToExchange(account);
    checkLoan(account);
    return true;
  }

  /**
   * �j�Y���Ă��Ȃ��S�Ă̌����̔j�Y�`�F�b�N���s��, �K�v�Ȃ��
   * �j�Y�������s���܂��D
   */
  public void checkBankruptForAllAccounts() {
    Enumeration accounts = fAccountArray.elements();
    while (accounts.hasMoreElements()) {
      UAccount account = (UAccount) accounts.nextElement();
      if (account.getStatus() == UAccount.AVAILABLE) {
        checkBankrupt(account);
      }
    }
  }

  /**
   * �S�Ă̌����̑Ó������`�F�b�N���܂��D�`�F�b�N���ڂ͈ȉ��̂Ƃ���ł�:<br>
   * 1. ��������܂ߑS�Ẳ���̔���|�W�V�����Ɣ����|�W�V������
   * ���a�͓������B<br>
   * 2. �S�Ăۗ̕L�����c���̑��a��su���܂ߏ�����ԂƓ����ł���B<br>
   * @return true: O.K., false: N.G.
   */
  public boolean checkConsistency() {
    Enumeration e = fAccountArray.elements();
    long totalSellPosition = fExchangeAccount.getSellPosition();
    long totalBuyPosition = fExchangeAccount.getBuyPosition();
    long totalCash = fExchangeAccount.getCash();
    // System.out.println("fExchangeAccount:" + totalCash);
    long totalInitialCash = 0;
    while (e.hasMoreElements()) {
      UAccount account = (UAccount) e.nextElement();
      totalSellPosition += this.getTotalSellPosition(account);
      totalBuyPosition += this.getTotalBuyPosition(account);
      UBalance balance = account.getTodayBalance();
      balance.updateCash();
      totalCash += balance.getCash();
      totalInitialCash += balance.getInitialCash();
    }
    /*
         System.out.println("No. of total sell positions: " + totalSellPosition
                       + ", No. of total buy positions: " + totalBuyPosition);
     */
    if (totalSellPosition != totalBuyPosition) {
      System.err.println("Position Check Error!");
      System.err.println("No. of total sell positions: " + totalSellPosition
                         + ", No. of total buy positions: " + totalBuyPosition);
      return false;
    } else {
      // System.out.println("Position Check OK!");
    }
    /*
     System.out.println("Total cash: " + totalCash + ", Total initial cash: " +
                       totalInitialCash);
     */
    if (totalInitialCash != totalCash) {
      System.err.println("Total Cash Check Error!");
      System.err.println("Total cash: " + totalCash + ", Total initial cash: " +
                         totalInitialCash);
      return false;
    } else {
      // System.out.println("Total Cash Check OK!");
    }
    return true;
  }

  /**
   * �l�􂢏������������s���܂��D
   */
  public void markToMarket() {
    long closingPrice = fLatestContractedPrice;
    Enumeration e = fAccountArray.elements();
    while (e.hasMoreElements()) {
      UAccount account = (UAccount) e.nextElement();
      if (account.getStatus() == UAccount.AVAILABLE) {
        long profitDiff = calculateUnrealizedProfit(account, closingPrice);
        moveUnrealizedProfitFromExchangeToMember(account, profitDiff);
        account.getPosition().collect();
        long todayInterest = calculateTodayInterest(account);
        payInterestFromMemberToExchange(account, todayInterest);
        checkLoan(account);
        checkBankrupt(account);
//				account.backup();
      }
    }
  }

  /**
   * �S����̓����̎��x���̃o�b�N�A�b�v���Ƃ�܂��D
   */
  public void backupAllBalances(int step) {
    Enumeration e = fAccountArray.elements();
    while (e.hasMoreElements()) {
      UAccount account = (UAccount) e.nextElement();
      account.backup(step);
    }
  }

  /**
   * �ŏI���Ϗ������s���܂��D
   * @param tomorrowSpotPrice �ŏI���ω��i(�ŏI�������̌������i)
   */
  public void settlement(long tomorrowSpotPrice) {
    fLatestContractedPrice = tomorrowSpotPrice;
    for (int i = 1; i <= fNoOfBoardsPerDay; ++i) {
      fContractedPrice[i - 1] = -1;
    }
    Enumeration e = fAccountArray.elements();
    while (e.hasMoreElements()) {
      UAccount account = (UAccount) e.nextElement();
      UBalance balance = account.getTodayBalance();
      long profit = calculateProfit(account, tomorrowSpotPrice);
      long unrealizedProfit = account.getTodayBalance().getUnrealizedProfit();
      /*
       System.out.println("ID: " + account.getUserID() + ", Final profit = " +
                         (profit + unrealizedProfit));
       */
      balance.setProfit(profit + unrealizedProfit);
      balance.setUnrealizedProfit(0);
      fExchangeAccount.addCash( -profit);
      payFeeToExchange(account,
                       Math.abs(getTotalSellPosition(account) -
                                getTotalBuyPosition(account)));
      returnMarginToMember(account);
      account.clearPosition();
    }
    fExchangeAccount.clearPosition();
  }

  /**
   * �O���̏I�l���X�V���܂��D������, �����̍ŏI�̔񂹂Ŏ������
   * ���������ɏI�l�����Ȃ������ꍇ�͒l�����Ă��钼�߂̖�艿�i��
   * �O���̏I�l�Ƃ��܂��D
   */
  public void updateYesterdayClosingPrice() {
    for (int i = fNoOfBoardsPerDay; i >= 1; --i) {
      if (fContractedPrice[i - 1] >= 0) {
        fYesterdayClosingPrice = fContractedPrice[i - 1];
        return;
      }
    }
  }

  /**
   * ��艿�i��ݒ肵�܂��DregisterContract���\�b�h���Ăяo���O��,
   * �K����x�Ăяo���K�v������܂��D
   * @param price ��艿�i
   * @param boards �񂹉�
   */
  public void setContractedPrice(long price, int boards) {
    fContractedPrice[boards - 1] = price;
    if (price >= 0) {
      fLatestContractedPrice = price;
    }
  }

  /**
   * �����������Ŏw�肳�ꂽ���[�U�[���������֎x�����܂��D
   * ���̃��\�b�h��markToMarket���\�b�h������Ăяo����܂��D
   * @param account �ΏۂƂȂ����
   * @param todayInterest �x���������
   */
  private void payInterestFromMemberToExchange(UAccount account,
                                               long todayInterest) {
    UBalance balance = account.getTodayBalance();
    balance.setSumOfInterest(balance.getSumOfInterest() + todayInterest);
    fExchangeAccount.addCash(todayInterest);
  }

  /**
   * ���������vprofitDiff���������������Ŏw�肳�ꂽ��ʃ��[�U�[��
   * �����ֈړ�����BmarkToMarket���\�b�h����Ăяo����܂��D
   * @param account �ΏۂƂȂ����
   * @param profitDiff �ړ�����関�������v
   */
  private void moveUnrealizedProfitFromExchangeToMember(
      UAccount account,
      long profitDiff) {
    UBalance balance = account.getTodayBalance();
    long newUnrealizedProfit = profitDiff + balance.getUnrealizedProfit();
    balance.setUnrealizedProfit(newUnrealizedProfit);
    fExchangeAccount.addCash( -profitDiff);
    balance.updateCash();
  }

  /**
   * �����Ŏw�肳�ꂽ�����Ɋւ���, �������̋������v�Z���܂��D
   * @param account �ΏۂƂȂ����
   * @return �������̋���
   */
  private long calculateTodayInterest(UAccount account) {
    return (long) (account.getTodayBalance().getLoan() * account.getInterest());
  }

  /**
   * �؋������������������Ŏw�肳�ꂽ�����ɖ߂��܂��D
   * @param account �ΏۂƂȂ����
   */
  private void returnMarginToMember(UAccount account) {
    UBalance balance = account.getTodayBalance();
    long margin = balance.getMargin();
    balance.setMargin(0);
    fExchangeAccount.addCash( -margin);
  }

  /**
   * �����Ŏw�肳�ꂽ��������؋�����������֎x�����܂��D
   * @param account �ΏۂƂȂ����
   */
  private void payMarginToExchange(UAccount account) {
    UBalance balance = account.getTodayBalance();
    long margin = calculateMargin(account);
    balance.setMargin(margin);
    fExchangeAccount.addCash(margin);
  }

  /**
   * �����Ŏw�肳�ꂽ�����̏؋������v�Z���܂��D
   * @param account �ΏۂƂȂ����
   * @return �؋���
   */
  private long calculateMargin(UAccount account) {
    return account.getMarginRate() *
        Math.abs(getTotalSellPosition(account) - getTotalBuyPosition(account));
  }

  /**
   * �����Ŏw�肳�ꂽ��������������volume���̎萔�����x�����܂��D
   * @param account �ΏۂƂȂ����
   * @param volume �����
   */
  private void payFeeToExchange(UAccount account, long volume) {
    UBalance balance = account.getTodayBalance();
    long fee = account.getFeePerUnit() * volume;
    fExchangeAccount.addCash(fee);
    fee += balance.getSumOfFee();
    balance.setSumOfFee(fee);
  }

  /**
   * �����Ŏw�肳�ꂽ�����ɂ��Ď��������value�����ؓ���܂��D
   * ������, �ؓ��z�̍��v�͎ؓ�����x�z���z���邱�Ƃ͂ł��܂���D
   * @param account �ΏۂƂȂ����
   * @param value �ؓ��z
   */
  private void borrowLoanFromExchange(UAccount account, long value) {
    UBalance balance = account.getTodayBalance();
    long totalLoan = balance.getLoan();
    if ( (totalLoan + value) > account.getMaxLoan()) {
      value = account.getMaxLoan() - totalLoan;
    }
    totalLoan += value;
    balance.setLoan(totalLoan);
    balance.updateCash();
    fExchangeAccount.addCash( -value);
  }

  /**
   * �����Ŏw�肳�ꂽ��������������value�����ԍς��܂��D
   * @param account �ΏۂƂȂ����
   * @param value �ԍϊz
   */
  private void repayLoanToExchange(UAccount account, long value) {
    UBalance balance = account.getTodayBalance();
    long totalLoan = balance.getLoan();
    if (0 >= totalLoan) {
      System.err.println("NoLoan error! \n");
      System.exit(5);
    }
    totalLoan -= value;
    balance.setLoan(totalLoan);
    balance.updateCash();
    fExchangeAccount.addCash(value);
  }

  /**
   * �����Ŏw�肳�ꂽ�����ɂ���, ���������}�C�i�X�Ȃ��
   * borrowLoanFromExchange���\�b�h���Ăяo�����Ƃɂ�菊������
   * �[���ɂȂ�悤�ɂ�, ���������v���X�Ŏؓ���������Ȃ��
   * repayLoanToExchange���\�b�h���Ăяo�����Ƃɂ�菊�����̋���������
   * �ؓ������[���ɂ���悤�Ɏ�����ɕԍς��܂��D
   * @param account �ΏۂƂȂ����
   */
  private void checkLoan(UAccount account) {
    UBalance balance = account.getTodayBalance();
    balance.updateCash();
    long cash = balance.getCash();
    if (0 > cash) {
      long loanValue = Math.min(account.getMaxLoan(), Math.abs(cash));
      borrowLoanFromExchange(account, loanValue);
    } else if (0 < balance.getLoan()) {
      long repayValue = Math.min(cash, balance.getLoan());
      repayLoanToExchange(account, repayValue);
    }
  }

  /**
   * �����Ŏw�肳�ꂽ�����ɂ��Ĕj�Y�������s���܂��D
   * ��̓I�ɂ�, �������~�ɂ�����, �ۗL�|�W�V������������ֈړ�, �j�Y
   * �������[�U�[�̌������i�[���Ă���x�N�^�ɓo�^���܂��D
   * @param account �ΏۂƂȂ����
   */
  private void bankrupt(UAccount account) {
    System.out.println("<<<<<<<< Member" + account.getUserID() +
                       " is bankrupted!!! >>>>>>>>>");
    // UBalance balance = account.getTodayBalance();
    account.setStatus(UAccount.UNAVAILABLE);
    fExchangeAccount.addSellPosition(getTotalSellPosition(account));
    fExchangeAccount.addBuyPosition(getTotalBuyPosition(account));
    account.clearPosition();
    fBankruptedAccountArray.addElement(account);
  }

  /**
   * �����Ŏw�肳�ꂽ�����ɂ��Ĕj�Y���Ă��邩�ǂ�������,
   * �K�v�Ȃ��bankrupt���\�b�h���Ăяo�����Ƃɂ��j�Y�������s���܂��B
   * @param account �ΏۂƂȂ����
   */
  private void checkBankrupt(UAccount account) {
    UBalance balance = account.getTodayBalance();
    balance.updateCash();
    long cash = balance.getCash();
    if (0 > cash) {
      bankrupt(account);
    }
  }

  /**
   * �����Ŏw�肳�ꂽ�����̑�����|�W�V������Ԃ��܂��D
   * @param account �ΏۂƂȂ����
   * @return ������|�W�V����
   */
  private long getTotalSellPosition(UAccount account) {
    UPosition userPosition = account.getPosition();
    long totalSellPosition = userPosition.getSumOfSellPositionUntilYesterday();
    for (int i = 1; i <= userPosition.getNoOfSessionsPerDay(); i++) {
      totalSellPosition += userPosition.getTodaySellPosition(i);
    }
    return totalSellPosition;
  }

  /**
   * �����Ŏw�肳�ꂽ�����̑������|�W�V������Ԃ��܂��D
   * @param account �ΏۂƂȂ����
   * @return �������|�W�V����
   */
  private long getTotalBuyPosition(UAccount account) {
    UPosition userPosition = account.getPosition();
    long totalBuyPosition = userPosition.getSumOfBuyPositionUntilYesterday();
    for (int i = 1; i <= userPosition.getNoOfSessionsPerDay(); i++) {
      totalBuyPosition += userPosition.getTodayBuyPosition(i);
    }
    return totalBuyPosition;
  }

  /**
   * �����Ŏw�肳�ꂽ�����ɂ��Ė��������v���v�Z���܂��D
   * @param account �ΏۂƂȂ����
   * @param closingPrice �I�l
   * @return ���������v
   */
  private long calculateUnrealizedProfit(UAccount account, long closingPrice) {
    UPosition userPosition = account.getPosition();
    long value_a = userPosition.getSumOfBuyPositionUntilYesterday()
        * (closingPrice - fYesterdayClosingPrice)
        - userPosition.getSumOfSellPositionUntilYesterday()
        * (closingPrice - fYesterdayClosingPrice);
    long value_b = 0;
    for (int i = 1; i <= userPosition.getNoOfSessionsPerDay(); i++) {
      value_b += userPosition.getTodayBuyPosition(i)
          * (closingPrice - fContractedPrice[i - 1])
          - userPosition.getTodaySellPosition(i)
          * (closingPrice - fContractedPrice[i - 1]);
    }
    return account.getTradingUnit() * (value_a + value_b);
  }

  /**
   * �����Ŏw�肳�ꂽ�����ɂ��Ď������v���v�Z���܂��D
   * @param account �ΏۂƂȂ����
   * @param settlementPrice �ŏI���ω��i
   * @return �������v
   */
  private long calculateProfit(UAccount account, long settlementPrice) {
    UPosition userPosition = account.getPosition();
    long value_a = userPosition.getSumOfBuyPositionUntilYesterday()
        * (settlementPrice - fYesterdayClosingPrice)
        - userPosition.getSumOfSellPositionUntilYesterday()
        * (settlementPrice - fYesterdayClosingPrice);
    long profit = account.getTradingUnit() * value_a;
    return profit;
  }

  /**
   * �e�X�g�p�̃��C�����\�b�h�ł��D
   */
  public static void main(String args[]) {
    try {
      FileWriter fw = new FileWriter("test01");
      BufferedWriter bw = new BufferedWriter(fw);
      PrintWriter w = new PrintWriter(bw);
      UMemberList members = new UMemberList();
      members.appendMember("member1", "passwd1", "Machine", "Local",
                           new ArrayList(),
                           "member1", new ArrayList(), 1, 1000000000,
                           1000, 10000, 300000, 30000000, 0.1);
      members.appendMember("member2", "passwd2", "Machine", "Local",
                           new ArrayList(),
                           "member2", new ArrayList(), 1, 1000000000,
                           1000, 10000, 300000, 30000000, 0.1);
      members.appendMember("member3", "passwd3", "Machine", "Local",
                           new ArrayList(),
                           "member3", new ArrayList(), 1, 1000000000,
                           1000, 10000, 300000, 30000000, 0.1);
      UAccountManager uam = new UAccountManager(members, 4);
      Random r = new Random();
      uam.checkConsistency();
      for (int day = 1; day <= 120; ++day) {
        for (int board = 1; board <= 4; ++board) {
          System.out.println("****** Day " + day + ", Board " + board +
                             " *****");
          w.write("****** Day " + day + ", Board " + board + " *****\n");
          long price = 3000 + (Math.abs(r.nextInt()) % 500) - 250;
          uam.setContractedPrice(price, board);
          boolean flag = false;
          for (int i = 0; i < 10; ++i) {
            int id1 = Math.abs(r.nextInt()) % 3 + 1;
            int id2 = Math.abs(r.nextInt()) % 3 + 1;
            while (id1 == id2) {
              id2 = Math.abs(r.nextInt()) % 3 + 1;
            }
            UAccount a1 = uam.getAccount(id1);
            if (a1 == null) {
              System.err.println("Can't find account a1:" + id1);
              System.exit(5);
            }
            UAccount a2 = uam.getAccount(id2);
            if (a2 == null) {
              System.err.println("Can't find account a2:" + id2);
              System.exit(5);
            }
            if (a1.getStatus() == UAccount.AVAILABLE
                && a2.getStatus() == UAccount.AVAILABLE) {
              long volume = Math.abs(r.nextInt()) % 100 + 1;
              uam.registerContract(id1, UOrder.SELL, volume, board);
              uam.registerContract(id2, UOrder.BUY, volume, board);
              flag = true;
            }
          }
          if (!flag) {
            uam.setContractedPrice( -1, board);
          }
          uam.checkBankruptForAllAccounts();
          uam.checkConsistency();
          uam.printOn(w);
        }
        System.out.println("****** Day " + day + ", Marking to market ******");
        w.write("****** Day " + day + ", Marking to market *****\n");
        uam.markToMarket();
        uam.checkConsistency();
        uam.printOn(w);
        uam.updateYesterdayClosingPrice();
      }
      System.out.println("****** Settlement ******");
      w.write("****** Settlement ******\n");
      uam.settlement(3200);
      uam.checkConsistency();
      uam.printOn(w);
      fw.close();
      System.out.println("******** TEST END ********\n");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
