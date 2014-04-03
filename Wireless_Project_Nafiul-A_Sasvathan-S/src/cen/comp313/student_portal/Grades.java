package cen.comp313.student_portal;

import java.text.DecimalFormat;
import java.util.List;

public class Grades {
	
	private double weightedMark=0;
	private double currentMax=0;
	private double pendingMarks=0;
	private List<Assesment> as;
	
	private double gradeToAPlus; //90-100
	private double gradeToA; //80-89
	private double gradeToBPlus; //75-79
	private double gradeToB; //70-74
	private double gradeToCPlus; //65-69
	private double gradeToC; //60-64
	private double gradeToDPlus; //55-59
	private double gradeToD; //50-54
	
	public double getCurrentMax() {
		return currentMax;
	}
	
	public double getPendingMarks() {
		return pendingMarks;
	}

	public double getWeightedMark(){
		return weightedMark;
	}

	public double getGradeToAPlus() {
		return gradeToAPlus;
	}

	public double getGradeToA() {
		return gradeToA;
	}

	public double getGradeToBPlus() {
		return gradeToBPlus;
	}

	public double getGradeToB() {
		return gradeToB;
	}

	public double getGradeToCPlus() {
		return gradeToCPlus;
	}

	public double getGradeToC() {
		return gradeToC;
	}

	public double getGradeToDPlus() {
		return gradeToDPlus;
	}

	public double getGradeToD() {
		return gradeToD;
	}

	public Grades(){
	}
	
	public Grades(List<Assesment> as){
		this.as=as;
		for (Assesment a:as ){
			Double pM=Double.parseDouble(a.getAchievedgrade());
			Double wt=Double.parseDouble(a.getWeight());
			currentMax+=wt;
			weightedMark+=(pM/100)*wt;
			
		}
		currentMax = RoundTo2Decimals(currentMax);
		weightedMark = RoundTo2Decimals(weightedMark);
		pendingMarks = 100-currentMax;
	}
	
	public double RoundTo2Decimals(double val) {
        DecimalFormat df2 = new DecimalFormat("###.##");
        return Double.valueOf(df2.format(val));
	}
	
	
	public void calculateGrades(){
		double maxPossible = weightedMark+pendingMarks; //calculate highest possible mark
		
		if(maxPossible >= 90){
			gradeToAPlus = 90-weightedMark;
				if (weightedMark<80){
					gradeToA = 80-weightedMark;
				} if(weightedMark<75){
					gradeToBPlus = 75-weightedMark;
				} if(weightedMark<70){
					gradeToB = 70-weightedMark;
				} if (weightedMark<65){
					gradeToCPlus = 65-weightedMark;
				} if (weightedMark<60){
					gradeToC = 60-weightedMark;
				} if (weightedMark<55){
					gradeToDPlus = 55-weightedMark;
				} if (weightedMark<50){
					gradeToD = 50-weightedMark;
				}
		} else if(maxPossible >=80){
			gradeToA = 80-weightedMark;
			if (weightedMark<75){
				gradeToBPlus= 75-weightedMark;
			} if(weightedMark<70){
			gradeToB = 70-weightedMark;
			} if (weightedMark<65){
			gradeToCPlus = 65-weightedMark;
			} if (weightedMark<60){
				gradeToC = 60-weightedMark;
			} if (weightedMark<55){
				gradeToDPlus = 55-weightedMark;
			} if (weightedMark<50){
				gradeToD = 50-weightedMark;
			}
		} else if (maxPossible >=75){
			gradeToBPlus = 75-weightedMark;
			if (weightedMark<70){
				gradeToB = 70-weightedMark;
			} if (weightedMark<65){
			gradeToCPlus = 65-weightedMark;
			} if (weightedMark<60){
				gradeToC = 60-weightedMark;
			} if (weightedMark<55){
				gradeToDPlus = 55-weightedMark;
			} if (weightedMark<50){
				gradeToD = 50-weightedMark;
			}
		}else if (maxPossible >=70){
			gradeToB = 70-weightedMark;
			if (weightedMark<65){
				gradeToCPlus = 65-weightedMark;
			} if (weightedMark<60){
				gradeToC = 60-weightedMark;
			} if (weightedMark<55){
				gradeToDPlus = 55-weightedMark;
			} if (weightedMark<50){
				gradeToD = 50-weightedMark;
			}
		}else if (maxPossible >=65){
			gradeToCPlus = 65-weightedMark;
			if (weightedMark<60){
				gradeToC = 60-weightedMark;
			} if (weightedMark<55){
				gradeToDPlus = 55-weightedMark;
			} if (weightedMark<50){
				gradeToD = 50-weightedMark;
			}
		}else if (maxPossible >=60){
			gradeToC = 60-weightedMark;
			if (weightedMark<55){
				gradeToDPlus = 55-weightedMark;
			} if (weightedMark<50){
				gradeToD = 50-weightedMark;
			}
		}else if (maxPossible >=55){
			gradeToDPlus = 55-weightedMark;
			if (weightedMark<50){
				gradeToD = 50-weightedMark;
			}
		}else if (maxPossible >=50){
			gradeToD = 50-weightedMark;
		}
		
		gradeToAPlus = RoundTo2Decimals(gradeToAPlus);
		gradeToA = RoundTo2Decimals(gradeToA);
		gradeToBPlus = RoundTo2Decimals(gradeToBPlus);
		gradeToB = RoundTo2Decimals(gradeToB);
		gradeToCPlus = RoundTo2Decimals(gradeToCPlus);
		gradeToC = RoundTo2Decimals(gradeToC);
		gradeToDPlus = RoundTo2Decimals(gradeToDPlus);
		gradeToD = RoundTo2Decimals(gradeToD);
	}

	public void setAs(List<Assesment> as) {
		this.as = as;
	}

	public List<Assesment> getAs() {
		return as;
	}

}
