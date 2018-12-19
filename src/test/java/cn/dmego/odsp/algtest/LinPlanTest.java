package cn.dmego.odsp.algtest;

import lombok.Data;

import java.util.*;

/**
 * class_name: LinPlanTest
 * package: cn.dmego.odsp.algtest
 * describe: 线性规划问题解
 * creat_user: Dmego
 * creat_date: 2018/12/17
 * creat_time: 1:42
 **/
@Data
public class LinPlanTest {
    public List<String> m_pListVariableItem;
    public int m_nVariable;
    public int m_nConstraint;
    public String m_sType;
    public double[][] m_dVariableCoefficientDataTable;
    public double[] m_mXcoefficient;
    public double[][] m_mCcoefficient;
    public double[] m_mB;
    public Map<Integer, String> m_htCcoefficientDirection = new HashMap<>();

    double m_dM = 10000.0;
    int m_nSongChiVariable;
    int m_nRenGongVariable;
    int m_nDieDai;
    int m_nResultType = 0;
    double[][] m_mCcoefficientBig;
    double[] m_mXcoefficientBig;
    Map<Integer, Integer> m_htGJColumnAndZSRow = new HashMap<>();
    double[][] m_mCTarget;
    double[] m_mXBest;
    double m_dZBest;
    String m_sLog;


    public LinPlanTest() {
        this.m_nVariable = 4;
        this.m_nConstraint = 4;
        this.m_sType = "极大值";
        this.m_mXcoefficient = new double[]{6, 2, 3, 5};
        this.m_mCcoefficient = new double[][]{
                {4, 2, 1, 3},
                {3, -5, 1, 6},
                {2, 1, 1, -1},
                {1, 2, 4, 5}
        };
        this.m_htCcoefficientDirection.put(0, "<=");
        this.m_htCcoefficientDirection.put(1, "=");
        this.m_htCcoefficientDirection.put(2, "<=");
        this.m_htCcoefficientDirection.put(3, ">=");

        this.m_mB = new double[]{10, 4, 3, 10};

        this.m_pListVariableItem = Arrays.asList("X1", "X2", "X3", "X4");

    }

    public static void main(String[] args) {

        LinPlanTest planTest = new LinPlanTest();
        planTest.SimplexMethod();
        System.out.println(111);
    }


    /**
     * 单纯形法求解线性规划
     */
    public boolean SimplexMethod() {
        try {
            m_nSongChiVariable = 0;
            m_nRenGongVariable = 0;
            String empty = null;
            for (int i = 0; i < this.m_nConstraint; ++i) {
                String str = this.m_htCcoefficientDirection.get(i);
                double num = this.m_mB[i];
                if (num < 0.0) {
                    if (str.equals(">=")) {
                        this.m_htCcoefficientDirection.put(i, "<=");
                        ++m_nSongChiVariable;
                    } else if (str.equals("<=")) {
                        this.m_htCcoefficientDirection.put(i, ">=");
                        ++m_nSongChiVariable;
                        ++m_nRenGongVariable;
                    } else
                        ++m_nRenGongVariable;
                    for (int j = 0; j < this.m_nVariable; ++j)
                        this.m_mCcoefficient[i][j] = this.m_mCcoefficient[i][j] * -1.0;
                    this.m_mB[i] = num * -1.0;
                } else if (str.equals(">=")) {
                    ++m_nSongChiVariable;
                    ++m_nRenGongVariable;
                } else if (str.equals("<="))
                    ++m_nSongChiVariable;
                else
                    ++m_nRenGongVariable;
            }
            int mNConstraint = this.m_nConstraint;
            int mNVariable1 = this.m_nVariable;
            int length = mNVariable1 + m_nSongChiVariable + m_nRenGongVariable;
            m_mCcoefficientBig = new double[mNConstraint][length];
            int num1 = 0;
            int num2 = 0;
            for (int i = 0; i < mNConstraint; ++i) {
                boolean flag1 = false;
                boolean flag2 = false;
                String str = this.m_htCcoefficientDirection.get(i);
                for (int j = 0; j < length; ++j) {
                    if (j < mNVariable1)
                        m_mCcoefficientBig[i][j] = this.m_mCcoefficient[i][j];
                    else if (str.equals(">=")) {
                        if (j == this.m_nVariable + num1) {
                            if (!flag1) {
                                m_mCcoefficientBig[i][j] = -1.0;
                                flag1 = true;
                            } else
                                m_mCcoefficientBig[i][j] = 0.0;
                        } else if (j == this.m_nVariable + m_nSongChiVariable + num2) {
                            if (!flag2) {
                                m_mCcoefficientBig[i][j] = 1.0;
                                flag2 = true;
                            } else
                                m_mCcoefficientBig[i][j] = 0.0;
                        } else
                            m_mCcoefficientBig[i][j] = 0.0;
                    } else if (str.equals("<=")) {
                        if (j == this.m_nVariable + num1) {
                            if (!flag1) {
                                m_mCcoefficientBig[i][j] = 1.0;
                                flag1 = true;
                            } else
                                m_mCcoefficientBig[i][j] = 0.0;
                        } else
                            m_mCcoefficientBig[i][j] = 0.0;
                    } else if (j == this.m_nVariable + m_nSongChiVariable + num2) {
                        if (!flag2) {
                            m_mCcoefficientBig[i][j] = 1.0;
                            flag2 = true;
                        } else
                            m_mCcoefficientBig[i][j] = 0.0;
                    } else
                        m_mCcoefficientBig[i][j] = 0.0;
                }
                if (str.equals(">=")) {
                    ++num1;
                    ++num2;
                } else if (str.equals("<="))
                    ++num1;
                else
                    ++num2;
            }
            m_mXcoefficientBig = new double[this.m_nVariable + m_nSongChiVariable + m_nRenGongVariable];

            for (int i = 0; i < this.m_nVariable; ++i)
                m_mXcoefficientBig[i] = !this.m_sType.equals("极小值") ? this.m_mXcoefficient[i] : this.m_mXcoefficient[i] * -1.0;
            for (int mNVariable2 = this.m_nVariable; mNVariable2 < this.m_nVariable + m_nSongChiVariable; ++mNVariable2)
                m_mXcoefficientBig[mNVariable2] = 0.0;
            for (int i = this.m_nVariable + m_nSongChiVariable; i < this.m_nVariable + m_nSongChiVariable + m_nRenGongVariable; ++i)
                m_mXcoefficientBig[i] = -1.0 * m_dM;
            m_mCTarget = new double[1][this.m_nConstraint];

            for (int i = 0; i < this.m_nConstraint; ++i)
                m_mCTarget[0][i] = !(this.m_htCcoefficientDirection.get(i).equals("<=")) ? -1.0 * m_dM : 0.0;
            m_htGJColumnAndZSRow = new HashMap<>();
            for (int i = 0; i < this.m_nVariable; ++i)
                m_htGJColumnAndZSRow.put(i, this.m_nVariable + 100);
            m_nDieDai = 0;
            DoCalculate();
            for (int i = 0; i < this.m_nConstraint; ++i) {
                if (m_mCTarget[0][i] == -1.0 * m_dM && this.m_mB[i] != 0.0) {
                    m_nResultType = 2;
                    break;
                }
            }
            if (m_nResultType != 2)
                CreateFinalBestValueInfo();
            return true;
        } catch (Exception ex) {
            m_sLog = "计算错误：" + ex.getMessage();
            return false;
        }

    }

    private void DoCalculate() {
        int index1 = 0;
        int index2 = 0;
        boolean flag = true;
        int num1 = m_nVariable + m_nSongChiVariable + m_nRenGongVariable;
        double[][] c = new double[1][1];
        double num2 = 0.0;
        for (int index3 = 0; index3 < num1; ++index3) {
            double[][] b = new double[this.m_nConstraint][1];
            for (int index4 = 0; index4 < this.m_nConstraint; ++index4)
                b[index4][0] = m_mCcoefficientBig[index4][index3];
            MatrixMultiply(m_mCTarget, b, c);
            double num3 = c[0][0] - m_mXcoefficientBig[index3];
            if (num3 < 0.0)
                flag = false;
            if (index3 == 0) {
                index1 = index3;
                num2 = num3;
            } else if (num3 < num2) {
                num2 = num3;
                index1 = index3;
            }
        }
        if (flag)
            return;
        if (m_nDieDai > this.m_nConstraint + m_nRenGongVariable + m_nSongChiVariable) {
            m_nResultType = 2;
        } else {
            double num3 = 0.0;
            for (int index3 = 0; index3 < this.m_nConstraint; ++index3) {
                double num4;
                try {
                    num4 = this.m_mB[index3] / m_mCcoefficientBig[index3][index1];
                    if (m_mCcoefficientBig[index3][index1] < 0.0)
                        num4 = -1.0;
                } catch (Exception e) {
                    if (index3 == 0) {
                        index2 = 0;
                        continue;
                    }
                    continue;
                }
                if (index3 == 0) {
                    index2 = 0;
                    num3 = num4 >= 0.0 ? num4 : m_dM;
                } else if (num4 >= 0.0 && num4 < num3) {
                    num3 = num4;
                    index2 = index3;
                }
            }
            if (m_htGJColumnAndZSRow.containsValue(index2)) {
                int key = -1;

                for (int i = 0; i < m_htGJColumnAndZSRow.size(); i++) {
                    if (m_htGJColumnAndZSRow.get(i) == index2) {
                        key = i;
                    }
                }
                m_htGJColumnAndZSRow.remove(key);
            }
            m_htGJColumnAndZSRow.put(index1, index2);
            m_mCTarget[0][index2] = m_mXcoefficientBig[index1];
            this.m_mB[index2] = this.m_mB[index2] / m_mCcoefficientBig[index2][index1];
            for (int index3 = 0; index3 < this.m_nConstraint; ++index3) {
                if (index3 != index2)
                    this.m_mB[index3] = this.m_mB[index3] - m_mCcoefficientBig[index3][index1] * this.m_mB[index2];
            }
            double[] numArray = new double[this.m_nVariable + m_nSongChiVariable + m_nRenGongVariable];
            for (int index3 = 0; index3 < this.m_nVariable + m_nSongChiVariable + m_nRenGongVariable; ++index3)
                numArray[index3] = m_mCcoefficientBig[index2][index3];
            double num5 = m_mCcoefficientBig[index2][index1];
            double num6 = 0.0;
            for (int index3 = 0; index3 < this.m_nConstraint; ++index3) {
                for (int index4 = 0; index4 < this.m_nVariable + m_nSongChiVariable + m_nRenGongVariable; ++index4) {
                    if (index3 == index2) {
                        m_mCcoefficientBig[index3][index4] = m_mCcoefficientBig[index3][index4] / num5;
                    } else {
                        if (index4 == 0)
                            num6 = m_mCcoefficientBig[index3][index1];
                        m_mCcoefficientBig[index3][index4] = m_mCcoefficientBig[index3][index4] - num6 * (numArray[index4] / num5);
                    }
                }
            }
            ++m_nDieDai;
            DoCalculate();
        }
    }

    private boolean MatrixMultiply(double[][] a, double[][] b, double[][] c) {
        if (a[0].length != b.length || a.length != c.length || b[0].length != c[0].length)
            return false;
        for (int index1 = 0; index1 < a.length; ++index1) {
            for (int index2 = 0; index2 < b[0].length; ++index2) {
                c[index1][index2] = 0.0;
                for (int index3 = 0; index3 < b.length; ++index3)
                    c[index1][index2] += a[index1][index3] * b[index3][index2];
            }
        }
        return true;
    }

    private void CreateFinalBestValueInfo() {
        m_mXBest = new double[this.m_pListVariableItem.size()];
        String empty1 = null;
        String empty2 = null;
        String empty3 = null;
        double num1 = 0.0;
        String empty4 = null;
        for (int index = 0; index < this.m_pListVariableItem.size(); ++index) {
            double num2;
            double num3;
            if (m_htGJColumnAndZSRow.containsKey(index)) {
                int int32 = (int) m_htGJColumnAndZSRow.get(index);
                try {
                    num2 = this.m_mB[int32];
                } catch (Exception e) {
                    num2 = 0.0;
                }
                try {
                    num3 = !this.m_sType.equals("极小值") ? this.m_mB[int32] * m_mCTarget[0][int32] : this.m_mB[int32] * m_mCTarget[0][int32] * -1.0;
                } catch (Exception e) {
                    num3 = 0.0;
                }
            } else {
                num2 = 0.0;
                num3 = 0.0;
            }
            m_mXBest[index] = num2;
            num1 += num3;
        }
        m_dZBest = num1;
    }

}
