package Funcs;
import java.lang.Math;
import Scanner.Func;

public class Func_Tan implements Func
{
	@Override
	public double calculate(double num)
	{
		return Math.tan(num);
	}

}
