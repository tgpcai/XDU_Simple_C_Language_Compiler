package Scanner;
/**
 * 
 * @author tgp
 *
 */
public class Token
{
	public Token_Type token_Type; //类别
	public String lexeme;		  //属性，原始输入的字符串
	public double value;		  //属性，若记号是常数则存常数的值
	public Func func;			  //属性，若记号是函数则调用函数接口
	
	public Token(Token_Type token_Type, String lexeme, double value, Func func)
	{
		super();
		this.token_Type = token_Type;
		this.lexeme = lexeme;
		this.value = value;
		this.func = func;
	}
	
	public Token()
	{
		
	}
}	
