import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.security.*;
public class TestPrimeGeneratorApplet extends Applet implements ActionListener {

  Label byteLengthLabel=new Label("Desired BYTE length of probable prime:");
  TextField byteLengthField=new TextField();
  Label numberBasesLabel=new Label("Number of bases to pass:");
  TextField numberBasesField=new TextField();
  Button getPrimeButton=new Button("Find probable prime using Rabin-Miller");
  Button getStrongPrimeButton=new Button("Find strong prime");
  Button getSafePrimeButton=new Button("Find safe prime and generator");
  TextArea primeArea=new TextArea("",10,40,TextArea.SCROLLBARS_VERTICAL_ONLY);
  Panel p1=new Panel();
  Panel p2=new Panel();
  Panel p3=new Panel();
  Panel p4=new Panel();

  SecureRandom sr;
  int byteLength=0;
  int numberBases=0;

  public void init() {
    setLayout(new GridLayout(2,1));
    add(p1);
    p1.setLayout(new GridLayout(1,2));
    p1.add(p3);
    p3.setLayout(new GridLayout(4,1));
    p1.add(p4);
    p4.setLayout(new GridLayout(3,1));
    add(p2);
    p2.setLayout(new GridLayout(1,1));

    p3.add(byteLengthLabel);
    p3.add(byteLengthField);
    p3.add(numberBasesLabel);
    p3.add(numberBasesField);
    p4.add(getPrimeButton);
    getPrimeButton.addActionListener(this);
    p4.add(getStrongPrimeButton);
    getStrongPrimeButton.addActionListener(this);
    p4.add(getSafePrimeButton);
    getSafePrimeButton.addActionListener(this);
    p2.add(primeArea);
    primeArea.setEditable(false);
    sr=new SecureRandom();
  }

  public void actionPerformed(ActionEvent e) {
    Object culprit=e.getSource();
    if (culprit==getPrimeButton) {
      primeArea.setText("Wait");
      try {
        byteLength=Integer.parseInt(byteLengthField.getText());
        if (byteLength<=0) {
          byteLength=1;
          byteLengthField.setText("1");
          primeArea.setText("Byte length must be a positive integer; using 1 - wait");
        }
        numberBases=Integer.parseInt(numberBasesField.getText());
        BigInteger num=null;
        do {
          byte[] ba=new byte[byteLength];
          sr.nextBytes(ba);
          num=new BigInteger(1,ba);
        } while (BigIntegerMath.primeProbability(num,numberBases,sr)==0);
        primeArea.setText(num.toString());
      } catch (NumberFormatException nfe) {
        primeArea.setText(nfe.toString());
      } catch (IllegalArgumentException iae) {
        primeArea.setText(iae.toString());
      }
    } else if (culprit==getStrongPrimeButton) {
      primeArea.setText("Wait");
      try {
        byteLength=Integer.parseInt(byteLengthField.getText());
        if (byteLength<=0) {
          byteLength=1;
          byteLengthField.setText("1");
          primeArea.setText("Byte length must be a positive integer; using 1 - wait");
        }
        numberBases=Integer.parseInt(numberBasesField.getText());
        PrimeGenerator pg=new PrimeGenerator(8*byteLength,numberBases,sr);
        BigInteger num=pg.getStrongPrime();
        primeArea.setText(num.toString());
      } catch (NumberFormatException nfe) {
        primeArea.setText(nfe.toString());
      } catch (IllegalArgumentException iae) {
        primeArea.setText(iae.toString());
      }
    } else if (culprit==getSafePrimeButton) {
      primeArea.setText("Wait");
      try {
        byteLength=Integer.parseInt(byteLengthField.getText());
        if (byteLength<=0) {
          byteLength=1;
          byteLengthField.setText("1");
          primeArea.setText("Byte length must be a positive integer; using 1 - wait");
        }
        numberBases=Integer.parseInt(numberBasesField.getText());
        PrimeGenerator pg=new PrimeGenerator(8*byteLength,numberBases,sr);
        BigInteger[] nums=pg.getSafePrimeAndGenerator();
        primeArea.setText(nums[0].toString()+"\n\n"+nums[1].toString());
      } catch (NumberFormatException nfe) {
        primeArea.setText(nfe.toString());
      } catch (IllegalArgumentException iae) {
        primeArea.setText(iae.toString());
      }
    }
  }

}
