import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * demo applet Lucas prime number test 
 *
 * @author   Holger Schmid
 * @version  23.02.2000
 */
public class Lucas extends Applet implements ActionListener, ItemListener
{
   Prime Zahl;
   Vector rVect, qVect;
	Vector Ergebnis;
	
	Panel InputPanel = new Panel();
   Panel ResultParent = new Panel();
   Panel ResultTop = new Panel();
   Panel ResultCenter = new Panel();
	Panel ResultBottom = new Panel();
	
	Label pLabel = new Label("number to test p= ");
   TextField pField = new TextField("",5);
	Button cButton = new Button("Test");
	
	Label ResultTATitel = new Label("prime factors of p-1");
	Label rListTitel = new Label("all r, that meet condition 1)");
	TextArea ResultTA = new TextArea("",10,20, java.awt.TextArea.SCROLLBARS_VERTICAL_ONLY);
   List rList = new List(11);
	
   Label Result = new Label("nothing tested");
   
   public void init()
   {
      // set layout
      setLayout (new BorderLayout());
   	InputPanel.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT));
   	ResultParent.setLayout(new BorderLayout());
      ResultTop.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT));
   	ResultCenter.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT));
      ResultBottom.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT));
      add("North", InputPanel);
   	add("South", ResultParent);
      ResultParent.add("North", ResultTop);
      ResultParent.add("Center", ResultCenter);
      ResultParent.add("South", ResultBottom);
   
      // add components
   	ResultTA.setEditable(false);
   	InputPanel.add(pLabel);
      InputPanel.add(pField);
   	InputPanel.add(cButton);
   	ResultTop.add(rListTitel);
   	ResultTop.add(ResultTATitel);
   	ResultCenter.add(rList);
   	ResultCenter.add(ResultTA);
      ResultBottom.add(Result);
   	
      // set action listeners
      cButton.addActionListener(this);
      rList.addItemListener(this);
   	
      doLayout();
   	return;
   }

	public void itemStateChanged(ItemEvent e)
      {
      int i, selR, qCount;
      
      if (e.getStateChange()==java.awt.event.ItemEvent.SELECTED)
         {
         qVect = Zahl.getQList();

         if (rList.getItemCount()>0)
            {
            selR = rList.getSelectedIndex();

            qCount=qVect.size();
            ResultTA.setText("");
            for (i=0; i<qCount; i++)
               {
               ResultTA.append((String)(qVect.elementAt(i).toString()+"\t"));
      	      ResultTA.append((String)((Vector)Ergebnis.elementAt(selR)).elementAt(i).toString()+"\n");
               }
            }
         }
      return;
      }
	   	
   public void actionPerformed(ActionEvent e)
   {
      Integer p;

   	if (e.getActionCommand()==cButton.getLabel())
      {// test if button clicked
         int i, qCount, rCount;

         // reset      
         ResultTA.setText("");
         rList.select(0); // needed because of bug in IE and Netscape
         rList.removeAll();

         // test input data and create new prime object
      	p = new Integer(pField.getText());
         if (p.intValue()>46000)
         {
            ResultTA.setText("Only values up to 46000 possible.");
            return;
         }
         else
         {
            Zahl = new Prime(p.intValue());
         }
      
      	rVect = Zahl.getRList();
      	qVect = Zahl.getQList();
      	Ergebnis = Zahl.LucasComplete();
      	qCount=qVect.size();
      	rCount=rVect.size();

         if (rCount==0)
         {
         	rList.add("no values found");
         }
      	else
         {
            for (i=0; i<rCount; i++)
            {// create selection list
            	rList.add((String)(rVect.elementAt(i).toString()));
            }
         	// select first element of list and show results
            ResultTA.setText("");
            for (i=0; i<qCount; i++)
            {
               ResultTA.append("     "+(String)(qVect.elementAt(i).toString()+"\t"));
               ResultTA.append((String)((Vector)Ergebnis.elementAt(0)).elementAt(i).toString()+"\n");
            }
         }
         rList.select(0);

         if (Zahl.isPrime())
         {
            Result.setText(p+" is prime.");
         }
   	   else
         {
            Result.setText(p+" is not prime.");      	
         }
      }
   	doLayout();
      return;
   }
}