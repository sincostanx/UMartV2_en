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
package experiments;

import java.io.*;
import java.util.*;

/**
 * ��W�͂ł̉��������Ɏg����ݒ�t�@�C���iCSV�`���j���쐬����v���O�����ł��D
 * ���̃N���X�́C�����p�N���X(Experiment1, Experiment2, Experiment3)
 * �ɂ���Ďg�p����邽�߁C���[�U�ɂ�薾���I�Ɏ��s�����K�v�͂���܂���D
 */

public class CSVCreator {
	/**
	 * �W���G�[�W�F���g��ێ����郊�X�g
	 */
	List standardAgents;
	/**
	 * �쐬�����G�[�W�F���g��ێ����郊�X�g
	 */
	List myAgents;
	/** 
	 * �����J�n���_
	 */
	Map startingPoints;
	/**
	 * �ő����
	 */
	int maxDate;
	/**
	 * �P��������̃Z�b�V������
	 */
	int numOfSessionsPerDay;
	/**
	 * �����p�f�B���N�g���i�t�H���_�j
	 */
	String dirName = "experiments/";
	/**
	 * �W���G�[�W�F���g���L�q����Ă���t�@�C����
	 */
	String stdAgentsFile = "StandardAgents.txt";
	/**
	 * �쐬�����G�[�W�F���g���L�q�����t�@�C����
	 */
	String myAgentsFile = "MyAgents.txt";
	/**
	 * ���i�n��t�@�C����
	 */
	String priceDefinitionFile = "Prices.txt";
	/**
	 * ����
	 */
	Random rand = new Random();
	/**
	 * ���O�C����
	 */
	public static final String LOGIN_NAME = "LOGIN_NAME";
	/**
	 * �N���X��
	 */
	public static final String CLASS_NAME = "CLASS_NAME";
	/**
	 * �������i�t�@�C����
	 */
	public static final String SPOT_FILENAME = "SPOT_FILENAME";
	/**
	 * �����J�n���_
	 */
	public static final String STARTING_POINT = "STARTING_POINT";

	/**
	 * ���X�g��n�b�V���}�b�v�̏��������s���܂��D
	 *
	 */
	public CSVCreator() {
		standardAgents = new ArrayList();
		myAgents = new ArrayList();
		startingPoints = new HashMap();
	}

	/**
	 * �ݒ�t�@�C�����쐬���܂��D
	 * @param dir ���\�[�X�t�@�C���̂���f�B���N�g��(�t�H���_�j
	 * @param members�@�����ɎQ������G�[�W�F���g
	 * @param spotFilename�@�����t�@�C����
	 * @param startingPoint�@����J�n���_
	 */
	private void createFiles(String dir, List members, String spotFilename,
			int startingPoint) {
		String resourceDirName = dir + "resources/";
		File resourceDir = new File(resourceDirName);
		resourceDir.mkdirs();
		createMembers(resourceDirName, members);
		createTimeSeriesDefinitions(resourceDirName, spotFilename,
				startingPoint);
		createSpotFile(resourceDirName, spotFilename);
	}

	/**
	 * �����p�G�[�W�F���g��`�t�@�C���FMembers.csv ���쐬���܂��D
	 * @param dir�@�쐬����f�B���N�g��(�t�H���_�j
	 * @param members�@�����ɗp����G�[�W�F���g�̃��X�g
	 */
	private void createMembers(String dir, List members) {
		File outputFile = new File(dir + "/Members.csv");
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(outputFile));
			pw.println("LoginName,Password,Attribute,Connection,Access(Allow_All)(CHART:BOARD:ORDER:ORDER_RESULT:PROFIT:POSITION:STDOUT),RealName,SystemParameters,Seed,InitialCash,TradingUnit,FeePerUnit,MarginRate,MaxLoan,Interest");
			Iterator i = members.iterator();
			int counter = 1;
			while (i.hasNext()) {
				HashMap info = (HashMap) i.next();
				String loginName = (String) info.get(CSVCreator.LOGIN_NAME);
				String className = (String) info.get(CSVCreator.CLASS_NAME);
				String str = loginName + ",passwd" + counter
						+ ",Machine,Local,," + className + ",,"
						+ Math.abs(rand.nextInt())
						+ ",1000000000,1000,0,300000,30000000,0.1";
				pw.println(str);
				counter++;
			}
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �����̂��߂̉��i�n���`�t�@�C�����쐬���܂��D
	 * @param dir�@�t�@�C�����쐬����f�B���N�g���i�t�H���_�j
	 * @param spotFilename�@�������i�t�@�C��
	 * @param startingPoint�@����J�n���_
	 */
	private void createTimeSeriesDefinitions(String dir, String spotFilename,
			int startingPoint) {
		File outputFile = new File(dir + "/TimeSeriesDefinitions.csv");
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(outputFile));
			pw
					.println("Nickname,Filename,BrandName,StartStep,MaxDate,NoOfSessionsPerDay");
			pw
					.println("Default,resource:" + spotFilename + ",J30,"
							+ startingPoint + "," + maxDate + ","
							+ numOfSessionsPerDay);
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �����Ŏw�肳�ꂽ�f�B���N�g��(�t�H���_�j�ɁC�����������Ŏw�肳�ꂽ�t�@�C�����ŁC
	 * �������i��ێ�����t�@�C�����쐬���܂��D
	 * @param dir �t�@�C�����쐬����f�B���N�g���i�t�H���_�j
	 * @param filename�@�t�@�C����
	 */
	private void createSpotFile(String dir, String filename) {
		File inputFile = new File(dirName + filename);
		File outputFile = new File(dir + "/" + filename);
		FileReader fr;
		FileWriter fw;
		String str = null;
		try {
			fr = new FileReader(inputFile);
			BufferedReader br = new BufferedReader(fr);
			fw = new FileWriter(outputFile);
			PrintWriter pw = new PrintWriter(fw);
			while ((str = br.readLine()) != null) {
				pw.println(str);
			}
			br.close();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �����ŗ^����ꂽ�񐔕��̎������s����悤�ȁC�����P�p�̐ݒ�t�@�C�����쐬���܂��D
	 * @param numOfExp1�@������
	 */
	public void createExp1Files(int numOfExp1) {
		Iterator i = myAgents.iterator();
		while (i.hasNext()) {
			List list = new ArrayList();
			HashMap info = (HashMap) i.next();
			list.add(info);
			Iterator k = standardAgents.iterator();
			while (k.hasNext()) {
				list.add(k.next());
			}
			// printList(list);
			String name = (String) info.get(CSVCreator.LOGIN_NAME);
			String dir = dirName + "exp1/" + name;
			Iterator j = startingPoints.keySet().iterator();
			while (j.hasNext()) {
				String situation = (String) j.next();
				HashMap spotInfo = (HashMap) startingPoints.get(situation);
				String spotFilename = (String) spotInfo.get(SPOT_FILENAME);
				int startingPoint = ((Integer) spotInfo.get(STARTING_POINT))
						.intValue();
				for (int expNo = 1; expNo <= numOfExp1; ++expNo) {
					String subDirName = dir + "_" + situation + "_" + expNo
							+ "/";
					//File directory = new File(subDirName);
					createFiles(subDirName, list, spotFilename, startingPoint);
				}
			}
		}
		System.out.println("CSV files for Exp. 1: created");
	}

	/**
	 * �����ŗ^����ꂽ�񐔕��̎������s����悤�ȁC�����Q�p�̐ݒ�t�@�C�����쐬���܂��D
	 * @param numOfExp2�@������
	 */
	public void createExp2Files(int numOfExp2) {
		Iterator i = myAgents.iterator();
		List list = new ArrayList();
		while (i.hasNext()) {
			list.add(i.next());
		}
		i = standardAgents.iterator();
		while (i.hasNext()) {
			list.add(i.next());
		}
		Iterator j = startingPoints.keySet().iterator();
		while (j.hasNext()) {
			String situation = (String) j.next();
			HashMap spotInfo = (HashMap) startingPoints.get(situation);
			String spotFilename = (String) spotInfo.get(SPOT_FILENAME);
			int startingPoint = ((Integer) spotInfo.get(STARTING_POINT))
					.intValue();
			for (int expNo = 1; expNo <= numOfExp2; ++expNo) {
				String subDirName = dirName + "exp2/" + situation
						+ "_" + expNo + "/";
				createFiles(subDirName, list, spotFilename, startingPoint);
			}
		}
		System.out.println("CSV files for Exp. 2: created");
	}

	/**
	 * �����ŗ^����ꂽ�񐔕��̎������s����悤�ȁC�����R�p�̐ݒ�t�@�C�����쐬���܂��D
	 * @param numOfExp3�@������
	 */
	public void createExp3Files(int numOfExp3) {
		for (int i = 1; i <= numOfExp3; i++) {
			List list = new ArrayList();
			Iterator itr = myAgents.iterator();
			while (itr.hasNext()) {
				list.add(itr.next());
			}
			itr = standardAgents.iterator();
			while (itr.hasNext()) {
				list.add(itr.next());
			}
			dropHalf(list);
			Iterator j = startingPoints.keySet().iterator();
			while (j.hasNext()) {
				String situation = (String) j.next();
				String subDirName = dirName + "exp3/set" + i + "_"
						+ situation + "_1/";
				HashMap spotInfo = (HashMap) startingPoints.get(situation);
				String spotFilename = (String) spotInfo.get(SPOT_FILENAME);
				int startingPoint = ((Integer) spotInfo.get(STARTING_POINT))
						.intValue();
				createFiles(subDirName, list, spotFilename, startingPoint);
			}
		}
		System.out.println("CSV files for Exp. 3: created");
	}

	/**
	 * �����ŗ^����ꂽ���X�g����v�f�̔����������_���ɍ폜���܂��D
	 * @param list
	 */
	private void dropHalf(List list) {
		int halfSize = list.size() / 2;
		for (int i = 0; i < halfSize; i++) {
			list.remove(rand.nextInt(list.size()));
		}
	}

	/**
	 * �������F�W���G�[�W�F���g�C�쐬�����G�[�W�F���g�C����ю���J�n���_��ݒ肵�܂��D
	 *
	 */
	public void initialize() {
		setAgents(stdAgentsFile, standardAgents);
		setAgents(myAgentsFile, myAgents);
		setStartingPoints(priceDefinitionFile);
	}

	/**
	 * ����J�n���_�̐ݒ���s���܂��D
	 * @param filename �t�@�C����
	 */
	private void setStartingPoints(String filename) {
		File file = new File(dirName + filename);
		FileReader fr = null;
		String str = null;
		try {
			fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			while ((str = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(str);
				if (str.startsWith("#")) {
					continue;
				} else if (str.startsWith("maxDate")) {
					st.nextToken();
					maxDate = Integer.parseInt(st.nextToken());
					continue;
				} else if (str.startsWith("numOfSessionsPerDay")) {
					st.nextToken();
					numOfSessionsPerDay = Integer.parseInt(st.nextToken());
					continue;
				}
				String situation = st.nextToken();
				String spotFilename = st.nextToken();
				// System.out.println("spotFilename: " + spotFilename);
				int startingPoint = Integer.parseInt(st.nextToken());
				HashMap info = new HashMap();
				info.put(CSVCreator.SPOT_FILENAME, spotFilename);
				info.put(CSVCreator.STARTING_POINT, new Integer(startingPoint));
				startingPoints.put(situation, info);
			}
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �����ł��Ă������t�@�C�����ɁC�����������ł��Ă������G�[�W�F���g�W�����������݂܂��D
	 * @param filename
	 * @param agentList
	 */
	private void setAgents(String filename, List agentList) {
		File file = new File(dirName + filename);
		FileReader fr = null;
		String str = null;
		try {
			fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			while ((str = br.readLine()) != null) {
				if (str.startsWith("#") || str.equals("")) {
					continue;
				}
				StringTokenizer st = new StringTokenizer(str);
				HashMap info = new HashMap();
				info.put(CSVCreator.LOGIN_NAME, st.nextToken());
				info.put(CSVCreator.CLASS_NAME, st.nextToken());
				agentList.add(info);
			}
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �����ŗ^����ꂽ���X�g�̓��e��\�����܂��D
	 * @param list
	 */
	private void printList(List list) {
		Iterator i = list.iterator();
		while (i.hasNext()) {
			System.out.println(i.next());
		}
	}

	/** 
	 * �����ŗ^����ꂽ�n�b�V���\�̓��e��\�����܂��D
	 * @param map
	 */
	private void printMap(Map map) {
		Iterator i = startingPoints.keySet().iterator();
		while (i.hasNext()) {
			Object obj = i.next();
			System.out.println(obj + ", " + startingPoints.get(obj));
		}
	}

	/**
	 * �e��ϐ��̕\���F�ő�����C�P��������̃Z�b�V�������C�W���G�[�W�F���g�C
	 * �쐬�����G�[�W�F���g�C�J�n���_�C��\�����܂��D
	 *
	 */
	private void PrintAll() {
		System.out.println("************Parameters***************");
		System.out.println("maxDate = " + maxDate);
		System.out.println("numOfSessionsPerDay = " + numOfSessionsPerDay);
		System.out.println("************Standards***************");
		printList(standardAgents);
		System.out.println("************MyAgents***************");
		printList(myAgents);
		System.out.println("************StartingPoints***************");
		printMap(startingPoints);
	}

}
