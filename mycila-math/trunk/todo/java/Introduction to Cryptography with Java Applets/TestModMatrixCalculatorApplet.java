import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.math.*;
public class TestModMatrixCalculatorApplet extends Applet implements ActionListener {

   int maxGridRows=25;
   ModMatrix val1;
   ModMatrix val2;
   BigInteger modulus;
   char arithmeticOp='=';
   BigInteger[] array;

   MatrixPanel display=new MatrixPanel(maxGridRows,maxGridRows);
   Label modulusLabel=new Label("Modulus");
   TextField modulusField=new TextField();
   Button add=new Button("+");
   Button subtract=new Button("-");
   Button multiply=new Button("*");
   Button equ=new Button("=");
   Panel p1=new Panel();
   Panel p2=new Panel();
   Object prevButton;
   Object thisButton=equ;
   Label errorMsg=new Label("");

   public void init() {
      setLayout(null);

      p1.setSize(600,500);
      p1.setLocation(0,100);
      p1.setLayout(new GridLayout(1,1));

      p2.setSize(600,50);
      p2.setLocation(0,0);
      p2.setLayout(new GridLayout(1,6));

      errorMsg.setSize(600,50);
      errorMsg.setLocation(0,50);

      p1.add(display);

      p2.add(modulusField);
      p2.add(modulusLabel);
      p2.add(add);
      add.addActionListener(this);
      p2.add(subtract);
      subtract.addActionListener(this);
      p2.add(multiply);
      multiply.addActionListener(this);
      p2.add(equ);
      equ.addActionListener(this);

      add(p2);
      add(errorMsg);
      add(p1);
   }

   public void actionPerformed(ActionEvent e) {
      char op=e.getActionCommand().charAt(0);
      try {
         modulusField.setEditable(false);
         modulus=new BigInteger(modulusField.getText());
         if (modulus.compareTo(BigInteger.valueOf(2))<0) {modulus=BigInteger.valueOf(2); modulusField.setText("2");}
      } catch (Exception nfe) {
         errorMsg.setText(nfe.toString()+" - click Refresh");
         return;
      }
      switch (op) {
         case '+': case '-': case '*':
         try {
            val1=readMatrix(display,maxGridRows);
         } catch (Exception nfe) {
            errorMsg.setText(nfe.toString()+" - click Refresh");
            return;
         }
         arithmeticOp=op;
         clearPanel(display);
         return;
         case '=':
         try {
            val2=readMatrix(display,maxGridRows);
         } catch (Exception nfe) {
            errorMsg.setText(nfe.toString()+" - click Refresh");
            return;
         }
         showResults(display,val1,val2);
      }
   }

   private ModMatrix readMatrix(MatrixPanel mp,int limit) {
      array=new BigInteger[limit*limit];
      int rows=0, columns=0, rowCount=0, columnCount=0;
      String value="";
      while (rowCount<limit&&!(value=mp.getEntry(rowCount,0)).equals("")) {
         columnCount=0;
         while (columnCount<limit&&!(value=mp.getEntry(rowCount,columnCount)).equals("")) {
            array[rowCount*columns+columnCount]=new BigInteger(value);
            columnCount++;
         }
         if (rowCount>0&&columnCount!=columns) throw new IllegalArgumentException("Invalid row/column length");
         else columns=columnCount;
         rowCount++;
      }
      rows=rowCount;
      return new ModMatrix(rows,columns,array,modulus);
   }

   private void showResults(MatrixPanel mp,ModMatrix op1,ModMatrix op2) {
      ModMatrix answer=calc(op1,op2);
      int rows=answer.rows();
      int columns=answer.columns();
      for (int i=0;i<maxGridRows;i++)
         for (int j=0;j<maxGridRows;j++)
            if (i<rows&&j<columns) mp.setEntry(i,j,answer.getElement(i+1,j+1).toString());
            else mp.setEntry(i,j,"");
   }

   private void clearPanel(MatrixPanel mp) {
      for (int i=0;i<maxGridRows;i++)
         for (int j=0;j<maxGridRows;j++)
            mp.setEntry(i,j,"");
   }

	private ModMatrix calc(ModMatrix op1, ModMatrix op2) {
		ModMatrix result=null;
      try {
   		switch (arithmeticOp) {
   			case '+': result = op1.add(op2); break;
   			case '-': result = op1.subtract(op2); break;
   			case '*': result = op1.multiply(op2); break;
   		}
      } catch (Exception exc) {
         errorMsg.setText(exc.toString()+"- click refresh");
      }
      return result;
	}


}