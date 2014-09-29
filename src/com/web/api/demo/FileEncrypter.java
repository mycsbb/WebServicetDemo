package com.web.api.demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class FileEncrypter extends JFrame {
	JTextArea jTextArea1 = new JTextArea();
	TitledBorder titledBorder1;
	TitledBorder titledBorder2;
	JRadioButton jRBEncry = new JRadioButton();
	JRadioButton jRBjEncry = new JRadioButton();
	JLabel jLabel1 = new JLabel();
	JButton jBSFile = new JButton();
	JButton jBtFile = new JButton();
	JTextField jTFsfile = new JTextField();
	JTextField jTFtfile = new JTextField();
	JComboBox jCBNb = new JComboBox();// 设置下拉框；
	JComboBox jCBNk = new JComboBox();
	JLabel jLabel2 = new JLabel();
	JLabel jLabel3 = new JLabel();
	JLabel jLabel4 = new JLabel();
	JLabel jLabel5 = new JLabel();
	JPasswordField jPasswordField = new JPasswordField();
	JButton jButtonEnsure = new JButton();
	JButton jButtonCancel = new JButton();

	JTextArea showProcess = new JTextArea();

	AES myAES = new AES();
	String encryfile, savefile, Epassword, safile;
	String PathName, DirFileName;

	String filename, savefilename;
	String exname = "";
	String temp = "";
	String password;

	String sNb, sNr;
	int Nb, Nr;

	public FileEncrypter() {
		try {
			display();
			action();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void display() throws Exception {
		int WIDTH = 480;
		int HEIGHT = 550;

		titledBorder1 = new TitledBorder("");
		titledBorder2 = new TitledBorder("");
		jTextArea1.setBackground(SystemColor.control);
		jTextArea1.setEnabled(true);
		jTextArea1.setFont(new java.awt.Font("MonoSpaced", 0, 12));
		jTextArea1.setForeground(new Color(104, 180, 50));
		jTextArea1.setBorder(BorderFactory.createLoweredBevelBorder());
		jTextArea1.setMinimumSize(new Dimension(0, 18));
		jTextArea1.setPreferredSize(new Dimension(0, 18));
		jTextArea1.setCaretColor(Color.black);
		jTextArea1.setEditable(false);
		jTextArea1.setMargin(new Insets(0, 0, 0, 0));
		jTextArea1.setSelectedTextColor(Color.white);
		jTextArea1.setSelectionEnd(0);
		jTextArea1.setText("本程序使用AES（高级加密标准）算法加密文件。");
		jTextArea1.setLineWrap(true);
		jTextArea1.setRows(3);
		jTextArea1.setTabSize(8);
		jTextArea1.setBounds(new Rectangle(21, 13, 426, 46));// 矩形左上角点的横纵坐标，后面两个参数是宽和高

		showProcess.setBackground(SystemColor.control);
		showProcess.setEnabled(true);
		showProcess.setFont(new java.awt.Font("MonoSpaced", 0, 12));
		showProcess.setForeground(new Color(104, 180, 50));
		showProcess.setBorder(BorderFactory.createLoweredBevelBorder());// BevelBorder.LOWERED是边界的一种
		showProcess.setMinimumSize(new Dimension(0, 18));
		showProcess.setPreferredSize(new Dimension(426, 150));
		showProcess.setCaretColor(Color.black);
		showProcess.setEditable(false);
		showProcess.setMargin(new Insets(0, 0, 0, 0));
		showProcess.setSelectedTextColor(Color.white);
		showProcess.setSelectionEnd(0);
		showProcess.setText("加（解）密过程：");
		showProcess.setLineWrap(true);// 设置自动换行,自动换行则不会出现横向的滚动条
		showProcess.setRows(3);
		showProcess.setTabSize(8);
		showProcess.setBounds(new Rectangle(21, 340, 426, 150));// setBounds(int
																// x, int y, int
																// width, int
																// height)
		// 前两个是组件左上角在容器中的坐标
		this.getContentPane().setLayout(null);// 手动的设置组件的坐标位置和大小；
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("文件加密器");
		jRBEncry.setFont(new java.awt.Font("Dialog", 0, 12));
		jRBEncry.setText("   加密");
		jRBEncry.setBounds(new Rectangle(147, 69, 76, 22));
		jRBjEncry.setFont(new java.awt.Font("Dialog", 0, 12));
		jRBjEncry.setText("   解密");
		jRBjEncry.setBounds(new Rectangle(289, 71, 82, 19));
		jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
		jLabel1.setText("选择执行操作：");// *;
		jLabel1.setBounds(new Rectangle(41, 66, 86, 28));
		jBSFile.setBounds(new Rectangle(34, 148, 86, 26));
		jBSFile.setFont(new java.awt.Font("Dialog", 0, 12));
		jBSFile.setText("源文件");
		jBtFile.setBounds(new Rectangle(34, 185, 86, 26));
		jBtFile.setFont(new java.awt.Font("Dialog", 0, 12));
		jBtFile.setText("目标文件");
		jTFsfile.setFont(new java.awt.Font("Dialog", 0, 12));
		jTFsfile.setText("");
		jTFsfile.setBounds(new Rectangle(147, 148, 300, 26));
		jTFtfile.setFont(new java.awt.Font("Dialog", 0, 12));
		jTFtfile.setText("");
		jTFtfile.setBounds(new Rectangle(147, 185, 300, 26));
		jCBNb.setFont(new java.awt.Font("Dialog", 0, 12));
		jCBNb.setBounds(new Rectangle(141, 97, 62, 18));
		jCBNk.setFont(new java.awt.Font("Dialog", 0, 12));
		jCBNk.setBounds(new Rectangle(284, 97, 62, 18));
		jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
		jLabel2.setText("密钥长度");
		jLabel2.setBounds(new Rectangle(356, 95, 50, 23));
		jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
		jLabel3.setText("分组大小");
		jLabel3.setBounds(new Rectangle(209, 95, 50, 23));
		jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
		jLabel4.setText("选择加密强度：");
		jLabel4.setBounds(new Rectangle(41, 98, 86, 17));
		jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
		jLabel5.setText("设置密码：");
		jLabel5.setBounds(new Rectangle(55, 220, 69, 23));
		jPasswordField.setFont(new java.awt.Font("Dialog", 0, 12));
		jPasswordField.setText("");
		jPasswordField.setBounds(new Rectangle(147, 221, 239, 23));
		jButtonEnsure.setBounds(new Rectangle(111, 267, 89, 25));
		jButtonEnsure.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonEnsure.setText("执行");
		jButtonCancel.setBounds(new Rectangle(279, 267, 87, 23));
		jButtonCancel.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonCancel.setText("取消");

		this.getContentPane().add(jButtonCancel, null);
		this.getContentPane().add(jTextArea1, null);
		this.getContentPane().add(jLabel1, null);
		this.getContentPane().add(jLabel4, null);
		this.getContentPane().add(jBSFile, null);
		this.getContentPane().add(jTFsfile, null);
		this.getContentPane().add(jBtFile, null);
		this.getContentPane().add(jTFtfile, null);
		this.getContentPane().add(jPasswordField, null);

		this.getContentPane().add(showProcess);

		/*
		 * JScrollPane textPane = new JScrollPane(showProcess);
		 * this.getContentPane().add(textPane);
		 * textPane.setVerticalScrollBarPolicy
		 * (JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		 */
		/*
		 * JPanel jp = new JPanel();//添加一个面板
		 * 
		 * textPane.setPreferredSize(new Dimension(426,100)); jp.add(textPane);
		 * this.getContentPane().add(jp);
		 */
		// 过程显示文本框，并添加滚动条；

		this.getContentPane().add(jCBNb, null);// 明文分组长度；
		jCBNb.addItem("128");
		jCBNb.addItem("192");
		jCBNb.addItem("256");
		this.getContentPane().add(jCBNk, null);// 密钥长度
		jCBNk.addItem("128");
		jCBNk.addItem("192");
		jCBNk.addItem("256");

		this.getContentPane().add(jLabel3, null);
		this.getContentPane().add(jLabel2, null);
		this.getContentPane().add(jRBjEncry, null);
		this.getContentPane().add(jLabel5, null);
		this.getContentPane().add(jButtonEnsure, null);

		ButtonGroup group = new ButtonGroup();
		group.add(jRBEncry);
		group.add(jRBjEncry);
		jRBEncry.setSelected(true);
		this.getContentPane().add(jRBEncry, null);
		this.getContentPane().add(jRBjEncry, null);

		this.setSize(WIDTH, HEIGHT);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		this.setLocation((screenSize.width - WIDTH) / 2,
				(screenSize.height - HEIGHT) / 2);
	}

	public void action() {// 添加事件跟踪器；
		jBSFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileDialog fg = new FileDialog(FileEncrypter.this, "选择要操作的文件",
						FileDialog.LOAD);
				fg.show();
				// PathName,
				DirFileName = fg.getFile();
				filename = fg.getDirectory() + fg.getFile();
				jTFsfile.setText(filename);
				// jTFtfile.setText(filename+".aes");

				if (jRBEncry.isSelected()) {
					temp = filename.substring(filename.length() - 4);
					jTFtfile.setText(filename + ".aes");
				} else if (jRBjEncry.isSelected()) {
					jTFtfile.setText(filename.substring(0,
							filename.length() - 4));
				}

			}

		});

		jBtFile.addActionListener(new ActionListener() {// 保存文件

			public void actionPerformed(ActionEvent e) {

				FileDialog fgs = new FileDialog(FileEncrypter.this, "保存为",
						FileDialog.SAVE);
				fgs.show();
				savefilename = fgs.getDirectory() + fgs.getFile();
				jTFtfile.setText(savefilename + ".aes");
			}
		});

		jButtonEnsure.addActionListener(new ActionListener() {// 点执行后的操作
					public void actionPerformed(ActionEvent e) {
						sNb = (String) jCBNb.getSelectedItem();
						Nb = Integer.parseInt(sNb) / 32; // 获取明文分组长度
						// System.out.print(Nb);
						sNr = (String) jCBNb.getSelectedItem();// 获得密钥长度
						Nr = Integer.parseInt(sNr) / 32;
						password = jPasswordField.getText(); // 获取密码
						encryfile = jTFsfile.getText(); // 加密文件路径 ，String类型
						safile = jTFtfile.getText(); // 文件解密路径

						if (jRBEncry.isSelected()) {
							try {
								myAES.AES_Encrypt(encryfile, safile, password,
										Nb, Nr);
								File f = new File(encryfile);
								f.delete();
								JOptionPane.showMessageDialog(null, "加密成功！",
										"提示", JOptionPane.OK_OPTION);

							} catch (IOException a) {
								System.out.println(a);
								// JOptionPane.showMessageDialog(
								// null,"对不起，加密失败！","提示",JOptionPane.OK_OPTION);
							}
						} else if (jRBjEncry.isSelected()) {
							try {
								myAES.AES_DeEncrypt(encryfile, safile,
										password, Nb, Nr);
								File f = new File(encryfile);
								f.delete();
								JOptionPane.showMessageDialog(null, "解密成功！",
										"提示", JOptionPane.OK_OPTION);
							} catch (IOException a) {
								System.out.println(a);
							}

						}

					}

				});

		jButtonCancel.addActionListener(new ActionListener() { // 取消按键
					public void actionPerformed(ActionEvent e) {
						FileEncrypter.this.dispose();
					}
				});

	}

	public static void main(String args[]) {
		FileEncrypter fp = new FileEncrypter();
		fp.show();
	}

}