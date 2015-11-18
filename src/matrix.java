import java.util.List;
import java.util.Map;

public class matrix {
	Map<String, List<Float>> content;
	List<Float> mean;
	List<Float> cov;
	public matrix(){
		
	}
	public void setCont(Map<String, List<Float>> x){
		content=x;
	}
	public Map<String, List<Float>> getCont(){
		return content;
	}
	public void setMean(List<Float> x){
		mean=x;
	}
	public List<Float> getMean(){
		return mean;
	}
	public void setCov(List<Float> x){
		cov=x;
	}
	public List<Float> getCov(){
		return cov;
	}
	
}
