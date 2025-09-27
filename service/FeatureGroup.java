package cb_ctt.features.service;

import java.util.Collection;

public class FeatureGroup {
	private FeatureFactory      factory;
	private Collection<Feature> features;

	public FeatureGroup(FeatureFactory factory, Collection<Feature> features) {
		this.factory = factory;
		this.features = features;
	}

	public String getName()                  { return factory.name(); }

	public Collection<Feature> getFeatures() { return features; }

	@Override
	public String toString() {
		return "FeatureGroup{" +
			   "name='" + getName() + '\'' +
			   ", features=" + features +
			   '}';
	}
}
