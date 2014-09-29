/**
 * AES加密解密工具包v1.1
 * 工具包说明
 * setKeyWithHex(String):用以16进制显示的字符串设置密钥，字符串长度需为32.
 * setKeyWithStr(String):用以字节显示的字符串设置密钥，字符串长度需为16.
 * runEncryptIntoHex(String):将字符串用AES加密，密文以16进制显示的字符串返回.
 * runEncryptIntoStr(String):将字符串用AES加密，密文以字节显示的字符串返回.
 * runDecryptWithHex(String):将以16进制显示的字符串密文解密，明文以字节显示的字符串返回.
 * runDecryptWithStr(String):将以字节显示的字符串密文解密，明文以字节显示的字符串返回.
 * 
 * 
 */
package com.web.api.demo;

/**
 * @author Erik Yu
 * 
 */

public class MyAES {
	// 16进制字符集
	private char[] charset = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'A', 'B', 'C', 'D', 'E', 'F' };
	// 变换各轮所用常数
	private byte[] constant = { 0x00 - 128, 0x01 - 128, 0x02 - 128, 0x04 - 128,
			0x08 - 128, 0x10 - 128, 0x20 - 128, 0x40 - 128, 0x80 - 128,
			0x1B - 128, 0x36 - 128, };
	// s盒
	private byte[] sBox = { 99 - 128, 124 - 128, 119 - 128, 123 - 128,
			242 - 128, 107 - 128, 111 - 128, 197 - 128, 48 - 128, 1 - 128,
			103 - 128, 43 - 128, 254 - 128, 215 - 128, 171 - 128, 118 - 128,
			202 - 128, 130 - 128, 201 - 128, 125 - 128, 250 - 128, 89 - 128,
			71 - 128, 240 - 128, 173 - 128, 212 - 128, 162 - 128, 175 - 128,
			156 - 128, 164 - 128, 114 - 128, 192 - 128, 183 - 128, 253 - 128,
			147 - 128, 38 - 128, 54 - 128, 63 - 128, 247 - 128, 204 - 128,
			52 - 128, 165 - 128, 229 - 128, 241 - 128, 113 - 128, 216 - 128,
			49 - 128, 21 - 128, 4 - 128, 199 - 128, 35 - 128, 195 - 128,
			24 - 128, 150 - 128, 5 - 128, 154 - 128, 7 - 128, 18 - 128,
			128 - 128, 226 - 128, 235 - 128, 39 - 128, 178 - 128, 117 - 128,
			9 - 128, 131 - 128, 44 - 128, 26 - 128, 27 - 128, 110 - 128,
			90 - 128, 160 - 128, 82 - 128, 59 - 128, 214 - 128, 179 - 128,
			41 - 128, 227 - 128, 47 - 128, 132 - 128, 83 - 128, 209 - 128,
			0 - 128, 237 - 128, 32 - 128, 252 - 128, 177 - 128, 91 - 128,
			106 - 128, 203 - 128, 190 - 128, 57 - 128, 74 - 128, 76 - 128,
			88 - 128, 207 - 128, 208 - 128, 239 - 128, 170 - 128, 251 - 128,
			67 - 128, 77 - 128, 51 - 128, 133 - 128, 69 - 128, 249 - 128,
			2 - 128, 127 - 128, 80 - 128, 60 - 128, 159 - 128, 168 - 128,
			81 - 128, 163 - 128, 64 - 128, 143 - 128, 146 - 128, 157 - 128,
			56 - 128, 245 - 128, 188 - 128, 182 - 128, 218 - 128, 33 - 128,
			16 - 128, 255 - 128, 243 - 128, 210 - 128, 205 - 128, 12 - 128,
			19 - 128, 236 - 128, 95 - 128, 151 - 128, 68 - 128, 23 - 128,
			196 - 128, 167 - 128, 126 - 128, 61 - 128, 100 - 128, 93 - 128,
			25 - 128, 115 - 128, 96 - 128, 129 - 128, 79 - 128, 220 - 128,
			34 - 128, 42 - 128, 144 - 128, 136 - 128, 70 - 128, 238 - 128,
			184 - 128, 20 - 128, 222 - 128, 94 - 128, 11 - 128, 219 - 128,
			224 - 128, 50 - 128, 58 - 128, 10 - 128, 73 - 128, 6 - 128,
			36 - 128, 92 - 128, 194 - 128, 211 - 128, 172 - 128, 98 - 128,
			145 - 128, 149 - 128, 228 - 128, 121 - 128, 231 - 128, 200 - 128,
			55 - 128, 109 - 128, 141 - 128, 213 - 128, 78 - 128, 169 - 128,
			108 - 128, 86 - 128, 244 - 128, 234 - 128, 101 - 128, 122 - 128,
			174 - 128, 8 - 128, 186 - 128, 120 - 128, 37 - 128, 46 - 128,
			28 - 128, 166 - 128, 180 - 128, 198 - 128, 232 - 128, 221 - 128,
			116 - 128, 31 - 128, 75 - 128, 189 - 128, 139 - 128, 138 - 128,
			112 - 128, 62 - 128, 181 - 128, 102 - 128, 72 - 128, 3 - 128,
			246 - 128, 14 - 128, 97 - 128, 53 - 128, 87 - 128, 185 - 128,
			134 - 128, 193 - 128, 29 - 128, 158 - 128, 225 - 128, 248 - 128,
			152 - 128, 17 - 128, 105 - 128, 217 - 128, 142 - 128, 148 - 128,
			155 - 128, 30 - 128, 135 - 128, 233 - 128, 206 - 128, 85 - 128,
			40 - 128, 223 - 128, 140 - 128, 161 - 128, 137 - 128, 13 - 128,
			191 - 128, 230 - 128, 66 - 128, 104 - 128, 65 - 128, 153 - 128,
			45 - 128, 15 - 128, 176 - 128, 84 - 128, 187 - 128, 22 - 128 };
	// 逆s盒
	private byte[] invSBox = { 82 - 128, 9 - 128, 106 - 128, 213 - 128,
			48 - 128, 54 - 128, 165 - 128, 56 - 128, 191 - 128, 64 - 128,
			163 - 128, 158 - 128, 129 - 128, 243 - 128, 215 - 128, 251 - 128,
			124 - 128, 227 - 128, 57 - 128, 130 - 128, 155 - 128, 47 - 128,
			255 - 128, 135 - 128, 52 - 128, 142 - 128, 67 - 128, 68 - 128,
			196 - 128, 222 - 128, 233 - 128, 203 - 128, 84 - 128, 123 - 128,
			148 - 128, 50 - 128, 166 - 128, 194 - 128, 35 - 128, 61 - 128,
			238 - 128, 76 - 128, 149 - 128, 11 - 128, 66 - 128, 250 - 128,
			195 - 128, 78 - 128, 8 - 128, 46 - 128, 161 - 128, 102 - 128,
			40 - 128, 217 - 128, 36 - 128, 178 - 128, 118 - 128, 91 - 128,
			162 - 128, 73 - 128, 109 - 128, 139 - 128, 209 - 128, 37 - 128,
			114 - 128, 248 - 128, 246 - 128, 100 - 128, 134 - 128, 104 - 128,
			152 - 128, 22 - 128, 212 - 128, 164 - 128, 92 - 128, 204 - 128,
			93 - 128, 101 - 128, 182 - 128, 146 - 128, 108 - 128, 112 - 128,
			72 - 128, 80 - 128, 253 - 128, 237 - 128, 185 - 128, 218 - 128,
			94 - 128, 21 - 128, 70 - 128, 87 - 128, 167 - 128, 141 - 128,
			157 - 128, 132 - 128, 144 - 128, 216 - 128, 171 - 128, 0 - 128,
			140 - 128, 188 - 128, 211 - 128, 10 - 128, 247 - 128, 228 - 128,
			88 - 128, 5 - 128, 184 - 128, 179 - 128, 69 - 128, 6 - 128,
			208 - 128, 44 - 128, 30 - 128, 143 - 128, 202 - 128, 63 - 128,
			15 - 128, 2 - 128, 193 - 128, 175 - 128, 189 - 128, 3 - 128,
			1 - 128, 19 - 128, 138 - 128, 107 - 128, 58 - 128, 145 - 128,
			17 - 128, 65 - 128, 79 - 128, 103 - 128, 220 - 128, 234 - 128,
			151 - 128, 242 - 128, 207 - 128, 206 - 128, 240 - 128, 180 - 128,
			230 - 128, 115 - 128, 150 - 128, 172 - 128, 116 - 128, 34 - 128,
			231 - 128, 173 - 128, 53 - 128, 133 - 128, 226 - 128, 249 - 128,
			55 - 128, 232 - 128, 28 - 128, 117 - 128, 223 - 128, 110 - 128,
			71 - 128, 241 - 128, 26 - 128, 113 - 128, 29 - 128, 41 - 128,
			197 - 128, 137 - 128, 111 - 128, 183 - 128, 98 - 128, 14 - 128,
			170 - 128, 24 - 128, 190 - 128, 27 - 128, 252 - 128, 86 - 128,
			62 - 128, 75 - 128, 198 - 128, 210 - 128, 121 - 128, 32 - 128,
			154 - 128, 219 - 128, 192 - 128, 254 - 128, 120 - 128, 205 - 128,
			90 - 128, 244 - 128, 31 - 128, 221 - 128, 168 - 128, 51 - 128,
			136 - 128, 7 - 128, 199 - 128, 49 - 128, 177 - 128, 18 - 128,
			16 - 128, 89 - 128, 39 - 128, 128 - 128, 236 - 128, 95 - 128,
			96 - 128, 81 - 128, 127 - 128, 169 - 128, 25 - 128, 181 - 128,
			74 - 128, 13 - 128, 45 - 128, 229 - 128, 122 - 128, 159 - 128,
			147 - 128, 201 - 128, 156 - 128, 239 - 128, 160 - 128, 224 - 128,
			59 - 128, 77 - 128, 174 - 128, 42 - 128, 245 - 128, 176 - 128,
			200 - 128, 235 - 128, 187 - 128, 60 - 128, 131 - 128, 83 - 128,
			153 - 128, 97 - 128, 23 - 128, 43 - 128, 4 - 128, 126 - 128,
			186 - 128, 119 - 128, 214 - 128, 38 - 128, 225 - 128, 105 - 128,
			20 - 128, 99 - 128, 85 - 128, 33 - 128, 12 - 128, 125 - 128 };
	// E表
	private byte[] eTable = { 1 - 128, 3 - 128, 5 - 128, 15 - 128, 17 - 128,
			51 - 128, 85 - 128, 255 - 128, 26 - 128, 46 - 128, 114 - 128,
			150 - 128, 161 - 128, 248 - 128, 19 - 128, 53 - 128, 95 - 128,
			225 - 128, 56 - 128, 72 - 128, 216 - 128, 115 - 128, 149 - 128,
			164 - 128, 247 - 128, 2 - 128, 6 - 128, 10 - 128, 30 - 128,
			34 - 128, 102 - 128, 170 - 128, 229 - 128, 52 - 128, 92 - 128,
			228 - 128, 55 - 128, 89 - 128, 235 - 128, 38 - 128, 106 - 128,
			190 - 128, 217 - 128, 112 - 128, 144 - 128, 171 - 128, 230 - 128,
			49 - 128, 83 - 128, 245 - 128, 4 - 128, 12 - 128, 20 - 128,
			60 - 128, 68 - 128, 204 - 128, 79 - 128, 209 - 128, 104 - 128,
			184 - 128, 211 - 128, 110 - 128, 178 - 128, 205 - 128, 76 - 128,
			212 - 128, 103 - 128, 169 - 128, 224 - 128, 59 - 128, 77 - 128,
			215 - 128, 98 - 128, 166 - 128, 241 - 128, 8 - 128, 24 - 128,
			40 - 128, 120 - 128, 136 - 128, 131 - 128, 158 - 128, 185 - 128,
			208 - 128, 107 - 128, 189 - 128, 220 - 128, 127 - 128, 129 - 128,
			152 - 128, 179 - 128, 206 - 128, 73 - 128, 219 - 128, 118 - 128,
			154 - 128, 181 - 128, 196 - 128, 87 - 128, 249 - 128, 16 - 128,
			48 - 128, 80 - 128, 240 - 128, 11 - 128, 29 - 128, 39 - 128,
			105 - 128, 187 - 128, 214 - 128, 97 - 128, 163 - 128, 254 - 128,
			25 - 128, 43 - 128, 125 - 128, 135 - 128, 146 - 128, 173 - 128,
			236 - 128, 47 - 128, 113 - 128, 147 - 128, 174 - 128, 233 - 128,
			32 - 128, 96 - 128, 160 - 128, 251 - 128, 22 - 128, 58 - 128,
			78 - 128, 210 - 128, 109 - 128, 183 - 128, 194 - 128, 93 - 128,
			231 - 128, 50 - 128, 86 - 128, 250 - 128, 21 - 128, 63 - 128,
			65 - 128, 195 - 128, 94 - 128, 226 - 128, 61 - 128, 71 - 128,
			201 - 128, 64 - 128, 192 - 128, 91 - 128, 237 - 128, 44 - 128,
			116 - 128, 156 - 128, 191 - 128, 218 - 128, 117 - 128, 159 - 128,
			186 - 128, 213 - 128, 100 - 128, 172 - 128, 239 - 128, 42 - 128,
			126 - 128, 130 - 128, 157 - 128, 188 - 128, 223 - 128, 122 - 128,
			142 - 128, 137 - 128, 128 - 128, 155 - 128, 182 - 128, 193 - 128,
			88 - 128, 232 - 128, 35 - 128, 101 - 128, 175 - 128, 234 - 128,
			37 - 128, 111 - 128, 177 - 128, 200 - 128, 67 - 128, 197 - 128,
			84 - 128, 252 - 128, 31 - 128, 33 - 128, 99 - 128, 165 - 128,
			244 - 128, 7 - 128, 9 - 128, 27 - 128, 45 - 128, 119 - 128,
			153 - 128, 176 - 128, 203 - 128, 70 - 128, 202 - 128, 69 - 128,
			207 - 128, 74 - 128, 222 - 128, 121 - 128, 139 - 128, 134 - 128,
			145 - 128, 168 - 128, 227 - 128, 62 - 128, 66 - 128, 198 - 128,
			81 - 128, 243 - 128, 14 - 128, 18 - 128, 54 - 128, 90 - 128,
			238 - 128, 41 - 128, 123 - 128, 141 - 128, 140 - 128, 143 - 128,
			138 - 128, 133 - 128, 148 - 128, 167 - 128, 242 - 128, 13 - 128,
			23 - 128, 57 - 128, 75 - 128, 221 - 128, 124 - 128, 132 - 128,
			151 - 128, 162 - 128, 253 - 128, 28 - 128, 36 - 128, 108 - 128,
			180 - 128, 199 - 128, 82 - 128, 246 - 128, 1 - 128 };
	// L表
	private byte[] lTable = { 0 - 128, 0 - 128, 25 - 128, 1 - 128, 50 - 128,
			2 - 128, 26 - 128, 198 - 128, 75 - 128, 199 - 128, 27 - 128,
			104 - 128, 51 - 128, 238 - 128, 223 - 128, 3 - 128, 100 - 128,
			4 - 128, 224 - 128, 14 - 128, 52 - 128, 141 - 128, 129 - 128,
			239 - 128, 76 - 128, 113 - 128, 8 - 128, 200 - 128, 248 - 128,
			105 - 128, 28 - 128, 193 - 128, 125 - 128, 194 - 128, 29 - 128,
			181 - 128, 249 - 128, 185 - 128, 39 - 128, 106 - 128, 77 - 128,
			228 - 128, 166 - 128, 114 - 128, 154 - 128, 201 - 128, 9 - 128,
			120 - 128, 101 - 128, 47 - 128, 138 - 128, 5 - 128, 33 - 128,
			15 - 128, 225 - 128, 36 - 128, 18 - 128, 240 - 128, 130 - 128,
			69 - 128, 53 - 128, 147 - 128, 218 - 128, 142 - 128, 150 - 128,
			143 - 128, 219 - 128, 189 - 128, 54 - 128, 208 - 128, 206 - 128,
			148 - 128, 19 - 128, 92 - 128, 210 - 128, 241 - 128, 64 - 128,
			70 - 128, 131 - 128, 56 - 128, 102 - 128, 221 - 128, 253 - 128,
			48 - 128, 191 - 128, 6 - 128, 139 - 128, 98 - 128, 179 - 128,
			37 - 128, 226 - 128, 152 - 128, 34 - 128, 136 - 128, 145 - 128,
			16 - 128, 126 - 128, 110 - 128, 72 - 128, 195 - 128, 163 - 128,
			182 - 128, 30 - 128, 66 - 128, 58 - 128, 107 - 128, 40 - 128,
			84 - 128, 250 - 128, 133 - 128, 61 - 128, 186 - 128, 43 - 128,
			121 - 128, 10 - 128, 21 - 128, 155 - 128, 159 - 128, 94 - 128,
			202 - 128, 78 - 128, 212 - 128, 172 - 128, 229 - 128, 243 - 128,
			115 - 128, 167 - 128, 87 - 128, 175 - 128, 88 - 128, 168 - 128,
			80 - 128, 244 - 128, 234 - 128, 214 - 128, 116 - 128, 79 - 128,
			174 - 128, 233 - 128, 213 - 128, 231 - 128, 230 - 128, 173 - 128,
			232 - 128, 44 - 128, 215 - 128, 117 - 128, 122 - 128, 235 - 128,
			22 - 128, 11 - 128, 245 - 128, 89 - 128, 203 - 128, 95 - 128,
			176 - 128, 156 - 128, 169 - 128, 81 - 128, 160 - 128, 127 - 128,
			12 - 128, 246 - 128, 111 - 128, 23 - 128, 196 - 128, 73 - 128,
			236 - 128, 216 - 128, 67 - 128, 31 - 128, 45 - 128, 164 - 128,
			118 - 128, 123 - 128, 183 - 128, 204 - 128, 187 - 128, 62 - 128,
			90 - 128, 251 - 128, 96 - 128, 177 - 128, 134 - 128, 59 - 128,
			82 - 128, 161 - 128, 108 - 128, 170 - 128, 85 - 128, 41 - 128,
			157 - 128, 151 - 128, 178 - 128, 135 - 128, 144 - 128, 97 - 128,
			190 - 128, 220 - 128, 252 - 128, 188 - 128, 149 - 128, 207 - 128,
			205 - 128, 55 - 128, 63 - 128, 91 - 128, 209 - 128, 83 - 128,
			57 - 128, 132 - 128, 60 - 128, 65 - 128, 162 - 128, 109 - 128,
			71 - 128, 20 - 128, 42 - 128, 158 - 128, 93 - 128, 86 - 128,
			242 - 128, 211 - 128, 171 - 128, 68 - 128, 17 - 128, 146 - 128,
			217 - 128, 35 - 128, 32 - 128, 46 - 128, 137 - 128, 180 - 128,
			124 - 128, 184 - 128, 38 - 128, 119 - 128, 153 - 128, 227 - 128,
			165 - 128, 103 - 128, 74 - 128, 237 - 128, 222 - 128, 197 - 128,
			49 - 128, 254 - 128, 24 - 128, 13 - 128, 99 - 128, 140 - 128,
			128 - 128, 192 - 128, 247 - 128, 112 - 128, 7 - 128 };

	// 密钥 以及默认值
	private byte[] key = { 0x00 - 128, 0x04 - 128, 0x08 - 128, 0x0c - 128,
			0x01 - 128, 0x05 - 128, 0x09 - 128, 0x0d - 128, 0x02 - 128,
			0x06 - 128, 0x0a - 128, 0x0e - 128, 0x03 - 128, 0x07 - 128,
			0x0b - 128, 0x0f - 128, };

	// 混合列矩阵
	private byte[][] matrix = {
			{ 0x02 - 128, 0x03 - 128, 0x01 - 128, 0x01 - 128 },
			{ 0x01 - 128, 0x02 - 128, 0x03 - 128, 0x01 - 128 },
			{ 0x01 - 128, 0x01 - 128, 0x02 - 128, 0x03 - 128 },
			{ 0x03 - 128, 0x01 - 128, 0x01 - 128, 0x02 - 128 } };
	// 混合列逆操作举证
	private byte[][] invMatrix = {
			{ 0x0e - 128, 0x0b - 128, 0x0d - 128, 0x09 - 128 },
			{ 0x09 - 128, 0x0e - 128, 0x0b - 128, 0x0d - 128 },
			{ 0x0d - 128, 0x09 - 128, 0x0e - 128, 0x0b - 128 },
			{ 0x0b - 128, 0x0d - 128, 0x09 - 128, 0x0e - 128 } };

	public MyAES() {

	}

	// 两个byte类型按位相与，返回结果
	private byte xorByte(byte a, byte b) {
		byte r;
		r = (byte) ((((int) a + 128) ^ ((int) b + 128)) - 128);
		return r;
	}

	// 以下表x，y为参数的s盒变换
	private byte substitute(int x, int y) {
		return sBox[x * 16 + y];
	}

	// 以byte为参数的s盒变换
	private byte substitute(byte inByte) {
		byte outByte;
		int x, y, val;
		val = (int) inByte + 128;
		x = val / 16;
		y = val - x * 16;
		outByte = sBox[x * 16 + y];
		return outByte;
	}

	// s盒逆变换
	private byte invSubstitute(byte inByte) {
		byte outByte;
		int x, y, val;
		val = (int) inByte + 128;
		x = val / 16;
		y = val - x * 16;
		outByte = invSBox[x * 16 + y];
		return outByte;
	}

	// 查E表
	private byte getETable(byte inByte) {
		byte outByte;
		int x, y, val;
		val = (int) inByte + 128;
		x = val / 16;
		y = val % 16;
		outByte = eTable[x * 16 + y];
		return outByte;
	}

	// 查L表
	private byte getLTable(byte inByte) {
		byte outByte;
		int x, y, val;
		val = (int) inByte + 128;
		x = val / 16;
		y = val - x * 16;
		outByte = lTable[x * 16 + y];
		return outByte;
	}

	// 利用伽利略场求乘积
	private byte product(byte a, byte b) {
		if (a == -128 || b == -128) {
			return (byte) (-128);
		}
		byte e, result;
		int val;
		val = getLTable(a) + 128 + getLTable(b) + 128;

		if (val > 255)
			val = val - 255;
		e = (byte) (val - 128);
		result = getETable(e);
		return result;
	}

	// 调试用显示方块
	/*
	 * private void show(byte[][] s){ int tt; for (int i=0;i<4;i++){ for (int
	 * j=0;j<4;j++){ tt=(int)s[i][j]+128;
	 * 
	 * int ta,tb; ta=tt/16; tb=tt-ta*16; char pa,pb; pa=charset[ta];
	 * pb=charset[tb]; System.out.print(pa); System.out.print(pb);
	 * System.out.print(' '); } System.out.println(' '); }
	 * System.out.println("-------------"); }
	 */
	// 16进制转为字节
	private String hexToString(String hexStr) {
		int l = hexStr.length();
		byte[] bytes = new byte[l / 2];
		for (int i = 0; i < l / 2; i++) {
			char c1, c2;
			int a, b, v;
			c1 = hexStr.charAt(i * 2);
			c2 = hexStr.charAt(i * 2 + 1);
			if (c1 >= '0' && c1 <= '9') {
				a = c1 - '0';
			} else {
				a = c1 - 'A' + 10;
			}
			if (c2 >= '0' && c2 <= '9') {
				b = c2 - '0';
			} else {
				b = c2 - 'A' + 10;
			}
			v = a * 16 + b;
			bytes[i] = (byte) (v - 128);
		}
		String str = new String(bytes);
		return str;
	}

	// 字节转为16进制
	/*
	 * private String stringToHex(String str){ String hexStr=""; byte[]
	 * bytes=str.getBytes(); int l=bytes.length; for (int i=0;i<l;i++){
	 * hexStr+=charset[(bytes[i]+128)/16]; hexStr+=charset[(bytes[i]+128)%16]; }
	 * return hexStr; }
	 */
	// 16进制转为chars
	private String hexToChars(String hexStr) {
		String str = "";
		int l = hexStr.length();
		for (int i = 0; i < l / 2; i++) {
			char c1, c2;
			int a, b, v;
			c1 = hexStr.charAt(i * 2);
			c2 = hexStr.charAt(i * 2 + 1);
			if (c1 >= '0' && c1 <= '9') {
				a = c1 - '0';
			} else {
				a = c1 - 'A' + 10;
			}
			if (c2 >= '0' && c2 <= '9') {
				b = c2 - '0';
			} else {
				b = c2 - 'A' + 10;
			}
			v = a * 16 + b;
			str += (char) (v);
		}
		return str;
	}

	// chars转为16进制

	private String charsToHex(String str) {
		String hexStr = "";
		int l = str.length();
		int v;
		for (int i = 0; i < l; i++) {
			v = str.charAt(i);
			hexStr += charset[v / 16];
			hexStr += charset[v % 16];
		}
		return hexStr;
	}

	// 用16进制数导入密钥
	public boolean setKeyWithHex(String s) {
		int l = s.length();
		// 1 检查长度
		if (l != 32) {
			// System.out.println("密钥长度需为128位");
			return false;
		}
		// 2 检查密钥是否为16进制数
		for (int i = 0; i < 32; i++) {
			char t = s.charAt(i);
			if ((t < '0' || t > '9') && (t < 'A' || t > 'F')
					&& (t < 'a' || t > 'f')) {
				// System.out.println("密钥需为16进制数");
				return false;
			}
		}
		// 3 导入密钥
		for (int i = 0; i < 16; i++) {
			int va, vb;
			char t;
			t = s.charAt(i * 2);
			if (t >= '0' && t <= '9') {
				va = t - '0';
			} else if (t >= 'A' && t <= 'F') {
				va = t - 'A' + 10;
			} else if (t >= 'a' && t <= 'f') {
				va = t - 'a' + 10;
			} else {
				// System.out.println("密钥需为16进制数 2");
				return false;
			}

			t = s.charAt(i * 2 + 1);
			if (t >= '0' && t <= '9') {
				vb = t - '0';
			} else if (t >= 'A' && t <= 'F') {
				vb = t - 'A' + 10;
			} else if (t >= 'a' && t <= 'f') {
				vb = t - 'a' + 10;
			} else {
				return false;
			}
			key[i] = (byte) (va * 16 + vb - 128);
		}
		// System.out.println("设置密钥成功");
		return true;
	}

	// 用字节导入密钥
	public boolean setKeyWithStr(String s) {
		int l = s.length();
		// 1 检查长度
		if (l != 16) {
			// System.out.println("密钥长度需为128位,即16字节");
			return false;
		}

		// 2 导入密钥
		for (int i = 0; i < 16; i++) {
			key[i] = (byte) (s.charAt(i) - 128);
		}
		// System.out.println("设置密钥成功");
		return true;
	}

	// 获取密钥
	public String getKey() {
		String keyStr = "";
		int a, b;
		for (int i = 0; i < 16; i++) {
			a = (key[i] + 128) / 16;
			b = (key[i] + 128) % 16;
			keyStr += charset[a];
			keyStr += charset[b];
		}
		return keyStr;
	}

	// 以16字明文块为输入，进行aes加密，输出为加密后的16进制密文
	private String encrypt(byte[] plainText) {
		// 一次性初始化处理
		// 扩展16字节密钥
		String cipherText = "";
		byte[] temp = new byte[4];
		byte[][][] keyEx = new byte[11][4][4];
		byte[][] state, nextState;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < 4; k++) {
					keyEx[0][j][k] = key[j * 4 + k];
				}
			}
		}
		for (int i = 1; i < 11; i++) {
			for (int j = 0; j < 4; j++) {
				// 索引为4的倍数的情况
				if (j == 0) {
					// rotate
					temp[0] = keyEx[i - 1][1][3];
					temp[1] = keyEx[i - 1][2][3];
					temp[2] = keyEx[i - 1][3][3];
					temp[3] = keyEx[i - 1][0][3];
					// Substitute
					for (int si = 0; si < 4; si++) {
						int x, y, val;
						val = (int) temp[si] + 128;
						x = val / 16;
						y = val - x * 16;
						temp[si] = substitute(x, y);

					}
					// xor Constant
					temp[0] = xorByte(temp[0], constant[i]);
				} else {
					// 索引不为4的倍数的情况
					// tmp
					temp[0] = keyEx[i][0][j - 1];
					temp[1] = keyEx[i][1][j - 1];
					temp[2] = keyEx[i][2][j - 1];
					temp[3] = keyEx[i][3][j - 1];
				}
				// w[i-4] xor tmp
				keyEx[i][0][j] = xorByte(keyEx[i - 1][0][j], temp[0]);
				keyEx[i][1][j] = xorByte(keyEx[i - 1][1][j], temp[1]);
				keyEx[i][2][j] = xorByte(keyEx[i - 1][2][j], temp[2]);
				keyEx[i][3][j] = xorByte(keyEx[i - 1][3][j], temp[3]);
			}
		}

		// 16字节明文块的一次性初始化
		state = new byte[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				state[i][j] = plainText[i * 4 + j];
			}
		}

		// state数组与密钥块进行XOR运算
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				state[i][j] = xorByte(state[i][j], keyEx[0][i][j]);
			}
		}
		// show(keyEx[0]);

		// 第一部分测试
		/*
		 * int tt; for (int i=0;i<4;i++){ for (int j=0;j<4;j++){
		 * tt=(int)state[i][j]+128;
		 * 
		 * int ta,tb; ta=tt/16; tb=tt-ta*16; char pa,pb; pa=charset[ta];
		 * pb=charset[tb]; System.out.print(pa); System.out.print(pb);
		 * System.out.print(' '); } System.out.println(' '); } //
		 */
		// 第一部分测试结束

		// 轮次开始
		for (int rounds = 1; rounds <= 10; rounds++) {
			// a 对每个明文字节应用S盒 checked
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					state[i][j] = substitute(state[i][j]);
				}
			}
			// show(state);

			// b 把明文块的k行旋转k个字节 checked
			for (int k = 0; k < 4; k++) {
				byte swapByte;
				for (int l = 0; l < k; l++) {
					swapByte = state[k][0];
					for (int m = 0; m < 3; m++) {
						state[k][m] = state[k][m + 1];
					}
					state[k][3] = swapByte;
				}
			}
			// show(state);
			// c 进行混合列操作 checked
			nextState = new byte[4][4];
			if (rounds != 10) {
				for (int j = 0; j < 4; j++) {
					for (int i = 0; i < 4; i++) {
						nextState[i][j] = xorByte(
								product(state[0][j], matrix[i][0]),
								product(state[1][j], matrix[i][1]));
						nextState[i][j] = xorByte(nextState[i][j],
								product(state[2][j], matrix[i][2]));
						nextState[i][j] = xorByte(nextState[i][j],
								product(state[3][j], matrix[i][3]));
					}
				}
			} else {
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 4; j++) {
						nextState[i][j] = state[i][j];
					}
				}
			}
			// show(nextState);
			// d 把state数组与密钥块进行XOR运算 checked
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					state[i][j] = xorByte(nextState[i][j], keyEx[rounds][i][j]);
				}
			}
		}
		// show(state);

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				int x, y, val;
				val = (int) state[i][j] + 128;
				x = val / 16;
				y = val % 16;
				cipherText += charset[x];
				cipherText += charset[y];
			}
		}
		// System.out.println(cipherText);
		return cipherText;
	}

	// 以16字节密文块为输入，进行aes解密，输出为解密后的16进制明文
	private String decrypt(byte[] cipherText) {
		String plainText = "";
		byte[] temp = new byte[4];
		byte[][][] keyEx = new byte[11][4][4];
		byte[][] state, nextState;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < 4; k++) {
					keyEx[0][j][k] = key[j * 4 + k];
				}
			}
		}
		for (int i = 1; i < 11; i++) {
			for (int j = 0; j < 4; j++) {
				// 索引为4的倍数的情况
				if (j == 0) {
					// rotate
					temp[0] = keyEx[i - 1][1][3];
					temp[1] = keyEx[i - 1][2][3];
					temp[2] = keyEx[i - 1][3][3];
					temp[3] = keyEx[i - 1][0][3];
					// Substitute
					for (int si = 0; si < 4; si++) {
						int x, y, val;
						val = (int) temp[si] + 128;
						x = val / 16;
						y = val - x * 16;
						temp[si] = substitute(x, y);

					}
					// xor Constant
					temp[0] = xorByte(temp[0], constant[i]);
				} else {
					// 索引不为4的倍数的情况
					// tmp
					temp[0] = keyEx[i][0][j - 1];
					temp[1] = keyEx[i][1][j - 1];
					temp[2] = keyEx[i][2][j - 1];
					temp[3] = keyEx[i][3][j - 1];
				}
				// w[i-4] xor tmp
				keyEx[i][0][j] = xorByte(keyEx[i - 1][0][j], temp[0]);
				keyEx[i][1][j] = xorByte(keyEx[i - 1][1][j], temp[1]);
				keyEx[i][2][j] = xorByte(keyEx[i - 1][2][j], temp[2]);
				keyEx[i][3][j] = xorByte(keyEx[i - 1][3][j], temp[3]);
			}
		}

		// 16字节密文块的一次性初始化
		state = new byte[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				state[i][j] = cipherText[i * 4 + j];
			}
		}

		// state数组与密钥块进行XOR运算
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				state[i][j] = xorByte(state[i][j], keyEx[10][i][j]);
			}
		}
		// show(state);
		// 轮次开始
		for (int rounds = 9; rounds >= 0; rounds--) {
			// a 逆旋转
			for (int k = 0; k < 4; k++) {
				byte swapByte;
				for (int l = 0; l < k; l++) {
					swapByte = state[k][3];
					for (int m = 3; m > 0; m--) {
						state[k][m] = state[k][m - 1];
					}
					state[k][0] = swapByte;
				}
			}
			// show(state);
			// b 逆s盒变换
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					state[i][j] = invSubstitute(state[i][j]);
				}
			}
			// show(state);
			// c 与密钥块xor
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					state[i][j] = xorByte(state[i][j], keyEx[rounds][i][j]);
				}
			}
			// show(state);
			// d 混合列逆操作
			nextState = new byte[4][4];
			if (rounds != 0) {
				for (int j = 0; j < 4; j++) {
					for (int i = 0; i < 4; i++) {
						nextState[i][j] = xorByte(
								product(state[0][j], invMatrix[i][0]),
								product(state[1][j], invMatrix[i][1]));
						nextState[i][j] = xorByte(nextState[i][j],
								product(state[2][j], invMatrix[i][2]));
						nextState[i][j] = xorByte(nextState[i][j],
								product(state[3][j], invMatrix[i][3]));
					}
				}
				for (int j = 0; j < 4; j++) {
					for (int i = 0; i < 4; i++) {
						state[i][j] = nextState[i][j];
					}
				}
			}
			// show(state);
		}

		// show(state);

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				int x, y, val;
				val = (int) state[i][j] + 128;
				x = val / 16;
				y = val % 16;
				plainText += charset[x];
				plainText += charset[y];
			}
		}
		return plainText;

	}

	// 以16进制字符串为输入，进行明文加密

	public String runEncryptIntoHex(String plainText) throws Exception {
		byte[] plainTextByte = plainText.getBytes();
		String cipherText = "";
		int l = plainTextByte.length;
		byte[] plainTextBlock = new byte[16];
		int pos = 0;
		while (pos < l) {
			for (int i = 0; i < 16; i++) {
				if (pos < l) {
					plainTextBlock[i] = plainTextByte[pos];
				} else {
					plainTextBlock[i] = (byte) (' ');
				}
				pos++;
			}
			cipherText += encrypt(plainTextBlock);
		}
		return cipherText;
	}

	/*
	 * String runEncryptIntoHex(String plainText){ String cipherText=""; byte[]
	 * plainTextByte=plainText.getBytes(); int l=plainText.length(); byte[]
	 * plainTextBlock=new byte[16]; int pos=0; while(pos<l){ for (int
	 * i=0;i<16;i++){ if (pos<l){
	 * plainTextBlock[i]=(byte)(plainText.charAt(pos)-128);
	 * System.out.print(plainTextBlock[i]); System.out.print(" ");
	 * System.out.println(plainTextByte[pos]); }else{
	 * plainTextBlock[i]=(byte)(' '-128); } pos++; }
	 * cipherText+=encrypt(plainTextBlock); } return cipherText; }
	 */
	// 以字节为单位的字符串为输入，进行明文加密
	public String runEncryptIntoStr(String plainText) throws Exception {
		String cipherText;
		String cipherTextStr;
		cipherText = runEncryptIntoHex(plainText);
		cipherTextStr = hexToChars(cipherText);
		return cipherTextStr;
	}

	// 以16进制字符串为输入，进行密文解密
	public String runDecryptWithHex(String cipherText) {
		String plainText = "";
		int l = cipherText.length();
		byte[] cipherTextBlock = new byte[16];
		int pos = 0;
		while (pos < l) {
			for (int i = 0; i < 16; i++) {
				int a, b;
				a = cipherText.charAt(pos);
				b = cipherText.charAt(pos + 1);
				if (a >= '0' && a <= '9') {
					a = a - '0';
				} else {
					a = a - 'A' + 10;
				}
				if (b >= '0' && b <= '9') {
					b = b - '0';
				} else {
					b = b - 'A' + 10;
				}
				cipherTextBlock[i] = (byte) (a * 16 + b - 128);
				pos += 2;
			}
			plainText += hexToString(decrypt(cipherTextBlock));
		}
		return plainText;
	}

	// 以字节为单位的字符串为输入，进行密文解密
	public String runDecryptWithStr(String cipherText) {
		System.out.println(cipherText);
		String cipherTextHex;
		String plainText = "";
		cipherTextHex = charsToHex(cipherText);
		plainText = runDecryptWithHex(cipherTextHex);
		return plainText;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO 自动生成方法存根
		/*
		 * byte[] pt={ 0x98-128,0x04-128,0x08-128,0x0c-128,
		 * 0x76-128,0xaa-128,0xbb-128,0x0d-128,
		 * 0x54-128,0xcc-128,0xdd-128,0x0e-128,
		 * 0x32-128,0x07-128,0x0b-128,0x0f-128, };
		 * 
		 * byte[] ct={ 0x5e-128,0x04-128,0xcd-128,0xfe-128,
		 * 0xbf-128,0x75-128,0xb6-128,0x9c-128,
		 * 0x58-128,0x7f-128,0xbc-128,0xb9-128,
		 * 0xed-128,0x66-128,0xe0-128,0x52-128, }; t=""; t+=
		 * (char)(0x98);t+=(char)(0x04);t+=(char)(0x08);t+=(char)(0x0c);t+=
		 * (char)(0x76);t+=(char)(0xaa);t+=(char)(0xbb);t+=(char)(0x0d);t+=
		 * (char)(0x54);t+=(char)(0xcc);t+=(char)(0xdd);t+=(char)(0x0e);t+=
		 * (char)(0x32);t+=(char)(0x07);t+=(char)(0x0b);t+=(char)(0x0f);
		 */
		MyAES a = new MyAES();

		// a.setKeyWithHex("0B6C9669FF7880E30BA8621EABDE289B"); //用16进制设置密钥
		a.setKeyWithStr("hello,world!good"); // 用字节字符串设置密钥

		/**/
		String s = "总结： 看来这种错误还是对soap协议以及wsdl描述文件的格式不熟导致的";
		String p = a.runEncryptIntoHex(s); // 加密成16进制
		String q = a.runDecryptWithHex(p); // 从16进制中解密
		System.out.println(s); // 输出明文
		System.out.println(p); // 输出16进制密文
		System.out.println(q); // 输出解密后的明文

		/*
		 * s="什么？what are you doing?"; //明文为 “what are you doing?”
		 * System.out.println(s); //输出明文 p=a.runEncryptIntoStr(s); //加密成字节字符串
		 * System.out.println(p); //输出字节字符串密文 q=a.runDecryptWithStr(p);
		 * //从字节字符串中解密 System.out.println(q); //输出解密后的明文
		 */
	}
}
