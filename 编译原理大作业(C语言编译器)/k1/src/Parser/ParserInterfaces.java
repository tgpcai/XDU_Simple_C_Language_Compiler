package Parser;

import Scanner.Func;
import Scanner.Token_Type;

public interface ParserInterfaces
{
	/**
	 * 分析器所需的辅助子程序
	 */
	public abstract void FetchToken(); //获取记号
	public abstract void MatchToken(Token_Type token_Type);	//匹配终结符
	public abstract void SyntaxError(int case_value); //出错处理
	public abstract void PrintSyntaxTree(TreeNode root,int indent); //打印语法树 

	/**
	 * 主要产生式的递归子程序
	 */
	public abstract void Parser(String file_name);
	public abstract void Program();
	public abstract void Statement();
	public abstract void OriginStatement();
	public abstract void RotStatement();
	public abstract void ScaleStatement();
	public abstract void ForStatement();
    public abstract void ColorStatement();
    public abstract void Colors();
	/**
	 * 为了消除左递归与左因子，所以额外定义了以下几个函数
	 * @return
	 */
    public abstract TreeNode Expression();
	public abstract TreeNode Term();
	public abstract TreeNode Factor();
	public abstract TreeNode Component();
	public abstract TreeNode Atom();
	
	/**
	 * 构造语法树的节点
	 */
	public abstract TreeNode MakeTreeNode(Token_Type token_Type,TreeNode left,TreeNode right);		//二元运算
	public abstract TreeNode MakeTreeNode(Token_Type token_Type);			//叶子结点，变量T
	public abstract TreeNode MakeTreeNode(Token_Type token_Type,double value);     //叶子结点，常数
	public abstract TreeNode MakeTreeNode(Token_Type token_Type,Func caseParmPtr,TreeNode value); //函数以及函数地址
	
	/**
	 * 嵌入测试语句
	 */
	public abstract void Enter(String s);
	public abstract void Exit(String s);
	public abstract void Match(String s); 
	
}
