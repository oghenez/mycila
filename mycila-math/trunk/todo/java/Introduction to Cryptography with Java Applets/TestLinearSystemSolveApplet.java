import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.math.*;
public class TestLinearSystemSolveApplet extends Applet implements ActionListener {

   Label modulusLabel=new Label("Modulus m");
   TextField modulusField=new TextField();
   Button generate=new Button("AX = B mod m - Compute X");
   Label errorMsg=new Label("");
   Panel top=new Panel();
   Label lab1=new Label("A");
   Label lab2=new Label("X");
   Label lab3=new Label("B");

   final int maxGridRows=20;
   BigInteger modulus;
   BigInteger[] array;

   MatrixPanel matrixpa=new MatrixPanel(maxGridRows,maxGridRows);
   MatrixPanel matrixpx=new MatrixPanel(maxGridRows,1);
   MatrixPanel matrixpb=new MatrixPanel(maxGridRows,1);

   ModSquareMatrix A;
   ModMatrix X;
   ModMatrix B;

   public void init() {
      setLayout(null);

      top.setSize(600,30);
      top.setLocation(0,0);
      add(top);
      top.setLayout(new GridLayout(1,3));
      top.add(modulusField);
      top.add(modulusLabel);
      top.add(generate);
      generate.addActionListener(this);

      errorMsg.setSize(600,30);
      errorMsg.setLocation(0,30);
      add(errorMsg);

      lab1.setSize(500,30);
      lab1.setLocation(0,60);
      add(lab1);
      lab2.setSize(50,30);
      lab2.setLocation(500,60);
      add(lab2);
      lab3.setSize(50,30);
      lab3.setLocation(550,60);
      add(lab3);

      matrixpa.setSize(500,510);
      matrixpa.setLocation(0,90);
      add(matrixpa);

      matrixpx.setSize(50,510);
      matrixpx.setLocation(500,90);
      add(matrixpx);
      matrixpx.setEditable(false);

      matrixpb.setSize(50,510);
      matrixpb.setLocation(550,90);
      add(matrixpb);

   }

   public void actionPerformed(ActionEvent e) {
      Object source=e.getSource();
      if (source==generate) {
         try {
            errorMsg.setText("");
            modulus=new BigInteger(modulusField.getText());
            A=readSquareMatrix(matrixpa,maxGridRows);
            B=readVector(matrixpb,maxGridRows);
            X=A.gaussianSolve(B);
            for (int i=0;i<X.rows();i++)
               matrixpx.setEntry(i,0,X.getElement(i+1,1).toString());
         } catch (Exception exc) {
            errorMsg.setText(exc.toString());
         }
      }
   }

   private ModSquareMatrix readSquareMatrix(MatrixPanel mp,int limit) {
      array=new BigInteger[limit*limit];
      int rows=0, columns=0, rowCount=0, columnCount=0;
      String value="";
      while (rowCount<limit&&!(value=matrixpa.getEntry(rowCount,0)).equals("")) {
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
      if (rows==columns) return new ModSquareMatrix(rows,array,modulus);
      else throw new IllegalArgumentException("Matrix A is not square");
   }

   private ModMatrix readVector(MatrixPanel mp,int limit) {
            array=new BigInteger[limit];
            String value="";
            int r=0;
            while (r<limit&&!(value=mp.getEntry(r,0)).equals("")) {
               array[r]=new BigInteger(value);
               r++;
            }
            return new ModMatrix(r,1,array,modulus);
   }

}