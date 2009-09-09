import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 *
 * Demo applet (p-1)-Factorization by Pollard
 *
 * @author Kristijan Dragicevic
 * @version xx.xx.04
 *
 */

public class PmeAlgSF extends Applet implements ActionListener
{
    PmeAlg Faktor;
    Integer n1, B1, a1;

    String strnLabel =       "Number to factorize n =\t\t\t";
    String strChSmLabel =    "Choose smooth-boarder B =\t";
    String strRandNumLabel = "Choose random number a from 2 to n-1:"; 

    Panel InputPanel = new Panel();
    Panel InputPanel1 = new Panel();
    Panel InputPanel2 = new Panel();
    Panel InputPanel3 = new Panel();
    Panel ResultPanel = new Panel();
    Panel CommentPanel = new Panel();

    Label nLabel = new Label(strnLabel);
    TextField nField = new TextField("", 7);
    Button facButton = new Button("Factorize");
    
    Label ChooseSmoothLabel = new Label(strChSmLabel);
    TextField BField = new TextField("", 5);
   
    Label RandNumLabel = new Label(strRandNumLabel);
    TextField aField = new TextField("", 7);
   
    Label exampleLabel = new Label("Result (see example)");
    TextArea ResultTA = 
	new TextArea("", 20, 60, java.awt.TextArea.SCROLLBARS_BOTH);

    Label Result = new Label("nothing factorized");
    Button ClearButton = new Button("Clear");

    public void init() {
	setLayout(new BorderLayout());
	
	InputPanel.setLayout(new BorderLayout());
	InputPanel.add("North", InputPanel1);
	InputPanel.add("Center", InputPanel2);
	InputPanel.add("South", InputPanel3);
	InputPanel1.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT));
	InputPanel2.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT));
	InputPanel3.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT));
	ResultPanel.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT));
	CommentPanel.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT));



	add("North", InputPanel);
	add("Center", ResultPanel);
	add("South", CommentPanel);

	ResultTA.setEditable(false);

	InputPanel1.add(nLabel);
	InputPanel1.add(nField);
	InputPanel1.add(facButton);

	InputPanel2.add(ChooseSmoothLabel);
	InputPanel2.add(BField);

	InputPanel3.add(RandNumLabel);
	InputPanel3.add(aField);

	ResultPanel.add(exampleLabel);
	ResultPanel.add(ResultTA);

	CommentPanel.add(Result);
	CommentPanel.add(ClearButton);

	facButton.addActionListener(this);
	ClearButton.addActionListener(this);

	return;
    }

    public void actionPerformed(ActionEvent e) {

	if(e.getActionCommand() == facButton.getLabel()) {
	     n1 = new Integer(nField.getText());
	    B1 = new Integer(BField.getText());
	    a1 = new Integer(aField.getText());

	    if (!(3 < n1.intValue() && n1.intValue() < 64000) 
		|| !(2 < B1.intValue() && B1.intValue() < 64000) 
		|| !(1 < a1.intValue() && a1.intValue() < n1.intValue()))
		ResultTA.setText("No valid input.\n" +
				 "The values have to be:\n3 < n < 64000\n2" +
				 " < B < 64000\n1 < a < n");
	    else {

	 	Faktor = 
		    new PmeAlg(n1.intValue(), B1.intValue(), a1.intValue());
		showRes(); 
	    }
	}

	if(e.getActionCommand() == ClearButton.getLabel()) {
	    ResultTA.setText("");
	    Result.setText("nothing factorized");
	}

	doLayout();
	return;
    }

    private void showRes() {
	int s = Faktor.sizeof(Faktor.aList);
	ResultTA.append
	    ("\nfirst gcd(a, n) = d = " + Faktor.firstd + "\n\n");
	ResultTA.append("\tq(i)\tl(i)\ta(i)\n\n");

	for (int i = 0; i != s; i++)
	    ResultTA.append("\t" + Faktor.qList.elementAt(i) + "\t"
			    + Faktor.lList.elementAt(i) + "\t"
			    + Faktor.aList.elementAt(i) + "\n");
	
	if (Faktor.success == true) {
	    Result.setText("Factorization succeeded");
	    
	    if ( Faktor.d == Faktor.firstd) 
		ResultTA.append("\nfound non-trivial factor d =\t"
				+ Faktor.d + "\nanother non-trivial factor:\t"
				+ (Faktor.n / Faktor.d));
	    
	    else {
		ResultTA.append("\nfound non-trivial factor:\nd = gcd( "
				+ Faktor.aList.elementAt(s - 1)
				+ " - 1, n) = " + Faktor.d
				+ "\nanother non-trivial factor is:\t"
				+ (Faktor.n / Faktor.d));
	    }
     		
	}

	else {
	    Result.setText("no factor found");
	    ResultTA.append("\n Try with another a");
	}
    }
}










