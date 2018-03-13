package gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import simplex.Simplex;
import xfind.FuzzyProblem;
import xfind.Problem;


import java.awt.*;
import java.util.*;

/**
 * Created by 'lex'.
 */
public class MainController {
    public static StringBuilder magicString = new StringBuilder();
    @FXML private TextField m_select;
    @FXML private TextField a1_input;
    @FXML private TextField a2_input;
    @FXML private TextField a3_input;
    @FXML private TextField alpha1_input;
    @FXML private TextField alpha2_input;
    @FXML private TextField alpha3_input;
    @FXML private TextField l_select;
    @FXML private TextField k1_input;
    @FXML private TextField k2_input;
    @FXML private TextField k3_input;
    @FXML private TextField t1_input;
    @FXML private TextField t2_input;
    @FXML private TextField t3_input;
    @FXML private TextField alpha_input;
    @FXML private TextArea ans;
    @FXML protected void handleCountButtonAction(ActionEvent event) {
        magicString = new StringBuilder();
        int m = Integer.parseInt(m_select.getText());


        Integer[] a = new Integer[m];
        a[0] = Integer.parseInt(a1_input.getText());
        if (m>=2) a[1] = Integer.parseInt(a2_input.getText());
        if (m>=3) a[2] = Integer.parseInt(a3_input.getText());
        Integer[] alpha = new Integer[m];
        alpha[0] = Integer.parseInt(alpha1_input.getText());
        if (m>=2) alpha[1] = Integer.parseInt(alpha2_input.getText());
        if (m>=3) alpha[2] = Integer.parseInt(alpha3_input.getText());

        int l = Integer.parseInt(l_select.getText());

        Integer[] k = new Integer[l];
        k[0] = Integer.parseInt(k1_input.getText());
        if (l>=2) k[1] = Integer.parseInt(k2_input.getText());
        if (l>=3) k[2] = Integer.parseInt(k3_input.getText());
        Integer[] t = new Integer[l];
        t[0] = Integer.parseInt(t1_input.getText());
        if (l>=2) t[1] = Integer.parseInt(t2_input.getText());
        if (l>=3) t[2] = Integer.parseInt(t3_input.getText());

        Double alp = Double.parseDouble(alpha_input.getText());

        Problem problem = new Problem(m,a,alpha,l,k,t);
        Simplex simplex = problem.createSimplexProblem();
        int initNV = simplex.getNumberOfVariables();
        simplex.prepare();
        java.util.List<Integer> ans = simplex.count();
        for (int i = 0; i < initNV - 1; i++) {
            if (ans.get(i) > 0) magicString.append("x[" + (i + 1) + "]=" + ans.get(i) +"\n");
        }
        magicString.append("Відповідь: " + ans.get(initNV - 1)+"\n");
        magicString.append("Залишок: " + problem.left(ans) +"\n");
        magicString.append("Задача оптиміста: "+"\n");
        FuzzyProblem lucky = new FuzzyProblem(m,a,alpha,l,k,t,alpha);
        lucky.lucky(alp);
        simplex = lucky.createSimplexProblem();
        initNV = simplex.getNumberOfVariables();
        simplex.prepare();
        ans = simplex.count();
        for (int i = 0; i < initNV - 1; i++) {
            if (ans.get(i) > 0) magicString.append("x[" + (i + 1) + "]=" + ans.get(i)+"\n");
        }
        magicString.append("Відповідь: " + ans.get(initNV - 1)+"\n");
        magicString.append("Залишок: " + lucky.left(ans) +"\n");
        magicString.append("Задача песиміста: "+"\n");
        FuzzyProblem unlucky = new FuzzyProblem(m,a,alpha,l,k,t,alpha);
        unlucky.unlucky(alp);
        simplex = unlucky.createSimplexProblem();
        initNV = simplex.getNumberOfVariables();
        simplex.prepare();
        ans = simplex.count();
        for (int i = 0; i < initNV - 1; i++) {
            if (ans.get(i) > 0) magicString.append("x[" + (i + 1) + "]=" + ans.get(i)+"\n");
        }
        magicString.append("Відповідь: " + ans.get(initNV - 1)+"\n");
        magicString.append("Залишок: " + unlucky.left(ans) +"\n");
        this.ans.setText(magicString.toString());
    }
}