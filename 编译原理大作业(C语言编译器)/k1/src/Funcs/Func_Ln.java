package Funcs;
import java.lang.Math;
import Scanner.Func;

public class Func_Ln implements Func
{

	@Override
	public double calculate(double num)
	{
		return Math.log(num);
	}

}
