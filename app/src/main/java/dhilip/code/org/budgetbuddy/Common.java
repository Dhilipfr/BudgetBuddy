package dhilip.code.org.budgetbuddy;

/**
 * Created by Dhilip on 21-10-2015.
 */
public class Common {

    public Double RoundofDecimal(Double value){
       return  (double) Math.round(value * 100) / 100;
    }
}
