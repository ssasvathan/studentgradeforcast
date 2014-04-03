package cen.comp313.student_portal;


public class DataTypeConverter {
	
	public double StringToDoubeConvertor(String s) throws Exception{
		double convertedDouble;
		if(s == null){
			throw new Exception("Invalid input has been entered.");
		}
		
		try{
			convertedDouble=Double.parseDouble(s);
		}catch (Exception e) {
			throw new Exception("Invalid input has been entered.");
		}
		return convertedDouble;
	}

}
