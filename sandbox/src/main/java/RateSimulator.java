import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class RateSimulator {
    public static void main(String[] args) {

        NumberFormat formater = NumberFormat.getInstance(Locale.ENGLISH);
        formater.setMaximumFractionDigits(4);
        formater.setGroupingUsed(false);
        formater.setRoundingMode(RoundingMode.HALF_UP);

        final double PRIX = 374500;
        //final double MISE = PRIX * 0.2; // = 74900
        final double MISE = 75000;
        final double M = PRIX - MISE;
        //final double TD = 0.0540;
        final double TD = 0.0235;
        //final double TF = TD;
        final double TF = 0.06;
        final double V = 800;
        final double N = 26;
        final double TERM = 5;

        double T = TD;
        double Mi = M;
        double TI = 0;
        double TP = 0;
        StringBuilder sb = new StringBuilder("numero\ttaux\tinteret\tpaiement\tcumul\trestant");
        for (int count = 1; Mi > 0 && count / N <= TERM; count++) {
            double I = Mi * T / N;
            TI += I;
            double P = V - I;
            if (Mi - P < 0) P = Mi;
            Mi = Mi - P;
            TP += P;
            sb.append("\n").append(count)
                    .append("\t").append(formater.format(T))
                    .append("\t").append(formater.format(I))
                    .append("\t").append(formater.format(P))
                    .append("\t").append(formater.format(TP))
                    .append("\t").append(formater.format(Mi))
                    ;
            T += (TF - TD) / TERM / N;
            if (T >= 0.04) T = 0.04;
        }

        System.out.println("Montant:\t" + formater.format(PRIX));
        System.out.println("Mise de fond:\t" + formater.format(MISE));
        System.out.println("Montant pret:\t" + formater.format(M));
        System.out.println("Versement periodique:\t" + formater.format(V));
        System.out.println("Taux allant de\t" + formater.format(TD * 100) + "\ta\t" + formater.format(TF * 100));
        System.out.println("Nombre versements par an:\t" + formater.format(N));
        System.out.println("Interets payes:\t" + formater.format(TI));
        System.out.println("Paiements:\t" + formater.format(TP));
        System.out.println("Total:\t" + formater.format(TP + TI));
        System.out.println(sb);
    }


}