package Main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;


/**
 *
 * 程序入口
 *
 */
public class Draw extends JFrame {

	public int size;
	public double time;
    private Graphics jg;
    private Color rectColor = new Color(0xf5f5f5);
    private JTextField textField;
    private JTextField textField_1;


    /**
     * DrawSee构造方法
     */
    public Draw() 
    {
    	Font font = new Font("Dialog", Font.PLAIN, 12);
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements())
		{
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof javax.swing.plaf.FontUIResource)
			{
				UIManager.put(key, font);
			}
		}
		
        Container p = getContentPane();
        setSize(700,700);
        setVisible(true);
        p.setBackground(rectColor);
        getContentPane().setLayout(null);
        
        JLabel label = new JLabel("请输入你要设置的点的大小：");
        label.setBounds(162, 13, 195, 18);
        getContentPane().add(label);
        
        textField = new JTextField();
        textField.setBounds(364, 11, 86, 24);
        p.add(textField);
        textField.setColumns(10);
        
        
        JButton button = new JButton("确定");
        button.setBounds(477, 51, 63, 27);
        button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		setsize();
        	}
        });
        getContentPane().add(button);
        
        JLabel label_1 = new JLabel("请输入你要设置的绘图速度：");
        label_1.setBounds(161, 53, 195, 18);
        getContentPane().add(label_1);
        
        textField_1 = new JTextField();
        textField_1.setBounds(365, 52, 86, 24);
        getContentPane().add(textField_1);
        textField_1.setColumns(10);
        setResizable(false);
        setLocationRelativeTo(null);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        jg = this.getGraphics();
        jg.translate(-20,0);
        jg.setColor(Color.RED);
        paintComponents(jg);
    }

    public void setsize()   //设置点的大小以及绘图速度
	{
    	String s = textField.getText();
    	size = Integer.parseInt(s);
    	time = Double.parseDouble(textField_1.getText());
        Main.semanticImp.Parser("C:\\Users\\汤国频\\Desktop\\test.txt");
	}

	public void drawPoint(int x,int y)
    {
        jg.drawLine(x,y,x,y);
    }
    public void drawManyPoint(int x,int y,int color)
    {
      if (color == 0)
      {
    	  Graphics2D g2d = (Graphics2D)jg;
    	  g2d.drawLine(x,y,x,y);
    	  g2d.setStroke(new BasicStroke(size));
    	  try
		{
			Thread.sleep((long)time);
		} catch (InterruptedException e)
		{
			
			e.printStackTrace();
		}
      }
      else 
      {
    	  Graphics2D g2d = (Graphics2D)jg;
    	  g2d.setColor(Color.BLACK);
    	  g2d.setStroke(new BasicStroke(size));
    	  try
		{
			Thread.sleep((long)time);
		} catch (InterruptedException e)
		{
			
			e.printStackTrace();
		}
    	  g2d.drawLine(x, y, x, y);        
      }
    }
    
    public void draw(Point point,int color) 
    {
        drawManyPoint((int)point.getX(),(int)point.getY(),color);
    }
}
