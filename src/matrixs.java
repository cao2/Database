import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class matrixs {
	matrix matrix_gene;

	matrix matrix_mir;
	public matrixs(String gene, String mri){
		try{
			BufferedReader br = new BufferedReader(new FileReader(gene));
			matrix_gene=makeMatrix(br);
			//System.out.println(matrix_gene);
			br = new BufferedReader(new FileReader(mri));
			matrix_mir=makeMatrix(br);
			System.out.println(matrix_mir);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public matrix makeMatrix(BufferedReader br) throws IOException{
		matrix ret=new matrix();
		Map<String, List<Float>> rst=new LinkedHashMap<String, List<Float>>();
		LinkedList<String> colum=new LinkedList<String> ();
		colum.add("patient#");
		String line;
		int firstline=0;
		while((line=br.readLine())!=null){
			String[] content=line.split(",");
			if(content!=null){
				for(int i=0;i<content.length;i++){
					if(firstline==0){
						if(i!=0)
							colum.add(content[i]);
						}
					else{
						List<Float> tmpcolum=rst.get(colum.get(i));
						if(tmpcolum==null)
							tmpcolum=new LinkedList<Float> ();
						tmpcolum.add(Float.parseFloat(content[i]));
						rst.put(colum.get(i), tmpcolum);
					}//if firstlilne==0
				}//for int i=0
			}//if content!=null
			firstline++;
		}//while loop
		ret.setCont(rst);
		List<Float> mean=new LinkedList<Float> ();
		List<Float> cov=new LinkedList<Float> ();
		for(String x:rst.keySet()){
			float mi=mean(rst.get(x));
			mean.add(mi);
			cov.add(cov(rst.get(x),mi));
		}
		ret.setMean(mean);
		ret.setCov(cov);
		return ret;
	}
	public float mean(List<Float> lis){
		float rst=0;
		for(Float x:lis)
			rst+=x;
		rst=rst/lis.size();
		return rst;
	}
	public float cov(List<Float> lis, float mean){
		float rst=0;
		for(Float x:lis)
			rst+=(x-mean)*(x-mean);
		rst=(float) Math.sqrt(rst);
		return rst;
	}
	public float coeffient(List<Float> gene_co, List<Float> mir_co, float g_mean, float g_cov, float m_mean, float m_cov){
		float rst=0;
		for(int i=0;i<gene_co.size();i++){
			rst+=(gene_co.get(i)-g_mean)*(mir_co.get(i)-m_mean);
		}
		rst=rst/(g_cov*m_cov);
		return rst;
	}
	
}