package cn.dmego.odsp.algorithms.service.impl;

import cn.dmego.odsp.algorithms.service.DEAService;
import cn.dmego.odsp.algorithms.vo.DEAVo;
import cn.dmego.odsp.common.JsonResult;
import org.opensourcedea.dea.DEAProblem;
import org.opensourcedea.dea.ModelType;
import org.opensourcedea.dea.VariableOrientation;
import org.opensourcedea.dea.VariableType;
import org.opensourcedea.exception.*;
import org.springframework.stereotype.Service;

/**
 * class_name: DEAServiceImpl
 * package: cn.dmego.odsp.algorithms.service.impl
 * describe: 数据包络分析 Service 实现类
 * creat_user: Dmego
 * creat_date: 2018/12/29
 * creat_time: 22:46
 **/
@Service
public class DEAServiceImpl implements DEAService {


    /**
     * 解决方案下效率评价DEA求解
     */
    @Override
    public JsonResult calFromSo(DEAVo deaVo) {

        String[] input = deaVo.getInput();
        String[] output = deaVo.getOutput();

        String[] funs = deaVo.getFunctions();
        String[] dmuNames = deaVo.getFunctions(); //决策单元数组
        String[] variableNames = deaVo.getVariableNames(); //所有指标数组
        double[][] matrix = deaVo.getMatrix(); //所有数据数组

        //1.确定决策单元个数和指标个数
        int nbDMUs = dmuNames.length; //决策单元数
        int nbVar = input.length + output.length; //投入与产出总指标数

        //2.初始化 VariableOrientation 数据
        VariableOrientation[] variableOrientations = new VariableOrientation[nbVar];
        for (int i = 0; i < nbVar; i++) {
            if(i < input.length){//前 input.length 项为投入
                variableOrientations[i] = VariableOrientation.INPUT;
            }else {//后面的为产出
                variableOrientations[i] = VariableOrientation.OUTPUT;
            }
        }

        //3.初始化 VariableTypes 数据
        VariableType[] variableTypes = new VariableType[nbVar];
        for (int i = 0; i < nbVar; i++) {
            variableTypes[i] = VariableType.STANDARD;
        }

        //4.构建DEA问题求解对象
        DEAProblem deaProblem = new DEAProblem(nbDMUs,nbVar);
        deaProblem.setDMUNames(dmuNames);
        deaProblem.setVariableNames(variableNames);
        deaProblem.setVariableOrientations(variableOrientations);
        deaProblem.setVariableTypes(variableTypes);
        deaProblem.setDataMatrix(matrix);

        //5.根据不同的方法进行计算,并将结果
        //            区域  综合效率  技术效率 规模效率  GRS效率 ..... DEA有效   排序
        //返回结果表头： AREA   OE      TE      SE      GRS   ....  VALID    RANK

        try {
            for (int i = 0; i < funs.length; i++) {
                if(funs[i].equals("CCR")){ //如果是CCR模型
                    deaProblem.setModelType(ModelType.CCR_I);
                    deaProblem.solve();

                }else if(funs[i].equals("BCC")){
                    deaProblem.setModelType(ModelType.BCC_I);
                    deaProblem.solve();
                }else if(funs[i].equals("DRS")){
                    deaProblem.setModelType(ModelType.DRS_I);
                    deaProblem.solve();
                } else if(funs[i].equals("GRS")){
                    deaProblem.setModelType(ModelType.GRS_I);
                    deaProblem.solve();
                }else if(funs[i].equals("IRS")){
                    deaProblem.setModelType(ModelType.IRS_I);
                    deaProblem.solve();
                }else if(funs[i].equals("SBM")){
                    deaProblem.setModelType(ModelType.SBM);
                    deaProblem.solve();
                }
            }





        } catch (Exception e) {
            e.printStackTrace();
        }










        return null;
    }
}
