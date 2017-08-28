package tech.bison.ints;

import java.util.HashSet;
import java.util.Set;

public class Ints {
  /**
   * Clone a two dimensional int array into a new array
   * 
   * @param array
   *          The array to copy
   * @return A copy of {@code array}
   */
  public static int[][] copy(int[][] array) {
    int[][] tmp = new int[array.length][];
    for (int i = 0; i < array.length; i++) {
      tmp[i] = array[i].clone();
    }
    return tmp;
  }

  public static int[] split(String regex, String data) {
    String[] tmp = data.split(regex);
    int[] arr = new int[data.length()];
    for (int i = 0; i < data.length(); i++) {
      arr[i] = Integer.parseInt(tmp[i]);
    }
    return arr;
  }

  /**
   * Check whether a two dimensional int array contains multiple occurrences of a certain value
   * 
   * @param ints
   *          the values to analyze
   * @param num
   *          the value to search for
   * @return {@code true} if {@code ints} contains more than one occurrences of {@code num}
   */
  public static boolean multiOccurence(int[][] ints, int num) {
    int count = 0;
    for (int[] i : ints) {
      for (int val : i) {
        if (val == num) {
          count++;
        }
        if (count > 1) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * @param ints
   *          the values to analyze
   * @param amount
   *          the minimum amount of distinct values
   * @return {@code true} if {@code ints} contains more than {@code amount} distinct, non zero
   *         values
   */
  public static boolean hasMoreDistinct(int[][] ints, int amount) {
    Set<Integer> values = new HashSet<>();
    for (int[] i : ints) {
      for (int val : i) {
        if (val != 0) {
          values.add(val);
        }
        if (values.size() > amount) {
          return true;
        }
      }
    }
    return false;
  }
}
