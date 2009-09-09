import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * demo applet Pratt prime number test 
 *
 * @author   Holger Schmid
 * @version  28.05.99
 */
public class Pratt extends Applet implements ActionListener
{
   Prime Zahl;
   Vector rVect, qVect;
	Vector Ergebnis;
	
	Panel InputPanel = new Panel();
	Panel Result1Panel = new Panel();
	Panel Result2Panel = new Panel();
	
	Label pLabel = new Label("number to test p= ");
   TextField pField = new TextField("",5);
	Button cButton = new Button("Test");
	
	Label ResultTATitel = new Label("Result (see example)");
	TextArea ResultTA = new TextArea("",20,60, java.awt.TextArea.SCROLLBARS_BOTH);
	
   Label Result = new Label("nothing tested");
   
   public void init()
   {
      // set layout
      setLayout (new BorderLayout());
   	InputPanel.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT));
   	Result1Panel.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT));
      Result2Panel.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT));
   	add("North", InputPanel);
   	add("Center", Result1Panel);
   	add("South", Result2Panel);
   		
      // add components
   	ResultTA.setEditable(false);
   	InputPanel.add(pLabel);
      InputPanel.add(pField);
   	InputPanel.add(cButton);
   	Result1Panel.add(ResultTATitel);
   	Result1Panel.add(ResultTA);
      Result2Panel.add(Result);
   	
      // set action listeners
   	cButton.addActionListener(this);
   	
      doLayout();
   	return;
   }//init

   public void actionPerformed(ActionEvent e)
   {
      Integer p;

 	if (e.getActionCommand()==cButton.getLabel())
      {// button clicked
         int i, qCount, rCount;
      	
      	// test input data
      	p = new Integer(pField.getText());
         if (p.intValue()>46000)
         {
            ResultTA.setText("Only values up to 46000 possible.");
            return;
         }
         else
         {
         	Zahl = new Prime(p.intValue());
            if (Zahl.isPrime())
            {
         	   ResultTA.setText("");
         	   ResultForQ(p.intValue(), 1);
            	Result.setText(p+" is prime.");
            }
         	else
            {
            	ResultTA.setText("no witness found");
            	Result.setText(p+" is not prime.");
            }
         }
      }
   	doLayout();
      return;
   }
	
	void ResultForQ(int q, int Tabs)
   {// recursive function for each prime factor
   	int i, j, qCount, rTemp;
      Prime Temp;
      Vector qTemp;
   	
   	Temp = new Prime(q);
   	qTemp = Temp.getQList();
   	qCount = qTemp.size();
   	rTemp = Temp.getRZeuge();
   	
   	if (q==2)
      {
         ResultTA.append("\tis prime");	
      }
   	else
      {
         if (rTemp==0)
         {
            ResultTA.append("no witness found");
            return;
         }
         if (Tabs>1) ResultTA.append("\t");
   	   ResultTA.append("r="+rTemp+"\n");
         for (i=0; i<qCount; i++)
            {
               for (j=1; j<Tabs; j++) ResultTA.append("\t");
            	ResultTA.append("q"+i+"="+qTemp.elementAt(i));
            	ResultForQ(((Integer)qTemp.elementAt(i)).intValue(), Tabs+1);
            	ResultTA.append("\n");
            }
      }
   }
}//class Pratt