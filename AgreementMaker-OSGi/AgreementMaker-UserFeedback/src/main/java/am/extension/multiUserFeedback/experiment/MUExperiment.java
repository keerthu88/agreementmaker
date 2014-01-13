/*
 * 	Francesco Loprete December 2013
 */
package am.extension.multiUserFeedback.experiment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import am.app.Core;
import am.app.mappingEngine.AbstractMatcher.alignType;
import am.app.mappingEngine.Alignment;
import am.app.mappingEngine.Mapping;
import am.app.mappingEngine.similarityMatrix.SimilarityMatrix;
import am.app.mappingEngine.similarityMatrix.SparseMatrix;
import am.extension.userfeedback.UserFeedback.Validation;
import am.extension.userfeedback.experiments.UFLExperiment;
import am.extension.userfeedback.logic.IndependentSequentialLogicPaper;
import am.extension.userfeedback.logic.UFLControlLogic;

public class MUExperiment extends UFLExperiment {


	
public BufferedWriter logFile;
private Alignment<Mapping> MLAlignment;
private Object[][] trainingSet_classes;
private Object[][] trainingSet_property;

	private SimilarityMatrix uflClassMatrix;
	private SimilarityMatrix uflPropertyMatrix;

private SparseMatrix uflStorageClass_pos;
private SparseMatrix uflStorageClass_neg;
private SparseMatrix uflStorageProperty_pos;
private SparseMatrix uflStorageProperty_neg;
public List<Mapping> disRanked;
public List<Mapping> uncertainRanking;
public List<Mapping> almostRanking;
public Mapping selectedMapping;
public int feedbackCount;
public String feedback;
public List<Mapping> alreadyEvaluated=new ArrayList<Mapping>();
public List<Mapping> conflictualClass;
public List<Mapping> conflictualProp;
public HashMap<String, List<Mapping>> usersMappings=new HashMap<String, List<Mapping>>();
public HashMap<String, Integer> usersGroup=new HashMap<String, Integer>();
public HashMap<String, SimilarityMatrix> usersClass=new HashMap<String, SimilarityMatrix>();
public HashMap<String, SimilarityMatrix> usersProp=new HashMap<String, SimilarityMatrix>();

public class csData{
	public int[] count={0,0,0};
	public int total=0;
}
public csData data=new csData();


private alignCardinality alignCardinalityType=alignCardinality.cn_m;

public MUExperiment ()
{
	super();
	
	try {
		final String root = Core.getInstance().getRoot();
		File logFileFile = new File(root + "settings/tmp/uflLog." + System.currentTimeMillis() + ".txt");
		FileWriter file = new FileWriter(logFileFile);
		logFile = new BufferedWriter(file);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		am.Utility.displayErrorPane("Permission error: Log file can not be created", "Error");
	}
}

public alignCardinality getAlignCardinalityType() {
	return alignCardinalityType;
}


public void setAlignCardinalityType(alignCardinality alignCardinalityType) {
	this.alignCardinalityType = alignCardinalityType;
}

	//forbidden position keeper
	private SparseMatrix forbiddenPositionsClasses;
	private SparseMatrix forbiddenPositionsProperties;

	public SparseMatrix getForbiddenPositions(alignType type) {
		switch(type) {
		case aligningClasses:
			return forbiddenPositionsClasses;
		case aligningProperties:
			return forbiddenPositionsProperties;
		default:
			throw new RuntimeException(type + " is not accepted");
		}
	}
	
	public void setForbiddenPositions(alignType type, SparseMatrix matrix) {
		switch(type) {
		case aligningClasses:
			this.forbiddenPositionsClasses = matrix; return;
		case aligningProperties:
			this.forbiddenPositionsProperties = matrix;	return;
		default:
			throw new RuntimeException(type + " is not accepted");
		}
	}
	
	public SparseMatrix getFeedbackMatrix(alignType type, Validation validation) {
		switch(type) {
		case aligningClasses:
			switch(validation) {
			case CORRECT:
				return uflStorageClass_pos;
			case INCORRECT:
				return uflStorageClass_neg;
			default:
				throw new RuntimeException(validation + " is not accepted");
			}
		case aligningProperties:
			switch(validation) {
			case CORRECT:
				return uflStorageProperty_pos;
			case INCORRECT:
				return uflStorageProperty_neg;
			default:
				throw new RuntimeException(validation + " is not accepted");
			}
		default:
			throw new RuntimeException(type + " is not accepted");
		}
	}


public void setUflStorageClass_neg(SparseMatrix uflStorageClass_neg) {
	this.uflStorageClass_neg = uflStorageClass_neg;
}

public void setUflStorageProperty_neg(SparseMatrix uflStorageProperty_neg) {
	this.uflStorageProperty_neg = uflStorageProperty_neg;
}

public void setUflStorageClassPos(SparseMatrix uflStorageClass) {
	this.uflStorageClass_pos = uflStorageClass;
}

public void setUflStoragePropertyPos(SparseMatrix uflStorageProperty) {
	this.uflStorageProperty_pos = uflStorageProperty;
}

public Object[][] getTrainingSet_classes() {
	return trainingSet_classes;
}


public void setTrainingSet_classes(Object[][] trainingSet_classes) {
	this.trainingSet_classes = trainingSet_classes;
}


public Object[][] getTrainingSet_property() {
	return trainingSet_property;
}


public void setTrainingSet_property(Object[][] trainingSet_property) {
	this.trainingSet_property = trainingSet_property;
}

	public SimilarityMatrix getComputedUFLMatrix(alignType type) {
		switch(type) {
		case aligningClasses:
			return uflClassMatrix;
		case aligningProperties:
			return uflPropertyMatrix;
		default:
			throw new RuntimeException(type + " is not accepted");
		}
	}

	public void setComputedUFLMatrix(alignType type, SimilarityMatrix matrix) {
		switch(type) {
		case aligningClasses:
			this.uflClassMatrix = matrix; break;
		case aligningProperties:
			this.uflPropertyMatrix = matrix; break;
		default:
			throw new RuntimeException(type + " is not accepted");
		}
	}


public Alignment<Mapping> getMLAlignment() {
	return MLAlignment;
}


public void setMLAlignment(Alignment<Mapping> mLAlignment) {
	MLAlignment = mLAlignment;
}

	@Override
	public boolean experimentHasCompleted() {
		if( userFeedback != null && userFeedback.getUserFeedback() == Validation.END_EXPERIMENT ) return true;  // we're done when the user says so
		return false;
	}

	@Override
	public void newIteration() {
		super.newIteration();
		// TODO: Save all the objects that we used in the previous iteration.
	}

	@Override
	public Alignment<Mapping> getFinalAlignment() {
		//return initialMatcher.getAlignment();
		return MLAlignment;
	}

	@Override
	public void info(String line) {
		if( logFile != null )
			try {
				logFile.write(line + "\n");
				logFile.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@Override
	public UFLControlLogic getControlLogic() {
		return new IndependentSequentialLogicPaper();
		//return new IndependentSequentialLogic();
	}
	
	@Override
	public String getDescription() {
		return "Work in progress";
	}
	
	
	public enum alignCardinality implements Serializable {
		c1_1("oneOne"),
		cn_1("nOne"),
		c1_m("OneM"),
		cn_m("nM"),
		unknown("UNKNOWN");

		private final String value;  

		alignCardinality(String value) {  
			this.value = value;  
		}  

		public static alignCardinality fromValue(String value) {  
			if (value != null) {  
				for (alignCardinality en : values()) {  
					if (en.value.equals(value)) {  
						return en;  
					}  
				}  
			}  

			// you may return a default value  
			return getDefault();  
			// or throw an exception  
			// throw new IllegalArgumentException("Invalid color: " + value);  
		}  

		public String toValue() {  
			return value;  
		}  

		public static alignCardinality getDefault() {  
			return unknown;  
		} 

		private Object readResolve () throws java.io.ObjectStreamException
		{
			if( value == c1_1.toValue() ) return c1_1;
			if( value == cn_1.toValue() ) return cn_1;
			if( value == c1_m.toValue() ) return c1_m;
			if( value == cn_m.toValue() ) return cn_m;
			return unknown;
		}


	}
	
	public void login(String id) {
		usersMappings.put(id, new ArrayList<Mapping>());
		usersGroup.put(id, getGroup());
	}

	
	private int getGroup()
	{
		int size=usersGroup.size();
		return size%3;
	}
}


