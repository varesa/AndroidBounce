package fi.dy.esav.GrafiikkaTest;

/**
 * Created with IntelliJ IDEA.
 * User: esa
 * Date: 3/13/13
 * Time: 2:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class Utils {
    static double getDistance(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }
}
