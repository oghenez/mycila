import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.math.*;
public class TestRandomModSquareMatrixApplet extends Applet implements ActionListener {

   Label l1=new Label("Matrix size");
   TextField rowsField=new TextField();
   Label l2=new Label("Modulus");
   TextField modulusField=new TextField();
   Button generate=new Button("Random invertible");
   Button invert=new Button("Invert");
   Label errorMsg=new Label("");
   Panel top=new Panel();
   Panel middle=new Panel();
   Panel bottom=new Panel();
   MatrixPanel matrixp;
   ModSquareMatrix matrix;

   int maxGridRows=25;
   int rows=25;
   BigInteger modulus=BigInteger.valueOf(2);

   public void init() {
      setLayout(null);
      top.setSize(600,30);
      top.setLocation(0,0);
      add(top);
      middle.setSize(600,30);
      middle.setLocation(0,30);
      middle.setLayout(new GridLayout(1,1));
      add(middle);
      bottom.setSize(600,540);
      bottom.setLocation(0,60);
      bottom.setLayout(new GridLayout(1,1));
      add(bottom);

      top.setLayout(new GridLayout(1,6));
      top.add(rowsField);
      top.add(l1);
      rowsField.setText(String.valueOf(rows));
      top.add(modulusField);
      top.add(l2);
      modulusField.setText(modulus.toString());
      top.add(generate);
      generate.addActionListener(this);
      top.add(invert);
      invert.addActionListener(this);

      middle.add(errorMsg);

      try {
         matrix=new ModSquareMatrix(rows,modulus,false,true);
      } catch (MatricesNonConformableException mnce) {
            errorMsg.setText(mnce.toString());
      }
      matrixp=new MatrixPanel(rows,rows);
      for (int i=0;i<rows;i++)
         for (int j=0;j<rows;j++)
            matrixp.setEntry(i,j,matrix.getElement(i+1,j+1).toString());
      bottom.add(matrixp);
      matrixp.setEditable(false);
   }

   public void actionPerformed(ActionEvent e) {
      Object source=e.getSource();
      if (source==generate) {
         try {
            errorMsg.setText("");
            rows=Integer.parseInt(rowsField.getText());
            if (rows<1) {rows=1;rowsField.setText("1");}
            if (rows>maxGridRows) {rows=maxGridRows; rowsField.setText(String.valueOf(maxGridRows));}
            modulus=new BigInteger(modulusField.getText());
            matrix=new ModSquareMatrix(rows,modulus,false,true);
            for (int i=0;i<maxGridRows;i++)
               for (int j=0;j<maxGridRows;j++)
                  if (i<rows&&j<rows) matrixp.setEntry(i,j,matrix.getElement(i+1,j+1).toString());
                  else matrixp.setEntry(i,j,"");
         } catch (MatricesNonConformableException mnce) {
            errorMsg.setText(mnce.toString());
         } catch (Exception exc) {
            errorMsg.setText(exc.toString());
         }
      } else if (source==invert) {
         try {
            errorMsg.setText("");
            matrix=matrix.inverse();
            for (int i=0;i<maxGridRows;i++)
               for (int j=0;j<maxGridRows;j++)
                  if (i<rows&&j<rows) matrixp.setEntry(i,j,matrix.getElement(i+1,j+1).toString());
                  else matrixp.setEntry(i,j,"");
         } catch (SingularMatrixException sme) {
            errorMsg.setText(sme.toString());
         } catch (MatricesNonConformableException mnce) {
            errorMsg.setText(mnce.toString());
         } catch (Exception exc) {
            errorMsg.setText(exc.toString());
         }
      }
   }

}