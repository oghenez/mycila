import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 *
 * Demo applet Quadratic-Sieve-Factorization
 *
 * @author Kristijan Dragicevic
 * @version xx.xx.04
 *
 */

public class QuadAlgSF extends Applet implements ActionListener
{
    Integer n1, t1;
    QuadAlg aim;

    String strnLabel =       "Number to factorize n =\t\t\t";
    String strChSmLabel =    "Size of factor base S =\t"; 

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
    TextField tField = new TextField("", 5);
    
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
	InputPanel2.add(tField);

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
	    t1 = new Integer(tField.getText());
	    aim = new QuadAlg(n1.intValue(), t1.intValue());
	    showRes(); 
	}
	

	if(e.getActionCommand() == ClearButton.getLabel()) {
	    ResultTA.setText("");
	    Result.setText("nothing factorized");
	}

	doLayout();
	return;
    }

    private void showRes() {
	boolean firstVi = true;
	boolean firstLj = true;
	ResultTA.append("Choosen Prime Base S =\n\t");
	for (int i = 0; i != aim.primeBase.size(); i++) 
	    ResultTA.append(((Integer)aim.primeBase.elementAt(i)).intValue()
			    + "\t");
	
	ResultTA.append("\nm = rootOf(" + n1.intValue() + ") = " + aim.m + "\n");
	ResultTA.append("Data collect for the first " + (t1.intValue() + 1) 
			+ " of x for which q(x) is " +
			((Integer)aim.primeBase.elementAt(aim.primeBase.size() - 1)).intValue()
			+ "-smooth\ni\tx\tq(x)\tai\tvi\n");

	for (int i = 0; i != aim.primeBase.size() + 1; i++) {
	    ResultTA.append((i + 1) + "\t" + aim.xVector.elementAt(i) + "\t" 
			    + aim.bi.elementAt(i) + "\t" + aim.ai.elementAt(i) + "\t(");
	    for (int j = 0; j != aim.vi.Csize() - 1; j++)
		ResultTA.append( (aim.vi.get(i, j)).intValue() + ", ");
	    ResultTA.append( ((Integer)aim.vi.get(i, aim.vi.Csize() - 1)).intValue() + ")\n" );
	}

	if (aim.success) {
	    ResultTA.append("\nBy inspection: ");
	    for (int i = 0; i != aim.solutionMat.Csize(); i++) {
		if (((Integer)aim.solutionMat.get(aim.currentSol - 1, i)).intValue() == 1) {
		    if (firstVi) {
			ResultTA.append("v" + (i + 1));
			firstVi = !firstVi;
		    }
		    else
			ResultTA.append(" + v" + (i + 1));
		}
	    }
	    ResultTA.append(" = 0\n");
	    ResultTA.append("x = " + aim.sumX + "\n");

	    for(int i = 0; i != aim.lj.size(); i++) {
		if (firstLj) {
		    ResultTA.append("l" + (i + 1) + " = " 
				    + ((Integer)aim.lj.elementAt(i)).intValue());
		    firstLj = !firstLj;
		}
		else
		    ResultTA.append(", l" + (i + 1) + " = "
				    + ((Integer)aim.lj.elementAt(i)).intValue());
	    }
	    ResultTA.append("\ny = " + aim.y + "\n");
	    ResultTA.append("gcd(x - y, n) = " + aim.result + "\n\n");
	    ResultTA.append("So two non-trivial factors of " + n1.intValue() 
			    + " are " + aim.result + " and " + (n1.intValue() / aim.result)
			    + "\n\n");	
	}
	
	else
	    ResultTA.append("Unlikely case of false Subset appeared");

    }
}










