package Funcs;
import java.lang.Math;
import Scanner.Func;

public class Func_Exp implements Func
{

	@Override
	public double calculate(double num)
	{
		return Math.exp(num);
	}

}
