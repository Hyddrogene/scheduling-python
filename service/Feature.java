package cb_ctt.features.service;

public class Feature {
	private String name;
	private double value;

	public Feature(String name, double value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public double getValue() {
		return value;
	}

	public Feature addPrefix(String prefix) {
		return new Feature(prefix+"."+this.name, this.value);
	}

	@Override
	public String toString() {
		return "Feature{" +
			   "name='" + name + '\'' +
			   ", value=" + value +
			   '}';
	}
}
