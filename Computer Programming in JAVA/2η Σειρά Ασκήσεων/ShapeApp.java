/*
	Name: 
	Student Number: 
*/

class Shape {
	void draw() {}

	void erase() {}
}

class Circle extends Shape {
	void draw() {
		System.out.println("Circle.draw()");
	}

	void erase() {
		System.out.println("Circle.erase()");
	}
}

class Square extends Shape {
	void draw() {
		System.out.println("Square.draw()");
	}

	void erase() {
		System.out.println("Square.erase()");
	}
}

class Triangle extends Shape {
	void draw() {
		System.out.println("Triangle.draw()");
	}

	void erase() {
		System.out.println("Triangle.erase()");
	}
}

class ShapeApp {
	static Shape randShape() {
		switch ((int) (Math.random() * 3)) {
			default:
			case 0:
				return new Circle();
			case 1:
				return new Square();
			case 2:
				return new Triangle();
		}
	}

	public static void main(String[] args) {
		Shape[] shapes = new Shape[10]; 			// 1
		for (int i = 0; i < shapes.length; i++) { 	// 2
			shapes[i] = randShape();
		}
		for (Shape i: shapes) { 	// 3
			i.draw();
		}
	}// main
}

