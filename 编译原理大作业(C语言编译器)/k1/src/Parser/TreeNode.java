package Parser;

import Scanner.Func;
import Scanner.Token_Type;

/**
 * 
 * @author tgp
 *
 */
public class TreeNode
{
	public Token_Type OpCode;  // 记号(算符)种类
	public CaseOperator case_operator; //二元运算
	public CaseFunc case_func; //1个孩子，默认为函数调用
	public double case_const;  //常量
	public CaseParmPtr case_parmPtr = new CaseParmPtr(0); //参数T，默认初始值为0；
	
	public TreeNode()
	{
		case_operator = new CaseOperator();
		case_func = new CaseFunc();
	}
	
	/**
	 * 二元运算有左右两个孩子
	 * @author tgp
	 *
	 */
	public class CaseOperator
	{
		public TreeNode left;
		public TreeNode right;
	}
	
	
	/**
	 * 函数调用，一个孩子，同时记录函数地址，为以后语义分析提供基础
	 * @author tgp
	 *
	 */
	public class CaseFunc
	{
		public TreeNode child;
		public Func func;
	}
 
}
