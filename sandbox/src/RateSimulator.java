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

        final double M = 300000;
        final double TD = 0.0235;
        final double TF = 0.06;
        final double V = 1800;
        final double N = 12;
        final double TERM = 5;

        double T = TD;
        double Mi = M;
        double TI = 0;
        double TP = 0;
        StringBuilder sb = new StringBuilder("interet;paiement;taux");
        for (int count = 0; Mi > 0 && count / N <= TERM; count++) {
            double I = Mi * T / N;
            TI += I;
            double P = V - I;
            if (Mi - P < 0) P = Mi;
            sb.append("\n").append(formater.format(I))
                    .append(";").append(formater.format(P))
                    .append(";").append(formater.format(T));
            TP += P;
            Mi = Mi - P;
            T += (TF - TD) / TERM / N;
        }

        System.out.println("Montant pret: " + formater.format(M));
        System.out.println("Versement periodique: " + formater.format(V));
        System.out.println("Taux allant de " + formater.format(TD * 100) + "% a " + formater.format(TF * 100) + "%");
        System.out.println("Nombre versements par an: " + formater.format(N));
        System.out.println("Interets payés: " + formater.format(TI));
        System.out.println("Paiements: " + formater.format(TP));
        System.out.println("Total: " + formater.format(TP + TI));
        System.out.println(sb);
    }


}