/*
	Name: 
	Student Number: 
*/

import java.util.Scanner;

class Product {
	// Fill your code here
	private String code, factory, model;
    private float price;
    private int stock;
	
	Product(String code, String factory, String model) {
        this.code = code;
        this.factory = factory;
        this.model = model;
		this.price = 0;
        this.stock = 0;
    }

    Product(String code, String factory, String model, float price, int stock) {
        this.code = code;
        this.factory = factory;
        this.model = model;
        this.price = price;
        this.stock = stock;
    }
	
	public void setPrice(float price) {
        this.price = price;
    }
	public void setStock(int stock) {
        this.stock = stock;
    }
    public float getPrice() {
        return price;
	}
	
	public int supplyProduct(int pieces) {
        this.stock += pieces;
        return this.stock;
    }
	
	public int sellProduct(int pieces) {
        if (pieces > stock) {
            return -1;
        } else {
            stock -= pieces;
            return stock;
        }
    }

	public String toString() {
        String s = "";

        s += "Product\n";
        s += "-------------------------\n";
        s += "code:\t\t" + code + "\n";
        s += "factory:\t" + factory + "\n";
        s += "model:\t\t" + model + "\n";
        s += "price:\t\t" + price + "\n";
        s += "stock:\t\t" + stock + "\n";
        s += "-------------------------";

        return s;
    }
}

class App7 {
	
	public static void main(String args[]){ 
		// Fill your code here
		Scanner in = new Scanner (System.in);
		Product product001 = new Product("p001", "Mobile Phones", "A21");
		System.out.println(product001.toString());
		
		System.out.print("price = ");
        float price = in.nextFloat();
        product001.setPrice(price);
		
		System.out.print("stock = ");
        int stock = in.nextInt();
        product001.setStock(stock);
		
		System.out.println(product001.toString());
		
		System.out.print("sale = ");
        int amount = in.nextInt();
        stock = product001.sellProduct(amount);

        if (stock == -1) {
            System.out.println("Not enough stock to complete sale");
        } else {
            System.out.println("New stock = " + stock);
            System.out.println("Amount to pay = " + amount * product001.getPrice());
        }
		
		System.out.println(product001.toString());
		
		System.out.print("supply = ");
        amount = in.nextInt();
        stock = product001.supplyProduct(amount);
        System.out.println("New stock = " + stock);
	
		Product product002 = new Product("p002", "Mobile Phones", "B21", 191.0f, 50);
		System.out.println(product002.toString());
		
	}	
}