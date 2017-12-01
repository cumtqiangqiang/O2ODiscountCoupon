import utils.Calculator;
import utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by UC227911 on 12/1/2017.
 */
public class DateTest {
    public static void main(String[] args) {

        List<Float> list = new ArrayList<Float>();
        list.add(0.1f);
        list.add(0.f);
        list.add(0.2f);
        float a =  Calculator.getAverage(list);
        System.out.println(a);


    }

}
