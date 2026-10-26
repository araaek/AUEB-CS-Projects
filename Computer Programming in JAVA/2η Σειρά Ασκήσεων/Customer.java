/*
	Name: 
	Student Number: 
*/

public class Customer {
    private String name;
    private String code;
    private myDate registrationDate;

    public Customer(String name, String code, int day, int month, int year) {
        this.name = name;
        this.code = code;
        this.registrationDate = new myDate(day, month, year);
    }

    public String getName() {
        return name;
    }

    /*
     * public void setName(String name) {
     * this.name = name;
     * }
     */
    public String getCode() {
        return code;
    }

    /*
     * public void setCode(String code) {
     * this.code = code;
     * }
     */
    public myDate getRegistrationDate() {
        return registrationDate;
    }

    /*
     * public void setRegistrationDate(myDate registrationDate) {
     * this.registrationDate = registrationDate;
     * }
     */
    public String toString() {
        return "Name: " + name + ", Code: " + code + ", Registration date: " + registrationDate.toString();
    }

}
