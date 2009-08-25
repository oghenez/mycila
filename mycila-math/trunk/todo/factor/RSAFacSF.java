import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 *
 * Demo applet RSA-Factorization with known secret-key
 *
 * @author Kristijan Dragicevic
 * @version xx.xx.04
 *
 */

public class RSAFacSF extends Applet implements ActionListener
{
    RSAFac Faktor;
    Integer n1, b1, a1, w1;

    String strnLabel =       "Number to factorize n =\t\t\t";
    String strChSmLabel =    "Public number b =\t";
    String strRandNumLabel = "Secret number a =\t"; 
    String strW = "Choosen w with 0 < w < n:\t";

    Panel InputPanel = new Panel();
    Panel InputPanel1 = new Panel();
    Panel InputPanel2 = new Panel();
    Panel InputPanel3 = new Panel();
    Panel InputPanel31 = new Panel();
    Panel InputPanel32 = new Panel();
    Panel InputPanel33 = new Panel();
    Panel ResultPanel = new Panel();
    Panel CommentPanel = new Panel();

    Label nLabel = new Label(strnLabel);
    TextField nField = new TextField("", 7);
    Button facButton = new Button("Factorize");
    
    Label ChooseSmoothLabel = new Label(strChSmLabel);
    TextField BField = new TextField("", 5);
   
    Label RandNumLabel = new Label(strRandNumLabel);
    TextField aField = new TextField("", 7);

    Label wLabel = new Label(strW);
    TextField wField = new TextField("", 7);
   
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
	InputPanel3.setLayout(new BorderLayout());
	ResultPanel.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT));
	CommentPanel.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT));

	InputPanel3.add("North",InputPanel31);
	InputPanel3.add("Center", InputPanel32); 
       	InputPanel3.add("South", InputPanel33);

	InputPanel31.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT));
	InputPanel32.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT));
       	InputPanel33.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT));


	add("North", InputPanel);
	add("Center", ResultPanel);
	add("South", CommentPanel);

	ResultTA.setEditable(false);

	InputPanel1.add(nLabel);
	InputPanel1.add(nField);
      
 	InputPanel2.add(ChooseSmoothLabel);
	InputPanel2.add(BField);

	InputPanel31.add(RandNumLabel);
	InputPanel31.add(aField);

	InputPanel32.add(wLabel);
	InputPanel32.add(wField);
	
	InputPanel32.add(facButton);


	ResultPanel.add(exampleLabel);
	ResultPanel.add(ResultTA);

	CommentPanel.add(Result);
	CommentPanel.add(ClearButton);

	facButton.addActionListener(this);
	ClearButton.addActionListener(this);

	return;
    }

    public void actionPerformed(ActionEvent e) {
	if (e.getActionCommand() == facButton.getLabel()) {
	    n1 = new Integer(nField.getText());
	    b1 = new Integer(BField.getText());
	    a1 = new Integer(aField.getText());
	    w1 = new Integer(wField.getText());
	    ResultTA.setText("");

	    if(n1.intValue() > 30000) {
ResultTA.append("Warning! Your choosen number is very big. There might be problems");
	    }
	   
	    Faktor =
		new RSAFac(n1.intValue(), b1.intValue(), a1.intValue(),
			   w1.intValue());
	    showRes();
	   
	}

	if (e.getActionCommand() == ClearButton.getLabel()) {
	    ResultTA.setText("");
	    Result.setText("nothing factorized");
	}
	doLayout();
	return;
    }

    private void showRes(){
	ResultTA.append("gcd(w, n) = " + Faktor.x1 + "\n\n");

	if (Faktor.success1 == true) {
	    ResultTA.append("p = " + Faktor.x1 + " and q = " + 
			    n1.intValue() / Faktor.x1 + "\n");
	    Result.setText("Factorization succeeded");
	}

	else {
	    ResultTA.append("a * b - 1 = " + Faktor.count + " * " + Faktor.r +
			    "\n\nfirst v = " + 
			    ((Integer)Faktor.vList.elementAt(0)).intValue() + 
			    "\n\n");

	    for (int i = 1; i != Faktor.vList.size(); i++) {
		ResultTA.append("v" + i + " = " + 
				((Integer)Faktor.vList.elementAt(i)).intValue()
				+ "\n");
	    }

	    if (Faktor.success2 == true) {
		Result.setText("Factorization succeeded");
    ResultTA.append("\np = gcd(v"+ 
		    Faktor.vList.elementAt(Faktor.vList.size() - 1) + 
		    " + 1, n) = " + Faktor.x + " and q = " + 
		    n1.intValue() / Faktor.x + "\n");
	    }

	    else {
		Result.setText("Factorization failed");
		ResultTA.append("\nTry another w\n");
	    }
	}
    }
}
