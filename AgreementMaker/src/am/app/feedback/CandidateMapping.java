package am.app.feedback;

import am.app.mappingEngine.Alignment;

/**
 * This class extends the Alignment class in order to add more information to be used
 * in candidate selection
 * @author cosmin
 *
 */

public class CandidateMapping extends Alignment {

	double relevance = 0.00;
	
	public CandidateMapping(double s) {
		super(s);
		// TODO Auto-generated constructor stub
	}
	
	public CandidateMapping( Alignment a, double relev ) {
		super(a.getEntity1(), a.getEntity2(), a.getSimilarity(), a.getRelation() );
		relevance = relev;
	}

}