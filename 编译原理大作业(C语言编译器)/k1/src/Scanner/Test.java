package Scanner;

public class Test
{
	public static void main(String[] args)
	{
		Token token = new Token();
		
		String fileName = "C:\\Users\\汤国频\\Desktop\\1.txt";
		
		Scanner.init_scanner(fileName);
		
		System.out.println("记号类别              字符串              常数值             函数指针");
		
		System.out.println("--------------------------------------------");
		
		while(true)
		{
			token = Scanner.GetToken();
			
			if (token.token_Type != Token_Type.NONTOKEN)
			{
				System.out.printf("%-10s %-6s %-10f %-10s\n",token.token_Type, token.lexeme, token.value, token.func);
			}
			else
			{
				System.out.println("--------------------------------------------");
				break;
			}
		}
		
		Scanner.close_scanner();
	}
}
