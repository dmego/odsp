package cn.dmego.odsp.solution;

import java.util.ArrayList;
import java.util.Arrays;

import org.opensourcedea.dea.*;

public class EfficientyTest {

    static String[] testDMUNames = new String[10];
    static String[] testVariableNames = new String [5];
    static VariableOrientation[] testVariableOrientations = new VariableOrientation[5];
    static VariableType[] testVariableTypes = new VariableType[5];
    static double[] [] testDataMatrix = new double[10] [5];

    public static void main(String[] args) {

        createData();

        //Create a DEAProblem and specify number of DMUs (7) and number of variables (3).
        DEAProblem tester = new DEAProblem(10, 5);

        //Set the DEA Problem Model Type (CCR Input Oriented).
        tester.setModelType(ModelType.CCR_I);

        //Set the DEA Problem DMU Names where testDMUName is a double[].
        tester.setDMUNames(testDMUNames);

        //Set the DEA Problem Variable Names where testVariableName is a String[].
        tester.setVariableNames(testVariableNames);

        //Set the DEA Problem Variable Orientation where testVariableOrientation is a VariableOrientation[].
        tester.setVariableOrientations(testVariableOrientations);

        //Set the DEA Problem Variable Types where testVariableTypes is a VariableType[].
        tester.setVariableTypes(testVariableTypes);

        /* Set the DEA Problem Data Matrix where testDataMatrix is a double[] [].
         * Each row of the Matrix corresponds to the DMU in the DMUNames array.
         * Each Column of the Matrix corresponds to the Variable in the Variables Arrays.*/
        tester.setDataMatrix(testDataMatrix);

        try {
            //Solve the DEA Problem
            tester.solve();

            //Get the solution Objectives
            double[] objectives = tester.getObjectives();
            for (int i = 0; i < objectives.length; i++) {
                System.out.println(testDMUNames[i]+":"+objectives[i]);
            }

            /* Get the solution Reference Set.*/
            ArrayList<NonZeroLambda>[] referenceSets = new ArrayList[10];
            referenceSets = tester.getReferenceSet();


            /* Get the solution Slacks.
             * The first array corresponds to the DMUs.
             * The second nested array corresponds to the Slack values.*/
            double[] [] slacks = tester.getSlacks();
            for (int i = 0; i < slacks.length; i++) {
                System.out.println(Arrays.toString(slacks[i]));
            }

            /* Get the solution Projections.
             * The first array corresponds to the DMUs.
             * The second nested array corresponds to the Projection values.*/
            double[] [] projections = tester.getProjections();
            for (int i = 0; i < projections.length; i++) {
                System.out.println(Arrays.toString(projections[i]));
            }
            /* Get the solution Weights.
             * The first array corresponds to the DMUs.
             * The second nested array corresponds to the Weight values.*/
            double[] [] weights = tester.getWeight();
            for (int i = 0; i < weights.length; i++) {
                System.out.println(Arrays.toString(weights[i]));
            }
            /* Get the DMU ranks.
             * The boolean confirms that the Highest DMU score is ranked first.
             * The STANDARD ranking type confirms that the ranking is standard.
             * This means that if they are two DMUs with an efficiency score of 1 both will be ranked first.
             * However, the following DMU will only be ranked 3rd as they are two DMUs which score better than it.
             * Conversely, a DENSE RankingType will have given the following (3rd) DMU the ranking of second.
             * The precision is the int value (between 0 and 16) used to round the score values before ranking
             * the objectives.*/
            int[] ranks = tester.getRanks(false, RankingType.DENSE, 10);
            for (int i = 0; i < ranks.length; i++) {
                System.out.print(ranks[i]+",");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createData() {

        //Set up the DMU Names
        testDMUNames[0] = "北京市";
        testDMUNames[1] = "天津市";
        testDMUNames[2] = "河北省";
        testDMUNames[3] = "上海市";
        testDMUNames[4] = "江苏省";
        testDMUNames[5] = "浙江省";
        testDMUNames[6] = "福建省";
        testDMUNames[7] = "山东省";
        testDMUNames[8] = "广东省";
        testDMUNames[9] = "海南省";

        //Set up the Variable Names
        testVariableNames[0] = "从业人员数";
        testVariableNames[1] = "固定资产投资额";
        testVariableNames[2] = "环境污染治理投资额";
        testVariableNames[3] = "GDP";
        testVariableNames[4] = "三废综合利用产品产值";

        //Set up the Data Matrix
        testDataMatrix [0] [0] = 13342117.7;
        testDataMatrix [0] [1] = 13342117;
        testDataMatrix [0] [2] = 13342117;
        testDataMatrix [0] [3] = 13342117;
        testDataMatrix [0] [4] = 13342117.4366;

        testDataMatrix [1] [0] = 520.8;
        testDataMatrix [1] [1] = 6278.1;
        testDataMatrix [1] [2] = 109.7;
        testDataMatrix [1] [3] = 9224.46;
        testDataMatrix [1] [4] = 19.265;


        testDataMatrix [2] [0] = 3790.2;
        testDataMatrix [2] [1] = 15083.4;
        testDataMatrix [2] [2] = 370.9;
        testDataMatrix [2] [3] = 20394.26;
        testDataMatrix [2] [4] = 107.1801;

        testDataMatrix [3] [0] = 924.7;
        testDataMatrix [3] [1] = 5108.9;
        testDataMatrix [3] [2] = 134.0;
        testDataMatrix [3] [3] = 17165.98;
        testDataMatrix [3] [4] = 17.0379;

        testDataMatrix [4] [0] = 4731.7;
        testDataMatrix [4] [1] = 23184.3;
        testDataMatrix [4] [2] = 466.4;
        testDataMatrix [4] [3] = 41425.48;
        testDataMatrix [4] [4] = 281.9749;

        testDataMatrix [5] [0] = 3989.2;
        testDataMatrix [5] [1] = 12376.0;
        testDataMatrix [5] [2] = 333.7;
        testDataMatrix [5] [3] = 27722.31;
        testDataMatrix [5] [4] = 286.3867;

        testDataMatrix [6] [0] = 2181.3;
        testDataMatrix [6] [1] = 8199.1;
        testDataMatrix [6] [2] = 129.7;
        testDataMatrix [6] [3] = 14737.12;
        testDataMatrix [6] [4] = 37.5029;

        testDataMatrix [7] [0] = 5654.7;
        testDataMatrix [7] [1] = 23280.5;
        testDataMatrix [7] [2] = 483.9;
        testDataMatrix [7] [3] = 39169.92;
        testDataMatrix [7] [4] = 187.1898;

        testDataMatrix [8] [0] = 5776.9;
        testDataMatrix [8] [1] = 15623.7;
        testDataMatrix [8] [2] = 1416.2;
        testDataMatrix [8] [3] = 46013.06;
        testDataMatrix [8] [4] = 62.4265;

        testDataMatrix [9] [0] = 445.7;
        testDataMatrix [9] [1] = 1317.0;
        testDataMatrix [9] [2] = 23.6;
        testDataMatrix [9] [3] = 2064.5;
        testDataMatrix [9] [4] = 3.1623;

        //Set up the variable types
        testVariableOrientations [0] = VariableOrientation.INPUT;
        testVariableOrientations [1] = VariableOrientation.INPUT;
        testVariableOrientations [2] = VariableOrientation.INPUT;
        testVariableOrientations [3] = VariableOrientation.OUTPUT;
        testVariableOrientations [4] = VariableOrientation.OUTPUT;

        testVariableTypes[0] = VariableType.STANDARD;
        testVariableTypes[1] = VariableType.STANDARD;
        testVariableTypes[2] = VariableType.STANDARD;
        testVariableTypes[3] = VariableType.STANDARD;
        testVariableTypes[4] = VariableType.STANDARD;
    }



}
