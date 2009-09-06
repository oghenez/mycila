import java.math.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
public class TestShadowApplet extends Applet implements ActionListener,ItemListener {

   TextField msg=new TextField(80);
   TextField entryField=new TextField(80);
   Button genShadowsButton=new Button("OK");
   Panel p1=new Panel();
   List shadowsList=new List(5,true);
   TextArea moduliArea=new TextArea("",5,50,TextArea.SCROLLBARS_BOTH);
   Panel p2=new Panel();
   TextField primeField=new TextField(80);
   TextField multField=new TextField(80);
   Button recoverButton=new Button("Recover master key");
   Panel p3=new Panel();

   BigInteger K=null,t,p;
   int r,s;
   BigInteger[] shadow,modulus;
   boolean[] useShadow;

   public void init() {
      setLayout(new GridLayout(3,1));
      p1.setLayout(new GridLayout(3,1));
      p2.setLayout(new GridLayout(1,2));
      p3.setLayout(new GridLayout(3,1));
      p1.add(msg);
      msg.setEditable(false);
      msg.setText("Enter master key");
      p1.add(genShadowsButton);
      p1.add(entryField);
      genShadowsButton.addActionListener(this);
      p2.add(shadowsList);
      shadowsList.addItemListener(this);
      p2.add(moduliArea);
      moduliArea.setEditable(false);
      p3.add(primeField);
      primeField.setEditable(false);
      p3.add(multField);
      multField.setEditable(false);
      p3.add(recoverButton);
      recoverButton.addActionListener(this);
      recoverButton.setEnabled(false);
      add(p1);
      add(p2);
      add(p3);
   }

   public void actionPerformed(ActionEvent e) {
      if (e.getSource()==genShadowsButton) {
         if (K==null) {
            K=new BigInteger(entryField.getText());
            msg.setText("Enter # of shadows to generate");
            entryField.setText("");
         } else {
            r=Integer.parseInt(entryField.getText());
            s=r/2+1;
            msg.setText(s+" of these shadows will be needed to reproduce the master key.");
            ShadowBuilder sb=new ShadowBuilder(K,r,16);
            shadow=sb.getShadows();
            useShadow=new boolean[shadow.length];
            for (int i=0;i<shadow.length;i++) shadowsList.add(shadow[i].toString());
            modulus=sb.getModuli();
            for (int i=0;i<modulus.length;i++) moduliArea.append(modulus[i].toString()+"\n");
            t=sb.getRandomMultiplier();
            multField.setText("Multiplier: "+t);
            p=sb.getReconstructingPrime();
            primeField.setText("Reconstructing prime: "+p);
            K=null;
            entryField.setText("");
            genShadowsButton.setEnabled(false);
            recoverButton.setEnabled(true);
         }
      } else if (e.getSource()==recoverButton) {
         BigInteger[] s2=new BigInteger[s];
         BigInteger[] m2=new BigInteger[s];
         int j=0;
         for (int i=0;i<r;i++) {
            if (useShadow[i]) {
               s2[j]=shadow[i];
               m2[j]=modulus[i];
               if (++j==s) break;
            }
         }
         if (j==s) {
            KeyRebuilder kr=new KeyRebuilder(s2,m2,t,p);
            BigInteger mk=kr.getMasterKey();
            msg.setText("The recovered master key is: "+mk);
         } else msg.setText("Minimum # of shadows not chosen!");
      }
   }

   public void itemStateChanged(ItemEvent e) {
      int pos=((Integer)e.getItem()).intValue();
      useShadow[pos]=!useShadow[pos];
   }

}
