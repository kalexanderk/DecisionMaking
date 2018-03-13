package xfind;

import gui.controller.MainController;
import simplex.Simplex;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 'lex'.
 */
public class Problem {
    int m;
    Integer[] a;
    Integer[] alpha;
    int l;
    Integer[] k;
    Integer[] t;

    public Problem(int m, Integer[] a, Integer[] alpha, int l, Integer[] k, Integer[] t) {
        this.m = m;
        this.a = a;
        this.alpha = alpha;
        this.l = l;
        this.k = k;
        this.t = t;
    }

    public Problem() {

    }

    public Simplex createSimplexProblem(){
        int numberOfRestrictions = l + m;
        List<List<Double>> restrictions = new LinkedList<List<Double>>();
        int[] restrictionSigns = new int[numberOfRestrictions];
        double[] restrictionConstants = new double[numberOfRestrictions];
        int[] aParts = new int[m];
        for (int iн = 0; i < numberOfRestrictions; i++) {
            restrictions.add(new LinkedList<Double>());
        }
        int step = 0;
        for (int i = 0; i < m; i++) {
            List<List<Integer>> split = Actions.split(alpha[i], Arrays.asList(t));
            MainController.magicString.append("Карта розкрою для довжини " + alpha[i]+"\n");
            for (List l : split){
                MainController.magicString.append(Arrays.toString(l.toArray())+"\n");
            }
            for (int j = 0; j < split.size(); j++) {
                for (int n = 0; n < l; n++) {
                    restrictions.get(n).add(j + step, (double) split.get(j).get(n));
                }
            }
            step+=split.size();
            aParts[i] = split.size();
            restrictionConstants[i] = 0;
            restrictionSigns[i] = 1;
        }
        for (int i = 0; i < l; i++) {
            restrictions.get(i).add(-(double)k[i]);
        }
        int numberOfVariables = restrictions.get(0).size();
        for (int i = l; i < numberOfRestrictions; i++) {
            int step1 = 0;
            for (int j = 0; j < i - l; j++) {
                step1 += aParts[j];
            }
            for (int j = 0; j < numberOfVariables; j++) {
                restrictions.get(i).add(j >= step1 && j < step1 + aParts[i - l] ? 1d : 0d);
            }
            restrictionSigns[i] = -1;
            restrictionConstants[i] = a[i - l];
        }

        for(List<Double> list: restrictions){
           // System.out.println(Arrays.toString(list.toArray()));
        }
        List<Double> targetFunction = new LinkedList<>();
        for (int i = 0; i < numberOfVariables - 1; i++) {
            targetFunction.add(0d);
        }
        targetFunction.add(1d);
        return new Simplex(numberOfVariables,targetFunction,true,numberOfRestrictions,restrictions,restrictionSigns,restrictionConstants);
    }

    public int left(List<Integer> ans){
        int var = 0;
        int left = 0;
        for (int size: alpha){
            List<List<Integer>> split = Actions.split(size, Arrays.asList(t));
            for (List<Integer> card: split){
                int thisLeft = size;
                for (int i = 0; i < t.length; i++) {
                    thisLeft -= card.get(i)*t[i];
                }
                left += ans.get(var)*thisLeft;
                var++;
            }
        }
        return left;
    }

    public static void main(String[] args) {
        Problem problem = new Problem(1,new Integer[]{75},new Integer[]{92},3,new Integer[]{3,5,2},new Integer[]{32,18,12});
        //Problem problem = new Problem(3,new Integer[]{113,264,48},new Integer[]{250,190,150},3,new Integer[]{5,4,12},new Integer[]{54,42,30});
        Simplex simplex = problem.createSimplexProblem();
        simplex.prepare();
        List<Integer> ans = simplex.count();
        System.out.println("answer: " + Arrays.toString(ans.toArray()));
        List<Double> tf = simplex.getTargetFunction();
        double result = 0;
        for (int i = 0; i < simplex.getNumberOfVariables(); i++) {
            result += tf.get(i)*ans.get(i);
        }
        System.out.println("result: "+(int)result);

    }
}
