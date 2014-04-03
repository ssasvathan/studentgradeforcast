package cen.comp313.student_portal;

public class Courses{
	private int courseID;
	private String name;

    public Courses() {
    }
    
	public int getCrsid() {
		return this.courseID;
	}

	public void setCrsid(int crsid) {
		this.courseID = crsid;
	}


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}