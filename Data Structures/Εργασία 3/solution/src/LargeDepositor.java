class LargeDepositor {
    private int AFM; // ΑΦΜ (ελληνικός)
    private String firstName; // Όνομα
    private String lastName; // Επώνυμο
    private double savings; // Συνολικό ύψος γνωστών καταθέσεων σε άλλες χώρες
    private double taxedIncome; // Συνολικό δηλωμένο εισόδημα στην Ελλάδα την τελευταία 3ετία

    // Μέθοδος για πρόσβαση στο κλειδί
    int key() {
        return AFM;
    }

    @Override
    public String toString() {
        return "Large Depositor{" +
                "AFM=" + String.format("%09d", AFM) +
                ", first name=" + firstName +
                ", last name=" + lastName +
                ", savings=" + savings +
                ", taxed income=" + taxedIncome +
                '}';
    }

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public double getSavings() {
        return savings;
    }

    public double getTaxedIncome() {
        return taxedIncome;
    }

    // Setters
    public void setAFM(int AFM) {
        this.AFM = AFM;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSavings(double savings) {
        this.savings = savings;
    }

    public void setTaxedIncome(double taxedIncome) {
        this.taxedIncome = taxedIncome;
    }
}
