package Parser;

import Scanner.Func;
import Scanner.Scanner;
import Scanner.Token;
import Scanner.Token_Type;

public class ParserImp implements ParserInterfaces
{
	public CaseParmPtr parameter = new CaseParmPtr(0.0);   //参数T默认值为0
	public int line_color = 0;  
	Token token;						
	public TreeNode start_ptr = new TreeNode();			//for语句中的5个Expression
	public TreeNode end_ptr = new TreeNode();
	public TreeNode step_ptr = new TreeNode();
	public TreeNode x_ptr = new TreeNode();
	public TreeNode y_ptr = new TreeNode();    
	public TreeNode angle_ptr = new TreeNode();

	Scanner scanner;
	public ParserImp()
	{
		scanner = new Scanner();
	}
	
	@Override
	public void Parser(String file_name)		//程序入口
	{
		Enter("parser");
		Scanner.init_scanner(file_name);
		FetchToken(); 	   					//获取记号
		Program();							//进入递归下降分析
		Scanner.close_scanner();
		Exit("parser");
	}
	
	@Override
	public void FetchToken()                //获取记号
	{
		token = Scanner.GetToken();
		if (token.token_Type == Token_Type.ERRTOKEN)
		{
			SyntaxError(1);   //非法记号
		}
	}

	@Override
	public void MatchToken(Token_Type token_type)  //匹配终结符
	{
		if (token.token_Type != token_type)
		{
			SyntaxError(2);   //不是预期记号
		}
		FetchToken();
	}

	@Override
	public void SyntaxError(int case_value)   //打印错误信息
	{
		switch (case_value)
		{
		case 1:
			System.err.println("Line: "+Scanner.line_num+" "+"非法记号"+" "+token.lexeme);  //Scanner.line_num，用于记录出错的行数
			break;
		case 2:
			System.err.println("Line: "+Scanner.line_num+" "+token.lexeme+" "+"不是预期记号");
			break;
		}
	}

	@Override
	public void PrintSyntaxTree(TreeNode root, int height)   //打印语法树
	{
        for (int a = 1; a <= height; a++)
		{
			System.out.print("  ");
		}
        switch (root.OpCode){
            case PLUS:
                System.out.println("+");
                break;
            case MINUS:
                System.out.println("-");
                break;
            case MUL:
                System.out.println("*");
                break;
            case DIV:
                System.out.println("/");
                break;
            case POWER:
                System.out.println("**");
                break;
            case FUNC:
                System.out.println("func");
//            	System.out.println(root.case_func.func);
                break;
            case CONST_ID:
                System.out.println(root.case_const);
                break;
            case T:
                System.out.println("T");
                break;
            default:
                System.out.println("非法节点");
        }
        
        if (root.OpCode == Token_Type.CONST_ID || root.OpCode == Token_Type.T)
		{
			return;
		}
        
        if (root.OpCode == Token_Type.FUNC)
		{
			PrintSyntaxTree(root.case_func.child, height + 1);
		}
        else
        {
        	PrintSyntaxTree(root.case_operator.left, height + 1);
        	PrintSyntaxTree(root.case_operator.right, height + 1);
        }
        
        return;
	}

	/**
	 * 进入递归下降分析
	 */
	@Override
	public void Program()
	{
		Enter("program");
		while(token.token_Type != Token_Type.NONTOKEN)
		{
			Statement();
			MatchToken(Token_Type.SEMICO);
		}
		Exit("program");
	}

	@Override
	public void Statement()
	{
		Enter("statement");
		switch (token.token_Type)
		{
		case ORIGIN:
			OriginStatement();
			break;
		case SCALE:
			ScaleStatement();
			break;
		case ROT:
			RotStatement();
			break;
		case FOR:
			ForStatement();
			break;
		case COLOR:
			ColorStatement();
			break;
		default:
			System.out.println("不存在该类型语句");
//			SyntaxError(2);
			break;
		}
		Exit("statement");
	}

	@Override
	public void ColorStatement()
	{
        Enter("color_statement");
        MatchToken(Token_Type.COLOR);
        MatchToken(Token_Type.IS);
        Colors();
        Exit("color_statement");
	}
	
	@Override
	public void Colors()
	{
		Enter("colors");
		System.out.println("color");
		if (token.token_Type == Token_Type.RED)
		{
			line_color = 0;
		}
		else if (token.token_Type == Token_Type.BLACK)
		{
			line_color = 1;
		}
		else 
		{
			SyntaxError(2);
		}
		MatchToken(token.token_Type);
		Exit("color");
	}
	
	@Override
	public void OriginStatement()		//匹配origin语句
	{
		Enter("origin_statement");
		Match("ORIGIN");
		MatchToken(Token_Type.ORIGIN);
		Match("IS");
		MatchToken(Token_Type.IS);
		Match("(");
		MatchToken(Token_Type.L_BRACKET);
		x_ptr = Expression();
		Match(",");
		MatchToken(Token_Type.COMMA);
		y_ptr = Expression();
		Match(")");
		MatchToken(Token_Type.R_BRACKET);
		Exit("origin_statement");
	}

	@Override
	public void RotStatement()			//匹配rot语句
	{
		Enter("rot_statement");
		Match("ROT");
		MatchToken(Token_Type.ROT);
		Match("IS");
		MatchToken(Token_Type.IS);
		angle_ptr = Expression();
		Exit("rot_statement");
	}

	@Override
	public void ScaleStatement() 	//匹配scale语句
	{
		Enter("scale_statement");
		Match("SCALE");
		MatchToken(Token_Type.SCALE);
		Match("IS");
		MatchToken(Token_Type.IS);
		Match("(");
		MatchToken(Token_Type.L_BRACKET);
		x_ptr = Expression();
		Match(",");
		MatchToken(Token_Type.COMMA);
		y_ptr = Expression();
		Match(")");
		MatchToken(Token_Type.R_BRACKET);
		Exit("scale_statement");
	}

	@Override
	public void ForStatement()				//匹配for语句
	{
		Enter("for_statment");
		Match("FOR");
		MatchToken(Token_Type.FOR);
		Match("T");
		MatchToken(Token_Type.T);
		Match("FROM");
		MatchToken(Token_Type.FROM);
		start_ptr = Expression();
		Match("TO");
		MatchToken(Token_Type.TO);
		end_ptr = Expression();
		Match("STEP");
		MatchToken(Token_Type.STEP);
		step_ptr = Expression();
		Match("DRAW");
		MatchToken(Token_Type.DRAW);
		Match("(");
		MatchToken(Token_Type.L_BRACKET);
		x_ptr = Expression();
		Match(",");
		MatchToken(Token_Type.COMMA);
		y_ptr = Expression();
		Match(")");
		MatchToken(Token_Type.R_BRACKET);
		Exit("for_statement");
	}

	@Override
	public TreeNode Expression()             //匹配expression,Expression 	→ Term   { ( PLUS | MINUS ) Term } 
	{
		TreeNode left;
		TreeNode right;
		Token_Type token_type;
		
		Enter("expression");
		left = Term();
		while(token.token_Type == Token_Type.PLUS || token.token_Type == Token_Type.MINUS)
		{
			token_type = token.token_Type;
			MatchToken(token_type);
			right = Term();
			left = MakeTreeNode(token_type,left,right);
		}
        PrintSyntaxTree(left,1);
        Exit("expression");
        return left;
	}

	@Override
	public TreeNode Term()   //匹配term,Term       	→ Factor { ( MUL  | DIV ) Factor }
	{
		TreeNode left;
		TreeNode right;
		Token_Type token_type;
		
		left = Factor();
		while(token.token_Type == Token_Type.MUL || token.token_Type == Token_Type.DIV)
		{
			token_type = token.token_Type;
			MatchToken(token_type);
			right = Factor();
			left = MakeTreeNode(token_type,left,right);
		}	
		return left;
	}

	@Override
	public TreeNode Factor()   //Factor  	→ ( PLUS | MINUS ) Factor | Component  一元加一元减
	{
		TreeNode left;
		TreeNode right;
		
		if (token.token_Type == Token_Type.PLUS)
		{
			MatchToken(Token_Type.PLUS);
			right = Factor();
		}
		else if (token.token_Type == Token_Type.MINUS)
		{
			MatchToken(Token_Type.MINUS);
			right = Factor();
			left = new TreeNode();
			left.OpCode = Token_Type.CONST_ID;
			left.case_const = 0;
			right = MakeTreeNode(Token_Type.MINUS,left,right);
		}
		else
		{
			right = Component();
		}
		return right;
	}

	@Override
	public TreeNode Component()    //Component 	→ Atom [ POWER Component ]
	{
		TreeNode left;
		TreeNode right;
		
		left = Atom();
		if (token.token_Type == Token_Type.POWER)
		{
			MatchToken(Token_Type.POWER);
			right = Component();		// 递归调用component以实现POWER的右结合性质
			left = MakeTreeNode(Token_Type.POWER,left,right);
		}
		return left;
	}

	/**
	 * Atom → CONST_ID
      | T
	  | FUNC L_BRACKET Expression R_BRACKET
      | L_BRACKET Expression R_BRACKET 
	 */
	@Override
	public TreeNode Atom()
	{
		Token t = token;
		TreeNode t_ptr = new TreeNode();
		TreeNode tmp = new TreeNode();
		
		switch (token.token_Type)
		{
		case CONST_ID:
			MatchToken(Token_Type.CONST_ID);
			t_ptr = MakeTreeNode(Token_Type.CONST_ID,t.value);
			break;
		case T:
			MatchToken(Token_Type.T);
			t_ptr = MakeTreeNode(Token_Type.T, t.value);
			break;
		case FUNC:
			MatchToken(Token_Type.FUNC);
			MatchToken(Token_Type.L_BRACKET);
			tmp = Expression();
			t_ptr = MakeTreeNode(Token_Type.FUNC,t.func,tmp);
			MatchToken(Token_Type.R_BRACKET);
			break;			
		default:
			SyntaxError(2);
			break;
		}
		return t_ptr;
	}

	@Override
	public TreeNode MakeTreeNode(Token_Type token_Type, TreeNode left, TreeNode right)
	{
		TreeNode t = new TreeNode();
		t.OpCode = token_Type;
		
		t.case_operator.left = left;
		t.case_operator.right = right;
		return t;
	}

	@Override
	public TreeNode MakeTreeNode(Token_Type token_Type)
	{
		TreeNode t = new TreeNode();
		t.OpCode = token_Type;
		t.case_parmPtr = parameter;
		return t;
	}

	@Override
	public TreeNode MakeTreeNode(Token_Type token_Type, double value)
	{
		TreeNode t = new TreeNode();
		t.OpCode = token_Type;

		if (token_Type == Token_Type.CONST_ID)
		{
			t.case_const = value;
		}
		else
		{
			t.case_parmPtr = parameter;
		}
		return t;
	}

	@Override
	public TreeNode MakeTreeNode(Token_Type token_Type, Func caseParmPtr, TreeNode value)
	{
		TreeNode t = new TreeNode();
		t.OpCode = token_Type;
		
		t.case_func.func = caseParmPtr;
		t.case_func.child = value;
		return t;
	}

	@Override
	public void Enter(String s)
	{
		System.out.println("Enter " + s);
//		System.out.printf("%-10s %-6s\n","Enter",s);
	}

	@Override
	public void Exit(String s)
	{
		System.out.println("Exit " + s);
//		System.out.printf("%-10s %-6s\n","Exit",s);
	}

	@Override
	public void Match(String s)
	{
		System.out.println("MatchToken " + s);
//		System.out.printf("%-10s %-6s\n","MatchToken",s);
	}

}
