import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Demo applet Pollard's rho factoring algorithm 
 *
 * @author  Kristijan Dragicevic
 * @version 20.09.04
 */

public class RhoFacSF extends Applet implements ActionListener
{
    RhoFac PolFac;
    int compnum;
    Panel InputPanel = new Panel();
    Panel Result1Panel = new Panel();
    Panel Result2Panel = new Panel();

    Label pLabel = new Label("number to factorize, n = ");
    Label cLabel = new Label("  Coefficient c = ");
    TextField pField = new TextField("", 5);
    TextField cField = new TextField("1", 2);
    Button cButton = new Button("Factorize");

    Label ResultTATitle = new Label("Result (see example)");
    TextArea ResultTA = 
	new TextArea("", 20, 60, java.awt.TextArea.SCROLLBARS_BOTH);
    Label Result = new Label("nothing tested                     ");

    public void init() {
	//set layout
	setLayout(new BorderLayout());
	InputPanel.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT));
	Result1Panel.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT));
	Result2Panel.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT));
	add("North", InputPanel);
	add("Center", Result1Panel);
	add("South", Result2Panel);

	//add components
	ResultTA.setEditable(false);
	InputPanel.add(pLabel);
	InputPanel.add(pField);
	InputPanel.add(cButton);
	InputPanel.add(cLabel);
	InputPanel.add(cField);
	Result1Panel.add(ResultTATitle);
	Result1Panel.add(ResultTA);
	Result2Panel.add(Result);

	//set action listeners
	cButton.addActionListener(this);
	doLayout();
	return;
    } //init

    public void actionPerformed(ActionEvent e) {
	Integer n, c;
	
	if(e.getActionCommand()==cButton.getLabel()){
	    n = new Integer(pField.getText());
	    c = new Integer(cField.getText());
	    
	  
	    PolFac = new RhoFac(n.intValue(), c.intValue());
	    compnum = n.intValue();
	    
	    if(PolFac.succeeded() == true){
		ResultTA.setText("");
		WriteRes();
		Result.setText("Factorization succeeded");
	    }
	    else{
		ResultTA.setText("no RHO-FACTORIZATION possible with " 
				 + "polynom x²+" + c);
		Result.setText(n + " is no valid number");
	    }
        }
 
	doLayout();
	return;
    }

    private void WriteRes(){
	ResultTA.append("\ty(i)\ty(2i)\td\n\n");
	for (int i = 0; i != PolFac.sizeof(PolFac.aList); i++)
	    ResultTA.append("\t" + PolFac.aList.elementAt(i) + "\t" 
			    + PolFac.bList.elementAt(i) + "\t" 
			    + PolFac.dList.elementAt(i) + "\n");
	ResultTA.append("\nfound non-trivial factor:\t" + PolFac.result 
			+ "\nanother non-trivial factor is:\t" 
			+ (compnum / PolFac.result));
    }

} // class RhoFacSF
