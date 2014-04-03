package cen.comp313.student_portal;


public class Assesment {
	private int assesmentid;
	private String achievedgrade;
	private String name;
	private String weight;

    public Assesment() {
    }


	public int getAssesmentid() {
		return this.assesmentid;
	}

	public void setAssesmentid(int assesmentid) {
		this.assesmentid = assesmentid;
	}


	public String getAchievedgrade() {
		return this.achievedgrade;
	}

	public void setAchievedgrade(String achievedgrade) {
		this.achievedgrade = achievedgrade;
	}


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getWeight() {
		return this.weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

}