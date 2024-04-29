package org.prog;

public class Main {

    /**
     *############################################################
     *###########################[Car2]###########################
     *############################################################
     *##########[Car1]###########[Car3]###########################
     *############################################################
     *######1################100###########200####################
     * @param args
     */

    public static void main(String[] args) {
        String s1 = "s1";
        String s2 = "s2";
        String s3 = "s3";

        try {
            paintCar(null, "green");
        } catch (NullPointerException e){
            System.out.println("NPE oops!");
        } catch (RuntimeException e){
            System.out.println("RTE oops!");
        } catch (Exception e){
            System.out.println("EX oops!");
        } catch (Throwable e){
            System.out.println("TH oops!");
        } finally {
            System.out.println("This code works always!");
        }

        try {

        } catch (Exception e){

        }

        try {

        } finally {

        }




        Car car1 = new Car();
        Car car2 = new Car();
        car1.color = "red";
        car2.color = "red";
        Car janesCar = car1;

        System.out.println(car1.equals(car2));
        System.out.println(car1.equals(janesCar));

        car2.color = "black";

        System.out.println(car1.equals(car2));

        System.out.println(">>>>" + car1.toString());

        Object o = new Object();
        o.hashCode();

        int i = 1;
        int j = 1;

        System.out.println(i == j);

        car2 = new Car();

        Car car3 = new Car();
        car3.color = "red";

        car1.color = "red";
        car2.color = "blue";

        Car joesCar = car3;
        Car thatGuysCar = car3;

        System.out.println("Car #3 color is " + joesCar.color);
        System.out.println("Joes car is " + joesCar.color);
        System.out.println("That guys car is " + thatGuysCar.color);

        thatGuysCar.color = "black";

        System.out.println("Car #3 color is " + joesCar.color);
        System.out.println("Joes car is " + joesCar.color);
        System.out.println("That guys car is " + thatGuysCar.color);

        paintCar(joesCar, "yellow");

        System.out.println("Car #3 color is " + joesCar.color);
        System.out.println("Joes car is " + joesCar.color);
        System.out.println("That guys car is " + thatGuysCar.color);
    }

    public static void paintCar(Car carToPaint, String carNewColor){
        carToPaint.color = carNewColor;
    }
}
