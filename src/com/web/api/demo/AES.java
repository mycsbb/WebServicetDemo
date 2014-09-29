package com.web.api.demo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class AES extends AESMap {

	public long AES_Encrypt(String OpenPath, String SavePath, String m_Key,
			int Nb, int Nk) throws IOException {
		System.out.println("\n加密文件...");
		// 以二进制读的方式打开要加密的文件；
		// 以二进制写的方式打开保存密文的文件；
		FileInputStream fp1 = new FileInputStream(OpenPath);
		FileOutputStream fp2 = new FileOutputStream(SavePath, true);
		int Length = fp1.available();// 得到要加密的文件的长度，单位bit；
		if (Length == 0)
			return 0;
		int leave = Length % (4 * Nb); // 求剩余的字块的字节数；
		long rounds = Length / (4 * Nb); // 得到整块的加密轮数；
		if (leave != 0)
			rounds++;
		long copy_rounds = rounds;// rounds可以变化不影响copy_rounds的值
		System.out.println("\n正在加密" + rounds + "个明文组");

		byte[] state = new byte[4 * 8]; // 作为加密时存放要加密的明文块，字节类型数组；
		byte[] copy = new byte[4 * 8]; // 用来进行短块处理时的缓存区；
		int Nr = GetRounds(Nb, Nk); // 得到加密的轮数，Nb为密钥矩阵的列数；
		KeyExpansion(m_Key, Nb, Nk, Nr); // 生成各轮子密钥；

		if (copy_rounds == 1 && rounds == 1) {

			if (leave == 0)
				fp1.read(state, 0, 4 * Nb);// 明文的长度恰好等于分组长度；
			else {
				fp1.read(state, 0, leave);// 明文的长度小于八个字符，每个字符一字节；
				for (int i = leave; i < 4 * Nb; i++)
					state[i] = 0; // 后面用空格补齐；
			}

			state = Transform(ByteToChar(state), Nb, Nr); // 加密变换；
			fp2.write(state, 0, 4 * Nb);// 将加密后的密文块写入目标文件；
			rounds--;
		} else if (copy_rounds > 1 && leave != 0)// 如果明文的长度大于分组长度且字符数不是分组长度的整数倍
		{ // 时，需要进行短块处理；
			fp1.read(state, 0, 4 * Nb);
			state = Transform(ByteToChar(state), Nb, Nr);// 先加密最前面的一块；
			fp2.write(state, 0, leave);// 将余数个数那么多的密文前面部分字符存入文件；
			int j = 0;
			for (int i = leave; i < 4 * Nb; i++)
				copy[j++] = state[i];// 将最先加密的密文的后部分放在短块前面，长度为分组长度减去leave；
			fp1.read(copy, j, leave);// 将剩余个数那么多的明文放在后面（是第二组明文块中的明文），组成一组完整加密明文块；
			copy = Transform(ByteToChar(copy), Nb, Nr);
			fp2.write(copy, 0, 4 * Nb);
			rounds -= 2;// 这就加密了2组了，实质是一组多；
		}
		while (rounds > 0)// 以下处理的明文是分组的整数倍的情况；
		{
			fp1.read(state, 0, 4 * Nb);
			state = Transform(ByteToChar(state), Nb, Nr);
			fp2.write(state, 0, 4 * Nb);
			rounds--;
		}
		fp1.close();// 关闭源文件和目标文件；
		fp2.close();
		return ((copy_rounds - 1) * 4 * Nb + leave);// 返回文件长度；
	}

	public long AES_DeEncrypt(String OpenPath, String SavePath, String m_Key,
			int Nb, int Nk) throws IOException {
		System.out.println("\n解密文件...");
		// 以二进制读的方式打开要解密密的文件；
		// 以二进制写的方式打开保存解开的密文文件；
		FileInputStream fp1 = new FileInputStream(OpenPath);
		FileOutputStream fp2 = new FileOutputStream(SavePath, true);
		int Length = fp1.available();// 得到要解密的文件的长度；
		if (Length == 0)
			return 0;
		int leave = Length % (4 * Nb);// 求剩余的字块的字节数；
		long rounds = Length / (4 * Nb);// 得到整块的解密轮数；
		if (leave != 0)
			rounds++;
		long copy_rounds = rounds;
		System.out.println("\n正在解密" + rounds + "个密文组");

		byte[] state = new byte[4 * 8]; // 解密时存放密文块；
		int Nr = GetRounds(Nb, Nk); // 得到解密时循环轮数；
		KeyExpansion(m_Key, Nb, Nk, Nr); // 生成各轮子密钥
		byte[] copy = new byte[32];

		if (leave != 0)// 需要进行短块处理
		{
			fp1.read(copy, 0, leave);// 先把余数个密文字符保存进数组；
			fp1.read(state, 0, 4 * Nb);// 读取紧接着的下一个密文块；
			state = ReTransform(ByteToChar(state), Nb, Nr); // 解密；
			int j = 0;
			for (int i = leave; i < 4 * Nb; i++)
				// 把解密后的明文前部分和前面的余数个合在一起组成一块，
				copy[i] = state[j++]; // 一起解密；
			copy = ReTransform(ByteToChar(copy), Nb, Nr);
			// 将解密后的明文写入目标文件；
			fp2.write(copy, 0, 4 * Nb);
			fp2.write(state, j, leave);// 将余数个明文写入目标文件；
			rounds -= 2; // 已经完成了两轮解密所以减二；
		}
		while (rounds > 0)// 对后面是分组长度的整数倍的密文块解密；
		{
			fp1.read(state, 0, 4 * Nb);// 读取密文块；
			copy = ReTransform(ByteToChar(state), Nb, Nr); // 解密变换；
			fp2.write(copy, 0, 4 * Nb);// 将解密后的明文写入目标文件；
			rounds--; // 轮数减一；
		}
		fp1.close();// 关闭源文件和目标文件；
		fp2.close();
		return ((copy_rounds - 1) * 4 * Nb + leave);// 返回文件长度
	}

	public void RotWord(char[] A)// 左移一个字节，用于密钥扩展中；
	{
		char temp;
		temp = A[0];
		A[0] = A[1];
		A[1] = A[2];
		A[2] = A[3];
		A[3] = temp;
	}

	public void SubWord(char[] A)// 字节替代，用于密钥扩展；
	{
		for (int i = 0; i < 4; i++)
			A[i] = S_BOX[A[i]];
	}

	public int GetRounds(int Nb, int Nk) {
		switch (Nb) {
		case 4:
			switch (Nk)// 四行四列的明文分组，128bit；
			{
			case 4:
				return 10;// 128bit密钥；
			case 6:
				return 12;
			case 8:
				return 14;
			default:
				return 0;
			}
		case 6:
			switch (Nk) {
			case 4:
			case 6:
				return 12;
			case 8:
				return 14;
			default:
				return 0;
			}
		case 8:
			switch (Nk) {
			case 4:
			case 6:
			case 8:
				return 14;
			default:
				return 0;
			}
		default:
			return 0;
		}
	}

	public void KeyExpansion(String m_Key, int Nb, int Nk, int Nr) {
		int i = 0;
		for (; i < 4; i++)
			for (int j = 0; j < Nk; j++)
				key[i * Nk + j] = m_Key.charAt(i * 4 + j);// key是全局变量，四行的4到4+Nk-1列存起来
		i = 0;
		for (; i < 4; i++)
			for (int j = 0; j < Nb; j++)
				key[i * j] = m_Key.charAt(i * j);// 0到 Nb-1列密码序列存起来 ，Nb一定小于Nk
		i = 0;
		while (i < Nk) {
			w[i * 4] = key[i * 4];// 第一行的第五个密钥字符，w中存扩展密钥；
			w[i * 4 + 1] = key[i * 4 + 1];
			w[i * 4 + 2] = key[i * 4 + 2];
			w[i * 4 + 3] = key[i * 4 + 3];
			i++;
		}
		i = Nk;
		while (i < Nb * (Nr + 1))// 一共需要的密钥列数为Nb*（Nr+1）
		{
			char[] temp = new char[4];
			temp[0] = w[(i - 1) * 4 + 0];
			temp[1] = w[(i - 1) * 4 + 1];
			temp[2] = w[(i - 1) * 4 + 2];
			temp[3] = w[(i - 1) * 4 + 3];
			if ((i % Nk) == 0)// 当密钥128比特是Nk为4；
			{
				RotWord(temp);
				SubWord(temp);
				for (int j = 0; j < 4; j++)
					temp[j] ^= Rcon[((i - 1) / Nk) * 4 + j];// 与常量Rcon异或；
			}
			// stop at
			// here....................................................................................................................
			else if (Nk == 8 && i % Nk == 4)
				SubWord(temp);
			w[i * 4 + 0] = (char) (w[(i - Nk) * 4 + 0] ^ temp[0]);
			w[i * 4 + 1] = (char) (w[(i - Nk) * 4 + 1] ^ temp[1]);
			w[i * 4 + 2] = (char) (w[(i - Nk) * 4 + 2] ^ temp[2]);
			w[i * 4 + 3] = (char) (w[(i - Nk) * 4 + 3] ^ temp[3]);
			i++;
		}
	}

	public void SubChar(char[] state, int Nb) {
		String STR = "";
		String output = "";
		for (int j = 0; j < 4 * Nb; j++) {
			state[j] = S_BOX[state[j] % 256];// s盒
			STR += state[j];
		}
		for (int i = 0; i < STR.length(); i++) {
			int ch = (int) STR.charAt(i);
			String s4 = Integer.toHexString(ch);
			output = output + "  0x" + s4;
		} // 0x表示十六进制

		System.out.println("经过字节替代后变为：" + output);

	}

	public void ShiftRows(char[] state, int Nb) {
		String STR = "";
		String output = "";
		char[] t = new char[8];
		for (int r = 0; r < 4; r++) {
			for (int c = 0; c < Nb; c++)
				t[c] = state[Nb * r + (r + c) % Nb];
			for (int c = 0; c < Nb; c++) {
				state[Nb * r + c] = t[c];
				STR += state[Nb * r + c];
			}
		}
		for (int i = 0; i < STR.length(); i++) {
			int ch = (int) STR.charAt(i);
			String s4 = Integer.toHexString(ch);
			output = output + "  0x" + s4;
		} // 0x表示十六进制
		System.out.println("\n经过行移位后变为：" + output);
	}

	public void MixColumns(char[] state, int Nb) {
		String STR = "";
		String output = "";
		int[] t = new int[4];
		for (int c = 0; c < Nb; c++) {
			for (int r = 0; r < 4; r++)
				t[r] = state[Nb * r + c];
			for (int r = 0; r < 4; r++) {
				state[Nb * r + c] = (char) (Ffmul(0x02, t[r])
						^ Ffmul(0x03, t[(r + 1) % 4]) ^ t[(r + 2) % 4] ^ t[(r + 3) % 4]);
				STR += state[Nb * r + c];
			}
		}
		for (int i = 0; i < STR.length(); i++) {
			int ch = (int) STR.charAt(i);
			String s4 = Integer.toHexString(ch);
			output = output + "  0x" + s4;
		} // 0x表示十六进制
		System.out.println("\n经过列混合后变为" + output);
	}

	public int Ffmul(int A, int B) {

		// 查对数表;
		if (A == 0 || B == 0)
			return 0;
		A = Log[A];
		B = Log[B];
		A = (A + B) % 0xff;
		// 查反对数表;
		A = Log_1[A];
		return A;
	}

	public void AddRoundKey(char[] state, int Nb, int round) {
		String Kkey = "";
		String STR = "";
		String output = "";
		for (int c = 0; c < Nb; c++, round++)
			for (int r = 0; r < 4; r++) {
				state[r * Nb + c] = (char) (state[r * Nb + c] ^ w[round * 4 + r]);
				Kkey += w[round * 4 + r];
				STR += state[Nb * r + c];
			}
		for (int i = 0; i < Kkey.length(); i++) {
			int ch = (int) Kkey.charAt(i);
			String s4 = Integer.toHexString(ch);
			output = output + "  0x" + s4;
		} // 0x表示十六进制
		System.out.println(output);
		for (int i = 0; i < STR.length(); i++) {
			int ch = (int) STR.charAt(i);
			String s4 = Integer.toHexString(ch);
			output = output + "  0x" + s4;
		} // 0x表示十六进制
		System.out.println("\n经过轮密钥加后变为：" + output);

	}

	public byte[] Transform(char[] state, int Nb, int Nr) {
		int round = 1;
		int l = 0;// 计算轮数；
		System.out.println("\n初始密钥为：");
		AddRoundKey(state, Nb, 0);
		for (; round < Nr; round++) {
			l++;
			SubChar(state, Nb);// 字节代替
			ShiftRows(state, Nb);// 行移位
			MixColumns(state, Nb);// 列混合
			System.out.println("\n第" + l + "轮子密钥为：");
			AddRoundKey(state, Nb, round * Nb);// 轮密钥加
		}
		SubChar(state, Nb);
		ShiftRows(state, Nb);
		AddRoundKey(state, Nb, round * Nb);
		return CharToByte(state);
	}

	public byte[] ReTransform(char[] state, int Nb, int Nr) {
		int l = 0;
		System.out.println("\n初始密钥为：");
		AddRoundKey(state, Nb, Nr * Nb);
		for (int round = Nr - 1; round >= 1; round--) {
			l++;
			InvShiftRows(state, Nb);
			InvSubint(state, Nb);
			System.out.println("\n初始密钥为：");
			AddRoundKey(state, Nb, round * Nb);
			System.out.println("\n第" + l + "轮子密钥为：");
			InvMixColumns(state, Nb);
		}
		InvShiftRows(state, Nb);
		InvSubint(state, Nb);
		AddRoundKey(state, Nb, 0);
		return CharToByte(state);
	}

	public void InvSubint(char[] state, int Nb) {
		for (int i = 0; i < 4 * Nb; i++)
			state[i] = S_BOX_1[state[i] % 256];
	}

	public void InvShiftRows(char[] state, int Nb) {
		char[] t = new char[8];
		for (int r = 0; r < 4; r++) {
			for (int c = 0; c < Nb; c++)
				t[(c + r) % Nb] = state[r * Nb + c];
			for (int c = 0; c < Nb; c++)
				state[r * Nb + c] = t[c];
		}
	}

	public void InvMixColumns(char[] state, int Nb) {
		char[] t = new char[4];
		for (int c = 0; c < Nb; c++) {
			for (int r = 0; r < 4; r++)
				t[r] = state[Nb * r + c];
			for (int r = 0; r < 4; r++) {
				state[Nb * r + c] = (char) (Ffmul(0x0e, t[r])
						^ Ffmul(0x0b, t[(r + 1) % 4])
						^ Ffmul(0x0d, t[(r + 2) % 4]) ^ Ffmul(0x09,
						t[(r + 3) % 4]));
			}
		}
	}

	public static char[] ByteToChar(byte[] data) {
		char[] A = new char[data.length];
		for (int i = 0; i < data.length; i++)
			A[i] = (char) data[i];
		return A;
	}

	/**
	 * This method is used to transform a array from char type to byte type.
	 * 
	 * @param data
	 *            a char type array.
	 * @return a byte type array.
	 */
	public static byte[] CharToByte(char[] data) {
		byte[] A = new byte[data.length];
		for (int i = 0; i < data.length; i++)
			A[i] = (byte) data[i];
		return A;
	}
}
