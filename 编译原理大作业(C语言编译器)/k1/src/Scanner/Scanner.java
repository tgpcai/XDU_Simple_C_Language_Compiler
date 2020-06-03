package Scanner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Scanner
{
	public static RandomAccessFile randomAccessFile = null;
	public static StringBuffer stringBuffer = null;
	public static int now_pos = 0;
	public static int line_num = 0;     //用于记录出错的行数
	
	/**
	 * 初始化词法分析器
	 * @param fileName 所需读取的文本的路径
	 */
	public static void init_scanner(String fileName)          
	{
		try
		{
			randomAccessFile = new RandomAccessFile(fileName, "rw");
		} catch (FileNotFoundException e)
		{			
			e.printStackTrace();
		}
		try
		{
			randomAccessFile.seek(0);
			stringBuffer = new StringBuffer();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭词法分析器
	 */
	public static void close_scanner()
	{
		try
		{
			randomAccessFile.close();
		} catch (IOException e)
		{			
			e.printStackTrace();
		}
	}

	/**
	 * 获取字符
	 * @return
	 */
	public static Token GetToken()
	{
		Token token = new Token();
		int read_char_from_file;	//从文件中读取的字符			
		
		Empty_Token_Buff();           //清空记号缓冲区
		token.lexeme = stringBuffer.toString(); //将缓冲区的字符串赋予记号中的lexeme
		while(true)					//过滤程序中的空格、回车、TAB符等，遇到文件结束返回空记号
		{
			read_char_from_file = get_char();
			//System.out.println((char)read_char_from_file);  //用于测试结果！！！！调试错误
			if (read_char_from_file == 65535)
			{
				token.token_Type = Token_Type.NONTOKEN;
				return token;
			}
			if (read_char_from_file == '\n')
			{
				line_num++;
			}
			if (!Character.isWhitespace(read_char_from_file))
			{
				break;
			}
		}	
		
		add_char_to_stringbuilder((char) read_char_from_file); // 若不是回车、空格、TAB、文件结束符等，则加入字符缓冲区

		if (Character.isLetter(read_char_from_file)) // 若char是A-Za-z，则它一定是函数、关键字、PI、E等
		{
			while (true) // 最大化匹配字符
			{
				read_char_from_file = get_char();
				if (Character.isDigit(read_char_from_file) || Character.isLetter(read_char_from_file)) //
				{
					add_char_to_stringbuilder((char) read_char_from_file);
				} 
				else
				{
					break;
				}
			}
			Back_Char((char) read_char_from_file);
			token = Judge_key_token(stringBuffer.toString());			//判断所给字符串是否在字符表中
			token.lexeme = stringBuffer.toString();					//若字符串在字符表中，则将字符串赋予记号lexeme的属性
			
			return token;
		}
		else if (Character.isDigit(read_char_from_file)) // 如果是一个数字，则一定是一个常量
		{
			while (true) // 最大化匹配字符
			{
				read_char_from_file = get_char();
				if (Character.isDigit(read_char_from_file))
				{
					add_char_to_stringbuilder((char) read_char_from_file);
				} else
				{
					break;
				}
			}
			if (read_char_from_file == '.') // 常量可能为小数
			{
				add_char_to_stringbuilder((char) read_char_from_file);
				while (true) // 最大化匹配字符
				{
					read_char_from_file = get_char();
					if (Character.isDigit(read_char_from_file))
					{
						add_char_to_stringbuilder((char) read_char_from_file);
					} else
					{
						break;
					}
				}
			}
			Back_Char((char) read_char_from_file);
			token.token_Type = Token_Type.CONST_ID;
			token.value = Double.parseDouble(stringBuffer.toString()); // 将字符串小数变为十进制小数
			return token;
		} 
		else // 若不是字母和数字，则一定是符号
		{
			switch (read_char_from_file)
			{
			case ',':
				token.token_Type = Token_Type.COMMA;
				break;
			case ';':
				token.token_Type = Token_Type.SEMICO;
				break;
			case '(':
				token.token_Type = Token_Type.L_BRACKET;
				break;
			case ')':
				token.token_Type = Token_Type.R_BRACKET;
				break;
			case '+':
				token.token_Type = Token_Type.PLUS;
				break;
			case '-':
				read_char_from_file = get_char();
				if (read_char_from_file == '-') // 遇到注释 --
				{
					while (read_char_from_file != '\n' && read_char_from_file != -1) // 最大匹配字符串
					{
						read_char_from_file = get_char();
					}
					Back_Char((char) read_char_from_file);
					return GetToken(); // 读完全部注释，从注释后接着读取与分析字符
				} 
				else
				{
					Back_Char((char) read_char_from_file); // 匹配减号
					token.token_Type = Token_Type.MINUS;
					break;
				}
			case '/':
				read_char_from_file = get_char();
				if (read_char_from_file == '/') // 遇到注释 //
				{
					while (read_char_from_file != '\n' && read_char_from_file != -1) // 最大匹配字符串
					{
						read_char_from_file = get_char();
					}
					Back_Char((char) read_char_from_file);
					return GetToken(); // 读完全部注释，从注释后接着读取与分析字符
				} 
				else
				{
					Back_Char((char) read_char_from_file);
					token.token_Type = Token_Type.DIV;
					break;
				}
			case '*':
				read_char_from_file = get_char();
				if (read_char_from_file == '*') // 匹配乘方
				{
					token.token_Type = Token_Type.POWER;
					break;
				} 
				else // 匹配乘法
				{
					Back_Char((char)read_char_from_file);
					token.token_Type = Token_Type.MUL;
					break;
				}
			default:
				token.token_Type = Token_Type.ERRTOKEN;
				break;
			}
			token.lexeme = stringBuffer.toString();
		}
		return token;
	}

	
	/**
	 * 判断所给字符串是否在字符表中
	 * @param string
	 * @return
	 */
	public static Token Judge_key_token(String string)
	{
		int loop;
		Token err_token = new Token();
		
		for(loop = 0; loop < Token_Table.token_table.length; loop++)
		{
			if (Token_Table.token_table[loop].lexeme.equals(string)) //如果传入的字符串在字符表中能找到则返回该记号
			{
				return Token_Table.token_table[loop];
			}
		}
		stringBuffer = new StringBuffer();		//更新字符缓冲区
		err_token.token_Type = Token_Type.ERRTOKEN; //如果在字符表中lexeme找不到传入的字符串，则返回错误记号
		return err_token;
	}

	/**
	 * 若此时的字符不是文本的开始字符，则将读取位置回退一个
	 * @param read_char_from_file
	 */
	public static void Back_Char(char read_char_from_file)
	{
		if ((int)read_char_from_file != -1)
		{
			now_pos--;
		}
	}

	/**
	 * 将读入的字符加入到字符缓冲区中
	 * @param read_char_from_file
	 */
	public static void add_char_to_stringbuilder(char read_char_from_file)
	{
		stringBuffer.append(read_char_from_file);
	}
	

	/**
	 * 从文本中获取下一个字符
	 * @return
	 */
	public static int get_char()
	{
		try
		{
			randomAccessFile.seek(now_pos);
			char next_char = (char)randomAccessFile.read();
			now_pos++;
			return Character.toUpperCase(next_char);
		} catch (IOException e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	
	/**
	 * 清空字符缓冲区
	 */
	public static void Empty_Token_Buff()
	{
		stringBuffer = new StringBuffer();
	}
}
