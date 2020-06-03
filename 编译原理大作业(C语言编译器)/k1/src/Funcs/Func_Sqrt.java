package Funcs;
import java.lang.Math;
import Scanner.Func;

public class Func_Sqrt implements Func
{

	@Override
	public double calculate(double num)
	{
		return Math.sqrt(num);
	}

}
