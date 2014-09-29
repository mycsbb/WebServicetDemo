package com.web.api.demo;

import java.io.*;

public class AES_Decrypt {
	static int Nk = 4; // number of columns in a key
	static int Nr = 10; // number of rounds in encryption
	static int Nb = 4;
	static int len = 0;
	/**
	 * s盒
	 */
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
	/**
	 * 反s盒
	 */
	static char InvSbox[] = { // inverse s-box
	0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e,
			0x81, 0xf3, 0xd7, 0xfb, 0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff,
			0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb, 0x54, 0x7b,
			0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42,
			0xfa, 0xc3, 0x4e, 0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2,
			0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25, 0x72, 0xf8, 0xf6,
			0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65,
			0xb6, 0x92, 0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e,
			0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84, 0x90, 0xd8, 0xab, 0x00,
			0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45,
			0x06, 0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf,
			0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b, 0x3a, 0x91, 0x11, 0x41, 0x4f,
			0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73,
			0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37,
			0xe8, 0x1c, 0x75, 0xdf, 0x6e, 0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29,
			0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b, 0xfc,
			0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe,
			0x78, 0xcd, 0x5a, 0xf4, 0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7,
			0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f, 0x60, 0x51,
			0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93,
			0xc9, 0x9c, 0xef, 0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0,
			0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61, 0x17, 0x2b, 0x04,
			0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21,
			0x0c, 0x7d };

	static char Xtime2[] = { 0x00, 0x02, 0x04, 0x06, 0x08, 0x0a, 0x0c, 0x0e,
			0x10, 0x12, 0x14, 0x16, 0x18, 0x1a, 0x1c, 0x1e, 0x20, 0x22, 0x24,
			0x26, 0x28, 0x2a, 0x2c, 0x2e, 0x30, 0x32, 0x34, 0x36, 0x38, 0x3a,
			0x3c, 0x3e, 0x40, 0x42, 0x44, 0x46, 0x48, 0x4a, 0x4c, 0x4e, 0x50,
			0x52, 0x54, 0x56, 0x58, 0x5a, 0x5c, 0x5e, 0x60, 0x62, 0x64, 0x66,
			0x68, 0x6a, 0x6c, 0x6e, 0x70, 0x72, 0x74, 0x76, 0x78, 0x7a, 0x7c,
			0x7e, 0x80, 0x82, 0x84, 0x86, 0x88, 0x8a, 0x8c, 0x8e, 0x90, 0x92,
			0x94, 0x96, 0x98, 0x9a, 0x9c, 0x9e, 0xa0, 0xa2, 0xa4, 0xa6, 0xa8,
			0xaa, 0xac, 0xae, 0xb0, 0xb2, 0xb4, 0xb6, 0xb8, 0xba, 0xbc, 0xbe,
			0xc0, 0xc2, 0xc4, 0xc6, 0xc8, 0xca, 0xcc, 0xce, 0xd0, 0xd2, 0xd4,
			0xd6, 0xd8, 0xda, 0xdc, 0xde, 0xe0, 0xe2, 0xe4, 0xe6, 0xe8, 0xea,
			0xec, 0xee, 0xf0, 0xf2, 0xf4, 0xf6, 0xf8, 0xfa, 0xfc, 0xfe, 0x1b,
			0x19, 0x1f, 0x1d, 0x13, 0x11, 0x17, 0x15, 0x0b, 0x09, 0x0f, 0x0d,
			0x03, 0x01, 0x07, 0x05, 0x3b, 0x39, 0x3f, 0x3d, 0x33, 0x31, 0x37,
			0x35, 0x2b, 0x29, 0x2f, 0x2d, 0x23, 0x21, 0x27, 0x25, 0x5b, 0x59,
			0x5f, 0x5d, 0x53, 0x51, 0x57, 0x55, 0x4b, 0x49, 0x4f, 0x4d, 0x43,
			0x41, 0x47, 0x45, 0x7b, 0x79, 0x7f, 0x7d, 0x73, 0x71, 0x77, 0x75,
			0x6b, 0x69, 0x6f, 0x6d, 0x63, 0x61, 0x67, 0x65, 0x9b, 0x99, 0x9f,
			0x9d, 0x93, 0x91, 0x97, 0x95, 0x8b, 0x89, 0x8f, 0x8d, 0x83, 0x81,
			0x87, 0x85, 0xbb, 0xb9, 0xbf, 0xbd, 0xb3, 0xb1, 0xb7, 0xb5, 0xab,
			0xa9, 0xaf, 0xad, 0xa3, 0xa1, 0xa7, 0xa5, 0xdb, 0xd9, 0xdf, 0xdd,
			0xd3, 0xd1, 0xd7, 0xd5, 0xcb, 0xc9, 0xcf, 0xcd, 0xc3, 0xc1, 0xc7,
			0xc5, 0xfb, 0xf9, 0xff, 0xfd, 0xf3, 0xf1, 0xf7, 0xf5, 0xeb, 0xe9,
			0xef, 0xed, 0xe3, 0xe1, 0xe7, 0xe5 };

	static char Xtime9[] = { 0x00, 0x09, 0x12, 0x1b, 0x24, 0x2d, 0x36, 0x3f,
			0x48, 0x41, 0x5a, 0x53, 0x6c, 0x65, 0x7e, 0x77, 0x90, 0x99, 0x82,
			0x8b, 0xb4, 0xbd, 0xa6, 0xaf, 0xd8, 0xd1, 0xca, 0xc3, 0xfc, 0xf5,
			0xee, 0xe7, 0x3b, 0x32, 0x29, 0x20, 0x1f, 0x16, 0x0d, 0x04, 0x73,
			0x7a, 0x61, 0x68, 0x57, 0x5e, 0x45, 0x4c, 0xab, 0xa2, 0xb9, 0xb0,
			0x8f, 0x86, 0x9d, 0x94, 0xe3, 0xea, 0xf1, 0xf8, 0xc7, 0xce, 0xd5,
			0xdc, 0x76, 0x7f, 0x64, 0x6d, 0x52, 0x5b, 0x40, 0x49, 0x3e, 0x37,
			0x2c, 0x25, 0x1a, 0x13, 0x08, 0x01, 0xe6, 0xef, 0xf4, 0xfd, 0xc2,
			0xcb, 0xd0, 0xd9, 0xae, 0xa7, 0xbc, 0xb5, 0x8a, 0x83, 0x98, 0x91,
			0x4d, 0x44, 0x5f, 0x56, 0x69, 0x60, 0x7b, 0x72, 0x05, 0x0c, 0x17,
			0x1e, 0x21, 0x28, 0x33, 0x3a, 0xdd, 0xd4, 0xcf, 0xc6, 0xf9, 0xf0,
			0xeb, 0xe2, 0x95, 0x9c, 0x87, 0x8e, 0xb1, 0xb8, 0xa3, 0xaa, 0xec,
			0xe5, 0xfe, 0xf7, 0xc8, 0xc1, 0xda, 0xd3, 0xa4, 0xad, 0xb6, 0xbf,
			0x80, 0x89, 0x92, 0x9b, 0x7c, 0x75, 0x6e, 0x67, 0x58, 0x51, 0x4a,
			0x43, 0x34, 0x3d, 0x26, 0x2f, 0x10, 0x19, 0x02, 0x0b, 0xd7, 0xde,
			0xc5, 0xcc, 0xf3, 0xfa, 0xe1, 0xe8, 0x9f, 0x96, 0x8d, 0x84, 0xbb,
			0xb2, 0xa9, 0xa0, 0x47, 0x4e, 0x55, 0x5c, 0x63, 0x6a, 0x71, 0x78,
			0x0f, 0x06, 0x1d, 0x14, 0x2b, 0x22, 0x39, 0x30, 0x9a, 0x93, 0x88,
			0x81, 0xbe, 0xb7, 0xac, 0xa5, 0xd2, 0xdb, 0xc0, 0xc9, 0xf6, 0xff,
			0xe4, 0xed, 0x0a, 0x03, 0x18, 0x11, 0x2e, 0x27, 0x3c, 0x35, 0x42,
			0x4b, 0x50, 0x59, 0x66, 0x6f, 0x74, 0x7d, 0xa1, 0xa8, 0xb3, 0xba,
			0x85, 0x8c, 0x97, 0x9e, 0xe9, 0xe0, 0xfb, 0xf2, 0xcd, 0xc4, 0xdf,
			0xd6, 0x31, 0x38, 0x23, 0x2a, 0x15, 0x1c, 0x07, 0x0e, 0x79, 0x70,
			0x6b, 0x62, 0x5d, 0x54, 0x4f, 0x46 };

	static char XtimeB[] = { 0x00, 0x0b, 0x16, 0x1d, 0x2c, 0x27, 0x3a, 0x31,
			0x58, 0x53, 0x4e, 0x45, 0x74, 0x7f, 0x62, 0x69, 0xb0, 0xbb, 0xa6,
			0xad, 0x9c, 0x97, 0x8a, 0x81, 0xe8, 0xe3, 0xfe, 0xf5, 0xc4, 0xcf,
			0xd2, 0xd9, 0x7b, 0x70, 0x6d, 0x66, 0x57, 0x5c, 0x41, 0x4a, 0x23,
			0x28, 0x35, 0x3e, 0x0f, 0x04, 0x19, 0x12, 0xcb, 0xc0, 0xdd, 0xd6,
			0xe7, 0xec, 0xf1, 0xfa, 0x93, 0x98, 0x85, 0x8e, 0xbf, 0xb4, 0xa9,
			0xa2, 0xf6, 0xfd, 0xe0, 0xeb, 0xda, 0xd1, 0xcc, 0xc7, 0xae, 0xa5,
			0xb8, 0xb3, 0x82, 0x89, 0x94, 0x9f, 0x46, 0x4d, 0x50, 0x5b, 0x6a,
			0x61, 0x7c, 0x77, 0x1e, 0x15, 0x08, 0x03, 0x32, 0x39, 0x24, 0x2f,
			0x8d, 0x86, 0x9b, 0x90, 0xa1, 0xaa, 0xb7, 0xbc, 0xd5, 0xde, 0xc3,
			0xc8, 0xf9, 0xf2, 0xef, 0xe4, 0x3d, 0x36, 0x2b, 0x20, 0x11, 0x1a,
			0x07, 0x0c, 0x65, 0x6e, 0x73, 0x78, 0x49, 0x42, 0x5f, 0x54, 0xf7,
			0xfc, 0xe1, 0xea, 0xdb, 0xd0, 0xcd, 0xc6, 0xaf, 0xa4, 0xb9, 0xb2,
			0x83, 0x88, 0x95, 0x9e, 0x47, 0x4c, 0x51, 0x5a, 0x6b, 0x60, 0x7d,
			0x76, 0x1f, 0x14, 0x09, 0x02, 0x33, 0x38, 0x25, 0x2e, 0x8c, 0x87,
			0x9a, 0x91, 0xa0, 0xab, 0xb6, 0xbd, 0xd4, 0xdf, 0xc2, 0xc9, 0xf8,
			0xf3, 0xee, 0xe5, 0x3c, 0x37, 0x2a, 0x21, 0x10, 0x1b, 0x06, 0x0d,
			0x64, 0x6f, 0x72, 0x79, 0x48, 0x43, 0x5e, 0x55, 0x01, 0x0a, 0x17,
			0x1c, 0x2d, 0x26, 0x3b, 0x30, 0x59, 0x52, 0x4f, 0x44, 0x75, 0x7e,
			0x63, 0x68, 0xb1, 0xba, 0xa7, 0xac, 0x9d, 0x96, 0x8b, 0x80, 0xe9,
			0xe2, 0xff, 0xf4, 0xc5, 0xce, 0xd3, 0xd8, 0x7a, 0x71, 0x6c, 0x67,
			0x56, 0x5d, 0x40, 0x4b, 0x22, 0x29, 0x34, 0x3f, 0x0e, 0x05, 0x18,
			0x13, 0xca, 0xc1, 0xdc, 0xd7, 0xe6, 0xed, 0xf0, 0xfb, 0x92, 0x99,
			0x84, 0x8f, 0xbe, 0xb5, 0xa8, 0xa3 };

	static char XtimeD[] = { 0x00, 0x0d, 0x1a, 0x17, 0x34, 0x39, 0x2e, 0x23,
			0x68, 0x65, 0x72, 0x7f, 0x5c, 0x51, 0x46, 0x4b, 0xd0, 0xdd, 0xca,
			0xc7, 0xe4, 0xe9, 0xfe, 0xf3, 0xb8, 0xb5, 0xa2, 0xaf, 0x8c, 0x81,
			0x96, 0x9b, 0xbb, 0xb6, 0xa1, 0xac, 0x8f, 0x82, 0x95, 0x98, 0xd3,
			0xde, 0xc9, 0xc4, 0xe7, 0xea, 0xfd, 0xf0, 0x6b, 0x66, 0x71, 0x7c,
			0x5f, 0x52, 0x45, 0x48, 0x03, 0x0e, 0x19, 0x14, 0x37, 0x3a, 0x2d,
			0x20, 0x6d, 0x60, 0x77, 0x7a, 0x59, 0x54, 0x43, 0x4e, 0x05, 0x08,
			0x1f, 0x12, 0x31, 0x3c, 0x2b, 0x26, 0xbd, 0xb0, 0xa7, 0xaa, 0x89,
			0x84, 0x93, 0x9e, 0xd5, 0xd8, 0xcf, 0xc2, 0xe1, 0xec, 0xfb, 0xf6,
			0xd6, 0xdb, 0xcc, 0xc1, 0xe2, 0xef, 0xf8, 0xf5, 0xbe, 0xb3, 0xa4,
			0xa9, 0x8a, 0x87, 0x90, 0x9d, 0x06, 0x0b, 0x1c, 0x11, 0x32, 0x3f,
			0x28, 0x25, 0x6e, 0x63, 0x74, 0x79, 0x5a, 0x57, 0x40, 0x4d, 0xda,
			0xd7, 0xc0, 0xcd, 0xee, 0xe3, 0xf4, 0xf9, 0xb2, 0xbf, 0xa8, 0xa5,
			0x86, 0x8b, 0x9c, 0x91, 0x0a, 0x07, 0x10, 0x1d, 0x3e, 0x33, 0x24,
			0x29, 0x62, 0x6f, 0x78, 0x75, 0x56, 0x5b, 0x4c, 0x41, 0x61, 0x6c,
			0x7b, 0x76, 0x55, 0x58, 0x4f, 0x42, 0x09, 0x04, 0x13, 0x1e, 0x3d,
			0x30, 0x27, 0x2a, 0xb1, 0xbc, 0xab, 0xa6, 0x85, 0x88, 0x9f, 0x92,
			0xd9, 0xd4, 0xc3, 0xce, 0xed, 0xe0, 0xf7, 0xfa, 0xb7, 0xba, 0xad,
			0xa0, 0x83, 0x8e, 0x99, 0x94, 0xdf, 0xd2, 0xc5, 0xc8, 0xeb, 0xe6,
			0xf1, 0xfc, 0x67, 0x6a, 0x7d, 0x70, 0x53, 0x5e, 0x49, 0x44, 0x0f,
			0x02, 0x15, 0x18, 0x3b, 0x36, 0x21, 0x2c, 0x0c, 0x01, 0x16, 0x1b,
			0x38, 0x35, 0x22, 0x2f, 0x64, 0x69, 0x7e, 0x73, 0x50, 0x5d, 0x4a,
			0x47, 0xdc, 0xd1, 0xc6, 0xcb, 0xe8, 0xe5, 0xf2, 0xff, 0xb4, 0xb9,
			0xae, 0xa3, 0x80, 0x8d, 0x9a, 0x97 };

	static char XtimeE[] = { 0x00, 0x0e, 0x1c, 0x12, 0x38, 0x36, 0x24, 0x2a,
			0x70, 0x7e, 0x6c, 0x62, 0x48, 0x46, 0x54, 0x5a, 0xe0, 0xee, 0xfc,
			0xf2, 0xd8, 0xd6, 0xc4, 0xca, 0x90, 0x9e, 0x8c, 0x82, 0xa8, 0xa6,
			0xb4, 0xba, 0xdb, 0xd5, 0xc7, 0xc9, 0xe3, 0xed, 0xff, 0xf1, 0xab,
			0xa5, 0xb7, 0xb9, 0x93, 0x9d, 0x8f, 0x81, 0x3b, 0x35, 0x27, 0x29,
			0x03, 0x0d, 0x1f, 0x11, 0x4b, 0x45, 0x57, 0x59, 0x73, 0x7d, 0x6f,
			0x61, 0xad, 0xa3, 0xb1, 0xbf, 0x95, 0x9b, 0x89, 0x87, 0xdd, 0xd3,
			0xc1, 0xcf, 0xe5, 0xeb, 0xf9, 0xf7, 0x4d, 0x43, 0x51, 0x5f, 0x75,
			0x7b, 0x69, 0x67, 0x3d, 0x33, 0x21, 0x2f, 0x05, 0x0b, 0x19, 0x17,
			0x76, 0x78, 0x6a, 0x64, 0x4e, 0x40, 0x52, 0x5c, 0x06, 0x08, 0x1a,
			0x14, 0x3e, 0x30, 0x22, 0x2c, 0x96, 0x98, 0x8a, 0x84, 0xae, 0xa0,
			0xb2, 0xbc, 0xe6, 0xe8, 0xfa, 0xf4, 0xde, 0xd0, 0xc2, 0xcc, 0x41,
			0x4f, 0x5d, 0x53, 0x79, 0x77, 0x65, 0x6b, 0x31, 0x3f, 0x2d, 0x23,
			0x09, 0x07, 0x15, 0x1b, 0xa1, 0xaf, 0xbd, 0xb3, 0x99, 0x97, 0x85,
			0x8b, 0xd1, 0xdf, 0xcd, 0xc3, 0xe9, 0xe7, 0xf5, 0xfb, 0x9a, 0x94,
			0x86, 0x88, 0xa2, 0xac, 0xbe, 0xb0, 0xea, 0xe4, 0xf6, 0xf8, 0xd2,
			0xdc, 0xce, 0xc0, 0x7a, 0x74, 0x66, 0x68, 0x42, 0x4c, 0x5e, 0x50,
			0x0a, 0x04, 0x16, 0x18, 0x32, 0x3c, 0x2e, 0x20, 0xec, 0xe2, 0xf0,
			0xfe, 0xd4, 0xda, 0xc8, 0xc6, 0x9c, 0x92, 0x80, 0x8e, 0xa4, 0xaa,
			0xb8, 0xb6, 0x0c, 0x02, 0x10, 0x1e, 0x34, 0x3a, 0x28, 0x26, 0x7c,
			0x72, 0x60, 0x6e, 0x44, 0x4a, 0x58, 0x56, 0x37, 0x39, 0x2b, 0x25,
			0x0f, 0x01, 0x13, 0x1d, 0x47, 0x49, 0x5b, 0x55, 0x7f, 0x71, 0x63,
			0x6d, 0xd7, 0xd9, 0xcb, 0xc5, 0xef, 0xe1, 0xf3, 0xfd, 0xa7, 0xa9,
			0xbb, 0xb5, 0x9f, 0x91, 0x83, 0x8d };

	static void InvShiftRows(char[] state) {
		char tmp;
		// try{
		// restore row 0
		state[0] = InvSbox[state[0] % 256];
		state[4] = InvSbox[state[4] % 256];
		state[8] = InvSbox[state[8] % 256];
		state[12] = InvSbox[state[12] % 256];
		// }catch(Exception e){System.out.println("fuck3333");}
		// restore row 1
		// try{
		tmp = InvSbox[state[13] % 256];
		state[13] = InvSbox[state[9] % 256];
		state[9] = InvSbox[state[5] % 256];
		state[5] = InvSbox[state[1] % 256];
		state[1] = tmp;
		// }catch(Exception e){System.out.println("fuck4444");}
		// restore row 2
		// try{
		tmp = InvSbox[state[2] % 256];
		state[2] = InvSbox[state[10] % 256];
		state[10] = tmp;
		tmp = InvSbox[state[6] % 256];
		state[6] = InvSbox[state[14] % 256];
		state[14] = tmp;
		// }catch(Exception e){System.out.println("fuck5555");}
		// restore row 3
		// try{
		tmp = InvSbox[state[3] % 256];
		state[3] = InvSbox[state[7] % 256];
		state[7] = InvSbox[state[11] % 256];
		state[11] = InvSbox[state[15] % 256];
		state[15] = tmp;
		// }catch(Exception e){System.out.println("fuck6666");}
	}

	// recombine and mix each row in a column
	/*****************************************************************************
	 * 0 4 8 12 0 4 8 12 S'0,j=(2*S0,j)^(3*S1,j)^S2,j^S3,j ;(column 1) 1 5 9 13
	 * 行变换后 5 9 13 1 然后进入列混淆 S'1,j=S0,j^(2*S1,j)^(3*S2,j)^S3,j ;(column 2) 2 6
	 * 10 14 --------> 10 14 2 6
	 * --------------》S'2,j=S0,j^S1,j^(2*S2,j)^(3*S3,j) ;(column 3) 3 7 11 15 15
	 * 3 7 11 S'3,j=(3*S0,j)^S1,j^S2,j^(2*S3,j) ;(column 4)
	 ****************************************************************************/
	// restore and un-mix each row in a column
	static void InvMixSubColumns(char[] state) {
		char[] newstate = new char[16];
		int i;

		// restore column 0
		newstate[0] = (char) (XtimeE[state[0]] ^ XtimeB[state[1]]
				^ XtimeD[state[2]] ^ Xtime9[state[3]]);
		newstate[5] = (char) (Xtime9[state[0]] ^ XtimeE[state[1]]
				^ XtimeB[state[2]] ^ XtimeD[state[3]]);
		newstate[10] = (char) (XtimeD[state[0]] ^ Xtime9[state[1]]
				^ XtimeE[state[2]] ^ XtimeB[state[3]]);
		newstate[15] = (char) (XtimeB[state[0]] ^ XtimeD[state[1]]
				^ Xtime9[state[2]] ^ XtimeE[state[3]]);

		// restore column 1
		newstate[4] = (char) (XtimeE[state[4]] ^ XtimeB[state[5]]
				^ XtimeD[state[6]] ^ Xtime9[state[7]]);
		newstate[9] = (char) (Xtime9[state[4]] ^ XtimeE[state[5]]
				^ XtimeB[state[6]] ^ XtimeD[state[7]]);
		newstate[14] = (char) (XtimeD[state[4]] ^ Xtime9[state[5]]
				^ XtimeE[state[6]] ^ XtimeB[state[7]]);
		newstate[3] = (char) (XtimeB[state[4]] ^ XtimeD[state[5]]
				^ Xtime9[state[6]] ^ XtimeE[state[7]]);

		// restore column 2
		newstate[8] = (char) (XtimeE[state[8]] ^ XtimeB[state[9]]
				^ XtimeD[state[10]] ^ Xtime9[state[11]]);
		newstate[13] = (char) (Xtime9[state[8]] ^ XtimeE[state[9]]
				^ XtimeB[state[10]] ^ XtimeD[state[11]]);
		newstate[2] = (char) (XtimeD[state[8]] ^ Xtime9[state[9]]
				^ XtimeE[state[10]] ^ XtimeB[state[11]]);
		newstate[7] = (char) (XtimeB[state[8]] ^ XtimeD[state[9]]
				^ Xtime9[state[10]] ^ XtimeE[state[11]]);

		// restore column 3
		newstate[12] = (char) (XtimeE[state[12]] ^ XtimeB[state[13]]
				^ XtimeD[state[14]] ^ Xtime9[state[15]]);
		newstate[1] = (char) (Xtime9[state[12]] ^ XtimeE[state[13]]
				^ XtimeB[state[14]] ^ XtimeD[state[15]]);
		newstate[6] = (char) (XtimeD[state[12]] ^ Xtime9[state[13]]
				^ XtimeE[state[14]] ^ XtimeB[state[15]]);
		newstate[11] = (char) (XtimeB[state[12]] ^ XtimeD[state[13]]
				^ Xtime9[state[14]] ^ XtimeE[state[15]]);

		for (i = 0; i < 4 * Nb; i++) {
			state[i] = InvSbox[newstate[i]];
		}
	}

	// encrypt/decrypt columns of the key
	// n.b. you can replace this with
	// byte-wise xor if you wish.

	static char Rcon[] = { 0x00, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40,
			0x80, 0x1b, 0x36 };

	// produce Nk bytes for each round
	public static void ExpandKey(char[] key, char[] expkey) {
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

	static void AddRoundKey(char[] state, char[] expkey, int key) {
		int idx;

		for (idx = 0; idx < 16; idx++) {
			state[idx] ^= expkey[key + idx];
		}

	}

	public static byte[] Decrypt(char[] in, char[] expkey) {
		char[] out = new char[16];
		int idx, round, j;
		char state[] = new char[Nb * 4];
		int i = 0;
		for (idx = 0; idx < Nb; idx++) {
			state[4 * idx + 0] = in[i++];
			state[4 * idx + 1] = in[i++];
			state[4 * idx + 2] = in[i++];
			state[4 * idx + 3] = in[i++];

		}
		// byte[] a=CharToByte(state);
		// System.out.println((int)a[0]);
		try {
			AddRoundKey(state, expkey, 160); // ////////////////////
			byte[] b = CharToByte(state);
			// System.out.println((int)b[0]+"----------AddRoundKey");
		} catch (Exception e) {
			System.out.println("fuck");
		}
		round = Nr;
		try {
			InvShiftRows(state);
			byte[] c = CharToByte(state);
			// System.out.println((int)c[0]+"----------InvShiftRows");
		} catch (Exception e) {
			System.out.println("fuck22222");
		}
		while (round-- > 0) {
			AddRoundKey(state, expkey, round * 16); // //////////////////////////
			if (round > 0) {
				InvMixSubColumns(state);
			}
		}
		// System.out.println((int)state[0]+"----------while");
		int n = 0;
		for (idx = 0; idx < Nb; idx++) {
			out[n++] = state[4 * idx + 0];
			out[n++] = state[4 * idx + 1];
			out[n++] = state[4 * idx + 2];
			out[n++] = state[4 * idx + 3];
		}
		// for(int d=0;d<out.length;d++)
		// System.out.print((int)out[0]);
		return CharToByte(out);

	}

	public static char[] ByteToChar(byte[] data) {
		char[] A = new char[data.length];
		for (int i = 0; i < data.length; i++) {
			A[i] = (char) data[i];
		}
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
		for (int i = 0; i < data.length; i++) {
			A[i] = (byte) data[i];
		}
		return A;
	}

	/**
	 * 求出需要删除的多余字节
	 * 
	 * @param data
	 *            byte[]
	 * @return int
	 */
	public static int getinsertlen(byte[] data) {
		int[] A = new int[2];
		for (int i = 0; i < 2; i++) {
			A[i] = (int) (char) data[i];
		}

		int len = A[0] * 10 + A[1];
		return len;

	}

	/**
	 * 去掉加入的无用字节
	 * 
	 * @param data
	 *            byte[]
	 * @param len
	 *            String
	 * @return byte[]
	 */

	public static byte[] Bytedelete(byte[] data, int len) {
		byte[] A = new byte[16 - len];
		for (int i = 0; i < 16 - len; i++) {
			A[i] = data[i + len];
		}
		return A;
	}

	/**
	 * 16位字节解密
	 * 
	 * @param in
	 *            byte[] 输入字节数组
	 * @param key
	 *            String 密钥
	 * @return byte[] 返回解密后的数组
	 */
	public byte[] Decrypt_Byte(byte[] in, String key) {
		if (in.length > 16 || key.length() != 16) {
			System.out.println("No!! 你输入的字节数或密钥位数大于16，请输入安全的数据,解密程序退出");
			System.exit(1);
		}
		char[] KEY = key.toCharArray();
		char[] expkey = new char[4 * Nb * (Nr + 1)];
		byte[] out = new byte[16];
		ExpandKey(KEY, expkey);
		try {
			out = Decrypt(ByteToChar(in), expkey); // 解密变换；
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	/**
	 * 解密文件
	 * 
	 * @param Encrypt_Befor
	 *            String 源文件路径
	 * @param Encrypt_After
	 *            String 目标文件路径
	 * @param key
	 *            String 密钥
	 * @throws Exception
	 */
	public void Decrypt_File(String Encrypt_Befor, String Encrypt_After,
			String key) throws Exception {
		if (key == null || key.length() != 16) {
			System.out.println("你的密钥为空，或不是16位的，程序退出");
			return;
		}
		long startTime = System.nanoTime();
		char[] KEY = key.toCharArray();
		byte[] copy = new byte[16];
		char[] expkey = new char[4 * Nb * (Nr + 1)];

		FileInputStream fp1 = new FileInputStream(Encrypt_Befor);
		FileOutputStream fp2 = null;
		long Length = fp1.available(); // 得到文件长度
		long leave = Length / (4 * Nb); // 得到整块的加密轮数；
		byte[] state = new byte[16];
		boolean isnull = true;
		boolean indexout = false;
		byte[] bb = new byte[14];
		while (leave > 0) { // 分组长度的整数倍的密文块解密；, for(int g=0;g<16;g++)
			fp1.read(state, 0, 4 * Nb); // 读取密文块；
			ExpandKey(KEY, expkey);
			try {
				copy = Decrypt(ByteToChar(state), expkey); // 解密变换；
			} catch (Exception e) {
				System.out.println("hello");
				e.printStackTrace();
				System.exit(1);
			}

			if (isnull == true) {
				if (indexout == false) {
					len = getinsertlen(copy);
					if (len > 18) {
						fp1.close();
						throw (new Exception("解密失败"));
					} else {
						fp2 = new FileOutputStream(Encrypt_After, true);
					}
				}
				if (len == 18) {
					bb = Bytedelete(copy, 1);
					fp2.write(bb, 0, 15); // 将解密后的明文写入目标文件;
					System.out.println("第2次A");
					indexout = false;
					isnull = false;
				}

				if (len <= 16) {
					bb = Bytedelete(copy, len);
					fp2.write(bb, 0, 16 - len); // 将解密后的明文写入目标文件；
					isnull = false;
				}

				if (len > 16 && len < 19) {
					bb = Bytedelete(copy, 15);
					indexout = true;
					len = 18;

				}

			} else {
				fp2.write(copy, 0, 16); // 将解密后的明文写入目标文件；
			}
			leave--; // 轮数减一；
		}
		fp1.close(); // 关闭源文件和目标文件；
		fp2.close();
		long timer = (System.nanoTime() - startTime) / 1000000;
		System.out.println("文件大小:  ....." + Length);
		System.out.println("共需时间:  ....." + timer);
	}

	public static void main(String[] args) throws Exception {
		String s = "xfire做客户端，不做上述修改，也能正常调用呢？？";
		System.out.println(s);
		String key = "abasdfasdfasdfasdfasdfsdfasdf";
		AES_Encrypt aes_en = new AES_Encrypt();
		byte[] en = aes_en.Encrypt(AES_Decrypt.ByteToChar(s.getBytes()),
				AES_Decrypt.ByteToChar(key.getBytes()));

		byte[] de = AES_Decrypt.Decrypt(AES_Decrypt.ByteToChar(en),
				AES_Decrypt.ByteToChar(key.getBytes()));

		System.out.println(new String(de));
	}
}
