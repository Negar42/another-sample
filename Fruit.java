package tryingagain;

import java.util.ArrayList;
import java.util.Iterator;

public class Fruit {
	public int weight;
	public String name;
	public String type;
	public static ArrayList<Fruit> fruits = new ArrayList<Fruit>();

	public Fruit(int weight, String name, String type) {
		this.weight = weight;
		this.name = name;
		this.type = type;
		fruits.add(this);
	}

	public int getWeight() {
		return weight;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public static void deleteFruitByName(String fruitName) {
		Iterator<Fruit> it = fruits.iterator();
		while (it.hasNext()) {
			Fruit fruit = it.next();
			if (fruit.getName().equals(fruitName)) {
				it.remove();
			}
		}

	}

	class Orange {
		public void printing(String name) {
			System.out.println(Fruit.this.name);
		}
	}
}
