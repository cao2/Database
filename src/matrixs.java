import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Arrays;

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
			System.out.println(matrix_mir.getCont());
			//create node tree for miRNA
			//define bucket size..let's make it 10
			Map<String, List<Float>> ctt=matrix_mir.getCont();
			Node root=build_tree(0,ctt);

		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public Node build_tree(int level, Map<String, List<Float>> ctt){
		Node rst;
		if(ctt.keySet().size()<10){
			float[] tmp=new float[ctt.size()];
			int key_name=ctt.size()%level;
			int j=0;
			for(String x:ctt.keySet()){
				tmp[j]=ctt.get(x).get(key_name);
				j++;
			}
			Arrays.sort(tmp);
			float key_value=tmp[ctt.size()/2];
			Map<String, List<Float>> ct_left=new LinkedHashMap<String, List<Float>> ();
			Map<String, List<Float>> ct_right=new LinkedHashMap<String, List<Float>> ();
			for(String x: ctt.keySet()){
				if(ctt.get(x).get(key_name)<key_value)
					ct_left.put(x, ctt.get(x));
				else
					ct_right.put(x, ctt.get(x));
			}
			rst=new Node(key_name,key_value);
			Node lf=build_tree(level+1,ct_left);
			lf.setParent(rst);
			Node rt=build_tree(level+1,ct_left);
			rt.setParent(rst);
			rst.setLeft_child(lf);
			rst.setRight_child(rt);
		}
		else{
			rst=new Node(0,(float)0);
			rst.setBucket_ct(ctt);
		}
		return rst;
	}
	public matrix makeMatrix(BufferedReader br) throws IOException{
		matrix ret=new matrix();
		Map<String, List<Float>> rst=new LinkedHashMap<String, List<Float>>();
		String line;
		int firstline=1;
		int firstcolum=1;
		while((line=br.readLine())!=null){
			String[] content=line.split(",");
			//System.out.println(line);
			if(content!=null&&firstline==0){
				String gid="";
				List<Float> tmpct=new LinkedList<Float>();
				for(String x:content)
					{
						if(firstcolum==1){
							gid=x;
							firstcolum=0;
						}
						else {
							if(x.equals(""))
								tmpct.add((float) 0.00);
							else
								tmpct.add(Float.parseFloat(x));
							}
				}
				rst.put(gid, tmpct);
			}//if content!=null
			else if(content!=null&&firstline==1){
				firstline=0;
			}
			firstcolum=1;
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