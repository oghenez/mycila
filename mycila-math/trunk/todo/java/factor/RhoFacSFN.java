import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Demo applet Pollard's rho factoring algorithm 
 *
 * @author  Kristijan Dragicevic
 * @version 20.09.04
 * changed by Klaus Reinhardt July 2 2007
 */

public class RhoFacSFN extends Applet implements ActionListener
{
    RhoFacN PolFac;
    long compnum;
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
	new TextArea("", 20, 90, java.awt.TextArea.SCROLLBARS_BOTH);
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
	    
	  
	    PolFac = new RhoFacN(n.longValue(), c.longValue());
	    compnum = n.longValue();
	    
	    if(PolFac.succeeded() == true){
		ResultTA.setText("");
		WriteRes();
		ResultTA.append("\nfound non-trivial factor:\t" + PolFac.result 
				+ "\nanother non-trivial factor is:\t" 
				+ (compnum / PolFac.result));
		Result.setText("Factorization succeeded");
	    }
	    else{
		ResultTA.setText("");
		WriteRes();
		ResultTA.append("no RHO-FACTORIZATION possible with " 
				 + "polynom x²+" + c);
		Result.setText(n + " is no valid number");
	    }
        }
 
	doLayout();
	return;
    }

    private void WriteRes(){
	ResultTA.append("y(i)\ty(2i)\tf\tg\th\ti\td\tdc\n\n");
	for (int i = 0; i != PolFac.sizeof(PolFac.aList); i++)
	    ResultTA.append(""+PolFac.aList.elementAt(i) + "\t" 
			    + PolFac.bList.elementAt(i) + "\t" 
			    + PolFac.fList.elementAt(i) + "\t" 
			    + PolFac.gList.elementAt(i) + "\t" 
			    + PolFac.hList.elementAt(i) + "\t" 
			    + PolFac.iList.elementAt(i) + "\t" 
			    + PolFac.dList.elementAt(i) + "\t" 
			    + PolFac.dcList.elementAt(i) + "\n");
    }

} // class RhoFacSF
