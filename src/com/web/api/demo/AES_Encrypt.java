package com.web.api.demo;

import java.io.FileOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.*;
import java.util.Properties;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class AES_Encrypt {
	// #include "jiami.h"

	// typedef unsigned char uchar;
	static int Nb = 4; // number of columns in the state & expanded key
	static int Nr = 10; // number of rounds in encryption
	static int Nk = 4; // number of columns in a key
	// unsigned char KEY[16] = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
	// 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
	// unsigned char EX_KEY[4*Nb*(Nr+1)];

	static char Sbox[] = { // forward s-box
	0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b,
			0xfe, 0xd7, 0xab, 0x76, 0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47,
			0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0, 0xb7, 0xfd,
			0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71,
			0xd8, 0x31, 0x15, 0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a,
			0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75, 0x09, 0x83, 0x2c,
			0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3,
			0x2f, 0x84, 0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a,
			0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf, 0xd0, 0xef, 0xaa, 0xfb,
			0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f,
			0xa8, 0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6,
			0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2, 0xcd, 0x0c, 0x13, 0xec, 0x5f,
			0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73,
			0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8,
			0x14, 0xde, 0x5e, 0x0b, 0xdb, 0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06,
			0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79, 0xe7,
			0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea,
			0x65, 0x7a, 0xae, 0x08, 0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4,
			0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a, 0x70, 0x3e,
			0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86,
			0xc1, 0x1d, 0x9e, 0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94,
			0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf, 0x8c, 0xa1, 0x89,
			0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54,
			0xbb, 0x16 };

	// combined Xtimes2[Sbox[]]
	static char Xtime2Sbox[] = { 0xc6, 0xf8, 0xee, 0xf6, 0xff, 0xd6, 0xde,
			0x91, 0x60, 0x02, 0xce, 0x56, 0xe7, 0xb5, 0x4d, 0xec, 0x8f, 0x1f,
			0x89, 0xfa, 0xef, 0xb2, 0x8e, 0xfb, 0x41, 0xb3, 0x5f, 0x45, 0x23,
			0x53, 0xe4, 0x9b, 0x75, 0xe1, 0x3d, 0x4c, 0x6c, 0x7e, 0xf5, 0x83,
			0x68, 0x51, 0xd1, 0xf9, 0xe2, 0xab, 0x62, 0x2a, 0x08, 0x95, 0x46,
			0x9d, 0x30, 0x37, 0x0a, 0x2f, 0x0e, 0x24, 0x1b, 0xdf, 0xcd, 0x4e,
			0x7f, 0xea, 0x12, 0x1d, 0x58, 0x34, 0x36, 0xdc, 0xb4, 0x5b, 0xa4,
			0x76, 0xb7, 0x7d, 0x52, 0xdd, 0x5e, 0x13, 0xa6, 0xb9, 0x00, 0xc1,
			0x40, 0xe3, 0x79, 0xb6, 0xd4, 0x8d, 0x67, 0x72, 0x94, 0x98, 0xb0,
			0x85, 0xbb, 0xc5, 0x4f, 0xed, 0x86, 0x9a, 0x66, 0x11, 0x8a, 0xe9,
			0x04, 0xfe, 0xa0, 0x78, 0x25, 0x4b, 0xa2, 0x5d, 0x80, 0x05, 0x3f,
			0x21, 0x70, 0xf1, 0x63, 0x77, 0xaf, 0x42, 0x20, 0xe5, 0xfd, 0xbf,
			0x81, 0x18, 0x26, 0xc3, 0xbe, 0x35, 0x88, 0x2e, 0x93, 0x55, 0xfc,
			0x7a, 0xc8, 0xba, 0x32, 0xe6, 0xc0, 0x19, 0x9e, 0xa3, 0x44, 0x54,
			0x3b, 0x0b, 0x8c, 0xc7, 0x6b, 0x28, 0xa7, 0xbc, 0x16, 0xad, 0xdb,
			0x64, 0x74, 0x14, 0x92, 0x0c, 0x48, 0xb8, 0x9f, 0xbd, 0x43, 0xc4,
			0x39, 0x31, 0xd3, 0xf2, 0xd5, 0x8b, 0x6e, 0xda, 0x01, 0xb1, 0x9c,
			0x49, 0xd8, 0xac, 0xf3, 0xcf, 0xca, 0xf4, 0x47, 0x10, 0x6f, 0xf0,
			0x4a, 0x5c, 0x38, 0x57, 0x73, 0x97, 0xcb, 0xa1, 0xe8, 0x3e, 0x96,
			0x61, 0x0d, 0x0f, 0xe0, 0x7c, 0x71, 0xcc, 0x90, 0x06, 0xf7, 0x1c,
			0xc2, 0x6a, 0xae, 0x69, 0x17, 0x99, 0x3a, 0x27, 0xd9, 0xeb, 0x2b,
			0x22, 0xd2, 0xa9, 0x07, 0x33, 0x2d, 0x3c, 0x15, 0xc9, 0x87, 0xaa,
			0x50, 0xa5, 0x03, 0x59, 0x09, 0x1a, 0x65, 0xd7, 0x84, 0xd0, 0x82,
			0x29, 0x5a, 0x1e, 0x7b, 0xa8, 0x6d, 0x2c };

	// combined Xtimes3[Sbox[]]
	static char Xtime3Sbox[] = { 0xa5, 0x84, 0x99, 0x8d, 0x0d, 0xbd, 0xb1,
			0x54, 0x50, 0x03, 0xa9, 0x7d, 0x19, 0x62, 0xe6, 0x9a, 0x45, 0x9d,
			0x40, 0x87, 0x15, 0xeb, 0xc9, 0x0b, 0xec, 0x67, 0xfd, 0xea, 0xbf,
			0xf7, 0x96, 0x5b, 0xc2, 0x1c, 0xae, 0x6a, 0x5a, 0x41, 0x02, 0x4f,
			0x5c, 0xf4, 0x34, 0x08, 0x93, 0x73, 0x53, 0x3f, 0x0c, 0x52, 0x65,
			0x5e, 0x28, 0xa1, 0x0f, 0xb5, 0x09, 0x36, 0x9b, 0x3d, 0x26, 0x69,
			0xcd, 0x9f, 0x1b, 0x9e, 0x74, 0x2e, 0x2d, 0xb2, 0xee, 0xfb, 0xf6,
			0x4d, 0x61, 0xce, 0x7b, 0x3e, 0x71, 0x97, 0xf5, 0x68, 0x00, 0x2c,
			0x60, 0x1f, 0xc8, 0xed, 0xbe, 0x46, 0xd9, 0x4b, 0xde, 0xd4, 0xe8,
			0x4a, 0x6b, 0x2a, 0xe5, 0x16, 0xc5, 0xd7, 0x55, 0x94, 0xcf, 0x10,
			0x06, 0x81, 0xf0, 0x44, 0xba, 0xe3, 0xf3, 0xfe, 0xc0, 0x8a, 0xad,
			0xbc, 0x48, 0x04, 0xdf, 0xc1, 0x75, 0x63, 0x30, 0x1a, 0x0e, 0x6d,
			0x4c, 0x14, 0x35, 0x2f, 0xe1, 0xa2, 0xcc, 0x39, 0x57, 0xf2, 0x82,
			0x47, 0xac, 0xe7, 0x2b, 0x95, 0xa0, 0x98, 0xd1, 0x7f, 0x66, 0x7e,
			0xab, 0x83, 0xca, 0x29, 0xd3, 0x3c, 0x79, 0xe2, 0x1d, 0x76, 0x3b,
			0x56, 0x4e, 0x1e, 0xdb, 0x0a, 0x6c, 0xe4, 0x5d, 0x6e, 0xef, 0xa6,
			0xa8, 0xa4, 0x37, 0x8b, 0x32, 0x43, 0x59, 0xb7, 0x8c, 0x64, 0xd2,
			0xe0, 0xb4, 0xfa, 0x07, 0x25, 0xaf, 0x8e, 0xe9, 0x18, 0xd5, 0x88,
			0x6f, 0x72, 0x24, 0xf1, 0xc7, 0x51, 0x23, 0x7c, 0x9c, 0x21, 0xdd,
			0xdc, 0x86, 0x85, 0x90, 0x42, 0xc4, 0xaa, 0xd8, 0x05, 0x01, 0x12,
			0xa3, 0x5f, 0xf9, 0xd0, 0x91, 0x58, 0x27, 0xb9, 0x38, 0x13, 0xb3,
			0x33, 0xbb, 0x70, 0x89, 0xa7, 0xb6, 0x22, 0x92, 0x20, 0x49, 0xff,
			0x78, 0x7a, 0x8f, 0xf8, 0x80, 0x17, 0xda, 0x31, 0xc6, 0xb8, 0xc3,
			0xb0, 0x77, 0x11, 0xcb, 0xfc, 0xd6, 0x3a };

	// exchanges columns in each of 4 rows
	// row0 - unchanged,
	// row1- shifted left 1,
	// row2 - shifted left 2
	// and row3 - shifted left 3

	void ShiftRows(char state[]) // 字节代换加行移位
	{
		char tmp;

		// just substitute row 0
		try {
			state[0] = Sbox[state[0] % 256];
			state[4] = Sbox[state[4] % 256]; // 第一行不用移动 ?state[0]序号对不？对
			state[8] = Sbox[state[8] % 256];
			state[12] = Sbox[state[12] % 256];
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			// rotate row 1
			tmp = Sbox[state[1] % 256];
			state[1] = Sbox[state[5] % 256]; // 第二行移动1格
			state[5] = Sbox[state[9] % 256];
			state[9] = Sbox[state[13] % 256];
			state[13] = tmp;
		} catch (Exception ex1) {
			ex1.printStackTrace();
		}

		try {
			// rotate row 2
			tmp = Sbox[state[2] % 256];
			state[2] = Sbox[state[10] % 256];
			state[10] = tmp; // 第三行移动两格
			tmp = Sbox[state[6] % 256];
			state[6] = Sbox[state[14] % 256];
			state[14] = tmp;
		} catch (Exception ex2) {
			ex2.printStackTrace();
		}

		try {
			// rotate row 3
			tmp = Sbox[state[15] % 256];
			state[15] = Sbox[state[11] % 256];
			state[11] = Sbox[state[7] % 256];
			state[7] = Sbox[state[3] % 256];
			state[3] = tmp; // 第四行移动三格
		} catch (Exception ex3) {
			ex3.printStackTrace();
		}
	}

	// recombine and mix each row in a column
	/*****************************************************************************
	 * 0 4 8 12 0 4 8 12 S'0,j=(2*S0,j)^(3*S1,j)^S2,j^S3,j ;(column 1) 1 5 9 13
	 * 行变换后 5 9 13 1 然后进入列混淆 S'1,j=S0,j^(2*S1,j)^(3*S2,j)^S3,j ;(column 2) 2 6
	 * 10 14 <-------- 10 14 2 6
	 * --------------》S'2,j=S0,j^S1,j^(2*S2,j)^(3*S3,j) ;(column 3) 3 7 11 15
	 * --反---- > 15 3 7 11 S'3,j=(3*S0,j)^S1,j^S2,j^(2*S3,j) ;(column 4)
	 ****************************************************************************/
	void MixSubColumns(char state[])// 该函数包含了行移位，列混淆和字节代换
	{
		char[] newstate = new char[4 * Nb]; // 自加注释：4行4列

		// mixing column 0
		newstate[0] = (char) (Xtime2Sbox[state[0] % 256]
				^ Xtime3Sbox[state[5] % 256] ^ Sbox[state[10] % 256] ^ Sbox[state[15] % 256]);
		newstate[1] = (char) (Sbox[state[0] % 256] ^ Xtime2Sbox[state[5] % 256]
				^ Xtime3Sbox[state[10] % 256] ^ Sbox[state[15] % 256]);
		newstate[2] = (char) (Sbox[state[0] % 256] ^ Sbox[state[5] % 256]
				^ Xtime2Sbox[state[10] % 256] ^ Xtime3Sbox[state[15] % 256]);
		newstate[3] = (char) (Xtime3Sbox[state[0] % 256] ^ Sbox[state[5] % 256]
				^ Sbox[state[10] % 256] ^ Xtime2Sbox[state[15] % 256]);

		// mixing column 1
		newstate[4] = (char) (Xtime2Sbox[state[4] % 256]
				^ Xtime3Sbox[state[9] % 256] ^ Sbox[state[14] % 256] ^ Sbox[state[3] % 256]);
		newstate[5] = (char) (Sbox[state[4] % 256] ^ Xtime2Sbox[state[9] % 256]
				^ Xtime3Sbox[state[14] % 256] ^ Sbox[state[3] % 256]);
		newstate[6] = (char) (Sbox[state[4] % 256] ^ Sbox[state[9] % 256]
				^ Xtime2Sbox[state[14] % 256] ^ Xtime3Sbox[state[3] % 256]);
		newstate[7] = (char) (Xtime3Sbox[state[4] % 256] ^ Sbox[state[9] % 256]
				^ Sbox[state[14] % 256] ^ Xtime2Sbox[state[3] % 256]);

		// mixing column 2
		newstate[8] = (char) (Xtime2Sbox[state[8] % 256]
				^ Xtime3Sbox[state[13] % 256] ^ Sbox[state[2] % 256] ^ Sbox[state[7] % 256]);
		newstate[9] = (char) (Sbox[state[8] % 256]
				^ Xtime2Sbox[state[13] % 256] ^ Xtime3Sbox[state[2] % 256] ^ Sbox[state[7] % 256]);
		newstate[10] = (char) (Sbox[state[8] % 256] ^ Sbox[state[13] % 256]
				^ Xtime2Sbox[state[2] % 256] ^ Xtime3Sbox[state[7] % 256]);
		newstate[11] = (char) (Xtime3Sbox[state[8] % 256]
				^ Sbox[state[13] % 256] ^ Sbox[state[2] % 256] ^ Xtime2Sbox[state[7] % 256]);

		// mixing column 3
		newstate[12] = (char) (Xtime2Sbox[state[12] % 256]
				^ Xtime3Sbox[state[1] % 256] ^ Sbox[state[6] % 256] ^ Sbox[state[11] % 256]);
		newstate[13] = (char) (Sbox[state[12] % 256]
				^ Xtime2Sbox[state[1] % 256] ^ Xtime3Sbox[state[6] % 256] ^ Sbox[state[11] % 256]);
		newstate[14] = (char) (Sbox[state[12] % 256] ^ Sbox[state[1] % 256]
				^ Xtime2Sbox[state[6] % 256] ^ Xtime3Sbox[state[11] % 256]);
		newstate[15] = (char) (Xtime3Sbox[state[12] % 256]
				^ Sbox[state[1] % 256] ^ Sbox[state[6] % 256] ^ Xtime2Sbox[state[11] % 256]);

		// memcpy (state, newstate, sizeof(newstate)); //存于state[]
		for (int i = 0; i < 4 * Nb; i++) {
			state[i] = newstate[i];
		}
	}

	char Rcon[] = { 0x00, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b,
			0x36 };

	// produce Nk bytes for each round
	void ExpandKey(char key[], char expkey[]) {
		char tmp0, tmp1, tmp2, tmp3, tmp4;
		int idx;

		for (idx = 0; idx < Nk; idx++) {
			expkey[4 * idx + 0] = key[4 * idx + 0];
			expkey[4 * idx + 1] = key[4 * idx + 1];
			expkey[4 * idx + 2] = key[4 * idx + 2];
			expkey[4 * idx + 3] = key[4 * idx + 3];
		}

		for (idx = Nk; idx < Nb * (Nr + 1); idx++) {
			tmp0 = expkey[4 * idx - 4];
			tmp1 = expkey[4 * idx - 3];
			tmp2 = expkey[4 * idx - 2];
			tmp3 = expkey[4 * idx - 1];
			if ((idx % Nk) <= 0) {
				tmp4 = tmp3;
				tmp3 = Sbox[tmp0];
				tmp0 = (char) (Sbox[tmp1] ^ Rcon[idx / Nk]);
				tmp1 = Sbox[tmp2];
				tmp2 = Sbox[tmp4];
			}

			// convert from longs to bytes

			expkey[4 * idx + 0] = (char) (expkey[4 * idx - 4 * Nk + 0] ^ tmp0);
			expkey[4 * idx + 1] = (char) (expkey[4 * idx - 4 * Nk + 1] ^ tmp1);
			expkey[4 * idx + 2] = (char) (expkey[4 * idx - 4 * Nk + 2] ^ tmp2);
			expkey[4 * idx + 3] = (char) (expkey[4 * idx - 4 * Nk + 3] ^ tmp3);
		}
	}

	// encrypt/decrypt columns of the key
	// n.b. you can replace this with
	// byte-wise xor if you wish.

	void AddRoundKey(char state[], char key[]) {
		int idx;

		for (idx = 0; idx < 16; idx++)
			// 把idx < 4改为idx < 16
			state[idx] ^= key[idx];
	}

	static void AddRoundKey(char[] state, char[] expkey, int key) {
		int idx;

		for (idx = 0; idx < 16; idx++) {
			state[idx] ^= expkey[key + idx];
		}

	}

	// encrypt one 128 bit block
	byte[] Encrypt(char in[], char expkey[]) {
		char[] out = new char[16];
		int round, idx, i = 0;
		char[] state = new char[Nb * 4]; // 自加注释：state[16]

		for (idx = 0; idx < Nb; idx++) {
			state[4 * idx + 0] = in[i++];
			state[4 * idx + 1] = in[i++];
			state[4 * idx + 2] = in[i++];
			state[4 * idx + 3] = in[i++];
		}

		AddRoundKey(state, expkey);
		// AddRoundKey (state, expkey,160);

		for (round = 1; round < Nr + 1; round++) {
			if (round < Nr)
				MixSubColumns(state);
			else
				ShiftRows(state);// 最后一轮不需要列混淆

			// AddRoundKey (state, expkey + round * Nb); //把round * Nb改为round *
			// 16
			AddRoundKey(state, expkey, round * 16);
		}
		int ii = 0;
		for (idx = 0; idx < Nb; idx++) {
			out[ii++] = state[4 * idx + 0];
			out[ii++] = state[4 * idx + 1];
			out[ii++] = state[4 * idx + 2];
			out[ii++] = state[4 * idx + 3];
		}
		return CharToByte(out);
	}

	public static char[] ByteToChar(byte[] data) {
		char[] A = new char[data.length];
		for (int i = 0; i < data.length; i++) {
			A[i] = (char) data[i];
		}
		return A;
	}

	public static byte[] CharToByte(char[] data) {
		byte[] A = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			A[i] = (byte) data[i];
		}
		return A;
	}

	/**
	 * 加密16字节的数组
	 * 
	 * @param in
	 *            byte[] 输入字节数，必须大小不能大于16
	 * @param key
	 *            char[] 密钥
	 * @return byte[] 输出字节数，必须大小不能大于16
	 */
	public byte[] Encrypt_Byte(byte[] in, String key) {
		if (in.length > 16 || key.length() != 16) {
			System.out.println("No!! 你输入的字节数或密钥位数大于16，请输入安全的数据,加密程序退出");
			System.exit(1);
		}
		char[] KEY = key.toCharArray();
		char[] expkey = new char[4 * Nb * (Nr + 1)];
		byte[] out = new byte[16];
		ExpandKey(KEY, expkey);
		try {
			out = Encrypt(ByteToChar(in), expkey); // 解密变换；
		} catch (Exception e) {
			System.out.println("hello");
			e.printStackTrace();
		}
		return out;

	}

	/**
	 * 文件加密，文件长度不能被16整除的，在文件的开头加‘A’，标示：02AAAAAAAAA
	 * 
	 * @param Encrypt_Befor
	 *            String 源文件路径
	 * @param Encrypt_After
	 *            String 目标文件路径
	 * @param Key
	 *            String 密钥字符串，必须是16位
	 * @throws Exception
	 */
	public void Encrypt_File(String Encrypt_Befor, String Encrypt_After,
			String Key) throws Exception {

		if (Key == null || Key.length() > 16) {
			System.out.println("你的密钥为空或者太长--程序退出");
			return;
		}
		char[] key = Key.toCharArray();
		long startTime = System.nanoTime();
		// char[] key = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
		// 'l', 'm', 'n','o', 'p'};
		byte[] copy = new byte[16];
		char[] expkey = new char[4 * Nb * (Nr + 1)];
		FileInputStream fp1 = new FileInputStream(Encrypt_Befor);
		FileOutputStream fp2 = new FileOutputStream(Encrypt_After, true);

		long Length = fp1.available(); // 得到文件长度

		long N = 0;
		if (Length != 0) {
			N = 18 - (Length + 2) % 16; // 补0个个数为N-2
			if (N == 18)
				N = 2;
		}
		Length = Length + N;

		long leave = Length / (4 * Nb); // 得到整块的加密轮数；
		byte[] state = new byte[16];
		boolean isnull = true;
		boolean indexout = false;
		byte[] bb = new byte[14];

		int first = 1;
		while (leave > 0) { // 分组长度的整数倍的密文块解密；, for(int g=0;g<16;g++)

			if (first == 1) {
				state[0] = (byte) (N / 10);
				state[1] = (byte) (N % 10);
				for (int j = 2; j < N; j++) {
					state[j] = 'A';
				}
				try {
					fp1.read(state, (int) (N), (int) (16 - N)); // 读取密文块；
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				first = 2;
			} else {
				fp1.read(state, 0, 16); // 读取密文块；
			}
			ExpandKey(key, expkey);
			try {
				copy = Encrypt(ByteToChar(state), expkey); // 解密变换；
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
			fp2.write(copy, 0, 16); // 将解密后的明文写入目标文件；
			leave--; // 轮数减一；
		}
		fp1.close(); // 关闭源文件和目标文件；
		fp2.close();
		long timer = (System.nanoTime() - startTime) / 1000000;
		System.out.println("文件大小:  ....." + Length);
		System.out.println("共需时间:  ....." + timer);
	}
}
