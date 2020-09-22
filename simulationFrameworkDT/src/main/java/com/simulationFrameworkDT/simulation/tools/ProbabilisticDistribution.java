package com.simulationFrameworkDT.simulation.tools;

import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.random.EmpiricalDistribution;

import java.security.InvalidParameterException;
import java.util.Hashtable;

public class ProbabilisticDistribution {

    private AbstractRealDistribution dist;

//    FDistribution(double numeratorDegreesOfFreedom, double denominatorDegreesOfFreedom),
//    NormalDistribution(double mean, double sd), CauchyDistribution(double median, double scale),
//    ChiSquaredDistribution(double degreesOfFreedom), BetaDistribution(double alpha, double beta)
//    EnumeratedRealDistribution(double[] singletons, double[] probabilities),
//    TDistribution(double degreesOfFreedom), TriangularDistribution(double a, double c, double b),
//    UniformRealDistribution(double lower, double upper), WeibullDistribution(double alpha, double beta),
//    EmpiricalDistribution(int binCount).
    public ProbabilisticDistribution(String type, Hashtable<String,Object> args) {
        this.dist = typeHandler(type,args);
    }

    public double getNextDistributionValue(){
        return dist.sample();
    }

    public AbstractRealDistribution typeHandler (String type, Hashtable<String,Object>args) throws InvalidParameterException {
        AbstractRealDistribution dist;
        if(type.equals("FDistribution")){
            double numeratorDegreesOfFreedom = (Double)args.get("numeratorDegreesOfFreedom");
            double denominatorDegreesOfFreedom = (Double)args.get("denominatorDegreesOfFreedom");
            dist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        } else if(type.equals("NormalDistribution")){
            double mean = (Double)args.get("mean");
            double sd = (Double)args.get("sd");
            dist = new NormalDistribution(mean, sd);
        } else if(type.equals("CauchyDistribution")){
            double median = (Double)args.get("median");
            double scale = (Double)args.get("scale");
            dist = new CauchyDistribution(median, scale);
        } else if(type.equals("ChiSquaredDistribution")){
            double degreesOfFreedom = (Double)args.get("degreesOfFreedom");
            dist = new ChiSquaredDistribution(degreesOfFreedom);
        } else if(type.equals("BetaDistribution")){
            double alpha = (Double)args.get("alpha");
            double beta = (Double)args.get("beta");
            dist = new BetaDistribution(alpha, beta);
        } else if(type.equals("EnumeratedRealDistribution")){
            double[] singletons = (double[])args.get("singletons");
            double[] probabilities = (double[])args.get("probabilities");
            dist = new EnumeratedRealDistribution(singletons, probabilities);
        } else if(type.equals("TDistribution")){
            double degreesOfFreedom = (Double)args.get("degreesOfFreedom");
            dist = new TDistribution(degreesOfFreedom);
        } else if(type.equals("TriangularDistribution")){
            double a = (Double)args.get("a");
            double b = (Double)args.get("b");
            double c = (Double)args.get("c");
            dist = new TriangularDistribution(a, c, b);
        } else if(type.equals("UniformRealDistribution")){
            double lower = (Double)args.get("lower");
            double upper = (Double)args.get("upper");
            dist = new UniformRealDistribution(lower, upper);
        } else if(type.equals("WeibullDistribution")){
            double alpha = (Double)args.get("alpha");
            double beta = (Double)args.get("beta");
            dist = new WeibullDistribution(alpha, beta);
        } else if(type.equals("EmpiricalDistribution")){
            int binCount = (Integer)args.get("binCount");
            dist = new EmpiricalDistribution(binCount);
        } else {
            throw new InvalidParameterException("Invalid type, cannot create a distribution " +
                    "object with the given type");
        }
        return dist;
    }

}
