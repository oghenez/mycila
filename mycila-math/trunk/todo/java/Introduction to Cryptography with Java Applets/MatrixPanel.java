import java.math.*;
import java.awt.*;
public class MatrixPanel extends Panel {

   //A MatrixPanel is just a panel with a grid of TextFields on it
   TextField[][] entries;

   //Number of rows/columns on MatrixPanel are stored here
   int rows,columns;

   //Constructor produces a MatrixPanel with specified number of rows & columns
   public MatrixPanel(int rows,int columns) {
      this.rows=rows;
      this.columns=columns;
      setLayout(new GridLayout(rows,columns));
      //Create each individual TextField
      entries=new TextField[rows][columns];
      //Place them on the Panel
      for (int i=0;i<rows;i++)
         for (int j=0;j<columns;j++) {
            entries[i][j]=new TextField();
            add(entries[i][j]);
         }
   }

   //Method makes each TextField editable or not
   public void setEditable(boolean b) {
      for (int i=0;i<rows;i++)
         for (int j=0;j<columns;j++)
            entries[i][j].setEditable(b);
   }

   //Method to set the text in the row,column-th TextField
   public void setEntry(int row,int column,String value) {
      //Check if indices are proper values first
      if (row<0 || row>=rows) throw new IllegalArgumentException("Invalid row "+row);
      if (column<0 || column>=columns) throw new IllegalArgumentException("Invalid column "+column);
      entries[row][column].setText(value);
   }

   //Method to get the text from the row,column-th TextField
   public String getEntry(int row, int column) {
      //Check if indices are proper values first
      if (row<0 || row>=rows) throw new IllegalArgumentException("Invalid row "+row);
      if (column<0 || column>=columns) throw new IllegalArgumentException("Invalid column "+column);
      return entries[row][column].getText();
   }
}