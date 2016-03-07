package graphics.nim.volterra.util;

import java.util.HashMap;
import java.util.Map;

public class Probability {
	public HashMap<Object, Double> map = new HashMap<Object, Double>();
	
	public double probSum = 0;
	
	public void addItem(Object item, double probability) {
		map.put(item, probability);
		probSum += probability;
	}
	
	public Object random() {
		double rand = Math.random();
		
		double ratio = 1 / probSum;
		double tempProb = 0;
		
		for (Map.Entry<Object, Double> entry : map.entrySet()) {
			Object key = entry.getKey();
			double value = entry.getValue();
			
			tempProb += value;
			if (rand / ratio <= tempProb) {
				return key;
			}
		}
		
		return null;
	}
}
