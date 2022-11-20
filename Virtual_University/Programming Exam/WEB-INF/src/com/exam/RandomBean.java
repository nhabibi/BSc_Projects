package com.exam;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: Aug 15, 2004
 * Time: 1:24:15 PM
 * To change this template use File | Settings | File Templates.
 */
import java.util.Random;

public class RandomBean {


      public RandomBean(){  setResult(); }

      private static int range = 5;
      private int result ;
      private Random  rd = new Random();

      public void setResult(){

          result = rd.nextInt(range);
      }

      public int getResult(){
           return result;
      }

  }


