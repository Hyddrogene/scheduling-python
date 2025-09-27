package cb_ctt.features.service.impl;

import org.logicng.formulas.Literal;

import java.util.Set;

public class SATClause {
		private Set<Literal> literals;
		private int weight;
		private boolean hard;

	SATClause(Set<Literal> literals, int weight, boolean hard) {
			this.literals = literals;
			this.weight = weight;
			this.hard = hard;
	}

	public Set<Literal> getLiterals() {
		return literals;
	}

	public int getWeight() {
		return weight;
	}

	public boolean isHard() {
		return hard;
	}
}
