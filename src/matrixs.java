import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class matrixs {
	matrix matrix_gene;
	matrix matrix_mir;
	Node root;
	public Node getroot(){
		return root;
	}
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
			root=build_tree(0,ctt);

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
	public Map<String, List<Float>> search(String ky){
		List<Float> xx=matrix_mir.getCont().get(ky);
 		Map<String, List<Float>> rst=new LinkedHashMap<String, List<Float>> ();
		Node tmp=root;
		while(tmp.getBucket_ct()==null){
			float v=tmp.getKey_value();
			if(xx.get(tmp.getKey_name())<=v)
				tmp=tmp.getLeft_child();
			else
				tmp=tmp.getRight_child();
		}
		//now tmp is the leat node 
		//if size of bigger than 9 then
		int sz=tmp.getBucket_ct().size();
		if(sz>8){
			rst=best(8,tmp.getBucket_ct(),xx);
			}
		else{
			//first , put all in rst
			for(String m: tmp.getBucket_ct().keySet())
				rst.put(m, tmp.getBucket_ct().get(m));
			Map<String, List<Float>> cttt=tmp.getBucket_ct();
			int flag=0;
			int left=0;
			for(String k:cttt.keySet()){
				if(flag==0){
					List<Float> tmplt=cttt.get(k);
					if(tmplt.get(tmp.getParent().getKey_name())<=tmp.getParent().getKey_value())
						left=1;
					flag=1;
				}
			}
			Map<String, List<Float>> leftct;
			if(left==1)
				leftct=best(8-sz,tmp.getParent().getRight_child().getBucket_ct(),xx);
			else
				leftct=best(8-sz,tmp.getParent().getLeft_child().getBucket_ct(),xx);
			
			for(String kyy:leftct.keySet())
				rst.put(kyy, leftct.get(ky));
		}
		return rst;
	}
	public Map<String, List<Float>> best(int x, Map<String, List<Float>> ct,List<Float> co){
		Map<String, List<Float>> rst= new LinkedHashMap<String, List<Float>> ();
		String[] ind=new String[ct.size()];
		float[] dist=new float[ct.size()];
		int i=0;
		for(String ky:ct.keySet()){
			ind[i]=ky;
			i++;
			dist[i]=coeffient(ct.get(ky),co);
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
	public float coeffient(List<Float> gene_co, List<Float> mir_co){
		float g_mean=mean(gene_co);
		float g_cov=cov(gene_co,g_mean);
		float m_mean=mean(mir_co);
		float m_cov=cov(mir_co,m_mean);

		float rst=0;
		for(int i=0;i<gene_co.size();i++){
			rst+=(gene_co.get(i)-g_mean)*(mir_co.get(i)-m_mean);
		}
		rst=rst/(g_cov*m_cov);
		return rst;
	}
	
}